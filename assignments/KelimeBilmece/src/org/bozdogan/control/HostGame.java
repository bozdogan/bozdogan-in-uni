package org.bozdogan.control;

import javafx.fxml.FXML;

import org.bozdogan.UiAssets;

public class HostGame {
    private UiAssets Ui = UiAssets.getInstance();

    @FXML
    private void goBack() {
        Ui.primaryStage.setTitle(Ui.mainMenuTitle);
        Ui.primaryStage.setScene(Ui.mainMenuScene);
    }

    @FXML
    private void start1P() {
        Ui.primaryStage.setTitle("Word Chain: Single Player Mode");
        Ui.primaryStage.setScene(Ui.gameScreen1PScene);
    }
}
