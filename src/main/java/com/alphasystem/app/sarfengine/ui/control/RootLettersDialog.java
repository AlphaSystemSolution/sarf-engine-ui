package com.alphasystem.app.sarfengine.ui.control;

import com.alphasystem.sarfengine.xml.model.RootLetters;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Dialog;

import static javafx.scene.control.ButtonType.CANCEL;
import static javafx.scene.control.ButtonType.OK;

/**
 * @author sali
 */
public class RootLettersDialog extends Dialog<RootLetters> {

    private final ObjectProperty<RootLetters> rootLetters;
    private final RootWordsSelectionPane pane = new RootWordsSelectionPane();
    private boolean initialized = false;

    public RootLettersDialog() {
        setTitle("Select Root Letters");

        rootLetters = new SimpleObjectProperty<>();
        rootLettersProperty().addListener((o, oV, nV) -> {
            initDialog(nV);
        });

        getDialogPane().getButtonTypes().addAll(OK, CANCEL);
        setResultConverter(param -> {
            RootLetters result = null;
            if (!param.getButtonData().isCancelButton()) {
                result = pane.getRootLetters();
            }
            return result;
        });
    }

    private void initDialog(RootLetters rootLetters) {
        if (rootLetters == null) {
            return;
        }

        pane.reset(rootLetters);

        if (!initialized) {
            getDialogPane().setContent(pane);
            initialized = true;
        }
    }

    public final void setRootLetters(RootLetters rootLetters) {
        this.rootLetters.set(rootLetters);
    }

    public final ObjectProperty<RootLetters> rootLettersProperty() {
        return rootLetters;
    }
}
