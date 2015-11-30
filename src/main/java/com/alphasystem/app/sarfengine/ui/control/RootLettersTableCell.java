package com.alphasystem.app.sarfengine.ui.control;

import com.alphasystem.app.sarfengine.ui.control.model.TableModel;
import com.alphasystem.arabic.model.ArabicSupport;
import com.alphasystem.sarfengine.xml.model.RootLetters;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Popup;

import static com.alphasystem.app.sarfengine.ui.SarfEnginePane.ARABIC_FONT;
import static javafx.scene.control.ContentDisplay.GRAPHIC_ONLY;

/**
 * @author sali
 */
public class RootLettersTableCell extends TableCell<TableModel, RootLetters> {

    private final Popup popup;
    private final RootLettersPickerKeyBoard keyBoard;

    public RootLettersTableCell(TableColumn<TableModel, RootLetters> column) {
        setContentDisplay(GRAPHIC_ONLY);

        popup = new Popup();
        keyBoard = new RootLettersPickerKeyBoard(this, new RootLetters());
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

            textFlow.getChildren().addAll(createLabel(item.getFirstRadical()), new Text(" "),
                    createLabel(item.getSecondRadical()), new Text(" "), createLabel(item.getThirdRadical()),
                    new Text(" "), createLabel(item.getFourthRadical()));
            label = new Group(textFlow);
        }
        setGraphic(label);
    }

    private Text createLabel(ArabicSupport letter) {
        Text text = new Text();
        text.setText(letter == null ? "" : letter.getLabel().toUnicode());
        text.setFont(ARABIC_FONT);
        return text;
    }
}
