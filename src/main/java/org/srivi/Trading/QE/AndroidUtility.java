package org.srivi.Trading.QE;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;
import org.srivi.Trading.AccountSelectionGUI;

public class AndroidUtility extends JFrame {

    private JFrame mainAppFrame;
    private JTextField fileNameField;
    private JTextField fileLocationField;
    public JLabel savedLocationLabel;
    private JLabel deviceStatusLabel;
    private JLabel scanningIconLabel;
    private JLabel scanningLabel;
    String desktopPath = System.getProperty("user.home") + "/Desktop";
    private HelpOptionsPanel helpOptionsPanel;
        public AndroidUtility() {
        setTitle("Android Utility");
        setSize(400, 400);
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

            // Label for device connection status
        deviceStatusLabel = new JLabel("Device Connected: Checking...");
        deviceStatusLabel.setFont(interFont);
        deviceStatusLabel.setBounds(50, 50, 300, 30);
        add(deviceStatusLabel);

        // Button for Take Screenshot option
        JButton screenshotButton = new JButton("Take Screenshot");
        screenshotButton.setFont(interFont);
        screenshotButton.setBounds(50, 90, 130, 30);
        screenshotButton.setEnabled(true); // Disable for now, enable when the function is implemented
        add(screenshotButton);
        // Load the Screenshot Success Icon

        ImageIcon screenshotIcon = new ImageIcon(getClass().getClassLoader().getResource("icons/Screencap.png"));
        Image scaledImage = screenshotIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);

// Create a new ImageIcon from the scaled image
        ImageIcon smoothIcon = new ImageIcon(scaledImage);

// Create a JLabel with the smoothed icon
        JLabel iconLabel = new JLabel(smoothIcon);
        iconLabel.setBounds(180, 90, 30, 30);
        iconLabel.setVisible(false); // Initially invisible
        add(iconLabel);

// Add ActionListener to the Screenshot Button
        screenshotButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean success = ADBHelper.takeScreenshot();
                if (success) {
                    // Show the icon
                    iconLabel.setVisible(true);

                    // Create a Timer to hide the icon after 2 seconds
                    Timer timer = new Timer(2000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            iconLabel.setVisible(false);
                        }

                    });
                    timer.setRepeats(false); // Make sure the timer only runs once
                    timer.start();
                    ImageIcon customIcon = new ImageIcon(AndroidUtility.class.getResource("/icons/Screencap.png"));

                    JOptionPane.showMessageDialog( null,
                            "Screenshot saved to Desktop",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE,
                            customIcon);

                } else {
                    JOptionPane.showMessageDialog(null, "Failed to take screenshot", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });



        // Button for Screen Recording option
        JButton screenRecordingButton = new JButton("Screen Recording");
        screenRecordingButton.setFont(interFont);
        screenRecordingButton.setBounds(50, 130, 130, 30);
        screenRecordingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openScreenRecordingGUI();
            }
        });
        add(screenRecordingButton);

        // Button for Account Finder option
        JButton accountFinderButton = new JButton("Account Finder");
        accountFinderButton.setBounds(50, 170, 130, 30);
        accountFinderButton.setFont(interFont);
        accountFinderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAccountSelectionGUI();
            }
        });
        add(accountFinderButton);
            scanningIconLabel = new JLabel();
            ImageIcon scanningIcon = new ImageIcon(getClass().getResource("/icons/Analyze.png"));
            scanningIconLabel.setIcon(scanningIcon);
            scanningIconLabel.setVisible(false);

            scanningLabel = new JLabel("Scanning Logs");
            scanningLabel.setForeground(new Color(0, 122, 255));
            scanningLabel.setVisible(false);
            // Button for Crash Scan option
            JButton crashScanButton = new JButton("Crash Scan");

            crashScanButton.setBounds(50, 210, 130, 30);
            add(scanningIconLabel).setBounds(250, 185, 45, 40); // Position it above the loadingLabel
            add(scanningLabel).setBounds(225, 220, 100, 30);
            crashScanButton.setFont(interFont);
            crashScanButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Show the icon and text
                    scanningIconLabel.setVisible(true);
                    scanningLabel.setVisible(true);
                    crashScanButton.setEnabled(false);

                    // Perform the crash scan
                    Timer timer = new Timer(500, evt -> {
                        try {
                            ADBHelper.crashScan();
                        } catch (InterruptedException | IOException ex) {
                            throw new RuntimeException(ex);
                        } finally {
                            scanningIconLabel.setVisible(false);
                            scanningLabel.setVisible(false);
                            crashScanButton.setEnabled(true);
                            ((Timer) evt.getSource()).stop();  // Stop the timer after the first execution
                        }
                    });
                    timer.setRepeats(false); // Ensure it only executes once
                    timer.start();
                }
            });
            add(crashScanButton);

