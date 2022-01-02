package org.bozdogan.test;

import org.bozdogan.util.Cryptor;
import org.bozdogan.model.Notebook;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

public class UpdateExistingNotes{
    private static final String testFile_location = "tests/update.db";
    private static final String password1 = "password1";

    @BeforeClass
    public static void create_test_directory(){
        try{ Files.createDirectories(Paths.get(testFile_location).getParent()); }
        catch(IOException e){ e.printStackTrace(); }
    }

    @Before
    public void create_dummy_notebook(){
        Path test_file = Paths.get(testFile_location);

        try{ Files.deleteIfExists(test_file); }
        catch(IOException e){ e.printStackTrace(); return; }


        try(Notebook notebook = new Notebook(test_file.toString())){
            notebook.setCryptor(new Cryptor(password1));

            notebook.putNote("Title1", "Content of Entry 1");
            notebook.putNote("Title2", "Content of Entry 2");
            notebook.putNote("Title3", "Content of Entry 3");
            notebook.putNote("Title4", "Content of Entry 4");
            notebook.putNote("Title5", "Content of Entry 5");

        } catch(SQLException e){e.printStackTrace();}
    }

    @Test
    public void update_an_existing_note_and_verify(){
        Path test_file = Paths.get(testFile_location);

        try(Notebook db = new Notebook((test_file.toString()))){
            db.updateNote(5, "Change", "The only constant of software developing.");

        } catch(SQLException e){e.printStackTrace();}

        boolean update_success = true;
        try(Notebook db = new Notebook((test_file.toString()))){
            System.out.println("\n@ INFO: Test loading db w/ encryption\n");

            Notebook.Note note = db.getNote(5);
            update_success = note.Title().equals("Change");
            update_success &= note.Content().equals("The only constant of software developing.");


        } catch(SQLException e){e.printStackTrace();}

        Assert.assertTrue(update_success);
    }
}
