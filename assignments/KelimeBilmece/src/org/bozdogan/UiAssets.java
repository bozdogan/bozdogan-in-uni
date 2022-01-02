package org.bozdogan;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class UiAssets {
    private static UiAssets ourInstance = new UiAssets();

    public Stage primaryStage;
    public String mainMenuTitle = "An Interesting Title";

    public Scene mainMenuScene;
    public Scene hostGameScene;
    public Scene joinGameScene;
    public Scene gameScreenScene;

    public Scene gameScreen1PScene;

    public static UiAssets getInstance() {
        return ourInstance;
    }
    private UiAssets() { }
}
