/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lib;

import gui.ChatServer;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.JOptionPane;

/**
 *
 * @author Hoang
 */
public class ServerThread implements Runnable {

    private Socket sock = null;
    FileInputStream fileInputStream = null;
    BufferedInputStream bufferedInputStream = null;
    BufferedOutputStream bufferedOutputStream = null;
    DataOutputStream dataOutputStream = null;
    DataInputStream dataInputStream = null;
    String sender = "";
    ChatServer server = null;
    
    public ServerThread() {
        
    }

    public ServerThread(ChatServer server, Socket sock) {
        this.sock = sock;
        this.server = server;
    }
    
    void sendFile(File f, Socket s, String note) {
        try {
            bufferedOutputStream = new BufferedOutputStream(s.getOutputStream());
            dataOutputStream = new DataOutputStream(bufferedOutputStream);
            dataOutputStream.writeUTF("FILE");
            long filesize = f.length();
            String name = f.getName();
            fileInputStream = new FileInputStream(f);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            dataOutputStream.writeLong(filesize);
            dataOutputStream.writeUTF(name);
            int theByte = 0;
            while ((theByte = bufferedInputStream.read()) != -1) {
                bufferedOutputStream.write(theByte);
            }
            bufferedOutputStream.flush();
            dataOutputStream.writeUTF(note);
            dataOutputStream.flush();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Exception: " + ex.getMessage());
            return;
        }
    }
    
    public void sendMess(String chat, Socket s) {
        try {
            bufferedOutputStream = new BufferedOutputStream(s.getOutputStream());
            dataOutputStream = new DataOutputStream(bufferedOutputStream);
            dataOutputStream.writeUTF("MESS");
            dataOutputStream.writeUTF(chat);
            dataOutputStream.flush();
        } catch (IOException ex) {
            System.out.print("Exception in sendMess: ");
            System.out.println(ex.getMessage());
        }
    }
    
    public void sendUsername(String username, Socket s) {
        try {
            bufferedOutputStream = new BufferedOutputStream(s.getOutputStream());
            dataOutputStream = new DataOutputStream(bufferedOutputStream);
            dataOutputStream.writeUTF("USER");
            dataOutputStream.flush();
            dataOutputStream.writeUTF(username);
            dataOutputStream.flush();
        } catch (IOException ex) {
            System.out.print("Exception in sendUsername: ");
            System.out.println(ex.getMessage());
        }
    }
    
    public void transferMessage(String mess, String username) {
        Hashtable<String, Socket> hashtab = server.getListUser();
        Enumeration e = hashtab.keys();
        while(e.hasMoreElements()) {
            String name = (String)e.nextElement();
            if(name.equals(username)) {
                Socket s = hashtab.get(name);
                sendMess(mess, s);
            }
        }
    }
    
    public void transferUsername(String username) {
        Hashtable<String, Socket> hashtab = server.getListUser();
        Enumeration e = hashtab.keys();
        String str = "";
        while(e.hasMoreElements()) {
            String name = (String)e.nextElement();
            if(!name.equals(username)) {
                System.out.println(name);
                Socket s = hashtab.get(name);
                str += name + "\n";
                sendUsername(username, s);
            }
        }
        if(!str.isEmpty()) {
            sendUsername(str, sock);
        }
    }
    
    public void transferFile(File f, String note, String username) {
        Hashtable<String, Socket> hashtab = server.getListUser();
        Enumeration e = hashtab.keys();
        while(e.hasMoreElements()) {
            String name = (String)e.nextElement();
            if(name.equals(username)) {
                Socket s = hashtab.get(name);
                sendFile(f, s, note);
            }
        }
    }
    
    @Override
    public void run() {
        while(sock.isConnected()) {
            try {
                bufferedInputStream = new BufferedInputStream(sock.getInputStream());
                dataInputStream = new DataInputStream(bufferedInputStream);
                bufferedOutputStream = new BufferedOutputStream(sock.getOutputStream());
                dataOutputStream = new DataOutputStream(bufferedOutputStream);
                String note = "";
                String mode = dataInputStream.readUTF();
                String username = "";
                switch(mode) {
                    case "NEWUSER":
                        username = dataInputStream.readUTF();
                        if(!server.getListUser().containsKey(username)) {
                            server.setListUser(username, sock);
                            transferUsername(username);
                            Thread.sleep(1000);
                        }
                        break;
                    case "FILE":
                        username = dataInputStream.readUTF();
                        long filesize = dataInputStream.readLong();
                        String filename = dataInputStream.readUTF();
                        File myFile = new File("Temp\\" + filename);
                        FileOutputStream fos = new FileOutputStream(myFile);
                        BufferedOutputStream bos = new BufferedOutputStream(fos);
                        for (int i = 0; i < filesize; i++) {
                            bos.write(bufferedInputStream.read());
                        }
                        bos.flush();
                        note = dataInputStream.readUTF();
                        transferFile(myFile, note, username);
                        Thread.sleep(1000);
                        break;
                    case "MESS":
                        String chat = "";
                        username = dataInputStream.readUTF();
                        chat = dataInputStream.readUTF();
                        transferMessage(chat, username);
                        Thread.sleep(1000);
                        break;
                }
            }catch(Exception ex) {
                
            }
        }
    }
    
}
