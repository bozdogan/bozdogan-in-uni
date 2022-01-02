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
import java.util.Map;

public class EnableEncryption{
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
            notebook.setCryptor(new Cryptor(password1));

            notebook.putNote("Title1", "Content of Entry 1");
            notebook.putNote("Title2", "Content of Entry 2");
            notebook.putNote("Title3", "Content of Entry 3");
            notebook.putNote("Title4", "Content of Entry 4");
            notebook.putNote("Title5", "Content of Entry 5");

        } catch(SQLException e){e.printStackTrace();}
    }

    @Test
    public void read_db_with_encryption(){
        Path test_file = Paths.get(testFile_location);

        try(Notebook db = new Notebook((test_file.toString()))){
            System.out.println("\n@ INFO: Test loading db w/ encryption\n");

            db.setCryptor(new Cryptor(password1));

            boolean read_success = true;
            for(Map.Entry<Integer, String> e: db.getTitles().entrySet()){
                int id = e.getKey();
                Notebook.Note current_entry = db.getNote(id);

                System.out.print(current_entry.Title()+" == ");
                System.out.println(current_entry.Content());

                read_success &= current_entry.Title().equals("Title"+id);
                read_success &= current_entry.Content().equals("Content of Entry "+id);
            }

            Assert.assertTrue(read_success);

        } catch(SQLException e){e.printStackTrace();}
    }


}
