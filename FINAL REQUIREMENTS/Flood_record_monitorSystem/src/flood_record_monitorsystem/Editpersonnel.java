/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package flood_record_monitorsystem;

import java.awt.Image;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author User
 */
public class Editpersonnel extends javax.swing.JDialog {
    
    private int personnelId;
    private javax.swing.JFrame parentDashboard;
    private ButtonGroup roleButtonGroup;
    private String loggedInUsername;
private String loggedInRole;
private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Editpersonnel.class.getName());

// Update constructor:
public Editpersonnel(java.awt.Frame parent, boolean modal, String username, String role, int id, 
                     String fName, String mName, String lName, 
                     String uname, String userRole, javax.swing.JFrame dashboard) {
    super(parent, modal);
    initComponents();
    
    this.personnelId = id;
    this.parentDashboard = dashboard;
    this.loggedInUsername = username;
    this.loggedInRole = role;               
    
        // Group radio buttons so only one role can be selected
        roleButtonGroup = new ButtonGroup();
        roleButtonGroup.add(ANP_Admin_radiobtn);
        roleButtonGroup.add(ANP_Staff_radiobtn);
        
        // Populate existing data into the form fields
        ANP_firstname.setText(fName);
        ANP_middlename.setText(mName);
        ANP_lastname.setText(lName);
        ANP_username.setText(username);
        
        if (userRole.equalsIgnoreCase("Admin")) {
    ANP_Admin_radiobtn.setSelected(true);
} else if (userRole.equalsIgnoreCase("Staff")) {
    ANP_Staff_radiobtn.setSelected(true);
}
        
        // Load safe image icon
        safeSetIcon(person, "Person.png", 52, 56);
    }

    private void safeSetIcon(JLabel label, String path, int width, int height) {
        ImageIcon icon = loadSafeImageIcon(path, width, height);
        if (icon != null) {
            label.setIcon(icon);
        }
    }

    private ImageIcon loadSafeImageIcon(String path, int width, int height) {
        try {
            java.net.URL imgURL = getClass().getResource(path);
            if (imgURL == null) {
                imgURL = getClass().getResource("/flood_record_monitorsystem/" + path);
            }
            if (imgURL != null) {
                ImageIcon icon = new ImageIcon(imgURL);
                Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            }
        } catch (Exception e) {
            System.out.println("Could not load icon: " + path);
        }
        return null;
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        person = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        ANP_username = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        addbtn = new javax.swing.JButton();
        cancelbtn = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        ANP_lastname = new javax.swing.JTextField();
        ANP_firstname = new javax.swing.JTextField();
        ANP_middlename = new javax.swing.JTextField();
        ANP_Staff_radiobtn = new javax.swing.JRadioButton();
        ANP_Admin_radiobtn = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(244, 254, 241));

        jPanel8.setBackground(new java.awt.Color(204, 255, 204));
        jPanel8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        person.setBackground(new java.awt.Color(255, 255, 255));
        person.setIcon(new javax.swing.ImageIcon(getClass().getResource("/flood_record_monitorsystem/Person.png"))); // NOI18N

        jLabel12.setFont(new java.awt.Font("Perpetua", 1, 48)); // NOI18N
        jLabel12.setText("Edit Personnel");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(167, 167, 167)
                .addComponent(person, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(person, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );

        jLabel1.setFont(new java.awt.Font("Trebuchet MS", 0, 30)); // NOI18N
        jLabel1.setText("First Name:");

        jLabel14.setFont(new java.awt.Font("Perpetua", 1, 48)); // NOI18N
        jLabel14.setText("Account Details");

        jLabel2.setFont(new java.awt.Font("Trebuchet MS", 0, 30)); // NOI18N
        jLabel2.setText("Middle Name:");

        jLabel3.setFont(new java.awt.Font("Trebuchet MS", 0, 30)); // NOI18N
        jLabel3.setText("Last Name:");

        jLabel4.setFont(new java.awt.Font("Trebuchet MS", 0, 30)); // NOI18N
        jLabel4.setText("Username:");

        ANP_username.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Trebuchet MS", 0, 30)); // NOI18N
        jLabel6.setText("Role:");

        addbtn.setBackground(new java.awt.Color(255, 255, 153));
        addbtn.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        addbtn.setText("UPDATE");
        addbtn.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        addbtn.addActionListener(this::addbtnActionPerformed);

        cancelbtn.setBackground(new java.awt.Color(204, 204, 204));
        cancelbtn.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        cancelbtn.setText("CANCEL");
        cancelbtn.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cancelbtn.addActionListener(this::cancelbtnActionPerformed);

        jLabel16.setFont(new java.awt.Font("Perpetua", 1, 48)); // NOI18N
        jLabel16.setText("Personal Information");

        ANP_lastname.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N

        ANP_firstname.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N

        ANP_middlename.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        ANP_middlename.addActionListener(this::ANP_middlenameActionPerformed);

        ANP_Staff_radiobtn.setFont(new java.awt.Font("Trebuchet MS", 0, 24)); // NOI18N
        ANP_Staff_radiobtn.setText("Staff");

        ANP_Admin_radiobtn.setFont(new java.awt.Font("Trebuchet MS", 0, 24)); // NOI18N
        ANP_Admin_radiobtn.setText("Admin");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(162, Short.MAX_VALUE)
                .addComponent(addbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(139, 139, 139)
                .addComponent(cancelbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(133, 133, 133))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel6)
                        .addGap(31, 31, 31)
                        .addComponent(ANP_Admin_radiobtn, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(ANP_Staff_radiobtn, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel14))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel4))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(ANP_username, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel1)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(ANP_lastname, javax.swing.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
                            .addComponent(ANP_firstname, javax.swing.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
                            .addComponent(ANP_middlename))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel16)
                .addGap(24, 24, 24)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(ANP_firstname, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel2))
                    .addComponent(ANP_middlename, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel3))
                    .addComponent(ANP_lastname, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addComponent(jLabel14)
                .addGap(24, 24, 24)
                .addComponent(jLabel4)
                .addGap(14, 14, 14)
                .addComponent(ANP_username, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(ANP_Admin_radiobtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ANP_Staff_radiobtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addbtnActionPerformed
       String firstName = ANP_firstname.getText().trim();
    String middleName = ANP_middlename.getText().trim();
    String lastName = ANP_lastname.getText().trim();
    String username = ANP_username.getText().trim();
    
    if (firstName.isEmpty()) {
        JOptionPane.showMessageDialog(this, "First name cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE);
        ANP_firstname.requestFocus();
        return;
    }
    if (lastName.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Last name cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE);
        ANP_lastname.requestFocus();
        return;
    }
    if (username.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Username cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE);
        ANP_username.requestFocus();
        return;
    }
 
    // 3. Database UPDATE Function (Fixes the ID incrementing issue)
    String role = "";
    if (ANP_Admin_radiobtn.isSelected()) {
        role = "Admin";
    } else if (ANP_Staff_radiobtn.isSelected()) {
        role = "Staff";
    } else {
        JOptionPane.showMessageDialog(this, "Please select an account role.", "Validation Error", JOptionPane.WARNING_MESSAGE);
        return;
    }

    String url = "jdbc:mysql://localhost:3306/flood_system";
    
    // CHECK FOR EXISTING USERNAME (excluding current personnel's ID)
    String checkQuery = "SELECT COUNT(*) FROM tbl_personnel WHERE username = ? AND personnel_id != ?";
    String updateQuery = "UPDATE tbl_personnel SET first_name2 = ?, middle_name2 = ?, last_name2 = ?, username = ?, role = ? WHERE personnel_id = ?";

    try (Connection con = DriverManager.getConnection(url, "root", "")) {
        
        // 1. Verify username uniqueness across other accounts
        try (PreparedStatement checkPst = con.prepareStatement(checkQuery)) {
            checkPst.setString(1, username);
            checkPst.setInt(2, personnelId);
            try (java.sql.ResultSet rs = checkPst.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(this, "This username is already used by another personnel.", "Duplicate Username", JOptionPane.WARNING_MESSAGE);
                    ANP_username.requestFocus();
                    return;
                }
            }
        }

        // 2. Proceed with Update if unique
        try (PreparedStatement pst = con.prepareStatement(updateQuery)) {
            pst.setString(1, firstName);
            pst.setString(2, middleName);
            pst.setString(3, lastName);
            pst.setString(4, username);
            pst.setString(5, role);
            pst.setInt(6, personnelId); 

            pst.executeUpdate();

            // Log using the personnel's name
            ActivityLogger.logAction(
                loggedInUsername, 
                loggedInRole, 
                "Updated personnel profile: " + firstName + " " + lastName
            );

            JOptionPane.showMessageDialog(this, "Personnel details updated successfully!");
            
            // Refresh parent dashboard table
            if (parentDashboard instanceof Admindashboard) {
                ((Admindashboard) parentDashboard).loadPersonnelDataToTable();
            }
            
            dispose();
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error updating personnel: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_addbtnActionPerformed

    private void ANP_middlenameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ANP_middlenameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ANP_middlenameActionPerformed

    private void cancelbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelbtnActionPerformed
        dispose();
    }//GEN-LAST:event_cancelbtnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
    try {
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
    } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
        logger.log(java.util.logging.Level.SEVERE, null, ex);
    }

    java.awt.EventQueue.invokeLater(new Runnable() {
        @Override
        public void run() {
            // Passed test username ("admin_test"), role ("Admin"), ID (1), sample names, and null dashboard
            Editpersonnel dialog = new Editpersonnel(new javax.swing.JFrame(), true, "admin_test", "Admin", 1, "Kenny", "Royce", "Rodriguez", "kenny1", "Admin", null);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        }
    });
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton ANP_Admin_radiobtn;
    private javax.swing.JRadioButton ANP_Staff_radiobtn;
    private javax.swing.JTextField ANP_firstname;
    private javax.swing.JTextField ANP_lastname;
    private javax.swing.JTextField ANP_middlename;
    private javax.swing.JTextField ANP_username;
    private javax.swing.JButton addbtn;
    private javax.swing.JButton cancelbtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JLabel person;
    // End of variables declaration//GEN-END:variables
}
