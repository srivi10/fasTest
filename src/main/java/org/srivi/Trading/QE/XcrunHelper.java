package org.srivi.Trading.QE;

import javax.swing.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class XcrunHelper {
    private static Process recordProcess;

    public static boolean takeScreenshot() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        // Append the timestamp to the file name
        String fileName = "MBNA_iOS_" + timeStamp;
        String savePath = System.getProperty("user.home") + "/Desktop/" + fileName + ".png";

        try {
            // Step 1: Take a screenshot and save it to the device's temporary directory
            ProcessBuilder takeScreenshot = new ProcessBuilder("xcrun", "simctl", "io", "booted", "screenshot", "/tmp/" + fileName + ".png");
            Process takeScreenshotProcess = takeScreenshot.start();
            takeScreenshotProcess.waitFor(); // Wait for the process to complete

            // Step 2: Move the screenshot from the device's temporary directory to the Desktop
            ProcessBuilder moveScreenshot = new ProcessBuilder("mv", "/tmp/" + fileName + ".png", savePath);
            Process moveScreenshotProcess = moveScreenshot.start();
            moveScreenshotProcess.waitFor(); // Wait for the process to complete

            // The file is automatically removed from the simulator after moving to the Desktop
            ImageIcon customIcon = new ImageIcon(XcrunHelper.class.getResource("/icons/Screencap.png"));

            JOptionPane.showMessageDialog( null,
                    "Screenshot saved to Desktop",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE,
                    customIcon);
            return true; // Return true if all commands executed successfully

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false; // Return false if any command fails
        }
    }

    public static List<String> getConnectedDevices() {
        List<String> devices = new ArrayList<>();

        try {
            // Use "bash -c" to run the command in a shell
            ProcessBuilder pb = new ProcessBuilder("bash", "-c", "xcrun simctl list devices | grep 'Booted'");
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("(Booted)")) {
                    devices.add(line.split("\\(")[0].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to retrieve device list: " + e.getMessage());
        }
        return devices;
    }

    public static void startiOSScreenRecording(String fileName, String fileLocation) {
        // Combine file name and location
        String recordingFileName = fileLocation + File.separator + fileName + ".mp4";
        System.out.println("Starting recording with file name: " + recordingFileName);

        // Use the HEVC codec for better compression
        String command = String.format("xcrun simctl io %s recordVideo --codec=hevc --force %s", "booted", recordingFileName);

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
            recordProcess = processBuilder.start();
            ImageIcon customIcon = new ImageIcon(XcrunHelper.class.getResource("/icons/VideoIcon.png"));

            //JOptionPane.showMessageDialog(null, "Recording started.");
            JOptionPane.showMessageDialog( null,
                    "Recording Started",
                    "",
                    JOptionPane.INFORMATION_MESSAGE,
                    customIcon);

            System.out.println("Recording started with HEVC codec.");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error starting recording: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void stopiOSScreenRecording(String fileName,String fileLocation,JLabel savedLocationLabel) {
        String savePath = fileLocation + File.separator + fileName + ".mp4";
        if (recordProcess != null) {

            String pid = String.valueOf(recordProcess.pid());
            String killCommand = "kill -2 " + pid;
            try {
                new ProcessBuilder("bash", "-c", killCommand).start();
                System.out.println("SIGINT signal sent to process " + pid);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error sending SIGINT to recording process: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        String command = String.format("xcrun simctl io %s recordVideo --stop", "booted");
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
            Process stopProcess = processBuilder.start();
            stopProcess.waitFor(3, TimeUnit.SECONDS); // Wait up to 3 seconds for the command to complete
            System.out.println("Recording stopped.");
            ImageIcon customIcon = new ImageIcon(XcrunHelper.class.getResource("/icons/VideoIcon.png"));

            //JOptionPane.showMessageDialog(null, "Recording started.");
            JOptionPane.showMessageDialog( null,
                    "Recording Stopped" +"  " +"File saved to: " + savePath,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE,
                    customIcon);
           // savedLocationLabel.setText("File saved to: " + savePath);
            } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error stopping recording: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
            JOptionPane.showMessageDialog(null, "Error stopping recording: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    public static void ENLocale() {
    }

    public static void FRLocale() {
    }
}

