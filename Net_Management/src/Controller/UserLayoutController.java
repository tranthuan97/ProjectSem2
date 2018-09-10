/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Models.ModelUserTable;
import com.sun.jndi.toolkit.dir.SearchFilter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
public class UserLayoutController implements Initializable {

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
    private TableView<Models.ModelUserTable> table;
    @FXML
    private TableColumn<Models.ModelUserTable, String> col_id;
    @FXML
    private TableColumn<Models.ModelUserTable, String> col_user;
    @FXML
    private TableColumn<Models.ModelUserTable, String> col_pwd;
//    @FXML
//    private TableColumn<Models.ModelUserTable, String> col_addr;
//    @FXML
//    private TableColumn<Models.ModelUserTable, String> col_phone;
//    @FXML
//    private TableColumn<Models.ModelUserTable, String> col_balance;
    @FXML
    CheckBox btn_Search;
    ObservableList<Models.ModelUserTable> oblist = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

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
        oblist.removeAll(oblist);
//            conn = function.connectDB();
//            ResultSet rs = conn.createStatement().executeQuery("Select * from USERS");
//            while (rs.next()) {
//                oblist.add(new ModelUserTable(
//                        rs.getString("Id"),
//                        rs.getString("Username"),
//                        rs.getString("Password")
//                ));
//            }
        oblist.add(new ModelUserTable("1", "admin", "admin"));
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_user.setCellValueFactory(new PropertyValueFactory<>("user"));
        col_pwd.setCellValueFactory(new PropertyValueFactory<>("pwd"));
//            col_addr.setCellValueFactory(new PropertyValueFactory<>("addr"));
//            col_phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
//        col_balance.setCellValueFactory(new PropertyValueFactory<>("balance"));
        table.setItems(oblist);
    }

    public void Handle_btnSearch() {
        if (btn_Search.isSelected()) {
            searchField.setDisable(false);

            FilteredList<Models.ModelUserTable> filteredTable = new FilteredList<>(oblist, e -> true);
            searchField.textProperty().addListener(((observable, oldValue, newValue) -> {
                filteredTable.setPredicate((Predicate<? super Models.ModelUserTable>) user -> {
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
            SortedList<Models.ModelUserTable> sortedTable = new SortedList<>(filteredTable);
            sortedTable.comparatorProperty().bind(table.comparatorProperty());
            table.setItems(sortedTable);
        } else {
            searchField.setDisable(true);
        }
    }

}
