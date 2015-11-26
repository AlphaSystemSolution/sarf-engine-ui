package com.alphasystem.app.sarfengine.ui.control;

import com.alphasystem.app.sarfengine.ui.skin.ArabicLabelViewSkin;
import com.alphasystem.arabic.model.ArabicSupport;
import javafx.beans.property.*;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.text.Font;

import static javafx.scene.text.FontPosture.REGULAR;
import static javafx.scene.text.FontWeight.BLACK;

/**
 * @author sali
 */
public class ArabicLabelView extends Control {

    private static final int DEFAULT_WIDTH = 64;
    private static final int DEFAULT_HEIGHT = 64;
    private static final Font DEFAULT_FONT = Font.font("Arabic Typesetting", BLACK, REGULAR, 36.0);

    private final DoubleProperty labelWidth = new SimpleDoubleProperty(DEFAULT_WIDTH, "width");
    private final DoubleProperty labelHeight = new SimpleDoubleProperty(DEFAULT_HEIGHT, "height");
    private final BooleanProperty selected = new SimpleBooleanProperty(false, "selected");
    private final ObjectProperty<Font> font = new SimpleObjectProperty<>(DEFAULT_FONT, "font");
    private final ObjectProperty<ArabicSupport> label = new SimpleObjectProperty<>(null, "label");
    private final ObjectProperty<ArabicLabelToggleGroup> group = new SimpleObjectProperty<>(null, "group");

    /**
     * Default Constructor
     */
    public ArabicLabelView() {
        this(null);
    }

    public ArabicLabelView(ArabicSupport label) {
        labelProperty().addListener((o, oV, nV) -> {
            setUserData(nV);
        });
        setLabel(label);
        setLabelWidth(DEFAULT_WIDTH);
        setLabelHeight(DEFAULT_HEIGHT);
        setFont(DEFAULT_FONT);
        setSelected(false);
    }

    @Override
    protected Skin<ArabicLabelView> createDefaultSkin() {
        return new ArabicLabelViewSkin(this);
    }

    public final double getLabelHeight() {
        return labelHeight.get();
    }

    public final void setLabelHeight(double labelHeight) {
        this.labelHeight.set(labelHeight);
    }

    public final DoubleProperty labelHeightProperty() {
        return labelHeight;
    }

    public final double getLabelWidth() {
        return labelWidth.get();
    }

    public final void setLabelWidth(double labelWidth) {
        this.labelWidth.set(labelWidth);
    }

    public final DoubleProperty labelWidthProperty() {
        return labelWidth;
    }

    public final boolean isSelected() {
        return selected.get();
    }

    public final void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public final BooleanProperty selectedProperty() {
        return selected;
    }

    public final ArabicLabelToggleGroup getGroup() {
        return group == null ? null : group.get();
    }

    public final void setGroup(ArabicLabelToggleGroup group) {
        this.group.set(group);
        getGroup().getToggles().add(this);
    }

    public final ObjectProperty<ArabicLabelToggleGroup> groupProperty() {
        return group;
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

    public final ArabicSupport getLabel() {
        return label.get();
    }

    public final void setLabel(ArabicSupport label) {
        this.label.set(label);
    }

    public final ObjectProperty<ArabicSupport> labelProperty() {
        return label;
    }

    public void makeSelection() {
        System.out.println("::::::::::::::::::: " + this);
        if (getLabel() == null) {
            return;
        }
        ArabicLabelToggleGroup group = getGroup();
        boolean selected = !isSelected();
        if (group == null) {
            setSelected(selected);
        } else {
            group.setSelected(this, selected);
        }
    }
}
