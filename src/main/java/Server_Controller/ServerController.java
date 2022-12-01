/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server_Controller;

import Common.FileTransfer;
import Model.Mail;
import Model.MailDAL;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import Model.ObjectWrapper;
import Common.Security;
import Common.Validation;
import Model.MailReceived;
import Model.Status;
import Model.User;
import Model.UserDAL;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

/**
 *
 * @author phamt
 */
public class ServerController {

    private ServerSocket myServer;
    private ServerListening myListening;
    private Scanner stdIn;
    private String host = "localhost";
    private String domainName = "smail.com";
    private int port = 1234;
    private int numThread = 5;
    public ArrayList<ClientHandler> clientList;
    private ExecutorService executor;
    private String logSignIn = "log_signin.txt";
    private String logSignOut = "log_signout.txt";
    private LinkedHashMap<String, String> mapSignin = new LinkedHashMap<>();
    private LinkedHashMap<String, String> mapSignout = new LinkedHashMap<>();
    private UserDAL uDAL = new UserDAL();
    private MailDAL mDAL = new MailDAL();
    private Security sec = new Security();
    private User u = new User();

    public ServerController() {

        executor = Executors.newFixedThreadPool(numThread);
        clientList = new ArrayList<>();
        saveIpServerToApi();
        openServer();
        stdIn = new Scanner(System.in);
    }

