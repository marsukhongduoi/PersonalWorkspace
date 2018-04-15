/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lib;

import java.awt.List;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import models.Client;
import server.ServerPrimary;

/**
 *
 * @author Tyrant Titan
 */
public class ServerPrimaryThread implements Runnable {
    
    private Socket sock = null;
    ServerPrimary server = null;
    private ArrayList <Client> listClient = new ArrayList<>();
    private String clientString = "clientName-clientIP-clientPort-clientFileName-clientPublicKey";

    

    public ServerPrimaryThread(ServerPrimary aThis, Socket sock) {
        this.sock = sock;
        this.server = aThis;
    }

    @Override
    public void run() {
        while(sock.isConnected()) {
            try {
                //read mode from socket
                // there are three mode:
                // NEWCLIENT: a String new ClientString in format: clientName-clientIP-clientPort-clientfileName-clientPublicKey
                // parse the String to get clientName, clientIP, clientPort, clientfileName, clientPublicKey and put into the list
                String username = "";
                String mode = "";
                switch(mode) {
                    case "NEWCLIENT":
                        Client newClient = new Client(clientString); //"trungClient-192.168.1.1-2000-trungFile.txt-12312414123121231241243123"
                        listClient.add(newClient);
                        for (Client a : listClient)
                        {
                            //SEND A TCP PACKET TO EACH CLIENT, THE MESSAGE IS A MODE "UPDATED" - check Client for more info
                            
                        }
                        break;
                    case "DELETECLIENT": //packet is a string in format: clientName-clientIP
                        client
                        for (Client a : listClient)
                            {
                               if (a.getClientName() = clientName && a.getIPAddress() = clientIP)
                               {
                                   listClient.remove(a);
                               }
                            }
                        for (Client a : listClient)
                        {
                            //SEND A TCP PACKET TO EACH CLIENT, THE MESSAGE IS A MODE "UPDATED" - check Client for more info
                            
                        }                                                       

                        Thread.sleep(1000);
                        break;
                    case "REQUEST": //a packet to ask server to send the listClient to user, String format: clientName
                        //SEND A STRING TO CLIENT WITH FORMAT:
                        // client1Name-client1IP-client1Port-client1fileName-client1PublicKey#client2Name-client2IP-client2Port-client2fileName-client2PublicKey#...
                        String clients = "";
                        for (Client a : listClient)
                        {
                            clients += a.getClientName() + "-" + a.getIPAddress() + "-" + String.valueOf(a.getPort()) + "-" +
                                    a.getFileName() + "-"+ a.getPublicKey()+"#";
                        }
                        //Send clients to clients.
                        Thread.sleep(1000);
                        break;
                }
            }catch(Exception ex) {
                
            }
        }
    }
    
}
