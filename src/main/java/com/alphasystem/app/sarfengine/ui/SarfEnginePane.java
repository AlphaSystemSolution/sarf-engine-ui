package com.alphasystem.app.sarfengine.ui;

import com.alphasystem.app.sarfengine.ui.control.AdverbTableCell;
import com.alphasystem.app.sarfengine.ui.control.RootLettersTableCell;
import com.alphasystem.app.sarfengine.ui.control.VerbalNounTableCell;
import com.alphasystem.app.sarfengine.ui.control.model.TableModel;
import com.alphasystem.arabic.model.NamedTemplate;
import com.alphasystem.sarfengine.xml.model.*;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.List;

import static com.alphasystem.app.sarfengine.ui.Global.ARABIC_FONT_24;
import static com.alphasystem.arabic.ui.ComboBoxHelper.createComboBox;
import static java.lang.String.format;
import static javafx.collections.FXCollections.observableArrayList;
import static javafx.geometry.NodeOrientation.RIGHT_TO_LEFT;
import static javafx.scene.control.ContentDisplay.GRAPHIC_ONLY;
import static javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED;
import static javafx.scene.control.SelectionMode.SINGLE;
import static javafx.scene.text.TextAlignment.CENTER;

/**
 * @author sali
 */
public class SarfEnginePane extends BorderPane {

    @SuppressWarnings({"unchecked"})
    public SarfEnginePane(ConjugationTemplate conjugationTemplate) {
        if (conjugationTemplate == null) {
            conjugationTemplate = new ConjugationTemplate();
        }

        ObservableList<TableModel> tableModels = observableArrayList();
        List<ConjugationData> dataList = conjugationTemplate.getData();
        dataList.forEach(data -> tableModels.add(new TableModel(data)));
        tableModels.add(new TableModel());
        TableView<TableModel> tableView1 = new TableView<>(tableModels);
        tableView1.getSelectionModel().setSelectionMode(SINGLE);
        tableView1.setPrefSize(1000, 640);
        tableView1.setEditable(true);

        // start adding columns
        TableColumn<TableModel, RootLetters> rootLettersColumn = new TableColumn<>();
        rootLettersColumn.setText("Root\nLetters");
        rootLettersColumn.setPrefWidth(200);
        rootLettersColumn.setEditable(true);
        rootLettersColumn.setCellValueFactory(new PropertyValueFactory<>("rootLetters"));
        rootLettersColumn.setCellFactory(RootLettersTableCell::new);

        TableColumn<TableModel, NamedTemplate> templateColumn = new TableColumn<>();
        templateColumn.setText("Form");
        templateColumn.setEditable(true);
        templateColumn.setPrefWidth(200);
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

                comboBox.focusedProperty().addListener((o, ov, nv) -> {
                    if (nv) {
                        getTableView().edit(getIndex(), getTableColumn());
                    } else {
                        commitEdit(comboBox.getSelectionModel().getSelectedItem());
                    }
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
        verbalNounsColumn.setText("Verbal\nNouns");
        verbalNounsColumn.setPrefWidth(200);
        verbalNounsColumn.setEditable(true);
        verbalNounsColumn.setCellValueFactory(new PropertyValueFactory<>("verbalNouns"));
        verbalNounsColumn.setCellFactory(VerbalNounTableCell::new);

        TableColumn<TableModel, ObservableList<NounOfPlaceAndTime>> adverbsColumn = new TableColumn<>();
        adverbsColumn.setText("Adverbs");
        adverbsColumn.setPrefWidth(200);
        adverbsColumn.setEditable(true);
        adverbsColumn.setCellValueFactory(new PropertyValueFactory<>("adverbs"));
        adverbsColumn.setCellFactory(AdverbTableCell::new);

        //TODO: figure out how to refresh Verbal Noun column with new values
        templateColumn.setOnEditCommit(event -> {
            NamedTemplate newValue = event.getNewValue();
            TableView<TableModel> tableView = event.getTableView();
            TableModel selectedItem = tableView.getSelectionModel().getSelectedItem();

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

        tableView1.getColumns().addAll(rootLettersColumn, templateColumn, verbalNounsColumn, adverbsColumn);
        ScrollPane scrollPane = new ScrollPane(tableView1);
        scrollPane.setVbarPolicy(AS_NEEDED);
        scrollPane.setHbarPolicy(AS_NEEDED);

        setCenter(scrollPane);
    }

}
