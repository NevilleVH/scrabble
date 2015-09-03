/**
 * Created by NevilleVH on 2015-08-19.
 */
public class Position { //could do with a better name
    private Board.Direction direction;
    private int row;
    private int col;
    private String word;
    private int points;

    public Position(Board.Direction direction, int row, int col, String word){
        this.row = row;
        this.col = col;
        this.direction = direction;
        this.word = word;

    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public Position(int row, int col, String word) {
        this.row = row;
        this.col = col;
        this.word = word;
    }

    public  String toString(){
        return String.format("%s\n%d\n%d\n%s\n",word,row+1,col+1, direction.name());

    }

    public int getPoints() {
        return points;
    }

    public Position(int row, int col, String word, int points){
        this.row = row;

        this.col = col;
        this.word = word;
        this.points = points;
    }

    public void setDirection(Board.Direction direction){
        this.direction = direction;
    }

    public Board.Direction getDirection() {
        return direction;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String getWord() {
        return word;
    }
}
