/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author Tyrant Titan
 */
public class Client {
    private String clientName;
    private String IPAddress;
    private int port;
    private String fileName;
    private String publicKey;

    public Client(String input) {
        String delims = "[-]";
        String[] tokens = input.split(delims);
        this.clientName = tokens[0];
        this.IPAddress = tokens[1];
        this.port = Integer.parseInt(tokens[2]);
        this.fileName = tokens[3];
        this.publicKey = tokens[4];
    }
    
    public String getClientName()
    {
        return this.clientName;
    }
    
    public String getIPAddress()
    {
        return this.IPAddress;
    }
    
    public int getPort()
    {
        return this.port;
    }
    
    public String getFileName()
    {
        return this.fileName;
    }
    
    public String getPublicKey()
    {
        return this.publicKey;
    }
}
