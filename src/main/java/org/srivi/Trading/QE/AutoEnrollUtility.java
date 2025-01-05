package org.srivi.Trading.QE;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.srivi.Trading.QE.AutoEnrollBrowserHelper;
import org.srivi.Trading.BrowserHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AutoEnrollUtility extends JFrame {
    private JFrame mainAppFrame;
    private HelpOptionsPanel helpOptionsPanel;
    private JTextArea consoleTextArea;
    private WebDriver driver;
    private AutoEnrollBrowserHelper autoEnrollBrowserHelper;
    private JCheckBox runInBackgroundCheckBox;

    public AutoEnrollUtility() {
        // Initialize the Auto Enroll frame
        setTitle("Auto Enroll Utility");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        // Add label for Account Number
        JLabel accountNumberLabel = new JLabel("Account Number:");
        accountNumberLabel.setBounds(30, 50, 120, 30);
        add(accountNumberLabel);

        // Add "Run in Background" checkbox
        runInBackgroundCheckBox = new JCheckBox("Run in Background");
        runInBackgroundCheckBox.setBounds(30, 80, 150, 30);
        add(runInBackgroundCheckBox);


        // Add text field for Account Number
        JTextField accountNumberField = new JTextField();
        accountNumberField.setBounds(150, 50, 150, 30);
        add(accountNumberField);

        // Add label for Console Text Area
        JLabel consoleLabel = new JLabel("Status:");
        consoleLabel.setBounds(30, 110, 120, 30);
        add(consoleLabel);

        consoleTextArea = new JTextArea();
        consoleTextArea.setEditable(false); // Set to false to prevent typing
        consoleTextArea.setLineWrap(true); // Enable line wrapping
        consoleTextArea.setWrapStyleWord(true); // Wrap at word boundaries
        consoleTextArea.setBounds(30, 140, 320, 120);
        consoleTextArea.setCaretPosition(0); // Ensure cursor starts at the beginning
        add(consoleTextArea);

        autoEnrollBrowserHelper = new AutoEnrollBrowserHelper(consoleTextArea);

        // Add play button
        ImageIcon playIcon = new ImageIcon(getClass().getClassLoader().getResource("icons/PlayIcon.png"));
        Image scaledPlayImage = playIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        JButton playButton = new JButton(new ImageIcon(scaledPlayImage));
        playButton.setToolTipText("Launch Browser");
        playButton.setBounds(310, 50, 30, 30);
        playButton.setBorderPainted(false);
        playButton.setContentAreaFilled(false);
        playButton.setFocusPainted(false);
        playButton.setOpaque(false);

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consoleTextArea.setText("");
                boolean headless = runInBackgroundCheckBox.isSelected();
                autoEnrollBrowserHelper.launchBrowser(headless);
            }
        });
        add(playButton);

        // Add reset button
        ImageIcon resetIcon = new ImageIcon(getClass().getClassLoader().getResource("icons/CleanIcon.png"));
        Image scaledResetImage = resetIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        JButton resetButton = new JButton(new ImageIcon(scaledResetImage));
        resetButton.setToolTipText("Reset Console");
        resetButton.setBounds(350, 50, 30, 30);
        resetButton.setBorderPainted(false);
        resetButton.setContentAreaFilled(false);
        resetButton.setFocusPainted(false);
        resetButton.setOpaque(false);
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accountNumberField.setText("");
                consoleTextArea.setText("");
                runInBackgroundCheckBox.setSelected(false);
            }
        });
        add(resetButton);

        // Add back button
        ImageIcon backIcon = new ImageIcon(getClass().getClassLoader().getResource("icons/MasterBackButton.png"));
        Image scaledBackImage = backIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledBackImage);

        JLabel backLabel = new JLabel("Back", scaledIcon, SwingConstants.LEFT);
        backLabel.setBounds(10, 10, 100, 30);
        backLabel.setFont(FontUtil.getInterFont(14f));
        backLabel.setIconTextGap(5);
        backLabel.setHorizontalAlignment(SwingConstants.LEFT);
        backLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                goBackToMainApp();
            }
        });

        add(backLabel);

        // Add HelpOptionsPanel
        helpOptionsPanel = new HelpOptionsPanel(getWidth(), getHeight());
        add(helpOptionsPanel);

        // Add ComponentListener to handle resizing
        // Add ComponentListener to handle resizing
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Reposition HelpOptionsPanel
                helpOptionsPanel.setBounds(0, getHeight() - 100, getWidth(), 100);
            }
        });


        setVisible(true);
    }

    // Method to set the main app frame
    public void setMainAppFrame(JFrame mainAppFrame) {
        this.mainAppFrame = mainAppFrame;
    }

    // Method to go back to the main app
    private void goBackToMainApp() {
        if (mainAppFrame != null) {
            mainAppFrame.setVisible(true);
        }
        dispose();
    }

    // Method to launch the browser using Selenium
    private void launchBrowser() {
        try {
            ChromeOptions options = new ChromeOptions();
            options.setBinary("/Applications/Google Chrome.app/Contents/MacOS/Google Chrome");
            driver = new ChromeDriver(options);
            driver.get("https://www.google.com");
            consoleTextArea.setForeground(Color.BLACK);
            consoleTextArea.setText("Launch Success");
             } catch (Exception e) {
            consoleTextArea.setForeground(Color.RED);
            consoleTextArea.setText("Browser launch Fail");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AutoEnrollUtility::new);
    }
}