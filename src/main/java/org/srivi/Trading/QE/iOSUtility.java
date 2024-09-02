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
    String desktopPath = System.getProperty("user.home") + "/Desktop";

    public iOSUtility() {
        setTitle("iOS Utility");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
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
        warningLabel.setBounds(170, 20, 300, 20);
        warningLabel.setFont(interFont);
        add(warningLabel);

        // Label for device connection status
        deviceStatusLabel = new JLabel("Device Connected: Checking...");
        deviceStatusLabel.setBounds(50, 60, 300, 30);
        deviceStatusLabel.setFont(interFont);
        add(deviceStatusLabel);

        // Button for Take Screenshot option
        JButton screenshotButton = new JButton("Take Screenshot");
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
                boolean success = XcrunHelper.takeScreenshot();
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
        screenRecordingButton.setBounds(50, 140, 130, 30);
        screenRecordingButton.setFont(interFont);
        screenRecordingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openScreenRecordingView();
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

//        JLabel localeLabel = new JLabel("Locale:");
//        localeLabel.setBounds(50, 220, 100, 30);
//        localeLabel.setFont(interFont);
//        add(localeLabel);
//
//        ImageIcon enLocaleIcon = new ImageIcon(getClass().getClassLoader().getResource("icons/US_Locale.png"));
//        Image scaledEnImage = enLocaleIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
//        ImageIcon scaledEnIcon = new ImageIcon(scaledEnImage);
//        JLabel enLocaleLabel = new JLabel(scaledEnIcon);
//        enLocaleLabel.setBounds(100, 220, 30, 30); // Adjust the position and size as needed
//        enLocaleLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//
//// Add a click listener for EN Locale
//        enLocaleLabel.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                XcrunHelper.ENLocale(); // Call the method in XcrunHelper
//            }
//        });
//
//// FR Locale Icon
//        ImageIcon frLocaleIcon = new ImageIcon(getClass().getClassLoader().getResource("icons/FR_Locale.png"));
//        Image scaledFrImage = frLocaleIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
//        ImageIcon scaledFrIcon = new ImageIcon(scaledFrImage);
//        JLabel frLocaleLabel = new JLabel(scaledFrIcon);
//        frLocaleLabel.setBounds(133, 220, 30, 30); // Adjust the position and size as needed
//        frLocaleLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//
//        // Add the labels to the frame
//        enLocaleLabel.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                XcrunHelper.ENLocale(); // Call the method in XcrunHelper
//            }
//        });
//        // Add a click listener for FR Locale
//        frLocaleLabel.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                XcrunHelper.FRLocale(); // Call the method in XcrunHelper
//            }
//        });

// Add the labels to the frame
//        add(enLocaleLabel);
//        add(frLocaleLabel);
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
    }

    // Method to update the device connection status
    public void updateDeviceStatus() {
        List<String> devices = XcrunHelper.getConnectedDevices();
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

        JLabel backLabel = BackButtonUtil.createBackLabel(recordingFrame);
        recordingFrame.add(backLabel);

        // File Name Label and Field
        JLabel fileNameLabel = new JLabel("File Name:");
        fileNameLabel.setBounds(50, 70, 100, 30);
        fileNameLabel.setFont(interFont);
        recordingFrame.add(fileNameLabel);

        fileNameField = new JTextField("ios_screen_record");
        fileNameField.setFont(interFont);
        fileNameField.setBounds(150, 70, 150, 30);
        recordingFrame.add(fileNameField);

        // File Location Label and Field
        JLabel fileLocationLabel = new JLabel("File Location:");
        fileLocationLabel.setFont(interFont);
        fileLocationLabel.setBounds(50, 120, 100, 30);
        recordingFrame.add(fileLocationLabel);

        fileLocationField = new JTextField(desktopPath);
        fileLocationField.setBounds(150, 120, 150, 30);
        fileLocationField.setFont(interFont);
        recordingFrame.add(fileLocationField);

        JButton browseButton = new JButton("Browse");
        browseButton.setBounds(360, 120, 80, 30);
        recordingFrame.add(browseButton);
        browseButton.setFont(interFont);
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
        savedLocationLabel.setBounds(20, 220, 460, 30);
        savedLocationLabel.setFont(interFont);
        recordingFrame.add(savedLocationLabel);

        startRecordButton.addActionListener(e -> {
            XcrunHelper.startiOSScreenRecording(fileNameField.getText(), fileLocationField.getText());
            startRecordButton.setEnabled(false);
            stopRecordButton.setEnabled(true);
        });

        stopRecordButton.addActionListener(e -> {
            XcrunHelper.stopiOSScreenRecording(fileNameField.getText(),fileLocationField.getText(),savedLocationLabel);
            startRecordButton.setEnabled(true);
            stopRecordButton.setEnabled(false);
        });

        recordingFrame.setVisible(true);
        helpOptionsPanel = new HelpOptionsPanel(getWidth(), getHeight());
        recordingFrame.add(helpOptionsPanel);

        // Add ComponentListener to handle resizing
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Reposition HelpOptionsPanel
                helpOptionsPanel.setBounds(0, getHeight() - 100, getWidth(), 100);
            }
        });

    }

    // Method to open Account Selection GUI
    private void openAccountSelectionGUI() {
        SwingUtilities.invokeLater(() -> new AccountSelectionGUI(this));
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
