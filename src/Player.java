import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by NevilleVH on 2015-08-10.
 */
public class Player {
    private String name;
    private int score;
    private TileList rack;

    public Player(){
        score = 0;
        rack = new TileList();
    }

    public Player(String pName){
        name = pName;
        score = 0;
        rack = new TileList();
    }

    //public ArrayList<Tile> getRack(){
    //    re
    //}


    public int getFinalScore(){
        return score - rack.totalPoints();
    }

    public void updateScore(int points){
        score += points;
    }

    public int getScore() {
        return score;
    }

    public void drawLetters(ArrayList<Tile> newLetters){
        rack.addTiles(newLetters);
    }

    public void removeLetter(char character){
        Tile letter = new Tile(character);
        letters.remove(letter);

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


    public TileList playTiles(TileList requiredLetters){
        //returns null if not all required letters are present
        if (rack.contains(requiredLetters)) {
            rack.removeTiles(requiredLetters);
            return requiredLetters;
        } else
            return null;
    }

}
