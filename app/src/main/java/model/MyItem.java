package model;

/**
 * Created by ushtinfeld on 18/03/2018.
 */

public class MyItem {
    private String id;
    private String name;
    private int quantity;

    public MyItem() {

    }

    public MyItem(String id, String name, int quantity) {
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
