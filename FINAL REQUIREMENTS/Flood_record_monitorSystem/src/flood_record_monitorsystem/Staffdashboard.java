    package flood_record_monitorsystem;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Dimension;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.RenderingHints;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.sql.PreparedStatement;
import javax.swing.JScrollPane;
import javax.swing.table.TableColumnModel;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import com.toedter.calendar.JCalendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import flood_record_monitorsystem.ActivityLogger;


public class Staffdashboard extends javax.swing.JFrame {

    private int previousTab = 0;
    private java.util.List<Object[]> allResidentData = new java.util.ArrayList<>();
    private java.util.List<Object[]> filteredResidentData = new java.util.ArrayList<>();
    private javax.swing.table.TableRowSorter<javax.swing.table.TableModel> directorySorter;
    private int currentYear = 2026; 
    private int maxYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
    private javax.swing.table.TableRowSorter<javax.swing.table.TableModel> floodSorter;
    private JLabel lblTotalResidentsValue;
private JLabel lblPendingValue;
private JLabel lblTotalFloodsValue;
private JLabel lblCriticalValue;
private String loggedInUser;
private String loggedInUsername;
private String loggedInRole;
    
    public Staffdashboard(String username, String role) {
        
        initComponents();
        ((javax.swing.text.AbstractDocument) validid.getDocument()).setDocumentFilter(new javax.swing.text.DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, javax.swing.text.AttributeSet attr) throws javax.swing.text.BadLocationException {
                if (string != null) {
                    replace(fb, offset, 0, string, attr);
                }
            }

            @Override
            public void remove(FilterBypass fb, int offset, int length) throws javax.swing.text.BadLocationException {
                super.remove(fb, offset, length);
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, javax.swing.text.AttributeSet attrs) throws javax.swing.text.BadLocationException {
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                StringBuilder sb = new StringBuilder(currentText);
                sb.replace(offset, offset + length, text != null ? text.toUpperCase() : "");
                String target = sb.toString();

                // Keep only letters and digits
                String cleaned = target.replaceAll("[^A-Z0-9]", "");
                if (cleaned.length() > 8) { // Max 3 letters + 5 digits = 8 characters
                    cleaned = cleaned.substring(0, 8);
                }

                String letters = "";
                String digits = "";
                for (int i = 0; i < cleaned.length(); i++) {
                    char c = cleaned.charAt(i);
                    if (letters.length() < 3 && Character.isLetter(c)) {
                        letters += c;
                    } else if (letters.length() == 3 && Character.isDigit(c) && digits.length() < 5) {
                        digits += c;
                    }
                }

                // Automatically format with a hyphen after 3 letters
                String built = letters;
                if (letters.length() == 3 || !digits.isEmpty()) {
                    built += "-";
                }
                built += digits;

                super.replace(fb, 0, fb.getDocument().getLength(), built, attrs);
            }
        });
        
    this.loggedInUsername = username;
    this.loggedInRole = role;
        java.util.Date now = new java.util.Date();
    customizeComboBoxes();
    customizeFloodComboBoxes();
    jPanel3.setPreferredSize(new Dimension(534, 383));
    jPanel4.setPreferredSize(new Dimension(515, 383));
    currentChartYear = 2026;
    updateYearNavigationButtons();
    setupTableConfiguration();
    loadResidentDataToTable();
    loadFloodDataToTable();
    loadDynamicResidentChart();
    loadDynamicFloodChart(currentChartYear);
    customizeFloodTimeField();
   
    setupFilterZoneCombo();
setupFilterStreetCombo();
setupFilterSeverityCombo();

loadDashboardMetrics();
loadUserGreeting();

    
    
    ButtonGroup timeGroup = new ButtonGroup();
    timeGroup.add(radioAuto);
    timeGroup.add(radioCustom);

// Set default state to Auto
    radioAuto.setSelected(true);
    flooddate.setEnabled(false);
    floodtime.setEnabled(false);

// Populate with current date and time

flooddate.setDate(now);
floodtime.setText(new java.text.SimpleDateFormat("HH:mm:ss").format(now));
    
arrow2.setVisible(true);
arrow3.setVisible(false);
arrow4.setVisible(false);
arrow5.setVisible(false);
arrow6.setVisible(false);
    

// Allow clicking a selected row again to deselect it
// Reliable click-to-select and click-again-to-deselect listener
floodtable.addMouseListener(new java.awt.event.MouseAdapter() {
    private int lastClickedRow = -1;

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        int row = floodtable.rowAtPoint(e.getPoint());
        
        if (row == -1) {
            // Clicked empty space below table rows -> clear selection
            floodtable.clearSelection();
            lastClickedRow = -1;
            return;
        }

        if (row == lastClickedRow) {
            // Clicked the same row a second time -> deselect it
            floodtable.clearSelection();
            lastClickedRow = -1;
        } else {
            // Clicked a new row -> select it and remember it
            lastClickedRow = row;
        }
    }
});

// Reliable click-to-select and click-again-to-deselect for Resident Directory
directorytable.addMouseListener(new java.awt.event.MouseAdapter() {
    private int lastClickedRow = -1;

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        int row = directorytable.rowAtPoint(e.getPoint());
        
        if (row == -1) {
            directorytable.clearSelection();
            lastClickedRow = -1;
            return;
        }

        if (row == lastClickedRow) {
            directorytable.clearSelection();
            lastClickedRow = -1;
        } else {
            lastClickedRow = row;
        }
    }
});
// Populate with current date and time (matching HH:mm format)

flooddate.setDate(now);
floodtime.setText(new java.text.SimpleDateFormat("HH:mm").format(now));
// Inside your constructor or initialization block

// Auto button listener
// Auto button listener
radioAuto.addActionListener(e -> {
    if (radioAuto.isSelected()) {
        flooddate.setEnabled(false);
        floodtime.setEnabled(false);
        java.util.Date currentDate = new java.util.Date();
        flooddate.setDate(currentDate);
        floodtime.setText(new java.text.SimpleDateFormat("HH:mm").format(currentDate));
    }
});

// Custom button listener
radioCustom.addActionListener(e -> {
    if (radioCustom.isSelected()) {
        flooddate.setEnabled(true);
        floodtime.setEnabled(true);
        flooddate.setDate(null);
        floodtime.setText("");
        floodtime.requestFocus();
    }
});
// For flood recording form
     
     


    searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
        @Override
        public void insertUpdate(javax.swing.event.DocumentEvent e) { applyFilter(); }
        @Override
        public void removeUpdate(javax.swing.event.DocumentEvent e) { applyFilter(); }
        @Override
        public void changedUpdate(javax.swing.event.DocumentEvent e) { applyFilter(); }
    });

    searchField.setText("Search resident...");
    searchField.setForeground(java.awt.Color.GRAY);

    searchField.addFocusListener(new java.awt.event.FocusAdapter() {
        @Override
        public void focusGained(java.awt.event.FocusEvent evt) {
            if (searchField.getText().equals("Search resident...")) {
                searchField.setText("");
                searchField.setForeground(java.awt.Color.BLACK);
            }
        }

        @Override
        public void focusLost(java.awt.event.FocusEvent evt) {
            if (searchField.getText().trim().isEmpty()) {
                searchField.setText("Search resident...");
                searchField.setForeground(java.awt.Color.GRAY);
            }
        }
    });

   
   

        mainTabbedPane.addChangeListener(e -> {
            int tab = mainTabbedPane.getSelectedIndex();
            
            arrow2.setVisible(tab == 0);
            arrow3.setVisible(tab == 1);
            arrow4.setVisible(tab == 2);
            arrow5.setVisible(tab == 3);
            arrow6.setVisible(tab == 4);
            
            if (tab == 0) {
        loadDashboardMetrics();
    }
            if (tab == 4) {
                loadFloodDataToTable();
            }
        });

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Load and scale images/icons safely
        safeSetIcon(jLabel19, "logo.png", 107, 100);
        safeSetIcon(jLabel10, "residentlogoreal.png", 107, 100);
        safeSetIcon(logoutexit, "logoutexit.png", 50, 40);
        safeSetIcon(jLabel14, "residentlogoreal.png", 107, 100);
        safeSetIcon(jLabel15, "floodpic.png", 107, 100);
        safeSetIcon(monitor, "monitor.png", 70, 80);
        safeSetIcon(dashpng, "dashboard.png", 73, 85);
        

        ImageIcon arrowIcon = loadSafeImageIcon("arrow.png", 50, 40);
        if (arrowIcon != null) {
            
            arrow2.setIcon(arrowIcon);
            arrow3.setIcon(arrowIcon);
            arrow4.setIcon(arrowIcon);
            arrow5.setIcon(arrowIcon);
            arrow6.setIcon(arrowIcon);
        }

        // Initial arrow state based on starting tab
        
        
                }

    // Helper for safe icon loading
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

    // -------------------------------------------------------------
    // COMBO BOX CUSTOMIZATION & VALIDATION METHODS
    // -------------------------------------------------------------
    
    private void customizeComboBoxes() {
        populateZonesFromDB();

        Font customFont = new Font("Segoe UI", Font.PLAIN, 14);
        Color backgroundColor = Color.WHITE;
        Color textColor = new Color(33, 33, 33);
        Color placeholderColor = new Color(128, 128, 128);
        Color accentGreen = new Color(204, 255, 204);          
        Color darkGreenBorder = new Color(120, 180, 120);      

        zonecheck.setFont(customFont);
        zonecheck.setBackground(backgroundColor);
        zonecheck.setForeground(textColor);
        zonecheck.setBorder(javax.swing.BorderFactory.createLineBorder(darkGreenBorder, 1, true));

        streetcheck.setFont(customFont);
        streetcheck.setBackground(backgroundColor);
        streetcheck.setForeground(textColor);
        streetcheck.setBorder(javax.swing.BorderFactory.createLineBorder(darkGreenBorder, 1, true));

        zonecheck.setMaximumRowCount(5);
        streetcheck.setMaximumRowCount(5);

        zonecheck.setPrototypeDisplayValue("------------------------");
        streetcheck.setPrototypeDisplayValue("------------------------");

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

        zonecheck.setRenderer(customRenderer);
        streetcheck.setRenderer(customRenderer);

        streetcheck.setModel(new DefaultComboBoxModel<>(new String[]{"-- Select Street --"}));
        zonecheck.addActionListener(e -> updateStreetOptionsFromDB());
    }

    private void customizeFloodComboBoxes() {
        populateFloodZonesFromDB();

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

        floodzone.setPrototypeDisplayValue("------------------------");
        floodstreet.setPrototypeDisplayValue("------------------------");
        sever.setPrototypeDisplayValue("------------------------");

        DefaultListCellRenderer floodRenderer = new DefaultListCellRenderer() {
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

        floodzone.setRenderer(floodRenderer);
        floodstreet.setRenderer(floodRenderer);
        sever.setRenderer(floodRenderer);

        floodstreet.setModel(new DefaultComboBoxModel<>(new String[]{"-- Select Street --"}));

        sever.setModel(new DefaultComboBoxModel<>(new String[]{
            "-- Select Severity --", 
            "Low", 
            "Moderate", 
            "High",
            "Critical"
        }));


        floodzone.addActionListener(e -> updateFloodStreetOptionsFromDB());
    }

    private void populateZonesFromDB() {
        DefaultComboBoxModel<String> zoneModel = new DefaultComboBoxModel<>();
        zoneModel.addElement("-- Select Zone --");

        String url = "jdbc:mysql://localhost:3306/flood_system";
        String user = "root";
        String pass = "";

        String query = "SELECT DISTINCT zone_name FROM tbl_location ORDER BY CAST(SUBSTRING_INDEX(zone_name, ' ', -1) AS UNSIGNED) ASC";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                zoneModel.addElement(rs.getString("zone_name"));
            }
            zonecheck.setModel(zoneModel);

        } catch (Exception e) {
            System.out.println("Error loading zones from DB: " + e.getMessage());
        }
    }

    private void populateFloodZonesFromDB() {
        DefaultComboBoxModel<String> zoneModel = new DefaultComboBoxModel<>();
        zoneModel.addElement("-- Select Zone --");

        String url = "jdbc:mysql://localhost:3306/flood_system";
        String user = "root";
        String pass = "";

        String query = "SELECT DISTINCT zone_name FROM tbl_location ORDER BY CAST(SUBSTRING_INDEX(zone_name, ' ', -1) AS UNSIGNED) ASC";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                zoneModel.addElement(rs.getString("zone_name"));
            }
            floodzone.setModel(zoneModel);

        } catch (Exception e) {
            System.out.println("Error loading flood zones from DB: " + e.getMessage());
        }
    }

    private void updateStreetOptionsFromDB() {
        if (streetcheck == null || zonecheck == null) return;

        if (zonecheck.getSelectedIndex() <= 0) {
            streetcheck.setModel(new DefaultComboBoxModel<>(new String[]{"-- Select Street --"}));
            return;
        }

        String selectedZone = (String) zonecheck.getSelectedItem();
        DefaultComboBoxModel<String> streetModel = new DefaultComboBoxModel<>();
        streetModel.addElement("-- Select Street --");

        String url = "jdbc:mysql://localhost:3306/flood_system";
        String user = "root";
        String pass = "";

        String query = "SELECT street_name FROM tbl_location WHERE zone_name = ? ORDER BY street_name ASC";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, selectedZone);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                streetModel.addElement(rs.getString("street_name"));
            }

            streetcheck.setModel(streetModel);
            streetcheck.setSelectedIndex(0);

        } catch (Exception e) {
            System.out.println("Error loading streets from DB: " + e.getMessage());
        }
    }

    private void updateFloodStreetOptionsFromDB() {
        if (floodstreet == null || floodzone == null) return;

        if (floodzone.getSelectedIndex() <= 0) {
            floodstreet.setModel(new DefaultComboBoxModel<>(new String[]{"-- Select Street --"}));
            return;
        }

        String selectedZone = (String) floodzone.getSelectedItem();
        DefaultComboBoxModel<String> streetModel = new DefaultComboBoxModel<>();
        streetModel.addElement("-- Select Street --");

        String url = "jdbc:mysql://localhost:3306/flood_system";
        String user = "root";
        String pass = "";

        String query = "SELECT street_name FROM tbl_location WHERE zone_name = ? ORDER BY street_name ASC";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, selectedZone);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                streetModel.addElement(rs.getString("street_name"));
            }

            floodstreet.setModel(streetModel);
            floodstreet.setSelectedIndex(0);

        } catch (Exception e) {
            System.out.println("Error loading flood streets from DB: " + e.getMessage());
        }
    }
    // Track the currently viewed year globally in your dashboard class
