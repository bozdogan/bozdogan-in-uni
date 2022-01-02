package org.bozdogan.control;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import org.bozdogan.GameLogic;

import java.net.URL;
import java.util.ResourceBundle;

public class GameScreen1P implements Initializable {

    private GameLogic.Host game = new GameLogic.Host();

    @FXML private TextField wordTx;
    @FXML private Button sendBt;
    @FXML private Label wordsLb;
    @FXML private Label statusLb;

    private InvalidationListener wordTxChangeListener = (e) -> wordTx_TextChanged();
    private boolean isFirstWord = true;
    private Timeline timer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        wordTx.textProperty().addListener(wordTxChangeListener);
        timer = new Timeline(new KeyFrame(
                Duration.seconds(10),
                e -> {
                    statusLb.setText("TIME'S UP! Score: " + game.getWordList().size());
                    sendBt.setDisable(true);
                }
        ));
    }

    @FXML
    private void send() {
        int errorstate = game.appendWord(wordTx.getText());
        System.out.println("[SEND] appendWord("+wordTx.getText()+") : "+ errorstate);

        wordsLb.setText( String.join(", ", (game.getWordList())) );

        if(errorstate == 0) {
            timer.stop();
            isFirstWord = false;
            wordTx.clear();
            timer.play();
        } else if(errorstate == 1) {
            statusLb.setText("Word already exist in the list ("+wordsLb+")");
        } else if(errorstate == 2) {
            // This should never occur normally as the UI already check this
            // condition. However, a hacked guest client can still cause the error
            // so user should be informed if it happens.
            statusLb.setText( String.format
                    ("Word doesn't begin with correct letters (%s, \"%s\" expected)",
                            wordsLb.getText(), GameLogic.coda(game.last())) );
        }
    }

    @FXML
    private void wordTx_TextChanged() {
        wordTx.textProperty().removeListener(wordTxChangeListener);

        if(! isFirstWord) {
            // Lock-in the first 2 letters

            if(wordTx.getText().length() < 2) {
                wordTx.setText(GameLogic.coda(game.last()));
            }

            else if( !GameLogic.onset(wordTx.getText()).equals(GameLogic.coda(game.last())) )
                wordTx.setText( (GameLogic.coda(game.last()) + GameLogic.coda(wordTx.getText())) );
        }
        wordTx.setText(wordTx.getText().toUpperCase());
        wordTx.positionCaret(wordTx.getText().length());

        wordTx.textProperty().addListener(wordTxChangeListener);
    }

}
