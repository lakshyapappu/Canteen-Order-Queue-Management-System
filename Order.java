import java.sql.Timestamp;

public class Order {

    public int id;              // Order ID (auto increment in DB)
    public String studentName;  // student who placed order
    public int menuId;          // foreign key referencing Menu.id
    public String itemName;     // for display (Tea, Coffee, etc.)
    public String type;         // Beverage / Fast-Food / Full-Meal / Bulk
    public Timestamp placedAt;  // timestamp of order
    public String status;       // PENDING, IN_PROGRESS, COMPLETED
    public int priorityScore;   // calculated based on type and prep time
    public String remarks;      // special requests / instructions

    // Constructor used AFTER fetching from DB (includes ID + timestamp)
    public Order(int id, String studentName, int menuId, String itemName, String type,
                 Timestamp placedAt, String status, int priorityScore, String remarks) {
        this.id = id;
        this.studentName = studentName;
        this.menuId = menuId;
        this.itemName = itemName;
        this.type = type;
        this.placedAt = placedAt;
        this.status = status;
        this.priorityScore = priorityScore;
        this.remarks = remarks;
    }

    // Constructor used BEFORE inserting into DB (ID assigned automatically)
    public Order(String studentName, int menuId, String itemName, String type,
                 int priorityScore, String remarks) {
        this.studentName = studentName;
        this.menuId = menuId;
        this.itemName = itemName;
        this.type = type;
        this.priorityScore = priorityScore;
        this.remarks = remarks;
        this.status = "PENDING";
        this.placedAt = new Timestamp(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return "#" + id + " | "
                + studentName + " | "
                + itemName + " (" + type + ") | "
                + "Priority: " + priorityScore + " | "
                + "Status: " + status;
    }
}
