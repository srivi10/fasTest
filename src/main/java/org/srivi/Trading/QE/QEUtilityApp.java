package org.srivi.Trading.QE;

import javax.swing.*;

public class QEUtilityApp {
    private JFrame frame;

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
        androidButton.addActionListener(e -> openAndroidUtilityScreen());

        // Placeholder ActionListener for iOS Button
        iosButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "iOS Utility not implemented yet."));

        frame.setVisible(true);
    }

    private void openAndroidUtilityScreen() {
        frame.dispose();
        new AndroidUtility();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(QEUtilityApp::new);
    }
}