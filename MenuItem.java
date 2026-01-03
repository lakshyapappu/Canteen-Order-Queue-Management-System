public class MenuItem {

    public int id;
    public String itemName;
    public String type;
    public double price;
    public int estPrepTime;

    public MenuItem(int id, String itemName, String type, double price, int estPrepTime) {
        this.id = id;
        this.itemName = itemName;
        this.type = type;
        this.price = price;
        this.estPrepTime = estPrepTime;
    }

    @Override
    public String toString() {
        return itemName + " (" + type + ")";
    }
}
