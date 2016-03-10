package Levensthein;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by antondahlin on 2016-01-30.
 */
public class Levensthein {

    ArrayList<String> closestWords = new ArrayList<>();
    int closestDistance = Integer.MAX_VALUE;
    protected static ArrayList<String> wordList = new ArrayList<>();
    static int wordLength = 0;
    static int compWordLength = 0;

    public Levensthein(ArrayList<String> wordList){
        this.wordList = wordList;
    }


    int partDist(String word, String comparingWord, int[] currentRow, int[] previousRow) {

        int cost;
        if(word.equals(comparingWord)) return 0;

        for(int i = 0; i < previousRow.length; i++)
            previousRow[i] = i;

        for(int i = 0; i < wordLength; i++){
            currentRow[0] = i + 1;
            for(int j = 0; j < compWordLength; j++ ){
                cost = word.charAt(i) == comparingWord.charAt(j) ? 0 : 1;
                currentRow[j + 1] = minimumOf(currentRow[j]+1, previousRow[j+1]+1, previousRow[j] + cost);//replace, insert, delete
            }
            for(int k = 0; k < previousRow.length; k++)
                previousRow[k] = currentRow[k];
        }
        return currentRow[compWordLength];
    }




    private final static int minimumOf(int a, int b, int c){
        return Math.min(a, Math.min(b, c));
    }



    int distance(String word, String comparingWord){
        int[] currentRow = new int[compWordLength +1];
        int[] previousRow = new int[compWordLength +1];

        return this.partDist(word, comparingWord, currentRow, previousRow);
    }





    public int[] getUniqueLetters(String word){
        int[] letters = new int[256];
        for(int i = 0; i< word.length(); i++)
            letters[(int) word.charAt(i)] = 1;
        return letters;
    }




    public int compareUniqueLetters(int[] distinctLetters, String s){
        int ret = 0;
        for (int i = 0; i < s.length(); i++) {
            if (distinctLetters[(int) s.charAt(i)] == 0)
                ret++;
        }
        return ret;
    }



    public void calculateDistance(String word){
        wordLength = word.length();
        closestDistance = Integer.MAX_VALUE;
        int[] wordUniqueLetters = getUniqueLetters(word);
        int dist = 0;

        for(String comparingWord: wordList){
            compWordLength = comparingWord.length();

            //If the difference in length between word and comparingWord
            //is bigger than closetDistance, don't bother comparing.
            //OR
            //if the difference between unique letters between word and comparingWord is greater
            //than closestDistance, don't bother comparing.
            if(Math.abs(wordLength-compWordLength) > closestDistance || compareUniqueLetters(wordUniqueLetters, comparingWord) > closestDistance) continue;

            dist = this.distance(word, comparingWord);

            if(dist < closestDistance){
                this.closestDistance = dist;
                this.closestWords = new ArrayList<>();
                this.closestWords.add(comparingWord);
            }else if(dist == this.closestDistance){
                this.closestWords.add(comparingWord);
            }
        }
    }




    int getMinDistance() {
        return this.closestDistance;
    }




    List<String> getClosestWords() {
        return this.closestWords;
    }

}
