import java.util.*;

/**
 * Created by NevilleVH on 2015-08-11.
 */
public class Game {
    private Board board = new Board();
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
        byte pos = positions.get(rnd.nextInt(positions.size()));
        players[pos] = new Player(name);
        positions.remove((Byte) pos);
        for (int i = 0; i < numAI; i++){
            pos = positions.get(rnd.nextInt(positions.size()));
            players[pos] = new AIPlayer("AI " + Integer.toString(pos+1),difficulty);
            positions.remove((Byte)pos);
        }
    }
    public void Play(){
        Scanner scanner = new Scanner(System.in);
        boolean gameOver = false;
        System.out.println(board);
        String turnReport = "";
        while (!gameOver){
            for (int i = 0; i < players.length && !gameOver; i++){
                players[i].drawTiles(letterBag.takeN(players[i].tilesToDraw()));
                if (players[i] instanceof AIPlayer){
                    WordPosition move = ((AIPlayer) players[i]).makeMove(board);

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

    private boolean playWord(int playerNum) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the word you wish to play:");
        try{
            String word = scanner.nextLine().toUpperCase();
            System.out.println("Enter the direction in which you wish to place your word (Right/Down):");
            Board.Direction direction = Board.Direction.valueOf(scanner.nextLine().toUpperCase());
            //System.out.println(board.possiblePositions(word, direction, players[i].getRack()));
            System.out.println("Enter the row in which you want to place your word:");
            int row = scanner.nextInt() - 1;
            scanner.nextLine();
            System.out.println("Enter the column in which you want to place your word:");
            int column = scanner.nextInt() - 1;
            scanner.nextLine();
            WordPosition wordPosition = new WordPosition(direction, row, column, word);
            String requiredLetters = board.requiredLetters(wordPosition);
            if (board.isValid(wordPosition,dictionary) && players[playerNum].rackContains(requiredLetters)) {
                ArrayList<Tile> letters = players[playerNum].playTiles(requiredLetters);
                int points = board.pointsFromWord(row, column, direction, letters);
                board.playWord(row, column, direction, letters);
                players[playerNum].updateScore(points);
                if (players[playerNum].rackEmpty())
                    System.out.println("Bonus!");
                return true;
            } else {
                System.out.println("Invalid move!");
                return false;
            }

        } catch (/*InputMismatch*/Exception e) {
            System.out.println("Invalid input!");
            return false;
        }

    }

}
