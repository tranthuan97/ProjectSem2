/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Models.ModelPcTable;
import Models.ModelUserTable;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author User
 */
public class PCLayoutController implements Initializable {

    /**
     * Initializes the controller class.
     */
    Function function = new Function();
    Connection conn;

    @FXML
    ImageView pc_1, pc_2, pc_3, pc_4, pc_5, pc_6;

    @FXML
    private TableView<Models.ModelPcTable> pc_table;
    @FXML
    private TableColumn<Models.ModelPcTable, String> col_pcid;
    @FXML
    private TableColumn<Models.ModelPcTable, String> col_pcusername;
    @FXML
    private TableColumn<Models.ModelPcTable, String> col_pctotal;
    @FXML
    private TableColumn<Models.ModelPcTable, String> col_pcstart;
    @FXML
    private TableColumn<Models.ModelPcTable, String> col_pcend;
    @FXML
    private TableColumn<Models.ModelPcTable, String> col_pcstatus;

    ObservableList<Models.ModelPcTable> oblist = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pcStatus(1);
        pcStatus(2);
        pcStatus(3);
        pcStatus(4);
        pcStatus(5);
        pcStatus(6);
    }

    public void showPcInfo_1() {
        showPcInfo(1);
    }

    public void showPcInfo_2() {
        showPcInfo(2);
    }

    public void showPcInfo_3() {
        showPcInfo(3);
    }

    public void showPcInfo_4() {
        showPcInfo(4);
    }

    public void showPcInfo_5() {
        showPcInfo(5);
    }

    public void showPcInfo_6() {
        showPcInfo(6);
    }

    public void showPcInfo_7() {
        showPcInfo(7);
    }

    public void showPcInfo_8() {
        showPcInfo(8);
    }

    public void showPcInfo_9() {
        showPcInfo(9);
    }
    
    public void AddNewHandle(){
        function.nextStageDefault2(function.AddNew, "Add new", false, "");
    }

    public void pcStatus(int pcid) {
        try {
            conn = function.connectDB();
            ResultSet rs = conn.createStatement().executeQuery("Select * from PCs where Id =" + pcid);
            while (rs.next()) {
                if (rs.getInt("status") == 1) {
                    switch (pcid) {
                        case 1: {
                            Image newImage = new Image("/image/offline1_computer.png");
                            pc_1.setImage(newImage);
                            break;
                        }
                        case 2: {
                            Image newImage = new Image("/image/offline1_computer.png");
                            pc_2.setImage(newImage);
                            break;
                        }
                        case 3: {
                            Image newImage = new Image("/image/offline1_computer.png");
                            pc_3.setImage(newImage);
                            break;
                        }
                        case 4: {
                            Image newImage = new Image("/image/offline1_computer.png");
                            pc_4.setImage(newImage);
                            break;
                        }
                        case 5: {
                            Image newImage = new Image("/image/offline1_computer.png");
                            pc_5.setImage(newImage);
                            break;
                        }
                        case 6: {
                            Image newImage = new Image("/image/offline1_computer.png");
                            pc_6.setImage(newImage);
                            break;
                        }
                        default:
                            break;
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PCLayoutController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void showPcInfo(int pcid) {
        try {
            oblist.removeAll(oblist);
            conn = function.connectDB();
            ResultSet rs = conn.createStatement().executeQuery("Select * from PCs where Id =" + pcid);
            while (rs.next()) {
                oblist.add(new ModelPcTable(
                        rs.getString("Id"),
                        rs.getString("Username"),
                        rs.getString("TotalUserTime"),
                        rs.getString("StartTime"),
                        rs.getString("EndTime"),
                        rs.getString("status")
                ));
            }

            col_pcid.setCellValueFactory(new PropertyValueFactory<>("id"));
            col_pcusername.setCellValueFactory(new PropertyValueFactory<>("user"));
            col_pctotal.setCellValueFactory(new PropertyValueFactory<>("total"));
            col_pcstart.setCellValueFactory(new PropertyValueFactory<>("start"));
            col_pcend.setCellValueFactory(new PropertyValueFactory<>("end"));
            col_pcstatus.setCellValueFactory(new PropertyValueFactory<>("status"));
            pc_table.setItems(oblist);
        } catch (SQLException ex) {
            Logger.getLogger(UserLayoutController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
