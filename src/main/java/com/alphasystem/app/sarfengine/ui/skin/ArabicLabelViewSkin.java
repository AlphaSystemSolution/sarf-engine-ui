package com.alphasystem.app.sarfengine.ui.skin;

import com.alphasystem.app.sarfengine.ui.control.ArabicLabelView;
import com.alphasystem.arabic.model.ArabicSupport;
import com.alphasystem.arabic.model.ArabicWord;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import static javafx.beans.binding.Bindings.when;
import static javafx.scene.layout.Region.USE_PREF_SIZE;
import static javafx.scene.paint.Color.*;

/**
 * @author sali
 */
public class ArabicLabelViewSkin extends SkinBase<ArabicLabelView> {

    private final StackPane stackPane;

    public ArabicLabelViewSkin(final ArabicLabelView view) {
        super(view);

        stackPane = new StackPane();
        stackPane.setFocusTraversable(true);

        final Text label = new Text();
        label.setFont(view.getFont());
        label.setOnMouseClicked(event -> makeSelection(view));
        view.labelProperty().addListener((o, oV, nV) -> {
            label.setText(getLabelText(nV));
        });
        label.setText(getLabelText(view.getLabel()));

        final Rectangle background = new Rectangle(view.getLabelWidth(), view.getLabelHeight());
        background.setFill(TRANSPARENT);
        background.setStrokeWidth(1);
        background.setArcWidth(6);
        background.setArcHeight(6);
        background.setOnMouseClicked(event -> makeSelection(view));
        view.labelWidthProperty().addListener((o, oV, nV) -> {
            if (nV != null) {
                background.setWidth((Double) nV);
            }
        });
        view.labelHeightProperty().addListener((o, oV, nV) -> {
            if (nV != null) {
                background.setHeight((Double) nV);
            }
        });
        background.strokeProperty().bind(when(view.selectedProperty()).then(RED).otherwise(BLACK));
        stackPane.disableProperty().bind(view.disabledProperty());

        stackPane.getChildren().addAll(background, label);
        stackPane.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        stackPane.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        stackPane.setPrefSize(view.getLabelWidth(), view.getLabelHeight());
        getChildren().add(stackPane);
    }

    private static String getLabelText(ArabicSupport value) {
        String text = "";
        if (value != null) {
            ArabicWord arabicWord = value.getLabel();
            text = arabicWord == null ? "" : arabicWord.toUnicode();
        }
        return text;
    }

    private void makeSelection(ArabicLabelView view) {
        stackPane.requestFocus();
        view.makeSelection();
    }
}
