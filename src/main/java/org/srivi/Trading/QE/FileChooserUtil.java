package org.srivi.Trading.QE;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class FileChooserUtil {
    public static void selectFileLocation(JTextField fileLocationField) {
        Font interFont = FontUtil.getInterFont(12f);
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose Save Location");
        fileChooser.setFont(interFont);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int userSelection = fileChooser.showOpenDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = fileChooser.getSelectedFile();
            fileLocationField.setText(selectedDirectory.getAbsolutePath());
        }
    }
}

