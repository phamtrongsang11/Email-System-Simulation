/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client_Controller;

import Client_View.MainView;
import Client_View.SignInForm;
import Client_View.SignUpForm;
import Client_View.SendMailForm;
import Common.FileTransfer;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import Model.ObjectWrapper;
import Common.Security;
import Model.User;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author phamt
 */
public class ClientController {

    private Socket mySocket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private MainView view;
    private ClientListening myListening;
    private ArrayList<ObjectWrapper> myActiveUI;
    public FileTransfer ft;
//    private String host = "localhost";
    private String ipServer;
    private int port = 1234;

    ExecutorService executor;
    int numThread = 1;
    Security sec = new Security();
    private String keyAES;

    public ClientController(MainView view) {
        
        getIpServer();
        openConnection();
        keyAES = sec.createRandomString(64);

        this.view = view;
        myActiveUI = new ArrayList<ObjectWrapper>();

    }

    public boolean openConnection() {
        try {
            mySocket = new Socket(ipServer, port);
            oos = new ObjectOutputStream(mySocket.getOutputStream());
            ois = new ObjectInputStream(mySocket.getInputStream());

            myListening = new ClientListening(this);

            ft = new FileTransfer(mySocket);

            executor = Executors.newFixedThreadPool(numThread);
            executor.execute(myListening);
        } catch (IOException ex) {
            view.showMessage("Error when connecting to the server!");
            return false;
        }
        return true;
    }

    public boolean sendData(Object obj) {
        try {
            oos.writeObject(this.encryptData(obj));
        } catch (IOException ex) {
            view.showMessage("Error when sending data to the server!");
            return false;
        }
        return true;
    }

    public ArrayList<ObjectWrapper> getActiveUI() {
        return myActiveUI;
    }

