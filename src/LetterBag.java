import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

/**
 * Created by NevilleVH on 2015-08-10.
 */
public class LetterBag {
    private ArrayList<Tile> letters;
    

    public LetterBag (){
        letters = new ArrayList<>(100);
        letters.addAll(Collections.nCopies(2, new Tile('_')));
        letters.addAll(Collections.nCopies(12, new Tile('E')));
        letters.addAll(Collections.nCopies(9, new Tile('A')));
        letters.addAll(Collections.nCopies(9, new Tile('I')));
        letters.addAll(Collections.nCopies(8, new Tile('O')));
        letters.addAll(Collections.nCopies(6, new Tile('N')));
        letters.addAll(Collections.nCopies(6, new Tile('R')));
        letters.addAll(Collections.nCopies(6, new Tile('T')));
        letters.addAll(Collections.nCopies(4, new Tile('L')));
        letters.addAll(Collections.nCopies(4, new Tile('S')));
        letters.addAll(Collections.nCopies(4, new Tile('U')));
        letters.addAll(Collections.nCopies(4, new Tile('D')));
        letters.addAll(Collections.nCopies(2, new Tile('B')));
        letters.addAll(Collections.nCopies(2, new Tile('C')));
        letters.addAll(Collections.nCopies(2, new Tile('M')));
        letters.addAll(Collections.nCopies(2, new Tile('P')));
        letters.addAll(Collections.nCopies(2, new Tile('F')));
        letters.addAll(Collections.nCopies(2, new Tile('H')));
        letters.addAll(Collections.nCopies(2, new Tile('V')));
        letters.addAll(Collections.nCopies(2, new Tile('W')));
        letters.addAll(Collections.nCopies(2, new Tile('Y')));
        letters.add(new Tile('K'));
        letters.add(new Tile('J'));
        letters.add(new Tile('X'));
        letters.add(new Tile('Q'));
        letters.add(new Tile('Z'));
    }
    
    public ArrayList<Tile> takeN(int N){
        N = Math.min(letters.size(), N);
        ArrayList<Tile> result = new ArrayList<>(N);
        Random rnd = new Random();
        for (int i = 0; i < N; i++){
            int index = rnd.nextInt(letters.size());
            Tile tile = letters.get(index);
            result.add(tile);
            letters.remove(tile);
        }
        return result;
    }

    public int numAvailable(){
        return letters.size();
    }
}
