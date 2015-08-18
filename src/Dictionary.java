import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by NevilleVH on 2015-08-11.
 */
public class Dictionary {
    /*private HashMap<char[], ArrayList<String>> anagrams = new HashMap<>();

    public Dictionary(){
        try {
            FileReader fileReader = new FileReader("sowpods.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);


            String word;
            while ((word = bufferedReader.readLine()) != null){
                char[] key = word.toCharArray();
                Arrays.sort(key) ;
                if (anagrams.get(key) == null){
                    anagrams.put(key, new ArrayList<>());
                }
                anagrams.get(key).add(word);
            }

        } catch (FileNotFoundException ex){
            //System.out.println("sowpods.txt not found,");
        } catch (IOException ex){
            //System.out.println("Error reading file.");
        }
    }
    */
    private HashSet<String> dictionary = new HashSet<>();
    private HashMap<Character, HashMap<Integer, HashSet<String>>> letterPositions = new HashMap<>(); // .. far from perfect4
    private PredictionTree predictionTree;

    public Dictionary(){
        try {
            //System.out.println(System.getProperty("user.dir"));
            String dir = System.getProperty("user.dir");
            if (dir.indexOf("src") == -1)
                dir += "\\src";

            FileReader fileReader = new FileReader(dir+"\\sowpods.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            ArrayList<String> temp = new ArrayList<>();


            String word;
            while ((word = bufferedReader.readLine()) != null){
                temp.add(word + " ");
                dictionary.add(word);
                /*
                for (int i = 0; i < word.length(); i++){
                    char c = word.charAt(i);
                    if (letterPositions.get(c) == null)
                        letterPositions.put(c, new HashMap<>());

                    if (letterPositions.get(c).get(i) == null)
                        letterPositions.get(c).put(i, new HashSet<>());

                    letterPositions.get(c).get(i).add(word);
                }
                */
            }
            predictionTree = new PredictionTree(temp.toArray(new String[temp.size()]));

        } catch (FileNotFoundException ex){
            System.out.println("sowpods.txt not found.");
        } catch (IOException ex){
            //System.out.println("Error reading file.");
        }
    }

    public Set<Character> possibleCharacters(String prefix){
        return predictionTree.possibleCharacters(prefix);
    }

    public HashSet<String> possibleWords(char c, int index){
        return letterPositions.get(c).get(index);
    }

    public HashSet<String> possibleWords(String letters, int maxLength){
        //returns all words which contain at least the letters specified
        HashSet<String> result = new HashSet<>();
        int i = 0;
        result.addAll(allContaining(letters.charAt(i),maxLength));
        for (i = 1; i < letters.length(); i++)
            result.addAll(allContaining(letters.charAt(i), maxLength));
        return result;
    }

    public HashSet<String> getDictionary(){
        return dictionary;
    }

    private HashSet<String> allContaining(char letter, int maxLength){
        // returns all words that contain the specified letter
        if (letter == '_')
            return dictionary;
        HashSet<String> result = new HashSet<>();
        for (int i  : letterPositions.get(letter).keySet()) {
            if (i < maxLength) {
                result.addAll(letterPositions.get(letter).get(i));
            }
        }
        return result;
    }

    public boolean wordExists(String word){
        return dictionary.contains(word);
    }
}
