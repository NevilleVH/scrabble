import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.System;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
        Game game = new Game();
        System.out.println("┌───┬");
        System.out.println("│A  │");
        System.out.println("│  1│");
        System.out.println("├───┴");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter number of players:");
        int numPlayers = scanner.nextInt();
        scanner.nextLine();
        System.out.println("How many of these do you wish to be bots?");
        int AIs = scanner.nextInt();
        scanner.nextLine();
        game.setPlayers(numPlayers);
        game.Play();
    }
}
