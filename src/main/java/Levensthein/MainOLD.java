package Levensthein;/* Labb 2 i DD1352 Algoritmer, datastrukturer och komplexitet    */
/* Se labbanvisning under kurssidan http://www.csc.kth.se/DD1352 */
/* Ursprunglig författare: Viggo Kann KTH viggo@nada.kth.se      */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class MainOLD {



  public static ArrayList<String> readWordList(BufferedReader input) throws IOException {
        ArrayList<String> list1 = new ArrayList<>();

        while (true) {
          String s = input.readLine();
          if (s.equals("#"))
            break;
          list1.add(s);
        }
        return list1;
  }

  public static void main(String args[]) throws IOException {
      //long t1 = System.currentTimeMillis();
      StringBuilder sb1 = new StringBuilder();
      StringBuilder sb2 = new StringBuilder();

      BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));

      // Säkrast att specificera att UTF-8 ska användas, för vissa system har annan
      // standardinställning för teckenkodningen.

      ArrayList<String> wordList1 = readWordList(stdin);
      String word;

      //create an object for closestWord with the wordlist as argument
      Levensthein levensthein = new Levensthein(wordList1);

      while ((word = stdin.readLine()) != null) {

          //calculate the distance from the given word and the wordlist.
          levensthein.calculateDistance(word);
          sb1.append(word).append(" (").append(levensthein.getMinDistance()).append(")");

         for (String w : levensthein.getClosestWords())
             sb1.append(" ").append(w);

          sb1.append("\n");
          sb2.append(sb1.toString());
          sb1.setLength(0);

    }
        System.out.print(sb2.toString());
        //long tottime = (System.currentTimeMillis() - t1);
        //System.out.println("CPU time: " + tottime + " ms");

  }
}
