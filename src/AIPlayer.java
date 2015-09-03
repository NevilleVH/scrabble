import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.stream.Stream;

/**
 * Created by NevilleVH on 2015-08-19.
 */
public class AIPlayer extends Player {
    static PredictionTree predictionTree;
    static Dictionary dictionary;
    public enum Difficulty {EASY, INTERMEDIATE, HARD};

    public AIPlayer(String name, Difficulty difficulty){
        this.name = name;

        if (predictionTree == null){ // better way to do this? can't use static initialiser as need to get difficulty first
            dictionary = new Dictionary(difficulty.toString() + ".txt");
            try {
                String dir = System.getProperty("user.dir");
                if (dir.indexOf("out") == -1)
                    dir += "\\src";

                FileReader fileReader = new FileReader(dir+"\\"+difficulty.toString()+".txt");
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                ArrayList<String> words = new ArrayList<>();
                String word;
                while ((word = bufferedReader.readLine()) != null)
                    words.add(word + " ");
                predictionTree = new PredictionTree(words.toArray(new String[words.size()]));
            }
            catch (FileNotFoundException e){
                System.out.println(e.getMessage());
            }
            catch (IOException e){
                System.out.println(e.getMessage());
            }
        }
    }

    //public ArrayList<String> probableOpponentRack(Board board){
    //    byte[] unusedLetters = new byte[26];

    //}

/*
    public boolean anyPossibleMoves(Board board){
        Position moveDown = optimalWord(board);
        board.transposeBoard();
        Position moveRight = optimalWord(board);
        board.transposeBoard();
        return moveDown.getPoints() > 0 || moveRight.getPoints() > 0;
    }
    */
    public Position makeMove(Game game){ // maybe work out an expected value for the letters one might draw (taking into account letters already played etc) when deciding whether to play
        incrementTurns();
        double scoreThreshold = 0.4;
        Board board = game.getBoard();
        Position optimalMove;
        ArrayList<Position> moves = optimalMoves(board, this.rack);
        if (moves.size() > 0 && (optimalMove = moves.get(0)).getPoints() >= scoreThreshold * avgPointsPerRound()) {
            ArrayList<Tile> requiredTiles = playTiles(board.requiredLetters(optimalMove));
            board.playWord(optimalMove.getRow(), optimalMove.getCol(), optimalMove.getDirection(), requiredTiles);
            updateScore(optimalMove.getPoints());

            updateWordsPlayed(optimalMove);

            return optimalMove;
        } else if (game.getLetterBag().canExchange()) {
            exchange(game);
        }
        return null;
    }

    private void exchange(Game game){ //how robust with blanks?
        ArrayList<Tile> probable = game.probableDraws(this);
        ArrayList<Tile> tempRack = new ArrayList<>();
        tempRack.addAll(rack);
        for (int i = 0; i < 7; i++){
            tempRack.add(probable.get(i));
        }

        ArrayList<Position> moves = optimalMoves(game.getBoard(), tempRack);
        if (moves.size() == 0)
            return;
        HashMap<Character, Integer> letterFrequency = new HashMap<>();
        for (int i = 0; i < 0.25*moves.size(); i++){
            String required = game.getBoard().requiredLetters(moves.get(i));
            for (char c: required.toCharArray()) {
                Integer frequency = letterFrequency.get(c);
                letterFrequency.put(c, frequency == null ? moves.get(i).getPoints() : frequency + moves.get(i).getPoints());
            } //pseudo-frequency which is weighted by how highly the words in which the letter appears score
        }
        //we have the required letters for the top 25% words that we could make with our rack including the top 7 most likely tiles we could draw
        ArrayList<Character> letters = new ArrayList<>(letterFrequency.keySet());
        Collections.sort(letters, Comparator.comparing(letter -> -1*letterFrequency.get(letter)));
        //String lettersToExchange = "";

        for (int i = 0 ; i < 7; i++)
            letters.remove((Character)probable.get(i).getLetter());
        letters.stream().
                filter(character ->
                        letterFrequency.get(character) >= 0.5 * letterFrequency.get(letters.get(0))).
                forEach(character1 ->
                        removeLetter(character1));

        //of the tiles originally in our rack; those that are the least used are exchanged

        //playTiles(lettersToExchange);
        //game.getLetterBag().
    }

