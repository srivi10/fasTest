package org.srivi.Trading;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DryRun extends JFrame {
    private JTextArea textArea;
    private JTextField packageField;
    private JButton startButton, stopButton;
    private boolean recording = false;
    private String fileName;

    public DryRun() {
        setTitle("Bug Step Recorder");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        textArea = new JTextArea();
        packageField = new JTextField(20);
        startButton = new JButton("Start Steps");
        stopButton = new JButton("Stop Steps");

        startButton.addActionListener(e -> startRecording());
        stopButton.addActionListener(e -> stopRecording());

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Package:"));
        topPanel.add(packageField);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.add(startButton);
        panel.add(stopButton);
        add(panel, BorderLayout.SOUTH);
    }

    private void startRecording() {
        recording = true;
        String packageName = packageField.getText().trim();
        fileName = "steps_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".txt";

        new Thread(() -> {
            try {
                while (recording && !Thread.currentThread().isInterrupted()) {
                    String activity = getCurrentActivity(packageName);
                    if (activity != null && !activity.isEmpty()) {
                        textArea.append("Activity: " + activity + "\n");
                    } else {
                        textArea.append("Activity: No activity found or could not be captured.\n");
                    }
                    Thread.sleep(2000); // Check every 2 seconds
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Set the interrupt flag
                System.out.println("Recording was interrupted.");
            }
        }).start();
    }

    private void stopRecording() {
        recording = false;
        saveToFile();
        openFile();
    }

    private String getCurrentActivity(String packageName) {
        try {
            String command = "adb shell dumpsys activity activities | grep mResumedActivity | grep " + packageName;
            System.out.println("Executing command: " + command);
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            return output.toString().trim();
        } catch (Exception e) {
            System.err.println("Error executing adb command: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private void saveToFile() {
        File file = new File(System.getProperty("user.home") + "/Desktop", fileName);

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(textArea.getText());
        } catch (IOException e) {
            System.err.println("Error saving file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void openFile() {
        File file = new File(System.getProperty("user.home") + "/Desktop", fileName);

        if (!file.exists()) {
            System.out.println("File does not exist: " + file.getAbsolutePath());
            return;
        }

        try {
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            System.err.println("Error opening file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DryRun().setVisible(true);
        });
    }
}
