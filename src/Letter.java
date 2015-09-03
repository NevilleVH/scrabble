import java.io.IOError;

/**
 * Created by NevilleVH on 2015-08-22.
 */
public class Letter extends Tile {
    public Letter(char letter){
        this.letter = letter;
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
                points =  1;
                break;
            case 'D' :
            case 'G' :
                points =  2;
                break;
            case 'B' :
            case 'C' :
            case 'M' :
            case 'P' :
                points =  3;
                break;
            case 'F' :
            case 'H' :
            case 'V' :
            case 'W' :
            case 'Y' :
                points =  4;
                break;
            case 'K' :
                points =  5;
                break;
            case 'J' :
            case 'X' :
                points =  8;
                break;
            case 'Q' :
            case 'Z' :
                points =  10;
                break;
            default:
                points =  -1;
        }
    }
    
}
