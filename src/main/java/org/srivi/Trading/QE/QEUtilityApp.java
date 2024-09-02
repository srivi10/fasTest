package org.srivi.Trading.QE;

import javax.swing.*;
import java.awt.*;

public class QEUtilityApp {
    private JFrame frame;
    private HelpOptionsPanel helpPanel;

    public QEUtilityApp() {
        // Initialize the main frame
        frame = new JFrame("Home");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Disable the maximize button
        frame.setResizable(false);

        // Add header title banner strip
        addHeaderBanner(frame);

        // Set the custom font
        Font interFont = FontUtil.getInterFont(12f);

        // Label for device selection
        JLabel selectDeviceLabel = new JLabel("Select Device:");
        selectDeviceLabel.setFont(interFont);
        selectDeviceLabel.setBounds(150, 80, 120, 30);
        frame.add(selectDeviceLabel);

        // Button for Android option
        JButton androidButton = new JButton("Android");
        androidButton.setFont(interFont);
        androidButton.setBounds(50, 130, 120, 30);
        frame.add(androidButton);

        // Button for iOS option (Future Implementation)
        JButton iosButton = new JButton("iOS");
        iosButton.setFont(interFont);
        iosButton.setBounds(220, 130, 120, 30);
        frame.add(iosButton);

        // ActionListener for Android Button
        androidButton.addActionListener(e -> openAndroidUtilityScreen());

        // Placeholder ActionListener for iOS Button
        iosButton.addActionListener(e -> openIOSUtilityScreen());

        // Add HelpOptionsPanel after the frame is visible
        frame.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
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

    private void addHeaderBanner(JFrame frame) {
        // Create a panel for the banner
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Create gradient paint
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();
                Color startColor = Color.decode("#007AFF");
                Color endColor = startColor.darker(); // A darker shade of the same color
                GradientPaint gradient = new GradientPaint(0, 0, startColor, width, height, endColor);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, width, height);
            }
        };
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        headerPanel.setBounds(0, 0, frame.getWidth(), 40);

        // Create the fasTest label with "fas" and "Test" styles
        JLabel headerLabel = new JLabel();
        headerLabel.setForeground(Color.WHITE);

        // Set custom font for "fas"
        Font interFont = FontUtil.getInterFont(18f);
        Font boldFont = interFont.deriveFont(Font.BOLD);

        headerLabel.setFont(interFont);
        headerLabel.setText("<html><span style='font-size:18px;'>Fas</span><span style='font-weight:italic;font-size:18px;'>Test</span></html>");

        headerPanel.add(headerLabel);
        frame.add(headerPanel);
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
