package com.alphasystem.app.sarfengine.ui.control.skin;

import com.alphasystem.app.sarfengine.conjugation.model.*;
import com.alphasystem.app.sarfengine.conjugation.model.sarfsagheer.ActiveLine;
import com.alphasystem.app.sarfengine.conjugation.model.sarfsagheer.AdverbLine;
import com.alphasystem.app.sarfengine.conjugation.model.sarfsagheer.ImperativeAndForbiddingLine;
import com.alphasystem.app.sarfengine.conjugation.model.sarfsagheer.PassiveLine;
import com.alphasystem.app.sarfengine.ui.control.SarfChartView;
import com.alphasystem.arabic.model.ArabicWord;
import com.alphasystem.arabic.ui.ArabicLabelToggleGroup;
import com.alphasystem.arabic.ui.ArabicLabelView;
import com.alphasystem.morphologicalanalysis.morphology.model.RootWord;
import com.alphasystem.morphologicalanalysis.morphology.model.support.SarfTermType;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

import static com.alphasystem.app.sarfengine.docx.ConjugationHelper.*;
import static com.alphasystem.app.sarfengine.ui.Global.*;
import static com.alphasystem.arabic.model.ArabicLetterType.NEW_LINE;
import static com.alphasystem.arabic.model.ArabicLetters.WORD_SPACE;
import static com.alphasystem.arabic.model.ArabicWord.*;
import static javafx.geometry.NodeOrientation.RIGHT_TO_LEFT;
import static javafx.geometry.Pos.CENTER_RIGHT;
import static javafx.scene.paint.Color.DODGERBLUE;
import static javafx.scene.paint.Color.TRANSPARENT;
import static org.apache.commons.lang3.ArrayUtils.*;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author sali
 */
public class SarfChartSkin extends SkinBase<SarfChartView> {

    private static final int NUM_OF_COLUMNS = 3;
    private static final int TOTAL_NUM_OF_COLUMNS = NUM_OF_COLUMNS * 2;
    private static final int WIDTH = 128;
    private static final int HEIGHT = 64;
    private static final int SPACING = 5;
    private static final double TOTAL_WIDTH = WIDTH * TOTAL_NUM_OF_COLUMNS + SPACING;

    private BorderPane viewPane;

    public SarfChartSkin(SarfChartView control) {
        super(control);

        viewPane = new BorderPane();
        getSkinnable().sarfChartProperty().addListener((o, ov, nv) -> initializeSkin(nv));

        initializeSkin(control.getSarfChart());
        getChildren().add(viewPane);
    }

    private void initializeSkin(SarfChart sarfChart) {
        viewPane.setCenter(new Pane());
        if (sarfChart != null) {
            VBox vBox = new VBox();
            vBox.setSpacing(SPACING);

            ConjugationHeader chartHeader = sarfChart.getHeader();
            vBox.getChildren().addAll(layoutHeader(chartHeader), createSarfSagheer(sarfChart.getSarfSagheer()));

            SarfKabeer sarfKabeer = sarfChart.getSarfKabeer();
            Node[] nodes = new Node[0];
            nodes = add(nodes, layoutSarfKabeerPair(sarfKabeer.getActiveTensePair()));
            nodes = layoutSarfKabeerPairs(nodes, sarfKabeer.getVerbalNounPairs());
            nodes = add(nodes, layoutSarfKabeerPair(sarfKabeer.getActiveParticiplePair()));
            nodes = add(nodes, layoutSarfKabeerPair(sarfKabeer.getPassiveTensePair()));
            nodes = add(nodes, layoutSarfKabeerPair(sarfKabeer.getImperativeAndForbiddingPair()));
            nodes = layoutSarfKabeerPairs(nodes, sarfKabeer.getAdverbPairs());
            vBox.getChildren().addAll(nodes);
            viewPane.setCenter(vBox);
        }
    }

    private HBox layoutHeader(ConjugationHeader conjugationHeader) {
        HBox hBox = new HBox();

        GridPane gridPane = new GridPane();

        gridPane.add(createTitle(conjugationHeader), 0, 0, 4, 1);

        HBox subBox = new HBox();
        subBox.setSpacing(SPACING);
        subBox.getChildren().add(createTranslation(conjugationHeader));
        subBox.getChildren().add(createConjugationTypeDetails(conjugationHeader));
        gridPane.add(subBox, 0, 1, 4, 1);

        hBox.getChildren().add(gridPane);
        return hBox;
    }

