package org.srivi.Trading;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class PriceCalculator {

    private static final String FILE_PATH = "tradingData.txt";

    public static void main(String[] args) {
        // Create the frame
        JFrame frame = new JFrame("Price Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 250);
        frame.setLayout(new GridLayout(5, 2));

        // Create labels and text fields
        JLabel buyPriceLabel = new JLabel("Enter Buy Price:");
        JTextField buyPriceField = new JTextField();

        JLabel dateLabel = new JLabel("Date of Purchase (YYYY-MM-DD):");
        JTextField dateField = new JTextField();

        JLabel minus3Label = new JLabel("-3% Price:");
        JTextField minus3Field = new JTextField();
        minus3Field.setEditable(false);

        JLabel plus10Label = new JLabel("+10% Price:");
        JTextField plus10Field = new JTextField();
        plus10Field.setEditable(false);

        // Create a button
        JButton calculateButton = new JButton("Calculate");

        // Load saved data
        loadData(buyPriceField, dateField);

        // Add action listener to the button
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Get the buy price and date from the text fields
                    double buyPrice = Double.parseDouble(buyPriceField.getText());
                    String dateOfPurchase = dateField.getText();

                    // Calculate -3% and +10% prices
                    double minus3Price = buyPrice * 0.97; // -3% price
                    double plus10Price = buyPrice * 1.10; // +10% price

                    // Set the results in the text fields
                    minus3Field.setText(String.format("%.2f", minus3Price));
                    plus10Field.setText(String.format("%.2f", plus10Price));

                    // Save the buy price and date
                    saveData(buyPriceField.getText(), dateField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid number for the buy price.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Add components to the frame
        frame.add(buyPriceLabel);
        frame.add(buyPriceField);
        frame.add(dateLabel);
        frame.add(dateField);
        frame.add(minus3Label);
        frame.add(minus3Field);
        frame.add(plus10Label);
        frame.add(plus10Field);
        frame.add(calculateButton);

        // Set the frame to be visible
        frame.setVisible(true);
    }

    private static void saveData(String buyPrice, String dateOfPurchase) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(FILE_PATH))) {
            writer.write(buyPrice + "\n" + dateOfPurchase);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadData(JTextField buyPriceField, JTextField dateField) {
        if (Files.exists(Paths.get(FILE_PATH))) {
            try (BufferedReader reader = Files.newBufferedReader(Paths.get(FILE_PATH))) {
                String savedPrice = reader.readLine();
                String savedDate = reader.readLine();
                if (savedPrice != null && !savedPrice.isEmpty()) {
                    buyPriceField.setText(savedPrice);
                }
                if (savedDate != null && !savedDate.isEmpty()) {
                    dateField.setText(savedDate);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
