package org.srivi.Trading;

import org.srivi.Trading.QE.AccountFetcher;
import org.srivi.Trading.QE.ExcelUtil;
import org.srivi.Trading.QE.FontUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountSelectionGUI extends JFrame {

    private JFrame mainAppFrame;
    private JComboBox<String> accountHolderComboBox;
    private JComboBox<String> accountTypeComboBox;
    private JComboBox<String> transferComboBox;
    private JComboBox<String> offersComboBox;
    private JComboBox<String> paymentPlanComboBox;
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
    private JLabel loadingLabel;
    private JTextArea errorTextArea;

    private boolean isLoading = false;

    public AccountSelectionGUI(JFrame mainAppFrame) {
        this.mainAppFrame = mainAppFrame;
        setupUI();
    }

    private void setupUI() {
        setTitle("Account Selection");
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        // Set the custom font
        Font interFont = FontUtil.getInterFont(12f);

        backButton = new JButton("Back");
        backButton.setBounds(10, 10, 80, 30);
        backButton.setFont(interFont);
        backButton.addActionListener(e -> goBackToMainApp());
        add(backButton);

        accountHolderComboBox = new JComboBox<>(new String[]{"Single", "Multi"});
        accountTypeComboBox = new JComboBox<>(new String[]{"PCH", "Au"});
        transferComboBox = new JComboBox<>(new String[]{"Select", "Eligible", "Ineligible"});
        offersComboBox = new JComboBox<>(new String[]{"Select", "Eligible", "Ineligible"});
        paymentPlanComboBox = new JComboBox<>(new String[]{"Select", "Eligible", "Ineligible"});

        submitButton = new JButton("Submit");
        resetButton = new JButton("Reset");

        usernameField = new JTextField();
        passwordField = new JTextField();
        accountHolderField = new JTextField();
        accountTypeField = new JTextField();
        transferCheckBox = new JCheckBox();
        offersCheckBox = new JCheckBox();
        paymentPlanCheckBox = new JCheckBox();

        loadingLabel = new JLabel("Loading...");
        loadingLabel.setForeground(Color.GREEN);
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

        add(submitButton).setBounds(50, 260, 100, 30);
        add(resetButton).setBounds(170, 260, 100, 30);

        add(new JLabel("Username:")).setBounds(20, 300, 120, 30);
        add(usernameField).setBounds(150, 300, 200, 30);
        add(new JLabel("Password:")).setBounds(20, 340, 120, 30);
        add(passwordField).setBounds(150, 340, 200, 30);

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

        add(loadingLabel).setBounds(450, 300, 100, 30);
        add(errorTextArea).setBounds(20, 500, 380, 40);

        submitButton.addActionListener(e -> {
            if (isLoading) return;  // Prevent action if loading is already active

            // Clear fields before fetching
            usernameField.setText("");
            passwordField.setText("");
            clearTextFieldsAndCheckBoxes();

            // Show loading label and fetch accounts after delay
            loadingLabel.setVisible(true);
            submitButton.setEnabled(false);
            new Timer(2000, evt -> {
                fetchAndDisplayAccounts();
                loadingLabel.setVisible(false);
                submitButton.setEnabled(true);
            }).start();
        });

        resetButton.addActionListener(e -> resetFields());

        setVisible(true);
    }

    private void fetchAndDisplayAccounts() {
        // Example file path - update this with the actual path
        String filePath = "src/main/resources/credentials/accounts.xlsx";

        Map<String, String[]> accounts = ExcelUtil.readAccountData(filePath);
        AccountFetcher fetcher = new AccountFetcher(accounts);

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

        List<String[]> matchedAccounts = fetcher.fetchAccounts(criteria);

        displayResults(matchedAccounts);
    }

    private void displayResults(List<String[]> accounts) {
        errorTextArea.setText(""); // Clear previous error messages
        if (accounts.isEmpty()) {
            errorTextArea.setText("Currently, CID with selected parameters is unavailable.");
        } else {
            for (String[] account : accounts) {
                usernameField.setText(account[0]);
                passwordField.setText(account[1]);

                accountHolderField.setText(account[2]);  // Set account holder text
                accountTypeField.setText(account[3]);    // Set account type text
                transferCheckBox.setSelected("Eligible".equals(account[4]));
                offersCheckBox.setSelected("Eligible".equals(account[5]));
                paymentPlanCheckBox.setSelected("Eligible".equals(account[6]));
            }
        }
    }

    private void clearTextFieldsAndCheckBoxes() {
        accountHolderField.setText("");
        accountTypeField.setText("");
        transferCheckBox.setSelected(false);
        offersCheckBox.setSelected(false);
        paymentPlanCheckBox.setSelected(false);
    }

    private void resetFields() {
        accountHolderComboBox.setSelectedIndex(0);
        accountTypeComboBox.setSelectedIndex(0);
        transferComboBox.setSelectedIndex(0);
        offersComboBox.setSelectedIndex(0);
        paymentPlanComboBox.setSelectedIndex(0);
        usernameField.setText("");
        passwordField.setText("");
        clearTextFieldsAndCheckBoxes();
        errorTextArea.setText("");
    }

    private void goBackToMainApp() {
        this.dispose();
        mainAppFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AccountSelectionGUI(null));
    }
}
