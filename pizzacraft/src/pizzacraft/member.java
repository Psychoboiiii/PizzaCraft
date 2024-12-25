/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pizzacraft;

import javax.swing.JFrame;
import java.sql.DriverManager;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import javax.swing.table.DefaultTableModel;
import model.MySQL;

/**
 *
 * @author shash
 */
public class member extends javax.swing.JFrame {

    /**
     * Creates new form member
     */
    public member() {
        initComponents();
        updatePizzaTable();

    }

    private void randomizeOrderStatus(String username) {

        String[] statuses = {"Preparing", "Out for Delivery", "Delivered"};

        try {
            String query = "SELECT orderId FROM orderplaced WHERE username = '" + username + "'";
            ResultSet rs = MySQL.execute(query);

            while (rs != null && rs.next()) {
                String orderId = rs.getString("orderId");
                String randomStatus = statuses[(int) (Math.random() * statuses.length)];

                String updateQuery = "UPDATE orderplaced SET status = '" + randomStatus + "' WHERE orderId = '" + orderId + "'";
                MySQL.execute(updateQuery);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(new JFrame(), "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void searchAndDisplayOrderStatus(String username) {

        randomizeOrderStatus(username);

        String query = "SELECT username, orderId, pizzaname, status FROM orderplaced WHERE username = '" + username + "'";

        try {
            ResultSet rs = MySQL.execute(query);

            String[] columnNames = {"Username", "Order ID", "Pizza Name", "Order Status"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);

            while (rs != null && rs.next()) {
                String user = rs.getString("username");
                String orderId = rs.getString("orderId");
                String pizzaName = rs.getString("pizzaname");
                String status = rs.getString("status");

                Object[] row = {user, orderId, pizzaName, status};
                model.addRow(row);
            }

            orddertrack_table.setModel(model);

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(new JFrame(), "No orders found for the username: " + username, "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(new JFrame(), "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void getPizzaDetailsById(String pizzaId) {

        String query = "SELECT * FROM pizza WHERE pizzaId = '" + pizzaId + "'";

        try {

            ResultSet rs = MySQL.execute(query);

            if (rs != null && rs.next()) {
                String pizzaName = rs.getString("pizzaname");
                double price = rs.getDouble("price");
                name_mtxt.setText(pizzaName);
                price_mtxt.setText(String.valueOf(price));

            } else {
                JOptionPane.showMessageDialog(null, "Pizza ID not found", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void updatePizzaTable() {
        // SQL query to fetch all pizza records
        String query = "SELECT * FROM pizza";

        try (
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pizzacraft", "root", ""); Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            // Define column names for the table
            String[] columnNames = {"Pizza ID", "Pizza Name", "Price", "Category", "Description"};

            // Create a table model to store the data
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);

            while (rs.next()) {
                String pizzaId = rs.getString("pizzaId");
                String pizzaName = rs.getString("pizzaname");
                double price = rs.getDouble("price");
                String category = rs.getString("category");
                String description = rs.getString("description");

                // Add each row to the table model
                Object[] row = {pizzaId, pizzaName, price, category, description};
                model.addRow(row);
            }

            pizzaveiw_table.setModel(model);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void searchPizzas(String category, String pizzaName) {

        String query = "SELECT * FROM pizza WHERE 1=1";

        if (!category.isEmpty()) {
            query += " AND category = '" + category + "'";
        }

        if (!pizzaName.isEmpty()) {
            query += " AND pizzaname LIKE '%" + pizzaName + "%'";
        }

        try {
            ResultSet rs = MySQL.execute(query);
            String[] columnNames = {"pizzaId", "pizzaname", "price", "category", "description"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);

            while (rs.next()) {
                String pizzaId = rs.getString("pizzaId");
                String pizzaNameFromDb = rs.getString("pizzaname");
                double price = rs.getDouble("price");
                String categoryFromDb = rs.getString("category");
                String description = rs.getString("description");

                Object[] row = {pizzaId, pizzaNameFromDb, price, categoryFromDb, description};
                model.addRow(row);
            }

            pizzaveiw_table.setModel(model);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error retrieving pizzas: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Method to check if the username exists in the database
    private static boolean doesUsernameExist(String username) {
        String query = "SELECT COUNT(*) FROM users WHERE name = ?";
        try {

            PreparedStatement pst = MySQL.connection.prepareStatement(query);
            pst.setString(1, username);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0; // If count is greater than 0, username exists
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error checking username: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return false; // If an error occurs or username doesn't exist
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel12 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        homep = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        order = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        id_mtxt = new javax.swing.JTextField();
        name_mtxt = new javax.swing.JTextField();
        cmb_m = new javax.swing.JComboBox<>();
        address_mtxt = new javax.swing.JTextField();
        price_mtxt = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        pizzaveiw_table = new javax.swing.JTable();
        order_mbtn = new javax.swing.JButton();
        clear_mbtn = new javax.swing.JButton();
        search_mtxt = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        search_mbtn = new javax.swing.JButton();
        user_mtxt = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        category_cmb = new javax.swing.JComboBox<>();
        pizzabyId_btn = new javax.swing.JButton();
        crust_cmb = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        chesse_cmb = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();
        top_cmb = new javax.swing.JComboBox<>();
        ordertrack = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        ok_orbtn = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        orddertrack_table = new javax.swing.JTable();
        jLabel21 = new javax.swing.JLabel();
        feedback = new javax.swing.JPanel();
        cmt_ftxt = new javax.swing.JTextField();
        sub_fbtn = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        name_ftxt = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        clear_fbtn = new javax.swing.JButton();

        jLabel12.setText("jLabel12");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PizzaCraft");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 51, 51));

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Home");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(255, 255, 255));
        jButton6.setText("Order");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setBackground(new java.awt.Color(255, 255, 255));
        jButton7.setText("Order tracking");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(255, 255, 255));
        jButton8.setText("Feedback");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setText("Log out");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(35, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(45, 45, 45))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 110, Short.MAX_VALUE)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 483));

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setMinimumSize(new java.awt.Dimension(39, 84));

        homep.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setText("Welcome to PizzaCraft");

        jLabel2.setFont(new java.awt.Font("Tahoma", 2, 14)); // NOI18N
        jLabel2.setText("\"Where every slice feels like home!\"");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pizzacraft/PizzaCraft_2 (1).png"))); // NOI18N

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Thank you for choosing PizzaCraft, your trusted solution for managing your pizza shop with ease and efficiency.");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Let's get started and make every order fresh, fast, and fantastic!");

        jLabel6.setText("Copyright © PizzaCraft All Rights Reserved");

        javax.swing.GroupLayout homepLayout = new javax.swing.GroupLayout(homep);
        homep.setLayout(homepLayout);
        homepLayout.setHorizontalGroup(
            homepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homepLayout.createSequentialGroup()
                .addGroup(homepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(homepLayout.createSequentialGroup()
                        .addGap(140, 140, 140)
                        .addComponent(jLabel1))
                    .addGroup(homepLayout.createSequentialGroup()
                        .addGap(173, 173, 173)
                        .addGroup(homepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(homepLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel5))
                    .addGroup(homepLayout.createSequentialGroup()
                        .addGap(152, 152, 152)
                        .addComponent(jLabel4))
                    .addGroup(homepLayout.createSequentialGroup()
                        .addGap(246, 246, 246)
                        .addComponent(jLabel6)))
                .addContainerGap(84, Short.MAX_VALUE))
        );
        homepLayout.setVerticalGroup(
            homepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homepLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addContainerGap())
        );

        jTabbedPane1.addTab("tab1", homep);

        order.setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel7.setText("Pizza ID :");

        jLabel8.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel8.setText("Pizza Name :");

        jLabel9.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel9.setText("Quantity :");

        jLabel10.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel10.setText("Total price :");

        jLabel11.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel11.setText("Delivery address :");

        id_mtxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                id_mtxtActionPerformed(evt);
            }
        });

        cmb_m.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4" }));

        pizzaveiw_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "PizzaID", "Pizza Name", "Price", "Category", "Description"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(pizzaveiw_table);
        if (pizzaveiw_table.getColumnModel().getColumnCount() > 0) {
            pizzaveiw_table.getColumnModel().getColumn(0).setResizable(false);
            pizzaveiw_table.getColumnModel().getColumn(1).setResizable(false);
            pizzaveiw_table.getColumnModel().getColumn(2).setResizable(false);
            pizzaveiw_table.getColumnModel().getColumn(3).setResizable(false);
            pizzaveiw_table.getColumnModel().getColumn(4).setResizable(false);
        }

        order_mbtn.setBackground(new java.awt.Color(255, 51, 51));
        order_mbtn.setText("Order");
        order_mbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                order_mbtnActionPerformed(evt);
            }
        });

        clear_mbtn.setBackground(new java.awt.Color(255, 51, 51));
        clear_mbtn.setText("Clear");
        clear_mbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clear_mbtnActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel13.setText("Place your order");

        search_mbtn.setBackground(new java.awt.Color(255, 51, 51));
        search_mbtn.setText("Search");
        search_mbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                search_mbtnActionPerformed(evt);
            }
        });

