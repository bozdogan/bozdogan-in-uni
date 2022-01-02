package org.bozdogan.model;

import org.bozdogan.util.Cryptor;
import org.bozdogan.util.MetaData;

import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

/** Notebook file interface for Simon. (It's a connector.)*/
public class Notebook implements AutoCloseable{

    private Connection conn;
    private String databaseLocation;
    /** The file path to the notebook. Can be relative. */
    public String getDatabaseLocation(){ return databaseLocation; }
    private Map<String, String> metadata;
    private Cryptor cryptor;
    /** The hashed password for the notebook, null if not encrypted. */
    public String getPassword(){
        //loadMetaData();
        if(isEncrypted())
            return metadata.get("hashed_pw");

        return null;
    }

    public Notebook(String URL) throws SQLException{
        this.databaseLocation = Paths.get(URL).toAbsolutePath().toString();

        loadMetaData();
        if(metadata==null)
            metadata = new HashMap<>();

        if(metadata.isEmpty()){
            Statement statement = getConnection().createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS entries(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "title TEXT NOT NULL," +
                    "content TEXT" +
                    ")");

            statement.close();

            metadata.put("encrypted", "0");
            saveMetaData();
        }

        if(! validateNotebook())
            throw new RuntimeException("UNRECOGNIZED TABLE");
    }

    private Connection getConnection() throws SQLException{
        if(conn==null){
            try{
                Class.forName("org.sqlite.JDBC"); // driver check

                this.conn = DriverManager.getConnection(
                        "jdbc:sqlite://"+databaseLocation);

            } catch(ClassNotFoundException e){
                System.out.println("SQLite driver is not present.");
                System.err.println("Error: "+e.getMessage());
            }
        }

        return conn;
    }

    /** Check if the database has a valid ´entries´ table for Simon. */
    private boolean validateNotebook(){
        String expectedTableStructure =
                "0|id|INTEGER|0|null|1\n"+
                        "1|title|TEXT|1|null|0\n"+
                        "2|content|TEXT|0|null|0\n";

        String currentStructure = "";

        try{ currentStructure = _generateTableStructure(); }
        catch(SQLException e){ e.printStackTrace(); }

        return currentStructure.equals(expectedTableStructure);
    }

    private String _generateTableStructure() throws SQLException{
        // Helper method that creates a simple string of table structure
        // to check if the table in question is actually a Simon database
        // or to determine if it's an empty database that does not contain
        // ´entries´ table.
        PreparedStatement get_table_info = getConnection().prepareStatement(
                "PRAGMA table_info(entries)"
        );

        ResultSet resultSet = get_table_info.executeQuery();

        StringBuilder tableStr = new StringBuilder();
        while (resultSet.next())
            tableStr.append(resultSet.getString(1)) //cid
                    .append("|").append(resultSet.getString(2)) //name
                    .append("|").append(resultSet.getString(3)) //type
                    .append("|").append(resultSet.getString(4)) //notnull
                    .append("|").append(resultSet.getString(5)) //dflt_value
                    .append("|").append(resultSet.getString(6)) //pk
                    .append("\n");

        return tableStr.toString();
    }

    public boolean isEncrypted(){
        loadMetaData();
        if(! metadata.isEmpty()){
            String val = metadata.get("encrypted");
            return val!=null && val.equals("1");
        }

        return false;
    }

    public boolean setCryptor(Cryptor cryptor){
        loadMetaData();

        if(! isEncrypted()){
            metadata.put("encrypted", "1");
            metadata.put("hashed_pw", cryptor.getHashedPassword());
            metadata.put("algorithm", cryptor.getAlgorithm());

            for(Map.Entry<Integer, String> e : getTitles().entrySet()){
                int id = e.getKey();
                Note current = getNote(id, false);

                if(current!=null){
                    String title = current.Title();
                    String content = current.Content();
                    String newTitle = cryptor.encrypt(title);
                    String newContent = cryptor.encrypt(content);

                    updateNote(id, newTitle, newContent, false);
                }
            }

            saveMetaData();
            this.cryptor = cryptor;
            return true;

        } else if(
                metadata.get("hashed_pw").equals(cryptor.getHashedPassword()) &&
                metadata.get("algorithm").equals(cryptor.getAlgorithm())
        ){
            this.cryptor = cryptor;
            return true;
        }

        return false;
    }

