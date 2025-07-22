package atm_system;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
    private TextField text4; // Username (card_number)
    @FXML
    private TextField text5; // Password (pin)
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
        String name = text3.getText().trim();
        String username = text4.getText().trim(); // ধরা হলো card_number
        String password = text5.getText().trim(); // ধরা হলো pin

        if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "All fields are required!");
        } else {
            // Database এ সেভ করার মেথড কল
            if(registerUser(name, username, password)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Account created successfully!");
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed", "Account creation failed. Try again!");
            }
        }
    }

    private boolean registerUser(String name, String username, String password) {
        String insertQuery = "INSERT INTO users (name, card_number, pin, balance) VALUES (?, ?, ?, 0.0)";
        // balance ডিফল্ট 0.0 ধরে নিচ্ছি
        try (Connection connectDB = DatabaseConnection.connect();
             PreparedStatement statement = connectDB.prepareStatement(insertQuery)) {

            statement.setString(1, name);
            statement.setString(2, username);
            statement.setString(3, password);

            int result = statement.executeUpdate();
            return result > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void clearFields() {
        text3.clear();
        text4.clear();
        text5.clear();
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
