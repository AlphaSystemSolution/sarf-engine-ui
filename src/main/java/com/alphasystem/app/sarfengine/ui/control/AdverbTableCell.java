package com.alphasystem.app.sarfengine.ui.control;

import com.alphasystem.app.sarfengine.ui.control.model.TableModel;
import com.alphasystem.sarfengine.xml.model.NounOfPlaceAndTime;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.TextFlow;
import javafx.stage.Popup;

import static com.alphasystem.app.sarfengine.ui.Global.*;
import static javafx.geometry.Pos.CENTER;
import static javafx.scene.control.ContentDisplay.GRAPHIC_ONLY;

/**
 * @author sali
 */
public class AdverbTableCell extends TableCell<TableModel, ObservableList<NounOfPlaceAndTime>> {

    private final Popup popup;
    private final AdverbPane adverbPane;

    public AdverbTableCell(TableColumn<TableModel, ObservableList<NounOfPlaceAndTime>> column) {
        setContentDisplay(GRAPHIC_ONLY);

        adverbPane = new AdverbPane();
        popup = new Popup();
        popup.getContent().add(adverbPane);
        popup.setHideOnEscape(false);

        Button doneButton = new Button("          Done          ");
        doneButton.setOnAction(event -> {
            commitEdit(adverbPane.getSelectedValues());
            popup.hide();
        });
        FlowPane flowPane = new FlowPane();
        flowPane.getChildren().add(doneButton);
        flowPane.setAlignment(CENTER);

        adverbPane.getChildren().add(doneButton);
    }

    @Override
    public void startEdit() {
        super.startEdit();
        final Bounds bounds = localToScreen(getBoundsInLocal());
        popup.show(this, bounds.getMinX(), bounds.getMinY() + bounds.getHeight());
    }

    @Override
    protected void updateItem(ObservableList<NounOfPlaceAndTime> item, boolean empty) {
        super.updateItem(item, empty);

        Group graphic = null;
        if (item != null && !item.isEmpty() && !empty) {
            graphic = new Group();
            TextFlow textFlow = new TextFlow();
            textFlow.getChildren().add(createLabel(item.get(0)));
            for (int i = 1; i < item.size(); i++) {
                textFlow.getChildren().addAll(createSpaceLabel(), createAndLabel(),
                        createSpaceLabel(), createLabel(item.get(i)));
            }
            graphic.getChildren().add(textFlow);
        }

        setGraphic(graphic);
    }

}
