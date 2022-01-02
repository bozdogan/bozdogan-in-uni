import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main{
    static double bakiye;
    static ArrayList<Double> cache;
    static Scanner scanner = new Scanner(System.in);
    static String filepath = "bakiye.db";

    public static void main(String[] args) throws IOException{
        System.out.println("   Müsrifmatik 0.1 beta programına hoş geldiniz!!1");
        System.out.println();

        // dosya yoksa --> oluştur
        // dosya varsa --> oku

        if(!Files.exists(Paths.get(filepath))){

            Filewalker.writeOver(filepath,"");
        } else{

        }


    }

    private static String pack(){
        StringBuilder sb = new StringBuilder();
        sb.append("bakiye:").append(bakiye).append(";");

        for(int i=0; i<cache.size(); i++)
            sb.append(cache.get(i)<0 ? "-" : "+").append(cache.get(i));

        return sb.toString();
    }

    private static void cacheData(String data_packed){
//        if(!matches)
    }

    private static String input(){ return input(null); }
    /** Gets next input line.
     * @param prompt Text to print before asking for information
     * @return next line
     */
    private static String input(String prompt){
        if(prompt != null) System.out.print(prompt);
        return scanner.nextLine();
    }

    private static double numberInput(){ return numberInput(null); }
    /** Gets next numeric input. (Doesn't handle exceptions.)
     * @param prompt Text to print before asking for information
     * @return next double
     */
    private static double numberInput(String prompt){
        if(prompt != null) System.out.print(prompt);
        return scanner.nextDouble();
    }


}
