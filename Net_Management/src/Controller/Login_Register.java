package Controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Window;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
public class Login_Register implements Initializable {

    Function function = new Function();
    Connection conn;
    Connection conn1;

    /**
     * Initializes the controller class.
     */
    @FXML
    Button btn_Login;
    @FXML
    TextField tf_User;
    @FXML
    PasswordField tp_Pwd;

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

    public void tp_PwdKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            btn_Login();
        }
    }

    public void btn_Login() {
        try {
            Stage current = (Stage) btn_Login.getScene().getWindow();
            conn = function.connectDB();
            try (PreparedStatement command = conn.prepareStatement("Select * from Accounts where Username=? and Password=? and Role='True'")) {
                command.setString(1, tf_User.getText());
                command.setString(2, md5(tp_Pwd.getText()));
                if (tf_User.getText().isEmpty() || tp_Pwd.getText().isEmpty()) {
                    function.showAlert(
                            "Login Dialog",
                            "Login failed",
                            "Username or password must not be empty!",
                            Alert.AlertType.INFORMATION
                    );
                } else {
                    try (ResultSet rs = command.executeQuery()) {
                        if (rs.next()) {
                            String EId = rs.getString("Id");
                            String EAccount = rs.getString("Username");
                            rs.close();
                            command.close();
                            conn1 = function.connectDB();
                            PreparedStatement command1 = conn1.prepareStatement("Update SaveLoginStatus Set EId=?, EAccount=?, IsLoggin=? where Id=1");
                            command1.setString(1, EId);
                            command1.setString(2, EAccount);
                            command1.setString(3, "true");
                            int i = command1.executeUpdate();
                            if (i == 1) {
                                System.out.println("Successfully");
                            }
                            current.hide();
                            function.nextStageSetWidthHeight(function.GUIMain, "Main Menu", 900, 700, "/css/pclayout.css", true);
                            command1.close();
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
