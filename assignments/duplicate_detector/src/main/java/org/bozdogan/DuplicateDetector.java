package org.bozdogan;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.bozdogan.util.Hashing.sha1FromFile;
import static org.bozdogan.util.Hashing.bytes2hex;

public class DuplicateDetector{

    public static void main(String[] args){

        Map<String, List<Path>> visitList = new HashMap<>(); // gonna cause OoM exception at some point.

        // Starting directory
        Path start = Paths.get("D:\\bora\\belge\\__GECICI__");
        FileVisitor<Path> visitor = new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException{
                try{
//                    System.out.println("Processing "+file.toString());
                    System.out.print(". ");
                    String sum = bytes2hex(sha1FromFile(file));

                    if(! visitList.containsKey(sum))
                        visitList.put(sum, new LinkedList<>());

                    visitList.get(sum).add(file);
                } catch(FileSystemException e){
                    // happens when two processes try to reach a file at the same time.
                    return FileVisitResult.SKIP_SUBTREE;
                }

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc)/* throws IOException*/{
                // Handling exceptions properly.

                if(exc instanceof AccessDeniedException)
                    System.out.println("Access Denied: "+exc.getMessage());
                else if(exc instanceof FileSystemException)
                    System.out.println("File System Issue: "+exc.getMessage());
                else
                    System.out.println("Some IO Error: "+exc.getMessage());

                return FileVisitResult.SKIP_SUBTREE;
            }
        };

        try{
            System.out.println("Processing...");
            Files.walkFileTree(start, visitor);
        } catch(IOException e){ e.printStackTrace(); }

        System.out.println("\n\n   -- Duplicate files: \n" +
                "-----------------------------------------");

        for(Map.Entry<String, List<Path>> e: visitList.entrySet()){
            if(e.getValue().size()>1){
                System.out.println(e.getKey()+":");
                for(Path path : e.getValue())
                    System.out.println("    "+path.toString());
            }
        }

    }
}

//bzdgn