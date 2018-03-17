package model;

/**
 * Created by ushtinfeld on 17/03/2018.
 */

public class Attendance {
    private String id;
    private String phone;
    private String name;

    public Attendance() {
    }

    public Attendance(String id, String name, String phone) {
        this.id = id;
        this.phone = phone;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }
}
