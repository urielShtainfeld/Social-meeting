package model;

/**
 * Created by ushtinfeld on 17/03/2018.
 */

public class Item {
    private String id;
    private String name;
    private int quantity;

    public Item() {
    }

    public Item(String id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
}
