package org.srivi.Trading.QE;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    // Method to take a screenshot, pull it to the Desktop, and delete it from the device
    public static boolean takeScreenshot() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        // Append the timestamp to the file name
        String fileName = "MBNA_Android_" + timeStamp;
        String savePath = System.getProperty("user.home") + "/Desktop/" + fileName + ".png";

        try {
            // Step 1: Take screenshot and save it to the device
            ProcessBuilder takeScreenshot = new ProcessBuilder("adb", "shell", "screencap", "-p", "/sdcard/" + fileName + ".png");
            Process takeScreenshotProcess = takeScreenshot.start();
            takeScreenshotProcess.waitFor(); // Wait for the process to complete

            // Step 2: Pull the screenshot from the device to the Desktop
            ProcessBuilder pullScreenshot = new ProcessBuilder("adb", "pull", "/sdcard/" + fileName + ".png", savePath);
            Process pullScreenshotProcess = pullScreenshot.start();
            pullScreenshotProcess.waitFor(); // Wait for the process to complete

            // Step 3: Delete the screenshot from the device
            ProcessBuilder deleteScreenshot = new ProcessBuilder("adb", "shell", "rm", "/sdcard/" + fileName + ".png");
            Process deleteScreenshotProcess = deleteScreenshot.start();
            deleteScreenshotProcess.waitFor(); // Wait for the process to complete

            return true; // Return true if all commands executed successfully

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false; // Return false if any command fails
        }
    }

    public static void passTextToEmulator(String text) {
        try {
            // Pass the text to the adb shell input text command
            String command = "adb shell input text \"" + text + "\"";
            Process processInputText = Runtime.getRuntime().exec(command);
            processInputText.waitFor();

            // Pass the adb shell input keyevent KEYCODE_TAB command
            String commandKeyEventTab = "adb shell input keyevent KEYCODE_TAB";
            Process processKeyEventTab = Runtime.getRuntime().exec(commandKeyEventTab);
            processKeyEventTab.waitFor();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    public static void enableWifi() {
        String command = "svc wifi enable";
        runADBCommand(command);
    }

    public static void disableWifi() {
        String command = "svc wifi disable";
        runADBCommand(command);
    }

    private static void runADBCommand(String command) {
        ProcessBuilder processBuilder = new ProcessBuilder("adb", "shell", command);
        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);  // Print output for debugging purposes
            }
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