private int currentChartYear = 2026; 



public void loadDynamicFloodChart(int targetYear) {
    String url = "jdbc:mysql://localhost:3306/flood_system";
    
    final int minAvailableYear;
    final int maxAvailableYear;
    
    int tempMin = targetYear;
    int tempMax = targetYear;
    try (java.sql.Connection con = java.sql.DriverManager.getConnection(url, "root", "");
         java.sql.Statement st = con.createStatement();
         java.sql.ResultSet rs = st.executeQuery("SELECT MIN(YEAR(flood_date)) AS min_y, MAX(YEAR(flood_date)) AS max_y FROM tbl_flood_record")) {
        if (rs.next()) {
            int dbMin = rs.getInt("min_y");
            int dbMax = rs.getInt("max_y");
            if (dbMin > 0) tempMin = dbMin;
            if (dbMax > 0) tempMax = dbMax;
        }
    } catch (Exception e) {
        System.out.println("Error fetching year bounds: " + e.getMessage());
    }
    
    minAvailableYear = tempMin;
    maxAvailableYear = tempMax;

    java.util.ArrayList<String> categories = new java.util.ArrayList<>();
    java.util.ArrayList<Integer> values = new java.util.ArrayList<>();
    
    String query = "SELECT DATE_FORMAT(flood_date, '%b') AS mth, COUNT(*) AS total " +
                   "FROM tbl_flood_record " +
                   "WHERE YEAR(flood_date) = " + targetYear + " " +
                   "GROUP BY MONTH(flood_date) " +
                   "ORDER BY MONTH(flood_date) ASC";

    try (java.sql.Connection con = java.sql.DriverManager.getConnection(url, "root", "");
         java.sql.Statement st = con.createStatement();
         java.sql.ResultSet rs = st.executeQuery(query)) {
        
        while (rs.next()) {
            categories.add(rs.getString("mth"));
            values.add(rs.getInt("total"));
        }
    } catch (Exception e) {
        System.out.println("Error loading flood chart data: " + e.getMessage());
    }
    
    javax.swing.JPanel chartPanel = new javax.swing.JPanel() {
        @Override
        protected void paintComponent(java.awt.Graphics g) {
            super.paintComponent(g);
            java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
            g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2d.setColor(java.awt.Color.WHITE);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            
            g2d.setColor(new java.awt.Color(40, 40, 40));
            g2d.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
            g2d.drawString("Flood Occurrences (" + targetYear + ")", 20, 25);
            
            if (categories.isEmpty()) {
                g2d.setColor(java.awt.Color.GRAY);
                g2d.setFont(new java.awt.Font("Segoe UI", java.awt.Font.ITALIC, 12));
                g2d.drawString("No flood records found for " + targetYear + ".", 20, 60);
                g2d.drawString("Data available from " + minAvailableYear + " to " + maxAvailableYear + ".", 20, 80);
                return;
            }
            
            int maxVal = 1;
            for (int v : values) { if (v > maxVal) maxVal = v; }
            
            int x = 40;
            int yBaseline = getHeight() - 40;
            int availableHeight = getHeight() - 90;
            
            for (int i = 0; i < categories.size(); i++) {
                int val = values.get(i);
                int barHeight = (int) (((double) val / maxVal) * availableHeight);
                
                int calculatedWidth = (getWidth() - 80) / categories.size() - 15;
                int barWidth = Math.min(50, Math.max(25, calculatedWidth));
                
                java.awt.GradientPaint gp = new java.awt.GradientPaint(
                    x, yBaseline - barHeight, new java.awt.Color(46, 204, 113),
                    x, yBaseline, new java.awt.Color(39, 174, 96)
                );
                g2d.setPaint(gp);
                g2d.fillRect(x, yBaseline - barHeight, barWidth, barHeight);
                
                // Fixed Outline Color line
                g2d.setColor(new java.awt.Color(27, 108, 59));
                g2d.drawRect(x, yBaseline - barHeight, barWidth, barHeight);
                
                g2d.setColor(java.awt.Color.DARK_GRAY);
                g2d.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 16));
                g2d.drawString(categories.get(i), x + 2, yBaseline + 15);
                g2d.drawString(String.valueOf(val), x + (barWidth / 3), yBaseline - barHeight - 5);
                
                x += barWidth + 15;
            }
        }
    };
    
    jPanel3.setLayout(new java.awt.BorderLayout());
    jPanel3.removeAll();
    jPanel3.add(chartPanel, java.awt.BorderLayout.CENTER);
    jPanel3.revalidate();
    jPanel3.repaint();
}

public void loadDynamicResidentChart() {
    java.util.ArrayList<String> zones = new java.util.ArrayList<>();
    java.util.ArrayList<Integer> counts = new java.util.ArrayList<>();
    
    String url = "jdbc:mysql://localhost:3306/flood_system";
    // FIXED: Changed 'floods' to 'tbl_flood_record'
    String query = "SELECT l.zone_name, COUNT(r.resident_id) AS total_residents " +
                   "FROM tbl_location l " +
                   "LEFT JOIN tbl_resident r ON l.location_id = r.location_id " +
                   "WHERE l.zone_name IN (" +
                   "    SELECT DISTINCT loc.zone_name " +
                   "    FROM tbl_location loc " +
                   "    JOIN tbl_flood_record f ON loc.location_id = f.location_id" +
                   ") " +
                   "GROUP BY l.zone_name " +
                   "ORDER BY CAST(SUBSTRING(l.zone_name, 6) AS UNSIGNED) ASC";

    System.out.println("--- DEBUG: Attempting to connect to database for Resident Chart ---");

    try (java.sql.Connection con = java.sql.DriverManager.getConnection(url, "root", "");
         java.sql.Statement st = con.createStatement();
         java.sql.ResultSet rs = st.executeQuery(query)) {
        
        System.out.println("--- DEBUG: Query executed successfully. Reading rows... ---");
        
        while (rs.next()) {
            String zone = rs.getString("zone_name");
            int total = rs.getInt("total_residents");
            
            String safeZone = (zone != null && !zone.trim().isEmpty()) ? zone : "Unknown";
            zones.add(safeZone);
            counts.add(total);
            
            // FIXED: Replaced '|' with '+' for proper string concatenation
            System.out.println("DEBUG FOUND -> Zone: " + safeZone + " | Residents Count: " + total);
        }
        
        System.out.println("--- DEBUG: Total zones loaded: " + zones.size() + " ---");

    } catch (Exception e) {
        System.out.println("ERROR in loadDynamicResidentChart: " + e.getMessage());
        e.printStackTrace(); 
    }
    
    // Create the custom drawing panel
    javax.swing.JPanel chartPanel = new javax.swing.JPanel() {
        @Override
        protected void paintComponent(java.awt.Graphics g) {
            super.paintComponent(g);
            java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
            g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2d.setColor(java.awt.Color.WHITE);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            
            g2d.setColor(new java.awt.Color(40, 40, 40));
            g2d.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
            g2d.drawString("Residents Affected per Zone (Flood History)", 20, 25);
            
            if (zones.isEmpty()) {
                g2d.setColor(java.awt.Color.RED);
                g2d.drawString("No flood-affected zones found in database.", 20, 60);
                return;
            }
            
            int maxVal = 1;
            for (int c : counts) { if (c > maxVal) maxVal = c; }
            
            int x = 40;
            int panelH = getHeight() > 0 ? getHeight() : 383;
            
            int yBaseline = panelH - 95; 
            int availableHeight = panelH - 160;
            if (availableHeight < 50) availableHeight = 50;
            
            for (int i = 0; i < zones.size(); i++) {
                int val = counts.get(i);
                int barHeight = (int) (((double) val / maxVal) * availableHeight);
                if (barHeight < 5) barHeight = 5; 
                int barWidth = 40;
                
                java.awt.Color fillColor = new java.awt.Color(52, 152, 219);
                java.awt.Color darkFill = new java.awt.Color(41, 128, 185);
                
                java.awt.GradientPaint gp = new java.awt.GradientPaint(
                    x, yBaseline - barHeight, fillColor,
                    x, yBaseline, darkFill
                );
                g2d.setPaint(gp);
                g2d.fillRect(x, yBaseline - barHeight, barWidth, barHeight);
                
                g2d.setColor(new java.awt.Color(21, 67, 96));
                g2d.drawRect(x, yBaseline - barHeight, barWidth, barHeight);
                
                g2d.setColor(java.awt.Color.BLACK);
                g2d.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 11));
                g2d.drawString(zones.get(i), x, yBaseline + 18); 
                g2d.drawString(String.valueOf(val), x + (barWidth / 3), yBaseline - barHeight - 5);
                
                x += barWidth + 25;
            }
        }
    };
    
    int requiredWidth = Math.max(jPanel4.getWidth(), zones.size() * 65 + 80);
    int panelHeight = jPanel4.getHeight() > 0 ? jPanel4.getHeight() : 383;
    
    chartPanel.setPreferredSize(new java.awt.Dimension(requiredWidth, panelHeight));

    if (!(jPanel4.getLayout() instanceof java.awt.BorderLayout)) {
        jPanel4.setLayout(new java.awt.BorderLayout());
    }
    
    jPanel4.removeAll();
    
    javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(chartPanel);
    scrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    scrollPane.setBorder(null);
    
    jPanel4.add(scrollPane, java.awt.BorderLayout.CENTER);
    jPanel4.revalidate();
    jPanel4.repaint();
}




private void customizeFloodTimeField() {
    java.awt.Color white = java.awt.Color.WHITE;
    java.awt.Color textColor = new java.awt.Color(40, 40, 40);
    java.awt.Font uiFont = new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14);

    floodtime.setBackground(white);
    floodtime.setForeground(textColor);
    floodtime.setFont(uiFont);

    floodtime.setBorder(javax.swing.BorderFactory.createCompoundBorder(
        javax.swing.BorderFactory.createLineBorder(java.awt.Color.BLACK, 1),
        javax.swing.BorderFactory.createEmptyBorder(4, 6, 4, 6)
    ));

    try {
        // MaskFormatter for HH:mm
        javax.swing.text.MaskFormatter timeMask = new javax.swing.text.MaskFormatter("##:##");
        timeMask.setPlaceholderCharacter('_');
        timeMask.install(floodtime); 
    } catch (java.text.ParseException e) {
        e.printStackTrace();
    }

    // Smooth focus handling: places caret at the start if empty without locking input
    floodtime.addFocusListener(new java.awt.event.FocusAdapter() {
        @Override
        public void focusGained(java.awt.event.FocusEvent evt) {
            javax.swing.SwingUtilities.invokeLater(() -> {
                if (floodtime.getText().replace("_", "").trim().isEmpty()) {
                    floodtime.setCaretPosition(0);
                }
            });
        }
    });

    // Use InputVerifier to validate and cleanly reset the mask on error
    floodtime.setInputVerifier(new javax.swing.InputVerifier() {
        @Override
        public boolean verify(javax.swing.JComponent input) {
            javax.swing.JFormattedTextField ftf = (javax.swing.JFormattedTextField) input;
            String text = ftf.getText();
            
            // If empty or still has placeholders, let it pass
            if (text == null || text.contains("_")) {
                return true; 
            }

            try {
                String[] parts = text.split(":");
                int hours = Integer.parseInt(parts[0].trim());
                int minutes = Integer.parseInt(parts[1].trim());

                if (hours > 23 || minutes > 59) {
                    javax.swing.JOptionPane.showMessageDialog(
                        ftf,
                        "Invalid Time Format! Hours must be between 00 and 23, and minutes between 00 and 59.",
                        "Time Validation Error",
                        javax.swing.JOptionPane.WARNING_MESSAGE
                    );
                    
                    // Explicitly reset the mask template and snap cursor to the front
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        ftf.setText("__:__");
                        ftf.setCaretPosition(0);
                        ftf.requestFocusInWindow();
                    });
                    
                    return true; 
                }
            } catch (NumberFormatException ignored) {
                // Ignore parsing errors
            }
            return true;
        }
    });
}
public void loadDashboardMetrics() {
    String url = "jdbc:mysql://localhost:3306/flood_system";

    try (Connection con = DriverManager.getConnection(url, "root", "")) {
        
        // 1. Pending Residents -> connects to pendinglbl
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM tbl_resident WHERE status = 'Pending'")) {
            if (rs.next()) {
                int pendingCount = rs.getInt(1);
                if (pendinglbl != null) {
                    pendinglbl.setText(String.valueOf(pendingCount));
                }
            }
        }

        // 2. Total Flood Reports -> connects to reportlbl
        String floodQuery = "SELECT COUNT(*) FROM tbl_flood_record WHERE YEAR(flood_date) = ?";
        try (PreparedStatement pst = con.prepareStatement(floodQuery)) {
            pst.setInt(1, currentChartYear);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int totalFloods = rs.getInt(1);
                if (reportlbl != null) {
                    reportlbl.setText(String.valueOf(totalFloods));
                }
            }
        }

        // 3. Most Affected Location -> connects to locationlbl
        String locQuery = "SELECT l.zone_name FROM tbl_location l JOIN tbl_flood_record f ON l.location_id = f.location_id GROUP BY l.zone_name ORDER BY COUNT(*) DESC LIMIT 1";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(locQuery)) {
            if (rs.next()) {
                String topLocation = rs.getString(1);
                if (locationlbl != null) {
                    locationlbl.setText(topLocation != null ? topLocation : "N/A");
                }
            } else {
                if (locationlbl != null) {
                    locationlbl.setText("N/A");
                }
            }
        }

        // 4. Critical Severity Floods -> connects to criticallbl
        String critQuery = "SELECT COUNT(*) FROM tbl_flood_record WHERE severity = 'Critical' AND YEAR(flood_date) = ?";
        try (PreparedStatement pst = con.prepareStatement(critQuery)) {
            pst.setInt(1, currentChartYear);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int criticalCount = rs.getInt(1);
                if (criticallbl != null) {
                    criticallbl.setText(String.valueOf(criticalCount));
                }
            }
        }

    } catch (Exception e) {
        System.out.println("Error loading dashboard metrics: " + e.getMessage());
    }
}

