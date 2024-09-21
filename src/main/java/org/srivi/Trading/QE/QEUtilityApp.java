package org.srivi.Trading.QE;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

public class QEUtilityApp {
    private JFrame frame;
    private HelpOptionsPanel helpPanel;
    private JLabel loadingLabel;       // Label to show loading text
    private JLabel loadingIconLabel;   // Label to show loading icon
    private JButton androidButton, iosButton;
    private JLabel selectDeviceLabel;
    private JLabel versionLabel;

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
        Font interFont = FontUtil.getInterFont(13f);
        Font BOLD_FONT = interFont.deriveFont(Font.BOLD);
        addVersionLabel(frame);

        // Label for device selection
        selectDeviceLabel = new JLabel("Select Device:");
        selectDeviceLabel.setFont(interFont);
        selectDeviceLabel.setBounds(150, 80, 120, 30);
        frame.add(selectDeviceLabel);

        // Button for Android option
        androidButton = new JButton("Android");
        androidButton.setFont(interFont);
        androidButton.setBounds(50, 130, 120, 30);
        frame.add(androidButton);

        // Button for iOS option (Future Implementation)
        iosButton = new JButton("iOS");
        iosButton.setFont(interFont);
        iosButton.setBounds(220, 130, 120, 30);
        frame.add(iosButton);

        // Label for the loading icon
        loadingIconLabel = new JLabel();
        ImageIcon loadingIcon = new ImageIcon(getClass().getResource("/icons/MasterLoading.png"));
        loadingIconLabel.setIcon(loadingIcon);
        loadingIconLabel.setBounds(170, 120, 84, 84);  // Adjust the bounds as needed
        loadingIconLabel.setVisible(false);  // Hide initially
        frame.add(loadingIconLabel);

        // Label for the loading text
        loadingLabel = new JLabel("Loading...");
        loadingLabel.setForeground(new Color(0, 122, 255));
        loadingLabel.setFont(interFont);
        loadingLabel.setBounds(167, 180, 160, 30);  // Adjust the bounds as needed
        loadingLabel.setVisible(false);  // Hide initially
        //frame.add(loadingLabel);

        // ActionListener for Android Button
        androidButton.addActionListener(e -> {
            showLoadingMessage("Loading Android Utility...");
            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    Thread.sleep(1000); // Simulate delay
                    return null;
                }

                @Override
                protected void done() {
                    openAndroidUtilityScreen();
                    hideLoadingMessage();
                }
            }.execute();
        });

        // ActionListener for iOS Button
        iosButton.addActionListener(e -> {
            showLoadingMessage("Loading iOS Utility...");
            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    Thread.sleep(1000); // Simulate delay
                    return null;
                }

                @Override
                protected void done() {
                    openIOSUtilityScreen();
                    hideLoadingMessage();
                }
            }.execute();
        });

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
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        headerPanel.setBounds(0, 0, frame.getWidth(), 40);

        // Create the fasTest label with "fas" and "Test" styles
        JLabel headerLabel = new JLabel();
        headerLabel.setForeground(Color.WHITE);

        // Set custom font for "fas"
        Font interFont = FontUtil.getInterFont(24f);

        headerLabel.setFont(interFont);
        headerLabel.setText("fas" + "Test");

        headerPanel.add(headerLabel);
        frame.add(headerPanel);
    }

    private void addVersionLabel(JFrame frame) {
        // Load version from file
        String version = loadVersionFromFile();

        // Create and style the version label
        versionLabel = new JLabel("Version  " + version);
        versionLabel.setFont(FontUtil.getInterFont(12f)); // Adjust the font size as needed
        versionLabel.setForeground(Color.DARK_GRAY);        // Default text color
        versionLabel.setBounds(315, 40, 150, 30); // Adjust size if needed

        // Set the tooltip text
        versionLabel.setToolTipText("Click here to download latest fasTest app");

        // Add a mouse listener to handle clicks and color changes
        versionLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://www.google.ca/"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                versionLabel.setForeground(new Color(0x007AFF)); // Change to link blue on hover

                versionLabel.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Change cursor to hand
            }

            @Override
            public void mouseExited(MouseEvent e) {
                versionLabel.setForeground(Color.DARK_GRAY); // Revert to default color
            }
        });

        // Add the version label to the frame
        frame.add(versionLabel);
    }

    private String loadVersionFromFile() {
        String version = "1.0.0"; // Default version in case file read fails
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream("versioning/version.txt")))) {
            version = reader.readLine();
        } catch (IOException | NullPointerException e) {
            System.err.println("Could not read version.txt, defaulting to " + version);
        }
        return version;
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

    // Method to show the loading message, icon, and hide buttons
    private void showLoadingMessage(String message) {
        loadingLabel.setText(message);
        loadingLabel.setVisible(true);
        loadingIconLabel.setVisible(true);

        // Hide buttons and non-heading components
        androidButton.setVisible(false);
        iosButton.setVisible(false);
        selectDeviceLabel.setVisible(false);

        if (helpPanel != null) {
            helpPanel.setVisible(false);
        }
    }

    // Method to hide the loading message and icon, and show buttons
    private void hideLoadingMessage() {
        loadingLabel.setVisible(false);
        loadingIconLabel.setVisible(false);

        // Show buttons and non-heading components again
        androidButton.setVisible(true);
        iosButton.setVisible(true);
        selectDeviceLabel.setVisible(true);

        if (helpPanel != null) {
            helpPanel.setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(QEUtilityApp::new);
    }
}
