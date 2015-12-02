package com.alphasystem.app.sarfengine.ui;

import com.alphasystem.ApplicationException;
import com.alphasystem.app.sarfengine.ui.control.AdverbTableCell;
import com.alphasystem.app.sarfengine.ui.control.FileSelectionDialog;
import com.alphasystem.app.sarfengine.ui.control.RootLettersTableCell;
import com.alphasystem.app.sarfengine.ui.control.VerbalNounTableCell;
import com.alphasystem.app.sarfengine.ui.control.model.TabInfo;
import com.alphasystem.app.sarfengine.ui.control.model.TableModel;
import com.alphasystem.app.sarfengine.util.TemplateReader;
import com.alphasystem.arabic.model.NamedTemplate;
import com.alphasystem.sarfengine.xml.model.*;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static com.alphasystem.app.sarfengine.ui.Global.*;
import static com.alphasystem.arabic.ui.ComboBoxHelper.createComboBox;
import static com.alphasystem.util.AppUtil.getResourceAsStream;
import static java.lang.Math.max;
import static java.lang.String.format;
import static javafx.application.Platform.runLater;
import static javafx.collections.FXCollections.observableArrayList;
import static javafx.geometry.NodeOrientation.RIGHT_TO_LEFT;
import static javafx.scene.control.Alert.AlertType.CONFIRMATION;
import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.ContentDisplay.GRAPHIC_ONLY;
import static javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED;
import static javafx.scene.control.SelectionMode.SINGLE;
import static javafx.scene.control.TabPane.TabClosingPolicy.SELECTED_TAB;
import static javafx.scene.control.cell.CheckBoxTableCell.forTableColumn;
import static javafx.scene.text.TextAlignment.CENTER;
import static javafx.stage.Screen.getPrimary;

/**
 * @author sali
 */
public class SarfEnginePane extends BorderPane {

    private static final double DEFAULT_MIN_HEIGHT = 500.0;
    private static final double ROW_SIZE = 40.0;
    private static int counter = 1;

    private final TabPane tabPane;
    private final FileSelectionDialog dialog;
    private final TemplateReader templateReader = TemplateReader.getInstance();

    @SuppressWarnings({"unchecked"})
    public SarfEnginePane() {
        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(SELECTED_TAB);
        tabPane.getTabs().add(createTab());

        dialog = new FileSelectionDialog(new TabInfo());

        setCenter(tabPane);
        setTop(createToolBar());
    }

    private static String createNewTabTitle() {
        return format("Untitled %s", counter++);
    }

    private static double calculateTableHeight(int numOfRows) {
        double height = numOfRows * ROW_SIZE + ROW_SIZE;
        height = roundTo100(height);
        return max(height, DEFAULT_MIN_HEIGHT);
    }

    private Tab getCurrentTab() {
        return tabPane.getSelectionModel().getSelectedItem();
    }

    private TabInfo getTabUserData() {
        Tab currentTab = getCurrentTab();
        return (currentTab == null) ? null : ((TabInfo) currentTab.getUserData());
    }

    @SuppressWarnings({"unchecked"})
    private TableView<TableModel> getCurrentTable() {
        TableView<TableModel> tableView = null;
        Tab currentTab = getCurrentTab();
        if (currentTab != null) {
            ScrollPane scrollPane = (ScrollPane) currentTab.getContent();
            tableView = (TableView<TableModel>) scrollPane.getContent();
        }
        return tableView;
    }

    private void makeDirty(boolean dirty) {
        TabInfo tabInfo = getTabUserData();
        if (tabInfo != null) {
            tabInfo.setDirty(dirty);
        }
    }

    private Tab createTab() {
        Tab tab = new Tab(createNewTabTitle(), createTable(null));
        tab.setUserData(new TabInfo());
        tab.setOnCloseRequest(event -> {
            TabInfo tabInfo = getTabUserData();
            if (tabInfo.getDirty()) {
                Alert alert = new Alert(CONFIRMATION);
                alert.setContentText("Do you  want to save data before closing?");
                Optional<ButtonType> result = alert.showAndWait();
                ButtonType buttonType = result.get();
                ButtonBar.ButtonData buttonData = buttonType.getButtonData();
                if (buttonData.isDefaultButton()) {
                    saveAction(SaveMode.SAVE);
                } else {
                    event.consume();
                }
            }

        });
        return tab;
    }

    private ScrollPane createTable(ConjugationTemplate conjugationTemplate) {
        if (conjugationTemplate == null) {
            conjugationTemplate = new ConjugationTemplate();
        }
        ObservableList<TableModel> tableModels = observableArrayList();
        List<ConjugationData> dataList = conjugationTemplate.getData();
        if (dataList.isEmpty()) {
            dataList.add(new ConjugationData());
        }
        dataList.forEach(data -> tableModels.add(new TableModel(data)));

        double boundsWidth = getPrimary().getVisualBounds().getWidth();

        TableView<TableModel> tableView = new TableView<>(tableModels);
        tableView.getSelectionModel().setSelectionMode(SINGLE);
        tableView.setEditable(true);
        initializeTable(tableView, boundsWidth);

        tableView.setFixedCellSize(ROW_SIZE);
        tableView.setPrefSize(boundsWidth, calculateTableHeight(tableModels.size()));
        ScrollPane scrollPane = new ScrollPane(tableView);
        scrollPane.setVbarPolicy(AS_NEEDED);
        scrollPane.setHbarPolicy(AS_NEEDED);

        return scrollPane;
    }

