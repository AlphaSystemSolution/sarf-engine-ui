package com.alphasystem.app.sarfengine.ui;

import com.alphasystem.app.sarfengine.ui.control.AdverbTableCell;
import com.alphasystem.app.sarfengine.ui.control.RootLettersTableCell;
import com.alphasystem.app.sarfengine.ui.control.VerbalNounTableCell;
import com.alphasystem.app.sarfengine.ui.control.model.TableModel;
import com.alphasystem.arabic.model.NamedTemplate;
import com.alphasystem.sarfengine.xml.model.*;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Screen;

import java.util.List;

import static com.alphasystem.app.sarfengine.ui.Global.ARABIC_FONT_24;
import static com.alphasystem.arabic.ui.ComboBoxHelper.createComboBox;
import static java.lang.String.format;
import static javafx.collections.FXCollections.observableArrayList;
import static javafx.geometry.NodeOrientation.RIGHT_TO_LEFT;
import static javafx.scene.control.ContentDisplay.GRAPHIC_ONLY;
import static javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED;
import static javafx.scene.control.SelectionMode.SINGLE;
import static javafx.scene.control.cell.CheckBoxTableCell.forTableColumn;
import static javafx.scene.input.KeyCode.D;
import static javafx.scene.input.KeyCombination.CONTROL_DOWN;
import static javafx.scene.text.TextAlignment.CENTER;

/**
 * @author sali
 */
public class SarfEnginePane extends BorderPane {

    private final TableView<TableModel> tableView;

    @SuppressWarnings({"unchecked"})
    public SarfEnginePane(ConjugationTemplate conjugationTemplate) {
        if (conjugationTemplate == null) {
            conjugationTemplate = new ConjugationTemplate();
        }

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        double boundsWidth = bounds.getWidth();
        double largeColumnWidth = boundsWidth * 20 / 100;
        double smallColumnWidth = boundsWidth * 9 / 100;

        ObservableList<TableModel> tableModels = observableArrayList();
        List<ConjugationData> dataList = conjugationTemplate.getData();
        dataList.forEach(data -> tableModels.add(new TableModel(data)));
        tableView = new TableView<>(tableModels);
        tableView.getSelectionModel().setSelectionMode(SINGLE);
        tableView.setPrefSize(boundsWidth, 640);
        tableView.setEditable(true);
        initializeTable(largeColumnWidth, smallColumnWidth);

        ScrollPane scrollPane = new ScrollPane(tableView);
        scrollPane.setVbarPolicy(AS_NEEDED);
        scrollPane.setHbarPolicy(AS_NEEDED);

        setCenter(scrollPane);

        MenuItem menuItem = new MenuItem("Debug");
        menuItem.setOnAction(event -> {
            ObservableList<TableModel> items = tableView.getItems();
            items.forEach(tableModel -> {
                RootLetters rootLetters = tableModel.getRootLetters();
                System.out.println(format("Template: %s, Root Letters: %s %s %s", tableModel.getTemplate(),
                        rootLetters.getFirstRadical().toCode(), rootLetters.getSecondRadical().toCode(),
                        rootLetters.getThirdRadical().toCode()));
            });
        });
        menuItem.setAccelerator(new KeyCodeCombination(D, CONTROL_DOWN));

        MenuBar menuBar = new MenuBar();

        Menu menu = new Menu("File");
        menu.getItems().add(menuItem);
        menuBar.getMenus().add(menu);
        setTop(menuBar);
    }

