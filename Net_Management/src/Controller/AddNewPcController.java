/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import static Controller.Login_Register.md5;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author User
 */
public class AddNewPcController implements Initializable {

    /**
     * Initializes the controller class.
     */
    Function function = new Function();
    Connection conn;
    Connection conn1;
    Connection conn2;

    @FXML
    ComboBox cb_addnew;
    @FXML
    Button btn_confirm, btn_add;
    @FXML
    TextField an_account, tf_role, tf_level, tf_start;
    @FXML
    PasswordField an_pwd;

    ObservableList oblist = FXCollections.observableArrayList();
    private int minute;
    private int hour;
    private int second;

    String Gid = new String();
    String account = new String();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        FillComboBox();
        start();
    }

    public void start() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            second = LocalDateTime.now().getSecond();
            minute = LocalDateTime.now().getMinute();
            hour = LocalDateTime.now().getHour();
            tf_start.setText(hour + ":" + (minute) + ":" + second);
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    public void FillComboBox() {
        try {
            conn = function.connectDB();
            String query = "select Id from PCs where status = 0";
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                oblist.add(rs.getString("Id"));
            }
            cb_addnew.setItems(oblist);

            rs.close();
            pst.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void confirmHandle() {
        try {
            conn = function.connectDB();
            try (PreparedStatement command = conn.prepareStatement("Select * from Accounts where Username=? and Password=? and Role='False'")) {
                command.setString(1, an_account.getText());
                command.setString(2, function.md5(an_pwd.getText()));
                if (an_account.getText().isEmpty() || an_pwd.getText().isEmpty()) {
                    function.showAlert(
                            "COMFIRMATION DIALOG",
                            "Access Denied",
                            "Username or password must not be empty!",
                            Alert.AlertType.INFORMATION
                    );
                } else {
                    try (ResultSet rs = command.executeQuery()) {

                        if (rs.next()) {
                            try {
                                btn_add.setDisable(false);
                                String getId = rs.getString("Id");
                                this.account = an_account.getText();
                                this.Gid = getId;
                                rs.close();
                                command.close();
                                conn1 = function.connectDB();
                                ResultSet rs1 = conn1.createStatement().executeQuery("Select * from Guests where GId =" + getId);
                                while(rs1.next()){
                                    tf_role.setText(rs1.getString("Role"));
                                    tf_level.setText(rs1.getString("Level"));
                                }
                                function.showAlert(
                                        "COMFIRMATION DIALOG",
                                        "",
                                        "Confirm successfully !",
                                        Alert.AlertType.INFORMATION
                                );
                                rs1.close();
                            } catch (Exception e) {
                                System.out.println(e);
                                e.printStackTrace();
                            }
                        } else {
                            function.showAlert(
                                    "COMFIRMATION DIALOG",
                                    "Access Denied",
                                    "The account or password does not exists!",
                                    Alert.AlertType.INFORMATION
                            );
                        }
                    }
                }
            }
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(Login_Register.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addNewHandle() {
        Stage current = (Stage) btn_add.getScene().getWindow();
        String start = LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute() + ":" + LocalDateTime.now().getSecond();

        try {
            conn2 = function.connectDB();
            if (cb_addnew.getValue() != null) {
                PreparedStatement command1 = conn2.prepareStatement("Select * from Pcs where Username=?");
                command1.setString(1, this.account);
                ResultSet rs1 = command1.executeQuery();
                if (rs1.next()) {
                    function.showAlert(
                            "Add new Dialog",
                            "Add new failed",
                            "The account already exists on device!",
                            Alert.AlertType.INFORMATION
                    );
                } else {

                    int pc = Integer.valueOf((String) cb_addnew.getValue());
                    PreparedStatement command = conn2.prepareStatement("Update PCs Set Username=?, GId=?, status=?, StartTime=? where Id=?");
                    command.setString(1, this.account);
                    command.setString(2, Gid);
                    command.setString(3, "true");
                    command.setString(4, function.getCurrentDate());
                    command.setInt(5, pc);
                    int i = command.executeUpdate();
                    if (i == 1) {
                        function.showAlert(
                                "Add new Dialog",
                                "Info",
                                "Add new successfully",
                                Alert.AlertType.INFORMATION
                        );
                        current.hide();
                    }
                }
            } else {
                function.showAlert(
                        "Add new Dialog",
                        "Add new failed",
                        "The PC does not exists!",
                        Alert.AlertType.INFORMATION
                );
            }
        } catch (SQLException e) {
            Logger.getLogger(Login_Register.class.getName()).log(Level.SEVERE, null, e);
        }
    }

}
