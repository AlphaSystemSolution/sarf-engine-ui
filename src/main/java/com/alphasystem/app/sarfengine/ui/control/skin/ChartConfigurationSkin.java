package com.alphasystem.app.sarfengine.ui.control.skin;

import com.alphasystem.app.sarfengine.ui.control.ChartConfigurationView;
import com.alphasystem.morphologicalanalysis.morphology.model.support.SortDirection;
import com.alphasystem.morphologicalanalysis.morphology.model.support.SortDirective;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import static javafx.collections.FXCollections.observableArrayList;

/**
 * @author sali
 */
public class ChartConfigurationSkin extends SkinBase<ChartConfigurationView> {

    /**
     * @param control chart configuration control
     */
    public ChartConfigurationSkin(ChartConfigurationView control) {
        super(control);

        BorderPane mainPane = new BorderPane();
        mainPane.setCenter(initializePane());
        getChildren().add(mainPane);
    }

    private GridPane initializePane() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        ChartConfigurationView view = getSkinnable();

        int row = 0;
        CheckBox checkBox = new CheckBox("Omit Abbreviated Conjugation");
        checkBox.selectedProperty().bindBidirectional(view.omitAbbreviatedConjugationProperty());
        gridPane.add(checkBox, 0, row);

        checkBox = new CheckBox("Omit Detailed Conjugation");
        checkBox.selectedProperty().bindBidirectional(view.omitDetailedConjugationProperty());
        gridPane.add(checkBox, 1, row);

        row++;
        checkBox = new CheckBox("Omit Table of Content");
        checkBox.selectedProperty().bindBidirectional(view.omitTocProperty());
        gridPane.add(checkBox, 0, row);

        checkBox = new CheckBox("Omit Title");
        checkBox.selectedProperty().bindBidirectional(view.omitTitleProperty());
        gridPane.add(checkBox, 1, row);

        row++;
        checkBox = new CheckBox("Omit Header");
        checkBox.selectedProperty().bindBidirectional(view.omitHeaderProperty());
        gridPane.add(checkBox, 0, row);

        checkBox = new CheckBox("Omit Sarf Term Caption");
        checkBox.selectedProperty().bindBidirectional(view.omitSarfTermCaptionProperty());
        gridPane.add(checkBox, 1, row);

        row++;
        Label label = new Label("Sort Directive:");
        gridPane.add(label, 0, row);
        ComboBox<SortDirective> sortDirectiveComboBox = new ComboBox<>(observableArrayList(SortDirective.values()));
        sortDirectiveComboBox.valueProperty().bindBidirectional(view.sortDirectiveProperty());
        sortDirectiveComboBox.getSelectionModel().select(0);
        label.setLabelFor(sortDirectiveComboBox);

        label = new Label("Sort Direction:");
        gridPane.add(label, 1, row);
        ComboBox<SortDirection> sortDirectionComboBox = new ComboBox<>(observableArrayList(SortDirection.values()));
        sortDirectionComboBox.valueProperty().bindBidirectional(view.sortDirectionProperty());
        sortDirectionComboBox.getSelectionModel().select(0);
        label.setLabelFor(sortDirectionComboBox);

        row++;
        gridPane.add(sortDirectiveComboBox, 0, row);
        gridPane.add(sortDirectionComboBox, 1, row);

        return gridPane;
    }
}
