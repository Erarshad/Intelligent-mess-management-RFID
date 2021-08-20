package sample;

import javafx.scene.control.Alert;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

public class export_to_excel{
    Alert a=new Alert(Alert.AlertType.CONFIRMATION);
   
   // public  static  String path="D:\\library\\javafx gui\\apps\\jit'smess\\excel_output\\";
   public  static  String path="C:\\mess management\\output\\";

    public void export(){
        try {
//            File file=new File("C:\\package\\output\\record.csv");
//            file.createNewFile();
            Date t=new Date();
            SimpleDateFormat date=new SimpleDateFormat("dd-MM-yyyy");



            PrintWriter pw= new PrintWriter(new File(path+"record"+date.format(t)+".csv"));
            StringBuilder sb=new StringBuilder();



            sqlite_connection obj_DB_Connection=new sqlite_connection();

            Connection  connection=obj_DB_Connection.connector();


            String query="select * from add_new";
            PreparedStatement ps=connection.prepareStatement(query);
            ResultSet rs=ps.executeQuery();
            //writing col name in csv file
            sb.append("uniqueid");
            sb.append(",");
            sb.append("fname");
            sb.append(",");

            sb.append("lname");
            sb.append(",");
            sb.append("Roll no");
            sb.append(",");
            sb.append("Course");
            sb.append(",");
            sb.append("year");
            sb.append(",");
            sb.append("branch");
            sb.append(",");
            sb.append("date of reg");
            sb.append(",");
            sb.append("time of reg");
            sb.append("\r\n");

            while(rs.next()){

                sb.append(rs.getString(1));
                sb.append(",");
                sb.append(rs.getString(2));
                sb.append(",");
                sb.append(rs.getString(3));
                sb.append(",");
                sb.append(rs.getString(4));
                sb.append(",");
                sb.append(rs.getString(6));
                sb.append(",");
                sb.append(rs.getString(7));
                sb.append(",");
                sb.append(rs.getString(8));
                sb.append(",");
                sb.append(rs.getString(9));
                sb.append(",");
                sb.append(rs.getString(10));


                sb.append("\r\n");
            }
            ///-----------------------now for wallet details


            sb.append("\r\n");
            sb.append("\r\n");
            String query2="select * from wallet";
            PreparedStatement ps2=connection.prepareStatement(query2);
            ResultSet rs2=ps2.executeQuery();
            sb.append("Uid");
            sb.append(",");
            sb.append("recharge mode");
            sb.append(",");
            sb.append("validity");
            sb.append(",");

            sb.append("recent recharge date");
            sb.append(",");
            sb.append("recent recharge time");
            sb.append("\r\n");



            while(rs2.next()){
                sb.append(rs2.getString(1));
                sb.append(",");
                sb.append(rs2.getString(2));
                sb.append(",");
                sb.append(rs2.getString(3)+"-"+rs2.getString(4));
                sb.append(",");
                sb.append(rs2.getString(6));
                sb.append(",");
                sb.append(rs2.getString(5));


                sb.append("\r\n");
            }




            //---
            pw.write(sb.toString());
            pw.close();


        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        ////------------------we need to also create scan history excel file
        try {
//            File file=new File("C:\\package\\output\\record.csv");
//            file.createNewFile();
            Date t=new Date();
            SimpleDateFormat date=new SimpleDateFormat("dd-MM-yyyy");



            PrintWriter pw= new PrintWriter(new File(path+"scan_history"+date.format(t)+".csv"));
            StringBuilder sb=new StringBuilder();



            sqlite_connection obj_DB_Connection=new sqlite_connection();

            Connection  connection=obj_DB_Connection.connector();


            String query="select * from scan_history";
            PreparedStatement ps=connection.prepareStatement(query);
            ResultSet rs=ps.executeQuery();
            //writing col name in csv file
            sb.append("serial no");
            sb.append(",");
            sb.append("uid");
            sb.append(",");

            sb.append("date");
            sb.append(",");
            sb.append("time");

            sb.append("\r\n");

            while(rs.next()){

                sb.append(rs.getString(1));
                sb.append(",");
                sb.append(rs.getString(2));
                sb.append(",");
                sb.append(rs.getString(3));
                sb.append(",");
                sb.append(rs.getString(4));


                sb.append("\r\n");
            }
            ///-----------------------now for wallet details




            //---
            pw.write(sb.toString());
            pw.close();
            System.out.println("finished");

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        //--- for today's attendence
        try {
//            File file=new File("C:\\package\\output\\record.csv");
//            file.createNewFile();
            Date t=new Date();
            SimpleDateFormat date=new SimpleDateFormat("dd-MM-yyyy");



            PrintWriter pw= new PrintWriter(new File(path+"today_attendence"+date.format(t)+".csv"));
            StringBuilder sb=new StringBuilder();



            sqlite_connection obj_DB_Connection=new sqlite_connection();

            Connection  connection=obj_DB_Connection.connector();


            String query="select * from scan_history where date=?";

            PreparedStatement ps=connection.prepareStatement(query);
            ps.setString(1,String.valueOf(date.format(t)));
            ResultSet rs=ps.executeQuery();
            //writing col name in csv file

            sb.append("uid");
            sb.append(",");

            sb.append("date");
            sb.append(",");
            sb.append("time ");

            sb.append("\r\n");

            while(rs.next()){

                sb.append(rs.getString(2));
                sb.append(",");
                sb.append(rs.getString(3));
                sb.append(",");
                sb.append(rs.getString(4));


                sb.append("\r\n");
            }
            ///-----------------------now for wallet details




            //---
            pw.write(sb.toString());
            pw.close();
            System.out.println("finished");

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        //----confirmation
        a.setAlertType(Alert.AlertType.CONFIRMATION);
        a.setContentText("excel file has been stored in "+path);

        // show the dialog
        a.show();






    }
























}












