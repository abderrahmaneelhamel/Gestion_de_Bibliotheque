package util;

import dao.DatabaseConnection;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class tools {


    // Try to parse a string to an integer, return 0 if it's not a valid integer
    public static Integer tryParse(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static void printInFile(String text, String fileName) {
        try {
            FileWriter fw = new FileWriter(fileName, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(text);
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            System.out.println("Error writing to file");
        }
    }

    // Check if an email has a valid format
    // Return true if the email is valid, false if not
    public static boolean isValidEmailFormat(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return !matcher.matches();
    }

    // Check if a password is valid
    public static boolean isValidPassword(String password) {
        String regex = "^(?=\\S+$).{8,}$";
        // Must have 8 characters or more
        // No whitespaces allowed
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return !matcher.matches();
    }

    public static void checkOutDatedBorrows(){
        try {
            Connection connection = new DatabaseConnection().connection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM borrow");
            HashMap<String, String> borrowedBooks = new HashMap<>();
            while (resultSet.next()) {
                borrowedBooks.put(resultSet.getString("id"), resultSet.getString("return_date"));
            }
            for (String key : borrowedBooks.keySet()) {
                String returnDate = borrowedBooks.get(key);
                LocalDate today = LocalDate.now();
                LocalDate return_date = LocalDate.parse(returnDate);
                if (today.isAfter(return_date)) {
                    statement.executeUpdate("UPDATE borrow SET status = 'lost' WHERE id = '" + key + "'");
                }
            }
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