private void loadUserGreeting() {
    String fullName = "User";
    String userRole = "Staff"; // Default fallback role for staff dashboard
    String url = "jdbc:mysql://localhost:3306/flood_system";
    
    // Fetch active logged-in user ID from Userusage session
    int activePersonnelId = Userusage.getUserId(); 
    
    // SQL Query fetching names AND role from tbl_personnel
    String query = "SELECT first_name2, middle_name2, last_name2, role FROM tbl_personnel WHERE personnel_id = ?";

    try (Connection con = DriverManager.getConnection(url, "root", "");
         PreparedStatement pst = con.prepareStatement(query)) {
        
        pst.setInt(1, activePersonnelId);
        ResultSet rs = pst.executeQuery();
        
        if (rs.next()) {
            String first = rs.getString("first_name2");
            String middle = rs.getString("middle_name2");
            String last = rs.getString("last_name2");
            String dbRole = rs.getString("role");
            
            // Set userRole dynamically from DB if present
            if (dbRole != null && !dbRole.trim().isEmpty()) {
                userRole = dbRole; 
            }
            
            StringBuilder sb = new StringBuilder();
            if (first != null && !first.trim().isEmpty()) sb.append(first).append(" ");
            if (middle != null && !middle.trim().isEmpty()) sb.append(middle).append(" ");
            if (last != null && !last.trim().isEmpty()) sb.append(last);
            
            String combined = sb.toString().trim();
            if (!combined.isEmpty()) {
                fullName = combined;
            }
        }
    } catch (Exception e) {
        System.out.println("Error loading active user's full name: " + e.getMessage());
    }

    // Determine time of day for greeting
    java.util.Calendar cal = java.util.Calendar.getInstance();
    int hour = cal.get(java.util.Calendar.HOUR_OF_DAY);
    
    String timeGreeting = "Good Morning";
    if (hour >= 12 && hour < 17) {
        timeGreeting = "Good Afternoon";
    } else if (hour >= 17) {
        timeGreeting = "Good Evening";
    }

    // Displays: "Good Morning, Staff Juan Dela Cruz!"
    if (lblWelcome != null) {
        lblWelcome.setText(timeGreeting + ", " + userRole + " " + fullName + "!");
        lblWelcome.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 22));
        lblWelcome.setForeground(new java.awt.Color(33, 33, 33));
    }
}
public void loadPersonnelDataToTable() {
    // Your database logic to fetch and reload personnel table rows
    // e.g., fetching from tbl_users and setting the table model
}
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        present = new javax.swing.ButtonGroup();
        customtime = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        logoutexit = new javax.swing.JLabel();
        logoutbtn = new javax.swing.JButton();
        dashboard = new javax.swing.JButton();
        ResidentInfosbtn1 = new javax.swing.JButton();
        floodinfo = new javax.swing.JButton();
        arrow2 = new javax.swing.JLabel();
        arrow3 = new javax.swing.JLabel();
        arrow4 = new javax.swing.JLabel();
        arrow5 = new javax.swing.JLabel();
        Recordfloodbtn = new javax.swing.JButton();
        Addresidentbtn2 = new javax.swing.JButton();
        arrow6 = new javax.swing.JLabel();
        bckgrnd = new javax.swing.JPanel();
        mainTabbedPane = new javax.swing.JTabbedPane();
        staffdashbaord = new javax.swing.JPanel();
        paneldashboard = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        dashpng = new javax.swing.JLabel();
        lblWelcome = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        reportlbl = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        pendinglbl = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jLabel48 = new javax.swing.JLabel();
        locationlbl = new javax.swing.JLabel();
        jPanel27 = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        criticallbl = new javax.swing.JLabel();
        addresidentpanel = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        firstname = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        contact = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        signups1 = new javax.swing.JButton();
        zonecheck = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        streetcheck = new javax.swing.JComboBox<>();
        jLabel21 = new javax.swing.JLabel();
        middlename = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        lastname = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        validid = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        residentdirectory = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        directorytable = new javax.swing.JTable();
        view = new javax.swing.JButton();
        next = new javax.swing.JButton();
        pages = new javax.swing.JLabel();
        searchField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        prev = new javax.swing.JButton();
        recording = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        submitbtn = new javax.swing.JButton();
        floodzone = new javax.swing.JComboBox<>();
        floodstreet = new javax.swing.JComboBox<>();
        sever = new javax.swing.JComboBox<>();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        floodtime = new javax.swing.JFormattedTextField();
        radioCustom = new javax.swing.JRadioButton();
        radioAuto = new javax.swing.JRadioButton();
        flooddate = new com.toedter.calendar.JDateChooser();
        monitoring = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        monitor = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        floodtable = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        btnNextYear = new javax.swing.JButton();
        btnPrevYear1 = new javax.swing.JButton();
        filterZoneCombo = new javax.swing.JComboBox<>();
        filterSeverityCombo = new javax.swing.JComboBox<>();
        filterStreetCombo = new javax.swing.JComboBox<>();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(240, 238, 238));
        jPanel1.setPreferredSize(new java.awt.Dimension(932, 417));

        jPanel2.setBackground(new java.awt.Color(11, 102, 35));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Segoe UI Emoji", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Flood Recording & Monitoring System");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(129, 78, -1, -1));

        jLabel11.setFont(new java.awt.Font("Segoe UI Emoji", 1, 22)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("BARANGAY CARMEN");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(123, 28, -1, 44));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel19.setBackground(new java.awt.Color(255, 255, 255));
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/flood_record_monitorsystem/logo.png"))); // NOI18N
        jPanel7.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 115, 98));

        jPanel2.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 8, 109, 100));

        logoutexit.setBackground(new java.awt.Color(255, 255, 255));
        logoutexit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/flood_record_monitorsystem/logoutexit.png"))); // NOI18N
        jPanel2.add(logoutexit, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 880, 60, 57));

        logoutbtn.setFont(new java.awt.Font("Segoe UI Emoji", 1, 29)); // NOI18N
        logoutbtn.setForeground(new java.awt.Color(255, 0, 0));
        logoutbtn.setText("LOGOUT");
        logoutbtn.setBorder(null);
        logoutbtn.setContentAreaFilled(false);
        logoutbtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        logoutbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutbtnActionPerformed(evt);
            }
        });
        jPanel2.add(logoutbtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 890, 120, 50));

        dashboard.setBackground(new java.awt.Color(11, 102, 35));
        dashboard.setFont(new java.awt.Font("Segoe UI Emoji", 1, 29)); // NOI18N
        dashboard.setForeground(new java.awt.Color(255, 255, 255));
        dashboard.setText("Dashboard");
        dashboard.setBorder(null);
        dashboard.setContentAreaFilled(false);
        dashboard.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        dashboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dashboardActionPerformed(evt);
            }
        });
        jPanel2.add(dashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 230, 150, 40));

        ResidentInfosbtn1.setBackground(new java.awt.Color(11, 102, 35));
        ResidentInfosbtn1.setFont(new java.awt.Font("Segoe UI Emoji", 1, 29)); // NOI18N
        ResidentInfosbtn1.setForeground(new java.awt.Color(255, 255, 255));
        ResidentInfosbtn1.setText("Resident Directory");
        ResidentInfosbtn1.setBorder(null);
        ResidentInfosbtn1.setContentAreaFilled(false);
        ResidentInfosbtn1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        ResidentInfosbtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ResidentInfosbtn1ActionPerformed(evt);
            }
        });
        jPanel2.add(ResidentInfosbtn1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 380, 250, 40));

        floodinfo.setBackground(new java.awt.Color(11, 102, 35));
        floodinfo.setFont(new java.awt.Font("Segoe UI Emoji", 1, 29)); // NOI18N
        floodinfo.setForeground(new java.awt.Color(255, 255, 255));
        floodinfo.setText("Monitoring");
        floodinfo.setBorder(null);
        floodinfo.setContentAreaFilled(false);
        floodinfo.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        floodinfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                floodinfoActionPerformed(evt);
            }
        });
        jPanel2.add(floodinfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 530, 160, 40));

        arrow2.setBackground(new java.awt.Color(255, 255, 255));
        arrow2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/flood_record_monitorsystem/arrow.png"))); // NOI18N
        jPanel2.add(arrow2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 220, 49, 55));

        arrow3.setBackground(new java.awt.Color(255, 255, 255));
        arrow3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/flood_record_monitorsystem/arrow.png"))); // NOI18N
        jPanel2.add(arrow3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 290, 49, 55));

        arrow4.setBackground(new java.awt.Color(255, 255, 255));
        arrow4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/flood_record_monitorsystem/arrow.png"))); // NOI18N
        jPanel2.add(arrow4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 370, 49, 55));

        arrow5.setBackground(new java.awt.Color(255, 255, 255));
        arrow5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/flood_record_monitorsystem/arrow.png"))); // NOI18N
        jPanel2.add(arrow5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 450, 49, 50));

        Recordfloodbtn.setBackground(new java.awt.Color(11, 102, 35));
        Recordfloodbtn.setFont(new java.awt.Font("Segoe UI Emoji", 1, 29)); // NOI18N
        Recordfloodbtn.setForeground(new java.awt.Color(255, 255, 255));
        Recordfloodbtn.setText("Record Flood");
        Recordfloodbtn.setBorder(null);
        Recordfloodbtn.setContentAreaFilled(false);
        Recordfloodbtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Recordfloodbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RecordfloodbtnActionPerformed(evt);
            }
        });
        jPanel2.add(Recordfloodbtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 460, 190, 40));

        Addresidentbtn2.setBackground(new java.awt.Color(11, 102, 35));
        Addresidentbtn2.setFont(new java.awt.Font("Segoe UI Emoji", 1, 29)); // NOI18N
        Addresidentbtn2.setForeground(new java.awt.Color(255, 255, 255));
        Addresidentbtn2.setText("Add Resident");
        Addresidentbtn2.setBorder(null);
        Addresidentbtn2.setContentAreaFilled(false);
        Addresidentbtn2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Addresidentbtn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Addresidentbtn2ActionPerformed(evt);
            }
        });
        jPanel2.add(Addresidentbtn2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 300, 190, 40));

        arrow6.setBackground(new java.awt.Color(255, 255, 255));
        arrow6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/flood_record_monitorsystem/arrow.png"))); // NOI18N
        jPanel2.add(arrow6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 520, 49, 55));

        bckgrnd.setBackground(new java.awt.Color(244, 254, 241));
        bckgrnd.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        bckgrnd.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        paneldashboard.setBackground(new java.awt.Color(255, 255, 255));
        paneldashboard.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jPanel10.setBackground(new java.awt.Color(204, 255, 204));
        jPanel10.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel38.setFont(new java.awt.Font("Perpetua", 1, 100)); // NOI18N
        jLabel38.setText("Dashboard");

        dashpng.setBackground(new java.awt.Color(255, 255, 255));
        dashpng.setIcon(new javax.swing.ImageIcon(getClass().getResource("/flood_record_monitorsystem/dashboard.png"))); // NOI18N

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(236, 236, 236)
                .addComponent(dashpng, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 711, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLabel38))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(dashpng, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        lblWelcome.setFont(new java.awt.Font("Segoe UI Emoji", 0, 40)); // NOI18N
        lblWelcome.setText("Name and welcome");

        jPanel22.setBackground(new java.awt.Color(204, 255, 204));

        jLabel45.setFont(new java.awt.Font("Segoe UI Emoji", 1, 24)); // NOI18N
        jLabel45.setText("Flood Reports");

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                .addContainerGap(156, Short.MAX_VALUE)
                .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(73, 73, 73))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );

        reportlbl.setBackground(new java.awt.Color(255, 0, 0));
        reportlbl.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        reportlbl.setForeground(new java.awt.Color(255, 0, 0));
        reportlbl.setText("R");

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(reportlbl)
                .addGap(205, 205, 205))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(reportlbl, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel24.setBackground(new java.awt.Color(204, 255, 204));

        jLabel47.setFont(new java.awt.Font("Segoe UI Emoji", 1, 24)); // NOI18N
        jLabel47.setText("Pending Residents");

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addGap(110, 110, 110)
                .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pendinglbl.setBackground(new java.awt.Color(255, 255, 153));
        pendinglbl.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        pendinglbl.setText("p");

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGap(195, 195, 195)
                .addComponent(pendinglbl)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pendinglbl, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 77, Short.MAX_VALUE))
        );

        jPanel26.setBackground(new java.awt.Color(204, 255, 204));

        jLabel48.setFont(new java.awt.Font("Segoe UI Emoji", 1, 24)); // NOI18N
        jLabel48.setText("Most Affected Location");

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addGap(98, 98, 98)
                .addComponent(jLabel48)
                .addContainerGap(79, Short.MAX_VALUE))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        locationlbl.setBackground(new java.awt.Color(204, 204, 0));
        locationlbl.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        locationlbl.setForeground(new java.awt.Color(204, 153, 0));
        locationlbl.setText("location");

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addGap(151, 151, 151)
                .addComponent(locationlbl, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(locationlbl, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel28.setBackground(new java.awt.Color(204, 255, 204));

        jLabel46.setFont(new java.awt.Font("Segoe UI Emoji", 1, 24)); // NOI18N
        jLabel46.setText("Critical Floods");

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel28Layout.createSequentialGroup()
                .addContainerGap(143, Short.MAX_VALUE)
                .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(86, 86, 86))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel28Layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        criticallbl.setBackground(new java.awt.Color(102, 102, 0));
        criticallbl.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        criticallbl.setForeground(new java.awt.Color(153, 153, 0));
        criticallbl.setText("C");

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel27Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(criticallbl)
                .addGap(200, 200, 200))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(criticallbl, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(73, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout paneldashboardLayout = new javax.swing.GroupLayout(paneldashboard);
        paneldashboard.setLayout(paneldashboardLayout);
        paneldashboardLayout.setHorizontalGroup(
            paneldashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneldashboardLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(paneldashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paneldashboardLayout.createSequentialGroup()
                        .addGroup(paneldashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(paneldashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel21, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel27, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(paneldashboardLayout.createSequentialGroup()
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(lblWelcome, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(31, 31, 31))
        );
        paneldashboardLayout.setVerticalGroup(
            paneldashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneldashboardLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(lblWelcome)
                .addGap(75, 75, 75)
                .addGroup(paneldashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(116, 116, 116)
                .addGroup(paneldashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(355, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout staffdashbaordLayout = new javax.swing.GroupLayout(staffdashbaord);
        staffdashbaord.setLayout(staffdashbaordLayout);
        staffdashbaordLayout.setHorizontalGroup(
            staffdashbaordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1132, Short.MAX_VALUE)
            .addGroup(staffdashbaordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(staffdashbaordLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(paneldashboard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        staffdashbaordLayout.setVerticalGroup(
            staffdashbaordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1325, Short.MAX_VALUE)
            .addGroup(staffdashbaordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(staffdashbaordLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(paneldashboard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        mainTabbedPane.addTab("tab5", staffdashbaord);

        addresidentpanel.setBackground(new java.awt.Color(255, 255, 255));
        addresidentpanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jPanel6.setBackground(new java.awt.Color(204, 255, 204));
        jPanel6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel2.setFont(new java.awt.Font("Perpetua", 1, 100)); // NOI18N
        jLabel2.setText("Add Resident ");

        jLabel10.setBackground(new java.awt.Color(255, 255, 255));
        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/flood_record_monitorsystem/residentlogoreal.png"))); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(214, 214, 214)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addContainerGap(141, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLabel2))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        firstname.setFont(new java.awt.Font("Segoe UI Light", 0, 30)); // NOI18N
        firstname.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204)));
        firstname.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                firstnameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                firstnameFocusLost(evt);
            }
        });
        firstname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                firstnameActionPerformed(evt);
            }
        });

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Segoe UI Emoji", 1, 36)); // NOI18N
        jLabel4.setText("First Name");

        jLabel6.setBackground(new java.awt.Color(255, 0, 0));
        jLabel6.setFont(new java.awt.Font("Segoe UI Emoji", 1, 36)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(204, 0, 51));
        jLabel6.setText("*");

        jLabel1.setFont(new java.awt.Font("Segoe UI Emoji", 1, 36)); // NOI18N
        jLabel1.setText("Select  Zone");

        jLabel8.setBackground(new java.awt.Color(255, 0, 0));
        jLabel8.setFont(new java.awt.Font("Segoe UI Emoji", 1, 36)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(204, 0, 51));
        jLabel8.setText("*");

        contact.setFont(new java.awt.Font("Segoe UI Light", 0, 30)); // NOI18N
        contact.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204)));
        contact.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                contactFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                contactFocusLost(evt);
            }
        });
        contact.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contactActionPerformed(evt);
            }
        });
        contact.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                contactKeyTyped(evt);
            }
        });

        jLabel18.setBackground(new java.awt.Color(255, 0, 0));
        jLabel18.setFont(new java.awt.Font("Segoe UI Emoji", 1, 36)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(204, 0, 51));
        jLabel18.setText("*");

        jLabel5.setFont(new java.awt.Font("Segoe UI Emoji", 1, 48)); // NOI18N
        jLabel5.setText("Personal Info");

        signups1.setBackground(new java.awt.Color(0, 204, 0));
        signups1.setFont(new java.awt.Font("Segoe UI Historic", 1, 50)); // NOI18N
        signups1.setText("SUBMIT");
        signups1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        signups1.setContentAreaFilled(false);
        signups1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        signups1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signups1ActionPerformed(evt);
            }
        });

        zonecheck.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        zonecheck.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        zonecheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zonecheckActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Segoe UI Emoji", 1, 36)); // NOI18N
        jLabel16.setText("Contact Number");

        jLabel17.setBackground(new java.awt.Color(255, 0, 0));
        jLabel17.setFont(new java.awt.Font("Segoe UI Emoji", 1, 36)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(204, 0, 51));
        jLabel17.setText("*");

        jLabel20.setFont(new java.awt.Font("Segoe UI Emoji", 1, 36)); // NOI18N
        jLabel20.setText("Select street");

        streetcheck.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        streetcheck.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        streetcheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                streetcheckActionPerformed(evt);
            }
        });

        jLabel21.setBackground(new java.awt.Color(255, 0, 0));
        jLabel21.setFont(new java.awt.Font("Segoe UI Emoji", 1, 36)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(204, 0, 51));
        jLabel21.setText("*");

        middlename.setFont(new java.awt.Font("Segoe UI Light", 0, 30)); // NOI18N
        middlename.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204)));
        middlename.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                middlenameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                middlenameFocusLost(evt);
            }
        });
        middlename.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                middlenameActionPerformed(evt);
            }
        });

        jLabel7.setBackground(new java.awt.Color(255, 255, 255));
        jLabel7.setFont(new java.awt.Font("Segoe UI Emoji", 1, 36)); // NOI18N
        jLabel7.setText("Last Name");

        lastname.setFont(new java.awt.Font("Segoe UI Light", 0, 30)); // NOI18N
        lastname.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204)));
        lastname.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                lastnameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                lastnameFocusLost(evt);
            }
        });
        lastname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lastnameActionPerformed(evt);
            }
        });

        jLabel24.setBackground(new java.awt.Color(255, 0, 0));
        jLabel24.setFont(new java.awt.Font("Segoe UI Emoji", 1, 36)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(204, 0, 51));
        jLabel24.setText("*");

        jLabel26.setBackground(new java.awt.Color(255, 255, 255));
        jLabel26.setFont(new java.awt.Font("Segoe UI Emoji", 1, 36)); // NOI18N
        jLabel26.setText("Middle Name");

        jLabel27.setBackground(new java.awt.Color(255, 0, 0));
        jLabel27.setFont(new java.awt.Font("Segoe UI Emoji", 1, 36)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(204, 0, 51));
        jLabel27.setText("*");

        validid.setFont(new java.awt.Font("Segoe UI Light", 0, 30)); // NOI18N
        validid.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204)));
        validid.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                valididFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                valididFocusLost(evt);
            }
        });
        validid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                valididActionPerformed(evt);
            }
        });
        validid.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                valididKeyTyped(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Segoe UI Emoji", 1, 36)); // NOI18N
        jLabel28.setText("Residential ID");

        jLabel37.setBackground(new java.awt.Color(255, 0, 0));
        jLabel37.setFont(new java.awt.Font("Segoe UI Emoji", 1, 36)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(204, 0, 51));
        jLabel37.setText("*");

        jLabel39.setFont(new java.awt.Font("Segoe UI Emoji", 1, 48)); // NOI18N
        jLabel39.setText("Residential Address");

        javax.swing.GroupLayout addresidentpanelLayout = new javax.swing.GroupLayout(addresidentpanel);
        addresidentpanel.setLayout(addresidentpanelLayout);
        addresidentpanelLayout.setHorizontalGroup(
            addresidentpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addresidentpanelLayout.createSequentialGroup()
                .addGroup(addresidentpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addresidentpanelLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(addresidentpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(addresidentpanelLayout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel8)
                                .addGap(145, 145, 145)
                                .addComponent(jLabel26)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel24))
                            .addGroup(addresidentpanelLayout.createSequentialGroup()
                                .addComponent(jLabel39)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel21))
                            .addGroup(addresidentpanelLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(addresidentpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(addresidentpanelLayout.createSequentialGroup()
                                        .addComponent(zonecheck, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(80, 80, 80)
                                        .addGroup(addresidentpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(addresidentpanelLayout.createSequentialGroup()
                                                .addComponent(jLabel20)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel18))
                                            .addComponent(streetcheck, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(addresidentpanelLayout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel17)))))
                        .addGap(0, 15, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addresidentpanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(addresidentpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(contact, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16)
                            .addComponent(firstname, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(addresidentpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(addresidentpanelLayout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(772, 772, 772))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addresidentpanelLayout.createSequentialGroup()
                                .addGroup(addresidentpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(addresidentpanelLayout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel27))
                                    .addComponent(lastname, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(70, 70, 70)))))
                .addContainerGap(16, Short.MAX_VALUE))
            .addGroup(addresidentpanelLayout.createSequentialGroup()
                .addGroup(addresidentpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addresidentpanelLayout.createSequentialGroup()
                        .addGap(385, 385, 385)
                        .addGroup(addresidentpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(addresidentpanelLayout.createSequentialGroup()
                                .addComponent(jLabel28)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel37))
                            .addGroup(addresidentpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(validid, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(middlename, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(addresidentpanelLayout.createSequentialGroup()
                        .addGap(337, 337, 337)
                        .addComponent(signups1, javax.swing.GroupLayout.PREFERRED_SIZE, 451, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        addresidentpanelLayout.setVerticalGroup(
            addresidentpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addresidentpanelLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(addresidentpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addresidentpanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(addresidentpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel27))
                        .addGap(18, 18, 18))
                    .addGroup(addresidentpanelLayout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jLabel5)
                        .addGroup(addresidentpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(addresidentpanelLayout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addGroup(addresidentpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel26)))
                            .addGroup(addresidentpanelLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel24)))
                        .addGap(12, 12, 12)))
                .addGroup(addresidentpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(firstname, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lastname, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(middlename, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(addresidentpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addresidentpanelLayout.createSequentialGroup()
                        .addGap(76, 76, 76)
                        .addGroup(addresidentpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(jLabel6)
                            .addComponent(jLabel28)
                            .addComponent(jLabel37))
                        .addGap(18, 18, 18)
                        .addGroup(addresidentpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(contact, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(validid, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(35, 35, 35)
                        .addComponent(jLabel21))
                    .addGroup(addresidentpanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 215, Short.MAX_VALUE)
                        .addComponent(jLabel39)
                        .addGap(18, 18, 18)
                        .addGroup(addresidentpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel17)
                            .addComponent(jLabel20)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(addresidentpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(zonecheck, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(streetcheck, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(52, 52, 52)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(signups1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(208, 208, 208))
        );

        mainTabbedPane.addTab("tab1", addresidentpanel);

        residentdirectory.setBackground(new java.awt.Color(255, 255, 255));
        residentdirectory.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jPanel8.setBackground(new java.awt.Color(204, 255, 204));
        jPanel8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel12.setFont(new java.awt.Font("Perpetua", 1, 100)); // NOI18N
        jLabel12.setText("Resident Info");

        jLabel14.setBackground(new java.awt.Color(255, 255, 255));
        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/flood_record_monitorsystem/residentlogoreal.png"))); // NOI18N

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(167, 167, 167)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(jLabel12)
                .addContainerGap(178, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(64, 64, 64))))
        );

        directorytable.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        directorytable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "First Name", "Middle Name", "Last Name", "Valid Id", "Zone", "Street", "Contact No", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(directorytable);
        if (directorytable.getColumnModel().getColumnCount() > 0) {
            directorytable.getColumnModel().getColumn(0).setResizable(false);
            directorytable.getColumnModel().getColumn(0).setPreferredWidth(100);
            directorytable.getColumnModel().getColumn(1).setResizable(false);
            directorytable.getColumnModel().getColumn(1).setPreferredWidth(100);
            directorytable.getColumnModel().getColumn(2).setResizable(false);
            directorytable.getColumnModel().getColumn(2).setPreferredWidth(69);
            directorytable.getColumnModel().getColumn(3).setResizable(false);
            directorytable.getColumnModel().getColumn(3).setPreferredWidth(100);
            directorytable.getColumnModel().getColumn(4).setResizable(false);
            directorytable.getColumnModel().getColumn(4).setPreferredWidth(69);
            directorytable.getColumnModel().getColumn(5).setResizable(false);
            directorytable.getColumnModel().getColumn(5).setPreferredWidth(125);
            directorytable.getColumnModel().getColumn(6).setResizable(false);
            directorytable.getColumnModel().getColumn(6).setPreferredWidth(100);
            directorytable.getColumnModel().getColumn(7).setResizable(false);
        }

        view.setBackground(new java.awt.Color(204, 255, 204));
        view.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        view.setText("VIEW");
        view.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        view.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewActionPerformed(evt);
            }
        });

        next.setBackground(new java.awt.Color(204, 255, 204));
        next.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        next.setText("NEXT");
        next.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextActionPerformed(evt);
            }
        });

        searchField.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                searchFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                searchFieldFocusLost(evt);
            }
        });
        searchField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchFieldActionPerformed(evt);
            }
        });

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/flood_record_monitorsystem/search.png"))); // NOI18N

        prev.setBackground(new java.awt.Color(204, 255, 204));
        prev.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        prev.setText("BACK");
        prev.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        prev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prevActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout residentdirectoryLayout = new javax.swing.GroupLayout(residentdirectory);
        residentdirectory.setLayout(residentdirectoryLayout);
        residentdirectoryLayout.setHorizontalGroup(
            residentdirectoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(residentdirectoryLayout.createSequentialGroup()
                .addGroup(residentdirectoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(residentdirectoryLayout.createSequentialGroup()
                        .addGap(176, 176, 176)
                        .addComponent(view, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(97, 97, 97)
                        .addComponent(prev, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)
                        .addComponent(pages, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(next, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(residentdirectoryLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(residentdirectoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(residentdirectoryLayout.createSequentialGroup()
                                .addGap(76, 76, 76)
                                .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 930, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(residentdirectoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane1)))))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        residentdirectoryLayout.setVerticalGroup(
            residentdirectoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(residentdirectoryLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66)
                .addGroup(residentdirectoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(searchField, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addGroup(residentdirectoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(residentdirectoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(view, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(prev, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(residentdirectoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(next, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pages, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)))
                .addContainerGap(378, Short.MAX_VALUE))
        );

        mainTabbedPane.addTab("tab2", residentdirectory);

        recording.setBackground(new java.awt.Color(255, 255, 255));
        recording.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jPanel9.setBackground(new java.awt.Color(204, 255, 204));
        jPanel9.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel13.setFont(new java.awt.Font("Perpetua", 1, 100)); // NOI18N
        jLabel13.setText("Record Flood");

        jLabel15.setBackground(new java.awt.Color(255, 255, 255));
        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/flood_record_monitorsystem/floodpic.png"))); // NOI18N

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(209, 209, 209)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13)
                .addContainerGap(168, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jLabel22.setFont(new java.awt.Font("Segoe UI Emoji", 1, 36)); // NOI18N
        jLabel22.setText("Flood Severity Level");

        jLabel25.setFont(new java.awt.Font("Segoe UI Emoji", 1, 36)); // NOI18N
        jLabel25.setText("Zone");

        jLabel29.setFont(new java.awt.Font("Segoe UI Emoji", 1, 36)); // NOI18N
        jLabel29.setText("Time of Flood");

        jLabel30.setFont(new java.awt.Font("Segoe UI Emoji", 1, 36)); // NOI18N
        jLabel30.setText("Date of Flood");

        jLabel31.setFont(new java.awt.Font("Segoe UI Emoji", 1, 36)); // NOI18N
        jLabel31.setText("Street");

        submitbtn.setBackground(new java.awt.Color(0, 204, 0));
        submitbtn.setFont(new java.awt.Font("Segoe UI Historic", 1, 50)); // NOI18N
        submitbtn.setText("SUBMIT");
        submitbtn.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        submitbtn.setContentAreaFilled(false);
        submitbtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        submitbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitbtnActionPerformed(evt);
            }
        });

        floodzone.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        floodzone.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        floodstreet.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        floodstreet.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        sever.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        sever.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        sever.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                severActionPerformed(evt);
            }
        });

        jLabel32.setBackground(new java.awt.Color(255, 0, 0));
        jLabel32.setFont(new java.awt.Font("Segoe UI Emoji", 1, 36)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(204, 0, 51));
        jLabel32.setText("*");

        jLabel33.setBackground(new java.awt.Color(255, 0, 0));
        jLabel33.setFont(new java.awt.Font("Segoe UI Emoji", 1, 36)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(204, 0, 51));
        jLabel33.setText("*");

        jLabel34.setBackground(new java.awt.Color(255, 0, 0));
        jLabel34.setFont(new java.awt.Font("Segoe UI Emoji", 1, 36)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(204, 0, 51));
        jLabel34.setText("*");

        jLabel35.setBackground(new java.awt.Color(255, 0, 0));
        jLabel35.setFont(new java.awt.Font("Segoe UI Emoji", 1, 36)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(204, 0, 51));
        jLabel35.setText("*");

        jLabel36.setBackground(new java.awt.Color(255, 0, 0));
        jLabel36.setFont(new java.awt.Font("Segoe UI Emoji", 1, 36)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(204, 0, 51));
        jLabel36.setText("*");

        floodtime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                floodtimeActionPerformed(evt);
            }
        });

        customtime.add(radioCustom);
        radioCustom.setFont(new java.awt.Font("Segoe UI Emoji", 1, 30)); // NOI18N
        radioCustom.setText("Custom");

        present.add(radioAuto);
        radioAuto.setFont(new java.awt.Font("Segoe UI Emoji", 1, 30)); // NOI18N
        radioAuto.setSelected(true);
        radioAuto.setText("Present");

        javax.swing.GroupLayout recordingLayout = new javax.swing.GroupLayout(recording);
        recording.setLayout(recordingLayout);
        recordingLayout.setHorizontalGroup(
            recordingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(recordingLayout.createSequentialGroup()
                .addGap(77, 77, 77)
                .addGroup(recordingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(floodtime, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(recordingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(recordingLayout.createSequentialGroup()
                            .addComponent(radioCustom)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(radioAuto))
                        .addComponent(flooddate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(recordingLayout.createSequentialGroup()
                        .addComponent(jLabel30)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel32))
                    .addGroup(recordingLayout.createSequentialGroup()
                        .addComponent(jLabel29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel35)))
                .addGroup(recordingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, recordingLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 271, Short.MAX_VALUE)
                        .addGroup(recordingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(recordingLayout.createSequentialGroup()
                                .addComponent(jLabel31)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel34))
                            .addComponent(floodzone, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(recordingLayout.createSequentialGroup()
                                .addComponent(jLabel25)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel33))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, recordingLayout.createSequentialGroup()
                        .addGap(271, 271, 271)
                        .addComponent(floodstreet, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(recordingLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(recordingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(recordingLayout.createSequentialGroup()
                                .addComponent(jLabel22)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel36))
                            .addComponent(sever, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(40, 40, 40))
            .addGroup(recordingLayout.createSequentialGroup()
                .addGroup(recordingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(recordingLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(recordingLayout.createSequentialGroup()
                        .addGap(307, 307, 307)
                        .addComponent(submitbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 451, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        recordingLayout.setVerticalGroup(
            recordingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(recordingLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(recordingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(recordingLayout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addGroup(recordingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(radioCustom, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(radioAuto))
                        .addGap(41, 41, 41))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, recordingLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(recordingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25)
                            .addComponent(jLabel33))))
                .addGroup(recordingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(recordingLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(floodzone, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(recordingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(recordingLayout.createSequentialGroup()
                            .addGap(28, 28, 28)
                            .addComponent(jLabel32))
                        .addComponent(jLabel30, javax.swing.GroupLayout.Alignment.TRAILING)))
                .addGroup(recordingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(recordingLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(flooddate, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                        .addGroup(recordingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, recordingLayout.createSequentialGroup()
                                .addComponent(jLabel29)
                                .addGap(18, 18, 18))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, recordingLayout.createSequentialGroup()
                                .addComponent(jLabel35)
                                .addGap(26, 26, 26))))
                    .addGroup(recordingLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(recordingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel34, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addComponent(floodstreet, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(53, 53, 53)))
                .addGroup(recordingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(jLabel36)
                    .addComponent(floodtime, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(sever, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(72, 72, 72)
                .addComponent(submitbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(262, Short.MAX_VALUE))
        );

        mainTabbedPane.addTab("tab3", recording);

        monitoring.setBackground(new java.awt.Color(255, 255, 255));
        monitoring.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jPanel11.setBackground(new java.awt.Color(204, 255, 204));
        jPanel11.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel23.setFont(new java.awt.Font("Perpetua", 1, 100)); // NOI18N
        jLabel23.setText("Flood Monitoring");

        monitor.setBackground(new java.awt.Color(255, 255, 255));
        monitor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/flood_record_monitorsystem/monitor.png"))); // NOI18N

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(111, 111, 111)
                .addComponent(monitor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel23)
                .addContainerGap(128, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addComponent(monitor, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30))))
        );

        floodtable.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        floodtable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Flood", "Date", "Time", "Zone", "Street", "Severity"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(floodtable);
        if (floodtable.getColumnModel().getColumnCount() > 0) {
            floodtable.getColumnModel().getColumn(0).setResizable(false);
            floodtable.getColumnModel().getColumn(0).setPreferredWidth(150);
            floodtable.getColumnModel().getColumn(1).setResizable(false);
            floodtable.getColumnModel().getColumn(1).setPreferredWidth(150);
            floodtable.getColumnModel().getColumn(2).setResizable(false);
            floodtable.getColumnModel().getColumn(2).setPreferredWidth(150);
            floodtable.getColumnModel().getColumn(3).setResizable(false);
            floodtable.getColumnModel().getColumn(4).setResizable(false);
            floodtable.getColumnModel().getColumn(4).setPreferredWidth(150);
            floodtable.getColumnModel().getColumn(5).setResizable(false);
            floodtable.getColumnModel().getColumn(5).setPreferredWidth(100);
        }

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 534, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 383, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 525, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 437, Short.MAX_VALUE)
        );

        btnNextYear.setBackground(new java.awt.Color(204, 255, 204));
        btnNextYear.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnNextYear.setText("NEXT");
        btnNextYear.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnNextYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextYearActionPerformed(evt);
            }
        });

        btnPrevYear1.setBackground(new java.awt.Color(204, 255, 204));
        btnPrevYear1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnPrevYear1.setText("BACK");
        btnPrevYear1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnPrevYear1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevYear1ActionPerformed(evt);
            }
        });

        filterZoneCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        filterSeverityCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        filterStreetCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel40.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(255, 180, 75));
        jLabel40.setText("Moderate");

        jLabel41.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(204, 204, 0));
        jLabel41.setText("Low");

        jLabel42.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(225, 75, 75));
        jLabel42.setText("High");

        jLabel44.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(120, 60, 30));
        jLabel44.setText("Critical");

        jLabel49.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel49.setText("= < 1m");

        jLabel50.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel50.setText("= < 3m");

        jLabel51.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel51.setText("= < 5m");

        jLabel52.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel52.setText("= > 5m");

        javax.swing.GroupLayout monitoringLayout = new javax.swing.GroupLayout(monitoring);
        monitoring.setLayout(monitoringLayout);
        monitoringLayout.setHorizontalGroup(
            monitoringLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(monitoringLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(monitoringLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(monitoringLayout.createSequentialGroup()
                        .addGap(99, 99, 99)
                        .addGroup(monitoringLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(filterSeverityCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 848, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, monitoringLayout.createSequentialGroup()
                .addContainerGap(36, Short.MAX_VALUE)
                .addGroup(monitoringLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(monitoringLayout.createSequentialGroup()
                        .addComponent(jLabel41)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel49)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel40)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel50)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel42)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel51)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel44)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel52)
                        .addGap(18, 18, 18)
                        .addComponent(filterZoneCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(filterStreetCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(monitoringLayout.createSequentialGroup()
                        .addGroup(monitoringLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, monitoringLayout.createSequentialGroup()
                                .addComponent(btnPrevYear1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(75, 75, 75)
                                .addComponent(btnNextYear, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(162, 162, 162)))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(15, 15, 15))
        );
        monitoringLayout.setVerticalGroup(
            monitoringLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(monitoringLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(monitoringLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(monitoringLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(monitoringLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(filterSeverityCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(filterZoneCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(filterStreetCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, monitoringLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(monitoringLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel40)
                            .addComponent(jLabel41)
                            .addComponent(jLabel42)
                            .addComponent(jLabel44)
                            .addComponent(jLabel49)
                            .addComponent(jLabel50)
                            .addComponent(jLabel51)
                            .addComponent(jLabel52))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(monitoringLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(monitoringLayout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(monitoringLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnPrevYear1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnNextYear, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(213, Short.MAX_VALUE))
        );

        mainTabbedPane.addTab("tab4", monitoring);

        bckgrnd.add(mainTabbedPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, -43, 1132, 1270));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(bckgrnd, javax.swing.GroupLayout.PREFERRED_SIZE, 1602, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bckgrnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 1121, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1920, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1121, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void contactKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_contactKeyTyped
       char c = evt.getKeyChar();
    
    // Allow Backspace/Delete, but block non-digits or if length is already 11
    if (!Character.isDigit(c) || contact.getText().length() >= 11) {
        if (c != java.awt.event.KeyEvent.VK_BACK_SPACE && c != java.awt.event.KeyEvent.VK_DELETE) {
            evt.consume(); // Cancels the key press
        }
    }

    }//GEN-LAST:event_contactKeyTyped

    private void contactActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contactActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_contactActionPerformed

    private void contactFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_contactFocusLost
        if (contact.getText().isEmpty()) {
            contact.setText("Enter Contact No.");
            contact.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_contactFocusLost

    private void contactFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_contactFocusGained
        if (contact.getText().equals("Enter Contact No.")) {
            contact.setText("");
            contact.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_contactFocusGained

    private void logoutbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutbtnActionPerformed
JOptionPane optionPane = new JOptionPane(
    "Are you sure you want to logout?",
    JOptionPane.WARNING_MESSAGE,
    JOptionPane.YES_NO_OPTION
);

JDialog dialog = optionPane.createDialog(this, "Confirm");

JButton yesButton = null;
JButton noButton = null;

for (java.awt.Component component : optionPane.getComponents()) {
    if (component instanceof JPanel) {
        for (java.awt.Component c : ((JPanel) component).getComponents()) {
            if (c instanceof JButton) {
                JButton button = (JButton) c;

                if (button.getText().equals("Yes")) {
                    yesButton = button;
                } else if (button.getText().equals("No")) {
                    noButton = button;
                }
            }
        }
    }
}

if (yesButton != null) {
    yesButton.setBackground(Color.RED);
    yesButton.setForeground(Color.BLACK);
}

dialog.setVisible(true);

Object selectedValue = optionPane.getValue();

if (selectedValue != null && selectedValue.equals(JOptionPane.YES_OPTION)) {
    login loginform = new login();
    loginform.setVisible(true);
    dispose();
}
        
    }//GEN-LAST:event_logoutbtnActionPerformed

    private void firstnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_firstnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_firstnameActionPerformed

    private void firstnameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_firstnameFocusLost
        if (firstname.getText().isEmpty()) {
            firstname.setText("Enter first name");
            firstname.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_firstnameFocusLost

    private void firstnameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_firstnameFocusGained
       if (firstname.getText().equals("Enter first name")) {
            firstname.setText("");
            firstname.setForeground(Color.BLACK);
        }

    }//GEN-LAST:event_firstnameFocusGained

    private void signups1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signups1ActionPerformed
                                      
    String fN, mN, lN, cO, vI, zO, sT;

    try {
        // Helper string constants or direct checks for your placeholders
        String fNameVal = firstname.getText().trim();
        String lNameVal = lastname.getText().trim();
        String cNumVal = contact.getText().trim();
        String vIdVal = validid.getText().trim();

        // 1. Sequential Field Presence & Placeholder Checks
        if (fNameVal.isEmpty() || fNameVal.equalsIgnoreCase("Enter full name")) {
            JOptionPane.showMessageDialog(this, "First Name is required", "Validation Error", JOptionPane.ERROR_MESSAGE);
            firstname.requestFocus();
            return;
        } 
        
        if (lNameVal.isEmpty() || lNameVal.equalsIgnoreCase("Enter last name")) { // Change "Enter last name" to match whatever placeholder you used for last name
            JOptionPane.showMessageDialog(this, "Last Name is required", "Validation Error", JOptionPane.ERROR_MESSAGE);
            lastname.requestFocus();
            return;
        } 
        
        if (cNumVal.isEmpty() || cNumVal.equalsIgnoreCase("Enter Contact number")) {
            JOptionPane.showMessageDialog(this, "Contact Info is required", "Validation Error", JOptionPane.ERROR_MESSAGE);
            contact.requestFocus();
            return;
        } 
        
        if (vIdVal.isEmpty() || vIdVal.equalsIgnoreCase("Enter Valid ID")) { // Change if you have a placeholder for ID
            JOptionPane.showMessageDialog(this, "Valid ID is required", "Validation Error", JOptionPane.ERROR_MESSAGE);
            validid.requestFocus();
            return;
        } 
        
        if (zonecheck.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Please select a Zone", "Validation Error", JOptionPane.ERROR_MESSAGE);
            zonecheck.requestFocus();
            return;
        } 
        
        if (streetcheck.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Please select a Street", "Validation Error", JOptionPane.ERROR_MESSAGE);
            streetcheck.requestFocus();
            return;
        }

        // Assign validated values to your variables
        fN = fNameVal;
        mN = middlename.getText().trim();
        lN = lNameVal;
        cO = cNumVal;
        vI = vIdVal;
        zO = zonecheck.getSelectedItem().toString().trim();
        sT = streetcheck.getSelectedItem().toString().trim();

        // 2. Validate Contact Number Format (11 digits starting with 09)
        if (!cO.matches("^09\\d{9}$")) {
            JOptionPane.showMessageDialog(this, 
                "Invalid Contact Number!\nMust be an 11-digit mobile number starting with 09 (e.g., 09123456789).", 
                "Format Error", JOptionPane.ERROR_MESSAGE);
            contact.requestFocus();
            return;
        }

        // 3. Validate Valid ID Format (3 CAPITAL letters, hyphen, 5 numbers -> ABC-12345)
        if (!vI.matches("^[A-Z]{3}-\\d{5}$")) {
            JOptionPane.showMessageDialog(this, 
                "Invalid Valid ID format!\nFormat must be exactly 3 CAPITAL letters, a hyphen, and 5 numbers.\nExample: ABC-12345", 
                "Format Error", JOptionPane.ERROR_MESSAGE);
            validid.requestFocus();
            return;
        }

        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/flood_system";
        String user = "root";
        String pass = "";

        Connection con = DriverManager.getConnection(url, user, pass);

        // 4. Check if Valid ID already exists in the database
        String checkIdQuery = "SELECT COUNT(*) FROM tbl_resident WHERE valid_id = ?";
        try (PreparedStatement pstCheck = con.prepareStatement(checkIdQuery)) {
            pstCheck.setString(1, vI);
            ResultSet rsCheck = pstCheck.executeQuery();
            if (rsCheck.next() && rsCheck.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "This Valid ID is already registered! Please enter a unique ID.", "Duplicate ID Error", JOptionPane.ERROR_MESSAGE);
                con.close();
                validid.requestFocus();
                return;
            }
        }

        // 5. Fetch location_id matching selected Zone and Street
        int locId = -1;
        String findLocQuery = "SELECT location_id FROM tbl_location WHERE zone_name = ? AND street_name = ?";
        try (PreparedStatement pstLoc = con.prepareStatement(findLocQuery)) {
            pstLoc.setString(1, zO);
            pstLoc.setString(2, sT);
            ResultSet rsLoc = pstLoc.executeQuery();
            if (rsLoc.next()) {
                locId = rsLoc.getInt("location_id");
            }
        }

        if (locId == -1) {
            JOptionPane.showMessageDialog(this, "Selected Zone and Street combination not found.", "Error", JOptionPane.ERROR_MESSAGE);
            con.close();
            return;
        }

        // 6. Insert Resident Record with 'Pending' Status
        String insertQuery = "INSERT INTO tbl_resident (first_name, middle_name, last_name, contact_number, valid_id, location_id, status) VALUES (?, ?, ?, ?, ?, ?, 'Pending')";
        try (PreparedStatement pst = con.prepareStatement(insertQuery)) {
            pst.setString(1, fN);
            pst.setString(2, mN);
            pst.setString(3, lN);
            pst.setString(4, cO);
            pst.setString(5, vI);
            pst.setInt(6, locId);
            pst.executeUpdate();
        }

        // 7. Reset Inputs
        firstname.setText("");
        middlename.setText("");
        lastname.setText("");
        contact.setText("");
        validid.setText("");
        zonecheck.setSelectedIndex(0);
        streetcheck.setModel(new DefaultComboBoxModel<>(new String[]{"-- Select Street --"}));

        
        ActivityLogger.logAction(loggedInUsername, loggedInRole, "Registered new resident: " + fN + " " + lN);
        JOptionPane.showMessageDialog(this, "Registration submitted successfully! Waiting for Admin approval.", "Success", JOptionPane.INFORMATION_MESSAGE);
        con.close();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error saving resident: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    }


    }//GEN-LAST:event_signups1ActionPerformed
    
    private void dashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dashboardActionPerformed
        mainTabbedPane.setSelectedIndex(0);

    }//GEN-LAST:event_dashboardActionPerformed

    private void ResidentInfosbtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ResidentInfosbtn1ActionPerformed
        mainTabbedPane.setSelectedIndex(2);
        loadResidentDataToTable();
    }//GEN-LAST:event_ResidentInfosbtn1ActionPerformed

    private void floodinfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_floodinfoActionPerformed
      mainTabbedPane.setSelectedIndex(4);
    }//GEN-LAST:event_floodinfoActionPerformed

    private void RecordfloodbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RecordfloodbtnActionPerformed
        mainTabbedPane.setSelectedIndex(3);
        loadFloodDataToTable();
    }//GEN-LAST:event_RecordfloodbtnActionPerformed

    private void nextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextActionPerformed
        if (currentPage < totalPages) {
        currentPage++;
        renderTablePage();
    }

    }//GEN-LAST:event_nextActionPerformed

    private void viewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewActionPerformed
                                      
    int selectedRow = directorytable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, 
            "Please select a resident row from the table first.", 
            "No Selection", 
            JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Direct calculation since table sorting is locked/disabled
    int globalIndex = (currentPage - 1) * rowsPerPage + selectedRow;
    
    if (globalIndex >= 0 && globalIndex < filteredResidentData.size()) {
        Object[] rowData = filteredResidentData.get(globalIndex);
        
        int id = (int) rowData[0];
        String fName = (String) rowData[1];
        String mName = (String) rowData[2];
        String lName = (String) rowData[3];
        String validId = (String) rowData[4];
        String zone = (String) rowData[5];
        String street = (String) rowData[6];
        String contact = (String) rowData[7];
        String status = (String) rowData[8];
        
        ResidentDetailsDialog dialog = new ResidentDetailsDialog(
            this, true, id, fName, mName, lName, contact, zone, street, validId, status, 
            loggedInRole, loggedInUsername, loggedInRole, this
        );
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        
        loadResidentDataToTable();
    }

    }//GEN-LAST:event_viewActionPerformed

    private void zonecheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zonecheckActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_zonecheckActionPerformed

    private void streetcheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_streetcheckActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_streetcheckActionPerformed

    private void submitbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitbtnActionPerformed
   try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/flood_system";
            String user = "root";
            String pass = "";

            Connection con = DriverManager.getConnection(url, user, pass);

            // Validation for Flood Recording form
            if (floodzone.getSelectedIndex() <= 0) {
                JOptionPane.showMessageDialog(this, "Please select a Zone", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (floodstreet.getSelectedIndex() <= 0) {
                JOptionPane.showMessageDialog(this, "Please select a Street", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (sever.getSelectedIndex() <= 0) {
                JOptionPane.showMessageDialog(this, "Please select Severity", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String zoneName = floodzone.getSelectedItem().toString().trim();
            String streetName = floodstreet.getSelectedItem().toString().trim();
            String severity = sever.getSelectedItem().toString().trim();
            String timeVal = floodtime.getText().trim();
    String timeRegex = "^([01][0-9]|2[0-3]):[0-5][0-9]$";
    
   
            
            // Extract date from JCalendar
            java.util.Date selectedDate = flooddate.getDate();
            if (selectedDate == null) {
                JOptionPane.showMessageDialog(this, "Please select a valid Date.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Extract and validate time from JFormattedTextField (floodtime)
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
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
                String datePart = new java.text.SimpleDateFormat("yyyy-MM-dd").format(selectedDate);
                java.util.Date fullSelectedDateTime = sdf.parse(datePart + " " + timeVal);
                java.util.Date currentDateTime = new java.util.Date();

                if (fullSelectedDateTime.after(currentDateTime)) {
                    JOptionPane.showMessageDialog(this, "Cannot record a flood with a future date and time!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid date or time format.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String dateVal = new java.text.SimpleDateFormat("yyyy-MM-dd").format(selectedDate);

            // Find location_id matching zone and street
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
                JOptionPane.showMessageDialog(this, "Location matching zone and street not found in database.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Insert into tbl_flood_record
            String insertFloodQuery = "INSERT INTO tbl_flood_record (location_id, flood_date, flood_time, severity) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstInsert = con.prepareStatement(insertFloodQuery)) {
                pstInsert.setInt(1, locationId);
                pstInsert.setString(2, dateVal);
                pstInsert.setString(3, timeVal);
                pstInsert.setString(4, severity);
                pstInsert.executeUpdate();
            }
            ActivityLogger.logAction(loggedInUsername, loggedInRole, "Recorded new flood at Zone: " + zoneName + ", Street: " + streetName);
            JOptionPane.showMessageDialog(this, "Flood record successfully submitted!");
            con.close();

            // Reset form fields
            floodzone.setSelectedIndex(0);
            floodstreet.setModel(new DefaultComboBoxModel<>(new String[]{"-- Select Street --"}));
            sever.setSelectedIndex(0);
            floodtime.setValue(null); 
            
            // Refresh monitoring table
            loadFloodDataToTable();
            loadDynamicResidentChart();              
            loadDynamicFloodChart(currentChartYear);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error submitting flood record: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_submitbtnActionPerformed

    private void btnNextYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextYearActionPerformed
        if (btnNextYear.isEnabled()) { // Only execute if it's active
    currentChartYear++;
    loadDynamicFloodChart(currentChartYear);
    loadDashboardMetrics();
    updateYearNavigationButtons(); // Recalculate states for both buttons
}
    }//GEN-LAST:event_btnNextYearActionPerformed

    private void btnPrevYear1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevYear1ActionPerformed
      if (btnPrevYear1.isEnabled()) { // Only execute if it's active
    currentChartYear--;
    loadDynamicFloodChart(currentChartYear);
    loadDashboardMetrics();     
    updateYearNavigationButtons(); // Recalculate states for both buttons
}
    }//GEN-LAST:event_btnPrevYear1ActionPerformed

    private void middlenameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_middlenameFocusGained
        if (middlename.getText().equals("Enter middle name")) {
            middlename.setText("");
            middlename.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_middlenameFocusGained

    private void middlenameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_middlenameFocusLost
        if (middlename.getText().isEmpty()) {
            middlename.setText("Enter middle name");
            middlename.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_middlenameFocusLost

    private void middlenameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_middlenameActionPerformed
       
    }//GEN-LAST:event_middlenameActionPerformed

    private void lastnameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_lastnameFocusGained
       if (lastname.getText().equals("Enter last name")) {
            lastname.setText("");
            lastname.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_lastnameFocusGained

    private void lastnameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_lastnameFocusLost
         if (lastname.getText().isEmpty()) {
            lastname.setText("Enter last name");
            lastname.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_lastnameFocusLost

    private void lastnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lastnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lastnameActionPerformed

    private void valididFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_valididFocusGained
       if (validid.getText().equals("Enter valid id")) {
            validid.setText("");
            validid.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_valididFocusGained

    private void valididFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_valididFocusLost
      if (validid.getText().isEmpty()) {
            validid.setText("Enter valid id");
           validid.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_valididFocusLost

    private void valididActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_valididActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_valididActionPerformed

    private void valididKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_valididKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_valididKeyTyped

    private void prevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prevActionPerformed
                                       
    if (currentPage > 1) {
        currentPage--;
        renderTablePage();
    }

    }//GEN-LAST:event_prevActionPerformed

    private void Addresidentbtn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Addresidentbtn2ActionPerformed
         mainTabbedPane.setSelectedIndex(1);
    }//GEN-LAST:event_Addresidentbtn2ActionPerformed

    private void severActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_severActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_severActionPerformed

    private void searchFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchFieldActionPerformed

    private void searchFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchFieldFocusGained
        if (searchField.getText().equals("Search resident...")) {
            searchField.setText("");
            searchField.setForeground(java.awt.Color.BLACK);
        }
    }//GEN-LAST:event_searchFieldFocusGained

    private void searchFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchFieldFocusLost
     
        if (searchField.getText().trim().isEmpty()) {
            searchField.setText("Search resident...");
            searchField.setForeground(java.awt.Color.GRAY);
        }
    }//GEN-LAST:event_searchFieldFocusLost

    private void floodtimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_floodtimeActionPerformed
       
    java.awt.Color white = java.awt.Color.WHITE;
    java.awt.Color textColor = new java.awt.Color(40, 40, 40);
    java.awt.Font uiFont = new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14);

    floodtime.setBackground(white);
    floodtime.setForeground(textColor);
    floodtime.setFont(uiFont);

    floodtime.setBorder(javax.swing.BorderFactory.createCompoundBorder(
        javax.swing.BorderFactory.createLineBorder(java.awt.Color.BLACK, 1),
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
            
            // Allow empty or incomplete fields during typing
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

    }//GEN-LAST:event_floodtimeActionPerformed
 private void setupFilterZoneCombo() {
    DefaultComboBoxModel<String> zoneModel = new DefaultComboBoxModel<>();
    zoneModel.addElement("Select Zone");
    zoneModel.addElement("All");

    String url = "jdbc:mysql://localhost:3306/flood_system";
    String query = "SELECT DISTINCT zone_name FROM tbl_location ORDER BY CAST(SUBSTRING_INDEX(zone_name, ' ', -1) AS UNSIGNED) ASC";

    try (Connection con = DriverManager.getConnection(url, "root", "");
         Statement st = con.createStatement();
         ResultSet rs = st.executeQuery(query)) {

        while (rs.next()) {
            zoneModel.addElement(rs.getString("zone_name"));
        }
        filterZoneCombo.setModel(zoneModel);

    } catch (Exception e) {
        System.out.println("Error loading filter zones: " + e.getMessage());
    }

    Font customFont = new Font("Segoe UI", Font.PLAIN, 14);
    filterZoneCombo.setFont(customFont);
    filterZoneCombo.setBackground(Color.WHITE);
    filterZoneCombo.setForeground(new Color(33, 33, 33));
    filterZoneCombo.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(120, 180, 120), 1, true));
    filterZoneCombo.setMaximumRowCount(5);
    filterZoneCombo.setPrototypeDisplayValue("------------------------");
    
    // ADD THIS RENDERER SO IT MATCHES STREET AND SEVERITY:
    filterZoneCombo.setRenderer(new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 12, 6, 12));
            if (value != null && (value.toString().startsWith("Select") || value.toString().equalsIgnoreCase("All"))) {
                label.setForeground(new Color(100, 100, 100)); // Muted gray
                label.setFont(customFont.deriveFont(Font.PLAIN));
                label.setBackground(isSelected ? new Color(204, 255, 204) : Color.WHITE);
            } else {
                if (isSelected) {
                    label.setBackground(new Color(204, 255, 204));
                    label.setForeground(new Color(20, 60, 20));
                    label.setFont(customFont.deriveFont(Font.BOLD));
                } else {
                    label.setBackground(Color.WHITE);
                    label.setForeground(new Color(33, 33, 33));
                    label.setFont(customFont.deriveFont(Font.PLAIN));
                }
            }
            return label;
        }
    });
    
    filterZoneCombo.addActionListener(e -> {
        updateFilterStreets();
        applyFloodFilters();
    });
}
 
private void setupFilterStreetCombo() {
    filterStreetCombo.setModel(new DefaultComboBoxModel<>(new String[]{"Select Street", "All"}));

    Font customFont = new Font("Segoe UI", Font.PLAIN, 14);
    filterStreetCombo.setFont(customFont);
    filterStreetCombo.setBackground(Color.WHITE);
    filterStreetCombo.setForeground(new Color(33, 33, 33));
    filterStreetCombo.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(120, 180, 120), 1, true));
    filterStreetCombo.setMaximumRowCount(5);
    filterStreetCombo.setPrototypeDisplayValue("------------------------");
    
    filterStreetCombo.setRenderer(new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 12, 6, 12));
            if (value != null && (value.toString().startsWith("Select") || value.toString().equalsIgnoreCase("All"))) {
                label.setForeground(new Color(100, 100, 100)); // Darker gray for better visibility
                label.setFont(customFont.deriveFont(Font.PLAIN));
                label.setBackground(isSelected ? new Color(204, 255, 204) : Color.WHITE);
            } else {
                if (isSelected) {
                    label.setBackground(new Color(204, 255, 204));
                    label.setForeground(new Color(20, 60, 20));
                    label.setFont(customFont.deriveFont(Font.BOLD));
                } else {
                    label.setBackground(Color.WHITE);
                    label.setForeground(new Color(33, 33, 33));
                    label.setFont(customFont.deriveFont(Font.PLAIN));
                }
            }
            return label;
        }
    });

    filterStreetCombo.addActionListener(e -> applyFloodFilters());
}

private void updateFilterStreets() {
    if (filterStreetCombo == null || filterZoneCombo == null) return;

    if (filterZoneCombo.getSelectedIndex() <= 0 || "All".equals(filterZoneCombo.getSelectedItem())) {
        filterStreetCombo.setModel(new DefaultComboBoxModel<>(new String[]{"Select Street", "All"}));
        return;
    }

    String selectedZone = (String) filterZoneCombo.getSelectedItem();
    DefaultComboBoxModel<String> streetModel = new DefaultComboBoxModel<>();
    streetModel.addElement("Select Street");
    streetModel.addElement("All");

    String url = "jdbc:mysql://localhost:3306/flood_system";
    String query = "SELECT street_name FROM tbl_location WHERE zone_name = ? ORDER BY street_name ASC";

    try (Connection con = DriverManager.getConnection(url, "root", "");
         PreparedStatement pst = con.prepareStatement(query)) {

        pst.setString(1, selectedZone);
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            streetModel.addElement(rs.getString("street_name"));
        }

        filterStreetCombo.setModel(streetModel);
        filterStreetCombo.setSelectedIndex(0);

    } catch (Exception e) {
        System.out.println("Error loading filter streets: " + e.getMessage());
    }
}
private void setupFilterSeverityCombo() {
    filterSeverityCombo.setModel(new DefaultComboBoxModel<>(new String[]{
        "Select Severity", 
        "All", 
        "Low", 
        "Moderate", 
        "High", 
        "Critical"
    }));

    Font customFont = new Font("Segoe UI", Font.PLAIN, 14);
    filterSeverityCombo.setFont(customFont);
    filterSeverityCombo.setBackground(Color.WHITE);
    filterSeverityCombo.setForeground(new Color(33, 33, 33));
    filterSeverityCombo.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(120, 180, 120), 1, true));
    filterSeverityCombo.setMaximumRowCount(5);
    filterSeverityCombo.setPrototypeDisplayValue("------------------------");
    
    filterSeverityCombo.setRenderer(new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 12, 6, 12));
            if (value != null && (value.toString().startsWith("Select") || value.toString().equalsIgnoreCase("All"))) {
                label.setForeground(new Color(100, 100, 100));
                label.setFont(customFont.deriveFont(Font.PLAIN));
                label.setBackground(isSelected ? new Color(204, 255, 204) : Color.WHITE);
            } else {
                if (isSelected) {
                    label.setBackground(new Color(204, 255, 204));
                    label.setForeground(new Color(20, 60, 20));
                    label.setFont(customFont.deriveFont(Font.BOLD));
                } else {
                    label.setBackground(Color.WHITE);
                    label.setForeground(new Color(33, 33, 33));
                    label.setFont(customFont.deriveFont(Font.PLAIN));
                }
            }
            return label;
        }
    });

    filterSeverityCombo.addActionListener(e -> applyFloodFilters());
}
    
   private void applyFloodFilters() {
    if (floodSorter == null) return;
    
    java.util.List<javax.swing.RowFilter<Object, Object>> filters = new java.util.ArrayList<>();
    
    // Zone Filter -> Column index 3
    String selectedZone = (String) filterZoneCombo.getSelectedItem();
    if (selectedZone != null && !selectedZone.startsWith("Select") && !selectedZone.equalsIgnoreCase("All")) {
        filters.add(javax.swing.RowFilter.regexFilter("(?i)^" + java.util.regex.Pattern.quote(selectedZone) + "$", 3));
    }
    
    // Street Filter -> Column index 4
    String selectedStreet = (String) filterStreetCombo.getSelectedItem();
    if (selectedStreet != null && !selectedStreet.startsWith("Select") && !selectedStreet.equalsIgnoreCase("All")) {
        filters.add(javax.swing.RowFilter.regexFilter("(?i)^" + java.util.regex.Pattern.quote(selectedStreet) + "$", 4));
    }
    
    // Severity Filter -> Column index 5
    String selectedSev = (String) filterSeverityCombo.getSelectedItem();
    if (selectedSev != null && !selectedSev.startsWith("Select") && !selectedSev.equalsIgnoreCase("All")) {
        filters.add(javax.swing.RowFilter.regexFilter("(?i)^" + java.util.regex.Pattern.quote(selectedSev) + "$", 5));
    }
    
    if (filters.isEmpty()) {
        floodSorter.setRowFilter(null);
    } else {
        floodSorter.setRowFilter(javax.swing.RowFilter.andFilter(filters));
    }
}
    
    private void updateYearNavigationButtons() {
    String url = "jdbc:mysql://localhost:3306/flood_system";
    int maxYear = 2026; // Default fallback
    int minYear = 2000; // Set your earliest possible year here (or fetch MIN(YEAR(...)) from DB)

    // Fetch max year from database safely
    try (java.sql.Connection con = java.sql.DriverManager.getConnection(url, "root", "");
         java.sql.Statement st = con.createStatement();
         java.sql.ResultSet rs = st.executeQuery("SELECT MAX(YEAR(flood_date)) AS max_y, MIN(YEAR(flood_date)) AS min_y FROM tbl_flood_record")) {
        if (rs.next()) {
            if (rs.getInt("max_y") > 0) maxYear = rs.getInt("max_y");
            if (rs.getInt("min_y") > 0) minYear = rs.getInt("min_y");
        }
    } catch (Exception e) {
        System.out.println("Error fetching year bounds: " + e.getMessage());
    }

    // Enable/Disable Next Year button (can't go past the latest year with data)
    if (btnNextYear != null) {
        btnNextYear.setEnabled(currentChartYear < maxYear);
    }

    // Enable/Disable Previous Year button (can't go past the earliest year with data)
    if (btnPrevYear1 != null) {
        btnPrevYear1.setEnabled(currentChartYear > minYear);
    }
}
    
    private void applyFilter() {
    String query = searchField.getText().trim();
    filteredResidentData.clear();

    if (query.isEmpty() || query.equals("Search resident...")) {
        filteredResidentData.addAll(allResidentData);
    } else {
        String lowerQuery = query.toLowerCase();
        for (Object[] row : allResidentData) {
            boolean match = false;
            for (Object col : row) {
                if (col != null && col.toString().toLowerCase().contains(lowerQuery)) {
                    match = true;
                    break;
                }
            }
            if (match) {
                filteredResidentData.add(row);
            }
        }
    }

    currentPage = 1; // Reset to first page on new search query
    renderTablePage();
}
    
    private void switchTab(int newTab) {
        previousTab = mainTabbedPane.getSelectedIndex();
        mainTabbedPane.setSelectedIndex(newTab);
    }
    
    private void showArrow(int arrowNumber) {
        
        arrow2.setVisible(arrowNumber == 1);
        arrow3.setVisible(arrowNumber == 2);
        arrow4.setVisible(arrowNumber == 3);
        arrow5.setVisible(arrowNumber == 4);
        arrow6.setVisible(arrowNumber == 5);
    }

    private int currentPage = 1;
    private int rowsPerPage = 8; 
    private int totalPages = 1;
   

    public void loadResidentDataToTable() {
    allResidentData.clear();

    String url = "jdbc:mysql://localhost:3306/flood_system";
    String user = "root";
    String pass = "";

    // Added r.resident_id to the SELECT query
    String query = "SELECT r.resident_id, r.first_name, r.middle_name, r.last_name, r.valid_id, l.zone_name, l.street_name, r.contact_number, r.status " +
                   "FROM tbl_resident r " +
                   "LEFT JOIN tbl_location l ON r.location_id = l.location_id";

    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, user, pass);
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            allResidentData.add(new Object[]{
                rs.getInt("resident_id"), // Index 0: ID
                rs.getString("first_name"), // Index 1
                rs.getString("middle_name") != null ? rs.getString("middle_name") : "", // Index 2
                rs.getString("last_name"), // Index 3
                rs.getString("valid_id"), // Index 4
                rs.getString("zone_name") != null ? rs.getString("zone_name") : "N/A", // Index 5
                rs.getString("street_name") != null ? rs.getString("street_name") : "N/A", // Index 6
                rs.getString("contact_number"), // Index 7
                rs.getString("status") != null ? rs.getString("status") : "Pending" // Index 8
            });
        }

        con.close();

        filteredResidentData = new java.util.ArrayList<>(allResidentData);
        currentPage = 1;
        renderTablePage();

    } catch (Exception e) {
        System.out.println("Error loading table with location JOIN: " + e.getMessage());
    }
}

    public void loadFloodDataToTable() {
    javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) floodtable.getModel();
    model.setRowCount(0);

    String url = "jdbc:mysql://localhost:3306/flood_system";
    
    String query = "SELECT f.flood_id, f.flood_date, f.flood_time, f.severity, l.zone_name, l.street_name " +
                   "FROM tbl_flood_record f " +
                   "LEFT JOIN tbl_location l ON f.location_id = l.location_id " +
                   "ORDER BY f.flood_id DESC";

    try {
        java.sql.Connection con = java.sql.DriverManager.getConnection(url, "root", "");
        java.sql.Statement st = con.createStatement();
        java.sql.ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("flood_id"),
                rs.getString("flood_date"),
                rs.getString("flood_time"),
                rs.getString("zone_name") != null ? rs.getString("zone_name") : "N/A",
                rs.getString("street_name") != null ? rs.getString("street_name") : "N/A",
                rs.getString("severity")
            });
        }
        con.close();

        floodtable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        if (floodtable.getColumnModel().getColumnCount() >= 6) {
            floodtable.getColumnModel().getColumn(0).setPreferredWidth(80);
            floodtable.getColumnModel().getColumn(1).setPreferredWidth(120);
            floodtable.getColumnModel().getColumn(2).setPreferredWidth(120);
            floodtable.getColumnModel().getColumn(3).setPreferredWidth(100);
            floodtable.getColumnModel().getColumn(4).setPreferredWidth(150);
            floodtable.getColumnModel().getColumn(5).setPreferredWidth(90);
        
        }

        styleTableHeader(floodtable);
        styleFloodTableRenderer(floodtable); 

        // Initialize sorter and re-apply filters so they don't get wiped out
        floodSorter = new javax.swing.table.TableRowSorter<>(floodtable.getModel());
        floodtable.setRowSorter(floodSorter);
        applyFloodFilters(); 

    } catch (Exception e) {
        System.out.println("Error loading flood monitoring table: " + e.getMessage());
    }
}
    private void styleFloodTableRenderer(JTable table) {
    table.setRowHeight(48);
    table.setShowGrid(false);

    DefaultTableCellRenderer severityRenderer = new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            c.setFont(new Font("Segoe UI", Font.PLAIN, 17));
            if (c instanceof JLabel) {
                ((JLabel) c).setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 0, 15));
            }

            if (isSelected) {
                // When selected, highlight the entire row uniformly so nothing looks "stuck"
                c.setBackground(new Color(180, 220, 180));
                c.setForeground(Color.BLACK);
            } else {
                // Normal state: Apply severity colors to column 3, zebra striping to others
                if (column == 5 && value != null) {
                    String sev = value.toString().trim();
                    switch (sev.toLowerCase()) {
                        case "low":
                            c.setBackground(new Color(255, 255, 153)); // Light Yellow
                            c.setForeground(new Color(50, 50, 50));     // Dark readable text
                            break;
                        case "moderate":
                            c.setBackground(new Color(255, 180, 75));  // Orange
                            c.setForeground(Color.BLACK);               // Dark readable text
                            break;
                        case "high":
                            c.setBackground(new Color(225, 75, 75));   // Red
                            c.setForeground(Color.WHITE);               // White readable text
                            break;
                        case "critical":
                            c.setBackground(new Color(120, 60, 30));   // Brown
                            c.setForeground(Color.WHITE);               // White readable text
                            break;
                        default:
                            c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 248, 240));
                            c.setForeground(Color.BLACK);
                            break;
                    }
                } else {
                    // Standard zebra striping for other columns
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(240, 248, 240));
                    }
                    c.setForeground(Color.BLACK);
                }
            }
            return c;
        }
    };

    for (int i = 0; i < table.getColumnCount(); i++) {
        table.getColumnModel().getColumn(i).setCellRenderer(severityRenderer);
    }
}

    // -------------------------------------------------------------
    // HORIZONTAL SCROLLING & COLUMN SIZING SETUP
    // -------------------------------------------------------------
