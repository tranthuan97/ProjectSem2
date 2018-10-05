/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author User
 */
public class GUIMainController implements Initializable {

    Function function = new Function();
    Connection conn;
    Connection conn1;

    ObservableList<String> olsUser = FXCollections.observableArrayList();
    ObservableList<String> olsAccount = FXCollections.observableArrayList();
    ObservableList<String> olsPc = FXCollections.observableArrayList();
    @FXML
    ListView<String> accountlist, pclist, userlist;
    @FXML
    TabPane tbpane;
    @FXML
    Label lb_clock;
    @FXML
    Button btn_logout;

    private int minute;
    private int hour;
    private int second;
    private Object date;
    private Object month;
    private Object year;

    private String EAccount;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        loadUser();
        loadAccountLV();
        loadPcLV();
        selectmenu();
        DateAndTime();
    }

    public void DateAndTime() {
        try {
            conn = function.connectDB();
            ResultSet rs = conn.createStatement().executeQuery("Select * from SaveLoginStatus");

            Calendar now = Calendar.getInstance();
            Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
                second = LocalDateTime.now().getSecond();
                minute = LocalDateTime.now().getMinute();
                hour = LocalDateTime.now().getHour();
                date = now.get(Calendar.DATE);
                month = now.get(Calendar.MONTH) + 1;
                year = now.get(Calendar.YEAR);
                try {
                    while (rs.next()) {
                        this.EAccount = rs.getString("EAccount");
                    }
//                    rs.close();

                } catch (SQLException ex) {
                    Logger.getLogger(GUIMainController.class.getName()).log(Level.SEVERE, null, ex);
                }
                lb_clock.setText(
                        "Manager: " + this.EAccount + "  "
                        + hour + ":"
                        + (minute) + ":"
                        + second + " - "
                        + date + "/"
                        + month + "/"
                        + year
                );
            }),
                    new KeyFrame(Duration.seconds(1))
            );

            clock.setCycleCount(Animation.INDEFINITE);
            clock.play();
        } catch (SQLException ex) {
            Logger.getLogger(GUIMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadAccountLV() {
        olsAccount.remove(olsAccount);
        String admin = "All Account";
        
        olsAccount.addAll(admin);
        accountlist.getItems().addAll(olsAccount);
    }
    
    private void loadUser() {
        olsUser.remove(olsUser);
        String emp = "Employee";
        String guest = "Guest";
        olsUser.addAll(emp, guest);
        userlist.getItems().addAll(olsUser);
    }

    private void loadPcLV() {
        olsPc.remove(olsPc);
        String a = "Máy trạm";
        String b = "History";
        olsPc.addAll(a, b);
        pclist.getItems().addAll(olsPc);
    }

    private void selectmenu() {
        accountlist.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                int i = accountlist.getSelectionModel().getSelectedIndex();
                if (i == 0) {
                    try {
                        Node node = (AnchorPane) FXMLLoader.load(getClass().getResource("/FXML/AccountLayout1.fxml"));
                        Tab tb = new Tab("All Account", node);
                        tbpane.getTabs().add(tb);
                    } catch (IOException ex) {
                        Logger.getLogger(AccountLayoutController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        pclist.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                int i = pclist.getSelectionModel().getSelectedIndex();
                if (i == 0) {
                    try {
                        Node node = (AnchorPane) FXMLLoader.load(getClass().getResource("/FXML/PCLayout.fxml"));
                        node.getStyleClass().add(getClass().getResource("/css/pclayout.css").toExternalForm());
                        Tab tb = new Tab("Máy Trạm", node);
                        tbpane.getTabs().add(tb);
                    } catch (IOException ex) {
                        Logger.getLogger(AccountLayoutController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else if (i == 1) {
                    try {
                        Node node = (AnchorPane) FXMLLoader.load(getClass().getResource("/FXML/PcHistory.fxml"));
//                        node.getStyleClass().add(getClass().getResource("/css/pclayout.css").toExternalForm());
                        Tab tb = new Tab("History", node);
                        tbpane.getTabs().add(tb);
                    } catch (IOException ex) {
                        Logger.getLogger(AccountLayoutController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        userlist.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                int i = userlist.getSelectionModel().getSelectedIndex();
                if (i == 0) {
                    try {
                        Node node = (AnchorPane) FXMLLoader.load(getClass().getResource("/FXML/EmployeeLayout.fxml"));
//                        node.getStyleClass().add(getClass().getResource("/css/pclayout.css").toExternalForm());
                        Tab tb = new Tab("Employee", node);
                        tbpane.getTabs().add(tb);
                    } catch (IOException ex) {
                        Logger.getLogger(AccountLayoutController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else if (i == 1) {
                    try {
                        Node node = (AnchorPane) FXMLLoader.load(getClass().getResource("/FXML/GuestLayout.fxml"));
//                        node.getStyleClass().add(getClass().getResource("/css/pclayout.css").toExternalForm());
                        Tab tb = new Tab("Guest", node);
                        tbpane.getTabs().add(tb);
                    } catch (IOException ex) {
                        Logger.getLogger(AccountLayoutController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }

    public void LogoutHandle() throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("CONFIRMATION DIALOG");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure to log out ?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            try {
                Stage current = (Stage) btn_logout.getScene().getWindow();

                conn1 = function.connectDB();
                PreparedStatement command1 = conn1.prepareStatement("Update SaveLoginStatus Set EId=?, EAccount=?, isLoggin=? where Id=1");
                command1.setString(1, null);
                command1.setString(2, null);
                command1.setBoolean(3, false);

                int i = command1.executeUpdate();
                if (i == 1) {
                    function.showAlert(
                            "EXIT DIALOG",
                            "INFO",
                            "The account has been logged out !",
                            Alert.AlertType.INFORMATION
                    );
                    function.nextStageDefault(function.Login, "Login", false, function.cssLogin);
                    current.hide();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GUIMainController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
