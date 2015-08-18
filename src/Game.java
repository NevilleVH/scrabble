import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by NevilleVH on 2015-08-11.
 */
public class Game {
    private Board board = new Board();
    private Player[] players;
    //private Dictionary dictionary = new Dictionary();
    private LetterBag letterBag = new LetterBag();

    public void setPlayers(int numPlayers){
        players = new Player[numPlayers];
        for (int i = 0; i < numPlayers; i++){
            players[i] = new Player("");
        }
    }
    public void Play(){
        Scanner scanner = new Scanner(System.in);
        boolean gameOver = false;
        while (!gameOver){
            for (int i = 0; i < players.length && !gameOver; i++){
                System.out.println(board);
                players[i].drawLetters(letterBag.takeN(players[i].lettersToDraw()));
                //maybe put the below as the player tostring
                System.out.printf("Player %d, you have the following letters:\n%s\nCurrent score: %d\n", i, players[i].viewTiles(), players[i].getScore());
                if (true) {
                    board.makeOptimalMove(players[i]);
                    scanner.nextLine();
                    continue;
                }
                boolean turnFinished = false;
                do {
                    System.out.println("Select an option:\n1) Play a word\n2) Exchange letters\n3) Pass\n4) Suggest end of game\n");
                    int option = scanner.nextInt();
                    scanner.nextLine();

                    switch (option) {
                        case 1:
                            System.out.println("Enter the word you wish to play:");
                            String word = scanner.nextLine().toUpperCase();
                            System.out.println("Enter the direction in which you want to place your word (Right/Down):");
                            Board.Direction direction = Board.Direction.valueOf(scanner.nextLine().toUpperCase());
                            System.out.println(board.possiblePositions(word,direction, players[i].getRack()));
                            System.out.println("Enter the row in which you want to place your word:");
                            int row = scanner.nextInt()-1;
                            scanner.nextLine();
                            System.out.println("Enter the column in which you want to place your word:");
                            int column = scanner.nextInt()-1;
                            scanner.nextLine();

                            ArrayList<Tile> letters = players[i].getTiles(board.requiredLetters(row, column, direction, word));

                            if (board.isValid(row, column, direction, word) && letters != null){
                                int points = board.pointsFromWord(row, column, direction, letters);
                                board.playWord(row,column,direction,letters);
                                players[i].updateScore(points);
                                turnFinished = true;
                                if (players[i].rackEmpty())
                                    System.out.println("Bonus!");

                            } else {
                                System.out.println("Invalid move!");

                            }
                            //}

                            break;
                        case 2:
                            if (letterBag.numAvailable() >= 7) {
                                System.out.println("Enter the number of letters you wish to exchange:");
                                int numLetters = scanner.nextInt();
                                scanner.nextLine();
                                if (numLetters > 0 && numLetters < 8) {
                                    System.out.println("Now enter each letter to be exchanged on a new line.");
                                    for (int j = 0; j < numLetters; j++) {
                                        Character letter = scanner.nextLine().toUpperCase().charAt(0);
                                        players[i].removeLetter(letter);

                                    }

                                    players[i].drawLetters(letterBag.takeN(players[i].lettersToDraw()));
                                } //else System.out.println();
                                turnFinished = true;
                            } else
                                System.out.println("There need to be at least 7 letters in the bag to make an exchange!");
                            break;
                        case 3:
                            turnFinished = true;
                            break;
                        case 4:
                            boolean allAgree = true;
                            int j;
                            for (j = 0; j < players.length && allAgree; j++){
                                if (j != i){
                                    System.out.printf("Player %d, Player %d has suggested that the game end. Do you agree? (Y/N)\n",j,i);
                                    allAgree = (scanner.nextLine().toUpperCase().charAt(0) == 'Y');
                                }
                            }
                            if (allAgree){
                                System.out.println("All players have agreed that the game end.");
                                gameOver = turnFinished = true;
                            } else {
                                System.out.printf("Player %d wishes the game to continue.\n",j);
                            }
                            break;

                    }
                } while (!turnFinished);
                if (players[i].rackEmpty() && letterBag.numAvailable() == 0)
                    gameOver = true;
            }

        }

        Arrays.sort(players, (Player p1, Player p2) -> Integer.compare(p1.getFinalScore(), p2.getFinalScore()));

    }


}
