package sample;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;

public class sqlite_connection {
    public static Connection connector(){
        try{
            Class.forName("org.sqlite.JDBC");
           //Connection conn= DriverManager.getConnection("jdbc:sqlite:mess_management_arshad.db");
            //for jarfile
            Connection conn= DriverManager.getConnection("jdbc:sqlite:C:\\mess management\\database\\mess_management_arshad.db");
            return  conn;

        }

        catch (Exception e){
            return null;

        }

    }
}