    private ArabicLabelView createTitle(ConjugationHeader conjugationHeader) {
        ArabicLabelView labelView = new ArabicLabelView();
        labelView.setLabel(conjugationHeader.getTitle());
        labelView.setDisabledStroke(TRANSPARENT);
        labelView.setFont(ARABIC_FONT_48);
        labelView.setDisable(true);
        labelView.setStroke(DODGERBLUE);
        labelView.setHeight(HEIGHT);
        labelView.setWidth(TOTAL_WIDTH);
        return labelView;
    }

    private ArabicLabelView createConjugationTypeDetails(ConjugationHeader conjugationHeader) {
        ArabicLabelView labelView = new ArabicLabelView();
        labelView.setFont(ARABIC_FONT_20);
        ArabicWord label = concatenate(WORD_SPACE, conjugationHeader.getTypeLabel1(),
                getWord(NEW_LINE), WORD_SPACE, conjugationHeader.getTypeLabel2(), getWord(NEW_LINE), WORD_SPACE,
                conjugationHeader.getTypeLabel3());
        labelView.setLabel(label);
        labelView.setAlignment(CENTER_RIGHT);
        labelView.setWidth(WIDTH * NUM_OF_COLUMNS);
        labelView.setHeight(HEIGHT * 2);
        labelView.setDisable(true);
        return labelView;
    }

    private ArabicLabelView createTranslation(ConjugationHeader conjugationHeader) {
        ArabicLabelView labelView = new ArabicLabelView();
        labelView.setFont(Font.font("Candara", 12.0));
        labelView.setWidth(WIDTH * NUM_OF_COLUMNS);
        labelView.setHeight(HEIGHT * 2);
        labelView.setDisable(true);
        String translation = conjugationHeader.getTranslation();
        if (isNotBlank(translation)) {
            labelView.setText(translation);
        }
        return labelView;
    }

    private VBox createSarfSagheer(SarfSagheer sarfSagheer) {
        VBox vBox = new VBox();

        vBox.getChildren().add(createActiveLine(sarfSagheer.getActiveLine()));
        PassiveLine passiveLine = sarfSagheer.getPassiveLine();
        if (passiveLine != null) {
            vBox.getChildren().add(createPassiveLine(passiveLine));
        }
        vBox.getChildren().add(createImperativeAndForbiddingLine(sarfSagheer.getImperativeAndForbiddingLine()));
        AdverbLine adverbLine = sarfSagheer.getAdverbLine();
        if (adverbLine != null) {
            vBox.getChildren().add(createAdverbLine(adverbLine));
        }
        return vBox;
    }

    private GridPane createActiveLine(ActiveLine activeLine) {
        GridPane gridPane = new GridPane();

        double width = TOTAL_WIDTH / 4;
        gridPane.add(createRootWord(activeLine.getActiveParticipleMasculine().getRootWord(), width), 0, 0);
        ArabicWord arabicWord = getConcatenateWithAnd(activeLine.getVerbalNouns());
        gridPane.add(createRootWord(arabicWord, width), 1, 0);
        gridPane.add(createRootWord(activeLine.getPresentTense().getRootWord(), width), 2, 0);
        gridPane.add(createRootWord(activeLine.getPastTense().getRootWord(), width), 3, 0);

        return gridPane;
    }

    private GridPane createPassiveLine(PassiveLine passiveLine) {
        GridPane gridPane = new GridPane();

        double width = TOTAL_WIDTH / 4;
        gridPane.add(createRootWord(passiveLine.getPassiveParticipleMasculine().getRootWord(), width), 0, 0);
        ArabicWord arabicWord = getConcatenateWithAnd(passiveLine.getVerbalNouns());
        gridPane.add(createRootWord(arabicWord, width), 1, 0);
        gridPane.add(createRootWord(passiveLine.getPresentPassiveTense().getRootWord(), width), 2, 0);
        gridPane.add(createRootWord(passiveLine.getPastPassiveTense().getRootWord(), width), 3, 0);

        return gridPane;
    }

    private GridPane createImperativeAndForbiddingLine(ImperativeAndForbiddingLine imperativeAndForbiddingLine) {
        GridPane gridPane = new GridPane();

        double width = TOTAL_WIDTH / 2;
        ArabicWord rootWord = imperativeAndForbiddingLine.getForbidding().getRootWord();
        ArabicWord arabicWord = concatenateWithSpace(FORBIDDING_PREFIX, rootWord);
        gridPane.add(createRootWord(arabicWord, width), 0, 0);

        rootWord = imperativeAndForbiddingLine.getImperative().getRootWord();
        arabicWord = concatenateWithSpace(COMMAND_PREFIX, rootWord);
        gridPane.add(createRootWord(arabicWord, width), 1, 0);
        return gridPane;
    }

