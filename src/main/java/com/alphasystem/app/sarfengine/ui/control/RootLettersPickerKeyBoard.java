package com.alphasystem.app.sarfengine.ui.control;

import com.alphasystem.arabic.model.ArabicLetterType;
import com.alphasystem.arabic.ui.ArabicLabelToggleGroup;
import com.alphasystem.arabic.ui.ArabicLabelView;
import com.alphasystem.sarfengine.xml.model.RootLetters;
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
    private final ObjectProperty<RootLetters> rootLetters = new SimpleObjectProperty<>();
    private int currentIndex;
    private ArabicLabelView currentView;

    /**
     * @param rootLetters current {@link RootLetters}
     */
    public RootLettersPickerKeyBoard(final RootLetters rootLetters) {
        group = new ArabicLabelToggleGroup();
        group.setMultipleSelect(false);
        group.setWidth(32);
        group.setHeight(32);

        setPadding(new Insets(SPACING, SPACING, SPACING, SPACING));
        setSpacing(SPACING);

        labels[0] = createLabel(rootLetters.getFirstRadical(), 0);
        labels[0].labelProperty().addListener((o, oV, nV) -> {
            getRootLetters().setFirstRadical((ArabicLetterType) nV);
        });
        labels[1] = createLabel(rootLetters.getSecondRadical(), 1);
        labels[1].labelProperty().addListener((o, oV, nV) -> {
            getRootLetters().setSecondRadical((ArabicLetterType) nV);
        });
        labels[2] = createLabel(rootLetters.getThirdRadical(), 2);
        labels[2].labelProperty().addListener((o, oV, nV) -> {
            getRootLetters().setThirdRadical((ArabicLetterType) nV);
        });
        labels[3] = createLabel(rootLetters.getFourthRadical(), 3);
        labels[3].labelProperty().addListener((o, oV, nV) -> {
            getRootLetters().setFourthRadical((ArabicLetterType) nV);
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

        rootLettersProperty().addListener((o, oV, nV) -> {
            if (nV != null) {
                labels[0].setLabel(nV.getFirstRadical());
                labels[1].setLabel(nV.getSecondRadical());
                labels[2].setLabel(nV.getThirdRadical());
                labels[3].setLabel(nV.getFourthRadical());
            }
        });
        setRootLetters(rootLetters);
    }

    public final ObjectProperty<RootLetters> rootLettersProperty() {
        return rootLetters;
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

    public final RootLetters getRootLetters() {
        RootLetters rootLetters = this.rootLetters.get();
        if (rootLetters == null) {
            setRootLetters(new RootLetters());
            rootLetters = this.rootLetters.get();
        }
        return rootLetters;
    }

    public final void setRootLetters(RootLetters rootLetters) {
        this.rootLetters.set(rootLetters);
    }

    private void selectFirst() {
        currentIndex = 0;
        labels[currentIndex].setSelected(true);
        currentView = labels[currentIndex];
    }
}
