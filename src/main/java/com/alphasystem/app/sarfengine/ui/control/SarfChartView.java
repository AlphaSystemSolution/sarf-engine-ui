package com.alphasystem.app.sarfengine.ui.control;

import com.alphasystem.app.sarfengine.conjugation.model.SarfChart;
import com.alphasystem.app.sarfengine.ui.control.skin.SarfChartSkin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/**
 * @author sali
 */
public class SarfChartView extends Control {

    private final ObjectProperty<SarfChart> sarfChart = new SimpleObjectProperty<>(null, "sarfChart");

    public SarfChartView() {
    }

    public final SarfChart getSarfChart() {
        return sarfChart.get();
    }

    public final void setSarfChart(SarfChart sarfChart) {
        this.sarfChart.set(sarfChart);
    }

    public final ObjectProperty<SarfChart> sarfChartProperty() {
        return sarfChart;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new SarfChartSkin(this);
    }
}
