
import java.lang.System;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by NevilleVH on 2015-08-10.
 * Easy: https://github.com/first20hours/google-10000-english/blob/master/google-10000-english.txt
 * Medium: http://www.mieliestronk.com/corncob_caps.txt
 * above two only contain english spelling
 * Hard: sowpods
 */
public class Scrabble {
    public static void main(String[] args) {
        //javac -encoding UTF8 name.java
        /*
        System.setProperty("file.encoding","UTF-8");54
        try{
        Field charset = Charset.class.getDeclaredField("defaultCharset");
        charset.setAccessible(true);
        charset.set(null, null);} catch (IllegalAccessException e){}  catch (NoSuchFieldException e){}
*/

        //System.out.println("╔══╦══╗");
        /*
        System.out.println("┌───┬");
        System.out.println("│A  │");
        System.out.println("│  1│");
        System.out.println("├───┴");


        System.out.println("Enter number of players:");
        int numPlayers = scanner.nextInt();
        scanner.nextLine();
        */

        new GUI();/*
        Scanner scanner = new Scanner(System.in);
        boolean newGame;
        startMessage();
        System.out.println("Please enter your name:");
        String name = scanner.nextLine();
        do {
            try {
                System.out.println("How AI players do you wish to play against?");
                int AIs = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Select your desired difficulty level:\n1) Easy\n2) Intermediate\n3) Hard");
                int difficultyNum = scanner.nextInt();
                scanner.nextLine();
                AIPlayer.Difficulty difficulty = AIPlayer.Difficulty.values()[difficultyNum - 1];
                Game game = new Game(name, AIs, difficulty);
                game.Play();
                startMessage();
                System.out.println("New game? (Y/N)");
                newGame = scanner.nextLine().toUpperCase().charAt(0) == 'Y';
            } catch (Exception e){
                System.out.println("Invalid input!");
                newGame = false;
            }
        } while (newGame);
        */
    }

    public static void startMessage(){
        ArrayList<Tile> scrabbleMsg = new ArrayList<>();
        scrabbleMsg.add(new Letter('S'));
        scrabbleMsg.add(new Letter('C'));
        scrabbleMsg.add(new Letter('R'));
        scrabbleMsg.add(new Letter('A'));
        scrabbleMsg.add(new Letter('B'));
        scrabbleMsg.add(new Letter('B'));
        scrabbleMsg.add(new Letter('L'));
        scrabbleMsg.add(new Letter('E'));
        System.out.println(Tile.displayTiles(scrabbleMsg));
        System.out.println("Welcome to Scrabble!");
    }
}
