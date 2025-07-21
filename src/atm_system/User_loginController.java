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

public class User_loginController implements Initializable {

    @FXML
    private TextField text1; // Card Number
    @FXML
    private TextField text2; // PIN
    @FXML
    private Button btn1;     // Login Button
    @FXML
    private Button btn2;     // Create Account Button

    private final String validCardNumber = "12345678";
    private final String validPin = "1234";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }

    @FXML
    private void login(ActionEvent event) {
        String cardNumber = text1.getText().trim();
        String pin = text2.getText().trim();

        if (cardNumber.equals(validCardNumber) && pin.equals(validPin)) {
            showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome!");
            DashboardController.setCurrentUser(cardNumber);
            
            loadScene("dashboard.fxml");
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid Card Number or PIN.");
        }
    }

    @FXML
    private void Create_a_new_account(ActionEvent event) {
        loadScene("Userregistration.fxml");
    }

    private void loadScene(String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = (Stage) btn1.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to load page: " + fxml);
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
