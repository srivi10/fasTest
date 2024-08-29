package org.srivi.Trading.QE;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class iOSScreenRecordingGUI {

    private static final String UDID = "64535FDE-4F15-45ED-A5A2-5D2F0C229FE8";
    private static final String OUTPUT_FILE_PATH = "~/Desktop/heeevc.mp4";

    private Process recordingProcess;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new iOSScreenRecordingGUI().createAndShowGUI());
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("iOS Screen Recording");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 150);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        JButton startButton = new JButton("Start Recording");
        JButton stopButton = new JButton("Stop Recording");

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startRecording();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopRecording();
            }
        });

        frame.add(startButton);
        frame.add(stopButton);

        frame.setVisible(true);
    }

    private void startRecording() {
        // Use the HEVC codec for better compression
        String command = String.format("xcrun simctl io %s recordVideo --codec=hevc --force %s", UDID, OUTPUT_FILE_PATH);

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
            recordingProcess = processBuilder.start();
            System.out.println("Recording started with HEVC codec.");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error starting recording: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void stopRecording() {
        if (recordingProcess != null) {
            String pid = String.valueOf(recordingProcess.pid());
            String killCommand = "kill -2 " + pid;
            try {
                new ProcessBuilder("bash", "-c", killCommand).start();
                System.out.println("SIGINT signal sent to process " + pid);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error sending SIGINT to recording process: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        String command = String.format("xcrun simctl io %s recordVideo --stop", UDID);
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
            Process stopProcess = processBuilder.start();
            stopProcess.waitFor(3, TimeUnit.SECONDS); // Wait up to 3 seconds for the command to complete
            System.out.println("Recording stopped.");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error stopping recording: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
            JOptionPane.showMessageDialog(null, "Error stopping recording: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
