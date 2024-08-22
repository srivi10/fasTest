package org.srivi.Trading.QE;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class FontUtil {
    public static Font getInterFont(float size) {
        try {
            InputStream is = FontUtil.class.getResourceAsStream("/fonts/Inter_18pt-Regular.ttf");
            if (is == null) {
                throw new IOException("Font file not found");
            }
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            return font.deriveFont(size);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            // Fallback to a default font
            return new Font("Inter_18pt-Regular.ttf", Font.PLAIN, Math.round(size));
        }
    }
}
