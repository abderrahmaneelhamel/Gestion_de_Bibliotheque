import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class tools {
    public static void cls() {
        for (int i = 0; i < 30; i++) {
            System.out.println();
        }
    }
    public static boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidPassword(String password) {
        String regex = "^(?=\\S+$).{8,}$";
        // must have 8 characters long
        // no whitespaces
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

//    public static boolean isValidUsername(String username) {
//        String regex = "^[A-Za-z0-9+_.-]+$";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(username);
//        return matcher.matches();
//    }
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

}
