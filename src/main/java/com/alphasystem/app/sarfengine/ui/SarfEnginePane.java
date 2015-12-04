package com.alphasystem.app.sarfengine.ui;

import com.alphasystem.ApplicationException;
import com.alphasystem.app.sarfengine.docx.SarfEngineHelper;
import com.alphasystem.app.sarfengine.ui.control.*;
import com.alphasystem.app.sarfengine.ui.control.model.TabInfo;
import com.alphasystem.app.sarfengine.ui.control.model.TableModel;
import com.alphasystem.app.sarfengine.util.TemplateReader;
import com.alphasystem.arabic.model.NamedTemplate;
import com.alphasystem.sarfengine.xml.model.*;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import static com.alphasystem.app.sarfengine.ui.Global.*;
import static com.alphasystem.app.sarfengine.util.TemplateReader.getDocxFile;
import static com.alphasystem.arabic.ui.ComboBoxHelper.createComboBox;
import static com.alphasystem.util.AppUtil.getResourceAsStream;
import static java.lang.Math.max;
import static java.lang.String.format;
import static javafx.application.Platform.runLater;
import static javafx.collections.FXCollections.observableArrayList;
import static javafx.geometry.NodeOrientation.RIGHT_TO_LEFT;
import static javafx.scene.control.Alert.AlertType.*;
import static javafx.scene.control.ButtonType.*;
import static javafx.scene.control.ContentDisplay.GRAPHIC_ONLY;
import static javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED;
import static javafx.scene.control.SelectionMode.SINGLE;
import static javafx.scene.control.TabPane.TabClosingPolicy.SELECTED_TAB;
import static javafx.scene.control.cell.CheckBoxTableCell.forTableColumn;
import static javafx.scene.input.KeyCode.S;
import static javafx.scene.input.KeyCombination.ALT_DOWN;
import static javafx.scene.input.KeyCombination.CONTROL_DOWN;
import static javafx.scene.text.TextAlignment.CENTER;
import static javafx.stage.Screen.getPrimary;
import static org.apache.commons.io.FilenameUtils.getBaseName;

/**
 * @author sali
 */
public class SarfEnginePane extends BorderPane {

    private static final double DEFAULT_MIN_HEIGHT = 500.0;
    private static final double ROW_SIZE = 40.0;
    private static final Background BACKGROUND = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
    private static int counter = 1;

    private final TabPane tabPane;
    private final FileSelectionDialog fileSelectionDialog;
    private final ChartConfigurationDialog chartConfigurationDialog;
    private final TemplateReader templateReader = TemplateReader.getInstance();

    @SuppressWarnings({"unchecked"})
    public SarfEnginePane() {
        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(SELECTED_TAB);
        tabPane.setBackground(BACKGROUND);
        newAction();

        chartConfigurationDialog = new ChartConfigurationDialog();
        fileSelectionDialog = new FileSelectionDialog(new TabInfo());

        setCenter(tabPane);
        setTop(createToolBar());
        setBackground(BACKGROUND);
    }

