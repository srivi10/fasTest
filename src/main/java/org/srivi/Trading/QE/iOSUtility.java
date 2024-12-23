package org.srivi.Trading.QE;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import org.srivi.Trading.AccountSelectionGUI;
import org.srivi.Trading.QE.XcrunHelper;

public class iOSUtility extends JFrame {

    private JFrame mainAppFrame;
    private JTextField fileNameField;
    private JTextField fileLocationField;
    private JLabel savedLocationLabel;
    private JLabel deviceStatusLabel;
    private HelpOptionsPanel helpOptionsPanel;
    private JButton screenshotButton;
    private JButton screenRecordingButton;
    private JButton enButton;
    private JButton frButton;
    private List<String> devices;

    String desktopPath = System.getProperty("user.home") + "/Desktop";

    public iOSUtility() {
        setTitle("iOS Utility");
        setSize(400, 370);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        // Set the custom font
        Font interFont = FontUtil.getInterFont(12f);



        ImageIcon backIcon = new ImageIcon(getClass().getClassLoader().getResource("icons/MasterBackButton.png"));
        Image scaledBackImage = backIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledBackImage);

        // Create the back label with icon and text
        JLabel backLabel = new JLabel("Back", scaledIcon, SwingConstants.LEFT);
        backLabel.setBounds(10, 10, 100, 30); // Adjust width to fit text and icon
        backLabel.setFont(FontUtil.getInterFont(14f)); // Set font
        backLabel.setIconTextGap(5); // Gap between icon and text
        backLabel.setHorizontalAlignment(SwingConstants.LEFT); // Align text to the left of the icon
        backLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Change cursor to hand on hover

        // Add mouse listener to handle clicks
        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                goBackToMainApp();
            }
        });

        // Add label to the frame
        add(backLabel);
//        JLabel backLabel = BackButtonUtil.createBackLabel(this);
//        add(backLabel);

        // Warning Icon
        ImageIcon warningIcon = new ImageIcon(getClass().getClassLoader().getResource("icons/iOSUtilityWarning.png"));
        Image scaledWarningIcon = warningIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);

        // Warning Label
        JLabel warningLabel = new JLabel("Supported Device Type: Simulator", new ImageIcon(scaledWarningIcon), JLabel.LEFT);
        warningLabel.setToolTipText("Physical Device is not supported");
        warningLabel.setBounds(170, 20, 300, 20);
        warningLabel.setFont(interFont);
        add(warningLabel);

        // Label for device connection status
        deviceStatusLabel = new JLabel("Device Connected: Checking...");
        deviceStatusLabel.setBounds(50, 60, 300, 30);
        deviceStatusLabel.setFont(interFont);
        add(deviceStatusLabel);

        // Button for Take Screenshot option
        screenshotButton = new JButton("Take Screenshot");
        screenshotButton.setBounds(50, 100, 130, 30);
        screenshotButton.setFont(interFont);
        screenshotButton.setEnabled(true); // Disable for now, enable when the function is implemented
        add(screenshotButton);

        // Load the Screenshot Success Icon
        ImageIcon screenshotIcon = new ImageIcon(getClass().getClassLoader().getResource("icons/Screencap.png"));
        Image scaledImage = screenshotIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);

        // Create a new ImageIcon from the scaled image
        ImageIcon smoothIcon = new ImageIcon(scaledImage);

        // Create a JLabel with the smoothed icon
        JLabel iconLabel = new JLabel(smoothIcon);
        iconLabel.setBounds(180, 100, 30, 30);
        iconLabel.setVisible(false); // Initially invisible
        add(iconLabel);

        // Add ActionListener to the Screenshot Button
        screenshotButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iconLabel.setVisible(true);
                boolean success = XcrunHelper.takeScreenshot();
                if (success) {
                    // Show the icon
                   // iconLabel.setVisible(true);

                    // Create a Timer to hide the icon after 2 seconds
                    Timer timer = new Timer(500, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            iconLabel.setVisible(false);
                        }
                    });
                    timer.setRepeats(false); // Make sure the timer only runs once
                    timer.start();
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to take screenshot", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Button for Screen Recording option
        screenRecordingButton = new JButton("Screen Recording");
        screenRecordingButton.setBounds(50, 140, 130, 30);
        screenRecordingButton.setFont(interFont);
        screenRecordingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openiOSScreenRecordingView();
            }
        });
        add(screenRecordingButton);

        // Button for Account Finder option
        JButton accountFinderButton = new JButton("Account Finder");
        accountFinderButton.setBounds(50, 180, 130, 30);
        accountFinderButton.setFont(interFont);
        accountFinderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAccountSelectionGUI();
            }
        });
        add(accountFinderButton);