    private GridPane createAdverbLine(AdverbLine adverbLine) {
        GridPane gridPane = new GridPane();

        ArabicWord arabicWord = concatenateWithSpace(ZARF_PREFIX, getConcatenateWithAnd(adverbLine.getAdverbs()));
        gridPane.add(createRootWord(arabicWord, TOTAL_WIDTH), 1, 0);

        return gridPane;
    }

    private ArabicLabelView createRootWord(ArabicWord rootWord, double width) {
        ArabicLabelToggleGroup labelGroup = new ArabicLabelToggleGroup();
        labelGroup.setWidth(width);
        labelGroup.setHeight(HEIGHT);
        labelGroup.setFont(ARABIC_FONT_30);
        labelGroup.setDisable(true);

        ArabicLabelView labelView = new ArabicLabelView();
        labelView.setGroup(labelGroup);
        labelView.setLabel(rootWord);

        return labelView;
    }

    private ArabicWord getConcatenateWithAnd(RootWord[] rootWords) {
        ArabicWord arabicWord = null;
        if (!isEmpty(rootWords)) {
            arabicWord = concatenate(rootWords[0].getLabel());
            for (int i = 1; i < rootWords.length; i++) {
                RootWord rootWord = rootWords[i];
                if (rootWord == null) {
                    continue;
                }
                arabicWord = concatenateWithAnd(arabicWord, rootWord.getLabel());
            }
        }
        return arabicWord;
    }

    private Node[] layoutSarfKabeerPairs(Node[] nodes, SarfKabeerPair[] sarfKabeerPairs) {
        if (sarfKabeerPairs != null) {
            for (SarfKabeerPair verbalNounPair : sarfKabeerPairs) {
                nodes = add(nodes, layoutSarfKabeerPair(verbalNounPair));
            }
        }
        return nodes;
    }

    private HBox layoutSarfKabeerPair(SarfKabeerPair sarfKabeerPair) {
        HBox hBox = new HBox();
        hBox.setSpacing(SPACING);

        GridPane rightSidePane = layoutConjugation(sarfKabeerPair.getRightSideStack());
        GridPane leftSidePane = layoutConjugation(sarfKabeerPair.getLeftSideStack());
        hBox.getChildren().addAll(leftSidePane, rightSidePane);

        return hBox;
    }

    private GridPane layoutConjugation(ConjugationStack conjugationStack) {
        GridPane gridPane = new GridPane();
        gridPane.setNodeOrientation(RIGHT_TO_LEFT);

        ArabicLabelToggleGroup labelGroup = new ArabicLabelToggleGroup();
        labelGroup.setWidth(WIDTH);
        labelGroup.setHeight(HEIGHT);
        labelGroup.setFont(ARABIC_FONT_30);
        labelGroup.setDisable(true);

        int row = 0;
        int column = 0;

        SarfTermType sarfTermType = conjugationStack.getLabel();
        ArabicLabelView label = new ArabicLabelView();
        label.setGroup(labelGroup);
        label.setWidth(WIDTH * NUM_OF_COLUMNS);
        label.setStroke(DODGERBLUE);
        Paint disabledStroke = conjugationStack.isEmpty() ? TRANSPARENT : label.getDisabledStroke();
        label.setDisabledStroke(disabledStroke);
        label.setLabel((sarfTermType == null) ? null : sarfTermType.getLabel());
        gridPane.add(label, column, row, NUM_OF_COLUMNS, 1);

        RootWord[] conjugations = conjugationStack.getConjugations();
        int length = conjugations.length;
        row++;
        int startIndex = 0;
        int endIndex = NUM_OF_COLUMNS;
        while (startIndex < length) {
            RootWord[] subValues = subarray(conjugations, startIndex, endIndex);
            for (int i = subValues.length - 1; i >= 0; i--) {
                RootWord rootWord = subValues[i];
                label = new ArabicLabelView();
                label.setLabel(rootWord);
                label.setDisabledStroke(disabledStroke);
                label.setGroup(labelGroup);
                gridPane.add(label, column++, row);
            }
            row++;
            column = 0;
            startIndex = endIndex;
            endIndex += NUM_OF_COLUMNS;
        }
        return gridPane;
    }

}
