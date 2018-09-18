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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    @FXML
    ComboBox cb_addnew;
    @FXML
    Button btn_accept;
    @FXML
    TextField an_account;
    @FXML
    PasswordField an_pwd;

    ObservableList oblist = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        FillComboBox();
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

    public static String md5(String msg) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(msg.getBytes());
            byte byteData[] = md.digest();
            //convert the byte to hex format method 1
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            return "";
        }
    }

    public void acceptHandle() {

        try {
            conn = function.connectDB();
            try (PreparedStatement command = conn.prepareStatement("Select * from Accounts where Username=? and Password=? and Role='False'")) {
                command.setString(1, an_account.getText());
                command.setString(2, md5(an_pwd.getText()));
                if (an_account.getText().isEmpty() || an_pwd.getText().isEmpty()) {
                    function.showAlert(
                            "Login Dialog",
                            "Login failed",
                            "Username or password must not be empty!",
                            Alert.AlertType.INFORMATION
                    );
                } else {
                    try (ResultSet rs = command.executeQuery()) {
                        if (rs.next()) {
                            try {
                                String getId = rs.getString("Id");
                                rs.close();
                                command.close();
                                conn.close();
                                conn1 = Function.connectDB();
                                PreparedStatement pst = conn1.prepareStatement("Select GId from Guests where GId=?");
                                pst.setString(1, getId);
                                ResultSet rs1 = pst.executeQuery();
//                                rs1.next();
                                System.out.println("12");
//                                System.out.println(rs1.getString("GId"));
                                while(rs1.next()){
                                    System.out.println("1");
                                }
                            } catch (Exception e) {
                                System.out.println(e);
                                e.printStackTrace();
                            }
                        } else {
                            function.showAlert(
                                    "Login Dialog",
                                    "Login failed",
                                    "The username or password does not exists!",
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

}
