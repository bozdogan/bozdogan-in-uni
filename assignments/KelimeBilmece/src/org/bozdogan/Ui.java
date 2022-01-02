package org.bozdogan;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Ui extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        UiAssets.getInstance().primaryStage = primaryStage;
        UiAssets.getInstance().mainMenuScene = new Scene
                (FXMLLoader.load(getClass().getResource("/org/bozdogan/fxml/mainMenu.fxml")));

        primaryStage.setTitle(UiAssets.getInstance().mainMenuTitle);
        primaryStage.setScene(UiAssets.getInstance().mainMenuScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
