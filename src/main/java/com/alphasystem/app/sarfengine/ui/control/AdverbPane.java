package com.alphasystem.app.sarfengine.ui.control;

import com.alphasystem.sarfengine.xml.model.NounOfPlaceAndTime;

import static com.alphasystem.sarfengine.xml.model.NounOfPlaceAndTime.values;

/**
 * @author sali
 */
public class AdverbPane extends ArabicSupportGroupPane<NounOfPlaceAndTime> {

    public AdverbPane() {
        super(6, values());
    }
}
