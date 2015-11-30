package com.alphasystem.app.sarfengine.ui.control;

import com.alphasystem.app.sarfengine.ui.control.model.TableModel;
import com.alphasystem.arabic.model.ArabicSupport;
import com.alphasystem.sarfengine.xml.model.VerbalNoun;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Popup;

import static com.alphasystem.app.sarfengine.ui.SarfEnginePane.ARABIC_FONT;
import static com.alphasystem.arabic.model.ArabicLetterType.WAW;
import static javafx.scene.control.ContentDisplay.GRAPHIC_ONLY;

/**
 * @author sali
 */
public class VerbalNounTableCell extends TableCell<TableModel, ObservableList<VerbalNoun>> {

    private final Popup popup;
    private final VerbalNounPane verbalNounPane;
    private final Button doneButton;

    public VerbalNounTableCell() {
        setContentDisplay(GRAPHIC_ONLY);

        verbalNounPane = new VerbalNounPane();
        popup = new Popup();
        popup.getContent().add(verbalNounPane);
        popup.setHideOnEscape(false);

        doneButton = new Button("          Done          ");
        doneButton.setOnAction(event -> {
            commitEdit(verbalNounPane.getSelectedValues());
            popup.hide();
        });
        FlowPane flowPane = new FlowPane();
        flowPane.getChildren().add(doneButton);
        flowPane.setAlignment(Pos.CENTER);

        verbalNounPane.getChildren().add(doneButton);
    }

    @Override
    public void startEdit() {
        super.startEdit();

        final Bounds bounds = localToScreen(getBoundsInLocal());
        popup.show(this, bounds.getMinX(), bounds.getMinY() + bounds.getHeight());
    }

    @Override
    protected void updateItem(ObservableList<VerbalNoun> item, boolean empty) {
        super.updateItem(item, empty);

        Group graphic = null;
        if (item != null && !item.isEmpty() && !empty) {
            System.out.println("Display Verbal Noun: " + item);

            graphic = new Group();
            TextFlow textFlow = new TextFlow();
            textFlow.getChildren().add(createLabel(item.get(0)));
            for (int i = 1; i < item.size(); i++) {
                textFlow.getChildren().addAll(new Text(" "), createLabel(WAW), new Text(" "), createLabel(item.get(i)));
            }
            graphic.getChildren().add(textFlow);
        }

        setGraphic(graphic);
    }

    private Text createLabel(ArabicSupport letter) {
        Text text = new Text();
        text.setText(letter == null ? "" : letter.getLabel().toUnicode());
        text.setFont(ARABIC_FONT);
        return text;
    }
}