    public void getIpServer() {
        try {
            String api = "https://api-generator.retool.com/Ls5Jx1/data/1";
            Document doc = Jsoup.connect(api)
                    .ignoreContentType(true).ignoreHttpErrors(true)
                    .header("Content-Type", "application/json")
                    .method(Connection.Method.GET).execute().parse();
            JSONObject jsonObject = new JSONObject(doc.text());
            ipServer = jsonObject.get("ip").toString();

        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    public void sendKeyAES(byte[] publicKeyRSA) throws IOException {
        String cipherText = sec.encrpytionRSA(keyAES, publicKeyRSA);
        oos.writeObject(new ObjectWrapper(ObjectWrapper.KEY_AES, cipherText));
    }

    public byte[] encryptData(Object data) throws IOException {
        return sec.encryptAES(sec.serialize(data), keyAES);
    }

    public Object decryptData(byte[] data) throws IOException, ClassNotFoundException {
        return sec.deserialize(sec.decryptAES(data, keyAES));
    }

    public byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

    public long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes);
        buffer.flip();//need flip 
        return buffer.getLong();
    }

//    public void sendFile(String path) {
//        try {
////            File file = new File(path);
////            FileInputStream fileInputStream = new FileInputStream(file);
//
//            int bytes = 0;
//
//            DataOutputStream dataOutputStream = new DataOutputStream(mySocket.getOutputStream());
//
//            File file = new File(path);
//            FileInputStream fileInputStream = new FileInputStream(file);
//
//            dataOutputStream.writeLong(file.length());
//
//            byte[] buffer = new byte[4 * 1024];
//            while ((bytes = fileInputStream.read(buffer)) != -1) {
//                
//                dataOutputStream.write(buffer, 0, bytes);
//                dataOutputStream.flush();
//            }
//            
//            fileInputStream.close();
//
//        } catch (IOException e) {
//            System.err.println(e);
//        }
//    }
    public File receiveFile(String folder, String fileName) {
        try {

            File file = new File(folder, fileName);
//            FileOutputStream fileOutputStream = new FileOutputStream(file);
////            in = new DataInputStream(socket.getInputStream());

            DataInputStream dataInputStream = new DataInputStream(mySocket.getInputStream());
            int bytes = 0;
            FileOutputStream fileOutputStream
                    = new FileOutputStream(file);

            long size = dataInputStream.readLong();
            byte[] buffer = new byte[4 * 1024];
            while (size > 0 && (bytes = dataInputStream.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {

                fileOutputStream.write(buffer, 0, bytes);
                size -= bytes;
            }

            System.out.println("File is Received");
            fileOutputStream.close();

        } catch (IOException ex) {
            System.err.println(ex);
        }
        return null;
    }

    public boolean closeConnection() {
        try {

            myActiveUI.clear();
            if (oos != null) {
                oos.close();
            }
            if (ois != null) {
                ois.close();
            }
            if (mySocket != null) {
                mySocket.close();
            }
            executor.shutdownNow();
            view.showMessage("Disconnected from the server!");

        } catch (IOException e) {
            e.printStackTrace();
            view.showMessage("Error when disconnecting from the server!");
            return false;
        }
        return true;
    }

//    class ClientSend implements Runnable {
//
//        ClientController clientCtr;
//
//        public ClientSend(ClientController clientCtr) {
//            super();
//            this.clientCtr = clientCtr;
//        }
//        public void run(){
//            
//        }
//
//    }
    class ClientListening implements Runnable {

        ClientController clientCtr;

        public ClientListening(ClientController clientCtr) {
            this.clientCtr = clientCtr;
        }

        @Override
        public void run() {
            boolean flag = true;
            try {
                while (true) {

                    Object object;
                    if (flag) {
                        object = ois.readObject();
                        flag = false;
                    } else {

                        object = decryptData((byte[]) ois.readObject());
                        System.out.println(object + "\n");
                    }

                    if (object instanceof ObjectWrapper data) {
                        switch (data.getPerformative()) {

                            case ObjectWrapper.GET_DOMAIN_NAME -> {
                                view.setDomainName(data);
                            }

                            case ObjectWrapper.GET_LIST_MAIL -> {
                                view.listMail(data);
                            }

                            case ObjectWrapper.BROARDCAST_INBOX -> {
                                clientCtr.sendData(new ObjectWrapper(ObjectWrapper.GET_TOTAL_MAIL_LIST, (User) data.getData()));                
                                clientCtr.sendData(new ObjectWrapper(ObjectWrapper.INBOX_LIST, (User) data.getData()));                    
                            }

                            case ObjectWrapper.BROARDCAST_SPAM -> {
                                clientCtr.sendData(new ObjectWrapper(ObjectWrapper.SPAM_LIST, (User) data.getData()));
                                clientCtr.sendData(new ObjectWrapper(ObjectWrapper.GET_TOTAL_MAIL_LIST, (User) data.getData()));
                            }

                            case ObjectWrapper.UPDATE_TO_READ_LIST -> {
                                clientCtr.sendData(new ObjectWrapper(ObjectWrapper.GET_TOTAL_MAIL_LIST, data.getData()));                
                                clientCtr.sendData(new ObjectWrapper(ObjectWrapper.INBOX_LIST, (User) data.getData()));                    
                            }

                            case ObjectWrapper.UPDATE_TO_DELETE_LIST -> {
                                clientCtr.sendData(new ObjectWrapper(ObjectWrapper.GET_TOTAL_MAIL_LIST, data.getData()));
                                clientCtr.sendData(new ObjectWrapper(ObjectWrapper.READ_LIST, (User) data.getData()));
                            }

                            case ObjectWrapper.UPDATE_TO_SPAM_LIST -> {
                                clientCtr.sendData(new ObjectWrapper(ObjectWrapper.GET_TOTAL_MAIL_LIST, data.getData()));                                
                                clientCtr.sendData(new ObjectWrapper(ObjectWrapper.READ_LIST, (User) data.getData()));  
                            }

                            case ObjectWrapper.GET_TOTAL_MAIL_LIST ->
                                view.displayTotalMail(data);

                            case ObjectWrapper.SCHEDULE_LIST -> {

                                view.listSchedule(data);
                            }

                            case ObjectWrapper.GET_TO_USER -> {

                                view.displayToUser(data);
                            }

                            case ObjectWrapper.DELETE_SCHEDULE -> {
                                view.deleteSchedule(data);
                            }

                            case ObjectWrapper.DELETE_MAIL -> {
                                view.deleteMail(data);
                            }

                            case ObjectWrapper.PUBLIC_KEY ->
                                clientCtr.sendKeyAES((byte[]) data.getData());

                            case ObjectWrapper.INIT_RECEIVED_FILE ->
                                view.saveFile();

                            case ObjectWrapper.SCHEDULE_COMPLETE -> {
                                view.schedule(data);
                            }

//                                case ObjectWrapper.SERVER_INFORM_CLIENT_NUMBER ->
//                                    view.showMessage(data.getData().toString());
                        }
                        if (!myActiveUI.isEmpty()) {
                            for (int i = 0; i < myActiveUI.size(); i++) {
                                if (myActiveUI.get(i).getPerformative() == data.getPerformative()) {
                                    switch (data.getPerformative()) {
                                        case ObjectWrapper.REPLY_SIGNIN_USER -> {
                                            SignInForm login = (SignInForm) myActiveUI.get(i).getData();
                                            login.receivedData(data);
                                        }

                                        case ObjectWrapper.REPLY_SIGNUP_USER -> {
                                            SignUpForm register = (SignUpForm) myActiveUI.get(i).getData();
                                            if (data.getData() instanceof Boolean) {
                                                register.checkExistMail(data);

                                            } else {
                                                register.receivedData(data);
                                            }
                                        }

                                        case ObjectWrapper.REPLY_SEND_MAIL -> {
                                            SendMailForm sendMail = (SendMailForm) myActiveUI.get(i).getData();
                                            if (data.getData() instanceof Integer) {
                                                sendMail.checkRecipient(data);

                                            } else if (data.getData() instanceof ArrayList) {
                                                sendMail.checkRecipients(data);

                                            } else if (data.getData() instanceof User user) {
                                                sendMail.receivedData(data);
                                                clientCtr.sendData(new ObjectWrapper(ObjectWrapper.SCHEDULE_LIST, user));
                                                clientCtr.sendData(new ObjectWrapper(ObjectWrapper.GET_TOTAL_MAIL_LIST, user));
                                            } else {

                                                sendMail.receivedData(data);

                                            }
                                        }

                                        default -> {
                                        }

                                    }
                                }

                            }
                        }
                    }
                }

            } catch (IOException ex) {
                ex.printStackTrace();
                if (mySocket != null) {
                    clientCtr.closeConnection();
                }

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}
