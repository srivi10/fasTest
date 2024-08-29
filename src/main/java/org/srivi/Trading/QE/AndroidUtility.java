package org.srivi.Trading.QE;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;
import org.srivi.Trading.AccountSelectionGUI;
import org.srivi.Trading.QE.ADBHelper;

public class AndroidUtility extends JFrame {

    private JFrame mainAppFrame;
    private JTextField fileNameField;
    private JTextField fileLocationField;
    private JLabel savedLocationLabel;
    private JLabel deviceStatusLabel;
    private HelpOptionsPanel helpOptionsPanel;
        public AndroidUtility() {
        setTitle("Android Utility");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        // Set the custom font
        Font interFont = FontUtil.getInterFont(12f);



        // Back button to return to main app
        JButton backButton = new JButton("Back");
        backButton.setFont(interFont);
        backButton.setBounds(10, 10, 80, 30);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackToMainApp();
            }
        });
        add(backButton);

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
                openScreenRecordingView();
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
// --- Wi-Fi Control Buttons ---
        JLabel wifiLabel = new JLabel("Wi-Fi Control :");
      //  wifiLabel.setFont(interFont.deriveFont(12f));
        wifiLabel.setFont(interFont);
        wifiLabel.setBounds(50, 210, 200, 30);
        add(wifiLabel);

        // Wi-Fi On Button
        ImageIcon wifiOnIcon = new ImageIcon(getClass().getClassLoader().getResource("icons/WifiOn.png"));
        Image scaledOnImage = wifiOnIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        JButton wifiOnButton = new JButton(new ImageIcon(scaledOnImage));
        wifiOnButton.setToolTipText("Turn Wi-Fi On");
        wifiOnButton.setBounds(150, 205, 35, 40);
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
        wifiOffButton.setBounds(190, 205, 35, 40);
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

    // Method to open Screen Recording View
    private void openScreenRecordingView() {
        JFrame recordingFrame = new JFrame("Screen Recording");
        recordingFrame.setSize(500, 400);
        recordingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        recordingFrame.setLayout(null);
        // Set the custom font
        Font interFont = FontUtil.getInterFont(12f);

        // Back button to return to AndroidUtility view
        JButton backButton = new JButton("Back");
        backButton.setBounds(10, 10, 80, 30);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recordingFrame.dispose();
                setVisible(true);
            }
        });
        recordingFrame.add(backButton);

        // File Name Label and Field
        JLabel fileNameLabel = new JLabel("File Name:");
        fileNameLabel.setBounds(50, 70, 100, 30);
        fileNameLabel.setFont(interFont);
        recordingFrame.add(fileNameLabel);

        fileNameField = new JTextField("android_screen_record");
        fileNameField.setBounds(150, 70, 150, 30);
        fileNameField.setFont(interFont);
        recordingFrame.add(fileNameField);

        // File Location Label and Field
        JLabel fileLocationLabel = new JLabel("File Location:");
        fileLocationLabel.setFont(interFont);
        fileLocationLabel.setBounds(50, 120, 100, 30);
        recordingFrame.add(fileLocationLabel);

        fileLocationField = new JTextField();
        fileLocationField.setBounds(150, 120, 150, 30);
        fileLocationField.setFont(interFont);
        recordingFrame.add(fileLocationField);

        JButton browseButton = new JButton("Browse");
        browseButton.setBounds(360, 120, 80, 30);
        browseButton.setFont(interFont);
        recordingFrame.add(browseButton);
        browseButton.addActionListener(e -> FileChooserUtil.selectFileLocation(fileLocationField));

        // Start and Stop Recording Buttons
        JButton startRecordButton = new JButton("Start Recording");
        startRecordButton.setBounds(50, 170, 150, 30);
        startRecordButton.setFont(interFont);
        recordingFrame.add(startRecordButton);

        JButton stopRecordButton = new JButton("Stop Recording");
        stopRecordButton.setBounds(220, 170, 150, 30);
        stopRecordButton.setFont(interFont);
        stopRecordButton.setEnabled(false);
        recordingFrame.add(stopRecordButton);

        savedLocationLabel = new JLabel("");
        savedLocationLabel.setBounds(20, 280, 460, 30);
        savedLocationLabel.setFont(interFont);
        recordingFrame.add(savedLocationLabel);

        startRecordButton.addActionListener(e -> {
            ADBHelper.startAndroidScreenRecording(fileNameField.getText());
            startRecordButton.setEnabled(false);
            stopRecordButton.setEnabled(true);
        });

        stopRecordButton.addActionListener(e -> {
            ADBHelper.stopAndroidScreenRecording(fileNameField.getText(), fileLocationField.getText(), savedLocationLabel);
            startRecordButton.setEnabled(true);
            stopRecordButton.setEnabled(false);
        });

        recordingFrame.setVisible(true);
    }

    // Method to open Account Selection GUI
    private void openAccountSelectionGUI() {
        SwingUtilities.invokeLater(() -> new AccountSelectionGUI(this));
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
