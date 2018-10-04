/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class RegisterController implements Initializable {

    //kiem tra tinh hop le cua du lieu
    private static final String USERNAME_PATTERN = "^[a-z0-9]{5,18}$";
    private static final String PASSWORD_PATTERN = "^[a-z0-9]{6,18}$";
    private static final String PHONE_PATTERN = "^[0-9]{10,11}";
    private static final String MONEY_PATTERN = "^[0-9]{4,10}";

    Function function = new Function();
    Connection conn;

//    public RegisterController() {
//        user_pattern = Pattern.compile(USERNAME_PATTERN);
//        password_pattern = Pattern.compile(PASSWORD_PATTERN);
//        phone_pattern = Pattern.compile(PHONE_PATTERN);
//        money_pattern = Pattern.compile(MONEY_PATTERN);
//    }
    public boolean validate(String pattern, final String validateInput) {
        return Pattern.compile(pattern).matcher(validateInput).matches();
    }

    @FXML
    TextField re_user, re_pwd, re_confirmpwd, re_sdt, re_addr, re_money;
    @FXML
    Button btn_Register;
    @FXML

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
    
    public void Handle_Register() {
        Stage register_scene = (Stage) btn_Register.getScene().getWindow();
        if (re_user.getText().isEmpty() || re_pwd.getText().isEmpty() || re_money.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Register Dialog");
            alert.setHeaderText("Register Failed");
            alert.setContentText("Username, password or money must not be empty!");
            alert.showAndWait();
    
        } else {
            try {
                conn = function.connectDB();

                try (PreparedStatement command = conn.prepareStatement("Insert into USERS(userName, password, sdt, addr, tk) values(?,?,?,?,?)")) {
                    if (validate(USERNAME_PATTERN, re_user.getText())) {
                        command.setString(1, re_user.getText());
                    } else {
                        System.out.println("username khong hop le");
                        return;
                    }
                    if (validate(PASSWORD_PATTERN, re_pwd.getText())) {
                        if (re_pwd.getText().equals(re_confirmpwd.getText())) {
                            command.setString(2, md5(re_pwd.getText()));
                        } else {
                            System.out.println("Password incorrectly confirmed");
                            return;
                        }
                    } else {
                        System.out.println("password khong hop le");
                        return;
                    }
                    command.setString(3, re_sdt.getText());
                    command.setString(4, re_addr.getText());
                    if (validate(MONEY_PATTERN, re_money.getText())) {
                        command.setString(5, re_money.getText());
                    } else {
                        System.out.println("money khong hop le");
                        return;
                    }

                    PreparedStatement command1 = conn.prepareStatement("Select * from PCs where userName=?");
                    command1.setString(1, re_user.getText());

                    ResultSet rs = command1.executeQuery();
                    if (rs.next()) {

                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Register Dialog");
                        alert.setHeaderText("Register failed");
                        alert.setContentText("The username already exists!");

                        alert.showAndWait();

                    } else {
                        command.executeUpdate();
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Register Dialog");
                        alert.setHeaderText("Register succecced");
//                alert.setContentText("I have a great message for you!");

                        alert.showAndWait();
                        register_scene.hide();
                    }
                    rs.close();
                }
                conn.close();

            } catch (SQLException ex) {
                System.out.println(ex);
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