        user_mtxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                user_mtxtActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel15.setText("User Name:");

        category_cmb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Veg", "Non-Veg" }));

        pizzabyId_btn.setBackground(new java.awt.Color(255, 51, 51));
        pizzabyId_btn.setText("Pizza by ID");
        pizzabyId_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pizzabyId_btnActionPerformed(evt);
            }
        });

        crust_cmb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Thin", "Normal" }));

        jLabel14.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel14.setText("Crust :");

        jLabel16.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel16.setText("Toppings :");

        chesse_cmb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mozzarella", "Cheddar", "Feta" }));

        jLabel17.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel17.setText("Cheese :");

        top_cmb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pepperoni", "Mushrooms", "Pineapple", "Onions" }));

        javax.swing.GroupLayout orderLayout = new javax.swing.GroupLayout(order);
        order.setLayout(orderLayout);
        orderLayout.setHorizontalGroup(
            orderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(orderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(orderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(orderLayout.createSequentialGroup()
                        .addGap(268, 268, 268)
                        .addComponent(jLabel13)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(orderLayout.createSequentialGroup()
                        .addGroup(orderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(orderLayout.createSequentialGroup()
                                .addGroup(orderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel14)
                                    .addGroup(orderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(order_mbtn)
                                        .addComponent(jLabel10)))
                                .addGroup(orderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(orderLayout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(clear_mbtn)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(orderLayout.createSequentialGroup()
                                        .addGap(46, 46, 46)
                                        .addGroup(orderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(orderLayout.createSequentialGroup()
                                                .addComponent(crust_cmb, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                            .addGroup(orderLayout.createSequentialGroup()
                                                .addGroup(orderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(name_mtxt)
                                                    .addComponent(cmb_m, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(id_mtxt)
                                                    .addComponent(price_mtxt, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(orderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(orderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING))
                                                    .addGroup(orderLayout.createSequentialGroup()
                                                        .addGap(37, 37, 37)
                                                        .addComponent(category_cmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                                .addGap(18, 18, 18))
                            .addGroup(orderLayout.createSequentialGroup()
                                .addGroup(orderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(pizzabyId_btn)
                                    .addGroup(orderLayout.createSequentialGroup()
                                        .addGroup(orderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel17)
                                            .addComponent(jLabel16))
                                        .addGap(62, 62, 62)
                                        .addGroup(orderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(chesse_cmb, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(top_cmb, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(72, 72, 72)))
                                .addGap(188, 188, 188)))
                        .addGroup(orderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(user_mtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(address_mtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(orderLayout.createSequentialGroup()
                                .addComponent(search_mtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(search_mbtn)))
                        .addGap(65, 65, Short.MAX_VALUE))))
        );
        orderLayout.setVerticalGroup(
            orderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(orderLayout.createSequentialGroup()
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(orderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(orderLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                        .addGroup(orderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(id_mtxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addGap(18, 18, 18)
                        .addGroup(orderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(name_mtxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(orderLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(orderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(address_mtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(orderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(orderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cmb_m, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(user_mtxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel15))
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(orderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10)
                    .addComponent(price_mtxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(orderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(category_cmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(search_mtxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(search_mbtn))
                .addGap(1, 1, 1)
                .addGroup(orderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(crust_cmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(orderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chesse_cmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(orderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(top_cmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addGroup(orderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(order_mbtn)
                    .addComponent(clear_mbtn)
                    .addComponent(pizzabyId_btn))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("tab2", order);

        ordertrack.setBackground(new java.awt.Color(255, 255, 255));

        jLabel18.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel18.setText("Search by name :");

        ok_orbtn.setBackground(new java.awt.Color(255, 51, 51));
        ok_orbtn.setText("ok");
        ok_orbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ok_orbtnActionPerformed(evt);
            }
        });

        orddertrack_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Order ID", "Name", "Pizza name", "Order Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(orddertrack_table);
        if (orddertrack_table.getColumnModel().getColumnCount() > 0) {
            orddertrack_table.getColumnModel().getColumn(0).setResizable(false);
            orddertrack_table.getColumnModel().getColumn(1).setResizable(false);
            orddertrack_table.getColumnModel().getColumn(2).setResizable(false);
            orddertrack_table.getColumnModel().getColumn(3).setResizable(false);
        }

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel21.setText("Tracking the Order");

        javax.swing.GroupLayout ordertrackLayout = new javax.swing.GroupLayout(ordertrack);
        ordertrack.setLayout(ordertrackLayout);
        ordertrackLayout.setHorizontalGroup(
            ordertrackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
            .addGroup(ordertrackLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addGap(34, 34, 34)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(ordertrackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21)
                    .addComponent(ok_orbtn))
                .addContainerGap(252, Short.MAX_VALUE))
        );
        ordertrackLayout.setVerticalGroup(
            ordertrackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ordertrackLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel21)
                .addGap(60, 60, 60)
                .addGroup(ordertrackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ok_orbtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 149, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("tab3", ordertrack);

        feedback.setBackground(new java.awt.Color(255, 255, 255));

        sub_fbtn.setBackground(new java.awt.Color(255, 51, 51));
        sub_fbtn.setText("Submit");
        sub_fbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sub_fbtnActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel19.setText("Name :");

        jLabel20.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel20.setText("Comment :");

        clear_fbtn.setBackground(new java.awt.Color(255, 51, 51));
        clear_fbtn.setText("Clear");
        clear_fbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clear_fbtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout feedbackLayout = new javax.swing.GroupLayout(feedback);
        feedback.setLayout(feedbackLayout);
        feedbackLayout.setHorizontalGroup(
            feedbackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(feedbackLayout.createSequentialGroup()
                .addGroup(feedbackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(feedbackLayout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, feedbackLayout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(44, 44, 44)
                .addGroup(feedbackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(feedbackLayout.createSequentialGroup()
                        .addComponent(sub_fbtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 136, Short.MAX_VALUE)
                        .addComponent(clear_fbtn))
                    .addComponent(name_ftxt)
                    .addComponent(cmt_ftxt))
                .addContainerGap(369, Short.MAX_VALUE))
        );
        feedbackLayout.setVerticalGroup(
            feedbackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(feedbackLayout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addGroup(feedbackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(name_ftxt, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19))
                .addGap(27, 27, 27)
                .addGroup(feedbackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmt_ftxt, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(feedbackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sub_fbtn)
                    .addComponent(clear_fbtn))
                .addContainerGap(209, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab4", feedback);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(255, -27, 800, 510));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        jTabbedPane1.setSelectedIndex(2);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        jTabbedPane1.setSelectedIndex(3);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        this.dispose();
        login home = new login();
        home.setVisible(true);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        jTabbedPane1.setSelectedIndex(0);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void id_mtxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_id_mtxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_id_mtxtActionPerformed

    private void order_mbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_order_mbtnActionPerformed
        // Declare variables to hold the form input
        String pizzaId, username, pizzaname, price, quantity, crust, cheese, toppings, address;

        try {
            // Check if the username exists in the database
            if ("".equals(user_mtxt.getText())) {
                JOptionPane.showMessageDialog(new JFrame(), "Username is required", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            username = user_mtxt.getText().trim();

            // Check if the username exists in the database
            if (!doesUsernameExist(username)) {
                JOptionPane.showMessageDialog(new JFrame(), "Username does not exist!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Collect other form data
            pizzaId = id_mtxt.getText();
            pizzaname = name_mtxt.getText();
            price = price_mtxt.getText();
            quantity = cmb_m.getSelectedItem().toString();
            crust = crust_cmb.getSelectedItem().toString();
            cheese = chesse_cmb.getSelectedItem().toString();
            toppings = top_cmb.getSelectedItem().toString();
            address = address_mtxt.getText();

            // Validate form fields
            if ("".equals(pizzaId)) {
                JOptionPane.showMessageDialog(new JFrame(), "PizzaId is required", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } else if ("".equals(pizzaname)) {
                JOptionPane.showMessageDialog(new JFrame(), "Pizza name is required", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } else if ("".equals(price)) {
                JOptionPane.showMessageDialog(new JFrame(), "Price is required", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } else if ("".equals(address)) {
                JOptionPane.showMessageDialog(new JFrame(), "Address is required", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Insert the order into the database
            String query = "INSERT INTO orderplaced (pizzaId, username, pizzaname, price, quantity, crust, cheese, toppings, address) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            // Prepare and execute the statement
            PreparedStatement pst = MySQL.connection.prepareStatement(query);
            pst.setString(1, pizzaId);
            pst.setString(2, username);  // Store the username
            pst.setString(3, pizzaname);
            pst.setString(4, price);
            pst.setString(5, quantity);
            pst.setString(6, crust);
            pst.setString(7, cheese);
            pst.setString(8, toppings);
            pst.setString(9, address);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Order placed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                // Clear form fields after successful order
                id_mtxt.setText("");
                name_mtxt.setText("");
                price_mtxt.setText("");
                cmb_m.setSelectedIndex(0);
                crust_cmb.setSelectedIndex(0);
                chesse_cmb.setSelectedIndex(0);
                top_cmb.setSelectedIndex(0);
                address_mtxt.setText("");
                
                Payment home = new Payment();
                home.setVisible(true);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }


    }//GEN-LAST:event_order_mbtnActionPerformed

    private void user_mtxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_user_mtxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_user_mtxtActionPerformed

    private void search_mbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_search_mbtnActionPerformed
        // Get the selected category from the JComboBox
        String category = category_cmb.getSelectedItem().toString();

        String pizzaName = search_mtxt.getText();

        searchPizzas(category, pizzaName);
    }//GEN-LAST:event_search_mbtnActionPerformed

    private void clear_mbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clear_mbtnActionPerformed
        id_mtxt.setText(""); //clear textboxes
        name_mtxt.setText("");
        price_mtxt.setText("");
        address_mtxt.setText("");
        search_mtxt.setText("");
        user_mtxt.setText("");
        category_cmb.setSelectedIndex(0);
        updatePizzaTable();
    }//GEN-LAST:event_clear_mbtnActionPerformed

    private void pizzabyId_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pizzabyId_btnActionPerformed
        String pizzaId = id_mtxt.getText();

        if (pizzaId.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter a pizza ID", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        getPizzaDetailsById(pizzaId);
    }//GEN-LAST:event_pizzabyId_btnActionPerformed

    private void sub_fbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sub_fbtnActionPerformed
        String name, comment;
        try {
            Connection connection = MySQL.connection;

            if ("".equals(name_ftxt.getText())) {
                JOptionPane.showMessageDialog(new JFrame(), "Name is required", "Error", JOptionPane.ERROR_MESSAGE);
            } else if ("".equals(name_ftxt.getText())) {
                JOptionPane.showMessageDialog(new JFrame(), "Comment is required", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                name = name_ftxt.getText(); //getting data
                comment = cmt_ftxt.getText();

                String query = "INSERT INTO feedback (name, comments) VALUES (?, ?)";

                PreparedStatement pst = connection.prepareStatement(query);
                pst.setString(1, name);
                pst.setString(2, comment);

                int rowsAffected = pst.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(new JFrame(), "Feedback submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                    name_ftxt.setText("");
                    cmt_ftxt.setText("");
                } else {
                    JOptionPane.showMessageDialog(new JFrame(), "Failed to submit feedback. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(new JFrame(), "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_sub_fbtnActionPerformed

    private void clear_fbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clear_fbtnActionPerformed
        name_ftxt.setText(""); //clear textboxes
        name_ftxt.setText("");

    }//GEN-LAST:event_clear_fbtnActionPerformed

    private void ok_orbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ok_orbtnActionPerformed
        String username = jTextField1.getText().trim();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(new JFrame(), "Please enter a username to search", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            searchAndDisplayOrderStatus(username);
        }
    }//GEN-LAST:event_ok_orbtnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(member.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(member.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(member.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(member.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new member().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField address_mtxt;
    private javax.swing.JComboBox<String> category_cmb;
    private javax.swing.JComboBox<String> chesse_cmb;
    private javax.swing.JButton clear_fbtn;
    private javax.swing.JButton clear_mbtn;
    private javax.swing.JComboBox<String> cmb_m;
    private javax.swing.JTextField cmt_ftxt;
    private javax.swing.JComboBox<String> crust_cmb;
    private javax.swing.JPanel feedback;
    private javax.swing.JPanel homep;
    private javax.swing.JTextField id_mtxt;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField name_ftxt;
    private javax.swing.JTextField name_mtxt;
    private javax.swing.JButton ok_orbtn;
    private javax.swing.JTable orddertrack_table;
    private javax.swing.JPanel order;
    private javax.swing.JButton order_mbtn;
    private javax.swing.JPanel ordertrack;
    private javax.swing.JButton pizzabyId_btn;
    private javax.swing.JTable pizzaveiw_table;
    private javax.swing.JTextField price_mtxt;
    private javax.swing.JButton search_mbtn;
    private javax.swing.JTextField search_mtxt;
    private javax.swing.JButton sub_fbtn;
    private javax.swing.JComboBox<String> top_cmb;
    private javax.swing.JTextField user_mtxt;
    // End of variables declaration//GEN-END:variables
}
