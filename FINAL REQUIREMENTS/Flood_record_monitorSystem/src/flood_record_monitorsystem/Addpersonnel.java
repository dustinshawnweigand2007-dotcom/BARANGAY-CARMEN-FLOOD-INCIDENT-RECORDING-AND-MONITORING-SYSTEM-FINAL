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
public class Addpersonnel extends javax.swing.JDialog {
    private String loggedInUsername;
    private String loggedInRole;
    private int personnelId;
    private javax.swing.JFrame parentDashboard;
    private ButtonGroup roleButtonGroup;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Addpersonnel.class.getName());

    /**
     * Creates new form Addpersonnel
     */
    public Addpersonnel(java.awt.Frame parent, boolean modal, String username, String role, javax.swing.JFrame dashboard) {
    super(parent, modal);
    initComponents();
    this.parentDashboard = dashboard;
    this.loggedInUsername = username;
    this.loggedInRole = role;
        
        // Group radio buttons so mutually exclusive role selection works properly
        roleButtonGroup = new ButtonGroup();
        roleButtonGroup.add(ANP_Admin_radiobtn);
        roleButtonGroup.add(ANP_Staff_radiobtn);
        
        // Load safe image icon for the header
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
        jLabel5 = new javax.swing.JLabel();
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
        ANP_showpass = new javax.swing.JCheckBox();
        ANP_password = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(244, 254, 241));

        jPanel8.setBackground(new java.awt.Color(204, 255, 204));
        jPanel8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        person.setBackground(new java.awt.Color(255, 255, 255));
        person.setIcon(new javax.swing.ImageIcon(getClass().getResource("/flood_record_monitorsystem/Person.png"))); // NOI18N

        jLabel12.setFont(new java.awt.Font("Perpetua", 1, 48)); // NOI18N
        jLabel12.setText("Add New Personnel");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(121, 121, 121)
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
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(person, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        jLabel5.setFont(new java.awt.Font("Trebuchet MS", 0, 30)); // NOI18N
        jLabel5.setText("Password:");

        ANP_username.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Trebuchet MS", 0, 30)); // NOI18N
        jLabel6.setText("Role:");

        addbtn.setBackground(new java.awt.Color(204, 255, 204));
        addbtn.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        addbtn.setText("ADD");
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
        ANP_firstname.addActionListener(this::ANP_firstnameActionPerformed);

        ANP_middlename.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        ANP_middlename.addActionListener(this::ANP_middlenameActionPerformed);

        ANP_Staff_radiobtn.setFont(new java.awt.Font("Trebuchet MS", 0, 24)); // NOI18N
        ANP_Staff_radiobtn.setText("Staff");

        ANP_Admin_radiobtn.setFont(new java.awt.Font("Trebuchet MS", 0, 24)); // NOI18N
        ANP_Admin_radiobtn.setText("Admin");
        ANP_Admin_radiobtn.addActionListener(this::ANP_Admin_radiobtnActionPerformed);

        ANP_showpass.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        ANP_showpass.setText("Show Password");
        ANP_showpass.addActionListener(this::ANP_showpassActionPerformed);

        ANP_password.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(addbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(139, 139, 139)
                .addComponent(cancelbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(133, 133, 133))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel1)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(ANP_lastname, javax.swing.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
                                    .addComponent(ANP_firstname, javax.swing.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
                                    .addComponent(ANP_middlename))))
                        .addGap(47, 47, 47))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(31, 31, 31)
                                .addComponent(ANP_Admin_radiobtn, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(ANP_Staff_radiobtn, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(ANP_showpass))
                            .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ANP_username, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 106, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(ANP_password, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(0, 17, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
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
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ANP_username, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ANP_password, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ANP_showpass)
                .addGap(4, 4, 4)
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
    String password = new String(ANP_password.getPassword()).trim();
    
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
    if (password.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Password cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE);
        ANP_password.requestFocus();
        return;
    }
    
    // 2. Radio Button Role Validation
   String role = "";
    if (ANP_Admin_radiobtn.isSelected()) {
        role = "Admin";
    } else if (ANP_Staff_radiobtn.isSelected()) {
        role = "Staff";
    } else {
        JOptionPane.showMessageDialog(this, "Please select an account role (Admin or Staff).", "Validation Error", JOptionPane.WARNING_MESSAGE);
        return;
    }

    String url = "jdbc:mysql://localhost:3306/flood_system";

    // CHECK FOR EXISTING USERNAME
    String checkQuery = "SELECT COUNT(*) FROM tbl_personnel WHERE username = ?";
    String insertQuery = "INSERT INTO tbl_personnel (first_name2, middle_name2, last_name2, username, password, role) VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection con = DriverManager.getConnection(url, "root", "")) {
        
        // 1. Verify if username already exists
        try (PreparedStatement checkPst = con.prepareStatement(checkQuery)) {
            checkPst.setString(1, username);
            try (java.sql.ResultSet rs = checkPst.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(this, "This username is already taken. Please choose another one.", "Duplicate Username", JOptionPane.WARNING_MESSAGE);
                    ANP_username.requestFocus();
                    return;
                }
            }
        }

        // 2. Proceed with Insertion if unique
        try (PreparedStatement pst = con.prepareStatement(insertQuery)) {
            pst.setString(1, firstName);
            pst.setString(2, middleName);
            pst.setString(3, lastName);
            pst.setString(4, username);
            pst.setString(5, password);
            pst.setString(6, role);

            pst.executeUpdate();

            // Add audit log for adding personnel
            ActivityLogger.logAction(loggedInUsername, loggedInRole, "Added new personnel: " + firstName + " " + lastName);

            JOptionPane.showMessageDialog(this, "Personnel added successfully!");

            // Refresh the parent dashboard table and metrics if applicable
            if (parentDashboard instanceof Admindashboard) {
                ((Admindashboard) parentDashboard).loadPersonnelDataToTable();
            } else if (parentDashboard instanceof Staffdashboard) {
                ((Staffdashboard) parentDashboard).loadPersonnelDataToTable();
            }

            dispose();
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error inserting personnel: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_addbtnActionPerformed

    private void ANP_middlenameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ANP_middlenameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ANP_middlenameActionPerformed

    private void cancelbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelbtnActionPerformed
     dispose();
    }//GEN-LAST:event_cancelbtnActionPerformed

    private void ANP_showpassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ANP_showpassActionPerformed
       if (ANP_showpass.isSelected()) {
            ANP_password.setEchoChar((char) 0); // Show plain text
        } else {
            ANP_password.setEchoChar('•'); // Mask with bullet character
        }
    }//GEN-LAST:event_ANP_showpassActionPerformed

    private void ANP_firstnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ANP_firstnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ANP_firstnameActionPerformed

    private void ANP_Admin_radiobtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ANP_Admin_radiobtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ANP_Admin_radiobtnActionPerformed
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
            // Pass username and role test strings right after 'true'
            Addpersonnel dialog = new Addpersonnel(new javax.swing.JFrame(), true, "admin_test", "Admin", null);
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
    private javax.swing.JPasswordField ANP_password;
    private javax.swing.JCheckBox ANP_showpass;
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
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JLabel person;
    // End of variables declaration//GEN-END:variables
}
