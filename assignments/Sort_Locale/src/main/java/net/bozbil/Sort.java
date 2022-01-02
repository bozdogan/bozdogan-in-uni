package net.bozbil;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.text.Collator;
import java.util.*;

// b_ozdogan
public class Sort{
    public static void main(String[] args){
        
        // MANUAL ARGUMENT HANDLING
        if(args.length < 1){ show_info(); return; }
        Path file = Paths.get(args[0]);
        String locale = args.length>=2 ? args[1] : "tr-TR"; // default locale is Turkish.

        try{

            List<String> cache = Files.readAllLines(file, Charset.forName("UTF-8"));

            // SPECIFY THE LOCALE
            Collator userColl = Collator.getInstance(Locale.forLanguageTag(locale));
            userColl.setDecomposition(Collator.CANONICAL_DECOMPOSITION);
            cache.sort(userColl);

            Files.write(Paths.get("sorted.txt"), cache, Charset.forName("UTF-8"));


        } catch(IOException err){
            if(Files.exists(file))
                System.out.println("Dosya ´"+file.getFileName()+"´ mevcut değil.");
            else
                System.out.println("Dosyaya ulaşılamıyor: ´"+file.getFileName()+"´.");
        }

    }

    private static void show_info(){
        System.out.println("KULLANIM: java Sort <girdi_dosyası> <dil&bölge>\n" +
                "    Ör. java Sort isimler.txt tr-TR");
    }
}