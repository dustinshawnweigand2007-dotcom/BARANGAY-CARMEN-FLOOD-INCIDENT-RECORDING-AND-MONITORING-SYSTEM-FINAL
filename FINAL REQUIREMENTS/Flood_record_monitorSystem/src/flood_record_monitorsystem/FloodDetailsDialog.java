package flood_record_monitorsystem;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import flood_record_monitorsystem.ActivityLogger;

public class FloodDetailsDialog extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FloodDetailsDialog.class.getName());
    
    private int floodId;
    private Admindashboard adminDashboardRef;
    private String loggedInUsername;
    private String loggedInRole;

    public FloodDetailsDialog(java.awt.Frame parent, boolean modal, int floodId, Admindashboard dashboardRef, String username, String role) {
    super(parent, modal);
    initComponents();
    
    // Assign everything correctly
    this.floodId = floodId;
    this.adminDashboardRef = dashboardRef; 
    this.loggedInUsername = username;
    this.loggedInRole = role;
    customizeFloodDetailsComboBoxes(); 
    customizeFloodTimeField();
    
    // Group radio buttons to match staff recording panel
    ButtonGroup timeGroup = new ButtonGroup();
    timeGroup.add(radioAuto);
    timeGroup.add(radioCustom);
    
    populateZones();
    loadSeverityOptions();
    loadFloodRecordData();
        
        // Listeners for radio buttons
        radioAuto.addActionListener(e -> {
            if (radioAuto.isSelected()) {
                flooddate.setEnabled(false);
                floodtime.setEnabled(false);
            }
        });

        radioCustom.addActionListener(e -> {
            if (radioCustom.isSelected()) {
                flooddate.setEnabled(true);
                floodtime.setEnabled(true);
                floodtime.requestFocus();
            }
        });
        
        floodzone.addActionListener(e -> updateStreets());
    }

    private void populateZones() {
        DefaultComboBoxModel<String> zoneModel = new DefaultComboBoxModel<>();
        zoneModel.addElement("-- Select Zone --");
        String url = "jdbc:mysql://localhost:3306/flood_system";
        String query = "SELECT DISTINCT zone_name FROM tbl_location ORDER BY CAST(SUBSTRING_INDEX(zone_name, ' ', -1) AS UNSIGNED) ASC";

        try (Connection con = DriverManager.getConnection(url, "root", "");
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                zoneModel.addElement(rs.getString("zone_name"));
            }
            floodzone.setModel(zoneModel);
        } catch (Exception e) {
            System.out.println("Error loading zones: " + e.getMessage());
        }
    }

    private void loadSeverityOptions() {
        sever.setModel(new DefaultComboBoxModel<>(new String[]{
            "-- Select Severity --", 
            "Low", 
            "Moderate", 
            "High",
            "Critical"
        }));
    }

    private void updateStreets() {
        if (floodzone.getSelectedIndex() <= 0) {
            floodstreet.setModel(new DefaultComboBoxModel<>(new String[]{"-- Select Street --"}));
            return;
        }
        String selectedZone = (String) floodzone.getSelectedItem();
        DefaultComboBoxModel<String> streetModel = new DefaultComboBoxModel<>();
        streetModel.addElement("-- Select Street --");

        String url = "jdbc:mysql://localhost:3306/flood_system";
        String query = "SELECT street_name FROM tbl_location WHERE zone_name = ? ORDER BY street_name ASC";

        try (Connection con = DriverManager.getConnection(url, "root", "");
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, selectedZone);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                streetModel.addElement(rs.getString("street_name"));
            }
            floodstreet.setModel(streetModel);
        } catch (Exception e) {
            System.out.println("Error loading streets: " + e.getMessage());
        }
    }

    private void loadFloodRecordData() {
        String url = "jdbc:mysql://localhost:3306/flood_system";
        String query = "SELECT f.flood_date, f.flood_time, f.severity, l.zone_name, l.street_name " +
                       "FROM tbl_flood_record f JOIN tbl_location l ON f.location_id = l.location_id " +
                       "WHERE f.flood_id = ?";

        try (Connection con = DriverManager.getConnection(url, "root", "");
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, floodId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                flooddate.setDate(rs.getDate("flood_date"));
                floodtime.setText(rs.getString("flood_time"));
                sever.setSelectedItem(rs.getString("severity"));
                floodzone.setSelectedItem(rs.getString("zone_name"));
                updateStreets();
                floodstreet.setSelectedItem(rs.getString("street_name"));
                
                // Default to custom so existing date/time fields are visible & editable
                radioCustom.setSelected(true);
                flooddate.setEnabled(true);
                floodtime.setEnabled(true);
            }
        } catch (Exception e) {
            System.out.println("Error loading record details: " + e.getMessage());
        }
    }
    private void customizeFloodDetailsComboBoxes() {
    Font customFont = new Font("Segoe UI", Font.PLAIN, 14);
    Color backgroundColor = Color.WHITE;
    Color textColor = new Color(33, 33, 33);
    Color placeholderColor = new Color(128, 128, 128);
    Color accentGreen = new Color(204, 255, 204);          
    Color darkGreenBorder = new Color(120, 180, 120);      

    floodzone.setFont(customFont);
    floodzone.setBackground(backgroundColor);
    floodzone.setForeground(textColor);
    floodzone.setBorder(javax.swing.BorderFactory.createLineBorder(darkGreenBorder, 1, true));

    floodstreet.setFont(customFont);
    floodstreet.setBackground(backgroundColor);
    floodstreet.setForeground(textColor);
    floodstreet.setBorder(javax.swing.BorderFactory.createLineBorder(darkGreenBorder, 1, true));

    sever.setFont(customFont);
    sever.setBackground(backgroundColor);
    sever.setForeground(textColor);
    sever.setBorder(javax.swing.BorderFactory.createLineBorder(darkGreenBorder, 1, true));

    floodzone.setMaximumRowCount(5);
    floodstreet.setMaximumRowCount(5);
    sever.setMaximumRowCount(5);

    DefaultListCellRenderer customRenderer = new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 12, 6, 12));

            if (value != null && value.toString().contains("--")) {
                label.setForeground(placeholderColor);
                label.setFont(customFont.deriveFont(Font.ITALIC));
                label.setBackground(isSelected ? accentGreen : backgroundColor);
            } else {
                if (isSelected) {
                    label.setBackground(accentGreen);
                    label.setForeground(new Color(20, 60, 20));
                    label.setFont(customFont.deriveFont(Font.BOLD));
                } else {
                    label.setBackground(backgroundColor);
                    label.setForeground(textColor);
                    label.setFont(customFont.deriveFont(Font.PLAIN));
                }
            }
            return label;
        }
    };

    floodzone.setRenderer(customRenderer);
    floodstreet.setRenderer(customRenderer);
    sever.setRenderer(customRenderer);
}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        person = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        back = new javax.swing.JButton();
        remove = new javax.swing.JButton();
        update = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        radioCustom = new javax.swing.JRadioButton();
        radioAuto = new javax.swing.JRadioButton();
        flooddate = new com.toedter.calendar.JDateChooser();
        jLabel29 = new javax.swing.JLabel();
        floodtime = new javax.swing.JFormattedTextField();
        jLabel33 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        floodzone = new javax.swing.JComboBox<>();
        jLabel26 = new javax.swing.JLabel();
        sever = new javax.swing.JComboBox<>();
        jLabel35 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        floodstreet = new javax.swing.JComboBox<>();
        jLabel36 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(244, 254, 241));

        jPanel8.setBackground(new java.awt.Color(204, 255, 204));
        jPanel8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        person.setBackground(new java.awt.Color(255, 255, 255));
        person.setIcon(new javax.swing.ImageIcon(getClass().getResource("/flood_record_monitorsystem/floodpic.png"))); // NOI18N

        jLabel12.setFont(new java.awt.Font("Perpetua", 1, 48)); // NOI18N
        jLabel12.setText("Flood Detail");

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

        back.setBackground(new java.awt.Color(204, 204, 204));
        back.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        back.setText("BACK");
        back.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        back.addActionListener(this::backActionPerformed);

        remove.setBackground(new java.awt.Color(255, 102, 102));
        remove.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        remove.setText("REMOVE");
        remove.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        remove.addActionListener(this::removeActionPerformed);

        update.setBackground(new java.awt.Color(255, 255, 204));
        update.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        update.setText("UPDATE");
        update.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        update.addActionListener(this::updateActionPerformed);

        jLabel15.setFont(new java.awt.Font("Perpetua", 1, 44)); // NOI18N
        jLabel15.setText("Date of Flood");

        jLabel32.setBackground(new java.awt.Color(255, 0, 0));
        jLabel32.setFont(new java.awt.Font("Segoe UI Emoji", 1, 36)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(204, 0, 51));
        jLabel32.setText("*");

        radioCustom.setFont(new java.awt.Font("Segoe UI Emoji", 1, 30)); // NOI18N
        radioCustom.setText("Custom");

        radioAuto.setFont(new java.awt.Font("Segoe UI Emoji", 1, 30)); // NOI18N
        radioAuto.setSelected(true);
        radioAuto.setText("Present");

        jLabel29.setFont(new java.awt.Font("Segoe UI Emoji", 1, 36)); // NOI18N
        jLabel29.setText("Time of Flood");

        floodtime.addActionListener(this::floodtimeActionPerformed);

        jLabel33.setBackground(new java.awt.Color(255, 0, 0));
        jLabel33.setFont(new java.awt.Font("Segoe UI Emoji", 1, 36)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(204, 0, 51));
        jLabel33.setText("*");

        jLabel25.setFont(new java.awt.Font("Segoe UI Emoji", 1, 36)); // NOI18N
        jLabel25.setText("Street");

        jLabel34.setBackground(new java.awt.Color(255, 0, 0));
        jLabel34.setFont(new java.awt.Font("Segoe UI Emoji", 1, 36)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(204, 0, 51));
        jLabel34.setText("*");

        floodzone.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        floodzone.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel26.setFont(new java.awt.Font("Segoe UI Emoji", 1, 36)); // NOI18N
        jLabel26.setText("Zone");

        sever.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        sever.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel35.setBackground(new java.awt.Color(255, 0, 0));
        jLabel35.setFont(new java.awt.Font("Segoe UI Emoji", 1, 36)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(204, 0, 51));
        jLabel35.setText("*");

        jLabel27.setFont(new java.awt.Font("Segoe UI Emoji", 1, 36)); // NOI18N
        jLabel27.setText("Flood Severity Level");

        floodstreet.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        floodstreet.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel36.setBackground(new java.awt.Color(255, 0, 0));
        jLabel36.setFont(new java.awt.Font("Segoe UI Emoji", 1, 36)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(204, 0, 51));
        jLabel36.setText("*");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(156, 156, 156)
                        .addComponent(update, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(82, 82, 82)
                        .addComponent(remove, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(92, 92, 92)
                        .addComponent(back, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(102, Short.MAX_VALUE))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel32))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(radioCustom)
                        .addGap(18, 18, 18)
                        .addComponent(radioAuto))
                    .addComponent(floodtime, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel33))
                    .addComponent(flooddate, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel36)
                        .addGap(12, 12, 12))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel26)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel34)
                            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(floodzone, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(177, 177, 177))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(floodstreet, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel25)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel35))
                                    .addComponent(sever, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(175, 175, 175))))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(radioCustom, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(radioAuto))
                        .addGap(49, 49, 49)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(jLabel32)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel26)
                            .addComponent(jLabel34))
                        .addGap(10, 10, 10)
                        .addComponent(floodzone, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(flooddate, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel29)
                            .addComponent(jLabel33))
                        .addGap(18, 18, 18)
                        .addComponent(floodtime, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel35))
                        .addGap(18, 18, 18)
                        .addComponent(floodstreet, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel36)
                            .addComponent(jLabel27))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(sever, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 137, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(remove, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void removeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeActionPerformed
     int confirm = JOptionPane.showConfirmDialog(
    this, 
    "Are you sure you want to delete this flood record?", 
    "Confirm Deletion", 
    JOptionPane.YES_NO_OPTION, 
    JOptionPane.WARNING_MESSAGE // This sets the warning icon/type
);

if (confirm == JOptionPane.YES_OPTION) {
    String url = "jdbc:mysql://localhost:3306/flood_system";
    String deleteQuery = "DELETE FROM tbl_flood_record WHERE flood_id = ?";

    try (Connection con = DriverManager.getConnection(url, "root", "");
         PreparedStatement pst = con.prepareStatement(deleteQuery)) {
        pst.setInt(1, floodId);
        pst.executeUpdate();

        ActivityLogger.logAction(loggedInUsername, loggedInRole, "Deleted flood record ID: " + floodId);
        JOptionPane.showMessageDialog(this, "Flood record deleted successfully!");
        
        // Safe update check
        if (adminDashboardRef != null) {
            adminDashboardRef.loadFloodDataToTable();
            adminDashboardRef.loadDashboardMetrics();
            adminDashboardRef.loadDynamicFloodChart(adminDashboardRef.getCurrentYear());
            adminDashboardRef.loadDynamicResidentChart();
        }

        dispose();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error deleting record: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}
    }//GEN-LAST:event_removeActionPerformed

    private void updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateActionPerformed
if (floodzone.getSelectedIndex() <= 0 || floodstreet.getSelectedIndex() <= 0 || sever.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Please select Zone, Street, and Severity.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String zoneName = floodzone.getSelectedItem().toString().trim();
        String streetName = floodstreet.getSelectedItem().toString().trim();
        String severity = sever.getSelectedItem().toString().trim();

        Date selectedDate = flooddate.getDate();
        if (selectedDate == null) {
            JOptionPane.showMessageDialog(this, "Please select a valid Date.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String timeVal = floodtime.getText().trim();
        String timeRegex = "^([01][0-9]|2[0-3]):[0-5][0-9]$";
        
        if (timeVal.contains("_") || !timeVal.matches(timeRegex)) {
            JOptionPane.showMessageDialog(this, 
                "Invalid Time Format!\nPlease enter a valid 24-hour time between 00:00 and 23:59 (e.g., 14:30).", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            floodtime.requestFocus();
            return;
        }

        // FUTURE DATE/TIME VALIDATION CHECK
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String datePart = new SimpleDateFormat("yyyy-MM-dd").format(selectedDate);
            Date fullSelectedDateTime = sdf.parse(datePart + " " + timeVal);
            Date currentDateTime = new Date();

            if (fullSelectedDateTime.after(currentDateTime)) {
                JOptionPane.showMessageDialog(this, "Cannot update a flood record with a future date and time!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid date or time format.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String dateVal = new SimpleDateFormat("yyyy-MM-dd").format(selectedDate);

        String url = "jdbc:mysql://localhost:3306/flood_system";
        try (Connection con = DriverManager.getConnection(url, "root", "")) {
            // Find location ID
            int locationId = -1;
            String locQuery = "SELECT location_id FROM tbl_location WHERE zone_name = ? AND street_name = ?";
            try (PreparedStatement pstLoc = con.prepareStatement(locQuery)) {
                pstLoc.setString(1, zoneName);
                pstLoc.setString(2, streetName);
                ResultSet rsLoc = pstLoc.executeQuery();
                if (rsLoc.next()) {
                    locationId = rsLoc.getInt("location_id");
                }
            }

            if (locationId == -1) {
                JOptionPane.showMessageDialog(this, "Location combination not found in database.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Update record query
            String updateQuery = "UPDATE tbl_flood_record SET location_id = ?, flood_date = ?, flood_time = ?, severity = ? WHERE flood_id = ?";
            try (PreparedStatement pstUpdate = con.prepareStatement(updateQuery)) {
                pstUpdate.setInt(1, locationId);
                pstUpdate.setString(2, dateVal);
                pstUpdate.setString(3, timeVal);
                pstUpdate.setString(4, severity);
                pstUpdate.setInt(5, floodId);
                pstUpdate.executeUpdate();
            }
            ActivityLogger.logAction(loggedInUsername, loggedInRole, "Updated flood record ID: " + floodId);
            JOptionPane.showMessageDialog(this, "Flood record updated successfully!");

            // Safe update check
            if (adminDashboardRef != null) {
                adminDashboardRef.loadFloodDataToTable();
                adminDashboardRef.loadDashboardMetrics();
                adminDashboardRef.loadDynamicFloodChart(adminDashboardRef.getCurrentYear()); 
                adminDashboardRef.loadDynamicResidentChart();
            }

            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating record: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_updateActionPerformed

    private void floodtimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_floodtimeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_floodtimeActionPerformed
private void customizeFloodTimeField() {
    java.awt.Color white = java.awt.Color.WHITE;
    java.awt.Color textColor = new java.awt.Color(40, 40, 40);
    java.awt.Font uiFont = new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14);

    floodtime.setBackground(white);
    floodtime.setForeground(textColor);
    floodtime.setFont(uiFont);

    floodtime.setBorder(javax.swing.BorderFactory.createCompoundBorder(
        javax.swing.BorderFactory.createLineBorder(new java.awt.Color(120, 180, 120), 1, true),
        javax.swing.BorderFactory.createEmptyBorder(4, 6, 4, 6)
    ));

    try {
        javax.swing.text.MaskFormatter timeMask = new javax.swing.text.MaskFormatter("##:##");
        timeMask.setPlaceholderCharacter('_');
        timeMask.install(floodtime); 
    } catch (java.text.ParseException e) {
        e.printStackTrace();
    }

    // InputVerifier using the 24-hour Regex pattern
    floodtime.setInputVerifier(new javax.swing.InputVerifier() {
        @Override
        public boolean verify(javax.swing.JComponent input) {
            javax.swing.JFormattedTextField ftf = (javax.swing.JFormattedTextField) input;
            String text = ftf.getText().trim();
            
            // Allow empty or incomplete fields during typing if custom is disabled
            if (text.contains("_") || text.isEmpty()) {
                return true; 
            }

            String timeRegex = "^([01][0-9]|2[0-3]):[0-5][0-9]$";
            if (!text.matches(timeRegex)) {
                javax.swing.JOptionPane.showMessageDialog(
                    ftf,
                    "Invalid Time Format! Hours must be between 00 and 23, and minutes between 00 and 59.",
                    "Time Validation Error",
                    javax.swing.JOptionPane.WARNING_MESSAGE
                );
                
                javax.swing.SwingUtilities.invokeLater(() -> {
                    ftf.setText("__:__");
                    ftf.setCaretPosition(0);
                    ftf.requestFocusInWindow();
                });
                
                return false;
            }
            return true;
        }
    });
}

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

     
    java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
            // Pass dummy values or nulls just for testing the form independently
            FloodDetailsDialog dialog = new FloodDetailsDialog(new javax.swing.JFrame(), true, 1, null, "testAdmin", "Admin");
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
    private javax.swing.JButton back;
    private com.toedter.calendar.JDateChooser flooddate;
    private javax.swing.JComboBox<String> floodstreet;
    private javax.swing.JFormattedTextField floodtime;
    private javax.swing.JComboBox<String> floodzone;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JLabel person;
    private javax.swing.JRadioButton radioAuto;
    private javax.swing.JRadioButton radioCustom;
    private javax.swing.JButton remove;
    private javax.swing.JComboBox<String> sever;
    private javax.swing.JButton update;
    // End of variables declaration//GEN-END:variables
}
