/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author User
 */
public class GUIMainController implements Initializable {

    ObservableList<String> olsUser = FXCollections.observableArrayList();
    ObservableList<String> olsPc = FXCollections.observableArrayList();
    @FXML
    ListView<String> accountlist, pclist;
    @FXML
    TabPane tbpane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        loadUserLV();
        loadPcLV();
        selectmenu();
    }

    private void loadUserLV() {
//        ObservableList<String> ols = FXCollections.observableArrayList();
//        ols.removeAll(ols);
//        ols.add("Normal");
//        ols.add("VIP");
//        lvmaster.setItems(ols);
        olsUser.remove(olsUser);
        String admin = "Admin";
        String emp = "Employee";
        String guest_v = "VIP";
        String guest_n = "Normal";
        olsUser.addAll(admin, emp, guest_n, guest_v);
        accountlist.getItems().addAll(olsUser);
    }
    
    
    private void loadPcLV() {
//        ObservableList<String> ols = FXCollections.observableArrayList();
//        ols.removeAll(ols);
//        ols.add("Normal");
//        ols.add("VIP");
//        lvmaster.setItems(ols);
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
                        Node node = (AnchorPane) FXMLLoader.load(getClass().getResource("/FXML/UserLayout.fxml"));
                        Tab tb = new Tab("Admin", node);
                        tbpane.getTabs().add(tb);
                    } catch (IOException ex) {
                        Logger.getLogger(UserLayoutController.class.getName()).log(Level.SEVERE, null, ex);
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
                        Logger.getLogger(UserLayoutController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }

}
