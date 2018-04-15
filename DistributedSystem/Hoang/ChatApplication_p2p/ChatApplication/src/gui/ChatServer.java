/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import lib.*;

/**
 *
 * @author Hoang
 */
public class ChatServer extends javax.swing.JFrame {

    /**
     * Creates new form SendFileServer
     */
    ServerSocket servSock = null;
    Socket sock = null;
    FileInputStream fileInputStream = null;
    BufferedInputStream bufferedInputStream = null;
    BufferedOutputStream bufferedOutputStream = null;
    DataOutputStream dataOutputStream = null;
//    ArrayList<String> connector;
//    Hashtable<String, Socket> listUser;
//    ArrayList<Socket> sockList;
    String username = "";
    ChatClient client;

    public ChatServer() {
        initComponents();
        //client = new ChatClient();
        setButtonState(true);
    }

//    public ArrayList<String> getConnector() {
//        return connector;
//    }
    public void setUsername(String username) {
        this.username = username;
        lbusername1.setText(username);
    }
    
    void SendMess() {
        try {
            // TODO add your handling code here:

            bufferedOutputStream = new BufferedOutputStream(sock.getOutputStream());
            dataOutputStream = new DataOutputStream(bufferedOutputStream);
            String chat = username + ": " + txtChat1.getText();
            dataOutputStream.writeUTF("MESS");
            dataOutputStream.writeUTF(chat);
            dataOutputStream.flush();
            txtChat1.setText("");
            txtHistory1.append(chat);
            txtHistory1.setCaretPosition(txtHistory1.getDocument().getLength());
        } catch (IOException ex) {

        }
    }

    void SendUsername() {
        try {
            // TODO add your handling code here:
            String mode = "USER";
            bufferedOutputStream = new BufferedOutputStream(sock.getOutputStream());
            dataOutputStream = new DataOutputStream(bufferedOutputStream);
            dataOutputStream.writeUTF(mode);
            dataOutputStream.flush();
            dataOutputStream.writeUTF(username);
            dataOutputStream.flush();
        } catch (IOException ex) {

        }
    }

