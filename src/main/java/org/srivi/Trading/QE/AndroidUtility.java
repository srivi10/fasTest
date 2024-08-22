package org.srivi.Trading.QE;


import javax.swing.*;
import java.util.List;

public class AndroidUtility {
    private JFrame androidFrame;
    private JTextField fileNameField;
    private JTextField fileLocationField;
    private JLabel savedLocationLabel;

    public AndroidUtility() {
        // Initialize the Android Utility frame
        androidFrame = new JFrame("Android Utility");
        androidFrame.setSize(500, 400);
        androidFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        androidFrame.setLayout(null);

        JLabel utilityLabel = new JLabel("Android Utility Options:");
        utilityLabel.setBounds(120, 20, 160, 30);
        androidFrame.add(utilityLabel);

        // Get list of connected devices
        List<String> devices = ADBHelper.getConnectedDevices();
        JLabel deviceLabel = new JLabel(devices.isEmpty() ? "No devices connected." : "Connected Devices: " + String.join(", ", devices));
        deviceLabel.setBounds(20, 60, 460, 30);
        androidFrame.add(deviceLabel);

        // File Name Label and Field
        JLabel fileNameLabel = new JLabel("File Name:");
        fileNameLabel.setBounds(50, 120, 100, 30);
        androidFrame.add(fileNameLabel);

        fileNameField = new JTextField("android_screen_record");
        fileNameField.setBounds(150, 120, 200, 30);
        androidFrame.add(fileNameField);

        // File Location Label and Field
        JLabel fileLocationLabel = new JLabel("File Location:");
        fileLocationLabel.setBounds(50, 170, 100, 30);
        androidFrame.add(fileLocationLabel);

        fileLocationField = new JTextField();
        fileLocationField.setBounds(150, 170, 200, 30);
        androidFrame.add(fileLocationField);

        JButton browseButton = new JButton("Browse");
        browseButton.setBounds(360, 170, 80, 30);
        androidFrame.add(browseButton);
        browseButton.addActionListener(e -> FileChooserUtil.selectFileLocation(fileLocationField));

        // Start and Stop Recording Buttons
        JButton startRecordButton = new JButton("Start Screen Recording");
        startRecordButton.setBounds(50, 230, 150, 30);
        androidFrame.add(startRecordButton);

        JButton stopRecordButton = new JButton("Stop Screen Recording");
        stopRecordButton.setBounds(220, 230, 150, 30);
        stopRecordButton.setEnabled(false);
        androidFrame.add(stopRecordButton);

        savedLocationLabel = new JLabel("");
        savedLocationLabel.setBounds(20, 280, 460, 30);
        androidFrame.add(savedLocationLabel);

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

        androidFrame.setVisible(true);
    }
}
