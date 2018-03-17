package model;

/**
 * Created by ushtinfeld on 17/03/2018.
 */

public class Item {
    private String id;
    private String name;
    private int quantity;
    private boolean selected;
    private int remainingQuantity;
    public Item() {
    }

    public Item(String id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.selected = false;
        this.remainingQuantity = quantity;
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
