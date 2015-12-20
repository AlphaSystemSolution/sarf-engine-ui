package com.alphasystem.app.sarfengine.ui.control.model;

import com.alphasystem.morphologicalanalysis.morphology.model.ChartConfiguration;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.File;

/**
 * @author sali
 */
public final class TabInfo {

    private final ObjectProperty<File> docxFile = new SimpleObjectProperty<>();
    private final ObjectProperty<File> sarfxFile = new SimpleObjectProperty<>();
    private final ObjectProperty<ChartConfiguration> chartConfiguration = new SimpleObjectProperty<>();
    private final BooleanProperty dirty = new SimpleBooleanProperty();

    public TabInfo() {
        setDirty(true);
        setChartConfiguration(null);
    }

    public final File getDocxFile() {
        return docxFile.get();
    }

    public final void setDocxFile(File docxFile) {
        this.docxFile.set(docxFile);
    }

    public final ObjectProperty<File> docxFileProperty() {
        return docxFile;
    }

    public final File getSarfxFile() {
        return sarfxFile.get();
    }

    public final void setSarfxFile(File sarfxFile) {
        this.sarfxFile.set(sarfxFile);
    }

    public final ObjectProperty<File> sarfxFileProperty() {
        return sarfxFile;
    }

    public ChartConfiguration getChartConfiguration() {
        return chartConfiguration.get();
    }

    public void setChartConfiguration(ChartConfiguration chartConfiguration) {
        this.chartConfiguration.set(chartConfiguration == null ? new ChartConfiguration() : chartConfiguration);
    }

    public ObjectProperty<ChartConfiguration> chartConfigurationProperty() {
        return chartConfiguration;
    }

    public final boolean getDirty() {
        return dirty.get();
    }

    public final void setDirty(boolean dirty) {
        this.dirty.set(dirty);
    }

    public final BooleanProperty dirtyProperty() {
        return dirty;
    }
}