    public boolean changeCryptor(Cryptor newCryptor){
        if(cryptor==null)
            return false;

        if(newCryptor.equals(this.cryptor))
            return true;

        loadMetaData();

        if(newCryptor.equals(new Cryptor(""))){
            newCryptor = null;
            metadata.put("encrypted", "0");
            metadata.remove("hashed_pw");
            metadata.remove("algorithm");
            saveMetaData();
        } else{
            metadata.put("encrypted", "1");
            metadata.put("hashed_pw", newCryptor.getHashedPassword());
            metadata.put("algorithm", newCryptor.getAlgorithm());
            saveMetaData();
        }

        for(Map.Entry<Integer, String> e : getTitles().entrySet()){
            int id = e.getKey();
            Note current = getNote(id, false);

            if(current!=null){
                String title = cryptor.decrypt(current.Title());
                String content = cryptor.decrypt(current.Content());

                if(newCryptor!=null){
                    String newTitle = newCryptor.encrypt(title);
                    String newContent = newCryptor.encrypt(content);
                    updateNote(id, newTitle, newContent, false);
                } else
                    updateNote(id, title, content, false);
            }
        }

        this.cryptor = newCryptor;
        return true;
    }

    /** Map of Note ids with the titles in the notebook */
    public SortedMap<Integer, String> getTitles(){
        TreeMap<Integer, String> indexedTitles = new TreeMap<>();
        boolean encryption = cryptor!=null && isEncrypted(); //@!

        try{
            Connection conn = getConnection();
            if(conn!=null){
                PreparedStatement select_notes = conn.prepareStatement(
                        "SELECT id, title FROM entries WHERE id <> 0");
                ResultSet results = select_notes.executeQuery();

                while(results.next()){
                    int id = results.getInt(1);
                    String title = results.getString(2);
                    if(encryption)
                        title = cryptor.decrypt(title);

                    indexedTitles.put(id, title);
                }

                select_notes.close();
            }

        } catch(SQLException e){ e.printStackTrace(); }

        return indexedTitles;
    }

    public Note getNote(int id){ return getNote(id, true); }
    private Note getNote(int id, boolean handleEncryption){
        if(id==0)
            return null; //metadata cannot be accessed this way
        boolean encryption = cryptor!=null && handleEncryption && isEncrypted(); //@!

        Note _note = null;
        try{
            Connection conn = getConnection();
            if(conn!=null){
                PreparedStatement select = conn.prepareStatement(
                        "SELECT title, content FROM entries WHERE id=?");
                select.setInt(1, id);
                ResultSet results = select.executeQuery();

                if(results.next()){
                    String title = results.getString(1);
                    String content = results.getString(2);
                    if(encryption){
                        title = cryptor.decrypt(title);
                        content = cryptor.decrypt((content));
                    }

                    if(title==null)
                        title = "DECRYPTION ERROR";

                    _note = new Note(title, content);
                }

                select.close();
            }

        } catch(SQLException e){ e.printStackTrace(); }

        return _note;
    }

    public boolean putNote(String title, String content){
        boolean _success = false;
        boolean encryption = cryptor!=null && isEncrypted(); //@!

        try{
            Connection conn = getConnection();
            if(conn!=null){
                PreparedStatement insert = conn.prepareStatement(
                        "INSERT INTO entries (title, content) VALUES (?, ?)");

                if(encryption){
                    title = cryptor.encrypt(title);
                    content = cryptor.encrypt((content));
                }

                insert.setString(1, title);
                insert.setString(2, content);

                if(insert.execute())
                    _success = true;

                insert.close();
            }

        } catch(SQLException e){ e.printStackTrace(); }

        return _success;
    }

