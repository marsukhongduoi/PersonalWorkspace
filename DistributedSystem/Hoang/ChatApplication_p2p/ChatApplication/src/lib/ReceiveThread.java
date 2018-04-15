/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lib;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.*;
import java.net.Socket;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTextArea;
import model.History;

/**
 *
 * @author Hoang
 */
public class ReceiveThread implements Runnable {

    private Socket sock;
    private JTextArea txtHistory;
    private String receiver;
    private JList listuser;
    private JComboBox cbxUsers;

    public ReceiveThread(Socket sock, JTextArea txtHistory, String receiver, JComboBox cbxUsers) {
        this.sock = sock;
        this.txtHistory = txtHistory;
        this.receiver = receiver;
//        this.listuser = listuser;
        this.cbxUsers = cbxUsers;
    }

    String formatFileSize(long size) {
        String hrsize = null;
        double b = size;
        double kb = size / 1024.0;
        double mb = (size / 1024.0) / 1024.0;
        double gb = ((size / 1024.0) / 1024.0) / 1024.0;
        double tb = (((size / 1024.0) / 1024.0) / 1024.0) / 1024.0;
        DecimalFormat dec = new DecimalFormat("0.00");
        if (tb > 1) {
            hrsize = dec.format(tb).concat(" TB");
        } else if (gb > 1) {
            hrsize = dec.format(gb).concat(" GB");
        } else if (mb > 1) {
            hrsize = dec.format(mb).concat(" MB");
        } else if (kb > 1) {
            hrsize = dec.format(kb).concat(" KB");
        } else {
            hrsize = dec.format(b).concat(" Bytes");
        }
        return hrsize;
    }

    String getCurrentDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        return dateFormat.format(calendar.getTime());
    }

    @Override
    public void run() {
        while (sock.isConnected()) {
            try {
                //system
                BufferedInputStream bufferedInputStream = new BufferedInputStream(sock.getInputStream());
                DataInputStream dataInputStream = new DataInputStream(bufferedInputStream);
                FileOutputStream fileOutputStream = null;
                BufferedOutputStream bufferedOutputStream = null;
//                String note = "";
                String mode = dataInputStream.readUTF();

                //Chat component
                String username = "";
                String filename = "";
                String message = "";
                String chatContent = "";
                String[] chatContentSplit;
                long filesize = 0;
                File file = null;
//                History history = null;

                switch (mode) {
                    case "USER":
                        username = dataInputStream.readUTF();
                        String[] nameSplit = username.split("\n");
//                        ((DefaultListModel)listuser.getModel()).addElement(username);
                        for (int i = 0; i < nameSplit.length; i++) {
                            cbxUsers.addItem(nameSplit[i]);
                        }
                        username = "";
                        Thread.sleep(1000);
                        break;
                    case "FILE":
                        filesize = dataInputStream.readLong();
                        filename = dataInputStream.readUTF();
                        file = new File("ReceivedFiles\\" + filename);
                        fileOutputStream = new FileOutputStream(file);
                        bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                        for (int i = 0; i < filesize; i++) {
                            bufferedOutputStream.write(bufferedInputStream.read());
                        }
                        bufferedOutputStream.flush();
                        chatContent = dataInputStream.readUTF();
                        chatContentSplit = chatContent.split(": ");
                        username = chatContentSplit[0];
                        message = chatContentSplit[1];
//                        history = new History(username, receiver, chatContent, getCurrentDateTime());
//                        history.insertHistory();
                        txtHistory.append(chatContent + "\n");
                        txtHistory.setCaretPosition(txtHistory.getDocument().getLength());

                        //reset all values;
                        username = "";
                        message = "";
                        filename = "";
                        filesize = 0;
                        file = null;
//                        history = null;
                        Thread.sleep(1000);
                        break;
                    case "MESS":
                        dataInputStream = new DataInputStream(bufferedInputStream);
                        chatContent = dataInputStream.readUTF();
                        chatContentSplit = chatContent.split(": ");
                        username = chatContentSplit[0];
                        message = chatContentSplit[1];
//                        history = new History(username, receiver, chatContent, getCurrentDateTime());
//                        history.insertHistory();
                        txtHistory.append(chatContent);
                        txtHistory.setCaretPosition(txtHistory.getDocument().getLength());

                        //reset all values;
//                        history = null;
                        username = "";
                        chatContent = "";
                        message = "";
                        Thread.sleep(1000);
                        break;
                }
            } catch (Exception ex) {

            }
        }
    }

}
