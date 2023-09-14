import service.LibraryService;
public class Main {

    public static void main(String[] args) {
        try {
            LibraryService libraryService = LibraryService.getInstance();
            libraryService.start();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}