    private ArrayList<Position> optimalMoves(Board board, ArrayList<Tile> rack){
        ArrayList<Position> playDown = optimalWord(board,rack);
        board.transposeBoard();
        ArrayList<Position> playRight = optimalWord(board,rack);
        playRight.forEach(position -> {
            int temp = position.getCol();
            position.setCol(position.getRow());
            position.setRow(temp);
            position.setDirection(Board.Direction.RIGHT);
        });
        board.transposeBoard();

        ArrayList<Position> result = new ArrayList<>();
        result.addAll(playDown);
        result.addAll(playRight);
        Collections.sort(result, Comparator.comparing(position -> -1 * position.getPoints()));
        return result;
    }

    //private ArrayList<Position> optimalWord(Board board){
    //    return optimalWord(board,this.rack);
    //}

    private ArrayList<Position> optimalWord(Board board, ArrayList<Tile> rack){ //perhaps dictate which dictionary the isValid function can use so that the AI doesn't make any good moves by mistake (when we don't want it to)
        ArrayList<Position> result = new ArrayList<>();

        String letters = "";
        for (Tile tile : rack){ // NOT this.rack!
            letters += tile instanceof Blank ? " " : tile.getLetter();
        }
        for (int col = 0; col < board.getSize(); col++){
            for (int row = 0; row < board.getSize(); row++){
                if (board.letterOnTile(row, col) == null){

                    int start = board.closestTileAbove(row, col);
                    if (start == -1)
                        start = row;
                    int end = board.closestTileBelow(row, col);
                    String rowPrefix = board.prefixAlongRow(row, col);
                    String rowSuffix = board.suffixAlongRow(row, col);
                    if (!board.isEmpty()) {
                        boolean isBetweenWords = !(rowPrefix.equals("") && rowSuffix.equals(""));
                        boolean isTileAbove = (start == row - 1) && (start != -1);
                        boolean isTileBelow = (end < board.getSize()) && (end - row <= rack.size());
                        boolean canMakeWord = isBetweenWords || isTileAbove || isTileBelow;

                        if (!canMakeWord)
                            continue;
                    } else if (row != board.getSize()/2 || col != board.getSize()/2)
                        continue;

                    String colPrefix = board.columnPrefix(row, col);


                    String tempLetters = letters;
                    ArrayList<String> possible;

                    if (letters.contains(" ")) { // firstly check whether the class is Blank, second - this doesn't work for two blanks
                        possible = new ArrayList<>();
                        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                        for (char c : alphabet.toCharArray())
                            possible.addAll(getPossibleWords(colPrefix, tempLetters.replace(" ", c+""),board, row, col));
                            //FACTORIALS ARE SCARY THINGS

                    } else
                        possible = getPossibleWords(colPrefix, tempLetters,board, row, col);

                    for (String word : possible){
                        Position wordPosition = new Position(Board.Direction.DOWN,start, col, word);
                        if (board.isValid(wordPosition,dictionary)) {
                            String required = board.requiredLetters(wordPosition);
                            if (rackContains(required)){
                                int pts = board.pointsFromWord(start, col, Board.Direction.DOWN, copyTiles(required));
                                result.add(new Position(row,col,word,pts));
                                /*
                                if (pts > maxPts){
                                    maxPts = pts;
                                    maxWord = word;
                                    maxCol = col;
                                    maxRow = start;//row;
                                }
                                */
                            }
                        }
                    }

                }
            }
        }
        //Collections.sort(result, Comparator.comparing(position -> -1*position.getPoints()));
        return result;//new Position(maxRow, maxCol, maxWord, maxPts);
    }

    private ArrayList<String> getPossibleWords(String prefix, String letters,Board board, int row, int col){
        ArrayList<String> result = new ArrayList<>();
        if (row == board.getSize() || col == board.getSize())
            return result;

        if (prefix.equals("")) {
            for (char c : letters.toCharArray()) {
                result.addAll(getPossibleWords(prefix + c, letters.replace("" + c, ""),board, row + 1, col));
            }
            return result;
        }


        if (predictionTree.possibleCharacters(prefix).contains(' ')){
            result.add(prefix);
            return result;
        }
        Character letter;
        if ((letter = board.letterOnTile(row, col)) == null) { //there is no tile at this position
            for (char c : letters.toCharArray()) {
                if (predictionTree.possibleCharacters(prefix).contains(c)) {
                    result.addAll(getPossibleWords(prefix + c,  letters.replace("" + c, ""),board, row + 1, col));
                }

            }
        } else {
            if (predictionTree.possibleCharacters(prefix).contains(letter)) {
                result.addAll(getPossibleWords(prefix + letter, letters,board, row + 1, col));
            }
        }
        return result;
    }

}
