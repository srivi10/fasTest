package org.srivi.Trading;

import org.srivi.Trading.QE.*;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class AccountSelectionGUI extends JFrame {

    private JFrame mainAppFrame;
    private JComboBox<String> accountHolderComboBox;
    private JComboBox<String> accountTypeComboBox;
    private JComboBox<String> transferComboBox;
    private JComboBox<String> offersComboBox;
    private JComboBox<String> paymentPlanComboBox;
    private JComboBox<String> creditLimitComboBox;
    private JButton submitButton;
    private JButton resetButton;
    private JButton backButton; // Back button
    private JTextField usernameField;
    private JTextField passwordField;
    private JTextField accountHolderField;
    private JTextField accountTypeField;
    private JCheckBox transferCheckBox;
    private JCheckBox offersCheckBox;

    private JCheckBox paymentPlanCheckBox;
    private JCheckBox creditLimitCheckBox;
    private JLabel loadingLabel;
    private JLabel loadingIconLabel;
    private JTextArea errorTextArea;
    private JComboBox<String> websiteComboBox;
    private BrowserHelper browserHelper;
    private JLabel chromeLoadingIconLabel;
    private JLabel chromeLoadingLabel;


    private boolean isLoading = false;
    private boolean isChromeLoading = false;
    private HelpOptionsPanel helpOptionsPanel;

    public AccountSelectionGUI(JFrame mainAppFrame) {
        this.mainAppFrame = mainAppFrame;
        setupUI();
    }

    private void setupUI() {
        setTitle("Account Selection");
        setSize(600, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        setResizable(false);


        // Set the custom font
        Font interFont = FontUtil.getInterFont(12f);

        //Font interFont = FontUtil.getInterFont(12f);

        // Use the BackButtonUtil to create and add the back label
        JLabel backLabel = BackButtonUtil.createBackLabel(this);
        add(backLabel);

        accountHolderComboBox = new JComboBox<>(new String[]{"Single", "Multi"});
        accountTypeComboBox = new JComboBox<>(new String[]{"PCH", "Au"});
        transferComboBox = new JComboBox<>(new String[]{"Select", "Eligible", "Ineligible"});
        offersComboBox = new JComboBox<>(new String[]{"Select", "Eligible", "Ineligible"});
        paymentPlanComboBox = new JComboBox<>(new String[]{"Select", "Eligible", "Ineligible"});
//herecombo
        creditLimitComboBox =new JComboBox<>(new String[]{"Select", "Eligible", "Ineligible"});
        submitButton = new JButton("Submit");
        resetButton = new JButton("Reset");

        usernameField = new JTextField();
        passwordField = new JTextField();
        accountHolderField = new JTextField();
        accountTypeField = new JTextField();
        transferCheckBox = new JCheckBox();
        offersCheckBox = new JCheckBox();
        //here
        paymentPlanCheckBox = new JCheckBox();
        creditLimitCheckBox = new JCheckBox();

        loadingIconLabel = new JLabel();
        ImageIcon loadingIcon = new ImageIcon(getClass().getResource("/icons/MasterLoading.png"));
        loadingIconLabel.setIcon(loadingIcon);
        loadingIconLabel.setVisible(false);

        loadingLabel = new JLabel("Loading...");
        loadingLabel.setForeground(new Color(0, 122, 255));
        loadingLabel.setVisible(false);

        errorTextArea = new JTextArea();
        errorTextArea.setEditable(false);
        errorTextArea.setForeground(Color.RED);
        errorTextArea.setLineWrap(true);
        errorTextArea.setWrapStyleWord(true);

        // Add components to frame
        add(new JLabel("Account Holder:")).setBounds(20, 50, 120, 30);
        add(accountHolderComboBox).setBounds(150, 50, 150, 30);
        add(new JLabel("Account Type:")).setBounds(20, 90, 120, 30);
        add(accountTypeComboBox).setBounds(150, 90, 150, 30);
        add(new JLabel("Transfer:")).setBounds(20, 130, 120, 30);
        add(transferComboBox).setBounds(150, 130, 150, 30);
        add(new JLabel("Offers:")).setBounds(20, 170, 120, 30);
        add(offersComboBox).setBounds(150, 170, 150, 30);
        add(new JLabel("Payment Plan:")).setBounds(20, 210, 120, 30);
        add(paymentPlanComboBox).setBounds(150, 210, 150, 30);
//herecombo
        add(new JLabel("Credit Limit:")).setBounds(320, 50, 120, 30);
        add(creditLimitComboBox).setBounds(420, 50, 150, 30);

        add(submitButton).setBounds(50, 260, 100, 30);
        add(resetButton).setBounds(170, 260, 100, 30);

        add(new JLabel("Username:")).setBounds(20, 300, 120, 30);
        add(usernameField).setBounds(150, 300, 200, 30);

        // Add keyboard icon next to Username field
        JLabel usernameKeyboardLabel = createIconLabel("/icons/Enter.png");
        usernameKeyboardLabel.setToolTipText("Quickly Enter Text to Android Devices");
        usernameKeyboardLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ADBHelper.passTextToEmulator(usernameField.getText());
            }
        });
        add(usernameKeyboardLabel).setBounds(360, 300, 30, 30); // Adjust bounds as needed

        add(new JLabel("Password:")).setBounds(20, 340, 120, 30);
        add(passwordField).setBounds(150, 340, 200, 30);

        // Add keyboard icon next to Password field
        JLabel passwordKeyboardLabel = createIconLabel("/icons/Enter.png");
        passwordKeyboardLabel.setToolTipText("Quickly Enter Text to Android Devices");
        passwordKeyboardLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ADBHelper.passTextToEmulator(passwordField.getText());
            }
        });
        add(passwordKeyboardLabel).setBounds(360, 340, 30, 30); // Adjust bounds as needed

        // Add copy icon next to Username field
        JLabel usernameCopyLabel = createIconLabel("/icons/CopyIcon.png");
        usernameCopyLabel.setToolTipText("Copy Username to Clipboard");

        ImageIcon usernameTickIcon = new ImageIcon(getClass().getResource("/icons/TickIcon.png"));
        Image usernameScaledTickImage = usernameTickIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        JLabel usernameTickIconLabel = new JLabel(new ImageIcon(usernameScaledTickImage));
        usernameTickIconLabel.setBounds(420, 300, 30, 30); // Adjust bounds as needed
        usernameTickIconLabel.setVisible(false);
        add(usernameTickIconLabel);

        usernameCopyLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                copyToClipboard(usernameField.getText());
                usernameTickIconLabel.setVisible(true);

                Timer timer = new Timer(1000, evt -> {
                    usernameTickIconLabel.setVisible(false);
                    repaint();
                });
                timer.setRepeats(false);
                timer.start();
            }
        });
        add(usernameCopyLabel).setBounds(390, 300, 30, 30); // Adjust bounds as needed
       // Add copy icon next to Password field
        
        JLabel passwordCopyLabel = createIconLabel("/icons/CopyIcon.png");
        passwordCopyLabel.setToolTipText("Copy Password to Clipboard");

        ImageIcon passwordTickIcon = new ImageIcon(getClass().getResource("/icons/TickIcon.png"));
        Image passwordScaledTickImage = usernameTickIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        JLabel passwwordTickIconLabel = new JLabel(new ImageIcon(usernameScaledTickImage));
        passwwordTickIconLabel.setBounds(420, 340, 30, 30); // Adjust bounds as needed
        passwwordTickIconLabel.setVisible(false);
        add(passwwordTickIconLabel);

        passwordCopyLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                copyToClipboard(passwordField.getText());
                passwwordTickIconLabel.setVisible(true);

                Timer timer = new Timer(1000, evt -> {
                    passwwordTickIconLabel.setVisible(false);
                    repaint();
                });
                timer.setRepeats(false);
                timer.start();
            }
        });
        add(passwordCopyLabel).setBounds(390, 340, 30, 30); // Adjust bounds as needed

        add(new JLabel("Account Holder")).setBounds(20, 380, 120, 30);
        add(accountHolderField).setBounds(150, 380, 200, 30);
        add(new JLabel("Account Type")).setBounds(20, 420, 120, 30);
        add(accountTypeField).setBounds(150, 420, 200, 30);
        add(new JLabel("Transfer")).setBounds(20, 460, 120, 30);
        add(transferCheckBox).setBounds(150, 460, 30, 30);
        add(new JLabel("Offers")).setBounds(200, 460, 120, 30);
        add(offersCheckBox).setBounds(320, 460, 30, 30);
        add(new JLabel("Payment Plan")).setBounds(370, 460, 120, 30);
        add(paymentPlanCheckBox).setBounds(490, 460, 30, 30);
        //here
        add(new JLabel("Credit Limit")).setBounds(20, 500, 120, 30);
        add(creditLimitCheckBox).setBounds(150, 500, 200, 30);

        add(loadingIconLabel).setBounds(450, 250, 45, 40); // Position it above the loadingLabel

        add(loadingLabel).setBounds(450, 300, 100, 30);
        add(errorTextArea).setBounds(20, 550, 380, 40);

        ImageIcon addAccountIcon = new ImageIcon(getClass().getResource("/icons/AddAccount1.png"));

        // Scale the image
        Image scaledImage = addAccountIcon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);

        // Create a new ImageIcon from the scaled image
        ImageIcon smoothIcon = new ImageIcon(scaledImage);

        // Create a JLabel with the smoothed icon
        JLabel iconLabel = new JLabel(smoothIcon);
        iconLabel.setBounds(410, 555, 35, 35); // Position and size of the icon
        iconLabel.setToolTipText("Click here to request for Adding Accounts"); // Add tooltip
        add(iconLabel);

        // Add mouse listener to the icon
        iconLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    // Open the URL in Safari
                    Desktop.getDesktop().browse(new URI("https://www.google.com"));
                } catch (IOException | URISyntaxException ex) {
                    ex.printStackTrace();
                }
            }
        });
        JLabel otpLabel = new JLabel("OTP Helper:");
        //otpLabel.setFont(interFont);
        otpLabel.setBounds(20, 600, 200, 30);
        add(otpLabel);

        ImageIcon otpHelperIcon = new ImageIcon(getClass().getClassLoader().getResource("icons/OTP.png"));
        Image scaledOtpImage = otpHelperIcon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
        JButton otpHelperButton = new JButton(new ImageIcon(scaledOtpImage));
        otpHelperButton.setToolTipText("Quickly Enter Stub OTP to Android Devices");
        otpHelperButton.setBounds(100, 595, 35, 40);
        otpHelperButton.setBorderPainted(false);
        otpHelperButton.setContentAreaFilled(false);
        otpHelperButton.setFocusPainted(false);
        otpHelperButton.setOpaque(false);
        add(otpHelperButton);

        otpHelperButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ADBHelper.passTextToEmulator("123456");
            }
        });

        JLabel launchTextLabel = new JLabel("Launch Website");
        launchTextLabel.setBounds(20, 640, 150, 30);
        add(launchTextLabel);

        ImageIcon cautionIcon = new ImageIcon(getClass().getClassLoader().getResource("icons/Tips.png"));
        Image scaledCautionImage = cautionIcon.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH);
        ImageIcon scaledCautionIcon = new ImageIcon(scaledCautionImage);

        JLabel cautionIconLabel = new JLabel(scaledCautionIcon);
        cautionIconLabel.setToolTipText("Click here to Learn More");
        cautionIconLabel.setBounds(120, 630, 35, 35);
        add(cautionIconLabel);

        cautionIconLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String message = "Browser Automation Instructions:\n\n" +
                        "Please select an account from the above account selection.\n" +
                        "1) If an account is not selected, it will load the MBNA online site and will not auto-login.\n" +
                        "2) For the very first time, it takes 7-10 seconds to download the Selenium driver.\n" +
                        "3) Subsequent attempts will take less than 3 seconds to start automation.";
                JOptionPane.showMessageDialog(null, message, "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        });

// Dropdown for website selection
        websiteComboBox = new JComboBox<>(new String[]{"Google", "Facebook", "YouTube"});
        websiteComboBox.setBounds(180, 640, 100, 30);
        add(websiteComboBox);

        ImageIcon chromeIcon = new ImageIcon(getClass().getClassLoader().getResource("icons/ChromeIcon.png"));
        Image scaledChromeImage = chromeIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon scaledChromeIcon = new ImageIcon(scaledChromeImage);

        JLabel chromeIconLabel = new JLabel(scaledChromeIcon);
        chromeIconLabel.setToolTipText("Open selected Env in Chrome");
        chromeIconLabel.setBounds(290, 635, 35, 35);

        chromeLoadingIconLabel = new JLabel();
        ImageIcon chromeLoadingIcon = new ImageIcon(getClass().getResource("/icons/MasterLoading.png"));
        chromeLoadingIconLabel.setIcon(chromeLoadingIcon);
        chromeLoadingIconLabel.setVisible(false);
        add(chromeLoadingIconLabel).setBounds(390, 625, 45, 40); // Position it next to the Safari icon

        chromeLoadingLabel = new JLabel("Loading...");
        chromeLoadingLabel.setForeground(new Color(0, 122, 255));
        chromeLoadingLabel.setVisible(false);
        add(chromeLoadingLabel).setBounds(390, 675, 100, 30);

        chromeIconLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (isChromeLoading) return;  // Prevent action if loading is already active

                isChromeLoading = true;  // Set loading flag
                chromeLoadingIconLabel.setVisible(true);
                chromeLoadingLabel.setVisible(true);

                Timer timer = new Timer(500, evt -> {
                    try {
                        // Initialize BrowserHelper with Chrome
                        browserHelper = new BrowserHelper(BrowserHelper.BrowserType.CHROME);
                        String selectedSite = (String) websiteComboBox.getSelectedItem();
                        if (selectedSite != null) {
                            String url = getUrlForSite(selectedSite);
                            boolean launchSuccess = browserHelper.launchWebsite(url);
                            System.out.println("Launch Success: " + launchSuccess);
                            if (!launchSuccess) {
                                JOptionPane.showMessageDialog(null, "Failed to launch browser. Aborting.", "Error", JOptionPane.ERROR_MESSAGE);
                                return; // Stop execution if browser launch fails
                            }
                            browserHelper.inputCredentials(usernameField.getText(), passwordField.getText());
                        }
                    } finally {
                        // Hide loading icon and label
                        chromeLoadingIconLabel.setVisible(false);
                        chromeLoadingLabel.setVisible(false);
                        isChromeLoading = false;  // Reset loading flag

                        ((Timer) evt.getSource()).stop();  // Stop the timer after executing
                    }
                });

                timer.setRepeats(false);  // Ensure the timer only runs once
                timer.start();  // Start the timer
            }
        });
        add(chromeIconLabel);

