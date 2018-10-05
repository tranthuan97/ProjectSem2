/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Models.ModelAccountTable;
import Models.ModelUserTable;
import java.io.IOException;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class EmployeeLayoutController implements Initializable {
    Function function = new Function();
    Connection conn;

    @FXML
    Button btn_Register;
    @FXML
    TextField searchField;
    @FXML
    private TableView<Models.ModelUserTable> table;
    @FXML
    private TableColumn<Models.ModelUserTable, String> col_id;
    @FXML
    private TableColumn<Models.ModelUserTable, String> col_name;
    @FXML
    private TableColumn<Models.ModelUserTable, String> col_phone;
    @FXML
    private TableColumn<Models.ModelUserTable, String> col_address;
//    @FXML
//    private TableColumn<Models.ModelAccountTable, String> col_phone;
//    @FXML
//    private TableColumn<Models.ModelAccountTable, String> col_balance;
    @FXML
    CheckBox btn_Search;
    ObservableList<Models.ModelUserTable> oblist = FXCollections.observableArrayList();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    
    public void employeeHandle() {
        Parent register;
        try {
            register = FXMLLoader.load(getClass().getResource("/FXML/EmployeeRegister.fxml"));
            Scene scene = new Scene(register);
            Stage stage = new Stage();

//            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            scene.getStylesheets().add(getClass().getResource("/css/registerStyle.css").toExternalForm());

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Employee Register");
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
        public void refreshHandle() {
        try {
            oblist.removeAll(oblist);
            conn = function.connectDB();
            ResultSet rs = conn.createStatement().executeQuery("Select * from Employee");
            while (rs.next()) {
                oblist.add(new ModelUserTable(
                        rs.getString("Id"),
                        rs.getString("Full Name"),
                        rs.getString("Address"),
                        rs.getString("Phone")
                ));
            }
            col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
            col_address.setCellValueFactory(new PropertyValueFactory<>("addr"));
            col_phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
//            col_phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
//        col_balance.setCellValueFactory(new PropertyValueFactory<>("balance"));
            table.setItems(oblist);
        } catch (SQLException ex) {
            Logger.getLogger(AccountLayoutController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
