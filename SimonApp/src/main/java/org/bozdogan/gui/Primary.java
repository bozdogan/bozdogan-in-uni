package org.bozdogan.gui;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import org.bozdogan.model.Notebook;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.bozdogan.util.Cryptor;
import org.bozdogan.util.Hashing;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;

public class Primary implements Initializable{

    @FXML private MenuItem saveCmd;
    @FXML private Menu menu_edit;
    @FXML private Menu menu_notebook;

    @FXML private ListView<ListEntry> noteLv;

    @FXML private TextField titleTx;
    @FXML private TextArea articleTx;

    @FXML private Label statusLb;

    @FXML private Button newNoteBt;
    @FXML private Button saveBt;
    @FXML private Button deleteBt;

    private final Timer statusTimer = new Timer();
    private final ListChangeListener<ListEntry> onSelectionChange = c -> fetchSelectedNote();
    private final InvalidationListener onEntryChange = c -> {
        entryChanged = true;
        if(newNoteBt.isDisable())
            newNoteBt.setDisable(false);
    };

    private ListEntry currentEntry = null;
    private boolean entryChanged;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        statusLb.setText(""); // it's "STATUS TEXT" in fxml.

        // Make sure timer thread will end and application will shut down when window closed.
        Main.primaryStage.setOnCloseRequest(e -> closeWindow());

        noteLv.getSelectionModel().getSelectedItems().addListener(onSelectionChange);

        titleTx.textProperty().addListener(onEntryChange);
        articleTx.textProperty().addListener(onEntryChange);

