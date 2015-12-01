package com.alphasystem.app.sarfengine.ui.control;

import com.alphasystem.arabic.model.ArabicSupport;
import javafx.collections.ObservableList;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import static com.alphasystem.util.AppUtil.getResource;
import static javafx.collections.FXCollections.observableArrayList;
import static org.apache.commons.lang3.ArrayUtils.*;

/**
 * @author sali
 */
public abstract class ArabicSupportGroupPane<T extends ArabicSupport> extends VBox {

    private static final int NUM_OF_COLUMNS = 8;
    private static final double SPACING = 10.0;

    protected final ArabicLabelToggleGroup toggleGroup = new ArabicLabelToggleGroup();

    protected ArabicSupportGroupPane(T[] srcValues) {
        this(NUM_OF_COLUMNS, srcValues);
    }

    protected ArabicSupportGroupPane(int numOfColumns, T[] srcValues) {
        initToggleGroup();

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
        setMinWidth(USE_PREF_SIZE);
        setMaxWidth(USE_PREF_SIZE);
        double width = toggleGroup.getWidth();
        int prefWidth = (int) ((width * numOfColumns) + (SPACING * numOfColumns));
        prefWidth = ((prefWidth + 99) / 100) * 100;
        setPrefWidth(prefWidth);

        getStyleClass().addAll("popup");
    }

    protected void initToggleGroup() {
        toggleGroup.setWidth(48);
        toggleGroup.setHeight(32);
        toggleGroup.setFont(Font.font("Arabic Typesetting", 20.0));
    }

    @Override
    public String getUserAgentStylesheet() {
        return getResource("sarf-engine-ui.css").toExternalForm();
    }

    @SuppressWarnings({"unchecked"})
    public final ObservableList<T> getSelectedValues() {
        ObservableList<T> values = observableArrayList();
        toggleGroup.getSelectedValues().forEach(view -> values.add((T) view.getLabel()));
        return values;
    }

    /**
     * Resets the group with new st of values. This method first un-select any previously selected values and
     * the select new values.
     *
     * @param selectedValues values to be reset in toggle group
     */
    @SafeVarargs
    public final void setSelectedValues(T... selectedValues) {
        toggleGroup.reset(selectedValues);
    }

}