// Safari Icon
        ImageIcon safariIcon = new ImageIcon(getClass().getClassLoader().getResource("icons/SafariIcon.png"));
        Image scaledSafariImage = safariIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon scaledSafariIcon = new ImageIcon(scaledSafariImage);

        JLabel safariIconLabel = new JLabel(scaledSafariIcon);
        safariIconLabel.setToolTipText("Open selected Env in Safari");
        safariIconLabel.setBounds(330, 635, 35, 35); // Position next to Chrome icon
        safariIconLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    // Initialize BrowserHelper with Safari
                    browserHelper = new BrowserHelper(BrowserHelper.BrowserType.SAFARI);
                    String selectedSite = (String) websiteComboBox.getSelectedItem();
                    if (selectedSite != null) {
                        String url = getUrlForSite(selectedSite);
                        browserHelper.launchWebsite(url);
                        browserHelper.inputCredentials(usernameField.getText(), passwordField.getText());
                    }
                } catch (org.openqa.selenium.SessionNotCreatedException ex) {
                    // Show error message pop-up
                    JOptionPane.showMessageDialog(
                            AccountSelectionGUI.this, // The parent component
                            "Could not start a new session. Please enable 'Allow remote automation' in Safari settings.\n" +
                                    "To enable this option:\n" +
                                    "1. Open Safari.\n" +
                                    "2. Go to Safari > Settings > Advanced.\n" +
                                    "3. Select features for Web Developers.\n"+
                                    "4. Click Developer TAB.\n"+
                                    "3. Enable 'Allow remote automation'.",
                            "Safari Automation Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        //add(safariIconLabel);


        submitButton.addActionListener(e -> {
            if (isLoading) return;  // Prevent action if loading is already active

            // Clear fields before fetching
            usernameField.setText("");
            passwordField.setText("");
            clearTextFieldsAndCheckBoxes();

            // Show loading label and fetch accounts after delay
            loadingIconLabel.setVisible(true);
            loadingLabel.setVisible(true);
            submitButton.setEnabled(false);
            new Timer(1000, evt -> {
                fetchAndDisplayAccounts();
                loadingIconLabel.setVisible(false);
                loadingLabel.setVisible(false);
                submitButton.setEnabled(true);
            }).start();
        });

        resetButton.addActionListener(e -> resetFields());
        helpOptionsPanel = new HelpOptionsPanel(getWidth(), getHeight());
        add(helpOptionsPanel);

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

    private JLabel createIconLabel(String iconPath) {
        JLabel label = new JLabel();
        URL iconURL = getClass().getResource(iconPath);
        if (iconURL != null) {
            ImageIcon icon = new ImageIcon(iconURL);
            label.setOpaque(true); // Make the label opaque to display the background color
            Image image = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH); // Scale to be compact
            label.setIcon(new ImageIcon(image));
        } else {
            System.err.println("Icon not found: " + iconPath);
        }
        return label;
    }

    private void fetchAndDisplayAccounts() {
        // Use ClassLoader to get the resource from the JAR
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("credentials/accounts.xlsx")) {
            if (inputStream == null) {
                throw new IOException("Resource not found: credentials/accounts.xlsx");
            }

            // Pass the InputStream to ExcelUtil for reading the data
            Map<String, String[]> accounts = ExcelUtil.readAccountData(inputStream);
            AccountFetcher fetcher = new AccountFetcher(accounts);

            // Get selected criteria
            Map<String, String> criteria = new HashMap<>();
            if (!"Select".equals(accountHolderComboBox.getSelectedItem())) {
                criteria.put("Account Holder", (String) accountHolderComboBox.getSelectedItem());
            }
            if (!"Select".equals(accountTypeComboBox.getSelectedItem())) {
                criteria.put("Account Type", (String) accountTypeComboBox.getSelectedItem());
            }
            if (!"Select".equals(transferComboBox.getSelectedItem())) {
                criteria.put("Transfer", (String) transferComboBox.getSelectedItem());
            }
            if (!"Select".equals(offersComboBox.getSelectedItem())) {
                criteria.put("Offers", (String) offersComboBox.getSelectedItem());
            }
            if (!"Select".equals(paymentPlanComboBox.getSelectedItem())) {
                criteria.put("Payment Plan", (String) paymentPlanComboBox.getSelectedItem());
            }
            //herecombo creditLimitComboBox
            if (!"Select".equals(creditLimitComboBox.getSelectedItem())) {
                criteria.put("Credit Limit", (String) creditLimitComboBox.getSelectedItem());
            }

            // Fetch accounts based on criteria
            List<String[]> matchingAccounts = fetcher.fetchAccounts(criteria);
            if (!matchingAccounts.isEmpty()) {
                String[] account = matchingAccounts.get(0);
                usernameField.setText(account[0]);
                passwordField.setText(account[1]);
                accountHolderField.setText(account[2]);
                accountTypeField.setText(account[3]);
                transferCheckBox.setSelected("Eligible".equalsIgnoreCase(account[4]));
                offersCheckBox.setSelected("Eligible".equalsIgnoreCase(account[5]));
                paymentPlanCheckBox.setSelected("Eligible".equalsIgnoreCase(account[6]));
                //here
                creditLimitCheckBox.setSelected("Eligible".equalsIgnoreCase(account[7]));
            } else {
                errorTextArea.setText("No accounts found matching the criteria.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void resetFields() {
        accountHolderComboBox.setSelectedIndex(0);
        accountTypeComboBox.setSelectedIndex(0);
        transferComboBox.setSelectedIndex(0);
        offersComboBox.setSelectedIndex(0);
        paymentPlanComboBox.setSelectedIndex(0);
        //herecombo
        creditLimitComboBox.setSelectedIndex(0);
        usernameField.setText("");
        passwordField.setText("");
        accountHolderField.setText("");
        accountTypeField.setText("");
        transferCheckBox.setSelected(false);
        offersCheckBox.setSelected(false);
        paymentPlanCheckBox.setSelected(false);
        //here
        creditLimitCheckBox.setSelected(false);
        errorTextArea.setText("");
    }

    private void clearTextFieldsAndCheckBoxes() {
        usernameField.setText("");
        passwordField.setText("");
        accountHolderField.setText("");
        accountTypeField.setText("");
        transferCheckBox.setSelected(false);
        offersCheckBox.setSelected(false);
        paymentPlanCheckBox.setSelected(false);
        creditLimitCheckBox.setSelected(false);
        //here
        errorTextArea.setText("");
    }

    // Method to get URL based on site name
    private String getUrlForSite(String siteName) {
        switch (siteName) {
            case "Google":
                return "https://www.google.com";
            case "Facebook":
                return "https://www.facebook.com";
            case "YouTube":
                return "https://www.youtube.com";
            default:
                return "";
        }
    }

    private void copyToClipboard(String text) {
        StringSelection stringSelection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    private void goBackToMainApp() {
        this.dispose(); // Close the Account Selection GUI
        mainAppFrame.setVisible(true); // Show the main application frame
    }
}
