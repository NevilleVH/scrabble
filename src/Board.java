import java.util.*;

/**
 * Created by NevilleVH on 2015-08-11.
 */
public class Board { //SORT OUT PROBLEM WUTH BLANKS BEING SCORED AS NORMAL LETTERS!!!!!!!!!!!!!
    private byte size = 15;
    private Cell[][] board = new Cell[size][size];
    private Dictionary dictionary= new Dictionary();
    public /*final*/ enum Direction {DOWN, RIGHT}
    //public boolean transposed = false;

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
    public ArrayList<Tile> requiredLetters(int row, int col,Direction direction, String word){
        ArrayList<Tile> result = new ArrayList<>();
        if (direction == Direction.DOWN){
            for (int i = 0; i < word.length(); i++){
                if (!isTile(board[row + i][col])){
                    result.add(new Tile(word.charAt(i)));
                }
            }
        } else {
            for (int i = 0; i < word.length(); i++){
                if (!isTile(board[row][col + i])){
                    result.add(new Tile(word.charAt(i)));
                }
            }
        }
        return result;
    }
    private boolean isTile(Cell cell){
        return cell.getClass().getName().equals("Tile");//instance of
    }
    private Character getLetter(Cell cell){
        if (cell.getClass().getName().equals("Tile")){
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
    public boolean isValid(int row, int col,Direction direction, String word){
        if (direction == Direction.RIGHT){
            //easier to do the following (though less efficient) than to recode everything for this direction
            transposeBoard();
            int temp = row;
            row = col;
            col = temp;
        }
        boolean result = isValidHelper(row, col, direction, word);

        if (direction == Direction.RIGHT)
            transposeBoard();

        return result;
    }
    public boolean isValidHelper(int row, int col,Direction direction, String word){
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

    private void transposeBoard(){
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
            try {isTile(board[row+i][col]);}
            catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("e");
            }
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

    public void makeOptimalMove(Player player){

        ArrayList<Tile> letters = player.getRack();
        Object[] move1 = optimalWordPrime(letters);
        transposeBoard();
        Object[] move2 = optimalWordPrime(letters);
        transposeBoard();
        int score1 = (Integer) move1[3];
        int score2 = (Integer) move2[3];
        if (!(score1 == -1 && score2 == -1)) {
            player.updateScore(Math.max(score1, score2));
            int row, col;
            if (score1 >= score2){//(Math.random() > 0.5) { //score1 >= score2
                row = (Integer) move1[1];
                col = (Integer) move1[2];
                letters = requiredLetters(row, col, Direction.DOWN, (String) move1[0]);
                playWord(row, col, Direction.DOWN, letters);
            } else {
                row = (Integer) move2[2];
                col = (Integer) move2[1];
                letters = requiredLetters(row, col, Direction.RIGHT, (String) move2[0]);
            playWord(row, col, Direction.RIGHT, letters);
            }
            player.removeLetters(letters);
        }

    }

    private Object[] shittyOptimalWord(ArrayList<Tile> letters, final Direction direction){
        //to do: if board is empty (centre cell is empty) just use highest scoring combination
        //MAKE TREE OF DICTIONARY W/ METRIC SCORE?

        if (direction == Direction.RIGHT){
            transposeBoard();
        }

        int  max = 0;
        int maxRow = -1;
        int maxCol = -1;
        String maxWord = "";

        if (isEmpty()){
            String s = "";
            for (Tile t : letters)
                s += t.getLetter();
            for (int numLetters = 1; numLetters <= letters.size(); numLetters++){
                HashSet<String> words = allPossibleWordsOfLength(letters,numLetters);
                int pts;
                for (String word : words) {

                    for (int r = 1; r <= numLetters; r++)
                        if ((pts = pointsFromWord(size / 2 - r + 1, size / 2, Direction.DOWN, requiredLetters(size / 2 - r + 1, size / 2, Direction.DOWN,word))) > max) {
                            max = pts;
                            maxCol = size/2;
                            maxRow = size / 2 - r + 1;
                            maxWord = word;
                            System.out.printf("%s %d %d %s\n", maxWord, maxRow, maxCol, direction.toString());
                        }
                }
            }

        } else {

            for (int col = 0; col < size; col++) {
                for (int row = 0; row < size; row++) {
                    if (!isTile(board[row][col])) {
                        int start;
                        if (row - 1 >= 0 && isTile(board[row - 1][col])) {
                            for (start = row - 2; start >= 0; start--) {
                            }
                            start++;
                        } else
                            start = row;
                        //ALSO CHECK ABOVE i.e. tempRow -= 1
                        for (int usingFromRack = 1; usingFromRack <= letters.size(); usingFromRack++) { // number of letters to use
                            HashSet<String> possibilities = new HashSet<>();
                            String s = "";
                            for (Tile t : letters) {
                                s += t.getLetter() != '_' ? t.getLetter() : ""; //IMPLEMENT BLANK USAGE
                            }
                            if ((col - 1 >= 0 && isTile(board[row][col - 1])) || (col + 1 < size && isTile(board[row][col + 1]))) {

                                HashSet<String> words = allPossibleWordsOfLength(letters, usingFromRack);

                                for (String word : words) {
                                    if (isValid(row, col, direction, word)) {
                                        ArrayList<Tile> tempTiles = new ArrayList<>();
                                        for (char c : word.toCharArray())
                                            tempTiles.add(new Tile(c));
                                        int pts;
                                        if ((pts = pointsFromWord(row, col, direction, tempTiles)) > max) {
                                            max = pts;
                                            maxCol = col;
                                            maxRow = row;
                                            maxWord = word;
                                            System.out.printf("%s %d %d %s\n", maxWord, maxRow, maxCol, direction.toString());
                                        }
                                    }
                                }

                            }
                            int count = 0;
                            possibilities.clear();
                            int tempRow = start;
                            while (tempRow < size && count <= usingFromRack) { //should go up until the last existing letter after the final letter to use has been counted
                                if (!isTile(board[tempRow][col])) {
                                    count++;
                                } else {
                                    HashSet<String> temp = dictionary.possibleWords(((Tile) board[tempRow][col]).getLetter(), tempRow - start);

                                    if (possibilities.size() == 0)
                                        possibilities.addAll(temp);
                                    else
                                        possibilities.retainAll(temp);
                                }
                                tempRow++;
                            }
                            for (String word : possibilities) {
                                if (isValid(start, col, direction, word)) {
                                    //System.out.printf("%d %d %s",start,col, word);
                                    ArrayList<Tile> req = requiredLetters(start, col, direction, word);
                                    int numRequiredLetters = req.size();
                                    for (Tile t : letters) {
                                        if (req.contains(t))
                                            numRequiredLetters -= 1;
                                        //req.remove(t);
                                    }
                                    if (numRequiredLetters == 0 && isValid(start, col, direction, word)) {
                                        int points;
                                        if ((points = pointsFromWord(start, col, direction, req)) > max) {
                                            max = points;
                                            maxCol = col;
                                            maxRow = start;
                                            maxWord = word;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (direction == Direction.RIGHT){
            transposeBoard();
            int temp = maxRow;
            maxRow = maxCol;
            maxCol = temp;
        }
        Object[] result = new Object[4]; // yes this is temporary
        result[0] = maxWord;
        result[1] = maxRow;
        result[2] = maxCol;
        result[3] = max;
        return result;
    }

    private boolean anyAdjacentTilesInRow(int row, int col){
        return (col - 1 >= 0 && isTile(board[row][col - 1])) || (col + 1 < size && isTile(board[row][col + 1]));
    }

    private String prefixAlongRow(int row, int col){
        String result = "";
        for (int i = col - 1; i > -1 && isTile(board[row][i]); i--){
            result = ((Tile) board[row][i]).getLetter() + result;
        }
        return  result;
    }

    private String suffixAlongRow(int row, int col){
        String result = "";
        for (int i = col + 1; i < size && isTile(board[row][i]); i++){
            result += ((Tile) board[row][i]).getLetter();
        }
        return  result;
    }

    private String columnPrefix(int row, int col){
        String result = "";
        while (row != 0 && isTile(board[row-1][col])) {
            row -= 1;
            result = ((Tile) board[row][col]).getLetter() + result;
        }
        return result;
    }


    private Object[] optimalWord(ArrayList<Tile> tiles){
        //TO DO: HOOKS, STARTING
        int maxPts = 0;
        String maxWord = "";
        int maxCol = -1;
        int maxRow = -1;
        String letters = "";
        for (Tile tile : tiles){
            letters += tile.getLetter() == '_' ? "" : tile.getLetter();
        }
        for (int col = 0; col < size; col++){
            for (int row = 0; row < size; row++){
                if (!isTile(board[row][col])){

                    int start = closestTileAbove(row, col);
                    if (start == -1)
                        start = row;
                    int end = closestTileBelow(row, col);

                    String prefix = prefixAlongRow(row, col);
                    String suffix = suffixAlongRow(row, col);

                    boolean isBetweenWords = !(prefix.equals("") && suffix.equals(""));
                    boolean isTileAbove = (start == row - 1) && (start != -1);
                    boolean isTileBelow = (end < size) && (end - row <= tiles.size());
                    boolean canMakeWord = isBetweenWords || isTileAbove || isTileBelow;

                    if (!canMakeWord)
                        continue;

                    HashSet<String> possible = dictionary.possibleWords(letters, size - row);
                    for (int i = start; i != -1 && i < row; i++){
                        char c = ((Tile) board[start][col]).getLetter();
                        possible.retainAll(dictionary.possibleWords(c, i - start));
                    }

                    if (isBetweenWords)
                        for (char c : letters.toCharArray()) {
                            if (!dictionary.wordExists(prefix + c + suffix))
                                possible.removeAll(dictionary.possibleWords(c, row - start));
                        }

                    do {
                        if (end < size) {
                            char c = ((Tile) board[end][col]).getLetter();
                            possible.retainAll(dictionary.possibleWords(c, end));
                        }

                        for (String word : possible){
                            if (isValid(start, col, Direction.DOWN, word)) {
                                ArrayList<Tile> required = requiredLetters(start, col, Direction.DOWN, word);
                                if (isContained(tiles, required)){
                                    //System.out.printf("%s %d %d\n", word, row, col);
                                    int pts = pointsFromWord(start, col, Direction.DOWN, required);
                                    if (pts > maxPts){
                                        maxPts = pts;
                                        maxWord = word;
                                        maxCol = col;
                                        maxRow = start;//row;
                                    }
                                }
                            }
                        }

                        end = closestTileBelow(end, col);
                    } while (end < size);
                }
            }
        }
        Object[] result = new Object[4]; // yes this is temporary
        result[0] = maxWord;
        result[1] = maxRow;
        result[2] = maxCol;
        result[3] = maxPts;
        //System.out.println(maxWord);
        return result;
    }

    private Object[] optimalWordPrime(ArrayList<Tile> tiles){
        //TO DO: HOOKS, STARTING
        int maxPts = 0;
        String maxWord = "";
        int maxCol = -1;
        int maxRow = -1;
        String letters = "";
        for (Tile tile : tiles){
            letters += tile.getLetter() == '_' ? "" : tile.getLetter();
        }
        for (int col = 0; col < size; col++){
            for (int row = 0; row < size; row++){
                if (!isTile(board[row][col])){

                    int start = closestTileAbove(row, col);
                    if (start == -1)
                        start = row;
                    int end = closestTileBelow(row, col);
                    String rowPrefix = prefixAlongRow(row, col);
                    String rowSuffix = suffixAlongRow(row, col);
                    if (!isEmpty()) {
                        boolean isBetweenWords = !(rowPrefix.equals("") && rowSuffix.equals(""));
                        boolean isTileAbove = (start == row - 1) && (start != -1);
                        boolean isTileBelow = (end < size) && (end - row <= tiles.size());
                        boolean canMakeWord = isBetweenWords || isTileAbove || isTileBelow;

                        if (!canMakeWord)
                            continue;
                    } else if (row != size/2 && col != size/2)
                        continue;

                    String colPrefix = columnPrefix(row, col);


                    String tempLetters = letters;
                    ArrayList<String> possible;
/*
                    if (letters.contains("_")) {

                        possible = new ArrayList<>();
                        tempLetters = letters.replace("_", "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
                        for (String s : combinations("",tempLetters, 7))
                            possible.addAll(getPossibleWords(colPrefix, s, row, col));
                            FACTORIALS ARE SCARY THINGS

                    } else*/
                    possible = getPossibleWords(colPrefix, tempLetters, row, col);

                    for (String word : possible){
                        if (isValid(start, col, Direction.DOWN, word)) {
                            ArrayList<Tile> required = requiredLetters(start, col, Direction.DOWN, word);
                            if (isContained(tiles, required)){
                                int pts = pointsFromWord(start, col, Direction.DOWN, required);
                                if (pts > maxPts){
                                    maxPts = pts;
                                    maxWord = word;
                                    maxCol = col;
                                    maxRow = start;//row;
                                }
                            }
                        }
                    }

                }
            }
        }
        Object[] result = new Object[4]; // yes this is temporary
        result[0] = maxWord;
        result[1] = maxRow;
        result[2] = maxCol;
        result[3] = maxPts;
        //System.out.println(maxWord);
        return result;
    }



    private int numLettersBelow(int row, int col){
        int count;
        for (count = 0; (row = closestTileBelow(row, col)) != size; count++){}

        return count;
    }

    private ArrayList<String> getPossibleWords(String prefix, String letters, int row, int col){
        ArrayList<String> result = new ArrayList<>();
        if (row == size || col == size)
            return result;

        if (prefix.equals("")) {
            for (char c : letters.toCharArray()) {
                result.addAll(getPossibleWords(prefix + c, letters.substring(1), row + 1, col));
            }
            return result;
        }


        if (dictionary.possibleCharacters(prefix).contains(' ')){
            result.add(prefix);
            return result;
        }
        if (!isTile(board[row][col])) {
            for (char c : letters.toCharArray()) {
                if (dictionary.possibleCharacters(prefix).contains(c)) {
                    result.addAll(getPossibleWords(prefix + c, letters.substring(1), row + 1, col));
                }

            }
        } else {
            char c = ((Tile) board[row][col]).getLetter();
            if (dictionary.possibleCharacters(prefix).contains(c)) {
                result.addAll(getPossibleWords(prefix + c, letters, row + 1, col));
            }
        }
        return result;
    }

    private boolean isContained(ArrayList<Tile> container, ArrayList<Tile> contained){
        ArrayList<Tile> temp = new ArrayList<>();
        temp.addAll(container);
        for (Tile tile : contained)
            if (temp.contains(tile)){
                temp.remove(tile); //java's containsAll method doesn't do this i.e. check that duplicates exist if required
            } else
                return false;
        return true;
    }

    private int closestTileAbove(int row, int col){
        int i;
        for (i = row - 1; i > -1 && !isTile(board[i][col]); i--){}
        return i;
    }
    private int closestTileBelow(int row, int col){
        int i;
        for (i = row + 1; i < size && !isTile(board[i][col]); i++){}
        return i;
    }

    private int furthestTileBelow(int row, int col){
        int i;
        for (i = size - 1; i > row && !isTile(board[i][col]); i--){}
        if (i == row)
            return -1;
        else
            return i;
    }


    private HashSet<String> allPossibleWordsOfLength(ArrayList<Tile> letters, int k) {

        HashSet<String> possibilities = new HashSet<>();
        if (k == 1){
            for (Tile t : letters) {
                possibilities.add(Character.toString(t.getLetter()));
             }
            return possibilities;
        }
        String s = "";
        for (Tile t : letters) {
            s += t.getLetter(); //!= '_' ? t.getLetter() : ""; //IMPLEMENT BLANK USAGE - will first have to make anagrams
        }

        possibilities = permutations("", s, k);
        possibilities.retainAll(dictionary.getDictionary());
        //HashSet<String> words = new HashSet<>();
        //for (String word : possibilities) {
        //    words.addAll(dictionary.anagrams(word));
        //}
        return possibilities;
    }


    private static HashSet<String> combinations (String prefix, String s, int k){
        HashSet<String> result = new HashSet<>();
        if (k == 0) {
            result.add(prefix);
            return result;
        }
        for (int i = 0; i < s.length(); i ++){
            result.addAll(combinations(prefix + s.charAt(i), s.substring(i + 1), k - 1));
        }
        return result;
    }
    private static HashSet<String> permutations (String prefix, String s, int length){
        HashSet<String> result = new HashSet<>();
        if (length == prefix.length()) {
            result.add(prefix);
            return result;
        }
        for (int i = 0; i < s.length(); i ++){
            result.addAll(permutations(prefix + s.charAt(i), s.replace("" + s.charAt(i), ""), length));
        }
        return result;
    }

    public String possiblePositions(String word, Direction direction, ArrayList<Tile> letters){
        String result = "";
        if (!isEmpty()) {
            for (int row = 0; row < size; row++) {
                for (int col = 0; col < size; col++) {
                    if (isValid(row, col, direction, word) && isContained(letters, requiredLetters(row, col, direction, word)))
                        result += String.format("Suggested word location:\nRow:\t%d\nColumn:\t%d\n", row + 1, col + 1);
                }
            }
        }
        return result;
    }
}
