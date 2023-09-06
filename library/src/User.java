public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private int role;


//    public User(int id, String name, String email,String password,int role) {
//        this.id = id;
//        this.name = name;
//        this.email = email;
//        this.password = password;
//        this.role = role;
//    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRole() {
        return this.role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    // Override toString method
    @Override
    public String toString() {
        return "User{" +
                "userID='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }


}
