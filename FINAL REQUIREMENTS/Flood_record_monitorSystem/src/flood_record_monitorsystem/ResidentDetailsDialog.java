/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package flood_record_monitorsystem;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import flood_record_monitorsystem.ActivityLogger;
/**
 *
 * @author User
 */
public class ResidentDetailsDialog extends javax.swing.JDialog {
    private int residentId;
    private javax.swing.JFrame parentDashboard;
    private String loggedInUsername;
private String loggedInRole;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ResidentDetailsDialog.class.getName());

    /**
     * Creates new form ResidentDetailsDialog
     */
   public ResidentDetailsDialog(java.awt.Frame parent, boolean modal, int id, 
                             String fName, String mName, String lName, 
                             String contact, String zone, String street, 
                             String validId, String status, String userRole, 
                             String username, String role, 
                             javax.swing.JFrame dashboard) {
    super(parent, modal);
    initComponents();
    
    this.residentId = id;
    this.parentDashboard = dashboard;
    this.loggedInUsername = username;
    this.loggedInRole = role;
    // rest of your constructor...

        
        // Populate text fields
        fname.setText(fName);
        mname.setText(mName);
        lname.setText(lName);
        cnumber.setText(contact);
        zname.setText(zone);
        sname.setText(street);
        id_valid.setText(validId);
        stat.setText(status);

        // ROLE-BASED ACCESS CONTROL (RBAC)
if (userRole.equalsIgnoreCase("Admin")) {
    // Admins can view and edit information
    fname.setEditable(true);
    mname.setEditable(true);
    lname.setEditable(true);
    cnumber.setEditable(true);
    zname.setEditable(true);
    sname.setEditable(true);
    id_valid.setEditable(false);
    stat.setEditable(false);
    
    // Explicitly make action buttons visible for Admin
    approve.setVisible(true);
    remove.setVisible(true);
    update.setVisible(true);
} else {
    // Staff can only view (read-only) and action buttons are hidden
    fname.setEditable(true);
    mname.setEditable(true);
    lname.setEditable(true);
    cnumber.setEditable(true);
    zname.setEditable(true);
    sname.setEditable(true);
    id_valid.setEditable(false);
    stat.setEditable(false);
    
    approve.setVisible(false);
    remove.setVisible(false);
    update.setVisible(true);
}
        safeSetIcon(person, "person.png", 50, 60);

        // Role-Based Access Control placed properly INSIDE the constructor
        if (userRole.equalsIgnoreCase("Staff")) {
            approve.setVisible(false);
            remove.setVisible(false);
        }
    }
        // ROLE-BASED ACCESS CONTROL (RBAC)

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

        jTextField3 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        person = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lblname1 = new javax.swing.JLabel();
        lblcontact1 = new javax.swing.JLabel();
        lblcontact2 = new javax.swing.JLabel();
        lblcontact3 = new javax.swing.JLabel();
        lblcontact4 = new javax.swing.JLabel();
        lblcontact5 = new javax.swing.JLabel();
        back = new javax.swing.JButton();
        approve = new javax.swing.JButton();
        remove = new javax.swing.JButton();
        lblcontact6 = new javax.swing.JLabel();
        lblcontact7 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        cnumber = new javax.swing.JTextField();
        fname = new javax.swing.JTextField();
        lname = new javax.swing.JTextField();
        mname = new javax.swing.JTextField();
        id_valid = new javax.swing.JTextField();
        stat = new javax.swing.JTextField();
        zname = new javax.swing.JTextField();
        sname = new javax.swing.JTextField();
        update = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(244, 254, 241));

        jPanel8.setBackground(new java.awt.Color(204, 255, 204));
        jPanel8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        person.setBackground(new java.awt.Color(255, 255, 255));
        person.setIcon(new javax.swing.ImageIcon(getClass().getResource("/flood_record_monitorsystem/Person.png"))); // NOI18N

        jLabel12.setFont(new java.awt.Font("Perpetua", 1, 48)); // NOI18N
        jLabel12.setText("Resident Profile");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(230, 230, 230)
                .addComponent(person, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(person, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );

        lblname1.setFont(new java.awt.Font("Trebuchet MS", 0, 30)); // NOI18N
        lblname1.setText("First Name:");

        lblcontact1.setFont(new java.awt.Font("Trebuchet MS", 0, 30)); // NOI18N
        lblcontact1.setText("Last Name:");

        lblcontact2.setFont(new java.awt.Font("Trebuchet MS", 0, 30)); // NOI18N
        lblcontact2.setText("Contact No:");

        lblcontact3.setFont(new java.awt.Font("Trebuchet MS", 0, 30)); // NOI18N
        lblcontact3.setText("Valid ID:");

        lblcontact4.setFont(new java.awt.Font("Trebuchet MS", 0, 30)); // NOI18N
        lblcontact4.setText("Street Name:");

        lblcontact5.setFont(new java.awt.Font("Trebuchet MS", 0, 30)); // NOI18N
        lblcontact5.setText("Zone Name:");

        back.setBackground(new java.awt.Color(204, 204, 204));
        back.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        back.setText("BACK");
        back.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        back.addActionListener(this::backActionPerformed);

        approve.setBackground(new java.awt.Color(204, 255, 204));
        approve.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        approve.setText("APPROVE");
        approve.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        approve.addActionListener(this::approveActionPerformed);

        remove.setBackground(new java.awt.Color(255, 102, 102));
        remove.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        remove.setText("REMOVE");
        remove.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        remove.addActionListener(this::removeActionPerformed);

        lblcontact6.setFont(new java.awt.Font("Trebuchet MS", 0, 30)); // NOI18N
        lblcontact6.setText("Status:");

        lblcontact7.setFont(new java.awt.Font("Trebuchet MS", 0, 30)); // NOI18N
        lblcontact7.setText("Middle Name:");

        jLabel13.setFont(new java.awt.Font("Perpetua", 1, 44)); // NOI18N
        jLabel13.setText("Location & Credentials");

        jLabel14.setFont(new java.awt.Font("Perpetua", 1, 44)); // NOI18N
        jLabel14.setText("Personal Information");

        cnumber.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N

        fname.setFont(new java.awt.Font("Yu Gothic UI", 0, 22)); // NOI18N

        lname.setFont(new java.awt.Font("Yu Gothic UI", 0, 22)); // NOI18N

        mname.setFont(new java.awt.Font("Yu Gothic UI", 0, 22)); // NOI18N
        mname.addActionListener(this::mnameActionPerformed);

        id_valid.setFont(new java.awt.Font("Yu Gothic UI", 0, 22)); // NOI18N

        stat.setFont(new java.awt.Font("Yu Gothic UI", 0, 22)); // NOI18N

        zname.setFont(new java.awt.Font("Yu Gothic UI", 0, 22)); // NOI18N

        sname.setFont(new java.awt.Font("Yu Gothic UI", 0, 22)); // NOI18N
        sname.addActionListener(this::snameActionPerformed);

        update.setBackground(new java.awt.Color(255, 255, 204));
        update.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        update.setText("UPDATE");
        update.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        update.addActionListener(this::updateActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblcontact5)
                            .addComponent(approve, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(zname, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(id_valid, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(update, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(37, 37, 37)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lblcontact6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(stat, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(remove, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblcontact4))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(sname, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(back, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(7, 7, 7)))))
                        .addGap(31, 31, 31))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblcontact3)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(lblcontact1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lname, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addComponent(lblname1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(fname, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(8, 8, 8)))
                                        .addGap(27, 27, 27)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(lblcontact2)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(cnumber, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(lblcontact7)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(mname, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblname1)
                    .addComponent(lblcontact7)
                    .addComponent(fname, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mname, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblcontact1)
                    .addComponent(lblcontact2)
                    .addComponent(cnumber, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lname, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblcontact5)
                    .addComponent(lblcontact4)
                    .addComponent(zname, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sname, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblcontact3)
                    .addComponent(lblcontact6)
                    .addComponent(id_valid, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stat, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 88, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(remove, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(approve, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(back, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(update, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backActionPerformed
       dispose();
    }//GEN-LAST:event_backActionPerformed

    private void approveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_approveActionPerformed
      String url = "jdbc:mysql://localhost:3306/flood_system";
    try (Connection con = DriverManager.getConnection(url, "root", "");
         PreparedStatement pst = con.prepareStatement("UPDATE tbl_resident SET status = 'Approved' WHERE resident_id = ?")) {
        
        pst.setInt(1, residentId);
        pst.executeUpdate();
        
        // Get name for the log message
        String residentName = fname.getText().trim() + " " + lname.getText().trim();
        
        JOptionPane.showMessageDialog(this, "Resident approved successfully!");
        ActivityLogger.logAction(loggedInUsername, loggedInRole, "Approved resident: " + residentName);
        
        if (parentDashboard instanceof Admindashboard) {
            ((Admindashboard) parentDashboard).loadResidentDataToTable();
            ((Admindashboard) parentDashboard).loadDashboardMetrics(); 
            ((Admindashboard) parentDashboard).loadActivityLogsToTextArea();
        } else if (parentDashboard instanceof Staffdashboard) {
            ((Staffdashboard) parentDashboard).loadResidentDataToTable();
        }
        
        dispose(); 
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error approving resident: " + e.getMessage());
    }
    
    }//GEN-LAST:event_approveActionPerformed

    private void removeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeActionPerformed
       int confirm = JOptionPane.showConfirmDialog(
        this, 
        "Are you sure you want to remove this record?", 
        "Confirm Deletion", 
        JOptionPane.YES_NO_OPTION, 
        JOptionPane.WARNING_MESSAGE 
    );

    if (confirm != JOptionPane.YES_OPTION) return;

    // Get name for the log message before deleting
    String residentName = fname.getText().trim() + " " + lname.getText().trim();

    String url = "jdbc:mysql://localhost:3306/flood_system";
    try (Connection con = DriverManager.getConnection(url, "root", "");
         PreparedStatement pst = con.prepareStatement("DELETE FROM tbl_resident WHERE resident_id = ?")) {
        
        pst.setInt(1, residentId);
        pst.executeUpdate();
        
        ActivityLogger.logAction(loggedInUsername, loggedInRole, "Removed resident: " + residentName);
        JOptionPane.showMessageDialog(this, "Resident record removed.");
        
        if (parentDashboard instanceof Admindashboard) {
            ((Admindashboard) parentDashboard).loadResidentDataToTable();
            ((Admindashboard) parentDashboard).loadDashboardMetrics(); 
        } else if (parentDashboard instanceof Staffdashboard) {
            ((Staffdashboard) parentDashboard).loadResidentDataToTable();
        }

        dispose();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error removing resident: " + e.getMessage());
    }
    }//GEN-LAST:event_removeActionPerformed

    private void mnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mnameActionPerformed

    private void snameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_snameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_snameActionPerformed

    private void updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateActionPerformed
                                  
    String url = "jdbc:mysql://localhost:3306/flood_system";
    String updateQuery = "UPDATE tbl_resident SET first_name = ?, middle_name = ?, last_name = ?, contact_number = ?, valid_id = ?, status = ? WHERE resident_id = ?";

    try (Connection con = DriverManager.getConnection(url, "root", "");
         PreparedStatement pst = con.prepareStatement(updateQuery)) {

        // Get the names directly from the text fields
        String updatedFirstName = fname.getText().trim();
        String updatedLastName = lname.getText().trim();

        pst.setString(1, updatedFirstName);
        pst.setString(2, mname.getText().trim());
        pst.setString(3, updatedLastName);
        pst.setString(4, cnumber.getText().trim());
        pst.setString(5, id_valid.getText().trim());
        pst.setString(6, stat.getText().trim());
        pst.setInt(7, residentId);

        pst.executeUpdate();

        // Log using the resident's name instead of just the ID
        ActivityLogger.logAction(
            loggedInUsername, 
            loggedInRole, 
            "Updated resident profile: " + updatedFirstName + " " + updatedLastName
        );

        JOptionPane.showMessageDialog(this, "Resident details updated successfully!");
        
        // Refresh the dashboard table and cards
        if (parentDashboard instanceof Admindashboard) {
            ((Admindashboard) parentDashboard).loadResidentDataToTable();
            ((Admindashboard) parentDashboard).loadDashboardMetrics();
            ((Admindashboard) parentDashboard).loadActivityLogsToTextArea();
        } else if (parentDashboard instanceof Staffdashboard) {
            ((Staffdashboard) parentDashboard).loadResidentDataToTable();
        }

        dispose();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error updating resident: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    }

    }//GEN-LAST:event_updateActionPerformed
// ROLE-BASED ACCESS CONTROL (RBAC)

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
            // Pass test/dummy data matching all parameters in order
            ResidentDetailsDialog dialog = new ResidentDetailsDialog(
                new javax.swing.JFrame(), true, 1, 
                "John", "D.", "Doe", "09123456789", 
                "Zone 1", "Rizal Street", "12345", "Pending", 
                "Staff",                // userRole parameter
                "testAdminUser",        // username parameter 
                "Admin",                // role parameter
                null                    // dashboard JFrame parameter
            );
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
    private javax.swing.JButton approve;
    private javax.swing.JButton back;
    private javax.swing.JTextField cnumber;
    private javax.swing.JTextField fname;
    private javax.swing.JTextField id_valid;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JLabel lblcontact1;
    private javax.swing.JLabel lblcontact2;
    private javax.swing.JLabel lblcontact3;
    private javax.swing.JLabel lblcontact4;
    private javax.swing.JLabel lblcontact5;
    private javax.swing.JLabel lblcontact6;
    private javax.swing.JLabel lblcontact7;
    private javax.swing.JLabel lblname1;
    private javax.swing.JTextField lname;
    private javax.swing.JTextField mname;
    private javax.swing.JLabel person;
    private javax.swing.JButton remove;
    private javax.swing.JTextField sname;
    private javax.swing.JTextField stat;
    private javax.swing.JButton update;
    private javax.swing.JTextField zname;
    // End of variables declaration//GEN-END:variables
}
