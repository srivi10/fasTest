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
    private JButton screenshotButton;
    private JButton screenRecordingButton;
    private JButton crashScanButton;
    private JButton wifiOnButton;
    private JButton wifiOffButton;
    private JButton enButton;
    private JButton frButton;
    private List<String> devices;
    String desktopPath = System.getProperty("user.home") + "/Desktop";
    private HelpOptionsPanel helpOptionsPanel;
        public AndroidUtility() {
        setTitle("Android Utility");
        setSize(400, 430);
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

            ImageIcon warningIcon = new ImageIcon(getClass().getClassLoader().getResource("icons/GreenCaution.png"));
            Image scaledWarningIcon = warningIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);

            // Warning Label
            JLabel warningLabel = new JLabel("Device Compatibililty Check", new ImageIcon(scaledWarningIcon), JLabel.LEFT);
            warningLabel.setToolTipText("Supports Emulator and Physcial Device");
            warningLabel.setBounds(190, 20, 300, 20);
            warningLabel.setFont(interFont);
            add(warningLabel);

            // Label for device connection status
        deviceStatusLabel = new JLabel("Device Connected: Checking...");
        deviceStatusLabel.setFont(interFont);
        deviceStatusLabel.setBounds(50, 50, 300, 30);
        add(deviceStatusLabel);

        // Button for Take Screenshot option
        screenshotButton = new JButton("Take Screenshot");
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
        screenRecordingButton = new JButton("Screen Recording");
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
            crashScanButton = new JButton("Crash Scan");

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
        JLabel wifiLabel = new JLabel("Wi-Fi Control");
      //  wifiLabel.setFont(interFont.deriveFont(12f));
        wifiLabel.setFont(interFont);
        wifiLabel.setBounds(50, 250, 200, 30);
        add(wifiLabel);

        // Wi-Fi On Button
        ImageIcon wifiOnIcon = new ImageIcon(getClass().getClassLoader().getResource("icons/WifiOn.png"));
        Image scaledOnImage = wifiOnIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        wifiOnButton = new JButton(new ImageIcon(scaledOnImage));
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
        wifiOffButton = new JButton(new ImageIcon(scaledOffImage));
        wifiOffButton.setToolTipText("Turn Wi-Fi Off");
        wifiOffButton.setBounds(190, 245, 35, 40);
        wifiOffButton.setBorderPainted(false);
        wifiOffButton.setContentAreaFilled(false);
        wifiOffButton.setFocusPainted(false);
        wifiOffButton.setOpaque(false);
        add(wifiOffButton);

            // Add loading icon for Wi-Fi actions
            JLabel wifiLoadingIconLabel = new JLabel();
            ImageIcon wifiLoadingIcon = new ImageIcon(getClass().getResource("/icons/MasterLoading.png"));
            wifiLoadingIconLabel.setIcon(wifiLoadingIcon);
            wifiLoadingIconLabel.setVisible(false);
            add(wifiLoadingIconLabel).setBounds(245, 245, 45, 40); // Position it next to the Wi-Fi buttons

            JLabel wifiLoadingLabel = new JLabel("Loading...");
            wifiLoadingLabel.setForeground(new Color(0, 122, 255));
            wifiLoadingLabel.setVisible(false);
            add(wifiLoadingLabel).setBounds(245, 285, 100, 30);

