import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by NevilleVH on 2015-08-10.
 */
public class Player {
    private String name;
    private int score;
    private ArrayList<Tile> letters;

    public Player(String pName){
        name = pName;
        score = 0;
        letters = new ArrayList<>();
    }

    public ArrayList<Tile> getRack(){
        ArrayList<Tile> copy = new ArrayList<>();
        copy.addAll(letters);
        return copy;
    }

    public boolean rackEmpty(){
        return letters.size() == 0;
    }

    public int getFinalScore(){
        for (Tile t : letters){
            score -= t.getPoints();
        }
        return score;
    }

    public void updateScore(int points){
        score += points;
    }

    public int getScore() {
        return score;
    }

    public void drawLetters(ArrayList<Tile> newLetters){
        letters.addAll(newLetters);
    }

    public void removeLetter(char character){
        Tile letter = new Tile(character);
        letters.remove(letter);

    }

    public void removeLetters(String characters){
        for (char c : characters.toCharArray()) {
            Tile letter = new Tile(c);
            letters.remove(letter);
        }
    }

    public void removeLetters(ArrayList<Tile> tiles){
        letters.removeAll(tiles);
    }

    public int lettersToDraw(){
        return 7 - letters.size();
    }

    public String viewTiles(){

        return Cell.toString(letters);
    }
    private Tile findAndRemove(Tile tile){
        Tile blank = new Tile('_');
        if (letters.contains(tile)){
            letters.remove(tile);
            return tile;
        } else if (letters.contains(blank)){
            letters.remove(blank);
            blank.setLetter(tile.getLetter());
            return blank;
        }

        return null;

    }


    public ArrayList<Tile> getTiles(ArrayList<Tile> requiredLetters){ //this can and should be rewritten
        //returns null if not all required letters are present
        ArrayList<Tile> result = new ArrayList<>();
        ArrayList<Tile> tempLetters = new ArrayList<>();
        tempLetters.addAll(letters);
        for (Tile tile : requiredLetters){
            Tile temp;
            if ((temp = findAndRemove(tile)) != null){
                result.add(temp);
            } else {
                letters = tempLetters;
                return null;
            }
        }
        return result;

    }

}
