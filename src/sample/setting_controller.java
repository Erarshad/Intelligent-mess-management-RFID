package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class setting_controller  implements Initializable {
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private TextField from_bf_time;
    @FXML
    private TextField to_bf_time;
    @FXML
    private TextField from_lunch_time;
    @FXML
    private TextField to_lunch_time;
    @FXML
    private TextField from_dinner_time;
    @FXML
    private TextField to_dinner_time;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gether_info_from_db();

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
    sqlite_connection s=new sqlite_connection();
    Alert a = new Alert(Alert.AlertType.NONE);
    public void gether_info_from_db(){
        String sql = "Select * from auth where id=?";

        try (Connection conn = s.connector();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, 1);


            ResultSet res=pstmt.executeQuery();
            username.setText(res.getString(2));
            password.setText(res.getString(3));








        } catch (SQLException e) {
            a.setAlertType(Alert.AlertType.WARNING);
            a.setContentText(e.getMessage());

            // show the dialog
            a.show();
            //  System.out.println(e.getMessage());
        }
        //second query
        String sql2 = "Select * from time where id=?";

        try (Connection conn = s.connector();
             PreparedStatement pstmt = conn.prepareStatement(sql2)) {
            pstmt.setInt(1, 1);


            ResultSet res=pstmt.executeQuery();
            from_bf_time.setText(res.getString(2));
            to_bf_time.setText(res.getString(3));
            from_lunch_time.setText(res.getString(4));
            to_lunch_time.setText(res.getString(5));
            from_dinner_time.setText(res.getString(6));
            to_dinner_time.setText(res.getString(7));









        } catch (SQLException e) {
            a.setAlertType(Alert.AlertType.WARNING);
            a.setContentText(e.getMessage());

            // show the dialog
            a.show();
            //  System.out.println(e.getMessage());
        }





    }

    //---for update
    public void updatesettings(String username,String pass,String from_bf_time,String to_bf_time,String from_lunch_time,String to_lunch_time,String from_dinner_time,String to_dinner_time) throws IOException{
            try (Connection connection = s.connector();
                 PreparedStatement statement =
                         connection.prepareStatement("UPDATE  auth set username = ?,password=? WHERE ID = 1")) {
                statement.setString(1,username);
                statement.setString(2,pass);
                statement.executeUpdate();



            } catch (SQLException e) {
                a.setAlertType(Alert.AlertType.WARNING);
                a.setContentText(e.getMessage());

                // show the dialog
                a.show();

            }
            //second query

        try (Connection connection = s.connector();
             PreparedStatement statement =
                     connection.prepareStatement("UPDATE time set from_breakfast = ?,to_breakfast = ?,from_lunch = ?,to_lunch=?,from_dinner=?,to_dinner=? where id=1")) {
            statement.setString(1,from_bf_time);
            statement.setString(2, to_bf_time);
            statement.setString(3, from_lunch_time);
            statement.setString(4, to_lunch_time);
            statement.setString(5, from_dinner_time);
            statement.setString(6, to_dinner_time);

            statement.executeUpdate();


        } catch (SQLException e) {
            a.setAlertType(Alert.AlertType.WARNING);
            a.setContentText(e.getMessage());

            // show the dialog
            a.show();
        }




    }






    public void update_data_setting(ActionEvent actionEvent) throws IOException {

            updatesettings(username.getText(),password.getText(),from_bf_time.getText(),to_bf_time.getText(),from_lunch_time.getText(),to_lunch_time.getText(),from_dinner_time.getText(),to_dinner_time.getText());
            a.setAlertType(Alert.AlertType.WARNING);
            a.setContentText("data updated_successfully");

            // show the dialog
            a.show();



    }






}
