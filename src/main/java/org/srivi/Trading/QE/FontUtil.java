package org.srivi.Trading.QE;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FontUtil {
    public static Font getInterFont(float size) {
        try {
            InputStream is = FontUtil.class.getResourceAsStream("/fonts/RobotoSlab-VariableFont_wght.ttf");
            if (is == null) {
                throw new IOException("Font file not found");
            }

            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            Map<TextAttribute, Object> attributes = new HashMap<>();
            attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR); // Regular 400 weight
            attributes.put(TextAttribute.SIZE, size);

            return font.deriveFont(attributes);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            // Fallback to a default font
            return new Font("Lora", Font.PLAIN, Math.round(size));
        }
    }
}
