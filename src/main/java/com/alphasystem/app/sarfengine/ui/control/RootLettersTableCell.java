package com.alphasystem.app.sarfengine.ui.control;

import com.alphasystem.app.sarfengine.ui.control.model.TableModel;
import com.alphasystem.arabic.model.ArabicLetterType;
import com.alphasystem.arabic.ui.RootLettersPickerKeyBoard;
import com.alphasystem.morphologicalanalysis.morphology.model.RootLetters;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.text.TextFlow;
import javafx.stage.Popup;

import static com.alphasystem.app.sarfengine.ui.Global.createLabel;
import static com.alphasystem.app.sarfengine.ui.Global.createSpaceLabel;
import static javafx.geometry.NodeOrientation.RIGHT_TO_LEFT;
import static javafx.scene.control.ContentDisplay.GRAPHIC_ONLY;

/**
 * @author sali
 */
public class RootLettersTableCell extends TableCell<TableModel, RootLetters> {

    private final Popup popup;
    private final RootLettersPickerKeyBoard keyBoard;

    public RootLettersTableCell(@SuppressWarnings({"unused"}) TableColumn<TableModel, RootLetters> column) {
        setContentDisplay(GRAPHIC_ONLY);
        setNodeOrientation(RIGHT_TO_LEFT);

        popup = new Popup();
        keyBoard = new RootLettersPickerKeyBoard();

        popup.getContent().add(keyBoard);
        popup.setAutoHide(true);
        popup.setOnHiding(event -> commitEdit());
        popup.setOnAutoHide(event -> commitEdit());
    }

    private void commitEdit() {
        ArabicLetterType[] arabicLetters = keyBoard.getRootLetters();
        RootLetters rootLetters = new RootLetters().withFirstRadical(arabicLetters[0]).withSecondRadical(arabicLetters[1])
                .withThirdRadical(arabicLetters[2]).withFourthRadical(arabicLetters[3]);
        commitEdit(rootLetters);
    }

    @Override
    public void startEdit() {
        super.startEdit();
        final Bounds bounds = localToScreen(getBoundsInLocal());
        popup.show(this, bounds.getMinX(), bounds.getMinY() + bounds.getHeight());
    }

    @Override
    public void updateItem(RootLetters item, boolean empty) {
        super.updateItem(item, empty);

        Group label = null;
        if (item != null && !empty) {
            keyBoard.setRootLetters(item.getFirstRadical(), item.getSecondRadical(),
                    item.getThirdRadical(), item.getFourthRadical());

            TextFlow textFlow = new TextFlow();

            ArabicLetterType fourthRadical = item.getFourthRadical();
            textFlow.getChildren().addAll(createLabel(item.getFirstRadical()), createSpaceLabel(5),
                    createLabel(item.getSecondRadical()), createSpaceLabel(5), createLabel(item.getThirdRadical()));
            if (fourthRadical != null) {
                textFlow.getChildren().addAll(createSpaceLabel(5), createLabel(fourthRadical));
            }
            label = new Group(textFlow);
        }
        setGraphic(label);
    }
}
