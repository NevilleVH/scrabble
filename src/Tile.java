import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by NevilleVH on 2015-08-12.
 */
public class Tile extends Cell{
    private char letter;
    private byte points; //this doesn't necessarily have to be stored
    private boolean isBlank; //perhaps wasteful to store this information for every tile; but this simplifies matters elsewhere

    public Tile(char pLetter){
        letter = pLetter;
        points = letterPoints(letter);
    }
    public Tile(Tile tile){
        letter = tile.getLetter();
        points = tile.getPoints();
    }

    public boolean isBlank() {
        return isBlank;
    }

    private byte letterPoints(char letter){
        switch (letter){
            case 'E' :
            case 'A' :
            case 'I' :
            case 'O' :
            case 'N' :
            case 'R' :
            case 'T' :
            case 'L' :
            case 'S' :
            case 'U' :
                return 1;
            case 'D' :
            case 'G' :
                return 2;
            case 'B' :
            case 'C' :
            case 'M' :
            case 'P' :
                return 3;
            case 'F' :
            case 'H' :
            case 'V' :
            case 'W' :
            case 'Y' :
                return 4;
            case 'K' :
                return 5;
            case 'J' :
            case 'X' :
                return 8;
            case 'Q' :
            case 'Z' :
                return 10;
            case '_' :
                return 0;
            default:
                return -1;
        }
    }
    public char getLetter(){
        return letter;
    }

    public byte getPoints() {
        return points;
    }

    public void setLetter(char pLetter) {
        if (letter == '_')
            letter = pLetter;
    }

    public String[] getConcatable(){
        String[] result = new String[4];
        //result[0] = result[3] = "───";
        //result[1] = " " + letter +" │";
        //result[2] = String.format("%3d│",points);
        result[0] = result[3] = "═══";
        result[1] = " " + letter +" ║";
        result[2] = String.format("%3d║",points);
        return result;
    }
       // @Override
    public boolean equals(Object other){
        if (other instanceof Tile && other != null)
            return letter == ((Tile) other).getLetter();
        return false;
    }




}
