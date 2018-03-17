package model;

/**
 * Created by ushtinfeld on 17/03/2018.
 */

public class Attendance {
    private String id;
    private String name;
    private String email;

    public Attendance() {
    }

    public Attendance(String id, String name, String email) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
