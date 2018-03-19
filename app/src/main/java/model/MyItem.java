package model;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ushtinfeld on 18/03/2018.
 */

public class MyItem {
    private final AtomicInteger unique = new AtomicInteger();
    private int id;
    private String name;
    private int quantity;

    public MyItem() {
    }

    public MyItem(String name, int quantity) {
        this.id = unique.incrementAndGet();
        this.name = name;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "name='" + name +", quantity=" + quantity + '\'';
    }
}
