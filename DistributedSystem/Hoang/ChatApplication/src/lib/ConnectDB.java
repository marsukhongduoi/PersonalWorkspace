/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lib;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Hoang
 */
public class ConnectDB {
    public Connection setConnection() {
        Connection conn = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String username = "sa";
            String password = "123";
            conn = DriverManager.getConnection("jdbc:sqlserver://localhost;databaseName=ChatAppDB", username, password);
        }catch(Exception ex) {
            System.out.print("Error while establish connection: ");
            System.out.println(ex);
        }
        return conn;
    }
}