// --- Language Selection Buttons ---
        JLabel languageLabel = new JLabel("Language");
        languageLabel.setFont(interFont);
        languageLabel.setBounds(60, 220, 200, 30);
        add(languageLabel);

        ImageIcon cautionIcon = new ImageIcon(getClass().getClassLoader().getResource("icons/Tips.png"));
        Image scaledCautionImage = cautionIcon.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH);
        ImageIcon scaledCautionIcon = new ImageIcon(scaledCautionImage);

        JLabel cautionIconLabel = new JLabel(scaledCautionIcon);
        cautionIconLabel.setToolTipText("Set Language and Region");
        cautionIconLabel.setBounds(110, 210, 35, 35);
        add(cautionIconLabel);

// EN Button
        ImageIcon enIcon = new ImageIcon(getClass().getClassLoader().getResource("icons/USLocale.png"));
        Image scaledEnImage = enIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        enButton = new JButton(new ImageIcon(scaledEnImage));
        enButton.setToolTipText("Set Language to English");
        enButton.setBounds(160, 217, 35, 40);
        enButton.setBorderPainted(false);
        enButton.setContentAreaFilled(false);
        enButton.setFocusPainted(false);
        enButton.setOpaque(false);
        add(enButton);

// FR Button
        ImageIcon frIcon = new ImageIcon(getClass().getClassLoader().getResource("icons/FR_Locale.png"));
        Image scaledFrImage = frIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        frButton = new JButton(new ImageIcon(scaledFrImage));
        frButton.setToolTipText("Set Language to French");
        frButton.setBounds(200, 217, 35, 40);
        frButton.setBorderPainted(false);
        frButton.setContentAreaFilled(false);
        frButton.setFocusPainted(false);
        frButton.setOpaque(false);
        add(frButton);

// Action Listeners for Language Buttons
        enButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Placeholder: Call ADBHelper to set language to EN
                XcrunHelper.setLanguageToEnglish(devices);
                JOptionPane.showMessageDialog(null, "Language set to English and Region Canada!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        frButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Placeholder: Call ADBHelper to set language to FR
                XcrunHelper.setLanguageToFrench(devices);
                JOptionPane.showMessageDialog(null, "Language set to French and Region Canada!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        updateDeviceStatus();

        setVisible(true);
        helpOptionsPanel = new HelpOptionsPanel(getWidth(), getHeight());
        add(helpOptionsPanel);

        // Add ComponentListener to handle resizing
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Reposition HelpOptionsPanel
                helpOptionsPanel.setBounds(0, getHeight() - 100, getWidth(), 100);
            }
        });
    }

    // Method to update the device connection status
    public void updateDeviceStatus() {
        devices = XcrunHelper.getConnectedDevices();
        String statusText = devices.isEmpty() ? "No devices connected." : "Connected Devices: " + String.join(", ", devices);
        deviceStatusLabel.setText(statusText);
        boolean devicesConnected = !devices.isEmpty();
        // Enable or disable buttons based on device status
        screenshotButton.setEnabled(devicesConnected);
        screenRecordingButton.setEnabled(devicesConnected);
        enButton.setEnabled(devicesConnected);
        frButton.setEnabled(devicesConnected);
    }


    // Method to open Account Selection GUI
    private void openAccountSelectionGUI() {
        SwingUtilities.invokeLater(() -> new AccountSelectionGUI(this));
    }

    private void openiOSScreenRecordingView() {
        SwingUtilities.invokeLater(() -> new iOSScreenRecordingView(this));
    }
    // Method to set the main app frame
    public void setMainAppFrame(JFrame mainAppFrame) {
        this.mainAppFrame = mainAppFrame;
    }

    // Method to return to main app
    private void goBackToMainApp() {
        mainAppFrame.setVisible(true);
        dispose();
    }
}
