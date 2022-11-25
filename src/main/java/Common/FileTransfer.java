/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author phamt
 */
public class FileTransfer {

    Socket socket;

    public FileTransfer(Socket socket) {
        this.socket = socket;
    }

    public void sendFile(String folder, String path) {
        try {

            int bytes = 0;
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            File file;
            if (!folder.isEmpty()) {
                file = new File(folder, path);
            } else {
                file = new File(path);
            }

            FileInputStream fileInputStream = new FileInputStream(file);

            dataOutputStream.writeLong(file.length());

            byte[] buffer = new byte[4 * 1024];
            while ((bytes = fileInputStream.read(buffer)) != -1) {

                dataOutputStream.write(buffer, 0, bytes);
                dataOutputStream.flush();
            }

            System.out.println("Sending file successfully");
            fileInputStream.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void receiveFile(String folder, String fileName) throws ClassNotFoundException {
        try {
            File file = new File(folder, fileName);
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            int bytes = 0;
            FileOutputStream fileOutputStream = new FileOutputStream(file);

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
    }
    
   public File clientReceiveFile(String folder, String fileName) {
        try {

            File file = new File(folder, fileName);
//            FileOutputStream fileOutputStream = new FileOutputStream(file);
////            in = new DataInputStream(socket.getInputStream());

            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            int bytes = 0;
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            long size = dataInputStream.readLong();
            byte[] buffer = new byte[4 * 1024];
            while (size > 0 && (bytes = dataInputStream.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {

                fileOutputStream.write(buffer, 0, bytes);
                size -= bytes;
            }

            System.out.println("File is Received");
            fileOutputStream.close();
            return file;

        } catch (IOException ex) {
            System.err.println(ex);
        }
        return null;
    }

}
