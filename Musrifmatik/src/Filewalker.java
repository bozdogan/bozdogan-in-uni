import java.io.*;
import java.util.Base64;

// Bozdoğan Bilişim A.Ş. presents
public class /*Luke*/Filewalker{

    /**
     * All the content of the file. Uses UTF-8 encoding.
     *
     * @param filepath path of the file
     * @return content of the file
     */
    public static String readAll(String filepath) throws IOException{
        StringBuilder cache = new StringBuilder(); // temporary storage
        FileReader in = null; // the input streamer

        try{
            in = new FileReader(filepath); // initializing input stream

            int c;
            while((c = in.read()) != -1){ /* until it reaches the end of the file
            that is gonna be read one char per time. when ´c´ is -1, it means the file is completely
            scanned. therefore i return the file's whole content as a ´String´
            */
                cache.append((char) c); // append it as a ´char´, obviously
            }
        } finally{
            if(in != null) in.close(); // İşin bitince kapat.
        }


        return cache.toString();
    }

    public static boolean writeOver(String filepath, String data) throws IOException{
        FileWriter out = null; // the output stream

        // write the whole data to a file, to THE file more specifically, byte by byte..
        try{
            out = new FileWriter(filepath);
            out.write(data);
        } catch(IOException e){
            e.printStackTrace();
            return false; /* At the end of the day, any IO error can cause this
                function to return false, no matter if it's file d.n.e. or stream gets
                interrupted.
                */
        } finally{
            if(out != null) out.close(); // closing thing..
        }


        return true;
    }
}
