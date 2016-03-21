package main;


import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import Levensthein.Levensthein;


/**
 * Created by antondahlin on 2016-03-03.
 */
public class Wordnet
{
    WordNetDatabase database;
    Levensthein levensthein;
    HashMap<String, Set<String>> cachedWords;

    public Wordnet() {
        database = WordNetDatabase.getFileInstance(); // Load the WordNet database
        levensthein = new Levensthein();
        cachedWords = new HashMap<String, Set<String>>();
    }

    public double calculateLevenstheinScore(String word1, String word2) {
        word1 = word1.toLowerCase();
        word2 = word2.toLowerCase();

        // Compare word1 with word2 and all words from wordnet
        double maxLevDistance = levensthein.getNormalizedDistance(word1, word2);
        double temp = 0.0;
        Synset[] synsets = database.getSynsets(word2, SynsetType.NOUN);
        NounSynset nounSynset;
        Set<String> w2words = new TreeSet<String>();
        for (int i = 0; i < synsets.length; i++) {
            nounSynset = (NounSynset) synsets[i];
            w2words.add(nounSynset.getWordForms()[0]);

            temp = levensthein.getNormalizedDistance(word1, nounSynset.getWordForms()[0]);
//
//            System.out.println("[word1] "+word1+" [word2] "+word2+" [nounset[0]] "+nounSynset.getWordForms()[0]
//            +" [temp : maxLev] "+temp+" : "+maxLevDistance);

            if(temp > maxLevDistance)
                maxLevDistance = temp;
        }

        cachedWords.put(word2, w2words);

        return maxLevDistance;
    }
}
