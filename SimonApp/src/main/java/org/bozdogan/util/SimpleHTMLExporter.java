package org.bozdogan.util;

import org.bozdogan.model.Notebook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

public class SimpleHTMLExporter{
    private final static String indexTemplate = "<html>\n"+
            "<head>\n"+
            "    <title>Notebook index</title>\n"+
            "    <meta charset=\"utf-8\">\n"+
            "</head>\n"+
            "<body>\n"+
            "    <div class=\"container\">\n"+
            "    <h2>Notebook index</h2>\n"+
            "    <ul>\n"+
            "        %s\n"+
            "    </ul>\n"+
            "    </div>\n"+
            "</body>\n"+
            "</html>";

    private final static String pageTemplate = "<html>\n"+
            "<head>\n"+
            "    <title>%s</title>\n"+
            "    <meta charset=\"utf-8\">\n"+
            "</head>\n"+
            "<body>\n"+
            "    <div class=\"container\">\n"+
            "    <h2>%s</h2>\n"+
            "    <p>%s</p>\n"+
            "    <hr>\n"+
            "    <a href=\"../index.html\">Back to index</a>\n"+
            "    </div>\n"+
            "</body>\n"+
            "</html>";

    public static boolean exportAsMultipleHTML(Notebook notebook, String exportLocation){
        Path location = Paths.get(exportLocation).toAbsolutePath();
        if(Files.exists(location))
            return false;

        try{
            Files.createDirectories(Paths.get(location.toString()+"/notes"));

            StringBuilder menuLinks = new StringBuilder();

            for(Map.Entry<Integer, String> e: notebook.getTitles().entrySet()){
                menuLinks.append(create_menu_anchor(e.getKey(), e.getValue())).append("\n");

                Notebook.Note current = notebook.getNote(e.getKey());

                ArrayList<String> file_content = new ArrayList<>();
                file_content.add(String.format(pageTemplate,
                        current.Title(), current.Title(), current.Content()));

                Files.write(Paths.get(location.toString()+"/notes/note_"+e.getKey()+".html"), file_content);
            }

            ArrayList<String> file_content = new ArrayList<>();
            file_content.add(String.format(indexTemplate, menuLinks.toString()));

            Files.write(Paths.get(location.toString()+"/index.html"), file_content);
            return true;

        } catch(IOException e){
            System.err.println("EXPORT: Cannot save files.");
            return false;
        }
    }

    private static String create_menu_anchor(int id, String title){
        return "<li><a href=\"notes/note_"+id+".html\">"+title+"</a></li>";
    }
}