    public boolean updateNote(int id, String title, String content){
        return updateNote(id, title, content, true);}
    private boolean updateNote(int id, String title, String content, boolean handleEncryption){
        boolean _success = false;
        boolean encryption = cryptor!=null && handleEncryption && isEncrypted(); //@!

        try{
            Connection conn = getConnection();
            if(conn!=null){
                PreparedStatement insert = conn.prepareStatement(
                        "UPDATE entries SET title=?, content=? WHERE id=?");

                if(encryption){
                    title = cryptor.encrypt(title);
                    content = cryptor.encrypt((content));
                }

                insert.setInt(3, id);
                insert.setString(1, title);
                insert.setString(2, content);

                if(insert.executeUpdate()>1)
                    _success = true;

                insert.close();
            }

        } catch(SQLException e){ e.printStackTrace(); }

        return _success;
    }

    public boolean deleteNote(int id){
        boolean _success = false;

        try{
            Connection conn = getConnection();
            if(conn!=null){
                PreparedStatement delete = conn.prepareStatement(
                        "DELETE FROM entries WHERE id=?");
                delete.setInt(1, id);

                if(delete.execute())
                    _success = true;

                delete.close();
            }
        } catch(SQLException e){ e.printStackTrace(); }

        return _success;
    }

    public Map<Integer, Note> search(String searchQuery){
        Cryptor currentCryptor = this.cryptor;
        if(currentCryptor!=null)
            // to make sql search possible, encryption is temporarily disabled.
            this.changeCryptor(new Cryptor(""));

        TreeMap<Integer, Note> searchResults = new TreeMap<>();
        try{
            Connection conn = getConnection();
            if(conn!=null){
                PreparedStatement select = conn.prepareStatement(
                        "SELECT id, title, content FROM entries WHERE content LIKE '%"+searchQuery+"%'");
                ResultSet results = select.executeQuery();

                while(results.next())
                    searchResults.put(results.getInt(1), new Note(
                            results.getString("title"),
                            results.getString("content")
                    ));
            }
        } catch(SQLException e){ e.printStackTrace(); }

        if(currentCryptor!=null)
            // encryption is re-enabled.
            this.changeCryptor(currentCryptor);

        return searchResults;
    }

    /** Closes the conn if no longer needed. To reconnect, another
     * {@code model.Notebook} instance must be created. */
    @Override
    public void close() throws SQLException{
        conn.close();
        conn = null; // to make seamless reconnect possible. getConnection() will handle the rest.
    }

    private void saveMetaData(){
        try{
            Connection conn = getConnection();
            if(conn!=null){
                PreparedStatement select = conn.prepareStatement(
                        "SELECT id FROM entries WHERE id=0");
                boolean metadata_exists = select.executeQuery().next();
                select.close();

                if(! metadata_exists){
                    PreparedStatement insert = conn.prepareStatement(
                            "INSERT INTO entries (id, title, content) VALUES (?, ?, ?)");

                    insert.setInt(1, 0);
                    insert.setString(2, "META_INF");
                    insert.setString(3, MetaData.serialize(metadata));
                    insert.execute();

                    insert.close();
                } else{
                    PreparedStatement update = conn.prepareStatement(
                            "UPDATE entries SET content=? WHERE id=0");

                    update.setString(1, MetaData.serialize(metadata));
                    update.execute();

                    update.close();
                }
            }
        } catch(SQLException e){ e.printStackTrace(); }
    }

    private void loadMetaData(){
        try{
            Connection conn = getConnection();
            if(conn!=null){
                PreparedStatement select = conn.prepareStatement(
                        "SELECT content FROM entries WHERE id=0");

                ResultSet results = select.executeQuery();
                if(results.next())
                    metadata = MetaData.deserialize(results.getString(1));

                select.close();
            }
        } catch(SQLException e){
            metadata = new HashMap<>();
        }
    }

    /** Notebook entry */
    public static class Note{
        private String title;
        private String content;

        public Note(String title, String content){
            this.title = title;
            this.content = content;
        }

        public String Title(){ return title; }
        public String Content(){ return content; }

        @Override
        public String toString(){
            return title+"=="+content;
        }

        @Override
        public boolean equals(Object o){
            return o instanceof Note
                    && ((Note) o).Title().equals(this.title)
                    && ((Note) o).Content().equals(this.content);
        }
    }
}