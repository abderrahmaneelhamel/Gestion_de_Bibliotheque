package model;
public class Librarian extends  User {
    private int id;
    private String name;
    private String email;
    private String password;
    private int role;

    public Librarian(User user) {
        super(user.getId(),user.getName(), user.getEmail(), user.getPassword(), user.getRole());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
