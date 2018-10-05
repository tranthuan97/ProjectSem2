/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Models.ModelAccountTable;
import com.sun.jndi.toolkit.dir.SearchFilter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class AccountLayoutController implements Initializable {

    /**
     * Initializes the controller class.
     */
    Function function = new Function();
    Connection conn;

    @FXML
    Button btn_Register;
    @FXML
    TextField searchField;
    @FXML
    private TableView<Models.ModelAccountTable> table;
    @FXML
    private TableColumn<Models.ModelAccountTable, String> col_id;
    @FXML
    private TableColumn<Models.ModelAccountTable, String> col_user;
    @FXML
    private TableColumn<Models.ModelAccountTable, String> col_pwd;
    @FXML
    private TableColumn<Models.ModelAccountTable, String> col_role;
//    @FXML
//    private TableColumn<Models.ModelAccountTable, String> col_phone;
//    @FXML
//    private TableColumn<Models.ModelAccountTable, String> col_balance;
    @FXML
    CheckBox btn_Search;
    ObservableList<Models.ModelAccountTable> oblist = FXCollections.observableArrayList();
    @FXML
    void entername(CellEditEvent event) {
        Models.ModelAccountTable u = table.getSelectionModel().getSelectedItem();
        String id = (String)event.getNewValue();
        String oldid = (String)event.getOldValue();
        System.out.println(oldid);
        System.out.println(id);
        
        try {
            String query = "update Accounts "
                    + "set Username='"+ id+ "' where Username ='"+oldid+"'";
            Statement pst = conn.createStatement();
            pst.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        table.setEditable(true);
        col_id.setCellFactory( TextFieldTableCell.forTableColumn());
        col_user.setCellFactory( TextFieldTableCell.forTableColumn());
        Handle_LoadingAccount();
    }

    public void btn_Register() {
        Parent register;
        try {
            register = FXMLLoader.load(getClass().getResource("/FXML/Register.fxml"));
            Scene scene = new Scene(register);
            Stage stage = new Stage();

//            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene.getStylesheets().add(getClass().getResource("/css/registerStyle.css").toExternalForm());

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Register Screen");
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public void Handle_LoadingAccount() {
        try {
            oblist.removeAll(oblist);
            conn = function.connectDB();
            ResultSet rs = conn.createStatement().executeQuery("Select * from Accounts");
            while (rs.next()) {
                oblist.add(new ModelAccountTable(
                        rs.getString("Id"),
                        rs.getString("Username"),
                        rs.getString("Password"),
                        rs.getString("Role")
                ));
            }
            col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            col_user.setCellValueFactory(new PropertyValueFactory<>("user"));
            col_pwd.setCellValueFactory(new PropertyValueFactory<>("pwd"));
            col_role.setCellValueFactory(new PropertyValueFactory<>("role"));
//            col_phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
//        col_balance.setCellValueFactory(new PropertyValueFactory<>("balance"));
            table.setItems(oblist);
        } catch (SQLException ex) {
            Logger.getLogger(AccountLayoutController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Handle_btnSearch() {
        if (btn_Search.isSelected()) {
            searchField.setDisable(false);

            FilteredList<Models.ModelAccountTable> filteredTable = new FilteredList<>(oblist, e -> true);
            searchField.textProperty().addListener(((observable, oldValue, newValue) -> {
                filteredTable.setPredicate((Predicate<? super Models.ModelAccountTable>) user -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCasesFilter = newValue.toLowerCase();
                    if (user.getId().contains(newValue)) {
                        return true;
                    } else if (user.getUser().toLowerCase().contains(newValue)) {
                        return true;
                    } else if (user.getUser().toLowerCase().contains(newValue)) {
                        return true;
                    }
//                    else if (user.getPhone().contains(newValue)) {
//                        return true;
//                    } else if (user.getAddr().toLowerCase().contains(newValue)) {
//                        return true;
//                    }
                    return false;
                });
            }));
            SortedList<Models.ModelAccountTable> sortedTable = new SortedList<>(filteredTable);
            sortedTable.comparatorProperty().bind(table.comparatorProperty());
            table.setItems(sortedTable);
        } else {
            searchField.setDisable(true);
        }
    }

}
