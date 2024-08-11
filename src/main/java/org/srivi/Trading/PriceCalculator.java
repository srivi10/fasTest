package org.srivi.Trading;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class PriceCalculator {

    public static void main(String[] args) {
        // Create the frame
        JFrame frame = new JFrame("Price Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new GridLayout(4, 2));

        // Create labels and text fields
        JLabel buyPriceLabel = new JLabel("Enter Buy Price:");
        JTextField buyPriceField = new JTextField();

        JLabel minus3Label = new JLabel("-3% Price:");
        JTextField minus3Field = new JTextField();
        minus3Field.setEditable(false);

        JLabel plus10Label = new JLabel("+10% Price:");
        JTextField plus10Field = new JTextField();
        plus10Field.setEditable(false);

        // Create a button
        JButton calculateButton = new JButton("Calculate");

        // Add action listener to the button
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Get the buy price from the text field
                    double buyPrice = Double.parseDouble(buyPriceField.getText());

                    // Calculate -3% and +10% prices
                    double minus3Price = buyPrice * 0.97; // -3% price
                    double plus10Price = buyPrice * 1.10; // +10% price

                    // Set the results in the text fields
                    minus3Field.setText(String.format("%.2f", minus3Price));
                    plus10Field.setText(String.format("%.2f", plus10Price));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid number for the buy price.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Add components to the frame
        frame.add(buyPriceLabel);
        frame.add(buyPriceField);
        frame.add(minus3Label);
        frame.add(minus3Field);
        frame.add(plus10Label);
        frame.add(plus10Field);
        frame.add(calculateButton);

        // Set the frame to be visible
        frame.setVisible(true);
    }
}
