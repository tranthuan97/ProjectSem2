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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class RegisterController implements Initializable {

    @FXML
    TextField re_user, re_pwd, re_confirmpwd, re_role, re_phone, re_id;
//            re_sdt, re_addr, re_money;
    @FXML
    Button btn_Register;
    @FXML
    ComboBox cb_user;
    //kiem tra tinh hop le cua du lieu
    private static final String USERNAME_PATTERN = "^[a-z0-9]{5,18}$";
    private static final String PASSWORD_PATTERN = "^[a-z0-9]{6,18}$";
    private static final String ROLE_PATTERN = "^[0-1]{1}$";

//    private static final String PHONE_PATTERN = "^[0-9]{10,11}";
//    private static final String MONEY_PATTERN = "^[0-9]{4,10}";
//    ObservableList<Object> roleList=FXCollections.observableArrayList("1","0");
    Function function = new Function();
    Connection conn;
    Connection conn1;

//    public RegisterController() {
//        user_pattern = Pattern.compile(USERNAME_PATTERN);
//        password_pattern = Pattern.compile(PASSWORD_PATTERN);
//        phone_pattern = Pattern.compile(PHONE_PATTERN);
//        money_pattern = Pattern.compile(MONEY_PATTERN);
//    }
    public boolean validate(String pattern, final String validateInput) {
        return Pattern.compile(pattern).matcher(validateInput).matches();
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
    
    public void searchHandle(){
        try {
            String user = (String) cb_user.getValue();
            conn1 = function.connectDB();
            PreparedStatement command = conn1.prepareStatement("Select * from "+user+" where Phone=?");
            command.setString(1, re_phone.getText());
            
            ResultSet rs = command.executeQuery();
            while(rs.next()){
                if("Employee".equals(user)){
                    re_id.setText(rs.getString("EId"));
                }else{
                    re_id.setText(rs.getString("GId"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public void Handle_Register() {
        Stage register_scene = (Stage) btn_Register.getScene().getWindow();
        if (re_user.getText().isEmpty() || re_pwd.getText().isEmpty() || re_role.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Register Dialog");
            alert.setHeaderText("Register Failed");
            alert.setContentText("Username or password must not be empty!");
            alert.showAndWait();

        } else {
            try {
                conn = function.connectDB();

                try (PreparedStatement command = conn.prepareStatement("Insert into Accounts( Username, Password, Role, Id) values(?,?,?,?)")) {
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
                    if (validate(ROLE_PATTERN, re_role.getText())) {
                        command.setString(3, re_role.getText());
                    } else {
                        System.out.println("Role khong hop le");
                        return;
                    }
                    command.setString(4, re_id.getText());

                    PreparedStatement command1 = conn.prepareStatement("Select * from Accounts where userName=?");
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
        ObservableList<String> cblist = FXCollections.observableArrayList(
                        "Employee",
                        "Guests"
                );
        cb_user.setItems(cblist);
    }
}