    @SuppressWarnings("unchecked")
    private void initializeTable(double largeColumnWidth, double smallColumnWidth) {
        // start adding columns
        TableColumn<TableModel, RootLetters> rootLettersColumn = new TableColumn<>();
        rootLettersColumn.setText("Root Letters");
        rootLettersColumn.setPrefWidth(largeColumnWidth);
        rootLettersColumn.setEditable(true);
        rootLettersColumn.setCellValueFactory(new PropertyValueFactory<>("rootLetters"));
        rootLettersColumn.setCellFactory(RootLettersTableCell::new);

        TableColumn<TableModel, NamedTemplate> templateColumn = new TableColumn<>();
        templateColumn.setText("Form");
        templateColumn.setEditable(true);
        templateColumn.setPrefWidth(largeColumnWidth);
        templateColumn.setCellValueFactory(new PropertyValueFactory<>("template"));

        templateColumn.setCellFactory(column -> new ComboBoxTableCell<TableModel, NamedTemplate>(NamedTemplate.values()) {
            private final Text labelText;
            private final Text arabicText;
            private final ComboBox<NamedTemplate> comboBox;

            {
                setContentDisplay(GRAPHIC_ONLY);
                comboBox = createComboBox(NamedTemplate.values());
                arabicText = new Text();
                arabicText.setFont(ARABIC_FONT_24);
                arabicText.setTextAlignment(CENTER);
                arabicText.setNodeOrientation(RIGHT_TO_LEFT);
                labelText = new Text();
                labelText.setTextAlignment(CENTER);
                labelText.setFont(Global.ENGLISH_FONT);

                comboBox.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> {
                    commitEdit(nv);
                });
            }

            @Override
            public void startEdit() {
                if (!isEditable() || !getTableView().isEditable() || !getTableColumn().isEditable()) {
                    return;
                }

                comboBox.getSelectionModel().select(getItem());

                super.startEdit();
                setText(null);
                setGraphic(comboBox);
            }

            @Override
            public void updateItem(NamedTemplate item, boolean empty) {
                super.updateItem(item, empty);

                TextFlow textFlow = new TextFlow();
                Node graphic = null;
                if (item != null && !empty) {
                    labelText.setText(format("(%s) ", item.getCode()));
                    arabicText.setText(item.getLabel().toUnicode());
                    textFlow.getChildren().addAll(labelText, arabicText);
                    graphic = new Group(textFlow);
                }
                setGraphic(graphic);
            }
        });

        TableColumn<TableModel, ObservableList<VerbalNoun>> verbalNounsColumn = new TableColumn<>();
        verbalNounsColumn.setText("Verbal Nouns");
        verbalNounsColumn.setPrefWidth(largeColumnWidth);
        verbalNounsColumn.setEditable(true);
        verbalNounsColumn.setCellValueFactory(new PropertyValueFactory<>("verbalNouns"));
        verbalNounsColumn.setCellFactory(VerbalNounTableCell::new);

        TableColumn<TableModel, ObservableList<NounOfPlaceAndTime>> adverbsColumn = new TableColumn<>();
        adverbsColumn.setText("Adverbs");
        adverbsColumn.setPrefWidth(largeColumnWidth);
        adverbsColumn.setEditable(true);
        adverbsColumn.setCellValueFactory(new PropertyValueFactory<>("adverbs"));
        adverbsColumn.setCellFactory(AdverbTableCell::new);

        //TODO: figure out how to refresh Verbal Noun column with new values
        templateColumn.setOnEditCommit(event -> {
            NamedTemplate newValue = event.getNewValue();
            TableView<TableModel> tableView = event.getTableView();
            TableModel selectedItem = tableView.getSelectionModel().getSelectedItem();
            selectedItem.setTemplate(newValue);

            // TODO: figure out how to update table
            List<VerbalNoun> verbalNouns = Global.VERBAL_NOUN_TEMPLATE_MAPPING.get(newValue);
            if (verbalNouns != null) {
                selectedItem.getVerbalNouns().clear();
                selectedItem.getVerbalNouns().addAll(verbalNouns);

            }

            List<NounOfPlaceAndTime> adverbs = Global.ADVERB_TEMPLATE_MAPPING.get(newValue);
            if (adverbs != null) {
                selectedItem.getAdverbs().clear();
                selectedItem.getAdverbs().addAll(adverbs);
            }
        });

        TableColumn<TableModel, Boolean> removePassiveLineColumn = new TableColumn<>();
        removePassiveLineColumn.setText("Remove Passive Line");
        removePassiveLineColumn.setPrefWidth(smallColumnWidth);
        removePassiveLineColumn.setEditable(true);
        removePassiveLineColumn.setCellValueFactory(new PropertyValueFactory<>("removePassiveLine"));
        removePassiveLineColumn.setCellFactory(forTableColumn(removePassiveLineColumn));

        TableColumn<TableModel, Boolean> skipRuleProcessingColumn = new TableColumn<>();
        skipRuleProcessingColumn.setText("Skip Rule Processing");
        skipRuleProcessingColumn.setPrefWidth(smallColumnWidth);
        skipRuleProcessingColumn.setEditable(true);
        skipRuleProcessingColumn.setCellValueFactory(new PropertyValueFactory<>("skipRuleProcessing"));
        skipRuleProcessingColumn.setCellFactory(forTableColumn(skipRuleProcessingColumn));

        tableView.getColumns().addAll(rootLettersColumn, templateColumn, verbalNounsColumn, adverbsColumn,
                removePassiveLineColumn, skipRuleProcessingColumn);
    }

}
