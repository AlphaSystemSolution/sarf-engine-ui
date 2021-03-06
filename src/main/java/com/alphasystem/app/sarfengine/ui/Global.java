package com.alphasystem.app.sarfengine.ui;

import com.alphasystem.arabic.model.ArabicSupport;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import static com.alphasystem.app.sarfengine.util.TemplateReader.SARF_FILE_DESCRIPTION;
import static com.alphasystem.app.sarfengine.util.TemplateReader.SARF_FILE_EXTENSION_ALL;
import static com.alphasystem.arabic.model.ArabicLetterType.WAW;
import static com.alphasystem.arabic.ui.util.FontConstants.ARABIC_FONT_24;
import static com.alphasystem.util.AppUtil.USER_HOME_DIR;

/**
 * @author sali
 */
public final class Global {

    public static final FileChooser FILE_CHOOSER = new FileChooser();

    static {
        FILE_CHOOSER.setInitialDirectory(USER_HOME_DIR);
        FILE_CHOOSER.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(SARF_FILE_DESCRIPTION, SARF_FILE_EXTENSION_ALL));
    }

    public static Text createLabel(ArabicSupport letter) {
        Text text = new Text();
        text.setText(letter == null ? "" : letter.getLabel().toUnicode());
        text.setFont(ARABIC_FONT_24);
        return text;
    }

    public static Text createAndLabel() {
        return createLabel(WAW);
    }

    public static Text createSpaceLabel() {
        return createSpaceLabel(1);
    }

    public static Text createSpaceLabel(int numOfSpace) {
        numOfSpace = numOfSpace <= 0 ? 1 : numOfSpace;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < numOfSpace; i++) {
            builder.append(" ");
        }
        return new Text(builder.toString());
    }

    public static double roundTo100(double srcValue) {
        return (double) ((((int) srcValue) + 99) / 100) * 100;
    }
}
