package org.bozdogan.test;

import org.bozdogan.model.Notebook;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

public class CreateNewDb{
    private static final String testFile_location = "tests/empty.db";

    @BeforeClass
    public static void create_test_directory(){
        try{ Files.createDirectories(Paths.get(testFile_location).getParent()); }
        catch(IOException e){ e.printStackTrace(); }
    }

    @Test
    public void create_new_database(){

        Path test_file = Paths.get(testFile_location);

        try{ Files.deleteIfExists(test_file); }
        catch(IOException e){ e.printStackTrace(); return; }


        boolean empty_database_exists = false;

        try(Notebook notebook = new Notebook(test_file.toString())){

            empty_database_exists = Files.exists(test_file);
            empty_database_exists &= notebook.getTitles().isEmpty();

        } catch(SQLException e){ e.printStackTrace(); }

        Assert.assertTrue(empty_database_exists);
    }
}
