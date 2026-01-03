import java.util.*;

public class OrderManager {

    private Map<Integer, MenuItem> menuMap;  // menu_id → MenuItem
    private PriorityQueue<Order> pq;         // in-memory priority queue

    public OrderManager(Map<Integer, MenuItem> menuMap) {
        this.menuMap = menuMap;

        // Priority queue sorts the smallest priorityScore first
        pq = new PriorityQueue<>(Comparator.comparingInt(o -> o.priorityScore));
    }

    // 1) CALCULATE PRIORITY SCORE

    public int computePriority(MenuItem item) {
        // Basic rule:
        // Beverage highest → Fast-food → Full Meal → Bulk lower priority
        int typeScore;
        switch (item.type) {
            case "Beverage" -> typeScore = 1;
            case "Fast-Food" -> typeScore = 2;
            case "Full-Meal" -> typeScore = 3;
            case "Bulk" -> typeScore = 4;
            default -> typeScore = 5;
        }
        return typeScore * 100 + item.estPrepTime;
    }

    // 2) PLACE ORDER

    public int placeOrder(String studentName, int menuId, String remarks) {
        MenuItem item = menuMap.get(menuId);

        int pr = computePriority(item);

        // Create new order
        Order o = new Order(studentName, menuId, item.itemName, item.type, pr, remarks);

        // Insert into DB → get generated id
        int orderId = OrderDAO.insertOrder(o);

        if (orderId != -1) {
            o.id = orderId; // update the object
            pq.add(o);      // add to priority queue
        }

        return orderId;
    }

    // 3) LOAD ALL PENDING ORDERS INTO PQ

    public void loadPendingOrders() {
        pq.clear();
        List<Order> pending = OrderDAO.getPendingOrders();
        pq.addAll(pending);
    }

    // 4) CALL NEXT ORDER

    public Order callNextOrder() {
        if (pq.isEmpty()) {
            loadPendingOrders(); // backup refresh
        }
        Order next = pq.poll(); // take highest priority

        if (next != null) {
            OrderDAO.updateStatus(next.id, "IN_PROGRESS");
            next.status = "IN_PROGRESS";
        }
        return next;
    }

    // 5) MARK COMPLETED

    public void markCompleted(int id) {
        OrderDAO.updateStatus(id, "COMPLETED");
    }

    // 6) GET PENDING ORDERS FOR DISPLAY

    public List<Order> getPendingList() {
        return OrderDAO.getPendingOrders();
    }
}
