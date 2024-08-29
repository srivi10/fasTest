package org.srivi.Trading.QE;

import javax.swing.*;
import java.awt.*;

public class QEUtilityApp {
    private JFrame frame;

    public QEUtilityApp() {
        // Initialize the main frame
        frame = new JFrame("QE Utility Appp");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);


        // Set the custom font
        Font interFont = FontUtil.getInterFont(12f);

        // Label for device selection
        JLabel selectDeviceLabel = new JLabel("Select Device:");
        selectDeviceLabel.setFont(interFont);
        selectDeviceLabel.setBounds(150, 30, 100, 30);
        frame.add(selectDeviceLabel);

        // Button for Android option
        JButton androidButton = new JButton("Android");
        androidButton.setFont(interFont);
        androidButton.setBounds(50, 80, 120, 30);
        frame.add(androidButton);

        // Button for iOS option (Future Implementation)
        JButton iosButton = new JButton("iOS");
        iosButton.setFont(interFont);
        iosButton.setBounds(220, 80, 120, 30);
        frame.add(iosButton);
        //iosButton.setEnabled(false);

        // ActionListener for Android Button
        androidButton.addActionListener(e -> openAndroidUtilityScreen());

        // Placeholder ActionListener for iOS Button
        // iosButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "iOS Utility not implemented yet."));
        iosButton.addActionListener(e -> openIOSUtilityScreen());

        frame.setVisible(true);
    }

    private void openAndroidUtilityScreen() {
        frame.setVisible(false); // Hide the main frame
        AndroidUtility androidUtility = new AndroidUtility();
        androidUtility.setMainAppFrame(frame); // Pass the main frame to AndroidUtility
    }

    private void openIOSUtilityScreen() {
        frame.setVisible(false); // Hide the main frame
        iOSUtility iosUtility = new iOSUtility();
        iosUtility.setMainAppFrame(frame); // Pass the main frame to iOSUtilityScreen
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(QEUtilityApp::new);
    }
}
