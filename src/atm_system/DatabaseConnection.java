package atm_system;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/atm_system";
    private static final String USER = "root";      // আপনার ডাটাবেস ইউজারনেম
    private static final String PASSWORD = "";      // আপনার ডাটাবেস পাসওয়ার্ড

    public static Connection connect() {
        try {
            // MySQL JDBC Driver লোড
            Class.forName("com.mysql.cj.jdbc.Driver");
            // কানেকশন তৈরি
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
