import java.util.ArrayList;

/**
 * Created by NevilleVH on 2015-08-11.
 */
public class Cell {
    public String[] getConcatable(){
        String[] result = new String[4];
        result[0] = result[3] = "───";
        result[1] = result[2] = "   │";
        return result;
    }

    public static String toString(Cell[][] cells){ //clean this up
        String result = "";
        String[] temp = new String[4];
        temp[0] = "┌";
        temp[1] = temp[2] = "│";
        temp[3] = "├";
        int count = 0;
        for (Cell cell : cells[0]){
            result += String.format(" %2s ", ++count);
            String[] concatable = cell.getConcatable();
            temp[0] += concatable[0] + "┬";
            temp[1] += concatable[1];
            temp[2] += concatable[2];
            temp[3] += concatable[3] + "┼";
        }
        count = 0;
        result += '\n';
        temp[0] = temp[0].substring(0, temp[0].length() - 1) + "┐";
        temp[1] += Integer.toString(++count);
        temp[3] = temp[3].substring(0, temp[3].length() - 1) + "┤";
        for (int i = 0; i < 4 ;i++){
            result += temp[i] + '\n';
            temp[i] = "";
        }

        for (int i = 1; i < cells.length - 1;i++){
            temp[1] = temp[2] = "│";
            temp[3] = "├";
            for (Cell cell : cells[i]){
                String[] concatable = cell.getConcatable();
                //temp[0] += concatable[0] + "┬";
                temp[1] += concatable[1];
                temp[2] += concatable[2];
                temp[3] += concatable[3] + "┼";
            }
            //temp[0] = temp[0].substring(0, temp[0].length() - 1) + "┐";
            temp[1] += Integer.toString(++count);
            temp[3] = temp[3].substring(0, temp[3].length() - 1) + "┤";
            for (int j = 1; j < 4; j++){
                result += temp[j] + '\n';
                temp[j] = "";
            }
        }
        temp[1] = temp[2] = "│";
        temp[3] = "└";
        for (Cell cell : cells[cells.length-1]){
            String[] concatable = cell.getConcatable();
            //temp[0] += concatable[0] + "┬";
            temp[1] += concatable[1];
            temp[2] += concatable[2];
            temp[3] += concatable[3] + "┴";
        }
        //temp[0] = temp[0].substring(0, temp[0].length() - 1) + "┐";
        temp[1] += Integer.toString(++count);
        temp[3] = temp[3].substring(0, temp[3].length() - 1) + "┘";
        for (int j = 1; j < 4; j++){
            result += temp[j] + '\n';
            temp[j] = "";
        }
        return result;
    }



}
