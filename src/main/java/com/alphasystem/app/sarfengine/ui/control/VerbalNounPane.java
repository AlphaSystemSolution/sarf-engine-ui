package com.alphasystem.app.sarfengine.ui.control;

import com.alphasystem.sarfengine.xml.model.VerbalNoun;

/**
 * @author sali
 */
public class VerbalNounPane extends ArabicSupportGroupPane<VerbalNoun> {

    public VerbalNounPane(VerbalNoun[] srcValues) {
        super(srcValues);
        toggleGroup.setWidth(96);
        toggleGroup.setHeight(96);
    }
}
