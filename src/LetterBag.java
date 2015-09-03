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
        letters.add (new Blank());
        letters.add (new Blank());//necessary because blanks are mutable, and n copies will give us two blanks of the same value: changing one changes the other
        letters.addAll(Collections.nCopies(12, new Letter('E')));
        letters.addAll(Collections.nCopies(9, new Letter('A')));
        letters.addAll(Collections.nCopies(9, new Letter('I')));
        letters.addAll(Collections.nCopies(8, new Letter('O')));
        letters.addAll(Collections.nCopies(6, new Letter('N')));
        letters.addAll(Collections.nCopies(6, new Letter('R')));
        letters.addAll(Collections.nCopies(6, new Letter('T')));
        letters.addAll(Collections.nCopies(4, new Letter('L')));
        letters.addAll(Collections.nCopies(4, new Letter('S')));
        letters.addAll(Collections.nCopies(4, new Letter('U')));
        letters.addAll(Collections.nCopies(4, new Letter('D')));
        letters.addAll(Collections.nCopies(2, new Letter('B')));
        letters.addAll(Collections.nCopies(2, new Letter('C')));
        letters.addAll(Collections.nCopies(2, new Letter('M')));
        letters.addAll(Collections.nCopies(2, new Letter('P')));
        letters.addAll(Collections.nCopies(2, new Letter('F')));
        letters.addAll(Collections.nCopies(2, new Letter('H')));
        letters.addAll(Collections.nCopies(2, new Letter('V')));
        letters.addAll(Collections.nCopies(2, new Letter('W')));
        letters.addAll(Collections.nCopies(2, new Letter('Y')));
        letters.add(new Letter('K'));
        letters.add(new Letter('J'));
        letters.add(new Letter('X'));
        letters.add(new Letter('Q'));
        letters.add(new Letter('Z'));
    }
    
    public ArrayList<Tile> takeN(int N){
        N = Math.min(letters.size(), N);
        ArrayList<Tile> result = new ArrayList<>(N);
        //int seed =(int) (Math.random()*1000);
        //System.out.println("seed is " + seed);
        Random rnd = new Random();
        //System.out.println("Seed is 2");
        for (int i = 0; i < N; i++){
            int index = rnd.nextInt(letters.size());
            Tile tile = letters.get(index);
            result.add(tile);
            letters.remove(tile);
        }
        return result;
    }
    public boolean canExchange(){
        return numAvailable() >= 7;
    }
    public int numAvailable(){
        return letters.size();
    }

    public ArrayList<Tile> getLetters() {
        return letters;
    }
}
