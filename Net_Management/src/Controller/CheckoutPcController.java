/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import java.text.NumberFormat;
import java.text.ParseException;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class CheckoutPcController implements Initializable {

    Function function = new Function();
    Connection conn;
    Connection conn1;
    Connection conn2;
    Connection conn3;

    String Eid;
    String GId;
    String Id;

    @FXML
    ComboBox cb_checkout;
    @FXML
    TextField tf_user, tf_start, tf_end, tf_total;
    @FXML
    Button btn_accept;
    
    ObservableList oblist = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        FillComboBox();
    }

    public void FillComboBox() {
        try {
            conn = function.connectDB();
            String query = "select Id from PCs where status = 1";
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                oblist.add(rs.getString("Id"));
            }
            cb_checkout.setItems(oblist);

            rs.close();
            pst.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void cbCheckoutHandle() throws ParseException {
//        String end1 = LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute() + ":" + LocalDateTime.now().getSecond();
        try {
            int pc = Integer.valueOf((String) cb_checkout.getValue());
            conn = function.connectDB();

            PreparedStatement command = conn.prepareStatement(("Select * from PCs where Id =?"));
            command.setInt(1, pc);
            ResultSet rs = command.executeQuery();
//            System.out.println(pc);
            while (rs.next()) {
                tf_user.setText(rs.getString("Username"));
                tf_start.setText(rs.getString("StartTime"));
                tf_end.setText(function.getCurrentDate());

                String end = function.getCurrentDate();
                long Start = function.convertDateTimeToMillis(rs.getString("StartTime"));
                long End = function.convertDateTimeToMillis(end);

                NumberFormat format = NumberFormat.getInstance();
                format.setGroupingUsed(true);
                format.setMaximumFractionDigits(2);

                long money = (End - Start) / 3600 * 5;
                tf_total.setText(String.valueOf(format.format(money)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CheckoutPcController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void acceptHandle() {
        Stage current = (Stage) btn_accept.getScene().getWindow();

        try {
            conn = function.connectDB();
            if (cb_checkout.getValue() != null) {
                int pc = Integer.valueOf((String) cb_checkout.getValue());

                ResultSet rs = conn.createStatement().executeQuery("Select * from SaveLoginStatus");
                while (rs.next()) {
                    this.Eid = rs.getString("EId");
                }
                rs.close();
                conn1 = function.connectDB();
                PreparedStatement command = conn1.prepareStatement(("Select * from PCs where Id =?"));
                command.setInt(1, pc);
                ResultSet rs1 = command.executeQuery();
                while (rs1.next()) {
                    this.Id = rs1.getString("Id");
                    this.GId = rs1.getString("GId");
                }
                rs1.close();
                command.close();
//             System.out.println(this.Eid);
//             System.out.println(this.GId);
//             System.out.println(this.Id);

                conn2 = function.connectDB();
                PreparedStatement command1 = conn2.prepareStatement("Update PCs Set Username=?, GId=?, status=?, StartTime=? where Id=?");

//            PreparedStatement command1 = conn2.prepareStatement("Update PCs Set Username=?, GId=?, status=?, StartTime=? where Id=?");
                command1.setString(1, "");
                command1.setString(2, "");
                command1.setString(3, "false");
                command1.setString(4, "");
                command1.setInt(5, pc);
                command1.executeUpdate();
                command1.close();
                System.out.println(pc);

                conn3 = function.connectDB();
                PreparedStatement command2 = conn3.prepareStatement("INSERT INTO PCLogs (PCId, GId, EId, StartTime, EndTime, Money)\n"
                        + "VALUES (?,?,?,?,?,?)");
                command2.setString(1, this.Id);
                command2.setString(2, this.GId);
                command2.setString(3, this.Eid);
                command2.setString(4, tf_start.getText());
                command2.setString(5, tf_end.getText());
                command2.setString(6, tf_total.getText());
                int i = command2.executeUpdate();
                if (i == 1) {
                        function.showAlert(
                                "Check out Dialog",
                                "Info",
                                "Check out successfully",
                                Alert.AlertType.INFORMATION
                        );
                        current.hide();
                    }
                command2.close();
            } else {
                System.out.println("123");
                function.showAlert(
                        "Check out Dialog",
                        "Access Denied",
                        "The PC does not exists!",
                        Alert.AlertType.INFORMATION
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(CheckoutPcController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
