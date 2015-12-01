package com.alphasystem.app.sarfengine.ui.control;

import com.alphasystem.arabic.model.ArabicLetterType;
import com.alphasystem.arabic.ui.ArabicLabelToggleGroup;
import com.alphasystem.arabic.ui.ArabicLabelView;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static com.alphasystem.arabic.model.ArabicLetterType.*;
import static com.alphasystem.util.AppUtil.getResource;
import static javafx.geometry.NodeOrientation.RIGHT_TO_LEFT;

/**
 * @author sali
 */
public class RootLettersPickerKeyBoard extends VBox {

    private static final int PREF_WIDTH = 48;
    private static final int PREF_HEIGHT = 36;
    private static final int SPACING = 5;
    private static final ArabicLetterType[] ROW_1 = {DDAD, SAD, THA, QAF, FA, GHAIN, AIN, HHA, KHA, HA, JEEM, DAL, THAL};
    private static final ArabicLetterType[] ROW_2 = {SHEEN, SEEN, YA, BA, LAM, ALIF, TA, NOON, MEEM, KAF, TTA, null, null};
    private static final ArabicLetterType[] ROW_3 = {YA_HAMZA_ABOVE, HAMZA, WAW_HAMZA_ABOVE, RA, ALIF_MAKSURA,
            TA_MARBUTA, WAW, ZAIN, DTHA, null, null, null, null};
    private static final Font FONT = Font.font("Arabic Typesetting", 24);

    private final ArabicLabelToggleGroup group;
    private final ArabicLabelView[] labels = new ArabicLabelView[4];
    private final ObjectProperty<ArabicLetterType> firstRadical = new SimpleObjectProperty<>();
    private final ObjectProperty<ArabicLetterType> secondRadical = new SimpleObjectProperty<>();
    private final ObjectProperty<ArabicLetterType> thirdRadical = new SimpleObjectProperty<>();
    private final ObjectProperty<ArabicLetterType> fourthRadical = new SimpleObjectProperty<>();
    private int currentIndex;
    private ArabicLabelView currentView;

    public RootLettersPickerKeyBoard() {
        this(null, null, null, null);
    }

    public RootLettersPickerKeyBoard(final ArabicLetterType firstRadical, final ArabicLetterType secondRadical,
                                     final ArabicLetterType thirdRadical, final ArabicLetterType fourthRadical) {
        group = new ArabicLabelToggleGroup();
        group.setMultipleSelect(false);
        group.setWidth(32);
        group.setHeight(32);

        setPadding(new Insets(SPACING, SPACING, SPACING, SPACING));
        setSpacing(SPACING);

        labels[0] = createLabel(firstRadical, 0);
        labels[0].labelProperty().addListener((o, oV, nV) -> {
            firstRadicalProperty().setValue((ArabicLetterType) nV);
        });
        labels[1] = createLabel(secondRadical, 1);
        labels[1].labelProperty().addListener((o, oV, nV) -> {
            secondRadicalProperty().setValue((ArabicLetterType) nV);
        });
        labels[2] = createLabel(thirdRadical, 2);
        labels[2].labelProperty().addListener((o, oV, nV) -> {
            thirdRadicalProperty().setValue((ArabicLetterType) nV);
        });
        labels[3] = createLabel(fourthRadical, 3);
        labels[3].labelProperty().addListener((o, oV, nV) -> {
            fourthRadicalProperty().setValue((ArabicLetterType) nV);
        });
        selectFirst();

        FlowPane flowPane = new FlowPane();
        flowPane.setHgap(SPACING);
        flowPane.setNodeOrientation(RIGHT_TO_LEFT);
        flowPane.getChildren().addAll(labels[0], labels[1], labels[2], labels[3]);
        flowPane.setAlignment(Pos.TOP_CENTER);

        getChildren().addAll(flowPane, createRow(ROW_1), createRow(ROW_2), createRow(ROW_3));
        setFocusTraversable(true);
        getStyleClass().addAll("popup");

        initializeListeners();

        setRootLetters(firstRadical, secondRadical, thirdRadical, fourthRadical);
    }

