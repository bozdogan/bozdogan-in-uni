package org.bozdogan.gui;

import javafx.stage.WindowEvent;
import org.bozdogan.model.Notebook;

import javafx.stage.Modality;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Function;

/*
 * -- TO DO List:
 * - implement;
 *   * closing notebook from edit menu
 *   * about window
 *   * open a notebook from command line arguments
 *   * quit button in gui
 *   -OK- new note creating
 *   -OK- opening a encrypted notebook
 *   -OK- cipher setting and changing
 *
 * - alter/fix;
 *   * select another note after deleting a note
 *       -> select a note at the same index, if n/a then select last note
 *   * suggest file extension at open/save dialogs
 *       -> add file type to dialogs: Simon Notebook (*.snb)
 *   -OK- are-you-sure-dialog popping out all the time
 *   -OK- prevent selected item changing, even if user says no. (roll back)
 *
 * -- Future Features:
 * - create note types; s.a. account, gallery, article.
 * - rich text support on articles
 * */
public class Main extends Application{
    static Notebook currentNotebook;
    static Stage primaryStage;
    static Parameters applicationArguments;

    @Override
    public void start(Stage stage) throws IOException{
        Main.primaryStage = stage;
        Main.applicationArguments = getParameters();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/primary.fxml"));

        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.setTitle("Simon");
        primaryStage.show();
    }

    static PwDialog spawnPasswordDialog(String title, String header, int dialogType,
                                    String hashedPassword, Function<String, String> hashFunction){
        try{
            // LOADER // cannot use getClass() on main 'cause of static context
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/pw_dialog.fxml"));
            Parent root = loader.load();
            PwDialog pwDialogController = loader.getController();

            // -- top level variables -- //
            pwDialogController.headerText = header;
            pwDialogController.dialogType = dialogType;
            pwDialogController.hashedPassword = hashedPassword;
            pwDialogController.hashFunction = hashFunction;
            pwDialogController.handleTopLevelVariables();

            // Stage //
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.setOnCloseRequest(event -> pwDialogController.closeWindow());
            stage.showAndWait();

            return pwDialogController;
        } catch(IOException e){
            e.printStackTrace();
            System.out.println("FXML for Password Dialog is missing.");
        }

        return null;
    }

    public static void main(String[] args){
        try{
            launch(args);
        } catch(Exception err){
            err.printStackTrace();
            //Stop the timer so process could end.
            primaryStage.getOnCloseRequest().handle(null);
        }
    }
}
