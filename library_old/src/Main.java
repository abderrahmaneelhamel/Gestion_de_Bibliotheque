import java.util.*;

public class Main {
    public static void main(String[] args) {
        while (true) {
            // Check for outdated borrows
            Book.checkOutDatedBorrows();
            // Display initial menu options
            System.out.println("Welcome to the library management system!");
            System.out.println("Please choose from the following options:");
            System.out.println("1 : login \n2 : signup");
            // Get user choice for login or signup
            int choice = tools.tryParse(new Scanner(System.in).nextLine());
            // Validate user choice
            while (!(choice == 2 || choice == 1)) {
                System.out.println("Invalid input");
                System.out.println("\n1 : login \n2 : signup");
                choice = tools.tryParse(new Scanner(System.in).nextLine());
            }

            if (choice == 1) {
                //User chose to login
                User current = Auth.login();
                int state = 2;
                while (state == 2) {
                    if (current == null) {
                        break;
                    }
                    if (current.getRole() == 1) {
                        // Logged in as Librarian
                        Librarian librarian = new Librarian(current);
                        librarianMenu(librarian);
                    } else if (current.getRole() == 2) {
                        // Logged in as Reader
                        Reader reader = new Reader(current);
                        readerMenu(reader);
                    }
                    System.out.println("\n1 : exit \n2 : restart\n3: logout");
                    state = tools.tryParse(new Scanner(System.in).nextLine());
                    // Validate user's exit or restart choice
                    while (!(state == 2 || state == 1 || state == 3)) {
                        System.out.println("Invalid input");
                        System.out.println("\n1 : exit \n2 : restart\n3: logout");
                        state = tools.tryParse(new Scanner(System.in).nextLine());
                    }
                    tools.cls();
                }
                if (state == 1) {
                    break;
                }
            } else if (choice == 2) {
                // User chose to sign up
                User current = Auth.signup();
                int state = 2;
                while (state == 2) {
                    Reader reader = new Reader(current);
                    readerMenu(reader);
                    System.out.println("\n1 : exit \n2 : restart\n3: logout");
                    state = tools.tryParse(new Scanner(System.in).nextLine());
                    // Validate user's exit or restart choice
                    while (!(state == 2 || state == 1 || state == 3)) {
                        System.out.println("Invalid input");
                        System.out.println("\n1 : exit \n2 : restart\n3: logout");
                        state = tools.tryParse(new Scanner(System.in).nextLine());
                    }
                    tools.cls();
                }
                if (state == 1) {
                    break;
                }
            }
        }
    }

    // Method to handle Reader menu
    public static void readerMenu(Reader reader) {
        System.out.println("hello "+reader.getName());
        System.out.println("1 : borrow a book \n2 : return a book\n3 : search a book by title\n4 : search book by author\n5 : log out");
        int choice = tools.tryParse(new Scanner(System.in).nextLine());

        // Validate user's menu choice
        while (!(choice == 2 || choice == 1 || choice == 3 ||  choice == 4 ||  choice == 5))  {
            System.out.println("Invalid input");
            System.out.println("1 : borrow a book \n2 : return a book\n3 : search a book by title\n4 : search book by author\n5 : log out");
            choice = tools.tryParse(new Scanner(System.in).nextLine());
        }

        // Perform action based on user's choice
        switch (choice) {
            case 1 -> reader.borrowBook();
            case 2 -> reader.returnBook();
            case 3 -> reader.searchByTitle();
            case 4 -> reader.searchByAuthor();
            case 5 -> System.out.println("Logging out");
            default -> System.out.println("Invalid choice");
        }
    }

    // Method to handle Librarian menu
    public static void librarianMenu(Librarian librarian) {
        System.out.println("Welcome Mr Librarian");
        System.out.println("1 : add a book \n2 : edit a book\n3 : delete a book\n4 : statistics \n5 : search\n6: add Admin\n7 : log out");
        int choice = tools.tryParse(new Scanner(System.in).nextLine());

        // Validate user's menu choice
        while (!(choice == 2 || choice == 1 || choice == 3 || choice == 4  || choice == 5 ||  choice == 6 ||   choice == 7))  {
            System.out.println("Invalid input");
            System.out.println("1 : add a book \n2 : edit a book\n3 : delete a book\n4 : statistics \n5 : search\n6: add Admin\n7 : log out");
            choice = tools.tryParse(new Scanner(System.in).nextLine());
        }

        // Perform action based on user's choice
        switch (choice) {
            case 1 -> librarian.addBook();
            case 2 -> librarian.editBook();
            case 3 -> librarian.deleteBook();
            case 4 -> librarian.statistics();
            case 5 -> librarian.search();
            case 6 -> librarian.addAdmin();
            case 7 -> System.out.println("Logging out");
            default -> System.out.println("Invalid choice");
        }
    }
}
