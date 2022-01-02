package org.bozdogan;

import java.util.Arrays;

public class Main{
    public static void main(String[] args){
        // duruma göre komut satırından ya da grafik arayüzle başlat.
        if(args.length>0 && args[0].equals("-cli")){
            CLI_UI.start(Arrays.copyOfRange(args, 1, args.length));
        } else{
            System.out.println("Starting Simon...");
            org.bozdogan.gui.Main.main(args);
        }
    }
}