private void setupTableConfiguration() {
    if (jScrollPane1 != null) {
        // Hides the visual scrollbar
        jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        // ADD THIS LINE to completely block mouse wheel scrolling:
        jScrollPane1.setWheelScrollingEnabled(false);
    }

    directorytable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    directorytable.getTableHeader().setReorderingAllowed(false); // Locks column dragging

    styleTableHeader(directorytable);
    styleResidentTableRenderer(directorytable); 
}

    
private void renderTablePage() {
    DefaultTableModel model = (DefaultTableModel) directorytable.getModel();
    model.setRowCount(0); // Clear current rows

    int totalRows = filteredResidentData.size(); 
    totalPages = (int) Math.ceil((double) totalRows / rowsPerPage);
    if (totalPages == 0) totalPages = 1;

    if (currentPage > totalPages) currentPage = totalPages;
    if (currentPage < 1) currentPage = 1;

    int startIndex = (currentPage - 1) * rowsPerPage;
    int endIndex = Math.min(startIndex + rowsPerPage, totalRows);

    for (int i = startIndex; i < endIndex; i++) {
        Object[] rowData = filteredResidentData.get(i);
        // Map indices 1 through 8 to the visible table columns
        model.addRow(new Object[]{
            rowData[1], // First Name
            rowData[2], // M.I
            rowData[3], // Last Name
            rowData[4], // Id
            rowData[5], // Zone
            rowData[6], // Street
            rowData[7], // Contact No
            rowData[8]  // Status
        });
    }

    if (pages != null) {
        pages.setText("Page " + currentPage + " of " + totalPages);
    }
    
    // ADD THIS: Disables the BACK button when on the first page
    if (prev != null) {
        prev.setEnabled(currentPage > 1);
    }
    
    if (next != null) {
        next.setEnabled(currentPage < totalPages);
    }
}

    // -------------------------------------------------------------
    // TABLE HEADER STYLING (DARK GREEN HEADER)
    // -------------------------------------------------------------
    private void styleTableHeader(JTable table) {
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setPreferredSize(new java.awt.Dimension(0, 50));

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                
                label.setBackground(new Color(11, 102, 35));
                label.setForeground(Color.WHITE);
                label.setFont(new Font("Segoe UI", Font.BOLD, 18));
                label.setHorizontalAlignment(JLabel.LEFT);
                label.setVerticalAlignment(JLabel.CENTER);
                label.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 0, 15));
                label.setOpaque(true);
                return label;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
    }

    // -------------------------------------------------------------
    // TABLE ROW STYLING (ZEBRA STRIPING + LARGER DATA TEXT)
    // -------------------------------------------------------------
    private void styleResidentTableRenderer(JTable table) {
    table.setRowHeight(48);
    table.setShowGrid(false);

    DefaultTableCellRenderer residentRenderer = new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            c.setFont(new Font("Segoe UI", Font.PLAIN, 17));
            if (c instanceof JLabel) {
                ((JLabel) c).setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 0, 15));
            }

            if (isSelected) {
                // Uniform selection highlight when clicked
                c.setBackground(new Color(180, 220, 180));
                c.setForeground(Color.BLACK);
            } else {
                // Column index 7 is Status
                if (column == 7 && value != null) {
                    String status = value.toString().trim();
                    if (status.equalsIgnoreCase("Approved")) {
                        c.setBackground(new Color(200, 255, 200)); // Soft Green
                        c.setForeground(new Color(20, 80, 20));     // Dark green text
                    } else if (status.equalsIgnoreCase("Pending")) {
                        c.setBackground(new Color(255, 255, 153)); // Soft Yellow
                        c.setForeground(new Color(80, 60, 0));      // Dark orange/brown text
                    } else {
                        c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 248, 240));
                        c.setForeground(Color.BLACK);
                    }
                } else {
                    // Standard zebra striping for other columns
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(240, 248, 240));
                    }
                    c.setForeground(Color.BLACK);
                }
            }
            return c;
        }
    };

    for (int i = 0; i < table.getColumnCount(); i++) {
        table.getColumnModel().getColumn(i).setCellRenderer(residentRenderer);
    }
}

  public static void main(String args[]) {
    try {
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
    } catch (Exception ex) {
        java.util.logging.Logger.getLogger(Staffdashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }

    java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
            // Pass dummy test credentials for previewing
            new Staffdashboard("testStaffUser", "Staff").setVisible(true);
        }
    });
}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Addresidentbtn2;
    private javax.swing.JButton Recordfloodbtn;
    private javax.swing.JButton ResidentInfosbtn1;
    private javax.swing.JPanel addresidentpanel;
    private javax.swing.JLabel arrow2;
    private javax.swing.JLabel arrow3;
    private javax.swing.JLabel arrow4;
    private javax.swing.JLabel arrow5;
    private javax.swing.JLabel arrow6;
    private javax.swing.JPanel bckgrnd;
    private javax.swing.JButton btnNextYear;
    private javax.swing.JButton btnPrevYear1;
    private javax.swing.JTextField contact;
    private javax.swing.JLabel criticallbl;
    private javax.swing.ButtonGroup customtime;
    private javax.swing.JButton dashboard;
    private javax.swing.JLabel dashpng;
    private javax.swing.JTable directorytable;
    private javax.swing.JComboBox<String> filterSeverityCombo;
    private javax.swing.JComboBox<String> filterStreetCombo;
    private javax.swing.JComboBox<String> filterZoneCombo;
    private javax.swing.JTextField firstname;
    private com.toedter.calendar.JDateChooser flooddate;
    private javax.swing.JButton floodinfo;
    private javax.swing.JComboBox<String> floodstreet;
    private javax.swing.JTable floodtable;
    private javax.swing.JFormattedTextField floodtime;
    private javax.swing.JComboBox<String> floodzone;
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
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField lastname;
    private javax.swing.JLabel lblWelcome;
    private javax.swing.JLabel locationlbl;
    private javax.swing.JButton logoutbtn;
    private javax.swing.JLabel logoutexit;
    private javax.swing.JTabbedPane mainTabbedPane;
    private javax.swing.JTextField middlename;
    private javax.swing.JLabel monitor;
    private javax.swing.JPanel monitoring;
    private javax.swing.JButton next;
    private javax.swing.JLabel pages;
    private javax.swing.JPanel paneldashboard;
    private javax.swing.JLabel pendinglbl;
    private javax.swing.ButtonGroup present;
    private javax.swing.JButton prev;
    private javax.swing.JRadioButton radioAuto;
    private javax.swing.JRadioButton radioCustom;
    private javax.swing.JPanel recording;
    private javax.swing.JLabel reportlbl;
    private javax.swing.JPanel residentdirectory;
    private javax.swing.JTextField searchField;
    private javax.swing.JComboBox<String> sever;
    private javax.swing.JButton signups1;
    private javax.swing.JPanel staffdashbaord;
    private javax.swing.JComboBox<String> streetcheck;
    private javax.swing.JButton submitbtn;
    private javax.swing.JTextField validid;
    private javax.swing.JButton view;
    private javax.swing.JComboBox<String> zonecheck;
    // End of variables declaration//GEN-END:variables
    }

