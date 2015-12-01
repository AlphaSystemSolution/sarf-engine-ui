package com.alphasystem.app.sarfengine.ui.control;

import com.alphasystem.app.sarfengine.ui.control.model.TableModel;
import com.alphasystem.sarfengine.xml.model.RootLetters;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.TextFlow;
import javafx.stage.Popup;

import static com.alphasystem.app.sarfengine.ui.Global.createLabel;
import static com.alphasystem.app.sarfengine.ui.Global.createSpaceLabel;
import static javafx.geometry.Pos.CENTER;
import static javafx.scene.control.ContentDisplay.GRAPHIC_ONLY;

/**
 * @author sali
 */
public class RootLettersTableCell extends TableCell<TableModel, RootLetters> {

    private final Popup popup;
    private final RootLettersPickerKeyBoard keyBoard;

    public RootLettersTableCell(@SuppressWarnings({"unused"}) TableColumn<TableModel, RootLetters> column) {
        setContentDisplay(GRAPHIC_ONLY);

        popup = new Popup();
        keyBoard = new RootLettersPickerKeyBoard(new RootLetters());

        Button doneButton = new Button("          Done          ");
        doneButton.setOnAction(event -> {
            commitEdit(keyBoard.getRootLetters());
            popup.hide();
        });

        FlowPane flowPane = new FlowPane();
        flowPane.setHgap(10);
        flowPane.getChildren().addAll(doneButton);
        flowPane.setAlignment(CENTER);
        keyBoard.getChildren().add(flowPane);

        popup.getContent().add(keyBoard);
        popup.setHideOnEscape(false);
    }

    @Override
    public void startEdit() {
        super.startEdit();

        keyBoard.setRootLetters(getItem());
        final Bounds bounds = localToScreen(getBoundsInLocal());
        popup.show(this, bounds.getMinX(), bounds.getMinY() + bounds.getHeight());
    }

    @Override
    public void commitEdit(RootLetters newValue) {
        super.commitEdit(newValue);
        popup.hide();
    }

    @Override
    public void updateItem(RootLetters item, boolean empty) {
        super.updateItem(item, empty);

        Group label = null;
        if (item != null && !empty) {
            TextFlow textFlow = new TextFlow();

            textFlow.getChildren().addAll(createLabel(item.getFirstRadical()), createSpaceLabel(),
                    createLabel(item.getSecondRadical()), createSpaceLabel(), createLabel(item.getThirdRadical()),
                    createSpaceLabel(), createLabel(item.getFourthRadical()));
            label = new Group(textFlow);
        }
        setGraphic(label);
    }
}
