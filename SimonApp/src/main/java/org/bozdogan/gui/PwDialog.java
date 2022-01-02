package org.bozdogan.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

public class PwDialog implements Initializable{
    /*   ASK PASSWORD : 01
    *    SET PASSWORD : 10
    * CHANGE PASSWORD : 11
    *
    * CHANGE = ASK & SET */
    /** Just ask password and return*/
    public static final int ASK = 1;
    public static final int SET = 2;
    public static final int CHANGE = 3;

    // -- top level inputs -- //
    String headerText;
    int dialogType;
    String hashedPassword;
    Function<String, String> hashFunction;

    // -- FXML references -- //
    @FXML private Label headerLb;
    @FXML private Label statusLb;
    @FXML private PasswordField currPwTx;
    @FXML private PasswordField newPwTx;
    @FXML private PasswordField newPwAgainTx;

    //
    private final Timer statusTimer = new Timer();

    // -- Responses -- //
    /** Dialog is closed by pressing OK button. */
    boolean confirmed;
    /** Current password if type is ASK,<br/>
     * New password if type is SET or CHANGE. */
    String password;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        statusLb.setText("");
        currPwTx.textProperty().addListener(e -> clearStatus());
        newPwTx.textProperty().addListener(e -> clearStatus());
        newPwAgainTx.textProperty().addListener(e -> clearStatus());
    }

    /** Apply top level inputs. */
    public void handleTopLevelVariables(){
        headerLb.setText(headerText);

        if((dialogType&ASK)!=ASK) /*don't show current password field*/
            currPwTx.getParent().setVisible(false);

        if((dialogType&SET)!=SET){ /*don't show new password fields*/
            newPwTx.getParent().setVisible(false);
            newPwAgainTx.getParent().setVisible(false);
        }
    }

    @FXML
    private void confirmAction(){
        confirmed = true;// if any of the tests fails, this'll end up ´false´.
        String status = null;

        if((dialogType&ASK)==ASK){
            // password matches with the notebook
            if(hashFunction.apply(currPwTx.getText()).equals(hashedPassword)){
                password = currPwTx.getText();
                confirmed &= true;
            } else{
                status = "Wrong password.";
                confirmed = false;
            }
        }

        if((dialogType&SET)==SET){
            // new password inputs are the same
            if(newPwTx.getText().equals(newPwAgainTx.getText())){
                password = newPwTx.getText();
                confirmed &= true;
            } else{
                status = status!=null ? status :"Passwords don't match.";
                confirmed = false;
            }
        }

        if(confirmed)
            closeWindow();
        else
            setStatus(status, 0);
    }

    @FXML
    void closeWindow(){
        statusTimer.cancel();
        ((javafx.stage.Stage) statusLb.getScene().getWindow()).close();
    }

    private void clearStatus(){ setStatus("", 0); }

    /**
     * @param duration in seconds, '0' permanently sets */
    private void setStatus(String statusText, double duration){
        if(duration>0){
            String previous = statusLb.getText();
            statusLb.setText(statusText);
            statusTimer.schedule(new TimerTask(){ public void run(){
                    Platform.runLater(() -> statusLb.setText(previous));
            }}, (int) duration*1000);
        } else statusLb.setText(statusText);
    }
}
