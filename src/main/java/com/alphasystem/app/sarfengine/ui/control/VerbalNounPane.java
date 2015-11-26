package com.alphasystem.app.sarfengine.ui.control;

import com.alphasystem.sarfengine.xml.model.VerbalNoun;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import static org.apache.commons.lang3.ArrayUtils.*;

/**
 * @author sali
 */
public class VerbalNounPane extends VBox {

    private static final int NUM_OF_COLUMNS = 8;

    private final ArabicLabelToggleGroup toggleGroup = new ArabicLabelToggleGroup();

    public VerbalNounPane() {
        VerbalNoun[] values = new VerbalNoun[0];
        values = addAll(values, VerbalNoun.values());
        while (values.length % NUM_OF_COLUMNS != 0) {
            values = add(values, null);
        }

        setSpacing(10.0);

        int startIndex = 0;
        int endIndex = NUM_OF_COLUMNS;
        while (startIndex < values.length) {
            VerbalNoun[] verbalNouns = subarray(values, startIndex, endIndex);
            reverse(verbalNouns);

            FlowPane flowPane = new FlowPane();
            flowPane.setHgap(10.0);

            for (VerbalNoun verbalNoun : verbalNouns) {
                ArabicLabelView view = new ArabicLabelView(verbalNoun);
                view.setDisable(verbalNoun == null);
                view.setGroup(toggleGroup);
                flowPane.getChildren().add(view);
            }
            getChildren().add(flowPane);
            startIndex = endIndex;
            endIndex += NUM_OF_COLUMNS;
        }
    }


}
