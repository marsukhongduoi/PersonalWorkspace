/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.table.DefaultTableModel;
import lib.*;
/**
 *
 * @author Hoang
 */
public class History {
    private String sender, receiver, history, datesent;

    public History() {
    }

    public History(String sender, String receiver, String history, String datesent) {
        this.sender = sender;
        this.receiver = receiver;
        this.history = history;
        this.datesent = datesent;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getHistory() {
        return history;
    }

    public String getDatesent() {
        return datesent;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public void setDatesent(String datesent) {
        this.datesent = datesent;
    }
    
    public DefaultTableModel getTable(String sender, String receiver) throws SQLException {
        DefaultTableModel table = new DefaultTableModel();
        ConnectDB db = new ConnectDB();
        Connection conn = db.setConnection();
        Statement sta = conn.createStatement();
        String sql = "select * from history where ((sender = '" + sender + "' and receiver = '" + receiver + "') or (sender = '" + receiver + "' and receiver = '" + sender + "')) and datesent < '" + getCurrentDateTime() + "'";
        ResultSet rs = null;
        
        table.addColumn("sender");
        table.addColumn("receiver");
        table.addColumn("history");
        rs = sta.executeQuery(sql);
        while(rs.next()) {
            String s = rs.getString("sender");
            String r = rs.getString("receiver");
            String history = rs.getString("history");
            table.addRow(new Object[]{sender, receiver, history});
        }
        return table;
    }
    
    public boolean insertHistory() {
        boolean result = false;
        ConnectDB connDB = new ConnectDB();
        Connection conn = connDB.setConnection();
        try {
            String sql = "{call sp_historyinsert (?, ?, ?, ?)}";
            CallableStatement cstmt = conn.prepareCall(sql);
            cstmt.setEscapeProcessing(true);
            cstmt.setString(1, sender);
            cstmt.setString(2, receiver);
            cstmt.setString(3, history);
            cstmt.setString(4, datesent);
            int rows = cstmt.executeUpdate();
            if(rows > 0) {
                result = true;
            }
        } catch (Exception ex) {
            System.out.println("Error from insertHistory");
            System.out.println(ex);
        }
        return result;
    }
    
    String getCurrentDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }
    
    
}
