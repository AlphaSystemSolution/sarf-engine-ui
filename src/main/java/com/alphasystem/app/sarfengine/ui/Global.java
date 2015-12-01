package com.alphasystem.app.sarfengine.ui;

import com.alphasystem.arabic.model.ArabicSupport;
import com.alphasystem.arabic.model.NamedTemplate;
import com.alphasystem.sarfengine.xml.model.NounOfPlaceAndTime;
import com.alphasystem.sarfengine.xml.model.VerbalNoun;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.alphasystem.arabic.model.ArabicLetterType.WAW;
import static com.alphasystem.arabic.model.NamedTemplate.*;
import static com.alphasystem.sarfengine.xml.model.NounOfPlaceAndTime.*;
import static com.alphasystem.sarfengine.xml.model.VerbalNoun.*;
import static java.util.Arrays.asList;
import static javafx.scene.text.FontPosture.REGULAR;
import static javafx.scene.text.FontWeight.BLACK;

/**
 * @author sali
 */
public final class Global {

    // Fonts
    public static final Font ARABIC_FONT_24 = Font.font("Arabic Typesetting", BLACK, REGULAR, 24.0);
    public static final Font ARABIC_FONT_20 = Font.font("Arabic Typesetting", BLACK, REGULAR, 20.0);
    public static final Font ENGLISH_FONT = Font.font("Candara", BLACK, REGULAR, 12.0);

    public static final Map<NamedTemplate, List<VerbalNoun>> VERBAL_NOUN_TEMPLATE_MAPPING = new LinkedHashMap<>();
    public static final Map<NamedTemplate, List<NounOfPlaceAndTime>> ADVERB_TEMPLATE_MAPPING = new LinkedHashMap<>();

    static {
        VERBAL_NOUN_TEMPLATE_MAPPING.put(FORM_II_TEMPLATE, asList(VERBAL_NOUN_FORM_II));
        VERBAL_NOUN_TEMPLATE_MAPPING.put(FORM_III_TEMPLATE, asList(VERBAL_NOUN_FORM_III_V1, VERBAL_NOUN_FORM_III_V2));
        VERBAL_NOUN_TEMPLATE_MAPPING.put(FORM_IV_TEMPLATE, asList(VERBAL_NOUN_FORM_IV));
        VERBAL_NOUN_TEMPLATE_MAPPING.put(FORM_V_TEMPLATE, asList(VERBAL_NOUN_FORM_V));
        VERBAL_NOUN_TEMPLATE_MAPPING.put(FORM_VI_TEMPLATE, asList(VERBAL_NOUN_FORM_VI));
        VERBAL_NOUN_TEMPLATE_MAPPING.put(FORM_VII_TEMPLATE, asList(VERBAL_NOUN_FORM_VII));
        VERBAL_NOUN_TEMPLATE_MAPPING.put(FORM_VIII_TEMPLATE, asList(VERBAL_NOUN_FORM_VIII));
        VERBAL_NOUN_TEMPLATE_MAPPING.put(FORM_IX_TEMPLATE, asList(VERBAL_NOUN_FORM_IX));
        VERBAL_NOUN_TEMPLATE_MAPPING.put(FORM_X_TEMPLATE, asList(VERBAL_NOUN_FORM_X));
        VERBAL_NOUN_TEMPLATE_MAPPING.put(FORM_XI_TEMPLATE, asList(VERBAL_NOUN_FORM_XI));

        ADVERB_TEMPLATE_MAPPING.put(FORM_II_TEMPLATE, asList(NOUN_OF_PLACE_AND_TIME_FORM_II));
        ADVERB_TEMPLATE_MAPPING.put(FORM_III_TEMPLATE, asList(NOUN_OF_PLACE_AND_TIME_FORM_III));
        ADVERB_TEMPLATE_MAPPING.put(FORM_IV_TEMPLATE, asList(NOUN_OF_PLACE_AND_TIME_FORM_IV));
        ADVERB_TEMPLATE_MAPPING.put(FORM_V_TEMPLATE, asList(NOUN_OF_PLACE_AND_TIME_FORM_V));
        ADVERB_TEMPLATE_MAPPING.put(FORM_VI_TEMPLATE, asList(NOUN_OF_PLACE_AND_TIME_FORM_VI));
        ADVERB_TEMPLATE_MAPPING.put(FORM_VII_TEMPLATE, asList(NOUN_OF_PLACE_AND_TIME_FORM_VII));
        ADVERB_TEMPLATE_MAPPING.put(FORM_VIII_TEMPLATE, asList(NOUN_OF_PLACE_AND_TIME_FORM_VIII));
        ADVERB_TEMPLATE_MAPPING.put(FORM_X_TEMPLATE, asList(NOUN_OF_PLACE_AND_TIME_FORM_X));
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
        return new Text(" ");
    }

    public static double roundTo100(double srcValue) {
        return (double) ((((int) srcValue) + 99) / 100) * 100;
    }
}
