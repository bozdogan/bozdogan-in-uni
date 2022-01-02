package org.bozdogan.control;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.bozdogan.UiAssets;

public class MainMenu implements Initializable{

    private UiAssets Ui = UiAssets.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Ui.hostGameScene = new Scene(FXMLLoader.load(getClass().getResource
                            ("/org/bozdogan/fxml/hostGame.fxml")));
            Ui.joinGameScene = new Scene(FXMLLoader.load(getClass().getResource
                            ("/org/bozdogan/fxml/joinGame.fxml")));
            Ui.gameScreenScene = new Scene(FXMLLoader.load(getClass().getResource
                            ("/org/bozdogan/fxml/gameScreen.fxml")));

            Ui.gameScreen1PScene = new Scene(FXMLLoader.load(getClass().getResource
                            ("/org/bozdogan/fxml/gameScreen1P.fxml")));
        } catch(IOException e) {
            if(!(e instanceof FileNotFoundException)) e.printStackTrace();
        }
    }

    @FXML
    void hostGameSequence(){
        Ui.primaryStage.setTitle("Host a Game");
        Ui.primaryStage.setScene(Ui.hostGameScene);
    }

    @FXML
    void joinGameSequence() {
        Ui.primaryStage.setTitle("Join a Game");
        Ui.primaryStage.setScene(Ui.joinGameScene);
    }

    @FXML
    void exitWindow() { Ui.primaryStage.close(); }
}
