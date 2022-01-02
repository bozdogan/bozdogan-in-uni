package org.bozdogan.control;

import javafx.fxml.FXML;

import org.bozdogan.UiAssets;

public class JoinGame {
    private UiAssets Ui = UiAssets.getInstance();

    @FXML
    private void goBack() {
        Ui.primaryStage.setTitle(Ui.mainMenuTitle);
        Ui.primaryStage.setScene(Ui.mainMenuScene);
    }
}