    public void openServer() {
        try {
            myServer = new ServerSocket(port);
            myListening = new ServerListening(this);
            executor.execute(myListening);
            readDomaiName();
            System.out.println("TCP server is running at the port " + port + "...");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void stopServer() {
        try {
            executor.shutdownNow();
            myServer.close();
            System.out.println("TCP server is stopped!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveIpServerToApi() {
        try {
            String localIP = InetAddress.getLocalHost().getHostAddress();

            String api = "https://retoolapi.dev/ItgGl1/data/1";
            String jsonData = "{\"ip\":\"" + localIP + "\"}";
            Jsoup.connect(api)
                    .ignoreContentType(true).ignoreHttpErrors(true)
                    .header("Content-Type", "application/json")
                    .requestBody(jsonData)
                    .method(Connection.Method.PUT).execute();
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

//    public void publicClientNumber() {
//        ObjectWrapper data = new ObjectWrapper(ObjectWrapper.SERVER_INFORM_CLIENT_NUMBER, "Number of client connecting to the server " + clientList.size());
//
//        for (ClientHandler client : clientList) {
//            client.sendData(data);
////                System.out.println("Server sent '" + msg + "' from Client " + name + "--> Client " + client.name);
//        }
//    }
    public void broardCastInbox(ArrayList<ClientHandler> clientList) {

        for (ClientHandler client : clientList) {
            client.sendData(new ObjectWrapper(ObjectWrapper.BROARDCAST_INBOX, client.getUser()));
        }

    }

    public void broardCastSpam(ArrayList<ClientHandler> clientList) {

        for (ClientHandler client : clientList) {
            ObjectWrapper data = new ObjectWrapper(ObjectWrapper.BROARDCAST_SPAM, client.getUser());
            client.sendData(data);
        }

    }

    public boolean writeFile(String path, String user) {
        boolean result = true;

        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);

        try ( FileWriter fw = new FileWriter(path, true)) {
            fw.write(user + ";" + currentTime + "\n");

        } catch (IOException ex) {
            result = false;
            System.err.println(ex.getMessage());
        }
        return result;
    }

    public void readDomaiName() {
        try ( BufferedReader br = new BufferedReader(new FileReader("domain.txt"))) {
            String data;
            if ((data = br.readLine()) != null) {
                domainName = data;
            }
            this.updateDomainName(domainName);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void writeDomainName() {
        try ( FileWriter fw = new FileWriter("domain.txt")) {
            if (!domainName.isEmpty()) {
                fw.write(domainName);
                System.out.println("Change success");
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public LinkedHashMap<String, String> readFile(LinkedHashMap<String, String> map, String path) {

        try ( BufferedReader br = new BufferedReader(new FileReader(path))) {
            String data;
            while ((data = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(data, ";");
                if (st.countTokens() == 2) {
                    String user = st.nextToken();
                    String time = st.nextToken();
//                    map.put(user, time);
                    map.put(time, user);
                } else {
                    System.out.println("Some line in file not have correct format");
                    break;
                }
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        return map;
    }

    public void getInfoLogUser(String email) {
        mapSignin = this.readFile(mapSignin, logSignIn);
        mapSignout = this.readFile(mapSignout, logSignOut);
        int i = 0;
        System.out.println("""
                           Info SignIn: 
                           -------------------""");
        for (Map.Entry<String, String> mapElement : mapSignin.entrySet()) {

            String user = mapElement.getValue();
            if (getNameFromEmail(user).equals(getNameFromEmail(email))) {
                System.out.println(++i + ". " + mapElement.getKey() + "\n");
            }
        }
        i = 0;

        System.out.println("""
                           Info Signout: 
                           -------------------""");
        for (Map.Entry<String, String> mapElement : mapSignout.entrySet()) {
            String user = mapElement.getValue();
            if (getNameFromEmail(user).equals(getNameFromEmail(email))) {
                System.out.println(++i + ". " + mapElement.getKey() + "\n");
            }
        }
    }

    public String getNameFromEmail(String email) {
        StringTokenizer st = new StringTokenizer(email, "@");
        if (st.hasMoreTokens()) {
            return st.nextToken();
        }
        return null;
    }

    public void SendMessageToAllUser(Mail mail) {
        ArrayList<User> uList = uDAL.getAllUser();
        ArrayList<MailReceived> recList = new ArrayList<>();

        for (User u : uList) {
            if (!u.isIsAdmin()) {
                recList.add(new MailReceived(u));
            }
        }
        mail.setToUser(recList);

        if (mDAL.sendMail(mail)) {
            this.broardCastInbox(clientList);
        }
    }

    public void getListLockedUser() {
        ArrayList<User> lockedList = new ArrayList<>();
        lockedList = uDAL.getLockedUser();
        if (!lockedList.isEmpty()) {
            for (User u : lockedList) {
                System.out.println(u.getEmail() + "\n");
            }
        } else {
            System.out.println("Not have any user been locked");
        }
    }

    public void lockOrUnlockUser(String email, int lock) {
        if (uDAL.lockOrUnlockUser(email, lock)) {
            System.out.println("Locked/Unlocked " + email + " success");
        } else {
            System.out.println("Locked/Unlocked " + email + " fail");
        }
    }

    public void displayUser() {
        ArrayList<User> uList = uDAL.getAllUser();
        System.out.println("Id\tEmail\t\t\tHadUsed\t\tStorage");
        for (User u : uList) {
            System.out.println(u.getId() + "\t" + u.getEmail() + "\t\t" + u.getHadUsed() + "\t\t" + u.getStorage());
        }
    }

    public void updateDomainName(String domain) {
        if (clientList.isEmpty()) {
            ArrayList<User> uList = uDAL.getAllUser();
            for (User u : uList) {
                StringTokenizer st = new StringTokenizer(u.getEmail(), "@");
                String name = st.nextToken();
                String email = name + "@" + domain;
                uDAL.updateDomain(email, u.getId());
            }
        } else {
            System.out.println("Cannot change domain name because the system had some online user");
        }
    }

    public void updateStorage(String email, Double value) {
        if (uDAL.updateStorage(email, value)) {
            System.out.println("Set storage success");
        } else {
            System.out.println("Set storage fail");
        }
    }

    public void createKeyFile() {
        sec.createKeyRSA();
    }

    public void menu() {
        System.out.println("Action List: ");
        System.out.println("\t1.Set domain name server: domain <Domain Name>\n");
        System.out.println("\t2.Get info login and logout of special user: log <Email>\n");
        System.out.println("\t3.Send mail to all user: sendall\n");
        System.out.println("\t4.Get list user in system: list\n");
        System.out.println("\t5.Locked special user: lock <Email>\n");
        System.out.println("\t6.Get list of user had locked: listlocked\n");
        System.out.println("\t7.Unlocked special user: unlock <Email>\n");
        System.out.println("\t8.Set storage inbox user: storage <Email>\n");
        System.out.println("\t9.Logout Admin: logout\n");

    }

    public void action() {
        ArrayList<User> userList = new ArrayList();

        try {
            System.out.println("Hi! You need to login to perform another action!!!");

            do {
                do {
                    System.out.println("Enter your email: ");
                    String email = stdIn.nextLine();

                    if (!uDAL.checkEmailExist(email)) {
                        System.out.println("This email is not invalid");

                    } else if (!uDAL.checkIsAdmin(email)) {
                        System.out.println("This email didn't have authoritative");

                    } else {
                        u = new User();
                        u.setEmail(email);

                        System.out.println("Enter your password: ");
                        String password = stdIn.nextLine();
                        this.u.setPassword(sec.hashMD5(password));

                        if (uDAL.checkLogin(this.u)) {
                            System.out.println("Login success");
                            break;
                        } else {
                            System.out.println("Wrong password!!!");
                        }

                    }
                } while (true);

                do {
                    System.out.println("Type <help> if you don't know syntax of each action");
                    String input = stdIn.nextLine();
                    StringTokenizer st = new StringTokenizer(input, " ");

                    if (st.countTokens() == 1) {
                        String action = st.nextToken().toLowerCase();
                        if (action.equals("logout")) {
                            this.u = null;
                            break;
                        }

                        switch (action) {
                            case "help" -> {
                                menu();
                            }

                            case "sendall" -> {
                                System.out.println("Please enter title you want to send: ");
                                String title = stdIn.nextLine();
                                System.out.println("Please enter content you want to send: ");
                                String content = stdIn.nextLine();

                                Mail mail = new Mail(title, content, this.u);

                                SendMessageToAllUser(mail);
                            }
                            case "list" -> {
                                displayUser();
                            }

                            case "listlocked" -> {
                                getListLockedUser();
                            }

//                            case "exit" -> {
//                                for (ClientHandler client : this.clientList) {
//                                    if (!client.close()) {
//                                        System.out.println("sth wrong in client handler");
//                                    }
//                                }
//                                executor.shutdownNow();
//                                stdIn.close();
//                                myServer.close();
//                            }
                            default ->
                                System.out.println("Wrong syntax please try again!!!");

                        }
                    } else if (st.countTokens() == 2) {
                        String action = st.nextToken();
                        switch (action.toLowerCase()) {

                            case "domain" -> {
                                if (st.hasMoreTokens()) {
                                    String name = st.nextToken();
                                    updateDomainName(name);
                                    domainName = name;
                                    writeDomainName();
                                }
                            }

                            case "log" -> {
                                if (st.hasMoreTokens()) {
                                    String email = st.nextToken();
                                    if (Validation.validationMail(email)) {
                                        int uId = uDAL.getId(email);
                                        if (uId != 0) {
                                            getInfoLogUser(email);
                                        } else {
                                            System.out.println("This email is not exist");
                                        }
                                    } else {
                                        System.out.println("This email is invalid");
                                    }
                                }
                            }

                            case "lock" -> {
                                if (st.hasMoreTokens()) {
                                    String email = st.nextToken();
                                    if (Validation.validationMail(email)) {
                                        int uId = uDAL.getId(email);
                                        if (uId != 0) {
                                            if (!uDAL.checkIsLock(email)) {
                                                lockOrUnlockUser(email, 1);
                                            } else {
                                                System.out.println("This email had been locked");
                                            }
                                        } else {
                                            System.out.println("This email is not exist");
                                        }
                                    } else {
                                        System.out.println("This email is invalid");
                                    }
                                }

                            }
                            case "unlock" -> {

                                if (st.hasMoreTokens()) {
                                    String email = st.nextToken();
                                    if (Validation.validationMail(email)) {
                                        int uId = uDAL.getId(email);
                                        if (uId != 0) {
                                            if (uDAL.checkIsLock(email)) {
                                                lockOrUnlockUser(email, 0);
                                            } else {
                                                System.out.println("This email hadn't been locked");
                                            }
                                        } else {
                                            System.out.println("This email is not exist");
                                        }
                                    } else {
                                        System.out.println("This email is invalid");
                                    }
                                }

                            }

                            case "storage" -> {
                                if (st.hasMoreTokens()) {
                                    String email = st.nextToken();
                                    if (Validation.validationMail(email)) {
                                        int uId = uDAL.getId(email);
                                        if (uId != 0) {
                                            System.out.println("Please enter storage you want to set: ");
                                            String in = stdIn.nextLine();
                                            try {
                                                Double value = Double.valueOf(in);
                                                if (value > 0) {
                                                    updateStorage(email, value);
                                                } else {
                                                    System.out.println("Value must be greater than 0");
                                                }
                                            } catch (NumberFormatException ex) {
                                                System.out.println("Value is not correct format");
                                            }
                                        } else {
                                            System.out.println("This email is not exist");
                                        }
                                    } else {
                                        System.out.println("This email is invalid");
                                    }

                                }
                            }

                            default ->
                                System.out.println("Wrong syntax please try again!!!");
                        }
                    }
                } while (true);
            } while (true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    class ServerListening implements Runnable {

        private ServerController serverCtr;

        public ServerListening(ServerController serverCtr) {
            this.serverCtr = serverCtr;
        }

        public void run() {
            System.out.println("Server is listening...");

            try {
                while (true) {
                    Socket clientSocket = myServer.accept();
                    ClientHandler client = new ClientHandler(clientSocket, serverCtr);

                    clientList.add(client);
                    executor.execute(client);
                    client.sendPublicKey();

                    System.out.println("Number of client connecting to the server: " + clientList.size());
//                    publicClientNumber();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    class ClientHandler implements Runnable {

        private Socket mySocket;
        private ServerController serverCtr;
        private User user;
        private ObjectInputStream ois;
        private ObjectOutputStream oos;
        private FileTransfer ft;
        private boolean shutdown = false;
        private String keyAES;

        public ClientHandler(Socket s, ServerController serverCtr) throws IOException {
            this.serverCtr = serverCtr;
            mySocket = s;
            ois = new ObjectInputStream(mySocket.getInputStream());
            oos = new ObjectOutputStream(mySocket.getOutputStream());
            ft = new FileTransfer(mySocket);

        }

        public User getUser() {
            return this.user;
        }

        public void setUser(User u) {
            user = u;
        }

        public boolean sendData(Object obj) {
            try {
                oos.writeObject(encryptData(obj));
            } catch (IOException ex) {
                System.out.println(ex);
                return false;
            }
            return true;
        }

        public long calculateSize(Object o) {
            long size = 0;
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(o);
                oos.close();
                size = baos.size();
            } catch (Exception ex) {
                System.err.println(ex);
            }
            return size;
        }

        public void getDomainName() throws IOException {
            if (!domainName.isEmpty()) {
                sendData(new ObjectWrapper(ObjectWrapper.GET_DOMAIN_NAME, domainName));
            } else {
                sendData(new ObjectWrapper(ObjectWrapper.GET_DOMAIN_NAME, "fail"));
            }
        }

        public void signin(Object data) throws IOException {
            User user = (User) data;
            user.setPassword(sec.hashMD5(user.getPassword()));

            if (!uDAL.checkIsLock(user.getEmail())) {

                if (uDAL.checkLogin(user)) {

                    if (!clientList.isEmpty()) {
                        for (ClientHandler client : clientList) {
                            if (client != this && client.user != null && client.user.getEmail().equals(user.getEmail())) {
                                sendData(new ObjectWrapper(ObjectWrapper.REPLY_SIGNIN_USER, "duplicated"));
                                return;
                            }
                        }
                    }

                    serverCtr.writeFile(logSignIn, user.getEmail());

                    Object oUser = (Object) user;
                    sendData(new ObjectWrapper(ObjectWrapper.REPLY_SIGNIN_USER, oUser));
                    setUser(user);

                } else {
                    sendData(new ObjectWrapper(ObjectWrapper.REPLY_SIGNIN_USER, "false"));
                }
            } else {
                sendData(new ObjectWrapper(ObjectWrapper.REPLY_SIGNIN_USER, "locked"));
            }
        }

        public void signup(Object data) throws IOException {
            User user = (User) data;
            user.setPassword(sec.hashMD5(user.getPassword()));
            if (uDAL.register(user)) {

                StringTokenizer st = new StringTokenizer(user.getEmail(), "@");
                String name = st.nextToken();
                new File("users" + "/" + name).mkdirs();

                sendData(new ObjectWrapper(ObjectWrapper.REPLY_SIGNUP_USER, "success"));
            } else {
                sendData(new ObjectWrapper(ObjectWrapper.REPLY_SIGNUP_USER, "false"));
            }
        }

        public void signout() {
            if (user != null) {
                serverCtr.writeFile(logSignOut, user.getEmail());
                setUser(null);
            }

        }

        public void checkRecipient(Object data) throws IOException {
            String mail = data.toString();
            int uId = uDAL.getId(mail);

            if (uId != 0) {
                sendData(new ObjectWrapper(ObjectWrapper.REPLY_SEND_MAIL, uId));
            } else {
                sendData(new ObjectWrapper(ObjectWrapper.REPLY_SEND_MAIL, 0));
            }

        }

        public void checkRecipients(Object data) throws IOException {
            ArrayList<String> mailToList = (ArrayList<String>) data;
            ArrayList<Integer> idList = new ArrayList<>();
            for (String mail : mailToList) {

                int uId = uDAL.getId(mail);
                if (uId == 0) {
                    sendData(new ObjectWrapper(ObjectWrapper.REPLY_SEND_MAIL, new ArrayList<Integer>()));
                    return;
                }
                idList.add(uId);
            }
            sendData(new ObjectWrapper(ObjectWrapper.REPLY_SEND_MAIL, idList));
        }

        public void sendMail(Object data) throws IOException {
            Mail mail = (Mail) data;
            for (int i = 0; i < mail.getToUser().size(); i++) {
                ArrayList<User> userSpamList = mDAL.getUserSpamMailList(mail.getToUser().get(i).getReceiver());
                for (User u : userSpamList) {
//                    System.out.println(u.getId() + "&" + mail.getFormUser().getId());
                    if (u.getId() == mail.getFormUser().getId()) {
                        mail.getToUser().get(i).getStatus().setId(ObjectWrapper.SPAM_LIST);
                    }
                }
            }

            for (int i = 0; i < mail.getToUser().size(); i++) {
                if (uDAL.checkStorage(mail.getToUser().get(i).getReceiver())) {
                    sendData(new ObjectWrapper(ObjectWrapper.IS_FULL, mail.getToUser().get(i).getReceiver()));
                    mail.getToUser().remove(i);
                }
            }

            if (!mail.getToUser().isEmpty() && mDAL.sendMail(mail)) {
//                String folder = "users/" + this.user.getEmail();
//                File file = new File(folder);
//                long size = file.length() / (1024 * 1024);\

                if (mail.getFile() != null) {
                    for (MailReceived rec : mail.getToUser()) {
                        Double sizeInDb = uDAL.getSizeQueryDb(rec.getReceiver());
                        if (sizeInDb == null) {
                            sizeInDb = 0.0;
                        }

                        Double total = sizeInDb + mail.getSize();
//                        System.out.println("Size of file: " + total);

                        uDAL.updateHadUsed(rec.getReceiver(), total);
                    }
                }

                ArrayList< ClientHandler> clientReceived = new ArrayList<>();
                for (MailReceived rec : mail.getToUser()) {
                    for (ClientHandler client : clientList) {
                        System.out.println(client);
//                          System.out.println(client.getUser().getEmail() + "&" + u.getEmail());
                        if (client.getUser().getEmail().equals(rec.getReceiver().getEmail())) {
                            clientReceived.add(client);

                        }
                    }
                }

                getTotalMailList(getUser());
                getSendMailList(getUser());

                for (int i = 0; i < mail.getToUser().size(); i++) {
                    int statusId = mail.getToUser().get(i).getStatus().getId();
                    switch (statusId) {
                        case ObjectWrapper.INBOX_LIST -> {
                            if (!clientReceived.isEmpty()) {
                                serverCtr.broardCastInbox(clientReceived);
                            }
                            sendData(new ObjectWrapper(ObjectWrapper.REPLY_SEND_MAIL, "success"));
                        }
                        case ObjectWrapper.SPAM_LIST -> {
                            if (!clientReceived.isEmpty()) {
                                serverCtr.broardCastSpam(clientReceived);
                            }
                            sendData(new ObjectWrapper(ObjectWrapper.REPLY_SEND_MAIL, "success"));
                        }

                        case ObjectWrapper.SCHEDULE_LIST -> {
                            try {
                                mail.setId(mDAL.getLastID());
                                sendData(new ObjectWrapper(ObjectWrapper.REPLY_SEND_MAIL, mail.getFormUser()));
                                
                                scheduleSendMail(mail);
                                
                            } catch (SQLException ex) {
                                System.err.println(ex);
                            }

                        }

                    }
                }

            } else {
                sendData(new ObjectWrapper(ObjectWrapper.REPLY_SEND_MAIL, "false"));
            }
        }

        public void getMailByStatus(Object data, int status) throws IOException {

            User user = (User) data;
            MailDAL mDAL = new MailDAL();
            ArrayList<Mail> mailList = mDAL.getListMailByStatus(user, status);

            if (!mailList.isEmpty()) {
//                for (Mail m : mailList) {
//                    m = mDAL.getReplyMail(m, m.getReplyMail().getId());
//                }
                for (Mail m : mailList) {
                    m = mDAL.getReplies(m);
                }
                sendData(new ObjectWrapper(ObjectWrapper.GET_LIST_MAIL, mailList));
            } else {
                sendData(new ObjectWrapper(ObjectWrapper.GET_LIST_MAIL, "empty"));
            }
        }

        public void getSendMailList(Object data) throws IOException {
            User user = (User) data;
            ArrayList<Mail> mailList = mDAL.getSendMail(user, 0);

            if (!mailList.isEmpty()) {
//                for (Mail m : mailList) {
//                    m = mDAL.getReplyMail(m, m.getReplyMail().getId());
//                }
                for (Mail m : mailList) {
                    m = mDAL.getReplies(m);
                }
                sendData(new ObjectWrapper(ObjectWrapper.GET_LIST_MAIL, mailList));
            } else {
                sendData(new ObjectWrapper(ObjectWrapper.GET_LIST_MAIL, "empty"));
            }

        }

        public void getScheduleList(Object data, int status) throws IOException {

            if (data instanceof ObjectWrapper) {

            }
            User user = (User) data;

            ArrayList<Mail> mailList = mDAL.getSendMail(user, status);

            if (!mailList.isEmpty()) {
                sendData(new ObjectWrapper(ObjectWrapper.SCHEDULE_LIST, mailList));
//                getTotalMailList(data);

            } else {
                sendData(new ObjectWrapper(ObjectWrapper.SCHEDULE_LIST, "empty"));
            }
        }

        public void scheduleSendMail(Mail mail) {
            try {
                Timer timer = new Timer();
                Thread myThread = new sendMail(serverCtr, this, mail, user);

                String time = mail.getSchedule();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date date = (Date) format.parse(time);

                timer.schedule(
                        new sendTask(myThread),
                        date
                );
            } catch (Exception ex) {
                System.err.println(ex);
            }
        }

//        public void enableScheduleSendMail() {
//            ArrayList<Mail> mailList = mDAL.getSendMail(user, ObjectWrapper.SCHEDULE_LIST);
//          
//            if (!mailList.isEmpty()) {
//                for (Mail m : mailList) {
//                    scheduleSendMail(m);
//                }
//            }
//        }
        public void updateStatus(Object data, int status, int performative) throws IOException {
            Mail mail = (Mail) data;
            if (mDAL.updateStatus(mail, status)) {

                User u = new User();
                for (ClientHandler client : clientList) {
                    if (client.equals(this)) {
                        u = client.getUser();
                    }
                }
                sendData(new ObjectWrapper(performative, u));

            } else {

                sendData(new ObjectWrapper(performative, "fail"));
            }
        }

        public void getTotalMailList(Object data) throws IOException {
            ArrayList<Integer> totalNum = new ArrayList<>();
            User user = (User) data;

            for (int i = 1; i <= 4; i++) {
                totalNum.add(mDAL.getTotalMailByStatus(user, i));
            }

            totalNum.add(mDAL.getTotalSendList(user, ObjectWrapper.SCHEDULE_LIST));
            totalNum.add(mDAL.getTotalSendList(user, 0));

            if (!totalNum.isEmpty()) {
                sendData(new ObjectWrapper(ObjectWrapper.GET_TOTAL_MAIL_LIST, totalNum));
            } else {
                sendData(new ObjectWrapper(ObjectWrapper.GET_TOTAL_MAIL_LIST, "empty"));

            }
        }

        public void checkExistEmail(Object data) throws IOException {
            if (uDAL.checkEmailExist(data.toString())) {
                sendData(new ObjectWrapper(ObjectWrapper.REPLY_SIGNUP_USER, true));
            } else {
                sendData(new ObjectWrapper(ObjectWrapper.REPLY_SIGNUP_USER, false));
            }
        }

        public void getToUser(Object data) throws IOException {
            Mail mail = (Mail) data;
            ArrayList<User> toUser = mDAL.getToUser(mail);
            if (!toUser.isEmpty()) {
                sendData(new ObjectWrapper(ObjectWrapper.GET_TO_USER, toUser));
            } else {
                sendData(new ObjectWrapper(ObjectWrapper.GET_TO_USER, "empty"));
            }
        }

        public void deleteMail(Object data, int performative) throws IOException {
            Mail mail = (Mail) data;
            switch (performative) {
                case ObjectWrapper.DELETE_SCHEDULE -> {
                    if (mDAL.deleteSchedule(mail.getId())) {
                        sendData(new ObjectWrapper(performative, user));
                    } else {
                        sendData(new ObjectWrapper(performative, "false"));
                    }
                }
                case ObjectWrapper.DELETE_MAIL -> {
                    if (mDAL.deleteMail(mail.getId(), user.getId())) {
                        uDAL.updateHadUsed(user, -mail.getSize());
                        sendData(new ObjectWrapper(performative, user));
                    } else {
                        sendData(new ObjectWrapper(performative, "false"));
                    }
                }
            }
        }

        public void sendPublicKey() throws IOException {
            byte[] key = sec.readPublicKeyRSA();
            oos.writeObject(new ObjectWrapper(ObjectWrapper.PUBLIC_KEY, key));
        }

        public void decryptKeyAES(String key) {
            keyAES = sec.decryptionRSA(key);
//            System.out.println(keyAES);
        }

        public byte[] encryptData(Object data) throws IOException {
            return sec.encryptAES(sec.serialize(data), keyAES);
        }

        public Object decryptData(byte[] data) throws IOException, ClassNotFoundException {
            return sec.deserialize(sec.decryptAES(data, keyAES));
        }

        public void checkStorage(User user) {
            boolean isFull = uDAL.checkStorage(user);
            if (isFull) {
                sendData(new ObjectWrapper(ObjectWrapper.CHECK_STORAGE, "isFull"));
            } else {
                sendData(new ObjectWrapper(ObjectWrapper.CHECK_STORAGE, "notFull"));
            }
        }

        public boolean close() {
            boolean result = true;
            try {
                if (mySocket != null) {
                    clientList.remove(this);

                    if (oos != null) {
                        oos.close();
                    }
                    if (ois != null) {
                        ois.close();
                    }
                    System.out.println("Number of client connecting to the server: " + clientList.size());
//                    publicClientNumber();

                    mySocket.close();
                    shutdown();
                }
            } catch (IOException ex) {
                result = false;
                ex.printStackTrace();
            }

            return result;
        }

        @Override
        public void run() {
            boolean flag = true;
            try {
                while (true && !shutdown) {
                    Object object;
                    if (flag) {
                        // read key aes client send
                        object = ois.readObject();
                        flag = false;

                    } else {
                        object = decryptData((byte[]) ois.readObject());

                    }
                    if (object instanceof ObjectWrapper data) {

                        switch (data.getPerformative()) {

                            case ObjectWrapper.GET_DOMAIN_NAME ->
                                getDomainName();

                            case ObjectWrapper.SIGNIN_USER ->
                                signin(data.getData());

                            case ObjectWrapper.SIGNUP_USER ->
                                signup(data.getData());

                            case ObjectWrapper.CHECK_RECEPIENT ->
                                checkRecipient(data.getData());

                            case ObjectWrapper.SEND_MAIL ->
                                sendMail(data.getData());

                            case ObjectWrapper.INBOX_LIST ->
                                getMailByStatus(data.getData(), ObjectWrapper.INBOX_LIST);

                            case ObjectWrapper.SEND_LIST ->
                                getSendMailList(data.getData());

                            case ObjectWrapper.SCHEDULE_LIST ->
                                getScheduleList(data.getData(), ObjectWrapper.SCHEDULE_LIST);

                            case ObjectWrapper.UPDATE_TO_READ_LIST ->
                                updateStatus(data.getData(), ObjectWrapper.READ_LIST, ObjectWrapper.UPDATE_TO_READ_LIST);
                            case ObjectWrapper.READ_LIST ->
                                getMailByStatus(data.getData(), ObjectWrapper.READ_LIST);

                            case ObjectWrapper.UPDATE_TO_SPAM_LIST ->
                                updateStatus(data.getData(), ObjectWrapper.SPAM_LIST, ObjectWrapper.UPDATE_TO_SPAM_LIST);
                            case ObjectWrapper.SPAM_LIST ->
                                getMailByStatus(data.getData(), ObjectWrapper.SPAM_LIST);

                            case ObjectWrapper.UPDATE_TO_DELETE_LIST ->
                                updateStatus(data.getData(), ObjectWrapper.DELETE_LIST, ObjectWrapper.UPDATE_TO_DELETE_LIST);

                            case ObjectWrapper.DELETE_LIST ->
                                getMailByStatus(data.getData(), ObjectWrapper.DELETE_LIST);

                            case ObjectWrapper.GET_TOTAL_MAIL_LIST ->
                                getTotalMailList(data.getData());

                            case ObjectWrapper.CHECK_RECEPIENTS ->
                                checkRecipients(data.getData());

                            case ObjectWrapper.SIGNOUT ->
                                signout();

                            case ObjectWrapper.CHECK_EXIST_EMAIL ->
                                checkExistEmail(data.getData());

                            case ObjectWrapper.GET_TO_USER ->
                                getToUser(data.getData());

                            case ObjectWrapper.DELETE_SCHEDULE ->
                                deleteMail(data.getData(), ObjectWrapper.DELETE_SCHEDULE);

                            case ObjectWrapper.DELETE_MAIL ->
                                deleteMail(data.getData(), ObjectWrapper.DELETE_MAIL);

                            case ObjectWrapper.KEY_AES -> {
                                decryptKeyAES((String) data.getData());
                            }

                            case ObjectWrapper.INIT_SEND_FILE -> {
                                StringTokenizer st = new StringTokenizer(user.getEmail(), "@");
                                String name = st.nextToken();
                                String folder = "users/" + name;
                                ft.receiveFile(folder, (String) data.getData());
                            }

                            case ObjectWrapper.INIT_RECEIVED_FILE -> {
                                sendData(new ObjectWrapper(ObjectWrapper.INIT_RECEIVED_FILE, ""));
                                StringTokenizer st = new StringTokenizer((String) data.getData(), ";");
                                if (st.countTokens() == 2) {
                                    String folder = "users/" + st.nextToken();
                                    String fileName = st.nextToken();
                                    ft.sendFile(folder, fileName);
                                }
                            }
                            case ObjectWrapper.CHECK_STORAGE -> {
                                checkStorage((User) data.getData());
                            }

                        }
                    }
                }
            } catch (IOException ex) {
                if (mySocket != null) {
                    close();
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public void shutdown() {
            shutdown = true;
        }
    }

    class sendTask extends TimerTask {

        Thread thread;

        sendTask(Thread thread) {
            this.thread = thread;
        }

        public void run() {
            thread.start();
        }
    }

    class sendMail extends Thread {

        private ServerController serverCtr;
        private ClientHandler client;
        private Mail mail;
        private User user;

        public sendMail(ServerController serverCtr, ClientHandler client, Mail mail, User user) {
            this.serverCtr = serverCtr;
            this.client = client;
            this.mail = mail;
            this.user = user;
        }

        public void run() {
            if (mDAL.updateStatus(mail, ObjectWrapper.INBOX_LIST)) {

                System.out.println("Send schedule mail success");

                ArrayList< ClientHandler> clientReceived = new ArrayList<>();
                for (MailReceived rec : mail.getToUser()) {
                    for (ClientHandler client : serverCtr.clientList) {
//                          System.out.println(client.getUser().getEmail() + "&" + u.getEmail());
                        if (client.getUser().getEmail().equals(rec.getReceiver().getEmail())) {
                            clientReceived.add(client);

                        }
                    }

                }
                serverCtr.broardCastInbox(clientReceived);

                client.sendData(new ObjectWrapper(ObjectWrapper.SCHEDULE_COMPLETE, user));

            } else {
                client.sendData(new ObjectWrapper(ObjectWrapper.SCHEDULE_COMPLETE, "fail"));
            }
        }
    }

    public static void main(String[] args) {
        ServerController server = new ServerController();
//        server.createKeyFile();
        server.action();
    }

}
