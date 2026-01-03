import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    // INSERT NEW ORDER INTO DB
    public static int insertOrder(Order order) {
        String sql = "INSERT INTO Orders(studentName, menu_id, status, priority_score, remarks) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, order.studentName);
            ps.setInt(2, order.menuId);
            ps.setString(3, order.status);
            ps.setInt(4, order.priorityScore);
            ps.setString(5, order.remarks);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);  // return generated order_id
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;  // failed
    }


    // FETCH ORDER JOINED WITH MENU TABLE
    public static Order getOrderById(int id) {
        String sql =
                "SELECT O.*, M.itemName, M.type " +
                        "FROM Orders O JOIN Menu M ON O.menu_id = M.id " +
                        "WHERE O.id = ?";

        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Order(
                        rs.getInt("id"),
                        rs.getString("studentName"),
                        rs.getInt("menu_id"),
                        rs.getString("itemName"),
                        rs.getString("type"),
                        rs.getTimestamp("placed_at"),
                        rs.getString("status"),
                        rs.getInt("priority_score"),
                        rs.getString("remarks")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // not found
    }


    // LOAD ALL PENDING ORDERS (sorted by priority)
    public static List<Order> getPendingOrders() {
        List<Order> list = new ArrayList<>();

        String sql =
                "SELECT O.*, M.itemName, M.type " +
                        "FROM Orders O JOIN Menu M ON O.menu_id = M.id " +
                        "WHERE O.status = 'PENDING' " +
                        "ORDER BY O.priority_score ASC, O.placed_at ASC";

        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Order(
                        rs.getInt("id"),
                        rs.getString("studentName"),
                        rs.getInt("menu_id"),
                        rs.getString("itemName"),
                        rs.getString("type"),
                        rs.getTimestamp("placed_at"),
                        rs.getString("status"),
                        rs.getInt("priority_score"),
                        rs.getString("remarks")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    // UPDATE ORDER STATUS (IN_PROGRESS or COMPLETED)
    public static void updateStatus(int id, String status) {
        String sql = "UPDATE Orders SET status = ? WHERE id = ?";

        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // LOAD ALL COMPLETED ORDERS
    public static List<Order> getCompletedOrders() {
        List<Order> list = new ArrayList<>();

        String sql =
                "SELECT O.*, M.itemName, M.type " +
                        "FROM Orders O JOIN Menu M ON O.menu_id = M.id " +
                        "WHERE O.status = 'COMPLETED' " +
                        "ORDER BY O.id DESC";

        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Order(
                        rs.getInt("id"),
                        rs.getString("studentName"),
                        rs.getInt("menu_id"),
                        rs.getString("itemName"),
                        rs.getString("type"),
                        rs.getTimestamp("placed_at"),
                        rs.getString("status"),
                        rs.getInt("priority_score"),
                        rs.getString("remarks")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    // LOAD ALL IN-PROGRESS ORDERS
    public static List<Order> getInProgressOrders() {
        List<Order> list = new ArrayList<>();

        String sql =
                "SELECT O.*, M.itemName, M.type " +
                        "FROM Orders O JOIN Menu M ON O.menu_id = M.id " +
                        "WHERE O.status = 'IN_PROGRESS' " +
                        "ORDER BY O.id ASC";

        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Order(
                        rs.getInt("id"),
                        rs.getString("studentName"),
                        rs.getInt("menu_id"),
                        rs.getString("itemName"),
                        rs.getString("type"),
                        rs.getTimestamp("placed_at"),
                        rs.getString("status"),
                        rs.getInt("priority_score"),
                        rs.getString("remarks")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

}
