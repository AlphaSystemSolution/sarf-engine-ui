package com.alphasystem.app.sarfengine.ui.control;

import com.alphasystem.app.sarfengine.ui.control.model.TableModel;
import com.alphasystem.arabic.model.ArabicSupport;
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
public abstract class ListTableCell<T extends ArabicSupport> extends TableCell<TableModel, ObservableList<T>> {

    private final Popup popup;
    private final ArabicSupportGroupPane<T> groupPane;

    public ListTableCell(TableColumn<TableModel, ObservableList<T>> column, ArabicSupportGroupPane<T> groupPane) {
        setContentDisplay(GRAPHIC_ONLY);

        this.groupPane = groupPane;
        popup = new Popup();
        popup.getContent().add(groupPane);
        popup.setHideOnEscape(false);

        Button doneButton = new Button("          Done          ");
        doneButton.setOnAction(event -> {
            commitEdit(groupPane.getSelectedValues());
            popup.hide();
        });
        FlowPane flowPane = new FlowPane();
        flowPane.getChildren().add(doneButton);
        flowPane.setAlignment(CENTER);

        groupPane.getChildren().add(doneButton);
    }

    @Override
    public void startEdit() {
        super.startEdit();
        final Bounds bounds = localToScreen(getBoundsInLocal());
        popup.show(this, bounds.getMinX(), bounds.getMinY() + bounds.getHeight());
    }

    @Override
    protected void updateItem(ObservableList<T> item, boolean empty) {
        super.updateItem(item, empty);

        Group graphic = null;
        if (item != null && !item.isEmpty() && !empty) {
            groupPane.setSelectedValues(item);
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
