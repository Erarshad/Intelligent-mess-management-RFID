package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.scene.media.*;
import javazoom.jl.player.Player;


public class scanner_controller  implements Initializable {
    @FXML
     private Label name;
    @FXML
    private  Label roll;
    @FXML
    private Label course;
    @FXML
    private  Label branch;
    @FXML
    private Label year;
    @FXML
    private  Label recharge_type;
    @FXML
    private Label session;
    @FXML
    private Label validity_recharge;
    @FXML
    private  Label remainingamount;
    @FXML
    private Label allowed;
    @FXML
    private TextField scan;
    @FXML
    private ImageView img;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
     manage_session();

    }
    public void back_2_menu(ActionEvent activity){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menu_page.fxml"));
            Stage stage = (Stage) ((Node)activity.getSource()).getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
        }catch (IOException io){
            io.printStackTrace();
        }

    }

    public void automation_input()  {
        Long long_uid = new Long(scan.getText());
        if(check_person_has_recharge_entry(long_uid)) {
            if (isNumeric(scan.getText())) {

                if (scan.getText().length() >= 10) {
                    Long l = new Long(scan.getText());


                    gether_info_from_db(l);
                    //----
                    scan.clear();
                    //---to clear scan search bar for next scan of uid from rfid card
                    //  && !check_time_based_scan_history(l)
                    if (ischeck_person_has_scan_three_time(l) && iseligiblealready(l) && !check_time_based_scan_history(l) && manage_session()) {
                        data_fill_scan_history(l);

                    }


                }
            } else {
                a.setAlertType(Alert.AlertType.WARNING);
                a.setContentText("please input numeric value in uid field");

                // show the dialog
                a.show();

            }

        }
        else{
            a.setAlertType(Alert.AlertType.WARNING);
            a.setContentText("THIS UID DONT HAVE ANY RECHARGE ENTRY FROM THE REGISTRATION TIME");

            // show the dialog
            a.show();

        }

    }
    public void search(ActionEvent actionEvent)  {
        automation_input();
    }

    sqlite_connection s=new sqlite_connection();
    Alert a = new Alert(Alert.AlertType.NONE);
    public   void gether_info_from_db(Long uid)  {
        String sql = "Select * from add_new where uid=?";

        try (Connection conn = s.connector();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, uid);


            ResultSet res=pstmt.executeQuery();

            name.setText(res.getString(2)+" "+res.getString(3));
            roll.setText(res.getString(4));
            Image image=new Image( new FileInputStream(res.getString(5)));
            img.setImage(image);

            course.setText(res.getString(6));
            branch.setText(res.getString(8));
            year.setText(String.valueOf(res.getInt(7)));





        } catch (SQLException | FileNotFoundException e) {
            a.setAlertType(Alert.AlertType.WARNING);
            a.setContentText(e.getMessage());

            // show the dialog
            a.show();
            //  System.out.println(e.getMessage());
        }
        //----sql table 2
        String sql2 = "Select * from wallet where uid=?";

        try (Connection conn = s.connector();
             PreparedStatement pstmt = conn.prepareStatement(sql2)) {
            pstmt.setLong(1, uid);


            ResultSet res=pstmt.executeQuery();
             validity_recharge.setText(res.getString(3)+" - "+res.getString(4));
             recharge_type.setText(res.getString(2));






        } catch (SQLException e) {
            a.setAlertType(Alert.AlertType.WARNING);
            a.setContentText(e.getMessage());

            // show the dialog
            a.show();
            //  System.out.println(e.getMessage());
        }
        //handle_reduction_amount_session();
        manage_status_of_allowance();






    }


    //-------------------
    public boolean  manage_session() {
        String sql2 = "Select * from time where id=1";
        Date  from_breakfast=null;
        Date  to_breakfast=null;
        Date from_lunch=null;
        Date to_lunch=null;
        Date from_dinner=null;
        Date to_dinner=null;


         try (Connection conn = s.connector();
              PreparedStatement pstmt = conn.prepareStatement(sql2)) {



             ResultSet res=pstmt.executeQuery();
             from_breakfast= new SimpleDateFormat("HH:mm").parse(res.getString(2));
             to_breakfast=new SimpleDateFormat("HH:mm").parse(res.getString(3));
             from_lunch=new SimpleDateFormat("HH:mm").parse(res.getString(4));
             to_lunch=new SimpleDateFormat("HH:mm").parse(res.getString(5));
             from_dinner=new SimpleDateFormat("HH:mm").parse(res.getString(6));
             to_dinner=new SimpleDateFormat("HH:mm").parse(res.getString(7));






         } catch (SQLException | ParseException e) {
             a.setAlertType(Alert.AlertType.WARNING);
             a.setContentText(e.getMessage());

             // show the dialog
             a.show();
             //  System.out.println(e.getMessage());
         }




        //------------------------------------------------------------
        Date date = new Date();
        SimpleDateFormat formatter_time = new SimpleDateFormat("HH:mm");
        Date current_time= null;
        try {
            current_time = new SimpleDateFormat("HH:mm").parse(formatter_time.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }


//        int hour=Integer.valueOf(formatter_time.format(date));
//        if(date2.compareTo(date1)==0){
//            //both dates are equal
//            return  true;
//        }
//        else if(date2.compareTo(date1)>0){
//            //date 2 is greater than date1
//            return true;
//
//
//        }
//        else if(date2.compareTo(date1)==0 && hour>=24){
//            return false;
//
//        }
//        else{
//            //date 2 is smaller than date1
//            return false;
//        }





        if(current_time.compareTo(from_breakfast)==0|| current_time.compareTo(from_breakfast)>0 && current_time.compareTo(to_breakfast)<0||current_time.compareTo(to_breakfast)==0){
            session.setText("Break-Fast");
            return true;
        }
        else if(current_time.compareTo(from_lunch)==0|| current_time.compareTo(from_lunch)>0 && current_time.compareTo(to_lunch)<0||current_time.compareTo(to_lunch)==0){
            session.setText("Lunch");
            return true;

        } else if(current_time.compareTo(from_dinner)==0|| current_time.compareTo(from_dinner)>0 && current_time.compareTo(to_dinner)<0||current_time.compareTo(to_dinner)==0){
            session.setText("dinner");
            return  true;
        }
        else{
            session.setText("no session");
            return false;
        }



    }

    ///handle reduction of amount
//    public void handle_reduction_amount_session(){
//
//        Date date = new Date();
//        SimpleDateFormat formatter_time = new SimpleDateFormat("HH");
//        int hour=Integer.valueOf(formatter_time.format(date));
//
//        String sql1 = "Select * from session_cost where id=1";
//
//        try (Connection conn = s.connector();
//             PreparedStatement pstmt = conn.prepareStatement(sql1)) {
//
//
//
//            ResultSet res=pstmt.executeQuery();
//            if(hour>=7 && hour<=9){
//                session_cost=res.getInt(2);
//                cs.setText(String.valueOf(res.getInt(2)));
//            }
//            else if(hour>=12 && hour<=14){
//                session_cost=res.getInt(3);
//                cs.setText(String.valueOf(res.getInt(3)));
//
//            }
//            else if(hour>=19 && hour<=20){
//                session_cost=res.getInt(4);
//                cs.setText(String.valueOf(res.getInt(4)));
//            }
//            else{
//                session_cost=0;
//                cs.setText("no session");
//            }
//
//
//
//
//        } catch (SQLException e) {
//            a.setAlertType(Alert.AlertType.WARNING);
//            a.setContentText(e.getMessage());
//
//            // show the dialog
//            a.show();
//            //  System.out.println(e.getMessage());
//        }
//
//
//
//
//    }
    //------
    public void manage_status_of_allowance()  {
        Long l=new Long(scan.getText());
        //&& !check_time_based_scan_history(l)
        if(ischeck_person_has_scan_three_time(l ) && iseligiblealready(l)&& !check_time_based_scan_history(l)&& manage_session()){

            allowed.setText("you are allowed");

            // amount will deduct when it is allowed else not
          //  deduct_amount();




        }
        else {
            allowed.setText("you are not allowed");
            //manage_remaininga_amount( l);
            buzzer();


        }
    }
//   public boolean  deduct_amount(){
//        if(amount_in_wallet>=session_cost){
//            int updated_amount=amount_in_wallet-session_cost;
//            Long l=new Long(scan.getText());
//            update_wallet(updated_amount,l);
//
//
//
//
//
//            return true;
//
//        }
//        return false;
//    }
    //--update the wallet_value
//    public void update_wallet(int amount,Long uid){
//        try (Connection connection = s.connector();
//             PreparedStatement statement =
//                     connection.prepareStatement("UPDATE wallet SET amount = ? WHERE uid=?")) {
//            statement.setInt(1,amount);
//            statement.setLong(2,uid);
//            statement.executeUpdate();
//            manage_remaininga_amount(uid);
//
//
//
//        } catch (SQLException e) {
//            a.setAlertType(Alert.AlertType.WARNING);
//            a.setContentText(e.getMessage());
//
//            // show the dialog
//            a.show();
//
//        }
//    }
//     public void manage_remaininga_amount(long uid){
//         String sql2 = "Select * from wallet where uid=?";
//
//         try (Connection conn = s.connector();
//              PreparedStatement pstmt = conn.prepareStatement(sql2)) {
//             pstmt.setLong(1, uid);
//
//
//             ResultSet res=pstmt.executeQuery();
//
//
//             remainingamount.setText(res.getString(2));
//
//
//
//         } catch (SQLException e) {
//             a.setAlertType(Alert.AlertType.WARNING);
//             a.setContentText(e.getMessage());
//
//             // show the dialog
//             a.show();
//             //  System.out.println(e.getMessage());
//         }
//     }
//

     //-=-----

    public boolean ischeck_person_has_scan_three_time(long uid){
        int counter=1;
        String sql2 = "Select * from scan_history where uid=? and date=?";

        try (Connection conn = s.connector();
             PreparedStatement pstmt = conn.prepareStatement(sql2)) {
            pstmt.setLong(1, uid);
            Date date=new Date();
            SimpleDateFormat formatter_date = new SimpleDateFormat("dd-MM-yyyy");

            pstmt.setString(2,formatter_date.format(date));


            ResultSet res=pstmt.executeQuery();
            while(res.next()) {
                counter++;
            }





        } catch (SQLException e) {
            a.setAlertType(Alert.AlertType.WARNING);
            a.setContentText(e.getMessage());

            // show the dialog
            a.show();
            //  System.out.println(e.getMessage());
        }
        System.out.println(counter);
        if(counter<=3){
            return true;

        }

        return false;

    }

    //-----data fill in scan history
    public  void data_fill_scan_history(Long l){

        Date date=new Date();
        SimpleDateFormat formatter_date = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat formatter_time = new SimpleDateFormat("HH:mm");

        String sql = "INSERT INTO scan_history(uid,date,time) VALUES(?,?,?)";

        try (Connection conn = s.connector();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, l);
            pstmt.setString(2,formatter_date.format(date));
            pstmt.setString(3,formatter_time.format(date));


            pstmt.executeUpdate();



        } catch (SQLException e) {
            a.setAlertType(Alert.AlertType.WARNING);
            a.setContentText(e.getMessage());

            // show the dialog
            a.show();
            //  System.out.println(e.getMessage());
        }

    }

    //------

    public boolean check_time_based_scan_history(Long uid){

        String sql2 = "Select * from scan_history where uid=? and date=?";

        try (Connection conn = s.connector();
             PreparedStatement pstmt = conn.prepareStatement(sql2)) {
            pstmt.setLong(1, uid);
            Date date=new Date();
            SimpleDateFormat formatter_date = new SimpleDateFormat("dd-MM-yyyy");


            pstmt.setString(2,formatter_date.format(date));


            ResultSet res=pstmt.executeQuery();
            StringBuilder times = new StringBuilder ();
//            StringBuilder lids = new StringBuilder ();

            while (res.next()) {

                // we will get all the row 's data
                times.append(res.getString(4)).append(",");
                // lids.append(res.getString(2)).append(" ");

            }
            SimpleDateFormat formatter_time = new SimpleDateFormat("HH");
            String t[]=times.toString().split(",");


            String  cur_hour = formatter_time.format(date);

            int  time_data=0;
            for(int i=0;i<t.length;i++){

                try{
                    //it will just take or slice the only hour part
                   time_data=Integer.parseInt(t[i].substring(0,2));


                }
                catch (Exception e){

                    time_data=0;
                }


                if(time_data==Integer.parseInt(cur_hour)){
                    return true;
                }
                else if(time_data+1==Integer.parseInt(cur_hour)){
                    return true;
                }
                else {
                    return false;
                }








            }






        } catch (SQLException  e) {
            a.setAlertType(Alert.AlertType.WARNING);
            a.setContentText(e.getMessage());

            // show the dialog
            a.show();
            //  System.out.println(e.getMessage());
        }



        return false;

    }
    public static boolean isNumeric(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
    //---

    public void  buzzer(){
     //   String path="D:\\library\\javafx gui\\apps\\jit'smess\\alarm\\alarm.mp3";
       String path="C:\\mess management\\alarm\\alarm.mp3";
        try{
            FileInputStream j=new FileInputStream(path);
            Player k=new Player(j);

            k.play();

        }
        catch (Exception e){
            e.printStackTrace();
        }



    }

    public boolean iseligiblealready(Long uid) {


        String sql1 = "Select * from wallet where uid=?";

        try (Connection conn = s.connector();
             PreparedStatement pstmt = conn.prepareStatement(sql1)) {
            pstmt.setLong(1, uid);


            ResultSet res=pstmt.executeQuery();

            String valid_till=res.getString(4);
            Date current_date=new Date();
            SimpleDateFormat format=new SimpleDateFormat("dd-MM-yyyy");
            Date date1=new SimpleDateFormat("dd-MM-yyyy").parse(format.format(current_date));
            Date date2 =new SimpleDateFormat("dd-MM-yyyy").parse(valid_till);
            SimpleDateFormat current_hour=new SimpleDateFormat("HH");


            int hour=Integer.parseInt(current_hour.format(current_date));




            if(date2.compareTo(date1)==0){
                //both dates are equal
                return  true;
            }
            else if(date2.compareTo(date1)>0){
                //date 2 is greater than date1
                return true;


            }
            else if(date2.compareTo(date1)==0 && hour>=24){
                return false;

            }
            else{
                //date 2 is smaller than date1
                return false;
            }









        } catch (SQLException | ParseException e) {
            a.setAlertType(Alert.AlertType.WARNING);
            a.setContentText(e.getMessage());

            // show the dialog
            a.show();
            //  System.out.println(e.getMessage());
        }



        return false;



    }
    //------------------check person has at least first recharge or not
    public boolean check_person_has_recharge_entry(Long uid){

        String sql2 = "Select * from wallet where uid=?";

        try (Connection conn = s.connector();
             PreparedStatement pstmt = conn.prepareStatement(sql2)) {
            pstmt.setLong(1, uid);





            ResultSet res=pstmt.executeQuery();
            if(res.next()){
                return true;
            }
            else {
                return false;
            }




        } catch (SQLException e) {
            a.setAlertType(Alert.AlertType.WARNING);
            a.setContentText(e.getMessage());

            // show the dialog
            a.show();
            //  System.out.println(e.getMessage());
        }


        return false;


    }












}
