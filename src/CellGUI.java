import javax.swing.*;
import java.awt.*;

/**
 * Created by NevilleVH on 2015-09-03.
 */
class CellGUI extends JButton {
    private final int HEIGHT = 100;
    private final int WIDTH = 75;
    private int row;
    private int col;
    private Cell cell;

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public Cell getCell() {
        return cell;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public CellGUI(Cell cell, int row, int col){
        this.col = col;
        this.row = row;
        this.cell = cell;

        this.setSize(WIDTH, HEIGHT);
        this.setLayout(new GridBagLayout());
        if (cell instanceof Tile) {
            this.setBackground(Color.orange);
            JLabel letter = new JLabel(((Tile) cell).getLetter() + "");
            JLabel points = new JLabel(Byte.toString(((Tile) cell).getPoints()));
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 1;
            constraints.gridy = 1;
            this.add(letter, constraints);
            constraints.gridx = 2;
            constraints.gridy = 2;
            this.add(points, constraints);
        } else  if (cell instanceof Multiplier){
            Multiplier multiplier = (Multiplier) cell;
            JLabel type = new JLabel(multiplier.getMultiplierType().name() + "");
            JLabel factor = new JLabel(Integer.toString(multiplier.getMultiplierValue()));
            type.setForeground(Color.white);
            factor.setForeground(Color.white);
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 1;
            constraints.gridy = 1;
            this.add(type, constraints);
            constraints.gridx = 1;
            constraints.gridy = 2;
            this.add(factor, constraints);
            if (multiplier.getMultiplierType() == Multiplier.MultiplierType.LTR) {
                if (multiplier.getMultiplierValue() == 2) {
                    this.setBackground(Color.cyan);
                    type.setForeground(Color.black);
                    factor.setForeground(Color.black);
                }
                else
                    this.setBackground(Color.blue);
            } else {

                if (multiplier.getMultiplierValue() == 2)
                    this.setBackground(Color.magenta);
                else
                    this.setBackground(Color.red);
            }

        }
        this.setBorder(BorderFactory.createRaisedBevelBorder());

        this.setVisible(true);
    }
    public String getLetter(){
        return ((JLabel)this.getComponent(0)).getText();
    }

    public void setLetter(String letter){
        ((JLabel)this.getComponent(0)).setText(letter);
    }


}