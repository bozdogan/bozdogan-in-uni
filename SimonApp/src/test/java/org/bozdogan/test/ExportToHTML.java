package org.bozdogan.test;

import org.bozdogan.util.SimpleHTMLExporter;
import org.bozdogan.model.Notebook;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.SQLException;

public class ExportToHTML{
    private static final String testFile_location = "tests/export.db";
    private static final String export_location = "tests/exportedHTML";

    @Before
    public void create_dummy_notebook(){
        Path test_file = Paths.get(testFile_location);

        try{ Files.deleteIfExists(test_file); }
        catch(IOException e){ e.printStackTrace(); return; }


        try(Notebook notebook = new Notebook(test_file.toString())){

            notebook.putNote("Title1", "Content of Entry 1");
            notebook.putNote("Title2", "Content of Entry 2");
            notebook.putNote("Title3", "Content of Entry 3");
            notebook.putNote("Title4", "Content of Entry 4");
            notebook.putNote("Title5", "Content of Entry 5");

        } catch(SQLException e){e.printStackTrace();}
    }

    @Test
    public void export_and_check(){
        Path test_file = Paths.get(testFile_location);

        boolean paths_exists = true;
        try(Notebook db = new Notebook((test_file.toString()))){

            try{ delete_directory_recursively(export_location);}
            catch(IOException e){ e.printStackTrace(); }

            SimpleHTMLExporter.exportAsMultipleHTML(db, export_location);
            paths_exists = Files.exists(Paths.get(export_location));
            paths_exists &= Files.exists(Paths.get(export_location+"/notes"));
            paths_exists &= Files.exists(Paths.get(export_location+"/notes/note_1.html"));
            paths_exists &= Files.exists(Paths.get(export_location+"/notes/note_2.html"));
            paths_exists &= Files.exists(Paths.get(export_location+"/notes/note_3.html"));
            paths_exists &= Files.exists(Paths.get(export_location+"/notes/note_4.html"));
            paths_exists &= Files.exists(Paths.get(export_location+"/notes/note_5.html"));

        } catch(SQLException e){e.printStackTrace();}

        Assert.assertTrue(paths_exists);
    }

    private void delete_directory_recursively(String directory) throws IOException{
        if(Files.exists(Paths.get(directory)))
            Files.walkFileTree(Paths.get(directory), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
    }
}
