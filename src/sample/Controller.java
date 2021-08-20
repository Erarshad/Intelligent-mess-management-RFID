package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import  javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller implements Initializable{
    // lateron we will do with database
    @FXML
    private TextField username;
    @FXML
    private PasswordField ps;

     private  String user;
     private  String pw;
      // list of course





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        get_ps();



    }


// it is for handling menu view
    public void getusername(ActionEvent event){
        if(username.getText().equals(user) && ps.getText().equals(pw)) {
            //System.out.println(username.getText() +"~--"+ pword.getText());
            handle_menu_view(event);


        }
        else  if(username.getText().isEmpty()){
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("your one or more field is empty");

            // show the dialog
            a.show();
            System.out.println("empty");
        }
        else{
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setAlertType(Alert.AlertType.WARNING);
            a.setContentText("you are not authenticated to get access");

            // show the dialog
            a.show();
            System.out.println("ur not authenticate");
        }
    }

    public  void handle_menu_view(ActionEvent actionEvent){

        // it will open new dialog box
//        try {
//            FXMLLoader fxmlLoader = new FXMLLoader();
//            fxmlLoader.setLocation(getClass().getResource("menu_page.fxml"));
//            /*
//             * if "fx:controller" is not set in fxml
//             * fxmlLoader.setController(NewWindowController);
//             */
//            Scene scene = new Scene(fxmlLoader.load(), 300, 275);
//            Stage stage = new Stage();
//            stage.setTitle("New Window");
//            stage.setScene(scene);
//            stage.show();
//        } catch (IOException e) {
//            Logger logger = Logger.getLogger(getClass().getName());
//            logger.log(Level.SEVERE, "Failed to create new Window.", e);
//        }
        //-----------------------------------------------------------------------------

        //it is for changing page in single window
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menu_page.fxml"));
            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
        }catch (IOException io){
            io.printStackTrace();
        }



    }
    public void open_add_new(ActionEvent activity){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("add_new.fxml"));
            /*
             * if "fx:controller" is not set in fxml
             * fxmlLoader.setController(NewWindowController);
             */
            Scene scene = new Scene(fxmlLoader.load(), 610, 400);
            Stage stage = new Stage();
            stage.setTitle("Add new student");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }

    }

    public void recharge_wallet(ActionEvent activity){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("recharge.fxml"));
            Stage stage = (Stage) ((Node)activity.getSource()).getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
        }catch (IOException io){
            io.printStackTrace();
        }

    }
    public void search_data(ActionEvent activity){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("search_db.fxml"));
            Stage stage = (Stage) ((Node)activity.getSource()).getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
        }catch (IOException io){
            io.printStackTrace();
        }

    }

    public void reductor(ActionEvent activity){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("scan_to_accept.fxml"));
            Stage stage = (Stage) ((Node)activity.getSource()).getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
        }catch (IOException io){
            io.printStackTrace();
        }

    }
    public void settings(ActionEvent activity){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("settings.fxml"));
            Stage stage = (Stage) ((Node)activity.getSource()).getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
        }catch (IOException io){
            io.printStackTrace();
        }

    }
    ///----get password from db

    public void get_ps(){
        sqlite_connection s=new sqlite_connection();
        String sql2 = "Select * from auth where id=1";

        try (Connection conn = s.connector();
             PreparedStatement pstmt = conn.prepareStatement(sql2)) {
            ResultSet res=pstmt.executeQuery();
            user=res.getString(2);
            pw=res.getString(3);







        } catch (SQLException e) {
            Alert a=new Alert(Alert.AlertType.CONFIRMATION);
            a.setAlertType(Alert.AlertType.WARNING);
            a.setContentText(e.getMessage());

            // show the dialog
            a.show();
            //  System.out.println(e.getMessage());
        }



    }
    //---handle export csv


    public void exxport2excel(ActionEvent activity ){
        export_to_excel csv=new export_to_excel();
        csv.export();

    }







}
