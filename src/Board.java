import java.util.*;

/**
 * Created by NevilleVH on 2015-08-11.
 */
public class Board {
    private byte size = 15;
    private Cell[][] board = new Cell[size][size];
    private Dictionary dictionary= new Dictionary();
    public enum Direction {DOWN, RIGHT}

    public Board(){
        byte[][] DW = {{7,7},{1,1},{2,2},{3,3},{4,4},{10,4},{11,3},{12,2},{13,1},{4,10},{3,11},{2,12},{1,13},{10,10},{11,11},{12,12},{13,13}};
        byte[][] TL = {{1,5},{1,9},{5,1},{5,5},{5,9},{5,13},{9,1},{9,5},{9,9},{9,13},{13,9},{13,5}};
        byte[][] DL = {{0,3},{0,11},{2,6},{2,8},{3,0},{3,7},{3,14},{6,2},{6,6},{6,8},{6,12},{8,8},{8,12},{7,3},{9,9},{8,2},{8,6},{12,8},{12,6},{7,11},{11,0},{11,7},{11,14},{14,3},{14,11}};
        byte[][] TW = {{0,0},{0,7},{0,14},{7,0},{7,14},{14,0},{14,14},{14,7}};
        for (byte[] coord : DW){
            board[coord[0]][coord[1]] = new Multiplier(Multiplier.MultiplierType.WRD, 2);
        }
        for (byte[] coord : DL){
            board[coord[0]][coord[1]] = new Multiplier(Multiplier.MultiplierType.LTR, 2);
        }
        for (byte[] coord : TW){
            board[coord[0]][coord[1]] = new Multiplier(Multiplier.MultiplierType.WRD, 3);
        }
        for (byte[] coord : TL){
            board[coord[0]][coord[1]] = new Multiplier(Multiplier.MultiplierType.LTR, 3);
        }
        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                if (board[i][j] == null)
                    board[i][j] = new Cell();
            }
        }
    }


    public Character letterOnTile(int row, int col){
        if (board[row][col] instanceof Tile){
            return ((Tile) board[row][col]).getLetter();
        } else
            return null;
    }
    public String requiredLetters(WordPosition wordPosition){
        String word = wordPosition.getWord();
        Direction direction = wordPosition.getDirection();
        int row = wordPosition.getRow();
        int col = wordPosition.getCol();
        String result = "";
        if (direction == Direction.RIGHT){
            transposeBoard();
            int temp = row;
            row = col;
            col = temp;
        }
        for (int i = 0; i < word.length(); i++){
            if (!isTile(board[row + i][col])){
                result += word.charAt(i);
            }
        }
        if (direction == Direction.RIGHT){
            transposeBoard();
        }
        return result;
    }

    private boolean isTile(Cell cell){
        return cell instanceof Tile;//instance of
    }
    private Character getLetter(Cell cell){
        if (cell instanceof Tile){
            return ((Tile) cell).getLetter();
        } else {
            return null;
        }
    }

    private Multiplier getMultiplier(Cell cell){
        if (cell.getClass().getName().equals("Multiplier")){
            return ((Multiplier) cell);
        } else {
            return null;
        }
    }
    public boolean isValid(WordPosition wordPosition){
        Direction direction = wordPosition.getDirection();
        int row = wordPosition.getRow();
        int col = wordPosition.getCol();
        if (direction == Direction.RIGHT){
            //easier to do the following (though less efficient) than to recode everything for this direction
            transposeBoard();
            wordPosition.setCol(row);
            wordPosition.setRow(col);
        }
        boolean result = isValidHelper(wordPosition);

        if (direction == Direction.RIGHT)
            transposeBoard();

        return result;
    }
    public boolean isValidHelper(WordPosition wordPosition){
        String word = wordPosition.getWord();
        int row = wordPosition.getRow();
        int col = wordPosition.getCol();
        //some redundancy here. refactor remaining code into separate methods
        if (row > size - 1 || row < 0 || col > size - 1 || col < 0 || row + word.length() - 1 >= size) {
            return false;
        }
        else {
            Character letter;

            if (!(row + word.length() > size )) {
                int i;

                //check whether the proposed word incorporates or hooks onto an existing word on the board.
                if (!isEmpty()) {
                    //then this is not the first move

                    boolean containsLetter = false;
                    //edges:
                    if (row > 0 && isTile(board[row-1][col]))
                        containsLetter = true;
                    else if (row + word.length() < size && isTile(board[row + word.length()][col]))
                        containsLetter = true;
                    for (i = row; i < row + word.length()-1 && !containsLetter; i++) {
                        for (int j = (col == 0) ? 0 : col - 1; j < size && j < col + 1 && !containsLetter; j++) {
                            containsLetter = (isTile(board[i][j]));
                        }
                    }
                    if (!containsLetter)
                        return false;
                } else if (!(col == size/2 && size/2 >= row && size/2 < row + word.length())){
                    return false;
                }

                //check whether at least one tile is open along the relevant cells
                int tileCount = 0;
                for (i = row; i < row + word.length(); i++){
                    if (isTile(board[i][col])){
                        tileCount++;
                    }
                }
                if (tileCount == word.length())
                    return false;

                //check whether any new words formed by playing the word are illegal:
                for (i = 0; i < word.length(); i++) {
                    Cell cell = board[row + i][col];
                    if (isTile(cell) && ((Tile) cell).getLetter() != word.charAt(i)) {
                        return false;
                    }
                }
                String possibleWord = "";
                for (i = row - 1; i >= 0 && (letter = getLetter(board[i][col])) != null; i--) {
                    possibleWord = letter + possibleWord;
                }

                possibleWord += word;
                for (i = row + word.length(); i <= size - 1 && (letter = getLetter(board[i][col])) != null; i++) {
                    possibleWord += letter;
                }

                if (!dictionary.wordExists(possibleWord)) {
                    return false;
                }
                for (i = row; i < row + word.length(); i++) {
                    possibleWord = "";
                    int j;
                    for (j = col - 1; j >= 0 && (letter = getLetter(board[i][j])) != null; j--) {
                        possibleWord = letter + possibleWord;
                    }
                    possibleWord += word.charAt(i - row);
                    for (j = col + 1; j <= size - 1 && (letter = getLetter(board[i][j])) != null; j++) {
                        possibleWord += letter;
                    }

                    if (possibleWord.length() > 1 && !dictionary.wordExists(possibleWord)) {
                        return false;
                    }
                }
            } else
                return false;

            return true;
        }
    }

    public void transposeBoard(){
        //transposed = !transposed;
        for (int i = 0; i < size; i++){
            for (int j = i + 1; j < size; j++){
                Cell temp = board[j][i];
                board[j][i] = board[i][j];
                board[i][j] = temp;
            }
        }
    }



    public String toString(){
        return Cell.toString(board);
    }

    public void playWord(int row, int col, Direction direction, ArrayList<Tile> tiles){
        if (direction == Direction.RIGHT){
            transposeBoard();
            int temp = row;
            row = col;
            col = temp;
        }
        int count = 0;
        for (int i = 0; count < tiles.size(); i++){
            if (!isTile(board[row+i][col])) {
                board[row + i][col] = tiles.get(count);
                count++;
            }
        }

        if (direction == Direction.RIGHT) transposeBoard();
    }


    public int pointsFromWord(int row, int col, Direction direction/*, String word*/, ArrayList<Tile> tiles){
        int points = 0;
        byte wordMultiplier = 1;

        if (direction == Direction.RIGHT){
            transposeBoard();
            int temp = row;
            row = col;
            col = temp;
        }
        int count = 0;
        Multiplier multiplier;
        int i = row-1;
        for (; i >= 0 && isTile(board[i][col]); i--){}
        i++;

        if (i != row) {
            if (direction == Direction.RIGHT) {
                transposeBoard();
                return pointsFromWord(col, i, Direction.RIGHT, tiles);
            }
        }

        for (i = 0; count < tiles.size()/*i < word.length()*/; i++){
            byte letterMultiplier = 1;

            if (!isTile(board[row + i][col])) {
                count++;
                if (board[row + i][col] instanceof Multiplier) {
                    multiplier = (Multiplier) board[row + i][col];
                    if (multiplier.getMultiplierType() == Multiplier.MultiplierType.LTR) {
                        letterMultiplier *= multiplier.getMultiplierValue();
                    } else if (multiplier.getMultiplierType() == Multiplier.MultiplierType.WRD) {
                        wordMultiplier *= multiplier.getMultiplierValue();
                    }
                }
                points += tiles.get(count - 1).getPoints() * letterMultiplier;
                //board[row + i][col] = tiles.get(count - 1);
            } else {
                points += ((Tile) board[row + i][col]).getPoints();
            }

        }
        if (count == 7) points += 50; //add a 'bonus' msg
        if (direction == Direction.RIGHT) transposeBoard();

        return (points) * wordMultiplier;
    }
    public boolean isEmpty(){
        return !isTile(board[size/2][size/2]);
    }

    public boolean isEmpty(int row, int col){
        return !(board[row][col] instanceof Tile);
    }

    public void playTile(Tile tile, int row, int col){
        board[row][col] = tile;
    }


    private boolean anyAdjacentTilesInRow(int row, int col){
        return (col - 1 >= 0 && isTile(board[row][col - 1])) || (col + 1 < size && isTile(board[row][col + 1]));
    }

    public String prefixAlongRow(int row, int col){
        String result = "";
        for (int i = col - 1; i > -1 && isTile(board[row][i]); i--){
            result = ((Tile) board[row][i]).getLetter() + result;
        }
        return  result;
    }

    public String suffixAlongRow(int row, int col){
        String result = "";
        for (int i = col + 1; i < size && isTile(board[row][i]); i++){
            result += ((Tile) board[row][i]).getLetter();
        }
        return  result;
    }

    public String columnPrefix(int row, int col){
        String result = "";
        while (row != 0 && isTile(board[row-1][col])) {
            row -= 1;
            result = ((Tile) board[row][col]).getLetter() + result;
        }
        return result;
    }

    public byte getSize(){return size;}





    public int closestTileAbove(int row, int col){
        int i;
        for (i = row - 1; i > -1 && !isTile(board[i][col]); i--){}
        return i;
    }
    public int closestTileBelow(int row, int col){
        int i;
        for (i = row + 1; i < size && !isTile(board[i][col]); i++){}
        return i;
    }

/*
    public String possiblePositions(String word, Direction direction, ArrayList<Tile> letters){
        String result = "";
        if (!isEmpty()) {
            for (int row = 0; row < size; row++) {
                for (int col = 0; col < size; col++) {
                    WordPosition wordPosition = new WordPosition(direction, row, col, word);
                    if (isValid(wordPosition) && isContained(letters, requiredLetters(wordPosition)))
                        result += String.format("Suggested word location:\nRow:\t%d\nColumn:\t%d\n", row + 1, col + 1);
                }
            }
        }
        return result;
    }
    */
}
