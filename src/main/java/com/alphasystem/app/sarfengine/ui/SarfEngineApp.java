package com.alphasystem.app.sarfengine.ui;

import com.alphasystem.sarfengine.xml.model.ConjugationData;
import com.alphasystem.sarfengine.xml.model.ConjugationTemplate;
import com.alphasystem.sarfengine.xml.model.RootLetters;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import static com.alphasystem.arabic.model.ArabicLetterType.*;
import static com.alphasystem.arabic.model.NamedTemplate.FORM_IV_TEMPLATE;
import static com.alphasystem.sarfengine.xml.model.VerbalNoun.*;
import static java.util.Arrays.asList;

/**
 * @author sali
 */
public class SarfEngineApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Sarf Engine UI");
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        primaryStage.setWidth(bounds.getWidth() / 4);
        primaryStage.setHeight(bounds.getHeight() / 4);

        ConjugationTemplate template = new ConjugationTemplate();

        ConjugationData data = new ConjugationData();
        RootLetters rootLetters = new RootLetters().withFirstRadical(FA).withSecondRadical(AIN).withThirdRadical(LAM);
        data.withRootLetters(rootLetters);
        data.getContainer().getVerbalNouns().addAll(asList(VERBAL_NOUN_V1, VERBAL_NOUN_V13));
        template.getData().add(data);

        data = new ConjugationData();
        rootLetters = new RootLetters().withFirstRadical(SEEN).withSecondRadical(LAM).withThirdRadical(MEEM);
        data.withRootLetters(rootLetters);
        data.setTemplate(FORM_IV_TEMPLATE);
        data.getContainer().getVerbalNouns().addAll(asList(VERBAL_NOUN_FORM_IV));
        template.getData().add(data);

        SarfEnginePane pane = new SarfEnginePane(template);
        Scene scene = new Scene(pane);
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
