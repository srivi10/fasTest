package org.srivi.Trading.QE;

import javax.swing.*;
import java.io.File;

public class FileChooserUtil {
    public static void selectFileLocation(JTextField fileLocationField) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose Save Location");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int userSelection = fileChooser.showOpenDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = fileChooser.getSelectedFile();
            fileLocationField.setText(selectedDirectory.getAbsolutePath());
        }
    }
}

