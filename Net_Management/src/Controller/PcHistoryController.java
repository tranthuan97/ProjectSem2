/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Models.ModelPcHistoryTable;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.Node;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author User
 */
public class PcHistoryController implements Initializable {

    Function function = new Function();
    Connection conn;
    Connection conn1;

    @FXML
    private Pagination pagination;
    @FXML
    private TableView<Models.ModelPcHistoryTable> table;
    @FXML
    private TableColumn<Models.ModelPcHistoryTable, String> col_id, col_pcid, col_gid, col_eid, col_start, col_end, col_money;
    ObservableList<Models.ModelPcHistoryTable> oblist = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println(Math.ceil((double) count() / 5));
        loadData(0, 5);
        pagination.setPageCount((int) Math.ceil((double) count() / 5));
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            oblist.removeAll(oblist);
            loadData((int) newIndex * 5, ((int) newIndex + 1) * 5);
        });
    }

    public int count() {
        try {
            conn = function.connectDB();
            ResultSet rs = conn.createStatement().executeQuery("Select count(*) from PcLogs");
            while (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PcHistoryController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public void loadData(int from, int to) {
        try {
            // TODO
            conn = function.connectDB();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from PcLogs order by Id offset " + from + " rows fetch next " + to + " rows only");
            while (rs.next()) {
                oblist.add(new ModelPcHistoryTable(
                        rs.getString("Id"),
                        rs.getString("PcId"),
                        rs.getString("GId"),
                        rs.getString("EId"),
                        rs.getString("StartTime"),
                        rs.getString("EndTime"),
                        rs.getString("Money")
                ));
            }

            col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            col_pcid.setCellValueFactory(new PropertyValueFactory<>("pcid"));
            col_gid.setCellValueFactory(new PropertyValueFactory<>("gid"));
            col_eid.setCellValueFactory(new PropertyValueFactory<>("eid"));
            col_start.setCellValueFactory(new PropertyValueFactory<>("start"));
            col_end.setCellValueFactory(new PropertyValueFactory<>("end"));
            col_money.setCellValueFactory(new PropertyValueFactory<>("money"));
            table.setItems(oblist);

        } catch (SQLException ex) {
            Logger.getLogger(PcHistoryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
