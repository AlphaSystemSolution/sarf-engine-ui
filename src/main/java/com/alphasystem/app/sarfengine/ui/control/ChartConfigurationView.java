package com.alphasystem.app.sarfengine.ui.control;

import com.alphasystem.app.sarfengine.ui.control.skin.ChartConfigurationSkin;
import com.alphasystem.morphologicalanalysis.morphology.model.ChartConfiguration;
import com.alphasystem.morphologicalanalysis.morphology.model.support.SortDirection;
import com.alphasystem.morphologicalanalysis.morphology.model.support.SortDirective;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/**
 * @author sali
 */
public class ChartConfigurationView extends Control {

    private final ObjectProperty<ChartConfiguration> chartConfiguration = new SimpleObjectProperty<>();
    private final BooleanProperty omitToc = new SimpleBooleanProperty();
    private final BooleanProperty omitAbbreviatedConjugation = new SimpleBooleanProperty();
    private final BooleanProperty omitDetailedConjugation = new SimpleBooleanProperty();
    private final BooleanProperty omitTitle = new SimpleBooleanProperty();
    private final BooleanProperty omitHeader = new SimpleBooleanProperty();
    private final BooleanProperty omitSarfTermCaption = new SimpleBooleanProperty();
    private final ObjectProperty<SortDirective> sortDirective = new SimpleObjectProperty<>();
    private final ObjectProperty<SortDirection> sortDirection = new SimpleObjectProperty<>();

    public ChartConfigurationView() {
        chartConfigurationProperty().addListener((o, ov, nv) -> {
            setOmitAbbreviatedConjugation(nv.isOmitAbbreviatedConjugation());
            setOmitDetailedConjugation(nv.isOmitDetailedConjugation());
            setOmitHeader(nv.isOmitHeader());
            setOmitSarfTermCaption(nv.isOmitSarfTermCaption());
            setOmitTitle(nv.isOmitTitle());
            setOmitToc(nv.isOmitToc());
            setSortDirection(nv.getSortDirection());
            setSortDirective(nv.getSortDirective());
        });

        omitAbbreviatedConjugationProperty().addListener((o, ov, nv) -> {
            getChartConfiguration().setOmitAbbreviatedConjugation(nv);
        });
        omitDetailedConjugationProperty().addListener((o, ov, nv) -> {
            getChartConfiguration().setOmitDetailedConjugation(nv);
        });
        omitHeaderProperty().addListener((o, ov, nv) -> {
            getChartConfiguration().setOmitHeader(nv);
        });
        omitSarfTermCaptionProperty().addListener((o, ov, nv) -> {
            getChartConfiguration().setOmitSarfTermCaption(nv);
        });
        omitTitleProperty().addListener((o, ov, nv) -> {
            getChartConfiguration().setOmitTitle(nv);
        });
        omitTocProperty().addListener((o, ov, nv) -> {
            getChartConfiguration().setOmitToc(nv);
        });
        sortDirectionProperty().addListener((o, ov, nv) -> {
            getChartConfiguration().setSortDirection(nv);
        });
        sortDirectiveProperty().addListener((o, ov, nv) -> {
            getChartConfiguration().setSortDirective(nv);
        });

        setChartConfiguration(null);
        setMinWidth(400);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ChartConfigurationSkin(this);
    }

    public final ChartConfiguration getChartConfiguration() {
        return chartConfiguration.get();
    }

    public final void setChartConfiguration(ChartConfiguration chartConfiguration) {
        this.chartConfiguration.set(chartConfiguration == null ? new ChartConfiguration() : chartConfiguration);
    }

    public final ObjectProperty<ChartConfiguration> chartConfigurationProperty() {
        return chartConfiguration;
    }

    public final BooleanProperty omitTocProperty() {
        return omitToc;
    }

    public final void setOmitToc(boolean omitToc) {
        this.omitToc.set(omitToc);
    }

    public final BooleanProperty omitAbbreviatedConjugationProperty() {
        return omitAbbreviatedConjugation;
    }

    public final void setOmitAbbreviatedConjugation(boolean omitAbbreviatedConjugation) {
        this.omitAbbreviatedConjugation.set(omitAbbreviatedConjugation);
    }

    public final BooleanProperty omitDetailedConjugationProperty() {
        return omitDetailedConjugation;
    }

    public final void setOmitDetailedConjugation(boolean omitDetailedConjugation) {
        this.omitDetailedConjugation.set(omitDetailedConjugation);
    }

    public final BooleanProperty omitTitleProperty() {
        return omitTitle;
    }

    public final void setOmitTitle(boolean omitTitle) {
        this.omitTitle.set(omitTitle);
    }

    public final BooleanProperty omitHeaderProperty() {
        return omitHeader;
    }

    public final void setOmitHeader(boolean omitHeader) {
        this.omitHeader.set(omitHeader);
    }

    public final BooleanProperty omitSarfTermCaptionProperty() {
        return omitSarfTermCaption;
    }

    public final void setOmitSarfTermCaption(boolean omitSarfTermCaption) {
        this.omitSarfTermCaption.set(omitSarfTermCaption);
    }

    public final ObjectProperty<SortDirective> sortDirectiveProperty() {
        return sortDirective;
    }

    public final void setSortDirective(SortDirective sortDirective) {
        this.sortDirective.set(sortDirective);
    }

    public final ObjectProperty<SortDirection> sortDirectionProperty() {
        return sortDirection;
    }

    public final void setSortDirection(SortDirection sortDirection) {
        this.sortDirection.set(sortDirection);
    }
}