// --- Wi-Fi Control Buttons ---
        JLabel wifiLabel = new JLabel("Wi-Fi Control :");
      //  wifiLabel.setFont(interFont.deriveFont(12f));
        wifiLabel.setFont(interFont);
        wifiLabel.setBounds(50, 250, 200, 30);
        add(wifiLabel);

        // Wi-Fi On Button
        ImageIcon wifiOnIcon = new ImageIcon(getClass().getClassLoader().getResource("icons/WifiOn.png"));
        Image scaledOnImage = wifiOnIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        JButton wifiOnButton = new JButton(new ImageIcon(scaledOnImage));
        wifiOnButton.setToolTipText("Turn Wi-Fi On");
        wifiOnButton.setBounds(150, 245, 35, 40);
        wifiOnButton.setBorderPainted(false);
        wifiOnButton.setContentAreaFilled(false);
        wifiOnButton.setFocusPainted(false);
        wifiOnButton.setOpaque(false);
        add(wifiOnButton);

// Wi-Fi Off Button
        ImageIcon wifiOffIcon = new ImageIcon(getClass().getClassLoader().getResource("icons/WifiOff.png"));
        Image scaledOffImage = wifiOffIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        JButton wifiOffButton = new JButton(new ImageIcon(scaledOffImage));
        wifiOffButton.setToolTipText("Turn Wi-Fi Off");
        wifiOffButton.setBounds(190, 245, 35, 40);
        wifiOffButton.setBorderPainted(false);
        wifiOffButton.setContentAreaFilled(false);
        wifiOffButton.setFocusPainted(false);
        wifiOffButton.setOpaque(false);
        add(wifiOffButton);

        wifiOnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ADBHelper.enableWifi();
                //JOptionPane.showMessageDialog(null, "Wi-Fi turned on!");
            }
        });

        wifiOffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ADBHelper.disableWifi();
              //  JOptionPane.showMessageDialog(null, "Wi-Fi turned off!");
            }
        });

        // Update the device status
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

        // Add HelpOptionsPanel after the frame is visible

    }



    // Method to update the device connection status
    public void updateDeviceStatus() {
        List<String> devices = ADBHelper.getConnectedDevices();
        String statusText = devices.isEmpty() ? "No devices connected." : "Connected Devices: " + String.join(", ", devices);
        deviceStatusLabel.setText(statusText);
    }

    // Method to open Account Selection GUI
    private void openAccountSelectionGUI() {
        SwingUtilities.invokeLater(() -> new AccountSelectionGUI(this));
    }

    private void openScreenRecordingGUI() {
        SwingUtilities.invokeLater(() -> new AndroidScreenRecordingGUI(this));
    }

    // Method to set the main app frame
    public void setMainAppFrame(JFrame mainAppFrame) {
        this.mainAppFrame = mainAppFrame;
    }

    // Method to go back to the main app
    private void goBackToMainApp() {
        if (mainAppFrame != null) {
            mainAppFrame.setVisible(true);
        }
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AndroidUtility::new);
    }
}
