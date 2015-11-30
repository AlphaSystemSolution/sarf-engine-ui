package com.alphasystem.app.sarfengine.ui.control.model;

import com.alphasystem.arabic.model.NamedTemplate;
import com.alphasystem.sarfengine.xml.model.*;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

import static com.alphasystem.arabic.model.NamedTemplate.FORM_I_CATEGORY_A_GROUP_U_TEMPLATE;
import static java.lang.Boolean.FALSE;
import static javafx.collections.FXCollections.observableArrayList;

/**
 * @author sali
 */
public final class TableModel {

    private final BooleanProperty checked = new SimpleBooleanProperty(FALSE, "checked");
    private final ObjectProperty<RootLetters> rootLetters = new SimpleObjectProperty<>(null, "rootLetters");
    private final ObjectProperty<NamedTemplate> template = new SimpleObjectProperty<>(FORM_I_CATEGORY_A_GROUP_U_TEMPLATE, "template");
    private final StringProperty translation = new SimpleStringProperty(null, "translation");
    private final ObservableList<VerbalNoun> verbalNouns = observableArrayList();
    private final ObservableList<NounOfPlaceAndTime> adverbs = observableArrayList();
    private final BooleanProperty removePassiveLine = new SimpleBooleanProperty(FALSE, "removePassiveLine");
    private final BooleanProperty skipRuleProcessing = new SimpleBooleanProperty(FALSE, "checked");
    private final ObjectProperty<Color> color = new SimpleObjectProperty<>(Color.BROWN, "color");

    public TableModel() {
        this(new ConjugationData());
    }

    public TableModel(ConjugationData data) {
        if (data == null) {
            data = new ConjugationData();
        }
        setRootLetters(data.getRootLetters());
        setTemplate(data.getTemplate());
        setTranslation(data.getTranslation());
        VernalNounAndAdverbContainer container = data.getContainer();
        getVerbalNouns().addAll(container.getVerbalNouns());
        getAdverbs().addAll(container.getAdverbs());
        ConjugationConfiguration configuration = data.getConfiguration();
        setRemovePassiveLine(configuration.isRemovePassiveLine());
        setSkipRuleProcessing(configuration.isSkipRuleProcessing());
    }

    public final boolean getChecked() {
        return checked.get();
    }

    public final void setChecked(boolean checked) {
        this.checked.set(checked);
    }

    public final BooleanProperty checkedProperty() {
        return checked;
    }

    public final RootLetters getRootLetters() {
        return rootLetters.get();
    }

    public final void setRootLetters(RootLetters rootLetters) {
        this.rootLetters.set(rootLetters);
    }

    public final ObjectProperty<RootLetters> rootLettersProperty() {
        return rootLetters;
    }

    public final NamedTemplate getTemplate() {
        return template.get();
    }

    public final void setTemplate(NamedTemplate template) {
        this.template.set(template);
    }

    public final ObjectProperty<NamedTemplate> templateProperty() {
        return template;
    }

    public final String getTranslation() {
        return translation.get();
    }

    public final void setTranslation(String translation) {
        this.translation.set(translation);
    }

    public final StringProperty translationProperty() {
        return translation;
    }

    public final ObservableList<VerbalNoun> getVerbalNouns() {
        return verbalNouns;
    }

    public final ObservableList<NounOfPlaceAndTime> getAdverbs() {
        return adverbs;
    }

    public final boolean getRemovePassiveLine() {
        return removePassiveLine.get();
    }

    public final void setRemovePassiveLine(boolean removePassiveLine) {
        this.removePassiveLine.set(removePassiveLine);
    }

    public final BooleanProperty removePassiveLineProperty() {
        return removePassiveLine;
    }

    public final boolean getSkipRuleProcessing() {
        return skipRuleProcessing.get();
    }

    public final void setSkipRuleProcessing(boolean skipRuleProcessing) {
        this.skipRuleProcessing.set(skipRuleProcessing);
    }

    public final BooleanProperty skipRuleProcessingProperty() {
        return skipRuleProcessing;
    }

    public final Color getColor() {
        return color.get();
    }

    public final void setColor(Color color) {
        this.color.set(color);
    }

    public final ObjectProperty<Color> colorProperty() {
        return color;
    }
}