package org.bozdogan.test;

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
import java.util.Map;

public class FindAWordInNotes{
    private static final String testFile_location = "tests/encryption.db";
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

            notebook.putNote("Title1", "Content of Entry 1");
            notebook.putNote("Apple is a fruit", "Content of Entry 2");
            notebook.putNote("Title3", "Content of Entry 3 including apples.");
            notebook.putNote("Title4", "Content of Entry 4");
            notebook.putNote("Title5", "I made myself some apple pies.");

        } catch(SQLException e){e.printStackTrace();}
    }

    @Test
    public void serch_a_word_in_notebook(){
        Path test_file = Paths.get(testFile_location);


        boolean search_succeed = true;
        try(Notebook db = new Notebook((test_file.toString()))){
            System.out.println("\n@ INFO: Test loading db w/ encryption\n");

            Map<Integer, Notebook.Note> results = db.search("apple");
            search_succeed = results.containsKey(3) && results.containsKey(5);

        } catch(SQLException e){e.printStackTrace();}

        Assert.assertTrue(search_succeed);
    }
}
