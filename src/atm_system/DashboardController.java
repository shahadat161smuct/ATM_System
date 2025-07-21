package atm_system;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DashboardController implements Initializable {

    @FXML
    private TextField text6; // amount input
    @FXML
    private Button btn5; // withdraw
    @FXML
    private Button btn6; // deposit
    @FXML
    private Label label1; // balance label
    @FXML
    private Button btn7; // logout
    @FXML
    private ListView<String> listview; // transaction history
    @FXML
    private Label labelname; // user card number

    private double balance = 0.0; 
    private ObservableList<String> transactions = FXCollections.observableArrayList();
    private static String currentUserCardNumber; // card number from login

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        label1.setText("Your Current Balance is: " + balance + " BDT");
        labelname.setText("Card Number: " + currentUserCardNumber);
        listview.setItems(transactions);
    }

    public static void setCurrentUser(String cardNumber) {
        currentUserCardNumber = cardNumber;
    }

    @FXML
    private void withdraw(ActionEvent event) {
        try {
            double amount = Double.parseDouble(text6.getText());
            if (amount <= 0) {
                showAlert("Enter a valid amount.");
            } else if (amount > balance) {
                showAlert("Insufficient balance!");
            } else {
                balance -= amount;
                label1.setText("Your Current Balance is: " + balance + " BDT");
                String time = dtf.format(LocalDateTime.now());
                transactions.add("Withdraw: " + amount + " BDT on " + time);
                text6.clear();
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid number input.");
        }
    }

    @FXML
    private void deposit(ActionEvent event) {
        try {
            double amount = Double.parseDouble(text6.getText());
            if (amount <= 0) {
                showAlert("Enter a valid amount.");
            } else {
                balance += amount;
                label1.setText("Your Current Balance is: " + balance + " BDT");
                String time = dtf.format(LocalDateTime.now());
                transactions.add("Deposit: " + amount + " BDT on " + time);
                text6.clear();
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid number input.");
        }
    }

    @FXML
    private void logout(ActionEvent event) {
        try {
            javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(getClass().getResource("User_login.fxml"));
            javafx.stage.Stage stage = (javafx.stage.Stage) btn7.getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root));
        } catch (Exception e) {
            showAlert("Unable to logout.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notice");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