        // Save note key combo: Ctrl-S
        saveCmd.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));

        /* * * DEBUG * * */
        for(String e : Main.applicationArguments.getRaw())
            System.out.println(e);
        // * END DEBUG * //

        /* * * DEBUG * * */if(Main.applicationArguments.getUnnamed().size()>0){
            System.out.println("ARG_COUNT: "+Main.applicationArguments.getUnnamed().size());
            System.out.println("ARG #0   : "+Main.applicationArguments.getUnnamed().get(0));
            System.out.println("IS_FILE  : "+
                    (Files.isRegularFile(Paths.get(
                            Main.applicationArguments.getUnnamed().get(0)
                    )) ? "TRUE" : "FALSE"));
            System.out.println();
            System.out.println(".size()>0 && isFileRegular = "+(
                    Main.applicationArguments.getUnnamed().size()>0 & Files.isRegularFile(
                            Paths.get(Main.applicationArguments.getUnnamed().get(0))
                    ) ? "TRUE" : "FALSE"));
        }// * END DEBUG * //

        // -- Opening notebook from CL argument -- //
        if(Main.applicationArguments.getUnnamed().size()>0){
            Path initialFile = Paths.get(Main.applicationArguments.getUnnamed().get(0));
            if(Files.isRegularFile(initialFile)){
                _openNotebook(initialFile.toFile());
                prepareUI();
            } else{
                resetUI();
                // entryChanged flag is set whenever content of any of the text fields
                // change. resetting ui causes that, too. but since it's when the app
                // just starts, I don't that to happen.
                entryChanged = false;
            }
        } else{
            resetUI();
            // ~
            entryChanged = false;
        }
    }

    @FXML
    private void newNotebook(){
        // 1. ask user the location to save
        File file  = new FileChooser().showSaveDialog(new Stage());
        if(file==null){
            System.out.println("[File Open Dialog] Dialog closed without choosing a file.");
            return;
        }

        // 2. create notebook at the location
        // 3. open it
        try{
            Main.currentNotebook = new Notebook(file.getAbsolutePath());
            setStatus("Notebook opened.");
        } catch(SQLException e){
            e.printStackTrace();
            System.out.println("Cannot create notebook.");
            setStatus("#! Cannot create notebook.");
            return;
        }

        // 4. trigger ui initialization
        prepareUI();
    }

    @FXML
    private void openNotebookDialog(){
        // 0. close current notebook
        if(Main.currentNotebook!=null)
            closeNotebook();

        // 1. ask user the location for a notebook
        File file  = new FileChooser().showOpenDialog(new Stage());
        if(file==null){
            System.out.println("[File Open Dialog] Dialog closed without choosing a file.");
            return;
        }

        // 2. open it.
        _openNotebook(file);

        // 4. trigger ui initialization
        prepareUI();
    }

    private void _openNotebook(File file){
        // 2. open it.
        try{
            Main.currentNotebook = new Notebook(file.getAbsolutePath());
            setStatus("Notebook opened.");
        } catch(SQLException e){
            e.printStackTrace();
            System.out.println("Cannot open notebook.");
            setStatus("#! Cannot open notebook.");
            return;
        }

        // 3. check if notebook is encrypted
        if(Main.currentNotebook.isEncrypted()){
            PwDialog controller = Main.spawnPasswordDialog("Encrypted Notebook",
                    "Please type the password for the notebook",
                    PwDialog.ASK, Main.currentNotebook.getPassword(), Hashing::md5hex);

            if(controller!=null && controller.password!=null &&
                    Main.currentNotebook.setCryptor(new Cryptor(controller.password))){
                setStatus("Encrypted notebook opened.");
            } else{
                setStatus("Cannot open encrypted notebook.");
            }
        }
    }

    @FXML
    private void closeNotebook(){
        if(entryChanged && ! ruSureDialog(
                "Note not saved",
                "Do you want to close notebook anyway?"))
            return;

        resetUI();
        setStatus("Notebook closed.");
    }

    private void prepareUI(){
        if(Main.currentNotebook!=null){
            // fill the list
            noteLv.setDisable(false);
            refreshNoteList();

            // enable text fields
            titleTx.setDisable(false);
            articleTx.setDisable(false);

            // enable buttons
            newNoteBt.setDisable(false);
            saveBt.setDisable(false);
            deleteBt.setDisable(false);

            // enable edit menu
            menu_edit.setDisable(false);
            menu_notebook.setDisable(false);
        }
    }

    private void resetUI(){
        try{
            if(Main.currentNotebook!=null)
                Main.currentNotebook.close();

            // clear the list
            noteLv.setDisable(true);
            noteLv.getItems().clear();
            noteLv.getSelectionModel().clearSelection(); // might not need this

            // disable text fields
            titleTx.setDisable(true);
            titleTx.clear();
            articleTx.setDisable(true);
            articleTx.clear();

            // disable buttons
            newNoteBt.setDisable(true);
            saveBt.setDisable(true);
            deleteBt.setDisable(true);

            // disable edit menu
            menu_edit.setDisable(true);
            menu_notebook.setDisable(true);
        } catch(SQLException e){ System.out.println("/* useless sorta errors occurred */"); }

        Main.currentNotebook = null;
    }

    private void fetchSelectedNote(){
        if(entryChanged){
            if(ruSureDialog(
                    "Note not saved",
                    "Do you want to proceed without saving?")){
                // change in previous entry is discarded and current
                // entry is not changed yet.
                entryChanged = false;
            } else{
                // directly selecting through list's selection model causes this
                // method to re-trigger, so...
                noteLv.getSelectionModel().getSelectedItems().removeListener(onSelectionChange);
                noteLv.getSelectionModel().select(currentEntry);
                noteLv.getSelectionModel().getSelectedItems().addListener(onSelectionChange);

                return;
            }
        }

        ListEntry selected = noteLv.getSelectionModel().getSelectedItem();
        if(selected!=null){
            Notebook.Note note = Main.currentNotebook.getNote(selected.id);
            currentEntry = selected;

            // unbind end rebind change listeners. or else it will trigger
            // every time I set these guys' text.
            titleTx.textProperty().removeListener(onEntryChange);
            articleTx.textProperty().removeListener(onEntryChange);

            titleTx.setText(note.Title());
            articleTx.setText(note.Content());

            titleTx.textProperty().addListener(onEntryChange);
            articleTx.textProperty().addListener(onEntryChange);
        }
    }

    private void refreshNoteList(){
        noteLv.getItems().clear();

        for(int Gauss=0; Gauss<100000000; Gauss++);

        for(Map.Entry<Integer, String> e: Main.currentNotebook.getTitles().entrySet())
            noteLv.getItems().add(new ListEntry(e.getKey(), e.getValue()));
    }

    @FXML
    private void createNote(){
        // -- clear fields -- //
        titleTx.textProperty().removeListener(onEntryChange);
        titleTx.clear();
        titleTx.textProperty().addListener(onEntryChange);
        articleTx.textProperty().removeListener(onEntryChange);
        articleTx.clear();
        articleTx.textProperty().addListener(onEntryChange);
        noteLv.getSelectionModel().clearSelection();

        currentEntry = null;
        entryChanged = false;
        newNoteBt.setDisable(true);
    }

    @FXML
    private void saveNote(){
        if(currentEntry==null){
            // New note.
            Main.currentNotebook.putNote(titleTx.getText(), articleTx.getText());
            setStatus("Note created.");
        } else if(entryChanged){
            Main.currentNotebook.updateNote(currentEntry.id, titleTx.getText(), articleTx.getText());
            setStatus("Note saved.");
        }

        entryChanged = false;
        refreshNoteList();
    }

    @FXML
    private void deleteNote(){
        if(currentEntry==null){
            System.out.println("[deleteNote] currentEntry is N/A");
            setStatus("#! Minor issue..", 0.4);
            return;
        }

        if(! ruSureDialog(
                "Delete a Note",
                String.format("Are you sure you want to delete this note?\n\nNote id: #%d\nTitle: %s",
                        currentEntry.id, noteLv.getSelectionModel().getSelectedItem().title)))
            return;

        Main.currentNotebook.deleteNote(currentEntry.id);

        // -- clear fields -- //
        titleTx.textProperty().removeListener(onEntryChange);
        titleTx.clear();
        titleTx.textProperty().addListener(onEntryChange);
        articleTx.textProperty().removeListener(onEntryChange);
        articleTx.clear();
        articleTx.textProperty().addListener(onEntryChange);
        noteLv.getSelectionModel().clearSelection();

        currentEntry = null;
        entryChanged = false;
        refreshNoteList();
    }

    @FXML
    private void passwordChangeDialog(){
        Notebook notebook = Main.currentNotebook;

        String title = "Encryption Management for "+notebook.getDatabaseLocation();
        String header;
        int dialogType;
        String currentPassword = notebook.getPassword();
        Function<String, String> hashFunction = Hashing::md5hex;
        PwDialog controller;

        if(notebook.isEncrypted()){
            header = "To change password, please type existing password and new password.";
            dialogType = PwDialog.CHANGE;
        } else{
            header = "To set encryption, please type a password.";
            dialogType = PwDialog.SET;
        }

        controller = Main.spawnPasswordDialog(title, header, dialogType, currentPassword, hashFunction);

        /* * * D E B U G * * */if(controller==null){
            System.out.println("\nCONFIRMED        : "+(controller.confirmed ? "TRUE" : "FALSE"));
            System.out.println(  "New password     : "+controller.password);
        }// * * END DEBUG * * //

        if(controller==null || ! controller.confirmed){
            System.out.println("Password dialog cancelled.");
            setStatus("Password operation cancelled.");
            return;
        }

        Cryptor newCryptor = new Cryptor(controller.password);

        if(dialogType == PwDialog.CHANGE)
            if(notebook.changeCryptor(newCryptor))
                setStatus("Password changed successfully.");
            else{
                System.out.println("[passwordChangeDialog] PW change error.");
                setStatus("Cannot change password.");
            }

        else/*if(dialogType == PwDialog.SET)*/
            if(notebook.setCryptor(newCryptor))
                setStatus("Password set successfully.");
            else{
                System.out.println("[passwordChangeDialog] PW set error.");
                setStatus("Cannot set password.");
            }

        prepareUI(); // to reset list view
    }

    @FXML
    private void aboutWindow(){ //@TODO Make a proper about window.
        ruSureDialog("NOT YET IMPLEMENTED", "I didn't create an" +
                " \"about window\" yet. I also didn't implemented a basic" +
                " alertbox, so using this confirmation dialog instead.\n\n" +
                "Do you acknowledge that? (Not that it counts..)");
    }

    /**
     * @return ´true´ if user clicks 'Yes' */
    private boolean ruSureDialog(String title, String content){
        Alert box = new Alert(Alert.AlertType.WARNING);
        box.setTitle(title);
        box.setHeaderText(null);
        box.setContentText(content);

        ButtonType yesButtonType = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType noButtonType = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        box.getButtonTypes().setAll(yesButtonType, noButtonType);

        Optional<ButtonType> result = box.showAndWait();
        return result.isPresent() && result.get() == yesButtonType;
    }

    private void setStatus(String statusText){
        setStatus(statusText, 3.0);
    }

    /**
     * @param statusText temporary status text
     * @param duration duration in seconds (past 3 decimal digits are ignored) */
    private void setStatus(String statusText, double duration){ //@TODO make it cancel previous task if new status is set while displaying another.
        // there is a small possibility of tasks to overlap, causing first
        // task's temp status to be set permanently by the second task
        // the way to fix it is not setting delay for latter task until
        // the first tasks executes, a.k.a. queueing them. this should not be
        // a problem for now but it if it becomes a problem, solution is extend
        // Timer class to make queueing tasks possible.

        if(duration>0){
            String previous = statusLb.getText();
            statusLb.setText(statusText);
            //@ this is how to set timer with javafx ui integration.
            statusTimer.schedule(new TimerTask(){
                @Override
                public void run(){
                    Platform.runLater(() -> statusLb.setText(previous));
                }
            }, (int) duration*1000);

        } else statusLb.setText(statusText);
    }

    @FXML
    private void closeWindow(){
        if(Main.currentNotebook!=null)
            try{
                Main.currentNotebook.close();
            } catch(SQLException e){ System.out.println("/* useless sorta errors occurred */"); }

        statusTimer.cancel();
        Main.primaryStage.close();
    }

    /** Entry class. It's sole purpose is getting id of a note from the list. */
    private static class ListEntry{
        int id;
        String title;

        ListEntry(int id, String title){
            this.id = id;
            this.title = title;
        }

        @Override
        public String toString(){
            return title;
        }
    }
}
