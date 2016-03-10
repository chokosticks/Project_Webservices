package main;


import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import Levensthein.Levensthein;

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

    public double calculateScore(String w1, String w2) {
        w1 = w1.toLowerCase();
        w2 = w2.toLowerCase();

        // Compare w1 with w2 and all words from wordnet
        double maxLevDistance = levensthein.getNormalizedDistance(w1, w2), temp = 0.0;
        Synset[] synsets = database.getSynsets(w2, SynsetType.NOUN);
        NounSynset nounSynset;
        Set<String> w2words = new TreeSet<String>();
        for (int i = 0; i < synsets.length; i++) {
            nounSynset = (NounSynset) synsets[i];
            w2words.add(nounSynset.getWordForms()[0]);

            temp = levensthein.getNormalizedDistance(w1, nounSynset.getWordForms()[0]);
            if(temp > maxLevDistance) maxLevDistance = temp;
        }

        cachedWords.put(w2, w2words);

        return maxLevDistance;
    }
}
