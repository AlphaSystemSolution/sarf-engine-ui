package com.alphasystem.app.sarfengine.ui.control;

import com.alphasystem.morphologicalanalysis.morphology.model.ChartConfiguration;
import javafx.scene.control.Dialog;

import static javafx.scene.control.ButtonType.CANCEL;
import static javafx.scene.control.ButtonType.OK;
import static javafx.stage.Modality.WINDOW_MODAL;

/**
 * @author sali
 */
public class ChartConfigurationDialog extends Dialog<ChartConfiguration> {

    private final ChartConfigurationView view = new ChartConfigurationView();

    public ChartConfigurationDialog() {
        setTitle("Select Chart Configuration");
        initModality(WINDOW_MODAL);

        getDialogPane().setContent(view);
        getDialogPane().getButtonTypes().addAll(OK, CANCEL);
        setResultConverter(param -> param.getButtonData().isDefaultButton() ? view.getChartConfiguration() : null);
    }

    public void setChartConfiguration(ChartConfiguration chartConfiguration) {
        view.setChartConfiguration(chartConfiguration);
    }
}