    private void initializeListeners() {
        firstRadicalProperty().addListener((o, ov, nv) -> {
            labels[0].setLabel(nv);
        });
        secondRadicalProperty().addListener((o, ov, nv) -> {
            labels[1].setLabel(nv);
        });
        thirdRadicalProperty().addListener((o, ov, nv) -> {
            labels[2].setLabel(nv);
        });
        fourthRadicalProperty().addListener((o, ov, nv) -> {
            labels[3].setLabel(nv);
        });
    }

    public final ArabicLetterType getFirstRadical() {
        return firstRadical.get();
    }

    public final ObjectProperty<ArabicLetterType> firstRadicalProperty() {
        return firstRadical;
    }

    public final ArabicLetterType getSecondRadical() {
        return secondRadical.get();
    }

    public final ObjectProperty<ArabicLetterType> secondRadicalProperty() {
        return secondRadical;
    }

    public final ArabicLetterType getThirdRadical() {
        return thirdRadical.get();
    }

    public final ObjectProperty<ArabicLetterType> thirdRadicalProperty() {
        return thirdRadical;
    }

    public final ArabicLetterType getFourthRadical() {
        return fourthRadical.get();
    }

    public final ObjectProperty<ArabicLetterType> fourthRadicalProperty() {
        return fourthRadical;
    }

    @Override
    public String getUserAgentStylesheet() {
        return getResource("sarf-engine-ui.css").toExternalForm();
    }

    private ArabicLabelView createLabel(ArabicLetterType letter, int index) {
        ArabicLabelView label = new ArabicLabelView(letter);
        label.readonlySelectedProperty().addListener((o, oV, nV) -> {
            if (currentView != null) {
                currentView.setSelected(false);
            }
            currentIndex = index;
            currentView = labels[currentIndex];
        });
        label.setGroup(group);
        label.setLabelWidth(32);
        label.setLabelHeight(32);
        label.setFont(FONT);
        return label;
    }

    private FlowPane createRow(ArabicLetterType[] row) {
        FlowPane flowPane = new FlowPane();
        flowPane.setHgap(SPACING);
        flowPane.setMinWidth(700);

        for (ArabicLetterType arabicLetterType : row) {
            flowPane.getChildren().add(createKeyBoardButton(arabicLetterType));
        }
        return flowPane;
    }

    private Button createKeyBoardButton(ArabicLetterType letter) {
        Button button = new Button();
        button.setStyle("-fx-base: beige;");
        boolean disable = (letter == null);
        button.setDisable(disable);
        button.setPrefSize(PREF_WIDTH, PREF_HEIGHT);
        button.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        button.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        Text graphic = null;
        if (!disable) {
            graphic = new Text(letter.toUnicode());
            graphic.setFont(FONT);
        }
        button.setGraphic(graphic);
        button.setOnAction(event -> {
            if (currentView != null) {
                currentView.setLabel(letter);
                currentView.setSelected(false);
            }
            currentIndex = (currentIndex + 1) % 4;
            currentView = labels[currentIndex];
            currentView.setSelected(true);
        });
        return button;
    }

    public final ArabicLetterType[] getRootLetters() {
        return new ArabicLetterType[]{getFirstRadical(), getSecondRadical(), getThirdRadical(), getFourthRadical()};
    }

    public final void setRootLetters(final ArabicLetterType firstRadical, final ArabicLetterType secondRadical,
                                     final ArabicLetterType thirdRadical, final ArabicLetterType fourthRadical) {
        firstRadicalProperty().setValue(firstRadical);
        secondRadicalProperty().setValue(secondRadical);
        thirdRadicalProperty().setValue(thirdRadical);
        fourthRadicalProperty().setValue(fourthRadical);
    }

    private void selectFirst() {
        currentIndex = 0;
        labels[currentIndex].setSelected(true);
        currentView = labels[currentIndex];
    }
}