    private static String getTabTitle(File file) {
        return (file == null) ? format("Untitled %s", counter++) : getBaseName(file.getAbsolutePath());
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

    private Tab createTab(File file, ConjugationTemplate template) {
        Tab tab = new Tab(getTabTitle(file), createTable(template));
        TabInfo value = new TabInfo();
        if (file != null) {
            value.setSarfxFile(file);
            value.setDocxFile(getDocxFile(file));
        }
        tab.setUserData(value);
        tab.setOnCloseRequest(event -> {
            TabInfo tabInfo = getTabUserData();
            if (tabInfo.getDirty()) {
                Alert alert = new Alert(CONFIRMATION);
                alert.setContentText("Do you want to save data before closing?");
                alert.getButtonTypes().setAll(YES, NO, CANCEL);
                Optional<ButtonType> result = alert.showAndWait();
                ButtonType buttonType = result.get();
                ButtonData buttonData = buttonType.getButtonData();
                String text = buttonType.getText();
                if (buttonData.isDefaultButton()) {
                    saveAction(SaveMode.SAVE);
                } else if (text.equals("Cancel")) {
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
        tableView.setBackground(BACKGROUND);
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
        button.setOnAction(event -> newAction());
        toolBar.getItems().add(button);

        button = new Button();
        button.setTooltip(new Tooltip("Open File"));
        button.setGraphic(new ImageView(new Image(getResourceAsStream("images.open-file-icon.png"))));
        button.setOnAction(event -> openAction());
        toolBar.getItems().add(button);

        SplitMenuButton splitMenuButton = new SplitMenuButton();
        splitMenuButton.setText("Save");
        splitMenuButton.setGraphic(new ImageView(new Image(getResourceAsStream("images.save-as-docx-icon.png"))));
        MenuItem menuItem = new MenuItem("Save");
        menuItem.setAccelerator(new KeyCodeCombination(S, CONTROL_DOWN));
        menuItem.setOnAction(event -> saveAction(SaveMode.SAVE));
        splitMenuButton.getItems().add(menuItem);
        menuItem = new MenuItem("Save As ...");
        menuItem.setOnAction(event -> saveAction(SaveMode.SAVE_AS));
        menuItem.setAccelerator(new KeyCodeCombination(S, ALT_DOWN, CONTROL_DOWN));
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

        button = new Button();
        button.setTooltip(new Tooltip("Remove Selected Row"));
        button.setGraphic(new ImageView(new Image(getResourceAsStream("images.button-delete-icon.png"))));
        button.setOnAction(event -> removeRowAction());

        toolBar.getItems().add(new Separator());
        button = new Button();
        button.setTooltip(new Tooltip("View / Edit Chart Configuration"));
        button.setGraphic(new ImageView(new Image(getResourceAsStream("images.settings-icon.png"))));
        button.setOnAction(event -> updateChartConfiguration());
        toolBar.getItems().add(button);

        return toolBar;
    }

    private void removeRowAction() {
        TableView<TableModel> currentTable = getCurrentTable();
        if (currentTable != null) {
            ObservableList<TableModel> items = currentTable.getItems();
            if (items != null && !items.isEmpty()) {
                ListIterator<TableModel> listIterator = items.listIterator();
                while (listIterator.hasNext()) {
                    TableModel model = listIterator.next();
                    if (model.getChecked()) {
                        listIterator.remove();
                    }
                }
            }
        }
    }

    private void updateChartConfiguration() {
        final TabInfo tabInfo = getTabUserData();
        if (tabInfo != null) {
            chartConfigurationDialog.setChartConfiguration(tabInfo.getChartConfiguration());
            Optional<ChartConfiguration> result = chartConfigurationDialog.showAndWait();
            result.ifPresent(tabInfo::setChartConfiguration);
        }

    }

    /**
     * New action.
     *
     * @see SarfEnginePane#openAction(boolean)
     */
    private void newAction() {
        openAction(false);
    }

    /**
     * Open action.
     *
     * @see SarfEnginePane#openAction(boolean)
     */
    private void openAction() {
        openAction(true);
    }

    /**
     * Performs either <strong>OPEN</strong> or <strong>NEW</strong> action. If the <code>showDialog</code> parameter
     * is true then file chooser dialog will get displayed and this method will behave like typical
     * <strong>OPEN</strong> action, if the given parameter is passed then this method will behave like typical
     * <strong>NEW</strong> action.
     * <div>
     * In case of "open" action in following case file not be opened:
     * <ul>
     * <li>If user canceled the file dialog</li>
     * <li>If errors occur reading file</li>
     * </ul>
     * </div>
     *
     * @param showDialog true for "open" action, false for "new" action
     */
    private void openAction(final boolean showDialog) {
        File file = null;
        if (showDialog) {
            file = FILE_CHOOSER.showOpenDialog(getScene().getWindow());
            if (file == null) {
                // use might have cancel the dialog
                return;
            }
        }
        FileOpenService service = new FileOpenService(file);
        service.setOnSucceeded(event -> {
            Tab tab = (Tab) event.getSource().getValue();
            if (tab != null) {
                tabPane.getTabs().add(tab);
                tabPane.getSelectionModel().select(tab);
            }
            changeToDefaultCursor();
        });
        service.setOnFailed(event -> {
            changeToDefaultCursor();
            Alert alert = new Alert(ERROR);
            alert.setContentText("Error occurred while opening document.");
            alert.showAndWait();
        });
        service.start();
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
        changeToWaitCursor();
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
                File sarfxFile = tabInfo.getSarfxFile();
                ConjugationTemplate conjugationTemplate = getConjugationTemplate(currentItems,
                        tabInfo.getChartConfiguration());
                templateReader.saveFile(sarfxFile, conjugationTemplate);
                saveAsDocx(tabInfo, conjugationTemplate);

                Tab currentTab = getCurrentTab();
                currentTab.setText(TemplateReader.getFileNameNoExtension(sarfxFile));
                if (SaveMode.SAVE_SELECTED.equals(saveMode)) {
                    tableView.getItems().clear();
                    tableView.getItems().addAll(currentItems);
                }
            } catch (ApplicationException e) {
                changeToDefaultCursor();
                e.printStackTrace();
                showError(e);
            }
            changeToDefaultCursor();
        };
    }

    private void saveAsDocx(final TabInfo tabInfo, final ConjugationTemplate conjugationTemplate) {
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        changeToWaitCursor();
                        SarfEngineHelper sarfEngineHelper = new SarfEngineHelper();
                        sarfEngineHelper.convert(tabInfo.getDocxFile(), conjugationTemplate);
                        return null;
                    }
                };
            }
        };
        service.setOnSucceeded(event -> {
            makeDirty(false);
            changeToDefaultCursor();
            Alert alert = new Alert(INFORMATION);
            alert.setContentText("Document publishing has been finished.");
            alert.showAndWait();
        });
        service.setOnFailed(event -> {
            changeToDefaultCursor();
            Alert alert = new Alert(ERROR);
            alert.setContentText("Error occurred while publishing document.");
            alert.showAndWait();
        });
        service.start();
    }

    private void showError(Exception ex) {
        Alert alert = new Alert(ERROR);
        alert.setContentText(ex.getMessage());
        alert.showAndWait();
    }

    private ConjugationTemplate getConjugationTemplate(ObservableList<TableModel> items,
                                                       ChartConfiguration chartConfiguration) {
        ConjugationTemplate template = new ConjugationTemplate();
        items.forEach(tableModel -> template.getData().add(tableModel.getConjugationData()));
        template.setChartConfiguration(chartConfiguration);
        return template;
    }

    private boolean showDialogIfApplicable(SaveMode saveMode, TabInfo tabInfo) {
        File sarfxFile = tabInfo.getSarfxFile();
        boolean showDialog = sarfxFile == null || SaveMode.SAVE_AS.equals(saveMode) ||
                SaveMode.SAVE_SELECTED.equals(saveMode);
        if (showDialog) {
            fileSelectionDialog.setTabInfo(tabInfo);
            Optional<TabInfo> result = fileSelectionDialog.showAndWait();
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
                setNodeOrientation(RIGHT_TO_LEFT);
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
                    textFlow.getChildren().addAll(arabicText, createSpaceLabel(), labelText);
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

    public void setDialogOwner(Stage primaryStage) {
        System.out.println("HERE");
        Window owner = fileSelectionDialog.getOwner();
        if (owner == null) {
            fileSelectionDialog.initOwner(primaryStage);
        }
        owner = chartConfigurationDialog.getOwner();
        if (owner == null) {
            chartConfigurationDialog.initOwner(primaryStage);
        }
    }

    private void changeCursor(Cursor cursor) {
        Scene scene = getScene();
        if (scene != null) {
            scene.setCursor(cursor);
        }
    }

    private void changeToDefaultCursor() {
        changeCursor(Cursor.DEFAULT);
    }

    private void changeToWaitCursor() {
        changeCursor(Cursor.WAIT);
    }

    private enum SaveMode {
        SAVE, SAVE_AS, SAVE_SELECTED
    }

    private class FileOpenService extends Service<Tab> {

        private final File file;

        private FileOpenService(File file) {
            this.file = file;
        }

        @Override
        protected Task<Tab> createTask() {
            return new Task<Tab>() {
                @Override
                protected Tab call() throws Exception {
                    changeToWaitCursor();
                    ConjugationTemplate template = file == null ? null : templateReader.readFile(file);
                    return createTab(file, template);
                } // end of method "call"
            }; // end of anonymous class "Task"
        } // end of method "createTask"
    } // end of class "FileOpenService"

}