    private ToolBar createToolBar() {
        ToolBar toolBar = new ToolBar();

        Button button;

        button = new Button();
        button.setTooltip(new Tooltip("Create New File"));
        button.setGraphic(new ImageView(new Image(getResourceAsStream("images.new-file-icon.png"))));
        button.setOnAction(event -> tabPane.getTabs().add(createTab()));
        toolBar.getItems().add(button);

        button = new Button();
        button.setTooltip(new Tooltip("Open File"));
        button.setGraphic(new ImageView(new Image(getResourceAsStream("images.open-file-icon.png"))));
        button.setOnAction(event -> {

        });
        toolBar.getItems().add(button);

        SplitMenuButton splitMenuButton = new SplitMenuButton();
        splitMenuButton.setText("Save");
        splitMenuButton.setGraphic(new ImageView(new Image(getResourceAsStream("images.save-as-docx-icon.png"))));
        MenuItem menuItem = new MenuItem("Save");
        menuItem.setOnAction(event -> saveAction(SaveMode.SAVE));
        splitMenuButton.getItems().add(menuItem);
        menuItem = new MenuItem("Save As ...");
        menuItem.setOnAction(event -> saveAction(SaveMode.SAVE_AS));
        splitMenuButton.getItems().add(menuItem);
        menuItem = new MenuItem("Save Selected Data ...");
        menuItem.setOnAction(event -> saveAction(SaveMode.SAVE_SELECTED));
        splitMenuButton.getItems().add(menuItem);
        toolBar.getItems().add(splitMenuButton);

        toolBar.getItems().add(new Separator());
        button = new Button();
        button.setTooltip(new Tooltip("Add new Row"));
        button.setGraphic(new ImageView(new Image(getResourceAsStream("images.root-Letters-icon.png"))));
        button.setOnAction(event -> addNewRowAction());
        toolBar.getItems().add(button);

        return toolBar;
    }

    private void addNewRowAction() {
        TableView<TableModel> tableView = getCurrentTable();
        if (tableView != null) {
            ObservableList<TableModel> items = tableView.getItems();
            items.add(new TableModel());
            tableView.setPrefHeight(calculateTableHeight(items.size()));
        }
    }

    private void saveAction(SaveMode saveMode) {
        final TabInfo tabInfo = getTabUserData();
        if (tabInfo != null) {
            if (showDialogIfApplicable(saveMode, tabInfo)) {
                runLater(saveData(saveMode, tabInfo));
            } // end of if "doSave"
        } // end of if "tabInfo != null"
    }

