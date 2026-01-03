import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
public class Canteen extends JFrame {
    private OrderManager manager;

    // Components
    private JTextField studentNameField;
    private JComboBox<String> itemBox;
    private JTextField remarksField;

    private JButton placeOrderBtn;
    private JButton callNextBtn;
    private JButton markCompletedBtn;
    private JTextField orderIdField;

    private JButton showPendingBtn;
    private JTextArea pendingArea;

    private JButton showCompletedBtn;
    private JTextArea completedArea;

    private JButton refreshInProgressBtn;
    private JTextArea inProgressArea;



    public Canteen() {
        Map<Integer, MenuItem> menuMap = new HashMap<>();

        menuMap.put(1, new MenuItem(1, "Tea", "Beverage", 10, 2));
        menuMap.put(2, new MenuItem(2, "Coffee", "Beverage", 15, 3));
        menuMap.put(3, new MenuItem(3, "Burger", "Fast-Food", 50, 5));
        menuMap.put(4, new MenuItem(4, "Thali", "Full-Meal", 80, 20));
        menuMap.put(5, new MenuItem(5, "Biryani", "Full-Meal", 90, 25));


        manager = new OrderManager(menuMap);
        manager.loadPendingOrders();
        setTitle("Canteen Order Management System");
        setSize(950, 900);


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // MAIN LAYOUT → vertical box
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // TITLE
        JLabel title = new JLabel("CANTEEN ORDER MANAGEMENT SYSTEM");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(title);

        mainPanel.add(Box.createVerticalStrut(15));

        // PLACE ORDER SECTION
        JPanel placePanel = new JPanel();
        placePanel.setLayout(new GridLayout(4, 2, 10, 10));
        placePanel.setBorder(BorderFactory.createTitledBorder("Place Order"));

        placePanel.add(new JLabel("Student Name:"));
        studentNameField = new JTextField();
        placePanel.add(studentNameField);

        placePanel.add(new JLabel("Select Item:"));
        itemBox = new JComboBox<>(new String[]{"Tea", "Coffee", "Burger", "Thali", "Biryani"});
        placePanel.add(itemBox);

        placePanel.add(new JLabel("Special Request:"));
        remarksField = new JTextField();
        placePanel.add(remarksField);

        placeOrderBtn = new JButton("Place Order");
        placePanel.add(placeOrderBtn);
        placePanel.add(new JLabel(""));

        // COMPLETED ORDERS SECTION
        JPanel completedPanel = new JPanel(new BorderLayout());
        completedPanel.setBorder(BorderFactory.createTitledBorder("Completed Orders"));

        showCompletedBtn = new JButton("Refresh Completed Orders");
        completedPanel.add(showCompletedBtn, BorderLayout.NORTH);

        completedArea = new JTextArea();
        completedArea.setEditable(false);
        completedArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane compScroll = new JScrollPane(completedArea);
        completedPanel.add(compScroll, BorderLayout.CENTER);
        compScroll.setPreferredSize(new Dimension(750, 180));


        mainPanel.add(completedPanel);

        mainPanel.add(placePanel);
        mainPanel.add(Box.createVerticalStrut(15));

        // PROCESS ORDERS SECTION
        JPanel processPanel = new JPanel();
        processPanel.setLayout(new BoxLayout(processPanel, BoxLayout.Y_AXIS));
        processPanel.setBorder(BorderFactory.createTitledBorder("Order Processing"));

        callNextBtn = new JButton("Call Next Order");
        callNextBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        processPanel.add(callNextBtn);

        processPanel.add(Box.createVerticalStrut(10));

        JPanel completePanel = new JPanel(new FlowLayout());
        completePanel.add(new JLabel("Order ID:"));
        orderIdField = new JTextField(8);
        completePanel.add(orderIdField);

        markCompletedBtn = new JButton("Mark Completed");
        completePanel.add(markCompletedBtn);

        processPanel.add(completePanel);

        mainPanel.add(processPanel);
        mainPanel.add(Box.createVerticalStrut(15));

        // PENDING ORDERS SECTION
        JPanel pendingPanel = new JPanel(new BorderLayout());
        pendingPanel.setBorder(BorderFactory.createTitledBorder("Pending Orders"));


        showPendingBtn = new JButton("Refresh Pending Orders");
        pendingPanel.add(showPendingBtn, BorderLayout.NORTH);

        pendingArea = new JTextArea();
        pendingArea.setEditable(false);
        pendingArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scroll = new JScrollPane(pendingArea);
        pendingPanel.add(scroll, BorderLayout.CENTER);
        scroll.setPreferredSize(new Dimension(750, 180));

        mainPanel.add(pendingPanel);

        // IN-PROGRESS ORDERS SECTION
        JPanel inProgressPanel = new JPanel(new BorderLayout());
        inProgressPanel.setBorder(BorderFactory.createTitledBorder("In-Progress Orders"));

        refreshInProgressBtn = new JButton("Refresh In-Progress Orders");
        inProgressPanel.add(refreshInProgressBtn, BorderLayout.NORTH);

        // create text area
        inProgressArea = new JTextArea();
        inProgressArea.setEditable(false);
        inProgressArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        // create ONE scrollpane
        JScrollPane inProgScroll = new JScrollPane(inProgressArea);
        inProgScroll.setPreferredSize(new Dimension(750, 180));

        inProgressPanel.add(inProgScroll, BorderLayout.CENTER);

        mainPanel.add(inProgressPanel);


        // FINALIZE FRAME
        add(mainPanel);
        setVisible(true);
        placeOrderBtn.addActionListener(e -> {
            String student = studentNameField.getText().trim();
            String itemName = (String) itemBox.getSelectedItem();
            String remarks = remarksField.getText().trim();

            if (student.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter student name");
                return;
            }

            // find menuId based on item name
            int menuId = switch (itemName) {
                case "Tea" -> 1;
                case "Coffee" -> 2;
                case "Burger" -> 3;
                case "Thali" -> 4;
                case "Biryani" -> 5;
                default -> -1;
            };

            int orderId = manager.placeOrder(student, menuId, remarks);

            if (orderId != -1) {
                JOptionPane.showMessageDialog(this,
                        "Order Placed Successfully!\nOrder ID: " + orderId);
                studentNameField.setText("");
                remarksField.setText("");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to place order!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        callNextBtn.addActionListener(e -> {
            Order next = manager.callNextOrder();

            if (next == null) {
                JOptionPane.showMessageDialog(this, "No pending orders!");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Next Order:\n#" + next.id + " - " + next.itemName +
                                "\nStudent: " + next.studentName +
                                "\nPriority: " + next.priorityScore);
            }
        });
        markCompletedBtn.addActionListener(e -> {
            String text = orderIdField.getText().trim();

            if (text.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter Order ID!");
                return;
            }

            int id = Integer.parseInt(text);
            manager.markCompleted(id);

            JOptionPane.showMessageDialog(this,
                    "Order #" + id + " marked as COMPLETED");

            orderIdField.setText("");
        });
        showPendingBtn.addActionListener(e -> {
            manager.loadPendingOrders();
            pendingArea.setText("");

            // HEADER
            pendingArea.append(String.format(
                    "%-5s %-15s %-25s %-10s %-12s\n",
                    "ID", "Student", "Item", "Priority", "Status"
            ));
            pendingArea.append("--------------------------------------------------------------------------\n");

            // ROWS
            for (Order o : manager.getPendingList()) {
                pendingArea.append(String.format(
                        "%-5s %-15s %-25s %-10d %-12s\n",
                        "#" + o.id,
                        o.studentName,
                        o.itemName + " (" + o.type + ")",
                        o.priorityScore,
                        o.status
                ));
            }

            if (manager.getPendingList().isEmpty()) {
                pendingArea.append("No pending orders.");
            }
        });

        showCompletedBtn.addActionListener(e -> {
            completedArea.setText("");

            // HEADER
            completedArea.append(String.format(
                    "%-5s %-15s %-25s %-10s %-12s\n",
                    "ID", "Student", "Item", "Priority", "Status"
            ));
            completedArea.append("--------------------------------------------------------------------------\n");

            // ROWS
            for (Order o : OrderDAO.getCompletedOrders()) {
                completedArea.append(String.format(
                        "%-5s %-15s %-25s %-10d %-12s\n",
                        "#" + o.id,
                        o.studentName,
                        o.itemName + " (" + o.type + ")",
                        o.priorityScore,
                        o.status
                ));
            }

            if (OrderDAO.getCompletedOrders().isEmpty()) {
                completedArea.append("No completed orders.");
            }
        });

        refreshInProgressBtn.addActionListener(e -> {
            inProgressArea.setText("");

            // HEADER
            inProgressArea.append(String.format(
                    "%-5s %-15s %-25s %-10s %-12s\n",
                    "ID", "Student", "Item", "Priority", "Status"
            ));
            inProgressArea.append("--------------------------------------------------------------------------\n");

            // ROWS
            for (Order o : OrderDAO.getInProgressOrders()) {
                inProgressArea.append(String.format(
                        "%-5s %-15s %-25s %-10d %-12s\n",
                        "#" + o.id,
                        o.studentName,
                        o.itemName + " (" + o.type + ")",
                        o.priorityScore,
                        o.status
                ));
            }

            if (OrderDAO.getInProgressOrders().isEmpty()) {
                inProgressArea.append("No in-progress orders.");
            }
        });


    }

    public static void main(String[] args) {
        new Canteen();
    }
}
