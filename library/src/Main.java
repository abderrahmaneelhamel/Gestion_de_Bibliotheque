import service.LibraryService;
public class Main {

    public static void main(String[] args) {
        try {
            LibraryService libraryService = new LibraryService();
            libraryService.start();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}