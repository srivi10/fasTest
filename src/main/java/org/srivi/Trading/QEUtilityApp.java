package org.srivi.Trading;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class QEUtilityApp {
    private JFrame frame;
    private Process screenRecordProcess;
    private JTextField fileNameField;
    private JTextField fileLocationField;
    private JLabel savedLocationLabel;

    public QEUtilityApp() {
        // Initialize the main frame
        frame = new JFrame("QE Utility App");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Label for device selection
        JLabel selectDeviceLabel = new JLabel("Select Device:");
        selectDeviceLabel.setBounds(150, 30, 100, 30);
        frame.add(selectDeviceLabel);

        // Button for Android option
        JButton androidButton = new JButton("Android");
        androidButton.setBounds(50, 80, 120, 30);
        frame.add(androidButton);

        // Button for iOS option (Future Implementation)
        JButton iosButton = new JButton("iOS");
        iosButton.setBounds(220, 80, 120, 30);
        frame.add(iosButton);

        // ActionListener for Android Button
        androidButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAndroidUtilityScreen();
            }
        });

        // Placeholder ActionListener for iOS Button
        iosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "iOS Utility not implemented yet.");
            }
        });

        frame.setVisible(true);
    }

    private void openAndroidUtilityScreen() {
        // Close the main frame
        frame.dispose();

        // Initialize the Android Utility frame
        JFrame androidFrame = new JFrame("Android Utility");
        androidFrame.setSize(500, 400);
        androidFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        androidFrame.setLayout(null);

        // Label for Android Utility
        JLabel utilityLabel = new JLabel("Android Utility Options:");
        utilityLabel.setBounds(120, 20, 160, 30);
        androidFrame.add(utilityLabel);

        // Get list of connected devices
        List<String> devices = getConnectedDevices();
        JLabel deviceLabel;

        if (devices.isEmpty()) {
            deviceLabel = new JLabel("No devices connected.");
        } else {
            StringBuilder devicesText = new StringBuilder("Connected Devices: ");
            for (String device : devices) {
                devicesText.append(device).append(" ");
            }
            deviceLabel = new JLabel(devicesText.toString());
        }

        deviceLabel.setBounds(20, 60, 460, 30);
        androidFrame.add(deviceLabel);

        // Label for file name
        JLabel fileNameLabel = new JLabel("File Name:");
        fileNameLabel.setBounds(50, 120, 100, 30);
        androidFrame.add(fileNameLabel);

        // Text field for file name input
        fileNameField = new JTextField("android_screen_record");
        fileNameField.setBounds(150, 120, 200, 30);
        androidFrame.add(fileNameField);

        // Label for file location
        JLabel fileLocationLabel = new JLabel("File Location:");
        fileLocationLabel.setBounds(50, 170, 100, 30);
        androidFrame.add(fileLocationLabel);

        // Text field for file location input
        fileLocationField = new JTextField();
        fileLocationField.setBounds(150, 170, 200, 30);
        androidFrame.add(fileLocationField);

        // Button to browse and select file location
        JButton browseButton = new JButton("Browse");
        browseButton.setBounds(360, 170, 80, 30);
        androidFrame.add(browseButton);

        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectFileLocation();
            }
        });

        // Button for Start Screen Recording
        JButton startRecordButton = new JButton("Start Screen Recording");
        startRecordButton.setBounds(50, 230, 150, 30);
        androidFrame.add(startRecordButton);

        // Button for Stop Screen Recording
        JButton stopRecordButton = new JButton("Stop Screen Recording");
        stopRecordButton.setBounds(220, 230, 150, 30);
        stopRecordButton.setEnabled(false); // Disable stop button initially
        androidFrame.add(stopRecordButton);

        // Label to display the saved file location
        savedLocationLabel = new JLabel("");
        savedLocationLabel.setBounds(20, 280, 460, 30);
        androidFrame.add(savedLocationLabel);

        // ActionListener for Start Recording Button
        startRecordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startAndroidScreenRecording(fileNameField.getText());
                startRecordButton.setEnabled(false);
                stopRecordButton.setEnabled(true);
            }
        });

        // ActionListener for Stop Recording Button
        stopRecordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopAndroidScreenRecording(fileNameField.getText(), fileLocationField.getText());
                startRecordButton.setEnabled(true);
                stopRecordButton.setEnabled(false);
            }
        });

        androidFrame.setVisible(true);
    }

    private List<String> getConnectedDevices() {
        List<String> devices = new ArrayList<>();
        try {
            ProcessBuilder pb = new ProcessBuilder("adb", "devices");
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.endsWith("device")) {
                    devices.add(line.split("\t")[0]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to retrieve device list: " + e.getMessage());
        }
        return devices;
    }

    private void selectFileLocation() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose Save Location");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int userSelection = fileChooser.showOpenDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = fileChooser.getSelectedFile();
            fileLocationField.setText(selectedDirectory.getAbsolutePath());
        }
    }

    private void startAndroidScreenRecording(String fileName) {
        try {
            // Start recording and save file on the device with the provided file name
            ProcessBuilder pb = new ProcessBuilder("adb", "shell", "screenrecord", "--size", "480x854",  // Directly setting the resolution
                    "--bit-rate", "500000", "/sdcard/" + fileName + ".mp4");
            screenRecordProcess = pb.start();
            JOptionPane.showMessageDialog(null, "Recording started.");
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to start screen recording: " + ex.getMessage());
        }
    }

    private void stopAndroidScreenRecording(String fileName, String fileLocation) {
         try {
            if (screenRecordProcess != null) {
                screenRecordProcess.destroy();

                if (fileLocation.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please specify a file location.");
                    return;
                }

                Thread.sleep(2000);

                // Pull the recorded file from the device
                String savePath = fileLocation + File.separator + fileName + ".mp4";
                ProcessBuilder pullProcess = new ProcessBuilder("adb", "pull", "/sdcard/" + fileName + ".mp4", savePath);
                Process process = pullProcess.start();

                // Capture output and error streams
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

                String line;
                StringBuilder output = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }

                StringBuilder errorOutput = new StringBuilder();
                while ((line = errorReader.readLine()) != null) {
                    errorOutput.append(line).append("\n");
                }

                int exitCode = process.waitFor();

                if (exitCode == 0) {
                    savedLocationLabel.setText("File saved at: " + savePath);
                } else {
                    JOptionPane.showMessageDialog(null, "Error pulling file: " + errorOutput.toString());
                }

            } else {
                JOptionPane.showMessageDialog(null, "No recording process found.");
            }
        }

         catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to stop screen recording: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new QEUtilityApp());
    }
}