package org.srivi.Trading.QE;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class QEUtilityApp {
    private JFrame frame;
    private HelpOptionsPanel helpPanel;

    public QEUtilityApp() {
        // Initialize the main frame
        frame = new JFrame("QE Utility App");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Set the custom font
        Font interFont = FontUtil.getInterFont(12f);

        // Label for device selection
        JLabel selectDeviceLabel = new JLabel("Select Device:");
        selectDeviceLabel.setFont(interFont);
        selectDeviceLabel.setBounds(150, 30, 120, 30);
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

        // ActionListener for Android Button
        androidButton.addActionListener(e -> openAndroidUtilityScreen());

        // Placeholder ActionListener for iOS Button
        iosButton.addActionListener(e -> openIOSUtilityScreen());

        // Add HelpOptionsPanel after the frame is visible
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (helpPanel == null) {
                    helpPanel = new HelpOptionsPanel(frame.getWidth(), frame.getHeight());
                    frame.add(helpPanel);
                } else {
                    // Update bounds if the frame is resized
                    helpPanel.setBounds(0, frame.getHeight() - 100, frame.getWidth(), 100);
                }
                frame.revalidate();
                frame.repaint();
            }
        });

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
        iosUtility.setMainAppFrame(frame); // Pass the main frame to iOSUtility
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(QEUtilityApp::new);
    }
}
