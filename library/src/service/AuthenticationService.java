package service;

import dao.UserDAOImpl;
import model.User;
import util.tools;

import java.util.Scanner;

public class AuthenticationService {
    private final UserDAOImpl userDAO;

    public AuthenticationService(UserDAOImpl userDAO) {
        this.userDAO = userDAO;
    }

    public User signIn(Scanner scanner) {
        System.out.println("=== User Sign-In ===");
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        // Validate user's email
        while (tools.isValidEmailFormat(email)) {
            System.out.println("Invalid email format. Please enter a valid email:");
            email = new Scanner(System.in).nextLine();
        }
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();
        // Validate user's password
        while (tools.isValidPassword(password)) {
            System.out.println("Invalid password format. Password must be at least 8 characters long without spaces:");
            password = new Scanner(System.in).nextLine();
        }

        User authenticatedUser = userDAO.authenticate(email, password);

        if (authenticatedUser != null) {
            System.out.println("Sign-In successful!");
        } else {
            System.out.println("Sign-In failed. Invalid email or password.");
        }

        return authenticatedUser;
    }

    public User signUp(Scanner scanner) {
        System.out.println("=== User Sign-Up ===");
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        // Validate user's email
        while (tools.isValidEmailFormat(email)) {
            System.out.println("Invalid email format. Please enter a valid email:");
            email = new Scanner(System.in).nextLine();
        }

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        // Validate user's password
        while (tools.isValidPassword(password)) {
            System.out.println("Invalid password format. Password must be at least 8 characters long without spaces:");
            password = new Scanner(System.in).nextLine();
        }

        User newUser = new User(0,name, email, password,2);

        if (userDAO.addUser(newUser)) {
            System.out.println("Sign-Up successful!");
            return newUser;
        } else {
            System.out.println("Sign-Up failed. User already exists.");
            return null;
        }
    }
    public void addAdmin(Scanner scanner) {
        System.out.println("=== Admin Registration ===");

        // Collect admin information
        System.out.print("Enter admin name: ");
        String name = scanner.nextLine();
        System.out.print("Enter admin email: ");
        String email = scanner.nextLine();
        // Validate user's email
        while (tools.isValidEmailFormat(email)) {
            System.out.println("Invalid email format. Please enter a valid email:");
            email = new Scanner(System.in).nextLine();
        }

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        // Validate user's password
        while (tools.isValidPassword(password)) {
            System.out.println("Invalid password format. Password must be at least 8 characters long without spaces:");
            password = new Scanner(System.in).nextLine();
        }

        // Create an admin user with role 1 (admin)
        User admin = new User(0,name, email, password, 1);

        // Register the admin user using the userDAO
        if (userDAO.addUser(admin)) {
            System.out.println("Admin registration successful!");
        } else {
            System.out.println("Admin registration failed. Please check the input.");
        }
    }

}
