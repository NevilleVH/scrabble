import javax.rmi.CORBA.Tie;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by NevilleVH on 2015-08-12.
 */
public class Tile extends Cell{
    protected char letter;
    protected byte points;

    public char getLetter(){
        return letter;
    }

    public byte getPoints(){
        return points;
    }


    public String[] getConcatable(){
        String[] result = new String[4];
        //result[0] = result[3] = "───";
        //result[1] = " " + letter +" │";
        //result[2] = String.format("%3d│",points);
        result[0] = result[3] = "═══";
        result[1] = " " + letter +" ║";
        result[2] = String.format("%3d║", points);
        return result;
    }
       // @Override
    public boolean equals(Object other){
        if (other instanceof Tile && other != null)
            return letter == ((Tile) other).getLetter();
        return false;
    }

    public static String displayTiles(ArrayList<Tile> tiles) {
        String result = "";
        String[] temp = new String[4];
        temp[0] = "╔";
        temp[1] = temp[2] = "║";
        temp[3] = "╚";
        for (Tile tile : tiles){
            String[] concatable = tile.getConcatable();
            temp[0] += concatable[0] + "╦";
            temp[1] += concatable[1];
            temp[2] += concatable[2];
            temp[3] += concatable[3] + "╩";
        }
        temp[0] = temp[0].substring(0, temp[0].length() - 1) + "╗";
        temp[3] = temp[3].substring(0, temp[3].length() - 1) + "╝";
        for (String line : temp){
            result += line + '\n';
        }
        return result;
    }



}