    private Runnable saveData(final SaveMode saveMode, final TabInfo tabInfo) {
        return () -> {
            TableView<TableModel> tableView = getCurrentTable();
            ObservableList<TableModel> items = tableView.getItems();
            final ObservableList<TableModel> currentItems = observableArrayList();
            if (SaveMode.SAVE_SELECTED.equals(saveMode)) {
                items.forEach(tableModel -> {
                    if (tableModel.getChecked()) {
                        currentItems.add(tableModel);
                    }
                });
            } else {
                currentItems.addAll(items);
            }
            try {
                templateReader.saveFile(tabInfo.getSarfxFile(), getConjugationTemplate(currentItems));
                // TODO: save a s DOCX
            } catch (ApplicationException e) {
                e.printStackTrace();
                Alert alert = new Alert(ERROR);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        };
    }

    private ConjugationTemplate getConjugationTemplate(ObservableList<TableModel> items) {
        ConjugationTemplate template = new ConjugationTemplate();
        items.forEach(tableModel -> template.getData().add(tableModel.getConjugationData()));
        // TODO: add ChartConfiguration
        return template;
    }

    private boolean showDialogIfApplicable(SaveMode saveMode, TabInfo tabInfo) {
        File sarfxFile = tabInfo.getSarfxFile();
        boolean showDialog = sarfxFile == null || SaveMode.SAVE_AS.equals(saveMode) ||
                SaveMode.SAVE_SELECTED.equals(saveMode);
        if (showDialog) {
            dialog.setTabInfo(tabInfo);
            if (dialog.getOwner() == null) {
                dialog.initOwner(getScene().getWindow());
            }
            Optional<TabInfo> result = dialog.showAndWait();
            result.ifPresent(ti -> {
                tabInfo.setSarfxFile(ti.getSarfxFile());
                tabInfo.setDocxFile(ti.getDocxFile());
                tabInfo.setDirty(false);
            });
        }// end of if "showDialog"
        return tabInfo.getSarfxFile() != null;
    }


    @SuppressWarnings("unchecked")
    private void initializeTable(TableView<TableModel> tableView, double boundsWidth) {
        double largeColumnWidth = boundsWidth * 20 / 100;
        double mediumColumnWidth = boundsWidth * 8 / 100;
        double smallColumnWidth = boundsWidth * 4 / 100;

        // start adding columns
        TableColumn<TableModel, Boolean> checkedColumn = new TableColumn<>();
        checkedColumn.setPrefWidth(smallColumnWidth);
        checkedColumn.setEditable(true);
        checkedColumn.setCellValueFactory(new PropertyValueFactory<>("checked"));
        checkedColumn.setCellFactory(forTableColumn(checkedColumn));

        TableColumn<TableModel, RootLetters> rootLettersColumn = new TableColumn<>();
        rootLettersColumn.setText("Root Letters");
        rootLettersColumn.setPrefWidth(largeColumnWidth);
        rootLettersColumn.setEditable(true);
        rootLettersColumn.setCellValueFactory(new PropertyValueFactory<>("rootLetters"));
        rootLettersColumn.setCellFactory(RootLettersTableCell::new);
        rootLettersColumn.setOnEditCommit(event -> makeDirty(true));

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
                labelText.setFont(ENGLISH_FONT);

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
        verbalNounsColumn.setOnEditCommit(event -> {
            makeDirty(true);
            TableView<TableModel> table = event.getTableView();
            TableModel selectedItem = table.getSelectionModel().getSelectedItem();
            selectedItem.getVerbalNouns().clear();
            selectedItem.getVerbalNouns().addAll(event.getNewValue());
        });

        TableColumn<TableModel, ObservableList<NounOfPlaceAndTime>> adverbsColumn = new TableColumn<>();
        adverbsColumn.setText("Adverbs");
        adverbsColumn.setPrefWidth(largeColumnWidth);
        adverbsColumn.setEditable(true);
        adverbsColumn.setCellValueFactory(new PropertyValueFactory<>("adverbs"));
        adverbsColumn.setCellFactory(AdverbTableCell::new);
        adverbsColumn.setOnEditCommit(event -> {
            makeDirty(true);
            TableView<TableModel> table = event.getTableView();
            TableModel selectedItem = table.getSelectionModel().getSelectedItem();
            selectedItem.getAdverbs().clear();
            selectedItem.getAdverbs().addAll(event.getNewValue());
        });

        //TODO: figure out how to refresh Verbal Noun column with new values
        templateColumn.setOnEditCommit(event -> {
            makeDirty(true);
            NamedTemplate newValue = event.getNewValue();
            TableView<TableModel> table = event.getTableView();
            TableModel selectedItem = table.getSelectionModel().getSelectedItem();
            selectedItem.setTemplate(newValue);

            // TODO: figure out how to update table
            List<VerbalNoun> verbalNouns = Global.VERBAL_NOUN_TEMPLATE_MAPPING.get(newValue);

            // clear the currently selected verbal nouns first then add new values, if there is no verbal noun mapped
            // then our list should be empty
            selectedItem.getVerbalNouns().clear();
            if (verbalNouns != null) {
                selectedItem.getVerbalNouns().addAll(verbalNouns);

            }

            List<NounOfPlaceAndTime> adverbs = Global.ADVERB_TEMPLATE_MAPPING.get(newValue);

            // clear the currently selected adverbs first then add new values, if there is no adverb mapped
            // then our list should be empty
            selectedItem.getAdverbs().clear();
            if (adverbs != null) {
                selectedItem.getAdverbs().addAll(adverbs);
            }
        });

        TableColumn<TableModel, Boolean> removePassiveLineColumn = new TableColumn<>();
        removePassiveLineColumn.setText("Remove\nPassive\nLine");
        removePassiveLineColumn.setPrefWidth(mediumColumnWidth);
        removePassiveLineColumn.setEditable(true);
        removePassiveLineColumn.setCellValueFactory(new PropertyValueFactory<>("removePassiveLine"));
        removePassiveLineColumn.setCellFactory(forTableColumn(removePassiveLineColumn));
        removePassiveLineColumn.setOnEditCommit(event -> makeDirty(true));

        TableColumn<TableModel, Boolean> skipRuleProcessingColumn = new TableColumn<>();
        skipRuleProcessingColumn.setText("Skip\nRule\nProcessing");
        skipRuleProcessingColumn.setPrefWidth(mediumColumnWidth);
        skipRuleProcessingColumn.setEditable(true);
        skipRuleProcessingColumn.setCellValueFactory(new PropertyValueFactory<>("skipRuleProcessing"));
        skipRuleProcessingColumn.setCellFactory(forTableColumn(skipRuleProcessingColumn));
        skipRuleProcessingColumn.setOnEditCommit(event -> makeDirty(true));

        tableView.getColumns().addAll(checkedColumn, rootLettersColumn, templateColumn, verbalNounsColumn,
                adverbsColumn, removePassiveLineColumn, skipRuleProcessingColumn);
    }

    private enum SaveMode {
        SAVE, SAVE_AS, SAVE_SELECTED
    }

}
