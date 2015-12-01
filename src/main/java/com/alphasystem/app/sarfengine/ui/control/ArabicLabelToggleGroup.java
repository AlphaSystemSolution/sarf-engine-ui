package com.alphasystem.app.sarfengine.ui.control;

import com.alphasystem.arabic.model.ArabicSupport;
import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.collections.VetoableListDecorator;
import javafx.beans.property.*;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.text.Font;

import java.util.List;

import static javafx.collections.FXCollections.observableArrayList;
import static javafx.scene.text.FontPosture.REGULAR;
import static javafx.scene.text.FontWeight.BLACK;
import static org.apache.commons.lang3.ArrayUtils.contains;

/**
 * @author sali
 */
public class ArabicLabelToggleGroup {

    public static final Font DEFAULT_FONT = Font.font("Arabic Typesetting", BLACK, REGULAR, 36.0);

    private final DoubleProperty width = new SimpleDoubleProperty(0, "width");
    private final DoubleProperty height = new SimpleDoubleProperty(0, "width");
    private final ObjectProperty<Font> font = new SimpleObjectProperty<>(null, "font");
    private final BooleanProperty multipleSelect = new SimpleBooleanProperty(true, "multipleSelect");
    private final ObjectProperty<ArabicLabelView> selectedLabel = new SimpleObjectProperty<>(null, "selectedLabel");
    private final ObservableList<ArabicLabelView> selectedValues = observableArrayList();
    private final ObservableList<ArabicLabelView> toggles = new VetoableListDecorator<ArabicLabelView>
            (new TrackableObservableList<ArabicLabelView>() {
                @Override
                protected void onChanged(ListChangeListener.Change<ArabicLabelView> c) {
                    while (c.next()) {
                        // Look through the removed toggles, and if any of them was the
                        // one and only selected toggle, then we will clear the selected
                        // toggle property.
                        c.getRemoved().stream().filter(t -> t.isSelected()).forEach(t -> setSelected(t, false));

                        // A Toggle can only be in one group at any one time. If the
                        // group is changed, then the toggle is removed from the old group prior to
                        // being added to the new group.
                        c.getAddedSubList().stream().filter(t -> !ArabicLabelToggleGroup.this.equals(t.getGroup())).forEach(t -> {
                            if (t.getGroup() != null) {
                                t.getGroup().getToggles().remove(t);
                            }
                            t.setGroup(ArabicLabelToggleGroup.this);
                        });

                        c.getAddedSubList().stream().filter(t -> ArabicLabelToggleGroup.this.equals(t.getGroup())).forEach(t -> {
                            t.readonlySelectedProperty().addListener((o, ov, nv) -> {
                                if (nv) {
                                    selectedValues.add(t);
                                } else {
                                    selectedValues.remove(t);
                                }
                            });
                            double width = getWidth();
                            if (width > 0) {
                                t.setLabelWidth(width);
                            }
                            double height = getHeight();
                            if (height > 0) {
                                t.setLabelHeight(height);
                            }
                            Font font = getFont();
                            if (font != null) {
                                t.setFont(font);
                            }
                        });

                    } // end of "while (c.next())"
                } // end of "onChanged"
            }) {
        @Override
        protected void onProposedChange(List<ArabicLabelView> toBeAdded, int... indexes) {

        } // end of "onProposedChange"
    };

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

    public final Font getFont() {
        return font.get();
    }

    public final void setFont(Font font) {
        this.font.set(font);
    }

    public final ObjectProperty<Font> fontProperty() {
        return font;
    }

    public final ObservableList<ArabicLabelView> getToggles() {
        return toggles;
    }

    public ObservableList<ArabicLabelView> getSelectedValues() {
        return selectedValues;
    }

    public void setSelected(ArabicLabelView view, boolean selected) {
        if (view == null) {
            return;
        }
        if (selected) {
            if (!isMultipleSelect()) {
                ArabicLabelView selectedLabel = getSelectedLabel();
                if (selectedLabel != null) {
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
