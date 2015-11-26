package com.alphasystem.app.sarfengine.ui.control;

import com.alphasystem.arabic.model.ArabicSupport;
import javafx.collections.ObservableList;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import static org.apache.commons.lang3.ArrayUtils.*;

/**
 * @author sali
 */
public class ArabicSupportGroupPane<T extends ArabicSupport> extends VBox {

    private static final int NUM_OF_COLUMNS = 8;
    private static final double SPACING = 10.0;

    protected final ArabicLabelToggleGroup toggleGroup = new ArabicLabelToggleGroup();

    public ArabicSupportGroupPane(T[] srcValues) {
        this(NUM_OF_COLUMNS, srcValues);
    }

    public ArabicSupportGroupPane(int numOfColumns, T[] srcValues) {
        numOfColumns = (numOfColumns <= 0) ? NUM_OF_COLUMNS : numOfColumns;

        ArabicSupport[] values = new ArabicSupport[0];
        values = addAll(values, srcValues);
        while (values.length % numOfColumns != 0) {
            values = add(values, null);
        }

        setSpacing(SPACING);

        int startIndex = 0;
        int endIndex = numOfColumns;
        while (startIndex < values.length) {
            ArabicSupport[] subarray = subarray(values, startIndex, endIndex);
            reverse(subarray);

            FlowPane flowPane = new FlowPane();
            flowPane.setHgap(SPACING);

            for (ArabicSupport label : subarray) {
                ArabicLabelView view = new ArabicLabelView(label);
                view.setGroup(toggleGroup);
                flowPane.getChildren().add(view);
            }
            getChildren().add(flowPane);
            startIndex = endIndex;
            endIndex += numOfColumns;
        }
    }

    public ObservableList<ArabicLabelView> getSelectedValues() {
        return toggleGroup.getSelectedLabels();
    }

    /**
     * Resets the group with new st of values. This method first un-select any previously selected values and
     * the select new values.
     *
     * @param values
     */
    public void reset(T... values) {
        toggleGroup.reset(values);
    }
}
