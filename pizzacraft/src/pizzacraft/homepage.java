package pizzacraft;

import com.mysql.cj.protocol.Resultset;
import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.io.File;
import java.io.FileInputStream;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.table.DefaultTableModel;
import model.MySQL;

/**
 *
 * @author shash
 */
public class homepage extends javax.swing.JFrame {

    public homepage() {
        initComponents();
        updateUserTable();
        displayAllOrders();
        updateFeedbackTable();

    }


    private void updateFeedbackTable() {

        String query = "SELECT name, comments FROM feedback";

        String url = "jdbc:mysql://localhost:3306/pizzacraft";
        String username = "root";
        String password = "";

        try (Connection con = DriverManager.getConnection(url, username, password); Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            String[] columnNames = {"Name", "Comment"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);

            while (rs.next()) {
                String name = rs.getString("name");
                String comment = rs.getString("comments");

                Object[] row = {name, comment};
                model.addRow(row);
            }

            jTable4.setModel(model);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void deleteUserById(String userId) {

        String query = "DELETE FROM users WHERE userId = ?";

        String url = "jdbc:mysql://localhost:3306/pizzacraft";
        String username = "root";
        String password = "";

        try (Connection con = DriverManager.getConnection(url, username, password); PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, userId);

            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "User deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "User not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void displayAllOrders() {
        String query = "SELECT orderId, username, pizzaname, price, address FROM orderplaced";

        try {

            ResultSet rs = MySQL.execute(query);

            String[] columnNames = {"Order ID", "Customer Name", "Pizza Name", "Price", "Address"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);

            while (rs != null && rs.next()) {
                String orderId = rs.getString("orderId");
                String customerName = rs.getString("username");
                String pizzaName = rs.getString("pizzaname");
                double price = rs.getDouble("price");
                String address = rs.getString("address");

                Object[] row = {orderId, customerName, pizzaName, price, address};
                model.addRow(row);
            }

            adminOrdersTable.setModel(model);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(new JFrame(), "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void deleteOrder(String orderId, String customerName) {
        String query = null;

        try {
            if (!orderId.isEmpty()) {
                query = "DELETE FROM orderplaced WHERE orderId = ?";
            } else if (!customerName.isEmpty()) {
                query = "DELETE FROM orderplaced WHERE username = ?";
            } else {
                JOptionPane.showMessageDialog(new JFrame(), "Please provide either an Order ID or Customer Name to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(new JFrame(), "Are you sure you want to delete this order?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            PreparedStatement pst = MySQL.connection.prepareStatement(query);

            if (!orderId.isEmpty()) {
                pst.setString(1, orderId);
            } else {
                pst.setString(1, customerName);
            }

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(new JFrame(), "Order deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                displayAllOrders(); // Refresh the table
            } else {
                JOptionPane.showMessageDialog(new JFrame(), "No matching orders found to delete.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void getUserDetailsByIdOrName(String userIdOrName) {
        String query = "SELECT * FROM users WHERE userId = '" + userIdOrName + "' OR name = '" + userIdOrName + "'";

        try {
            ResultSet rs = MySQL.execute(query);

            if (rs != null && rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");

                name_txtm.setText(name);
                email_txtm.setText(email);
                phone_txtm.setText(phone);

            } else {
                JOptionPane.showMessageDialog(null, "User not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
        private void updateUserById(String userId, String newName, String newEmail, String newPhoneNumber) {

        String query = "UPDATE users SET name = ?, email = ?, phone = ? WHERE userId = ?";

        try (Connection con = MySQL.connection) {

            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, newName);
            pst.setString(2, newEmail);
            pst.setString(3, newPhoneNumber);
            pst.setString(4, userId);

            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "User updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "User not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void updateUserTable() {
        String query = "SELECT * FROM users";

        try {
            ResultSet rs = MySQL.execute(query);
            String[] columnNames = {"userId", "name", "phone", "email", "loyaltyPoints"};

            DefaultTableModel model = new DefaultTableModel(columnNames, 0);

            while (rs.next()) {
                String userId = rs.getString("userId");
                String username = rs.getString("name");
                String phone = rs.getString("email");
                String email = rs.getString("phone");
                String loyaltyPoints = rs.getString("loyaltyPoints");
                Object[] row = {userId, username, email, phone, loyaltyPoints};
                model.addRow(row);
            }

            userTable.setModel(model);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void updatePizzaTable() {

        String query = "SELECT * FROM pizza";

        try (
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pizzacraft", "root", ""); Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            String[] columnNames = {"Pizza ID", "Pizza Name", "Price", "Category", "Description"};

            DefaultTableModel model = new DefaultTableModel(columnNames, 0);

            while (rs.next()) {
                String pizzaId = rs.getString("pizzaId");
                String pizzaName = rs.getString("pizzaname");
                double price = rs.getDouble("price");
                String category = rs.getString("category");
                String description = rs.getString("description");

                Object[] row = {pizzaId, pizzaName, price, category, description};
                model.addRow(row);
            }

            pizzaTable.setModel(model);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel7 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        addpizza = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        pizzaid_txt = new javax.swing.JTextField();
        pizza_txt = new javax.swing.JTextField();
        img_txt = new javax.swing.JTextField();
        price_txt = new javax.swing.JTextField();
        des_txt = new javax.swing.JTextField();
        cat_combo = new javax.swing.JComboBox<>();
        add_btn = new javax.swing.JButton();
        clear_btn = new javax.swing.JButton();
        get_btn = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        logout_btn = new javax.swing.JButton();
        editpizza = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        pizzaid_txtedt = new javax.swing.JTextField();
        pizza_nameedt = new javax.swing.JTextField();
        image_edt = new javax.swing.JTextField();
        price_edt = new javax.swing.JTextField();
        des_textedt = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        update_btnedt = new javax.swing.JButton();
        clear_btnedt = new javax.swing.JButton();
        delete_btnedt = new javax.swing.JButton();
        get_btnedt = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        pizzaTable = new javax.swing.JTable();
        pizzaid_search = new javax.swing.JTextField();
        search_btnedt = new javax.swing.JButton();
        cmb_catedt = new javax.swing.JComboBox<>();
        order = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        adminOrdersTable = new javax.swing.JTable();
        jLabel16 = new javax.swing.JLabel();
        delete_txt = new javax.swing.JTextField();
        del_btn = new javax.swing.JButton();
        orderadmin_search = new javax.swing.JTextField();
        search_idbtn = new javax.swing.JButton();
        member = new javax.swing.JPanel();
        jTextField2 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        name_txtm = new javax.swing.JTextField();
        email_txtm = new javax.swing.JTextField();
        phone_txtm = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        userTable = new javax.swing.JTable();
        clear_membtn = new javax.swing.JButton();
        edit_membtn = new javax.swing.JButton();
        delete_membtn = new javax.swing.JButton();
        search_membtn = new javax.swing.JButton();
        feedback = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();

        jLabel7.setText("jLabel7");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane1.setBackground(new java.awt.Color(255, 51, 51));
        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        jTabbedPane1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N

        addpizza.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("PizzaId :");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("Pizza Name :");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("Image :");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setText("Price :");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setText("Category :");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setText("Description :");

        des_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                des_txtActionPerformed(evt);
            }
        });

        cat_combo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Veg", "Non-Veg" }));
        cat_combo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cat_comboActionPerformed(evt);
            }
        });

        add_btn.setBackground(new java.awt.Color(255, 51, 51));
        add_btn.setText("Add");
        add_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                add_btnActionPerformed(evt);
            }
        });

        clear_btn.setBackground(new java.awt.Color(255, 51, 51));
        clear_btn.setText("Clear");
        clear_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clear_btnActionPerformed(evt);
            }
        });

        get_btn.setBackground(new java.awt.Color(255, 51, 51));
        get_btn.setText("Get");
        get_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                get_btnActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel8.setText("Add Pizza");

        jLabel24.setBackground(new java.awt.Color(255, 255, 255));
        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pizzacraft/PizzaCraft_2 (1).png"))); // NOI18N

        logout_btn.setBackground(new java.awt.Color(255, 51, 51));
        logout_btn.setText("Logout");
        logout_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logout_btnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout addpizzaLayout = new javax.swing.GroupLayout(addpizza);
        addpizza.setLayout(addpizzaLayout);
        addpizzaLayout.setHorizontalGroup(
            addpizzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addpizzaLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 103, Short.MAX_VALUE)
                .addGroup(addpizzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addpizzaLayout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(img_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(get_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(addpizzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(addpizzaLayout.createSequentialGroup()
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(pizza_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(addpizzaLayout.createSequentialGroup()
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(addpizzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel8)
                                .addComponent(pizzaid_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(addpizzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, addpizzaLayout.createSequentialGroup()
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(cat_combo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, addpizzaLayout.createSequentialGroup()
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(price_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(addpizzaLayout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(addpizzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(des_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(addpizzaLayout.createSequentialGroup()
                                .addComponent(add_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(clear_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(53, 53, 53))
            .addGroup(addpizzaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(logout_btn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        addpizzaLayout.setVerticalGroup(
            addpizzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addpizzaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addGap(25, 25, 25)
                .addGroup(addpizzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pizzaid_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(addpizzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addpizzaLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(addpizzaLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(addpizzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pizza_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(addpizzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(img_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(get_btn))
                        .addGap(18, 18, 18)
                        .addGroup(addpizzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(price_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(addpizzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(cat_combo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(addpizzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(des_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29)
                        .addGroup(addpizzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(add_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(clear_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(logout_btn)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Add Pizza", addpizza);

        editpizza.setBackground(new java.awt.Color(255, 255, 255));

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel9.setText("PizzaId :");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel10.setText("Pizza Name :");

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel11.setText("Image :");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel12.setText("Price :");

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel13.setText("Category :");

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel14.setText("Description :");

        pizzaid_txtedt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pizzaid_txtedtActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel15.setText("Edit Pizza");

        update_btnedt.setBackground(new java.awt.Color(255, 51, 51));
        update_btnedt.setText("Update");
        update_btnedt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                update_btnedtActionPerformed(evt);
            }
        });

        clear_btnedt.setBackground(new java.awt.Color(255, 51, 51));
        clear_btnedt.setText("Clear");
        clear_btnedt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clear_btnedtActionPerformed(evt);
            }
        });

        delete_btnedt.setBackground(new java.awt.Color(255, 51, 51));
        delete_btnedt.setText("Delete");

        get_btnedt.setText("Get");
        get_btnedt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                get_btnedtActionPerformed(evt);
            }
        });

        pizzaTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "PizzaID", "Pizza name", "Image", "Price", "Category", "Description"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(pizzaTable);
        if (pizzaTable.getColumnModel().getColumnCount() > 0) {
            pizzaTable.getColumnModel().getColumn(0).setResizable(false);
            pizzaTable.getColumnModel().getColumn(1).setResizable(false);
            pizzaTable.getColumnModel().getColumn(2).setResizable(false);
            pizzaTable.getColumnModel().getColumn(3).setResizable(false);
            pizzaTable.getColumnModel().getColumn(4).setResizable(false);
            pizzaTable.getColumnModel().getColumn(5).setResizable(false);
        }

        search_btnedt.setText("Search");
        search_btnedt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                search_btnedtActionPerformed(evt);
            }
        });

        cmb_catedt.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Veg", "Non-Veg" }));

        javax.swing.GroupLayout editpizzaLayout = new javax.swing.GroupLayout(editpizza);
        editpizza.setLayout(editpizzaLayout);
        editpizzaLayout.setHorizontalGroup(
            editpizzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editpizzaLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(editpizzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, editpizzaLayout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(322, 322, 322))
                    .addGroup(editpizzaLayout.createSequentialGroup()
                        .addGroup(editpizzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(editpizzaLayout.createSequentialGroup()
                                .addGroup(editpizzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12))
                                .addGap(67, 67, 67)
                                .addGroup(editpizzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(price_edt, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(pizza_nameedt, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(pizzaid_txtedt, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(editpizzaLayout.createSequentialGroup()
                                        .addComponent(image_edt, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(get_btnedt))))
                            .addComponent(jLabel11)
                            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, editpizzaLayout.createSequentialGroup()
                                .addGroup(editpizzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel14))
                                .addGap(44, 44, 44)
                                .addGroup(editpizzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(editpizzaLayout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(des_textedt, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, editpizzaLayout.createSequentialGroup()
                                        .addGroup(editpizzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(cmb_catedt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, editpizzaLayout.createSequentialGroup()
                                                .addComponent(update_btnedt)
                                                .addGap(27, 27, 27)
                                                .addComponent(clear_btnedt)
                                                .addGap(18, 18, 18)
                                                .addComponent(delete_btnedt)))
                                        .addGap(0, 0, Short.MAX_VALUE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(editpizzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, editpizzaLayout.createSequentialGroup()
                        .addComponent(pizzaid_search, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(search_btnedt)
                        .addGap(38, 38, 38))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 477, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        editpizzaLayout.setVerticalGroup(
            editpizzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editpizzaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(editpizzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addGroup(editpizzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(pizzaid_search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(search_btnedt)))
                .addGap(24, 24, 24)
                .addGroup(editpizzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(editpizzaLayout.createSequentialGroup()
                        .addGroup(editpizzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pizzaid_txtedt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(editpizzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pizza_nameedt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(editpizzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(image_edt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(get_btnedt))
                        .addGap(18, 18, 18)
                        .addGroup(editpizzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(price_edt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(editpizzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(cmb_catedt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(editpizzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(editpizzaLayout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(jLabel14))
                            .addGroup(editpizzaLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(des_textedt, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(editpizzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(update_btnedt)
                            .addComponent(clear_btnedt)
                            .addComponent(delete_btnedt))
                        .addGap(30, 30, 30))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 431, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab(" Edit Pizza", editpizza);

        order.setBackground(new java.awt.Color(255, 255, 255));

        adminOrdersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "OrderID", "Cutomer name", "Pizza name", "Price", "Address"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class
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
        jScrollPane2.setViewportView(adminOrdersTable);
        if (adminOrdersTable.getColumnModel().getColumnCount() > 0) {
            adminOrdersTable.getColumnModel().getColumn(0).setResizable(false);
            adminOrdersTable.getColumnModel().getColumn(1).setResizable(false);
            adminOrdersTable.getColumnModel().getColumn(2).setResizable(false);
            adminOrdersTable.getColumnModel().getColumn(3).setResizable(false);
            adminOrdersTable.getColumnModel().getColumn(4).setResizable(false);
        }

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel16.setText("Orders");

        del_btn.setBackground(new java.awt.Color(255, 51, 51));
        del_btn.setText("Delete");
        del_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                del_btnActionPerformed(evt);
            }
        });

        search_idbtn.setBackground(new java.awt.Color(255, 51, 51));
        search_idbtn.setText("Search");
        search_idbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                search_idbtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout orderLayout = new javax.swing.GroupLayout(order);
        order.setLayout(orderLayout);
        orderLayout.setHorizontalGroup(
            orderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
            .addGroup(orderLayout.createSequentialGroup()
                .addGap(407, 407, 407)
                .addComponent(jLabel16)
                .addGap(33, 33, 33)
                .addComponent(delete_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(del_btn)
                .addGap(18, 18, 18)
                .addComponent(orderadmin_search, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(search_idbtn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        orderLayout.setVerticalGroup(
            orderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, orderLayout.createSequentialGroup()
                .addGroup(orderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(orderLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, orderLayout.createSequentialGroup()
                        .addContainerGap(87, Short.MAX_VALUE)
                        .addGroup(orderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(delete_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(del_btn)
                            .addComponent(orderadmin_search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(search_idbtn))
                        .addGap(24, 24, 24)))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("Order", order);

        member.setBackground(new java.awt.Color(255, 255, 255));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel17.setText("User Id :");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel18.setText("Name :");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel19.setText("Email :");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel21.setText("Phone :");

        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel22.setText("Members");

        userTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "User ID", "Name", "Phone", "Email "
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Object.class
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
        jScrollPane3.setViewportView(userTable);
        if (userTable.getColumnModel().getColumnCount() > 0) {
            userTable.getColumnModel().getColumn(0).setResizable(false);
            userTable.getColumnModel().getColumn(1).setResizable(false);
            userTable.getColumnModel().getColumn(2).setResizable(false);
            userTable.getColumnModel().getColumn(3).setResizable(false);
        }

        clear_membtn.setBackground(new java.awt.Color(255, 51, 51));
        clear_membtn.setText("Clear");
        clear_membtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clear_membtnActionPerformed(evt);
            }
        });

        edit_membtn.setBackground(new java.awt.Color(255, 51, 51));
        edit_membtn.setText("Edit");
        edit_membtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edit_membtnActionPerformed(evt);
            }
        });

        delete_membtn.setBackground(new java.awt.Color(255, 51, 51));
        delete_membtn.setText("Delete");
        delete_membtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delete_membtnActionPerformed(evt);
            }
        });

        search_membtn.setBackground(new java.awt.Color(255, 51, 51));
        search_membtn.setText("Search");
        search_membtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                search_membtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout memberLayout = new javax.swing.GroupLayout(member);
        member.setLayout(memberLayout);
        memberLayout.setHorizontalGroup(
            memberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(memberLayout.createSequentialGroup()
                .addGap(337, 337, 337)
                .addComponent(jLabel22)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(memberLayout.createSequentialGroup()
                .addGroup(memberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(memberLayout.createSequentialGroup()
                        .addGap(147, 147, 147)
                        .addComponent(clear_membtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(delete_membtn, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addGroup(memberLayout.createSequentialGroup()
                        .addGroup(memberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(memberLayout.createSequentialGroup()
                                .addGap(147, 147, 147)
                                .addComponent(search_membtn, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edit_membtn, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(memberLayout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addGroup(memberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(memberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(memberLayout.createSequentialGroup()
                                            .addGroup(memberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel17)
                                                .addComponent(jLabel18))
                                            .addGap(51, 51, 51))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, memberLayout.createSequentialGroup()
                                            .addComponent(jLabel19)
                                            .addGap(65, 65, 65)))
                                    .addGroup(memberLayout.createSequentialGroup()
                                        .addGap(3, 3, 3)
                                        .addComponent(jLabel21)
                                        .addGap(54, 54, 54)))
                                .addGroup(memberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(phone_txtm, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(email_txtm, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(name_txtm, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)))
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 602, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        memberLayout.setVerticalGroup(
            memberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(memberLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22)
                .addGap(24, 24, 24)
                .addGroup(memberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(memberLayout.createSequentialGroup()
                        .addGroup(memberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(memberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(name_txtm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18))
                        .addGap(21, 21, 21)
                        .addGroup(memberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(memberLayout.createSequentialGroup()
                                .addComponent(email_txtm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(memberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel21)
                                    .addComponent(phone_txtm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel19))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(memberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(edit_membtn)
                            .addComponent(search_membtn))
                        .addGap(18, 18, 18)
                        .addGroup(memberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(clear_membtn)
                            .addComponent(delete_membtn))
                        .addGap(104, 104, 104))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 431, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Member", member);

        feedback.setBackground(new java.awt.Color(255, 255, 255));

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel23.setText("Feedbacks");

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Name", "Comments"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(jTable4);
        if (jTable4.getColumnModel().getColumnCount() > 0) {
            jTable4.getColumnModel().getColumn(0).setResizable(false);
            jTable4.getColumnModel().getColumn(1).setResizable(false);
        }

        javax.swing.GroupLayout feedbackLayout = new javax.swing.GroupLayout(feedback);
        feedback.setLayout(feedbackLayout);
        feedbackLayout.setHorizontalGroup(
            feedbackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(feedbackLayout.createSequentialGroup()
                .addGap(374, 374, 374)
                .addComponent(jLabel23)
                .addContainerGap(437, Short.MAX_VALUE))
            .addComponent(jScrollPane4)
        );
        feedbackLayout.setVerticalGroup(
            feedbackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(feedbackLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 72, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 383, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("Feedback", feedback);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void pizzaid_txtedtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pizzaid_txtedtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pizzaid_txtedtActionPerformed

    private void get_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_get_btnActionPerformed
        JFileChooser chooser = new JFileChooser(); //Browsing and inserting an image
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        filename = f.getAbsolutePath();
        img_txt.setText(filename);

        try {
            File image = new File(filename);
            FileInputStream fis = new FileInputStream(image);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum);
            }
            photo = bos.toByteArray();
        } catch (Exception e) {

        }
    }//GEN-LAST:event_get_btnActionPerformed

    private void clear_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clear_btnActionPerformed
        pizzaid_txt.setText(""); //clear textboxes
        pizza_txt.setText("");
        img_txt.setText("");
        price_txt.setText("");
        des_txt.setText("");


    }//GEN-LAST:event_clear_btnActionPerformed

    private void des_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_des_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_des_txtActionPerformed

    private void add_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_add_btnActionPerformed
        String pizzaId, pizzaname, price, category, description;
        String SUrl, SUser, SPass;
        SUrl = "jdbc:mysql://localhost:3306/pizzacraft"; // database connection
        SUser = "root";
        SPass = "";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            java.sql.Connection con = DriverManager.getConnection(SUrl, SUser, SPass);
            System.out.println("Database connected successfully!"); //checking database connection

            Statement st = con.createStatement();
            {

            }
            if ("".equals(pizzaid_txt.getText())) {
                JOptionPane.showMessageDialog(new JFrame(), "PizzaId is required", "Error", JOptionPane.ERROR_MESSAGE);
            } else if ("".equals(pizza_txt.getText())) {
                JOptionPane.showMessageDialog(new JFrame(), "Pizza name is required", "Error", JOptionPane.ERROR_MESSAGE);
            } else if ("".equals(img_txt.getText())) {
                JOptionPane.showMessageDialog(new JFrame(), "Image is required", "Error", JOptionPane.ERROR_MESSAGE);
            } else if ("".equals(price_txt.getText())) {
                JOptionPane.showMessageDialog(new JFrame(), "Price is required", "Error", JOptionPane.ERROR_MESSAGE);
            } else if ("".equals(des_txt.getText())) {
                JOptionPane.showMessageDialog(new JFrame(), "Description is required", "Error", JOptionPane.ERROR_MESSAGE);
            } else {

                pizzaId = pizzaid_txt.getText(); //getting data
                pizzaname = pizza_txt.getText();
                price = price_txt.getText();
                category = cat_combo.getSelectedItem().toString();
                description = des_txt.getText();

                String query = "INSERT INTO pizza (pizzaId, pizzaname, image, price, category, description) VALUES (?, ?, ?, ?, ?, ?)";

                PreparedStatement pst = con.prepareStatement(query);
                pst.setString(1, pizzaId);
                pst.setString(2, pizzaname);

                // Set image as a binary stream
                File imageFile = new File(img_txt.getText());
                FileInputStream fis = new FileInputStream(imageFile);
                pst.setBinaryStream(3, fis, (int) imageFile.length());

                pst.setString(4, price);
                pst.setString(5, category);
                pst.setString(6, description);

                pst.executeUpdate();

                st.execute(query);
                pizzaid_txt.setText(""); // setting data
                pizza_txt.setText("");
                img_txt.setText("");
                cat_combo.setSelectedIndex(0);
                price_txt.setText("");
                showMessageDialog(null, "Pizza added successfully!");
            }
            ;
        } catch (Exception e) {
            System.out.println("Error!" + e.getMessage());
        }
    }//GEN-LAST:event_add_btnActionPerformed

    private void cat_comboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cat_comboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cat_comboActionPerformed

    private void search_membtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_search_membtnActionPerformed

        String userIdOrName = jTextField2.getText().trim();

        if (!userIdOrName.isEmpty()) {
            getUserDetailsByIdOrName(userIdOrName);
        } else {
            JOptionPane.showMessageDialog(null, "Please enter a User ID or Username to search.", "Input Error", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_search_membtnActionPerformed

    private void search_btnedtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_search_btnedtActionPerformed
        String pizzaId = pizzaid_search.getText().trim();

        // Input validation
        if (pizzaId.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Pizza ID is required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // SQL query
        String query = "SELECT * FROM pizza WHERE pizzaId = ?";

        try (
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pizzacraft", "root", ""); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, pizzaId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                pizzaid_txtedt.setText(rs.getString("pizzaId"));
                pizza_nameedt.setText(rs.getString("pizzaname"));

                // Retrieve price as DOUBLE and convert it to a string
                double price = rs.getDouble("price");
                if (price == 0) { // Handle case where price might be 0
                    JOptionPane.showMessageDialog(null, "Price is missing in the database for Pizza ID: " + pizzaId, "Data Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                price_edt.setText(String.valueOf(price)); // Display the price as a string in the text field

                cmb_catedt.setSelectedItem(rs.getString("category"));
                des_textedt.setText(rs.getString("description"));
            } else {
                JOptionPane.showMessageDialog(null, "Pizza not found!", "Search Result", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_search_btnedtActionPerformed

    private void clear_btnedtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clear_btnedtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_clear_btnedtActionPerformed

    private void update_btnedtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_update_btnedtActionPerformed
        // Get input values
        String pizzaId = pizzaid_txtedt.getText().trim();
        String pizzaname = pizza_nameedt.getText().trim();
        String priceText = price_edt.getText().trim();
        String cmb = cmb_catedt.getSelectedItem().toString();
        String description = des_textedt.getText().trim();
        String imagePath = image_edt.getText().trim(); // Get image path

        // Debug: Log field values
        System.out.println("Pizza ID: " + pizzaId);
        System.out.println("Pizza Name: " + pizzaname);
        System.out.println("Price: " + priceText);
        System.out.println("Category: " + cmb);
        System.out.println("Description: " + description);
        System.out.println("Image Path: " + imagePath);

        // Input validation
        if (pizzaId.isEmpty() || pizzaname.isEmpty() || priceText.isEmpty() || description.isEmpty() || imagePath.isEmpty()) {
            JOptionPane.showMessageDialog(null, "All fields are required, including the image!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double price = 0;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid price format! Please enter a valid number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate image path and check if file exists
        File imageFile = new File(imagePath);
        if (!imageFile.exists() || !imageFile.isFile()) {
            JOptionPane.showMessageDialog(null, "Invalid image path! Please select a valid image file.", "File Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // SQL query for updating pizza data including image
        String query = "UPDATE pizza SET pizzaname = ?, price = ?, category = ?, description = ?, image = ? WHERE pizzaId = ?";

        try (
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pizzacraft", "root", ""); PreparedStatement pst = con.prepareStatement(query); FileInputStream fis = new FileInputStream(imageFile) // Open the image file as InputStream
                ) {
            // Set query parameters
            pst.setString(1, pizzaname);
            pst.setDouble(2, price);  // Set price as a double
            pst.setString(3, cmb);
            pst.setString(4, description);
            pst.setBinaryStream(5, fis, (int) imageFile.length());  // Set image file as binary stream
            pst.setString(6, pizzaId);

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Pizza record updated successfully", "Update Success", JOptionPane.INFORMATION_MESSAGE);
                updatePizzaTable(); // Update the table after successful update
            } else {
                JOptionPane.showMessageDialog(null, "No record found with Pizza ID: " + pizzaId, "Update Failed", JOptionPane.WARNING_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_update_btnedtActionPerformed

    private void get_btnedtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_get_btnedtActionPerformed
        JFileChooser chooser = new JFileChooser(); // Browsing and inserting an image
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();

        if (f != null) {
            filename = f.getAbsolutePath();
            image_edt.setText(filename);

            try {
                File image = new File(filename);
                FileInputStream fis = new FileInputStream(image);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                for (int readNum; (readNum = fis.read(buf)) != -1;) {
                    bos.write(buf, 0, readNum);
                }
                photo = bos.toByteArray();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error reading image: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
                System.err.println("File Error: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "No file selected!", "File Selection", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_get_btnedtActionPerformed

    private void logout_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logout_btnActionPerformed
        this.dispose();
        login home = new login();
        home.setVisible(true);
    }//GEN-LAST:event_logout_btnActionPerformed

    private void search_idbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_search_idbtnActionPerformed

        String userIdOrName = orderadmin_search.getText().trim();

        if (!userIdOrName.isEmpty()) {
            getUserDetailsByIdOrName(userIdOrName);
        } else {
            JOptionPane.showMessageDialog(null, "Please enter a User ID or Username to search.", "Input Error", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_search_idbtnActionPerformed

    private void del_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_del_btnActionPerformed
        String orderId = delete_txt.getText().trim();
        String customerName = delete_txt.getText().trim();

        if (orderId.isEmpty() && customerName.isEmpty()) {
            JOptionPane.showMessageDialog(new JFrame(), "Please provide either an Order ID or Customer Name to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {

            deleteOrder(orderId, customerName);
        }
    }//GEN-LAST:event_del_btnActionPerformed

    private void clear_membtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clear_membtnActionPerformed
        jTextField2.setText("");
        name_txtm.setText("");        
        email_txtm.setText(""); 
        phone_txtm.setText(""); 
        
    }//GEN-LAST:event_clear_membtnActionPerformed

    private void delete_membtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delete_membtnActionPerformed
        String userId = jTextField2.getText().trim();

        if (userId.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter a User ID.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            deleteUserById(userId);
        }
    }//GEN-LAST:event_delete_membtnActionPerformed

    private void edit_membtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edit_membtnActionPerformed
        String userId = jTextField2.getText().trim();  
        String newName = name_txtm.getText().trim();    
        String newEmail = email_txtm.getText().trim();  
        String newPhoneNumber = phone_txtm.getText().trim(); 

        if (userId.isEmpty() || newName.isEmpty() || newEmail.isEmpty() || newPhoneNumber.isEmpty()) {
            JOptionPane.showMessageDialog(null, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            updateUserById(userId, newName, newEmail, newPhoneNumber); 
        }
    }//GEN-LAST:event_edit_membtnActionPerformed

    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(homepage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(homepage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(homepage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(homepage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new homepage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton add_btn;
    private javax.swing.JPanel addpizza;
    private javax.swing.JTable adminOrdersTable;
    private javax.swing.JComboBox<String> cat_combo;
    private javax.swing.JButton clear_btn;
    private javax.swing.JButton clear_btnedt;
    private javax.swing.JButton clear_membtn;
    private javax.swing.JComboBox<String> cmb_catedt;
    private javax.swing.JButton del_btn;
    private javax.swing.JButton delete_btnedt;
    private javax.swing.JButton delete_membtn;
    private javax.swing.JTextField delete_txt;
    private javax.swing.JTextField des_textedt;
    private javax.swing.JTextField des_txt;
    private javax.swing.JButton edit_membtn;
    private javax.swing.JPanel editpizza;
    private javax.swing.JTextField email_txtm;
    private javax.swing.JPanel feedback;
    private javax.swing.JButton get_btn;
    private javax.swing.JButton get_btnedt;
    private javax.swing.JTextField image_edt;
    private javax.swing.JTextField img_txt;
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
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable4;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JButton logout_btn;
    private javax.swing.JPanel member;
    private javax.swing.JTextField name_txtm;
    private javax.swing.JPanel order;
    private javax.swing.JTextField orderadmin_search;
    private javax.swing.JTextField phone_txtm;
    private javax.swing.JTable pizzaTable;
    private javax.swing.JTextField pizza_nameedt;
    private javax.swing.JTextField pizza_txt;
    private javax.swing.JTextField pizzaid_search;
    private javax.swing.JTextField pizzaid_txt;
    private javax.swing.JTextField pizzaid_txtedt;
    private javax.swing.JTextField price_edt;
    private javax.swing.JTextField price_txt;
    private javax.swing.JButton search_btnedt;
    private javax.swing.JButton search_idbtn;
    private javax.swing.JButton search_membtn;
    private javax.swing.JButton update_btnedt;
    private javax.swing.JTable userTable;
    // End of variables declaration//GEN-END:variables
byte[] photo = null;
    String filename = null;
}
