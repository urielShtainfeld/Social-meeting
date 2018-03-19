package model;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ushtinfeld on 17/03/2018.
 */

public class Attendance {
    private final AtomicInteger unique = new AtomicInteger();
    private int id;
    private String name;
    private String email;

    public Attendance() {
    }

    public Attendance(String name, String email) {
        this.id = unique.incrementAndGet();
        this.email = email;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
