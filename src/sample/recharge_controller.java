package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;


public class recharge_controller implements Initializable {
    @FXML
    private TextField uid;
    @FXML
    private ComboBox<String> recharge_type;
    //-.---- i need to remove this
//    private TextField amount;



    ObservableList<String> ls= FXCollections.observableArrayList("Monthly recharge","yearly recharge");


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
      recharge_type.setItems(ls);


    }

    public void go_back_to_menu(ActionEvent activity){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menu_page.fxml"));
            Stage stage = (Stage) ((Node)activity.getSource()).getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
        }catch (IOException io){
            io.printStackTrace();
        }
    }

    public  void  do_recharge_wallet(ActionEvent activity){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
       alert.setTitle("PAY ATTENTION");
        alert.setContentText("Are you sure to do transaction to perform recharge of wallet");
//
        Optional<ButtonType> result=alert.showAndWait();

        if (result.isPresent() && result.get()==ButtonType.OK) {
            if(!uid.getText().isEmpty() && !recharge_type.getSelectionModel().isEmpty()){
                //System.out.print( db.Isdb_connected());

                // to do i need to check in db if data already exist we will update that column
                Date date = new Date();
                SimpleDateFormat formatter_date = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat formatter_time = new SimpleDateFormat("HH:mm:ss");
                Long l=new Long(uid.getText());

                if(isNumeric(uid.getText()) && !isalready(l)) {
                    if(recharge_type.getValue().equals("Monthly recharge")){
                        SimpleDateFormat for_cur_month = new SimpleDateFormat("MM");
                        int month=Integer.parseInt(for_cur_month.format(date));
                        SimpleDateFormat for_cur_year = new SimpleDateFormat("yyyy");
                        int year=Integer.parseInt(for_cur_year.format(date));

                        get_month_in_last_date gminl=new get_month_in_last_date(month,year);
                        int last_date=gminl.get_last_date(month);

                      //  LocalDate futureDate = LocalDate.now().plusMonths(1);
                        String valid_month=last_date+"-"+month+"-"+year;



                        do_transaction_in_wallet(l,recharge_type.getValue(),String.valueOf(formatter_date.format(date)),valid_month,String.valueOf(formatter_date.format(date)), String.valueOf(formatter_time.format(date)));

                    }
                    else if(recharge_type.getValue().equals("yearly recharge")){
//
//                        LocalDate futureDate = LocalDate.now().plusYears(1);
//                        String valid_month=futureDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                        //----
                        SimpleDateFormat for_cur_month = new SimpleDateFormat("MM");
                        int month=Integer.parseInt(for_cur_month.format(date));
                        SimpleDateFormat for_cur_year = new SimpleDateFormat("yyyy");
                        int year=Integer.parseInt(for_cur_year.format(date));
                       /*\

                       * for yearly recharge july to 30 june
                       * */

                        String valid_month="";
                        if(month>6){
                            valid_month="30-6-"+year+1;

                        }
                        else{
                            valid_month="30-6-"+year;

                        }




                        do_transaction_in_wallet(l,recharge_type.getValue(),String.valueOf(formatter_date.format(date)),valid_month,String.valueOf(formatter_date.format(date)), String.valueOf(formatter_time.format(date)));



                    }


                }else{
                    //agar numeric ni hai
                    a.setAlertType(Alert.AlertType.WARNING);
                    a.setContentText("please input numeric value in uid field or it may can reason that you are doing recharge before ending quota of last recharge");

                    // show the dialog
                    a.show();
                }
            }
            else {
                Alert a = new Alert(Alert.AlertType.NONE);
                a.setAlertType(Alert.AlertType.ERROR);
                a.setTitle("PAY ATTENTION");
                a.setContentText("your one or more field is empty");

                // show the dialog
                a.show();

            }

        }



    }
    sqlite_connection s=new sqlite_connection();
    Alert a = new Alert(Alert.AlertType.NONE);

    public void  do_transaction_in_wallet(Long uid,String recharge_type,String valid_from,String valid_till,String date,String time){
        if(!is_already_have_recharge_entry(uid)) {
            String sql = "INSERT INTO wallet(uid,recharge_type,valid_from,valid_till,time,date) VALUES(?,?,?,?,?,?)";

            try (Connection conn = s.connector();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setLong(1, uid);

                pstmt.setString(2, recharge_type);
                pstmt.setString(3, valid_from);
                pstmt.setString(4, valid_till);
                pstmt.setString(5, time);
                pstmt.setString(6, date);



                pstmt.executeUpdate();

                a.setAlertType(Alert.AlertType.CONFIRMATION);
                a.setContentText(recharge_type+" Recharge has done for " + uid+" it is valid from "+valid_from+"to "+valid_till);

                // show the dialog
                a.show();
                reciept_output(uid,date,recharge_type,valid_from,valid_till,time);

            } catch (SQLException e) {
                a.setAlertType(Alert.AlertType.WARNING);
                a.setContentText(e.getMessage());


                // show the dialog
                a.show();
                //  System.out.println(e.getMessage());
            }
        }
        else{



            try (Connection connection = s.connector();
                 PreparedStatement statement =
                         connection.prepareStatement("UPDATE wallet SET recharge_type = ?,valid_from=?,valid_till=?  WHERE uid = ?")) {
                statement.setString(1,recharge_type);
                statement.setString(2,valid_from);
                statement.setString(3,valid_till);
                statement.setLong(4,uid);

                statement.executeUpdate();
                a.setAlertType(Alert.AlertType.CONFIRMATION);
                a.setContentText(recharge_type+" Recharge has done for " + uid +"it is valid from "+valid_from+"to "+valid_till);

                // show the dialog
                a.show();

                reciept_output(uid,date,recharge_type,valid_from,valid_till,time);
            } catch (SQLException e) {
                a.setAlertType(Alert.AlertType.WARNING);
                a.setContentText(e.getMessage());

                // show the dialog
                a.show();
            }









        }

    }
    public boolean is_already_have_recharge_entry(Long l){
        String sql2 = "Select * from wallet where uid=?";

        try (Connection conn = s.connector();
             PreparedStatement pstmt = conn.prepareStatement(sql2)) {
            pstmt.setLong(1, l);


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
    //----
    public static boolean isNumeric(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
    public boolean isalready(Long uid) {


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


            if(date2.compareTo(date1)==0){
                //both dates are equal
                return  true;
            }
            else if(date2.compareTo(date1)>0){
                //date 2 is greater than date1
                return true;


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
     public void reciept_output(Long uid,String date,String recharge_mod,String validto,String valid_till,String curtime)  {
      //  String path="D:\\library\\javafx gui\\apps\\jit'smess\\reciept_output\\";
         String path="C:\\mess management\\reciept_output\\";


         File f=new File(path+"reciept"+uid+date+".txt");
         try {
             f.createNewFile();
         }
         catch (Exception e){
             a.setAlertType(Alert.AlertType.ERROR);
             a.setContentText( e.getMessage());
             a.show();

         }

         try (BufferedWriter bw = new BufferedWriter(new FileWriter(f,false/*a bool to append file we can also remove bool*/))) {
             bw.write("   RECEIPT OF RECHARGE ");
             bw.write("\n");
             bw.write("Unique Id : "+uid);
             bw.write("\n");
             bw.write("Recharge type:  "+recharge_mod);
             bw.write("\n");
             bw.write("Recharge valid:  "+validto+" to "+valid_till);
             bw.write("\n");
             bw.write("time of recharge: "+curtime);
             bw.write("\n");
             bw.write("date  of recharge: "+date);
             bw.write("\n");
             bw.write("Sign of mess vendor:........................................ ");
             bw.write("\n");
             bw.write("computer generated receipt  ");
             bw.write("\n");
             bw.write("\n");
             bw.write(" .................................");
             bw.write("#######MESS ERP 1.0 ######################## @RSHAD");






             a.setAlertType(Alert.AlertType.CONFIRMATION);
             a.setContentText("printable reciept can be found at "+path);
             a.show();
         } catch (IOException e) {
             a.setAlertType(Alert.AlertType.ERROR);
             a.setContentText( e.getMessage());
             a.show();

         }
     }




}
