package org.bozdogan;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GameLogic {
    public static class Host {
        List<String> words = new LinkedList<>();

        /**
         * @return 0 on success, 1 if word is already on the list,
         * 2 if word doesn't match the previous one.*/
        public int appendWord(String word) {
            word = word.toUpperCase().trim();

            if(! words.isEmpty()) {
                if(words.contains(word))
                    return 1;

                System.out.printf("  [appendWord] onset(\"%s\"): %s; coda(\"%s\"): %s\n",
                        word, onset(word), last(), coda(last()));

                if(!onset(word).equals(coda(last())))
                    return 2;
            }

            words.add(word);
            return 0;
        }

        public List<String> getWordList() {
            return new ArrayList<>(words);
        }

        public String last() {
            return words.get(words.size() - 1);
        }



    }

    //
    // AUX //
    public static String onset(String s) {
        return s.substring(0, 2);
    }

    public static String coda(String s) {
        return s.substring(s.length()-2, s.length());
    }
}
