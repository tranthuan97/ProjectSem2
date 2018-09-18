package Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author User
 */
public class Function {

    // Next Stage
    public static final String Login = "/FXML/Login_Register.fxml";
    public static final String Register = "/FXML/Register.fxml";
    public static final String GUIMain = "/FXML/GUIMain.fxml";
    public static final String AddNew = "/FXML/AddNewPc.fxml";

    // Css Style
    public static final String cssLogin = "/css/loginStyle.css";
    public static final String cssRegister = "/css/registerStyle.css";
    public static final String cssUserLayout = "/css/userlayout.css";

    static final String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    static final String DB_URL = "jdbc:sqlserver://localhost;";
    //  Ten database, nguoi dung va mat khau cua co so du lieu
    static final String DATABASENAME = "databaseName=Net_Management;";
    static final String USER = "user=sa;";
    static final String PASS = "password=123123";

    //Connect Database
    public static Connection connectDB() throws SQLException {
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn;
            conn = DriverManager.getConnection(DB_URL + DATABASENAME + USER + PASS);
            return conn;
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    // Next Stage func
    public void nextStageDefault(String fxml, String title, Boolean resizable, String css) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource(fxml));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
//          Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene.getStylesheets().add(getClass().getResource(css).toExternalForm());
            stage.setTitle(title);
            stage.setResizable(resizable);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(Login_Register.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void nextStageDefault2(String fxml, String title, Boolean resizable, String css) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource(fxml));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
//          Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene.getStylesheets().add(getClass().getResource(css).toExternalForm());
            stage.setTitle(title);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(resizable);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(Login_Register.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void nextStageSetWidthHeight(String fxml, String title, int width, int height, String css, boolean resizable) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource(fxml));
            Scene scene = new Scene(root, width, height);
            Stage stage = new Stage();
//                                Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene.getStylesheets().add(getClass().getResource(css).toExternalForm());

//                                app_stage.hide();
            stage.setTitle(title);
            stage.setResizable(resizable);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(Login_Register.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Show Alert
    public void showAlert(String title, String headerText, String text, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(text);
        alert.showAndWait();
    }
}
