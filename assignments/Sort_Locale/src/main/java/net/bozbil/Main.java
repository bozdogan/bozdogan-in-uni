package net.bozbil;


import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.text.Collator;
import java.util.*;

// Bozdogan Bilisim
public class Main{
    public static void main(String[] args){
//        for(String arg: args)System.out.print(arg+", ");
//        System.out.println("\nsorting is happening, i guess..");

        if(args.length < 1){ show_info(); return; }

        // encoding
        // nio2


        Path file = Paths.get(args[0]);
        String locale = args.length>=2 ? args[1] : "tr-TR"; // default locale is Turkish.

        try{

            List<String> cache = Files.readAllLines(file, Charset.forName("UTF-8"));

            //@ that uses the locale string directly comes from the user. should i check it?
            Collator userColl = Collator.getInstance(Locale.forLanguageTag(locale));
            userColl.setDecomposition(Collator.CANONICAL_DECOMPOSITION);
            cache.sort(userColl);

            Files.write(file, cache, Charset.forName("UTF-8"));


        } catch(IOException err){
            if(Files.exists(file))
                System.out.println("Dosya ´"+file.getFileName()+"´ mevcut değil.");
            else
                System.out.println("Dosyaya ulaşılamıyor: ´"+file.getFileName()+"´.");
        }

    }

    private static void show_info(){
        System.out.println("KULLANIM: sortrows <girdi_dosyası> <dil&bölge>\n" +
                "    Ör. sortrows names.txt tr-TR");
    }
}
// https://stackoverflow.com/questions/12889760/sort-list-of-strings-with-localization //
// Main.java SONU