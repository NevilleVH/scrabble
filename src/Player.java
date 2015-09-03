import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

/**
 * Created by NevilleVH on 2015-08-10.
 */
public class Player {
    protected String name;
    private int score;
    private int numTurns = 0;
    private Stack<Position> wordsPlayed= new Stack<>(); //only need to access most recent word
    protected ArrayList<Tile> rack;

    public Player(){
        score = 0;
        rack = new ArrayList<>();
    }

    public Player(String pName){
        name = pName;
        score = 0;
        rack = new ArrayList<>();
    }

    public void updateWordsPlayed(Position position){
        wordsPlayed.push(position);
    }

    public String toString(){
        return String.format("%s\t\t%d\t\t%.2f\n", name, getFinalScore(), numTurns == 0 ? 0.0 : getFinalScore()/numTurns);
    }

    public double avgPointsPerRound(){
        return numTurns > 0 ? score/numTurns : 0;
    }


    public int getFinalScore(){
        return score - totalPointsOnRack();
    }

    public void updateScore(int points){
        score += points;
    }

    public int getScore() {
        return score;
    }

    public void drawTiles(ArrayList<Tile> newTiles){
        rack.addAll(newTiles);
    }

    public void incrementTurns(){
        numTurns++;
    }

    public void removeLetter(char character){
        Letter letter = new Letter(character);
        rack.remove(letter);

    }


    public void removeTiles(ArrayList<Tile> tiles){
        rack.removeAll(tiles);
    }

    public int tilesToDraw(){
        return 7 - rack.size();
    }

    public String viewTiles(){

        return Tile.displayTiles(rack);
    }


    private Tile getTile(char c){
        for (Tile tile : rack)
            if (tile.getLetter() == c)
                return tile;
        return null;
    }

    private Blank popBlank(){
        for (int i = 0; i < rack.size(); i++) {
            Tile tile = rack.get(i);
            if (tile instanceof Blank) {
                rack.remove(i);
                return new Blank();
            }
        }
        return null;
    }

    public ArrayList<Tile> playTiles(String requiredLetters){
        ArrayList<Tile> toBePlayed = new ArrayList<>();
        for (Character c : requiredLetters.toCharArray()){
            if (rackContains(c)){
                toBePlayed.add(new Letter(c));
                removeLetter(c);
            } else if (numberOfBlanks() > 0) { //the things we do in the name of readability
                Blank blank = popBlank();
                blank.setLetter(c);
                toBePlayed.add(blank);
            }
        }
        return toBePlayed;
    }

    private void removeBlank(){
        for (Tile t : rack)
            if (t instanceof Blank) {
                rack.remove(t);
                return;
            }

    }


    private int totalPointsOnRack(){
        int result = 0;
        for (Tile t : rack){
            result += t.getPoints();
        }
        return result;
    }




    public ArrayList<Tile> getTiles(){
        ArrayList<Tile> copy = new ArrayList<>();
        copy.addAll(rack);
        return copy;
    }

    public ArrayList<Tile> getTiles(ArrayList<Tile> tiles){ //this is necessary as the required rack will have default scores, whereas

        ArrayList<Tile> copy = new ArrayList<>();
        copy.addAll(rack);
        return copy;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Tile> copyTiles(String requiredLetters){ //this is necessary as the required rack will have default scores, whereas
        ArrayList<Tile> copy = new ArrayList<>();
        for (Character c : requiredLetters.toCharArray()){
            if (rackContains(c)){
                copy.add(new Letter(c));
            } else if (numberOfBlanks() > 0) { //the things we do in the name of readability
                Blank blank = new Blank();
                blank.setLetter(c);
                copy.add(blank);
            }
        }
        return copy;
    }

    public boolean rackContains(Tile tile){
        return rack.contains(tile);
    }

    public boolean rackContains(char letter){
        for (Tile tile : rack)
            if (tile.getLetter() == letter)
                return true;
        return false;
    }

    public boolean rackEmpty(){
        return rack.size() == 0;
    }
    private int numberOfBlanks(){
        int count = 0;
        for (Tile tile : rack)
            if (tile instanceof Blank)
                count++;
        return count;
    }

    public ArrayList<Tile> getRack(){
        ArrayList<Tile> temp = new ArrayList<>();
        temp.addAll(rack);
        return temp;
    }

    public boolean rackContains(String letters){
        for (Tile tile : rack) {
            String letter = tile.getLetter() + "";
            if (letters.contains(letter))
                letters = letters.replaceFirst(letter, "");
        }
        return numberOfBlanks() >= letters.length();

    }

    public boolean rackContains(ArrayList<Tile> tiles){
        ArrayList<Tile> temp = new ArrayList<>();
        temp.addAll(rack);
        for (Tile tile : tiles)
            if (temp.contains(tile)){
                temp.remove(tile); //java's containsAll method doesn't do this i.e. check that duplicates exist if required
            } else
                return false;
        return true;
    }



}
