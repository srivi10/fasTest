package org.srivi.Trading;

import org.srivi.Trading.QE.AccountFetcher;
import org.srivi.Trading.QE.ExcelUtil;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountSelectionGUI {
    private JComboBox<String> accountHolderComboBox;
    private JComboBox<String> accountTypeComboBox;
    private JComboBox<String> transferComboBox;
    private JComboBox<String> offersComboBox;
    private JComboBox<String> paymentPlanComboBox;
    private JButton submitButton;
    private JButton resetButton;
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

    public AccountSelectionGUI() {
        JFrame frame = new JFrame("Account Selection");
        frame.setSize(700, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        accountHolderComboBox = new JComboBox<>(new String[]{"Single", "Multi"});
        accountTypeComboBox = new JComboBox<>(new String[]{"PCH", "Au"});
        transferComboBox = new JComboBox<>(new String[]{"Eligible", "Ineligible"});
        offersComboBox = new JComboBox<>(new String[]{"Eligible", "Ineligible"});
        paymentPlanComboBox = new JComboBox<>(new String[]{"Eligible", "Ineligible"});

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
        frame.add(new JLabel("Account Holder:")).setBounds(20, 20, 120, 30);
        frame.add(accountHolderComboBox).setBounds(150, 20, 150, 30);
        frame.add(new JLabel("Account Type:")).setBounds(20, 60, 120, 30);
        frame.add(accountTypeComboBox).setBounds(150, 60, 150, 30);
        frame.add(new JLabel("Transfer:")).setBounds(20, 100, 120, 30);
        frame.add(transferComboBox).setBounds(150, 100, 150, 30);
        frame.add(new JLabel("Offers:")).setBounds(20, 140, 120, 30);
        frame.add(offersComboBox).setBounds(150, 140, 150, 30);
        frame.add(new JLabel("Payment Plan:")).setBounds(20, 180, 120, 30);
        frame.add(paymentPlanComboBox).setBounds(150, 180, 150, 30);

        frame.add(submitButton).setBounds(50, 230, 100, 30);
        frame.add(resetButton).setBounds(170, 230, 100, 30);

        frame.add(new JLabel("Username:")).setBounds(20, 270, 120, 30);
        frame.add(usernameField).setBounds(150, 270, 200, 30);
        frame.add(new JLabel("Password:")).setBounds(20, 310, 120, 30);
        frame.add(passwordField).setBounds(150, 310, 200, 30);

        frame.add(new JLabel("Account Holder")).setBounds(20, 350, 120, 30);
        frame.add(accountHolderField).setBounds(150, 350, 200, 30);
        frame.add(new JLabel("Account Type")).setBounds(20, 390, 120, 30);
        frame.add(accountTypeField).setBounds(150, 390, 200, 30);
        frame.add(new JLabel("Transfer")).setBounds(20, 430, 120, 30);
        frame.add(transferCheckBox).setBounds(150, 430, 30, 30);
        frame.add(new JLabel("Offers")).setBounds(200, 430, 120, 30);
        frame.add(offersCheckBox).setBounds(320, 430, 30, 30);
        frame.add(new JLabel("Payment Plan")).setBounds(370, 430, 120, 30);
        frame.add(paymentPlanCheckBox).setBounds(490, 430, 30, 30);

        frame.add(loadingLabel).setBounds(450, 270, 100, 30);
        frame.add(errorTextArea).setBounds(20, 470, 380, 40);

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

        frame.setVisible(true);
    }

    private void fetchAndDisplayAccounts() {
        // Example file path - update this with the actual path
        String filePath = "src/main/resources/credentials/accounts.xlsx";

        Map<String, String[]> accounts = ExcelUtil.readAccountData(filePath);
        AccountFetcher fetcher = new AccountFetcher(accounts);

        Map<String, String> criteria = new HashMap<>();
        criteria.put("Account Holder", (String) accountHolderComboBox.getSelectedItem());
        criteria.put("Account Type", (String) accountTypeComboBox.getSelectedItem());
        criteria.put("Transfer", (String) transferComboBox.getSelectedItem());
        criteria.put("Offers", (String) offersComboBox.getSelectedItem());
        criteria.put("Payment Plan", (String) paymentPlanComboBox.getSelectedItem());

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
        errorTextArea.setText(""); // Clear error message
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AccountSelectionGUI::new);
    }
}
