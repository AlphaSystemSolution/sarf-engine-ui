package com.alphasystem.arabic.ui;


import com.alphasystem.morphologicalanalysis.morphology.model.support.NounOfPlaceAndTime;

import static com.alphasystem.morphologicalanalysis.morphology.model.support.NounOfPlaceAndTime.values;

/**
 * @author sali
 */
public class AdverbPane extends ArabicSupportGroupPane<NounOfPlaceAndTime> {

    public AdverbPane() {
        super(6, values());
    }
}
