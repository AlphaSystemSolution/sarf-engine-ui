package com.alphasystem.app.sarfengine.ui;

import com.alphasystem.app.sarfengine.conjugation.builder.ConjugationBuilder;
import com.alphasystem.app.sarfengine.conjugation.model.SarfChart;
import com.alphasystem.app.sarfengine.guice.GuiceSupport;
import com.alphasystem.app.sarfengine.ui.control.SarfChartView;
import com.alphasystem.arabic.model.ArabicLetterType;
import com.alphasystem.arabic.model.NamedTemplate;
import com.alphasystem.morphologicalanalysis.morphology.model.support.VerbalNoun;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.Collections;

import static javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED;

/**
 * @author sali
 */
public class SarfChartViewerApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Sarf Chart Viewer");
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        primaryStage.setWidth(bounds.getWidth() / 4);
        primaryStage.setHeight(bounds.getHeight() / 4);

        ConjugationBuilder conjugationBuilder = GuiceSupport.getInstance().getConjugationBuilderFactory().getConjugationBuilder();

        SarfChart sarfChart = conjugationBuilder.doConjugation(NamedTemplate.FORM_IX_TEMPLATE, "", false, false,
                ArabicLetterType.NOON, ArabicLetterType.QAF, ArabicLetterType.DDAD,
                Collections.singletonList(VerbalNoun.VERBAL_NOUN_FORM_IX), null);
        SarfChartView sarfChartView = new SarfChartView();
        sarfChartView.setSarfChart(sarfChart);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(sarfChartView);
        scrollPane.setVbarPolicy(AS_NEEDED);
        scrollPane.setHbarPolicy(AS_NEEDED);
        Scene scene = new Scene(scrollPane);
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

}
