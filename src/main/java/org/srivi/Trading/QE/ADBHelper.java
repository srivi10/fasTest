package org.srivi.Trading.QE;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ADBHelper {
    private static Process screenRecordProcess;

    public static List<String> getConnectedDevices() {
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

    public static void startAndroidScreenRecording(String fileName) {
        try {
            ProcessBuilder pb = new ProcessBuilder("adb", "shell", "screenrecord", "--size", "480x854", "--bit-rate", "500000", "/sdcard/" + fileName + ".mp4");
            screenRecordProcess = pb.start();
            JOptionPane.showMessageDialog(null, "Recording started.");
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to start screen recording: " + ex.getMessage());
        }
    }

    public static void stopAndroidScreenRecording(String fileName, String fileLocation, JLabel savedLocationLabel) {
        try {
            if (screenRecordProcess != null) {
                screenRecordProcess.destroy();

                if (fileLocation.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please specify a file location.");
                    return;
                }

                Thread.sleep(2000);

                String savePath = fileLocation + File.separator + fileName + ".mp4";
                ProcessBuilder pullProcess = new ProcessBuilder("adb", "pull", "/sdcard/" + fileName + ".mp4", savePath);
                Process process = pullProcess.start();

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
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to stop screen recording: " + ex.getMessage());
        }
    }
}
