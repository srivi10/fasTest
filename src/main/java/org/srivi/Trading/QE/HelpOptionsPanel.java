package org.srivi.Trading.QE;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;

public class HelpOptionsPanel extends JPanel {

    private final int iconWidth = 25;
    private final int iconHeight = 25;
    private final int iconSpacing = 90; // Adjust spacing as needed
    private final Font interFont;

    public HelpOptionsPanel(int panelWidth, int panelHeight) {
        setLayout(null);
        setBounds(0, panelHeight - 100, panelWidth, 100); // Position at the bottom

        interFont = FontUtil.getInterFont(12f); // Ensure this method returns a valid Font

        // Add "Need Help?" Label
        JLabel needHelpLabel = new JLabel("Need Help?");
        needHelpLabel.setFont(interFont.deriveFont(Font.BOLD, 14f));
        needHelpLabel.setBounds((panelWidth - 114) / 2, 5, 100, 20);
        needHelpLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(needHelpLabel);

        // Add icons with custom tooltips
        int totalWidth = 0;
        int numberOfOptions = 3; // Number of options to center-align
        int totalSpacing = iconSpacing * (numberOfOptions - 1);
        totalWidth = numberOfOptions * (iconWidth + iconSpacing) - iconSpacing;

        int startX = (panelWidth - totalWidth) / 2; // Calculate starting X position to center-align

        addIconLabel("icons/MasterHelp.png", "User Guide", "https://google.ca", startX, panelWidth);
        addIconLabel("icons/MasterFeatureRequest.png", "New Feature Request", "https://your-feature-request-url.com", startX + iconSpacing, panelWidth);
        addIconLabel("icons/MasterEmail.png", "Email", "mailto:sri@gmail.com?subject=Fastest%20Support%20Request", startX + 2 * iconSpacing, panelWidth);
    }

    private void addIconLabel(String iconPath, String tooltip, String url, int xPosition, int panelWidth) {
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(iconPath));
        Image scaledImage = icon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JLabel iconLabel = new JLabel(scaledIcon);
        iconLabel.setBounds(xPosition, 30, iconWidth, iconHeight);

        // Set tooltip text and use a custom renderer
        iconLabel.setToolTipText(tooltip);
        ToolTipManager.sharedInstance().setInitialDelay(0); // Make tooltip appear immediately
        ToolTipManager.sharedInstance().setDismissDelay(10000); // Adjust tooltip display time

        add(iconLabel);

        iconLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    if (url.startsWith("mailto:")) {
                        Desktop.getDesktop().mail(new URI(url));
                    } else {
                        Desktop.getDesktop().browse(new URI(url));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
