/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import Controller.Function;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author User
 */
public class NewFXMain extends Application {

    Function function = new Function();
    Connection conn;

    @Override
    public void start(Stage primaryStage) throws IOException {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/FXML/Login_Register.fxml"));
//        Parent root = FXMLLoader.load(getClass().getResource("/FXML/UserLayout.fxml"));
//        Parent root = FXMLLoader.load(getClass().getResource("/FXML/PcHistory.fxml"));
            conn = function.connectDB();
            ResultSet rs = conn.createStatement().executeQuery("Select * from SaveLoginStatus");
            while (rs.next()) {
                if (rs.getBoolean("isLoggin")) {
                    function.nextStageSetWidthHeight(function.GUIMain, "Main Menu", 900, 700, "/css/pclayout.css", true);

                } else {
                    Scene scene = new Scene(root);
                    scene.getStylesheets().add(getClass().getResource("/css/loginStyle.css").toExternalForm());
                    primaryStage.setTitle("Login Screen");
                    primaryStage.setScene(scene);
//        primaryStage.setResizable(false);
                    primaryStage.show();
                }
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(NewFXMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
