package com.alphasystem.app.sarfengine.ui.control;

import com.alphasystem.app.sarfengine.ui.control.model.TabInfo;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import java.io.File;

import static com.alphasystem.app.sarfengine.util.TemplateReader.*;
import static com.alphasystem.util.AppUtil.USER_HOME_DIR;
import static javafx.beans.binding.Bindings.when;
import static javafx.geometry.Pos.CENTER;
import static javafx.scene.control.ButtonType.CANCEL;
import static javafx.scene.control.ButtonType.OK;
import static javafx.stage.Modality.WINDOW_MODAL;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author sali
 */
public class FileSelectionDialog extends Dialog<TabInfo> {

    private static final int DEFAULT_VALUE = 10;
    private final ObjectProperty<TabInfo> tabInfo = new SimpleObjectProperty<>();
    private final StringProperty sarfxFile = new SimpleStringProperty();
    private final FileChooser fileChooser = new FileChooser();

    public FileSelectionDialog(final TabInfo src) {
        super();

        setTitle("Select Files");
        initModality(WINDOW_MODAL);
        fileChooser.setInitialDirectory(USER_HOME_DIR);
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Sarfx Files", SARF_FILE_EXTENSION_ALL));

        tabInfoProperty().addListener((o, ov, nv) -> {
            initDialogPane(nv);
        });
        setTabInfo(src);

        getDialogPane().getButtonTypes().addAll(OK, CANCEL);

        setResultConverter(param -> {
            ButtonData buttonData = param.getButtonData();
            if (buttonData.isDefaultButton()) {
                return tabInfoProperty().get();
            }
            return null;
        });

        Button okButton = (Button) getDialogPane().lookupButton(OK);
        okButton.disableProperty().bind(when(sarfxFile.isNotNull()).then(false).otherwise(true));
    }

    private void initDialogPane(TabInfo tabInfo) {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(DEFAULT_VALUE);
        gridPane.setVgap(DEFAULT_VALUE);
        gridPane.setAlignment(CENTER);
        gridPane.setPadding(new Insets(DEFAULT_VALUE, DEFAULT_VALUE, DEFAULT_VALUE, DEFAULT_VALUE));

        Label label = new Label("Selected Sarfx File:");
        TextField sarfxField = new TextField(getSafeFilePath(tabInfo.getSarfxFile()));
        sarfxField.setPrefColumnCount(30);
        sarfxField.setDisable(true);
        label.setLabelFor(sarfxField);
        Button button = new Button(" ... ");
        button.setOnAction(event -> {
            File file = fileChooser.showSaveDialog(getOwner());
            if (file != null) {
                File sarfxFile = getSarfxFile(file);
                sarfxField.setText(sarfxFile.getAbsolutePath());
            }
        });
        gridPane.add(label, 0, 0);
        gridPane.add(sarfxField, 1, 0);
        gridPane.add(button, 2, 0);

        label = new Label("Selected Docx File:");
        TextField textField = new TextField(getSafeFilePath(tabInfo.getDocxFile()));
        textField.setPrefColumnCount(30);
        textField.setDisable(true);
        sarfxField.textProperty().addListener((o, ov, nv) -> {
            if (!isBlank(nv)) {
                File sarfxFile = getSarfxFile(new File(nv));
                this.sarfxFile.setValue(sarfxFile.getAbsolutePath());
                tabInfo.setSarfxFile(sarfxFile);
                File docxFile = getDocxFile(sarfxFile);
                tabInfo.setDocxFile(docxFile);
                textField.setText(docxFile.getAbsolutePath());
            }
        });

        label.setLabelFor(textField);
        gridPane.add(label, 0, 1);
        gridPane.add(textField, 1, 1);

        getDialogPane().setContent(gridPane);
    }

    private String getSafeFilePath(File file) {
        return file == null ? "" : file.getAbsolutePath();
    }

    public final ObjectProperty<TabInfo> tabInfoProperty() {
        return tabInfo;
    }

    public final void setTabInfo(TabInfo tabInfo) {
        this.tabInfo.set(tabInfo);
    }
}
