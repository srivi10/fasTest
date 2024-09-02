package org.srivi.Trading.QE;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BackButtonUtil {

    /**
     * Creates a "Back" label with an icon and adds it to the specified frame.
     * When clicked, the label will close the current frame and optionally show the main app frame.
     *
     * @param currentFrame The current frame to dispose of when the "Back" label is clicked.
     * @return JLabel The created "Back" label.
     */
    public static JLabel createBackLabel(JFrame currentFrame) {
        // Load and scale the back icon
        ImageIcon backIcon = new ImageIcon(BackButtonUtil.class.getClassLoader().getResource("icons/MasterBackButton.png"));
        Image scaledBackImage = backIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledBackImage);

        // Create the back label with icon and text
        JLabel backLabel = new JLabel("Back", scaledIcon, SwingConstants.LEFT);
        backLabel.setBounds(10, 10, 100, 30); // Adjust width to fit text and icon
        backLabel.setFont(FontUtil.getInterFont(14f)); // Set font
        backLabel.setIconTextGap(5); // Gap between icon and text
        backLabel.setHorizontalAlignment(SwingConstants.LEFT); // Align text to the left of the icon
        backLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Change cursor to hand on hover

        // Add mouse listener to handle clicks
        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                currentFrame.dispose(); // Close the current frame
            }
        });

        return backLabel;
    }
}
