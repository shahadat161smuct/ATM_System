package atm_system;

import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class DashboardController implements Initializable {

    @FXML private TextField text6;
    @FXML private Button btn5;
    @FXML private Button btn6;
    @FXML private Label label1;
    @FXML private Button btn7;
    @FXML private ListView<String> listview;
    @FXML private Label labelname;

    private static String currentUserCardNumber;
    private double balance = 0.0;
    private ObservableList<String> transactions = FXCollections.observableArrayList();
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (currentUserCardNumber == null || currentUserCardNumber.isEmpty()) {
            showAlert("User card number not set. Please login again.");
            return;
        }
        labelname.setText("Card Number: " + maskCardNumber(currentUserCardNumber)); // updated line
        listview.setItems(transactions);
        loadUserBalance();
        loadTransactions();
    }

    public static void setCurrentUser(String cardNumber) {
        currentUserCardNumber = cardNumber;
    }

    private void loadUserBalance() {
        try (Connection con = DatabaseConnection.connect()) {
            String sql = "SELECT balance FROM users WHERE card_number = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, currentUserCardNumber);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                balance = rs.getDouble("balance");
                label1.setText(String.format("Your Current Balance is: %.2f BDT", balance));
            } else {
                showAlert("User not found in database.");
            }
        } catch (Exception e) {
            showAlert("Error loading balance: " + e.getMessage());
        }
    }

    private void updateBalance() {
        try (Connection con = DatabaseConnection.connect()) {
            String sql = "UPDATE users SET balance = ? WHERE card_number = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setDouble(1, balance);
            pst.setString(2, currentUserCardNumber);
            int updated = pst.executeUpdate();
            if (updated > 0) {
                label1.setText(String.format("Your Current Balance is: %.2f BDT", balance));
            } else {
                showAlert("Failed to update balance.");
            }
        } catch (Exception e) {
            showAlert("Error updating balance: " + e.getMessage());
        }
    }

    private void saveTransaction(String type, double amount, LocalDateTime time) {
        try (Connection con = DatabaseConnection.connect()) {
            String sql = "INSERT INTO transactions (card_number, type, amount, transaction_time) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, currentUserCardNumber);
            pst.setString(2, type);
            pst.setDouble(3, amount);
            pst.setTimestamp(4, Timestamp.valueOf(time));
            pst.executeUpdate();
        } catch (Exception e) {
            showAlert("Error saving transaction: " + e.getMessage());
        }
    }

    private void loadTransactions() {
        transactions.clear();
        try (Connection con = DatabaseConnection.connect()) {
            String sql = "SELECT type, amount, transaction_time FROM transactions WHERE card_number = ? ORDER BY transaction_time DESC";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, currentUserCardNumber);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String type = rs.getString("type");
                double amount = rs.getDouble("amount");
                Timestamp ts = rs.getTimestamp("transaction_time");
                LocalDateTime dateTime = ts.toLocalDateTime();
                String formattedTime = dtf.format(dateTime);

                String transactionText = String.format("%s: %.2f BDT on %s", type, amount, formattedTime);
                transactions.add(transactionText);
            }
        } catch (Exception e) {
            showAlert("Error loading transactions: " + e.getMessage());
        }
    }

    @FXML
    private void withdraw(ActionEvent event) {
        try {
            double amount = Double.parseDouble(text6.getText());
            if (amount <= 0) {
                showAlert("Enter a valid positive amount.");
            } else if (amount > balance) {
                showAlert("Insufficient balance!");
            } else {
                balance -= amount;
                updateBalance();
                LocalDateTime now = LocalDateTime.now();
                saveTransaction("Withdraw", amount, now);
                transactions.add(0, String.format("Withdraw: %.2f BDT on %s", amount, dtf.format(now)));
                text6.clear();
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid amount input.");
        }
    }

    @FXML
    private void deposit(ActionEvent event) {
        try {
            double amount = Double.parseDouble(text6.getText());
            if (amount <= 0) {
                showAlert("Enter a valid positive amount.");
            } else {
                balance += amount;
                updateBalance();
                LocalDateTime now = LocalDateTime.now();
                saveTransaction("Deposit", amount, now);
                transactions.add(0, String.format("Deposit: %.2f BDT on %s", amount, dtf.format(now)));
                text6.clear();
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid amount input.");
        }
    }

    @FXML
    private void logout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("User_login.fxml"));
            Stage stage = (Stage) btn7.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            showAlert("Unable to logout: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notice");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

  
    private String maskCardNumber(String cardNumber) {
        if (cardNumber.length() != 8) return cardNumber;
        String first3 = cardNumber.substring(0, 3);
        String last2 = cardNumber.substring(6);
        return first3 + "***" + last2;
    }
}
