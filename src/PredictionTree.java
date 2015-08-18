import java.util.*;

/**
 * Created by NevilleVH on 2015-08-15.
 */
public class PredictionTree{
    //REQUIRES SORTED ARRAY OF UNIQUE WORDS EACH FOLLOWED BY A SPACE
    private HashMap<Character,PredictionTree> branches = new HashMap<Character,PredictionTree>();

    PredictionTree(){}
    PredictionTree(String[] words) {
        AddBranches(words);
    }

    public Set<Character> possibleCharacters(String prefix){
        return possibleCharacters(prefix.substring(1), branches.get(prefix.charAt(0)));
    }

    private HashSet<Character> possibleCharacters(String prefix, PredictionTree pt){
        if (prefix.length() == 0) {
            HashSet<Character> temp = new HashSet<>();
            temp.addAll(pt.branches.keySet());
            return temp;
        }
        else
            return possibleCharacters(prefix.substring(1), pt.branches.get(prefix.charAt(0)));
    }

    private void AddBranches(String[] words){
        int start = 0;
        if (words[0].equals(" ")) {//clean this up?
            branches.put(' ',new PredictionTree());
            start = 1;
        }
        int end;
        for (int i = start; i < words.length; i++) {

            char current = words[i].charAt(0);
            words[i] = words[i].substring(1);
            if (i == words.length - 1 || current != words[i + 1].charAt(0)) {
                end = i + 1;
                branches.put(current, new PredictionTree(Arrays.copyOfRange(words, start, end)));
                start = i + 1;
            }
        }

    }
}