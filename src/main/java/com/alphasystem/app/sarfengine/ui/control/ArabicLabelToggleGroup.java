package com.alphasystem.app.sarfengine.ui.control;

import com.alphasystem.arabic.model.ArabicSupport;
import javafx.beans.property.*;
import javafx.collections.ObservableList;

import static javafx.collections.FXCollections.observableArrayList;
import static org.apache.commons.lang3.ArrayUtils.contains;

/**
 * @author sali
 */
public class ArabicLabelToggleGroup {

    private final DoubleProperty width = new SimpleDoubleProperty(0, "width");
    private final DoubleProperty height = new SimpleDoubleProperty(0, "width");
    private final BooleanProperty multipleSelect = new SimpleBooleanProperty(true, "multipleSelect");
    private final ObjectProperty<ArabicLabelView> selectedLabel = new SimpleObjectProperty<>(null, "selectedLabel");
    private final ObservableList<ArabicLabelView> selectedLabels = observableArrayList();
    private final ObservableList<ArabicLabelView> toggles = observableArrayList();

    /**
     * Default Constructor
     */
    public ArabicLabelToggleGroup() {
        setMultipleSelect(true);
    }

    public final boolean isMultipleSelect() {
        return multipleSelect.get();
    }

    public final void setMultipleSelect(boolean multipleSelect) {
        this.multipleSelect.set(multipleSelect);
    }

    public final BooleanProperty multipleSelectProperty() {
        return multipleSelect;
    }

    public final ArabicLabelView getSelectedLabel() {
        return selectedLabel.get();
    }

    public final void setSelectedLabel(ArabicLabelView selectedLabel) {
        this.selectedLabel.set(selectedLabel);
    }

    public final ObjectProperty<ArabicLabelView> selectedLabelProperty() {
        return selectedLabel;
    }

    public final double getWidth() {
        return width.get();
    }

    public final void setWidth(double width) {
        this.width.set(width);
    }

    public final DoubleProperty widthProperty() {
        return width;
    }

    public final double getHeight() {
        return height.get();
    }

    public final void setHeight(double height) {
        this.height.set(height);
    }

    public final DoubleProperty heightProperty() {
        return height;
    }

    public final ObservableList<ArabicLabelView> getToggles() {
        return toggles;
    }

    public ObservableList<ArabicLabelView> getSelectedLabels() {
        return selectedLabels;
    }

    public void setSelected(ArabicLabelView view, boolean selected) {
        if (view == null || !selected) {
            return;
        }
        if (selected) {
            selectedLabels.add(view);
            if (!isMultipleSelect()) {
                ArabicLabelView selectedLabel = getSelectedLabel();
                if (selectedLabel != null) {
                    selectedLabels.remove(selectedLabel);
                    selectedLabel.readonlySelectedProperty().set(false);
                }
                setSelectedLabel(view);
            }
        }
        view.readonlySelectedProperty().set(selected);
    }

    public void reset(ArabicSupport... values) {
        toggles.forEach(view -> {
            view.setSelected(false);
            ArabicSupport label = view.getLabel();
            if (values != null && label != null && contains(values, label)) {
                view.setSelected(true);
            }
        });
    }

}