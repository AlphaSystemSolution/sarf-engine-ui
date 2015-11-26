package com.alphasystem.app.sarfengine.ui.control;

import com.alphasystem.arabic.model.ArabicLetter;
import com.alphasystem.arabickeyboard.ui.Keyboard;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import static com.alphasystem.arabic.model.ArabicLetterType.getByUnicode;
import static javafx.geometry.NodeOrientation.RIGHT_TO_LEFT;
import static javafx.scene.input.KeyEvent.KEY_TYPED;

/**
 * @author sali
 */
public class RootWordsSelectionPane extends VBox {

    private static final double SPACING = 10.0;

    private final ArabicLabelToggleGroup toggleGroup = new ArabicLabelToggleGroup();
    private final ArabicLabelView[] labels = new ArabicLabelView[4];
    private int currentIndex;
    private ArabicLabelView currentView;

    public RootWordsSelectionPane() {
        this(null, null, null, null);
    }

    public RootWordsSelectionPane(ArabicLetter firstRadical, ArabicLetter secondRadical, ArabicLetter thirdRadical,
                                  ArabicLetter fourthRadical) {
        setSpacing(SPACING);

        FlowPane flowPane = new FlowPane();
        flowPane.setHgap(SPACING);
        flowPane.setPadding(new Insets(SPACING * 2, (SPACING * 50) + 40, SPACING * 2, (SPACING * 50) + 40));
        flowPane.setNodeOrientation(RIGHT_TO_LEFT);

        toggleGroup.setMultipleSelect(false);

        labels[0] = createArabicLabelView(firstRadical);
        labels[1] = createArabicLabelView(secondRadical);
        labels[2] = createArabicLabelView(thirdRadical);
        labels[3] = createArabicLabelView(fourthRadical);

        selectFirst();
        flowPane.getChildren().addAll(labels[0], labels[1], labels[2], labels[3]);

        TextField target = new TextField();
        target.addEventFilter(KEY_TYPED, event -> handleKeyboardEvent(target, event));
        Keyboard keyboard = new Keyboard(target);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(flowPane);
        getChildren().add(borderPane);

        borderPane = new BorderPane();
        borderPane.setCenter(keyboard.view());
        getChildren().add(borderPane);
    }

    private void handleKeyboardEvent(TextField target, KeyEvent event) {
        if (currentView != null) {
            currentView.setLabel(new ArabicLetter(getByUnicode(event.getCharacter().charAt(0))));
            currentView.setSelected(false);
        }
        currentIndex = (currentIndex + 1) % 4;
        currentView = labels[currentIndex];
        currentView.setSelected(true);
        target.setText("");
    }

    private void selectFirst() {
        currentIndex = 0;
        labels[currentIndex].setSelected(true);
        currentView = labels[currentIndex];
    }

    private ArabicLabelView createArabicLabelView(ArabicLetter arabicLetter) {
        ArabicLabelView view = new ArabicLabelView(arabicLetter);
        view.setGroup(toggleGroup);
        return view;
    }

    public void reset(ArabicLetter firstRadical, ArabicLetter secondRadical, ArabicLetter thirdRadical,
                      ArabicLetter fourthRadical) {
        toggleGroup.reset(firstRadical, secondRadical, thirdRadical, fourthRadical);
        selectFirst();
    }
}
