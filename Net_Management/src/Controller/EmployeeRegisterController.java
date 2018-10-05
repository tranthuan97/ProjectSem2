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
public class EmployeeRegisterController implements Initializable {

    //kiem tra tinh hop le cua du lieu
    private static final String NAME_PATTERN = "^[a-z]{0,100}$";
    private static final String PHONE_PATTERN = "^[0-9]{10,11}$";
    private static final String ADDRESS_PATTERN = "^[a-z0-9/,]{0,100}$";

//    private static final String PHONE_PATTERN = "^[0-9]{10,11}";
//    private static final String MONEY_PATTERN = "^[0-9]{4,10}";
//    ObservableList<Object> roleList=FXCollections.observableArrayList("1","0");
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
    TextField e_name, e_phone, e_address;
//            re_sdt, re_addr, re_money;
    @FXML
    Button btn_employee;
    
            

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
    
    public void Handle_Employee() {
        Stage register_scene = (Stage) btn_employee.getScene().getWindow();
        if (e_name.getText().isEmpty() || e_phone.getText().isEmpty() || e_address.getText().isEmpty() ) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Register Dialog");
            alert.setHeaderText("Register Failed");
            alert.setContentText("Name,Phone or Address must not be empty!");
            alert.showAndWait();
    
        } else {
            try {
                conn = function.connectDB();

                try (PreparedStatement command = conn.prepareStatement("Insert into Employee(Fullname, phone, address) values(?,?,?)")) {
                    if (validate(NAME_PATTERN, e_name.getText())) {
                        command.setString(1, e_name.getText());
                    } else {
                        System.out.println("Ten khong hop le");
                        return;
                    }
                     if (validate(PHONE_PATTERN, e_phone.getText())) {
                        command.setString(1, e_phone.getText());
                    } else {
                        System.out.println("SĐT khong hop le");
                        return;
                    }
                    if (validate(ADDRESS_PATTERN, e_address.getText())) {
                        command.setString(3, e_address.getText());
                    } else {
                        System.out.println("Dia chi khong hop le");
                       return;
                    }
                    

                   {
                        command.executeUpdate();
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Register Dialog");
                        alert.setHeaderText("Register succecced");
//                alert.setContentText("I have a great message for you!");

                        alert.showAndWait();
                        register_scene.hide();
                    }
                    
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
