/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import lib.ConnectDB;

/**
 *
 * @author Hoang
 */
public class UserAccount {
    private String username, password;

    public UserAccount() {
    }

    public UserAccount(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public Hashtable getHashtable() throws SQLException {
        Hashtable hashtab = new Hashtable();
        ConnectDB db = new ConnectDB();
        Connection conn = db.setConnection();
        Statement sta = conn.createStatement();
        String sql = "select * from useraccount";
        ResultSet rs = null;
        
        rs = sta.executeQuery(sql);
        while(rs.next()) {
            String usrname = rs.getString("username");
            String pwd = rs.getString("password");
            
            hashtab.put(usrname, pwd);
        }
        return hashtab;
    }
    
    public boolean addNewAccount() {
        boolean result = false;
        ConnectDB connDB = new ConnectDB();
        Connection conn = connDB.setConnection();
        try {
            String sql = "{call sp_useraccinsert (?, ?)}";
            CallableStatement cstmt = conn.prepareCall(sql);
            cstmt.setEscapeProcessing(true);
            cstmt.setString(1, username);
            cstmt.setString(2, password);
            int rows = cstmt.executeUpdate();
            if(rows > 0) {
                result = true;
            }
        } catch (Exception ex) {
            System.out.println("Error from addNewAccount");
            System.out.println(ex);
        }
        return result;
    }
    
    public boolean updatePassword() {
        boolean result = false;
        ConnectDB connDB = new ConnectDB();
        Connection conn = connDB.setConnection();
        try {
            String sql = "{call sp_useraccupdate (?, ?)}";
            CallableStatement cstmt = conn.prepareCall(sql);
            cstmt.setEscapeProcessing(true);
            cstmt.setString(1, username);
            cstmt.setString(2, password);
            int rows = cstmt.executeUpdate();
            if(rows > 0) {
                result = true;
            }
        } catch (Exception ex) {
            System.out.println("Error from updatePassword");
            System.out.println(ex);
        }
        return result;
    }
    
    public boolean deleteAccount() {
        boolean result = false;
        ConnectDB connDB = new ConnectDB();
        Connection conn = connDB.setConnection();
        try {
            String sql = "{call sp_useraccdelete (?, ?)}";
            CallableStatement cstmt = conn.prepareCall(sql);
            cstmt.setEscapeProcessing(true);
            cstmt.setString(1, username);
            int rows = cstmt.executeUpdate();
            if(rows > 0) {
                result = true;
            }
        } catch (Exception ex) {
            System.out.println("Error from deleteAccount");
            System.out.println(ex);
        }
        return result;
    }
}
