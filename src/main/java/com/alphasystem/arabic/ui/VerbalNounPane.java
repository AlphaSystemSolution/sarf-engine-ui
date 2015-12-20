package com.alphasystem.arabic.ui;


import com.alphasystem.morphologicalanalysis.morphology.model.support.VerbalNoun;

import static com.alphasystem.morphologicalanalysis.morphology.model.support.VerbalNoun.values;

/**
 * @author sali
 */
public class VerbalNounPane extends ArabicSupportGroupPane<VerbalNoun> {

    public VerbalNounPane() {
        super(values());
    }
}
