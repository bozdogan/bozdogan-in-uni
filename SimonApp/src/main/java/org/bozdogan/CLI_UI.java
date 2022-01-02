package org.bozdogan;

import org.bozdogan.model.Notebook;
import org.bozdogan.util.Cryptor;
import org.bozdogan.util.SimpleHTMLExporter;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class CLI_UI{
    private static ArrayList<String> options = new ArrayList<>();
    private static Scanner input = new Scanner(System.in);

    public static void start(String[] args){

        options.add(0, null);
        options.add(1, "Select a Notebook to Read");
        options.add(2, "Insert New Notes");
        options.add(3, "Update Notes");
        options.add(4, "Delete Notes");
        options.add(5, "Create a Notebook");
        options.add(6, "Set Encryption or Change Password");
        options.add(7, "Search in a Notebook");
        options.add(8, "Export a Notebook to HTML");

        menu_loop();
    }

    private static void menu_loop(){
        String decision = "";
        while(!decision.equals("0")){
            System.out.println();
            System.out.println("  -- MAIN MENU --");

            for(int i = 1; i<options.size(); i++)
                System.out.println("  "+i+". "+options.get(i));
            System.out.println("\n  0. Quit");

            System.out.print("\nSelect operation: ");
            if(input.hasNextLine())
                decision = input.nextLine();

            System.out.println();

            if(decision.equalsIgnoreCase("q"))
                decision = "0"; // quit app when q typed.

            switch(decision){
                case "1": read_loop(); break;
                case "2": insert_loop(); break;
                case "3": update_sequence(); break;
                case "4": delete_sequence(); break;
                case "5": create_notebook_sequence(); break;
                case "6": encryption_sequence(); break;
                case "7": search_sequence(); break;
                case "8": export_sequence(); break;

                case "0":
                    System.out.println("Quitting..."); break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void read_loop(){
        System.out.println("READ");

        System.out.println("Please type the location for the notebook.");
        System.out.print(" >_ ");
        String location = input.nextLine();

        Notebook notebook;
        try{notebook = new Notebook(location);
        } catch(SQLException e){
            System.out.println("Cannot open notebook.");
            return;
        }

        if(! Files.exists(Paths.get(location))){
            System.out.println("No such file: "+location);
            return;
        }

        if(notebook.isEncrypted()){
            System.out.println("Please type the password.");
            System.out.print(" >_ ");
            String password = input.nextLine();

            notebook.setCryptor(new Cryptor(password));
        }

        int selection;
        do{
            System.out.println("\nNotes:");
            for(Map.Entry<Integer, String> e : notebook.getTitles().entrySet())
                System.out.println(e.getKey()+". "+e.getValue());

            System.out.print("\nSelect a note to view('q' to quit): ");
            String __ = input.nextLine();
            if(__.equals("q"))
                selection = 0;
            else{
                try{ selection = Integer.parseInt(__); }
                catch(NumberFormatException e){ selection = -1; }
            }

            Notebook.Note note = notebook.getNote(selection);
            if(note!=null){
                System.out.println("\n    "+note.Title());
                for(int i=0; i<note.Title().length()+8; i++) System.out.print("-"); //formatting text
                System.out.println();

                System.out.println(note.Content());
            } else
                System.out.println("No note with id '"+selection+"'.");

        } while(selection!=0);

        try{
            notebook.close();
        } catch(SQLException e){
            System.out.println("something minor went wrong.");
        }

        System.out.println("Quitting to menu.");

    }

    private static void insert_loop(){
        System.out.println("INSERT");

        System.out.println("Please type the location for the notebook.");
        System.out.print(" >_ ");
        String location = input.nextLine();

        Notebook notebook;
        try{ notebook = new Notebook(location);
        } catch(SQLException e){
            System.out.println("Cannot open notebook.");
            return;
        }

        if(! Files.exists(Paths.get(location))){
            System.out.println("No such file: "+location);
            return;
        }

        if(notebook.isEncrypted()){
            System.out.println("Please type the password.");
            System.out.print(" >_ ");
            String password = input.nextLine();

            notebook.setCryptor(new Cryptor(password));
        }

        String title, content;

        System.out.print("Title: ");
        title = input.nextLine();
        System.out.println("Content:");
        content = input.nextLine();

        notebook.putNote(title, content);
        System.out.println("Note added.");

        try{
            notebook.close();
        } catch(SQLException e){
            System.out.println("ERROR SAVING NOTEBOOK");
        }
    }

    private static void update_sequence(){
        System.out.println("UPDATE");

        System.out.println("Please type the location for the notebook.");
        System.out.print(" >_ ");
        String location = input.nextLine();

        Notebook notebook;
        try{notebook = new Notebook(location);
        } catch(SQLException e){
            System.out.println("Cannot open notebook.");
            return;
        }

        if(!Files.exists(Paths.get(location))){
            System.out.println("No such file: "+location);
            return;
        }

        if(notebook.isEncrypted()){
            System.out.println("Please type the password.");
            System.out.print(" >_ ");
            String password = input.nextLine();

            notebook.setCryptor(new Cryptor(password));
        }

        System.out.println("\nNotes:");
        for(Map.Entry<Integer, String> e : notebook.getTitles().entrySet())
            System.out.println(e.getKey()+". "+e.getValue());


        int selection;
        System.out.print("\nSelect a note to update('q' to quit): ");
        String __ = input.nextLine();
        if(__.equals("q"))
            selection = 0;
        else{
            try{ selection = Integer.parseInt(__); }
            catch(NumberFormatException e){ selection = -1; }
        }

        Notebook.Note note = notebook.getNote(selection);
        if(note!=null){
            String title, content;

            System.out.print("New Title: ");
            title = input.nextLine();
            if(title.equals(""))
                title = note.Title();

            System.out.println("New Content:");
            content = input.nextLine();
            if(content.equals(""))
                content = note.Content();

            notebook.updateNote(selection, title, content);
            System.out.println("Note updated.");
        } else
            System.out.println("No note with id '"+selection+"'.");

        try{
            notebook.close();
        } catch(SQLException e){
            System.out.println("ERROR SAVING NOTEBOOK");
        }

        System.out.println("Quitting to menu.");
    }

    private static void delete_sequence(){
        System.out.println("DELETE");

        System.out.println("Please type the location for the notebook.");
        System.out.print(" >_ ");
        String location = input.nextLine();

        Notebook notebook;
        try{ notebook = new Notebook(location);
        } catch(SQLException e){
            System.out.println("Cannot open notebook.");
            return;
        }

        if(!Files.exists(Paths.get(location))){
            System.out.println("No such file: "+location);
            return;
        }

        if(notebook.isEncrypted()){
            System.out.println("Please type the password.");
            System.out.print(" >_ ");
            String password = input.nextLine();

            notebook.setCryptor(new Cryptor(password));
        }

        System.out.println("Notes:");
        for(Map.Entry<Integer, String> e : notebook.getTitles().entrySet())
            System.out.println(e.getKey()+". "+e.getValue());


        int selection;
        System.out.print("\nSelect a note to delete('q' to quit): ");
        String __ = input.nextLine();
        if(__.equals("q"))
            selection = 0;
        else{
            try{ selection = Integer.parseInt(__); }
            catch(NumberFormatException e){ selection = -1; }
        }

        Notebook.Note note = notebook.getNote(selection);
        if(note!=null){
            System.out.println("Are you sure you want to delete '"+note.Title()+"'?");
            __ = input.nextLine();
            if(__.equalsIgnoreCase("yes") || __.equalsIgnoreCase("y")){
                notebook.deleteNote(selection);
                System.out.println("Note deleted.");
            }
        } else
            System.out.println("No note with id '"+selection+"'.");

        try{
            notebook.close();
        } catch(SQLException e){
            System.out.println("ERROR SAVING NOTEBOOK");
        }

        System.out.println("Quitting to menu.");
    }

    private static void create_notebook_sequence(){
        System.out.println("CREATE NOTEBOOK");

        System.out.println("Please type the location for the notebook.");
        System.out.print(" >_ ");
        String location = input.nextLine();

        if(! Files.exists(Paths.get(location))){
            try{
                new Notebook(location).close();
                System.out.println("A new notebook is created at '"+location+"'");

            } catch(SQLException e){
                System.out.println("Cannot create notebook.");
            }
        } else
            System.out.println("A file already exists at '"+location+"'");

        System.out.println("Quitting to menu.");
    }

    /**
     * Setting or resetting the encryption of a notebook
     * ---
     * 1. Get notebook location.
     * 2. Open notebook.
     * 3. Check if encrypted.
     *   3.1. Ask for pw if encrypted.
     *   3.2. Set a cryptor w/ pw.
     *   3.4. If pw didn't match, ABORT.
     * 4. Ask for new pw.
     * 5. If encrypted, change cryptor.
     * 6. If not encrypted, set the new cryptor.
     * 7. Close notebook
     * 8. Exit to menu.
     */
    private static void encryption_sequence(){
        System.out.println("ENCRYPTION");

        System.out.println("Please type the location for the notebook.");
        System.out.print(" >_ ");
        String location = input.nextLine();

        Notebook notebook;
        try{ notebook = new Notebook(location);
        } catch(SQLException e){
            System.out.println("Cannot open notebook.");
            return;
        }

        if(!Files.exists(Paths.get(location))){
            System.out.println("No such file: "+location);
            return;
        }

        if(notebook.isEncrypted()){
            System.out.println("Please type the password.");
            System.out.print(" >_ ");
            String password = input.nextLine();

            boolean passwordMatches = notebook.setCryptor(new Cryptor(password));
            if(! passwordMatches){
                System.out.println("Wrong password.");

                try{ notebook.close(); }
                catch(SQLException e){ System.out.println("ERROR SAVING NOTEBOOK"); }
                System.out.println("Quitting to menu.");
            }
        }

        System.out.print("Enter new password: ");
        String password = input.nextLine();
        Cryptor newCryptor = new Cryptor(password);

        if(notebook.isEncrypted())
            notebook.changeCryptor(newCryptor);
        else
            notebook.setCryptor(newCryptor);
        System.out.println("Password has changed.");

        try{
            notebook.close();
        } catch(SQLException e){
            System.out.println("ERROR SAVING NOTEBOOK");
        }

        System.out.println("Quitting to menu.");
    }

    private static void search_sequence(){
        System.out.println("SEARCH");

        System.out.println("Please type the location for the notebook.");
        System.out.print(" >_ ");
        String location = input.nextLine();

        Notebook notebook;
        try{ notebook = new Notebook(location);
        } catch(SQLException e){
            System.out.println("Cannot open notebook.");
            return;
        }

        if(!Files.exists(Paths.get(location))){
            System.out.println("No such file: "+location);
            return;
        }

        if(notebook.isEncrypted()){
            System.out.println("Please type the password.");
            System.out.print(" >_ ");
            String password = input.nextLine();

            notebook.setCryptor(new Cryptor(password));
        }

        System.out.print("Your search query: ");
        String query = input.nextLine();

        System.out.println("\nNotes containing '"+query+"':");
        for(Map.Entry<Integer, Notebook.Note> e: notebook.search(query).entrySet())
            System.out.println(e.getKey()+". "+e.getValue().Title());
        System.out.println();

        try{
            notebook.close();
        } catch(SQLException e){
            System.out.println("ERROR SAVING NOTEBOOK");
        }

        System.out.println("Quitting to menu.");
    }

    private static void export_sequence(){
        System.out.println("EXPORT");

        System.out.println("Please type the location for the notebook.");
        System.out.print(" >_ ");
        String location = input.nextLine();

        Notebook notebook;
        try{ notebook = new Notebook(location);
        } catch(SQLException e){
            System.out.println("Cannot open notebook.");
            return;
        }

        if(!Files.exists(Paths.get(location))){
            System.out.println("No such file: "+location);
            return;
        }

        if(notebook.isEncrypted()){
            System.out.println("Please type the password.");
            System.out.print(" >_ ");
            String password = input.nextLine();

            notebook.setCryptor(new Cryptor(password));
        }

        System.out.print("Directory to export: ");
        String exportLocation = input.nextLine();

        System.out.println("Exported notes will NOT have encryption. Do you want to proceed?");
        String __ = input.nextLine();
        if(__.equalsIgnoreCase("yes") || __.equalsIgnoreCase("y")){

            SimpleHTMLExporter.exportAsMultipleHTML(notebook, exportLocation);
            System.out.println("Notebook exported.");
        } else
            System.out.println("Export operation aborted.");

        try{
            notebook.close();
        } catch(SQLException e){
            System.out.println("ERROR SAVING NOTEBOOK");
        }

        System.out.println("Quitting to menu.");
    }


    public static void main(String[] args){ start(args); }
}
