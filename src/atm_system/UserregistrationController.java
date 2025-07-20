package atm_system;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UserregistrationController implements Initializable {

    @FXML
    private TextField text3; // Name
    @FXML
    private TextField text4; // Username
    @FXML
    private TextField text5; // Password
    @FXML
    private Button btn3;     // Submit
    @FXML
    private Button btn4;     // Back

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialization if needed
    }

    @FXML
    private void submit(ActionEvent event) {
        String name = text3.getText();
        String username = text4.getText();
        String password = text5.getText();

        if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "All fields are required!");
        } else {
           
            showAlert(Alert.AlertType.INFORMATION, "Success", "Account created successfully!");
        }
    }

    @FXML
    private void backtologin(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("User_login.fxml"));
            Stage stage = (Stage) btn4.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.setHeaderText(null);
        alert.show();
    }
}
