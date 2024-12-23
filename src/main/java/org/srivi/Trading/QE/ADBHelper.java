package org.srivi.Trading.QE;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ADBHelper {
    private static Process screenRecordProcess;
    public static String adbPath = "/Library/Android/sdk/platform-tools/adb";
    public static String adbExecutable = System.getProperty("user.home") + adbPath;

    public static List<String> getConnectedDevices() {
        List<String> devices = new ArrayList<>();

        try {
            // Use adbExecutable instead of just "adb"
            ProcessBuilder pb = new ProcessBuilder(adbExecutable, "devices");
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.endsWith("device")) {
                    devices.add(line.split("\t")[0]);
                }
            }
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to retrieve device list: " + e.getMessage());
        }

        return devices;
    }


    public static void startAndroidScreenRecording(String fileName, String fileLocation) {
        if (fileLocation.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please specify a file location.");
            return;
        }

        try {
            ProcessBuilder pb = new ProcessBuilder(adbExecutable, "shell", "screenrecord",
                    "--size", "480x854", "--bit-rate", "500000",
                    "/sdcard/" + fileName + ".mp4");
            screenRecordProcess = pb.start();

            ImageIcon customIcon = new ImageIcon(ADBHelper.class.getResource("/icons/VideoIcon.png"));
            JOptionPane.showMessageDialog(null,
                    "Recording started.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE,
                    customIcon);

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

                Thread.sleep(1000);

                String savePath = fileLocation + File.separator + fileName + ".mp4";
                ProcessBuilder pullProcess = new ProcessBuilder(adbExecutable, "pull", "/sdcard/" + fileName + ".mp4", savePath);
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
                    ImageIcon customIcon = new ImageIcon(XcrunHelper.class.getResource("/icons/VideoIcon.png"));
                    JOptionPane.showMessageDialog(null,
                            "Recording Stopped" + "  " + "File saved to: " + savePath,
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE,
                            customIcon);
                } else {
                    JOptionPane.showMessageDialog(null, "Error pulling file, please connect device: " + errorOutput.toString());
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
            ProcessBuilder takeScreenshot = new ProcessBuilder(adbExecutable, "shell", "screencap", "-p", "/sdcard/" + fileName + ".png");
            Process takeScreenshotProcess = takeScreenshot.start();
            takeScreenshotProcess.waitFor(); // Wait for the process to complete

            // Step 2: Pull the screenshot from the device to the Desktop
            ProcessBuilder pullScreenshot = new ProcessBuilder(adbExecutable, "pull", "/sdcard/" + fileName + ".png", savePath);
            Process pullScreenshotProcess = pullScreenshot.start();
            pullScreenshotProcess.waitFor(); // Wait for the process to complete

            // Step 3: Delete the screenshot from the device
            ProcessBuilder deleteScreenshot = new ProcessBuilder(adbExecutable, "shell", "rm", "/sdcard/" + fileName + ".png");
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
            ProcessBuilder processInputText = new ProcessBuilder(adbExecutable, "shell", "input", "text", text);
            Process process = processInputText.start();
            process.waitFor();

            // Pass the adb shell input keyevent KEYCODE_TAB command
            ProcessBuilder processKeyEventTab = new ProcessBuilder(adbExecutable, "shell", "input", "keyevent", "KEYCODE_TAB");
            Process processTab = processKeyEventTab.start();
            processTab.waitFor();
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
          ProcessBuilder processBuilder = new ProcessBuilder(adbExecutable, "shell", command);

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


    public static void crashScan() throws InterruptedException, IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String crashFileName = "Android" + timeStamp;
// Define the ADB devices command
        String adbDevicesCommand = adbExecutable + " logcat -b crash";
// Create the process builder for ADB devices
        ProcessBuilder adbProcessBuilder = new ProcessBuilder(adbDevicesCommand.split("\\s+"));
        adbProcessBuilder.redirectErrorStream(true);
        String directory = "/Desktop/";
        String fileType = "_crashed.txt";
        // String savePath = crashFileName;
        StringBuilder crashFile = new StringBuilder();
        crashFile.append(directory).append(crashFileName).append(fileType);
        File outputFile = new File(System.getProperty("user.home") + crashFile);
        adbProcessBuilder.redirectOutput(outputFile);
// Start the ADB devices process
        Process adbProcess = adbProcessBuilder.start();
        Thread.sleep(3000);
        adbProcess.destroy();

        int numLines = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(outputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                numLines++;
                if (numLines >= 3) {
                    break;
                }
            }

            if (numLines >= 3) {
                ProcessBuilder textEditProcessBuilder = new ProcessBuilder("open", "-e",
                        outputFile.getAbsolutePath());
                textEditProcessBuilder.start();
                ImageIcon customIcon = new ImageIcon(XcrunHelper.class.getResource("/icons/Caution.png"));

                JOptionPane.showMessageDialog(null,
                        "Crash Found" + "  " + "File saved to: " + crashFile,
                        "Crash Scan",
                        JOptionPane.INFORMATION_MESSAGE,
                        customIcon);

            } else {
                ProcessBuilder textEditProcessBuilder = new ProcessBuilder("rm",
                        outputFile.getAbsolutePath());
                textEditProcessBuilder.start();
                ImageIcon customIcon = new ImageIcon(XcrunHelper.class.getResource("/icons/Safe.png"));
                Image img = customIcon.getImage();
                Image scaledImg = img.getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH); // Example size (40x40)
                customIcon = new ImageIcon(scaledImg);

                JOptionPane.showMessageDialog(null,
                        "No Crash Found",
                        "Crash Scan",
                        JOptionPane.INFORMATION_MESSAGE,
                        customIcon);

            }
        }
    }

    public static void setLanguageToEnglish() {
        try {
            // Execute 'adb root' command
            ProcessBuilder processRoot = new ProcessBuilder(adbExecutable, "root");
            Process rootProcess = processRoot.start();
            rootProcess.waitFor();

            // Wait for 0.5 seconds
            Thread.sleep(500);

            // Execute 'adb shell "setprop persist.sys.locale fr-CA; stop; start"' command
            ProcessBuilder processLocale = new ProcessBuilder(adbExecutable, "shell", "setprop", "persist.sys.locale", "en-CA", ";", "stop", ";", "start");
            Process localeProcess = processLocale.start();
            localeProcess.waitFor();

                    } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Failed to change language to French: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void setLanguageToFrench() {
        try {
            // Execute 'adb root' command
            ProcessBuilder processRoot = new ProcessBuilder(adbExecutable, "root");
            Process rootProcess = processRoot.start();
            rootProcess.waitFor();

            // Wait for 0.5 seconds
            Thread.sleep(500);

            // Execute 'adb shell "setprop persist.sys.locale fr-CA; stop; start"' command
            ProcessBuilder processLocale = new ProcessBuilder(adbExecutable, "shell", "setprop", "persist.sys.locale", "fr-CA", ";", "stop", ";", "start");
            Process localeProcess = processLocale.start();
            localeProcess.waitFor();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Failed to change language to French: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
