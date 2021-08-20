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

import javax.swing.plaf.synth.SynthDesktopIconUI;
import javax.swing.plaf.synth.SynthStyle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.invoke.LambdaConversionException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class search_database_controller  implements Initializable {
    @FXML
    private TextField scan;
    @FXML
    private Label name_label;
    @FXML
    private Label roll_label;
    @FXML
    private Label course_label;
    @FXML
    private Label branch_label;
    @FXML
    private Label year_label;
    @FXML
    private Label mdate_label;
    @FXML
    private Label mtime_label;
    @FXML
    private Label wallet_label;
    @FXML
    private Label wallet_rtime;
    @FXML
    private Label wallet_rdate;
    @FXML
    private ImageView imv;
    @FXML
    private Label rmod;



    private  String path_image;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


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

    public void search(ActionEvent actionEvent){
        Long l=new Long(scan.getText());

        gether_info_from_db(l);

    }
    sqlite_connection s=new sqlite_connection();
    Alert a = new Alert(Alert.AlertType.NONE);
    public   void gether_info_from_db(Long uid){
        String sql = "Select * from add_new where uid=?";

        try (Connection conn = s.connector();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, uid);


            ResultSet res=pstmt.executeQuery();

          name_label.setText(res.getString(2)+" "+res.getString(3));
          roll_label.setText(res.getString(4));

            course_label.setText(res.getString(6));
            branch_label.setText(res.getString(8));
            year_label.setText(String.valueOf(res.getInt(7)));
            mtime_label.setText(res.getString(10));
            mdate_label.setText(res.getString(9));
            path_image=res.getString(5);

            Image image=new Image(new FileInputStream(path_image));
            imv.setImage(image);



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

            wallet_label.setText(res.getString(3)+"-"+res.getString(4));
            wallet_rtime.setText(res.getString(5));
            wallet_rdate.setText(res.getString(6));
            rmod.setText(res.getString(2));


        } catch (SQLException e) {
            a.setAlertType(Alert.AlertType.WARNING);
            a.setContentText(e.getMessage());

            // show the dialog
            a.show();
            //  System.out.println(e.getMessage());
        }





    }



}
