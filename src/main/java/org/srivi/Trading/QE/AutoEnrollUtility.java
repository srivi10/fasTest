package org.srivi.Trading.QE;

import org.openqa.selenium.WebDriver;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class AutoEnrollUtility extends JFrame {
    private JFrame mainAppFrame;
    private HelpOptionsPanel helpOptionsPanel;
    private JTextPane consoleTextPane;
    private WebDriver driver;
    private AutoEnrollBrowserHelper autoEnrollBrowserHelper;
    private JCheckBox runInBackgroundCheckBox;
    String accountNumber;
    File autoEnrollFolder;
    private StyledDocument doc;

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

        consoleTextPane = new JTextPane();
        consoleTextPane.setEditable(false); // Set to false to prevent typing
        // Wrap at word boundaries
        // Wrap the consoleTextPane in a JScrollPane
        JScrollPane scrollPane = new JScrollPane(consoleTextPane);
        scrollPane.setBounds(30, 140, 320, 120);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane);

        autoEnrollBrowserHelper = new AutoEnrollBrowserHelper(consoleTextPane);

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
        playButton.setEnabled(false);

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consoleTextPane.setText("");
                boolean headless = runInBackgroundCheckBox.isSelected();
                accountNumber = accountNumberField.getText().trim();
                autoEnrollFolder = createAutoEnrollFolder(accountNumber);
                //autoEnrollBrowserHelper.launchBrowser(accountNumber, headless, autoEnrollFolder);
                //autoEnrollBrowserHelper.performLogin("Admin", "admin123",accountNumber,autoEnrollFolder);
                boolean launchSuccess = autoEnrollBrowserHelper.launchBrowser(accountNumber, headless, autoEnrollFolder);
                if (!launchSuccess) {
                    updateConsole("Failed to launch browser. Aborting.", Color.RED);
                    return; // Stop execution if browser launch fails
                }

                boolean loginSuccess = autoEnrollBrowserHelper.performLogin("Admin", "admin123", accountNumber, autoEnrollFolder);
                if (!loginSuccess) {
                    updateConsole("Failed to perform login. Aborting.", Color.RED);
                    return; // Stop execution if login fails
                }

            }
        });
        add(playButton);

        // Add reset button
        ImageIcon resetIcon = new ImageIcon(getClass().getClassLoader().getResource("icons/CleanIcon.png"));
        Image scaledResetImage = resetIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        JButton resetButton = new JButton(new ImageIcon(scaledResetImage));
        resetButton.setToolTipText("Reset");
        resetButton.setBounds(350, 50, 30, 30);
        resetButton.setBorderPainted(false);
        resetButton.setContentAreaFilled(false);
        resetButton.setFocusPainted(false);
        resetButton.setOpaque(false);
        resetButton.setEnabled(false);
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accountNumberField.setText("");
                consoleTextPane.setText("");
                runInBackgroundCheckBox.setSelected(false);
            }
        });
        add(resetButton);
// Add DocumentListener to accountNumberField
        accountNumberField.getDocument().addDocumentListener(new DocumentListener() {
            private void updateButtons() {
                boolean isEnabled = !accountNumberField.getText().trim().isEmpty();
                playButton.setEnabled(isEnabled);
                resetButton.setEnabled(isEnabled);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                updateButtons();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateButtons();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateButtons();
            }
        });

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

    // Method to create "AutoEnroll" folder in Downloads and a folder with the account number
    private File createAutoEnrollFolder(String accountNumber) {
        // Get the path to the Downloads folder
        Path downloadsFolder = Paths.get(System.getProperty("user.home"), "Downloads", "AutoEnroll");

        // Create AutoEnroll folder if it doesn't exist
        if (!Files.exists(downloadsFolder)) {
            try {
                Files.createDirectory(downloadsFolder);
            } catch (IOException e) {
                e.printStackTrace();
                updateConsole("Failed to create AutoEnroll folder", Color.RED);
                return null;
            }
        }

        // Create folder with account number
        Path accountFolder = downloadsFolder.resolve(accountNumber);
        if (Files.exists(accountFolder)) {
            // Delete the existing folder if it exists
            try {
                Files.walk(accountFolder)
                        .sorted(Comparator.reverseOrder()) // To delete files before directories
                        .map(Path::toFile)
                        .forEach(File::delete);
            } catch (IOException e) {
                e.printStackTrace();
                updateConsole("Failed to delete existing account folder", Color.RED);
                return null;
            }
        }

        // Create the new folder
        try {
            Files.createDirectory(accountFolder);
        } catch (IOException e) {
            e.printStackTrace();
            updateConsole("Failed to create account folder", Color.RED);
            return null;
        }

        return accountFolder.toFile();
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

    private void updateConsole(String message, Color color) {
        Style style = consoleTextPane.addStyle("Style", null);
        StyleConstants.setForeground(style, color);
        try {
            doc.insertString(doc.getLength(), message + "\n", style);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(AutoEnrollUtility::new);
    }
}