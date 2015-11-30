package com.alphasystem.app.sarfengine.ui.control;

import com.alphasystem.arabic.model.ArabicSupport;
import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.collections.VetoableListDecorator;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.List;

import static com.alphasystem.util.AppUtil.getResource;
import static com.alphasystem.util.AppUtil.isGivenType;
import static org.apache.commons.lang3.ArrayUtils.*;

/**
 * @author sali
 */
public abstract class ArabicSupportGroupPane<T extends ArabicSupport> extends VBox {

    private static final int NUM_OF_COLUMNS = 8;
    private static final double SPACING = 10.0;

    protected final ArabicLabelToggleGroup toggleGroup = new ArabicLabelToggleGroup();
    protected final ObservableList<T> selectedValues = new VetoableListDecorator<T>
            (new TrackableObservableList<T>() {
                @Override
                protected void onChanged(ListChangeListener.Change<T> c) {
                    while (c.next()) {

                        c.getAddedSubList().forEach(l -> {
                            ObservableList<Node> children = ArabicSupportGroupPane.this.getChildren();
                            children.stream().filter(node -> isGivenType(ArabicLabelView.class, node)).forEach(node -> {
                                ArabicLabelView label = (ArabicLabelView) node;
                                if (label.getLabel().equals(l)) {
                                    System.out.println("Some item is added to selected values: " + l);
                                    label.setSelected(true);
                                }
                            });
                        });

                    } // end of "while (c.next())"
                } // end of "onChanged"
            }) {
        @Override
        protected void onProposedChange(List<T> toBeAdded, int... indexes) {

        } // end of "onProposedChange"
    };

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
                view.readonlySelectedProperty().addListener((o, ov, nv) -> {
                    if (nv) {
                        if (!getSelectedValues().contains(label)) {
                            System.out.println("Adding new selected label: " + label);
                            getSelectedValues().add((T) label);
                        }
                    }
                });
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

    public final ObservableList<T> getSelectedValues() {
        return selectedValues;
    }

    public final void setSelectedValues(ObservableList<T> values) {
        selectedValues.clear();
        selectedValues.addAll(values);
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