    void SendFile(File f) {
        try {
            bufferedOutputStream = new BufferedOutputStream(sock.getOutputStream());
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
            String note = username + ": Sending file " + name + " total size " + formatFileSize(filesize) + "\n";
            dataOutputStream.writeUTF(note);
            dataOutputStream.flush();
            txtHistory1.append(note);
            txtHistory1.setCaretPosition(txtHistory1.getDocument().getLength());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Exception: " + ex.getMessage());
            return;
        }
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtIP1 = new javax.swing.JTextField();
        txtPort1 = new javax.swing.JTextField();
        btnConnect1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btnBrowse1 = new javax.swing.JButton();
        btnSend1 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtChat1 = new javax.swing.JTextArea();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtHistory1 = new javax.swing.JTextArea();
        btnDisconnect1 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        cbxUsers1 = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        lbusername1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        txtIP1.setText("127.0.0.1");

        txtPort1.setText("3333");

        btnConnect1.setText("Connect");
        btnConnect1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConnect1ActionPerformed(evt);
            }
        });

        jLabel4.setText("IP");

        jLabel5.setText("Port");

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Chat box"));

        btnBrowse1.setText("Send file");
        btnBrowse1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowse1ActionPerformed(evt);
            }
        });

        btnSend1.setText("Send");
        btnSend1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSend1ActionPerformed(evt);
            }
        });

        txtChat1.setColumns(20);
        txtChat1.setRows(5);
        txtChat1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtChat1KeyTyped(evt);
            }
        });
        jScrollPane3.setViewportView(txtChat1);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(btnSend1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBrowse1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane3)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSend1)
                    .addComponent(btnBrowse1))
                .addGap(1, 1, 1))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("History"));

        txtHistory1.setColumns(20);
        txtHistory1.setRows(5);
        txtHistory1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtHistory1KeyTyped(evt);
            }
        });
        jScrollPane4.setViewportView(txtHistory1);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
        );

        btnDisconnect1.setText("Disconnect");
        btnDisconnect1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDisconnect1ActionPerformed(evt);
            }
        });

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Chat list"));

        cbxUsers1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxUsers1ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbxUsers1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbxUsers1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel6.setText("username: ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtIP1)
                            .addComponent(txtPort1, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnDisconnect1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnConnect1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 111, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(lbusername1)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIP1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(btnConnect1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPort1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(btnDisconnect1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(lbusername1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnConnect1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConnect1ActionPerformed
        // TODO add your handling code here:
        int port = Integer.parseInt(txtPort1.getText());
        String ip = txtIP1.getText();

        try {
            sock = new Socket(ip, port);
            txtHistory1.setText("<Connected>\n");
            Thread t = new Thread(new ReceiveThread(sock, txtHistory1, username, cbxUsers1));
            t.start();
            setButtonState(false);
        } catch (Exception ex) {
            try {
                servSock = new ServerSocket(port);
                txtHistory1.setText("<Waiting for connection>\n");
                sock = servSock.accept();
//                sockList.add(sock);
                if (sock.isConnected()) {
                    txtHistory1.setText("<Connected>\n");
                    Thread t = new Thread(new ReceiveThread(sock, txtHistory1, username, cbxUsers1));
                    t.start();
                }
                setButtonState(false);
            } catch (Exception e) {

            }
        }
    }//GEN-LAST:event_btnConnect1ActionPerformed

    private void btnBrowse1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowse1ActionPerformed
        // TODO add your handling code here:
        JFileChooser chooser = new JFileChooser();
        File f = null;
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            f = chooser.getSelectedFile();
            SendFile(f);
        }
    }//GEN-LAST:event_btnBrowse1ActionPerformed

    private void btnSend1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSend1ActionPerformed
        // TODO add your handling code here:
        SendMess();
    }//GEN-LAST:event_btnSend1ActionPerformed

    private void txtChat1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtChat1KeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if (c == KeyEvent.VK_ENTER) {
            SendMess();
        }
    }//GEN-LAST:event_txtChat1KeyTyped

    private void txtHistory1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHistory1KeyTyped
        evt.consume();
    }//GEN-LAST:event_txtHistory1KeyTyped

    private void btnDisconnect1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDisconnect1ActionPerformed
        try {
            // TODO add your handling code here:
            servSock.close();
            setButtonState(false);
            txtHistory1.setText("Server was disconnected");
        } catch (IOException ex) {

        }
    }//GEN-LAST:event_btnDisconnect1ActionPerformed

    private void cbxUsers1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxUsers1ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxUsers1ItemStateChanged

    void setButtonState(boolean bState) {
        btnConnect1.setEnabled(bState);
        btnDisconnect1.setEnabled(!bState);
    }

//    public void setConnector(String name) {
//        connector.add(name);
//    }
//
//    public ArrayList<Socket> getSockList() {
//        return sockList;
//    }
//
//    public void setListUser(String name, Socket client) {
//        listUser.put(name, client);
//    }
//
//    public void setSockList(Socket s) {
//        sockList.add(s);
//    }
//
//    public Hashtable<String, Socket> getListUser() {
//        return listUser;
//    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ChatServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChatServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChatServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChatServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ChatServer().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBrowse1;
    private javax.swing.JButton btnConnect1;
    private javax.swing.JButton btnDisconnect1;
    private javax.swing.JButton btnSend1;
    private javax.swing.JComboBox<String> cbxUsers1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lbusername1;
    private javax.swing.JTextArea txtChat1;
    private javax.swing.JTextArea txtHistory1;
    private javax.swing.JTextField txtIP1;
    private javax.swing.JTextField txtPort1;
    // End of variables declaration//GEN-END:variables
}
