import java.util.*;

/**
 * Created by NevilleVH on 2015-08-11.
 */
public class Game {
    private Board board = new Board();
    private Byte playerIndex;
    private Player[] players;
    private LetterBag letterBag = new LetterBag();
    private Dictionary dictionary = new Dictionary("HARD.txt");

    public Game(String name, int numAI, AIPlayer.Difficulty difficulty){
        ArrayList<Byte> positions = new ArrayList<>(numAI + 1);
        for (byte i = 0; i < numAI + 1; i++){
            positions.add(i);
        }
        players = new Player[numAI + 1];
        Random rnd = new Random();
        byte pos = playerIndex = positions.get(rnd.nextInt(positions.size()));
        players[pos] =  new Player(name);
        positions.remove((Byte) pos);
        for (int i = 0; i < numAI; i++){
            pos = positions.get(rnd.nextInt(positions.size()));
            players[pos] = new AIPlayer("AI " + Integer.toString(pos+1),difficulty);
            positions.remove((Byte)pos);
        }
    }

    public Board getBoard() {
        return board;
    }

    public HashMap<Tile, Integer> unseenLetters(Player toWhom){ //i.e. to whom are they unseen?
        HashMap<Tile, Integer> result = new HashMap<>();
        for (Player player : players){
            if (player != toWhom){
                for (Tile tile : player.getRack())
                    result.put(tile, result.get(tile) == null ? 1 : result.get(tile) + 1);
            }
        }
        for (Tile tile : letterBag.getLetters()){
            result.put(tile, result.get(tile) == null ? 1 : result.get(tile) + 1);
        }
        return result;
    }

    public ArrayList<Tile> probableDraws(Player player){
        ArrayList<Tile> result = new ArrayList<>();
        HashMap<Tile, Integer> unseen = unseenLetters(player);

        result.addAll(unseen.keySet());
        Collections.sort(result, Comparator.comparing(tile -> -1*unseen.get(tile)));
        return result;
    }

    public LetterBag getLetterBag(){
        return letterBag;
    }

    public Object[][] scoreData(){
        Object[][] result = new Object[players.length][2];
        for (int i = 0; i < players.length; i++){
            result[i][0] = players[i].getName();
            result[i][1] = players[i].getScore();
        }
        return result;
    }

    public void nextTurn(){
        if (!gameOver()) {
            for (int i = 0; i < players.length; i++) {
                players[i].drawTiles(letterBag.takeN(players[i].tilesToDraw()));
                if (players[i] instanceof AIPlayer) {
                    ((AIPlayer) players[i]).makeMove(this); //if null has passed
                }

            }
        }
    }
/*
    public void Play(){
        Scanner scanner = new Scanner(System.in);
        boolean gameOver = false;
        System.out.println(board);
        String turnReport = "";

        while (!gameOver){
            for (int i = 0; i < players.length && !gameOver; i++){
                players[i].drawTiles(letterBag.takeN(players[i].tilesToDraw()));
                if (players[i] instanceof AIPlayer){
                    //System.out.println(players[i].viewTiles());
                    Position move = ((AIPlayer) players[i]).makeMove(board);

                    if (move != null)
                        turnReport +=String.format("%s played %s at position (%d,%d); scoring %d points.\n",players[i].getName(),move.getWord(),move.getCol()+1,move.getRow()+1,move.getPoints());
                    else
                        turnReport += players[i].getName() + " declined to make a move.\n";
                    continue;
                }
                System.out.println(board);
                System.out.println(turnReport);
                turnReport = "";
                System.out.printf("%s, you have the following letters:\n%sCurrent score: %d\n\n", players[i].getName(), players[i].viewTiles(), players[i].getScore());
                boolean turnFinished = false;
                do {
                    System.out.println("Select an option:\n1) Play a word\n2) Exchange letters\n3) Pass\n4) Suggest end of game\n");
                    int option = scanner.nextInt();

                    scanner.nextLine();

                    switch (option) {
                        case 1:
                            turnFinished = playWord(i);
                            break;
                        case 2:
                            turnFinished = exchangeTiles(i);
                            break;
                        case 3:
                            turnFinished = true;
                            break;
                        case 4:
                            gameOver = turnFinished = suggestEnd(i);
                            break;

                    }
                } while (!turnFinished);
                players[i].incrementTurns();
                if (players[i].rackEmpty() && letterBag.numAvailable() == 0)
                    gameOver = true;
            }

        }

        displayResult();

    }
*/
    public boolean validateMove(ArrayList<Position> positions){
        return board.isValid(positions,dictionary);
    }

    public boolean gameOver(){
        if (letterBag.numAvailable() == 0){
            for (Player player: players)
                if (player.rackEmpty())
                    return true;
        }
        return false;
    }

    public ArrayList<Tile> getRack(){
        return players[playerIndex].getRack();
    }

    private void displayResult(){
        Arrays.sort(players, Comparator.comparing(Player::getFinalScore));
        System.out.println("Game Over!\nThe results are as follows:\nName\t\tScore");
        for (int i = players.length - 1; i >= 0; i--)
            System.out.println(players[i]);
    }

    private boolean exchangeTiles(int playerNum){
        Scanner scanner = new Scanner(System.in);
        if (letterBag.numAvailable() >= 7) {
            System.out.println("Enter the number of letters you wish to exchange:");
            int numLetters = scanner.nextInt();
            scanner.nextLine();
            if (numLetters > 0 && numLetters < 8) {
                System.out.println("Now enter each letter to be exchanged on a new line.");
                for (int j = 0; j < numLetters; j++) {
                    Character letter = scanner.nextLine().toUpperCase().charAt(0);
                    players[playerNum].removeLetter(letter);

                }

                players[playerNum].drawTiles(letterBag.takeN(players[playerNum].tilesToDraw()));
            } //else System.out.println();
            return true;
        } else
            System.out.println("There need to be at least 7 letters in the bag to make an exchange!");
        return false;
    }
    public boolean canExchange(){
        return letterBag.numAvailable() >= 7;
    }

    public void exchangeTile(char letter){
        players[playerIndex].removeLetter(letter);
    }
/*
    private boolean suggestEnd(int playerNum){
        boolean allAgree = true;
        int j;
        for (j = 0; j < players.length && allAgree; j++){
            if (j != playerNum){
                allAgree = ((AIPlayer) players[j]).anyPossibleMoves(board);
                //System.out.printf("Player %d, Player %d has suggested that the game end. Do you agree? (Y/N)\n",j,i);
                //allAgree = (scanner.nextLine().toUpperCase().charAt(0) == 'Y');
            }
        }
        if (allAgree){
            System.out.println("All players have agreed that the game end.");
            return true;
        } else {
            System.out.printf("%s wishes the game to continue.\n",players[j-1].getName());
            return false;
        }
    }
*/
    public void playWord(ArrayList<Position> positions/*int playerNum*/) {

        String requiredLetters = "";
        for (Position position : positions)
            requiredLetters += position.getWord();
        int row = positions.get(0).getRow();
        int column = positions.get(0).getCol();
        Board.Direction direction = board.inferDirection(positions);
        ArrayList<Tile> letters = players[playerIndex].playTiles(requiredLetters);
        int points = board.pointsFromWord(row, column, direction, letters);
        board.playWord(row, column, direction, letters);
        players[playerIndex].updateScore(points);
        //if (players[playerNum].rackEmpty())
        //    System.out.println("Bonus!");

    }

}