// Add loading icon for language actions
            JLabel languageLoadingIconLabel = new JLabel();
            ImageIcon languageLoadingIcon = new ImageIcon(getClass().getResource("/icons/MasterLoading.png"));
            languageLoadingIconLabel.setIcon(languageLoadingIcon);
            languageLoadingIconLabel.setVisible(false);
            add(languageLoadingIconLabel).setBounds(245, 275, 45, 40); // Position it next to the language buttons

            JLabel languageLoadingLabel = new JLabel("Loading...");
            languageLoadingLabel.setForeground(new Color(0, 122, 255));
            languageLoadingLabel.setVisible(false);
            add(languageLoadingLabel).setBounds(245, 315, 100, 30);

        wifiOnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wifiLoadingIconLabel.setVisible(true);
                wifiLoadingLabel.setVisible(true);

                Timer timer = new Timer(500, evt -> {
                    ADBHelper.enableWifi();
                    wifiLoadingIconLabel.setVisible(false);
                    wifiLoadingLabel.setVisible(false);
                    ((Timer) evt.getSource()).stop();
                });
                timer.setRepeats(false);
                timer.start();
            }
        });

        wifiOffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wifiLoadingIconLabel.setVisible(true);
                wifiLoadingLabel.setVisible(true);

                Timer timer = new Timer(500, evt -> {
                    ADBHelper.disableWifi();
                    wifiLoadingIconLabel.setVisible(false);
                    wifiLoadingLabel.setVisible(false);
                    ((Timer) evt.getSource()).stop();
                });
                timer.setRepeats(false);
                timer.start();
            }
        });

            // --- Language Selection Buttons ---
            JLabel languageLabel = new JLabel("Language");
            languageLabel.setFont(interFont);
            languageLabel.setBounds(50, 290, 200, 30);
            add(languageLabel);

            ImageIcon cautionIcon = new ImageIcon(getClass().getClassLoader().getResource("icons/Tips.png"));
            Image scaledCautionImage = cautionIcon.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH);
            ImageIcon scaledCautionIcon = new ImageIcon(scaledCautionImage);

            JLabel cautionIconLabel = new JLabel(scaledCautionIcon);
            cautionIconLabel.setToolTipText("Click Here to Learn More");
            cautionIconLabel.setBounds(100, 280, 35, 35);
            add(cautionIconLabel);

    cautionIconLabel.addMouseListener(new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent e) {
        String message = "This feature is compatible only with Android emulator.\n\n" +
                         "If the language change is unsuccessful, try this one-time activity:\n" +
                         "1) Wipe Android device data (Android Studio -> Device Manager -> Click three dots -> Wipe Data)\n" +
                         "2) Now click on the EN and FR buttons. The language change should work from now on.\n";
        JOptionPane.showMessageDialog(null, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
});

// EN Button
            ImageIcon enIcon = new ImageIcon(getClass().getClassLoader().getResource("icons/USLocale.png"));
            Image scaledEnImage = enIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            enButton = new JButton(new ImageIcon(scaledEnImage));
            enButton.setToolTipText("Set Language to English");
            enButton.setBounds(150, 285, 35, 40);
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
            frButton.setBounds(190, 285, 35, 40);
            frButton.setBorderPainted(false);
            frButton.setContentAreaFilled(false);
            frButton.setFocusPainted(false);
            frButton.setOpaque(false);
            add(frButton);

// Action Listeners for Language Buttons
            enButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    languageLoadingIconLabel.setVisible(true);
                    languageLoadingLabel.setVisible(true);

                    Timer timer = new Timer(500, evt -> {
                        ADBHelper.setLanguageToEnglish();
                        languageLoadingIconLabel.setVisible(false);
                        languageLoadingLabel.setVisible(false);
                        JOptionPane.showMessageDialog(null, "Language set to English and Region Canada!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        ((Timer) evt.getSource()).stop();
                    });
                    timer.setRepeats(false);
                    timer.start();
                }
            });

            frButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    languageLoadingIconLabel.setVisible(true);
                    languageLoadingLabel.setVisible(true);

                    Timer timer = new Timer(500, evt -> {
                        ADBHelper.setLanguageToFrench();
                        languageLoadingIconLabel.setVisible(false);
                        languageLoadingLabel.setVisible(false);
                        JOptionPane.showMessageDialog(null, "Language set to French and Region Canada!");
                        ((Timer) evt.getSource()).stop();
                    });
                    timer.setRepeats(false);
                    timer.start();

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
        devices = ADBHelper.getConnectedDevices();

        // Update status text based on the number of connected devices
        String statusText;
        if (devices.isEmpty()) {
            statusText = "Device Connected: No devices connected !";
        } else if (devices.size() > 1) {
            statusText = "Multiple Device connected: " + " Please Connect One !";
        } else {
            statusText = "Connected Device: " + devices.get(0);  // Show the single connected device
        }

        deviceStatusLabel.setText(statusText);

        // Only set devicesConnected to true if exactly one device is connected
        boolean devicesConnected = devices.size() == 1;

        // Enable or disable buttons based on the number of connected devices
        screenshotButton.setEnabled(devicesConnected);
        screenRecordingButton.setEnabled(devicesConnected);
        crashScanButton.setEnabled(devicesConnected);
        wifiOnButton.setEnabled(devicesConnected);
        wifiOffButton.setEnabled(devicesConnected);
        boolean isSingleEmulatorConnected = devices.size() == 1 && devices.get(0).startsWith("emulator");

        if (isSingleEmulatorConnected) {
            enButton.setEnabled(true);
            frButton.setEnabled(true);
        } else {
            enButton.setEnabled(false);
            frButton.setEnabled(false);
        }


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
