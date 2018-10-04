package Controller;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
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
    public static final String Checkout = "/FXML/CheckoutPc.fxml";

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
    public Connection connectDB() throws SQLException {
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

    public static String md5(String msg) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(msg.getBytes());
            byte byteData[] = md.digest();
            //convert the byte to hex format method 1
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            return "";
        }
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

    //    -------------------------------------------------------------------------
    //convert millis to time "HH:mm:ss" (millis * 3600 / 5)
    public String convertMillisecondToTime(long millis) {
        Date date = new Date(millis);
        // formattter
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        // Pass date object
        String formatted = formatter.format(date);
        return formatted;
    }

    //convert string "HH:mm:ss" to millisecond (time / 3600 *5)
    public long convertTimeToMillisecond(String time) {
        String source = time;
        String[] tokens = source.split(":");
        int secondsToMs = Integer.parseInt(tokens[2]) * 1000;
        int minutesToMs = Integer.parseInt(tokens[1]) * 60000;
        int hoursToMs = Integer.parseInt(tokens[0]) * 3600000;
        long total = secondsToMs + minutesToMs + hoursToMs;
        return total;
    }

    public long getCurrentDateTimeToMillis() {
        Date date1;
        date1 = new Date();
        long timeMilli = date1.getTime();
        return timeMilli;
    }

    public long convertDateTimeToMillis(String date) throws ParseException {
            String myDate = date;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date1 = sdf.parse(myDate);
            long millis = date1.getTime();
            return millis;
    }

    public String getCurrentDate() {
        String pattern = "dd/MM/yyyy HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String date3 = simpleDateFormat.format(new Date());
        return date3;
    }

    public String convertMillisToDate(long millis) {
        String pattern = "dd/MM/yyyy HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String date3 = simpleDateFormat.format(new Date(millis));
        return date3;
    }

}
