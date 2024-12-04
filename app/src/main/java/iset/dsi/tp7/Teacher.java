package iset.dsi.tp7;

public class Teacher {
    private int id;
    private String name;
    private String email;

    // Constructor
    public Teacher(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // Getter for id
    public int getId() {
        return id;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Getter for email
    public String getEmail() {
        return email;
    }

    // Setter for id (optional)
    public void setId(int id) {
        this.id = id;
    }

    // Setter for name (optional)
    public void setName(String name) {
        this.name = name;
    }

    // Setter for email (optional)
    public void setEmail(String email) {
        this.email = email;
    }
}
