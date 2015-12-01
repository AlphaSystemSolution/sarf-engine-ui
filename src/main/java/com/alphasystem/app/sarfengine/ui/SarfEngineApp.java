package com.alphasystem.app.sarfengine.ui;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * @author sali
 */
public class SarfEngineApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

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

        SarfEnginePane pane = new SarfEnginePane();
        Scene scene = new Scene(pane);
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
