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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class addnew_controller  implements Initializable {

    @FXML
    private ComboBox<String> lts;
    @FXML
    private ComboBox<Integer> list_year;
    @FXML
    private ComboBox<String> list_branch;
    @FXML
    private Button c;
    @FXML
    private TextField uid;
    @FXML
    private TextField fname;
    @FXML
    private TextField lname;
    @FXML
    private TextField roll_no;





    ObservableList<String> ls= FXCollections.observableArrayList("B.Tech","B.pharm","M.B.A","Diploma in engineering","I.T.I","B.A","Diploma in media");
    ObservableList<Integer> ls2= FXCollections.observableArrayList(1,2,3,4);
    ObservableList<String> ls3= FXCollections.observableArrayList("CSE","ME","CE","EE","ECE","pharmacy","Draughtsman","Fitter","Electrician","Welder","Digital Photographer","Multimedia Animation and Special Effect","urdu","history","political science","education","english","hindi","Diploma in Journalism & Mass Communication","Diploma in Electronic Media & Film Production","other media course");



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lts.setItems(ls);// adding course in combobox
        list_year.setItems(ls2);
        list_branch.setItems(ls3);


    }
     String imagePath=null;
    public void file_chooser_for_image(ActionEvent activity) throws IOException {

        FileChooser fileselect=new FileChooser();
        fileselect.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All images" ,"*.*"),
                new FileChooser.ExtensionFilter("only jpeg" ,"*.jpg"),
                new FileChooser.ExtensionFilter("only png" ,"*.png")



        );
        File s = fileselect.showOpenDialog(null);
        if(s!=null){
           imagePath=String.valueOf(s.getAbsoluteFile());
           //after copy image path
            Long l=new Long(uid.getText());
          imagePath= copy_image(imagePath, l);


        }
        else{
            System.out.println("not found");
        }
    }
    public String copy_image(String imagePath,long uid) throws IOException {

        FileInputStream in = new FileInputStream(imagePath);

       //  String output_path="D:\\library\\javafx gui\\apps\\jit'smess\\photo\\"+uid+".jpg";
        //lateronforproduction
       String output_path="C:\\mess management\\photo\\"+uid+".jpg";
        FileOutputStream ou = new FileOutputStream(output_path);
        BufferedInputStream bin = new BufferedInputStream(in);
        BufferedOutputStream bou = new BufferedOutputStream(ou);
        int b=0;
        while(b!=-1){
            b=bin.read();
            bou.write(b);
        }
        bin.close();
        bou.close();
        return output_path;


    }
    //-----



     db_controller db=new db_controller();
    Date date = new Date();

    public  void add_in_database(ActionEvent activity){

        //@todo i want to add excetion handling
       //System.out.print( db.Isdb_connected());
        if(isNumeric(uid.getText()) &&!fname.getText().isEmpty() && !lname.getText().isEmpty() && imagePath!=null && !lts.getSelectionModel().isEmpty() && !list_branch.getSelectionModel().isEmpty() && !list_year.getSelectionModel().isEmpty()) {
            SimpleDateFormat formatter_date = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat formatter_time = new SimpleDateFormat("HH:mm:ss");
            Long l = new Long(uid.getText());
            if(!is_already_have_entry(l)) {
                insert(l, fname.getText(), lname.getText(), roll_no.getText(), imagePath, lts.getValue(), list_year.getValue(), list_branch.getValue(), formatter_date.format(date), formatter_time.format(date), activity);
            }else{
                update(l,  lts.getValue(), list_year.getValue(), list_branch.getValue(), activity);

            }


        }
        else {
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.CONFIRMATION);
            a.setContentText("Please Enter the Numeric value in Uid field or You may have empty field please filled them ");

            // show the dialog
            a.show();

        }





    }
    //for inserting data in db
    sqlite_connection s=new sqlite_connection();
    Alert a = new Alert(Alert.AlertType.NONE);
    public void insert(long uid,String fname,String lname,String roll_no,String image_path,String course,int year,String branch,String date_reg,String time_reg,ActionEvent actionEvent) {
        String sql = "INSERT INTO add_new(uid,fname,lname,roll_no,image_path,course,year,branch,date_reg,time_reg) VALUES(?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = s.connector();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, uid);
            pstmt.setString(2, fname);
            pstmt.setString(3, lname);
            pstmt.setString(4, roll_no);
            pstmt.setString(5, image_path);
            pstmt.setString(6, course);
            pstmt.setInt(7, year);
            pstmt.setString(8,branch);
            pstmt.setString(9,date_reg);
            pstmt.setString(10,time_reg);
            pstmt.executeUpdate();

            a.setAlertType(Alert.AlertType.CONFIRMATION);
            a.setContentText("Record of "+fname+" is successfully added ");

            // show the dialog
            a.show();
            //for recharge winow;
            rechargewindow(actionEvent);

        } catch (SQLException e) {
            a.setAlertType(Alert.AlertType.WARNING);
            a.setContentText(e.getMessage());

            // show the dialog
            a.show();
          //  System.out.println(e.getMessage());
        }
    }
    public static boolean isNumeric(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
    public  void rechargewindow(ActionEvent actionEvent){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("PAY ATTENTION");
        alert.setContentText("Are you willing to do recharge too");
//
        Optional<ButtonType> result=alert.showAndWait();
        if (result.isPresent() && result.get()==ButtonType.OK) {

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("recharge.fxml"));
                Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(loader.load());
                stage.setScene(scene);
            }catch (IOException io){
                io.printStackTrace();
            }


        }

    }
    //----------------either we have already entry or not
    public boolean is_already_have_entry(Long l){
        String sql2 = "Select * from add_new where uid=?";

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
    //---- it willl update the data
    public void update(long uid,String course,int year,String branch,ActionEvent actionEvent) {
     //  "" String sql = "INSERT INTO add_new(uid,fname,lname,roll_no,image_path,course,year,branch,date_reg,time_reg) VALUES(?,?,?,?,?,?,?,?,?,?)";
        try (Connection connection = s.connector();
             PreparedStatement statement =
                     connection.prepareStatement("UPDATE add_new SET course = ? , year=?,branch=? WHERE uid = ?")) {
            statement.setString(1,course);
            statement.setInt(2,year);
            statement.setString(3,branch);
            statement.setLong(4,uid);

            statement.executeUpdate();
            a.setAlertType(Alert.AlertType.CONFIRMATION);
            a.setContentText("record is updated for "+uid);

            // show the dialog
            a.show();


        } catch (SQLException e) {
            a.setAlertType(Alert.AlertType.WARNING);
            a.setContentText(e.getMessage());

            // show the dialog
            a.show();
        }


    }



}
