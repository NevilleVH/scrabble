import javafx.scene.control.RadioButton;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * Created by NevilleVH on 2015-08-30.
 */

public class GUI extends JFrame implements ActionListener/*, KeyListener*/{
    private final int WIDTH = 1200;
    private final int HEIGHT = 1200;
    private final int BOARD_SIZE = 15;
    private CellGUI tileSelectedFromRack;
    private ArrayList<Position> newMove = new ArrayList<>();
    private DefaultTableModel scoreBoard;
    private JPanel board;
    private JPanel rack;
    private Game game;
    private JProgressBar availableLetters;
    public GUI() {
        //game = new Game("", 1, AIPlayer.Difficulty.INTERMEDIATE);
        this.setTitle("Scrabble");
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        JButton newGame = new JButton("New game");
        newGame.setMaximumSize(newGame.getPreferredSize());
        newGame.addActionListener(this);
        JPanel right = new JPanel();
        right.add(newGame);
        right.setLayout(new BoxLayout(right, BoxLayout.PAGE_AXIS));
        //this.add(right, BorderLayout.EAST);
        rack = new JPanel(new GridLayout(1,7));
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(rack, BorderLayout.CENTER);
        JButton exchange = new JButton("Exchange");
        exchange.addActionListener(this);
        bottom.add(exchange, BorderLayout.WEST);
        JButton endMove = new JButton("End turn");
        /*
        endMove.getInputMap().put(KeyStroke.getKeyStroke("ENTER"),"END_TURN");
        endMove.getActionMap().put("END_TURN", new Action() {
            @Override
            public Object getValue(String key) {
                return null;
            }

            @Override
            public void putValue(String key, Object value) {

            }

            @Override
            public void setEnabled(boolean b) {

            }

            @Override
            public boolean isEnabled() {
                return false;
            }

            @Override
            public void addPropertyChangeListener(PropertyChangeListener listener) {

            }

            @Override
            public void removePropertyChangeListener(PropertyChangeListener listener) {

            }

            @Override
            public void actionPerformed(ActionEvent e) {
                endTurn();
            }
        });*/
        endMove.addActionListener(this);
        bottom.add(endMove, BorderLayout.EAST);
        this.add(bottom, BorderLayout.SOUTH);
        board = new JPanel(new GridBagLayout());//GridLayout(15, 15)
        //board.setSize(700,400);
        //updateBoard();
        this.add(board, BorderLayout.CENTER);

        this.setVisible(true);

        NewGameDialog n = new NewGameDialog(this);


    }

    public void newGame(int numOpponents, AIPlayer.Difficulty difficulty){
        game = new Game("You", numOpponents, difficulty);

        availableLetters = new JProgressBar(0, game.getLetterBag().numAvailable());
        availableLetters.setOrientation(1);
        availableLetters.setStringPainted(true);
        //this.add(availableLetters, BorderLayout.SOUTH);
        this.add(availableLetters, BorderLayout.EAST);
        game.nextTurn();
        availableLetters.setValue(game.getLetterBag().numAvailable());
        scoreBoard = new DefaultTableModel(game.scoreData(),new String[]{"Name", "Score"});

        JTable table = new JTable(scoreBoard);
        JScrollPane tableContainer = new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        table.setEnabled(false);
        this.add(table, BorderLayout.WEST);
        updateBoard();
        //this.pack();
        updateRack();

    }

    private void updateCell(int row, int col){
        CellGUI cell = new CellGUI(game.getBoard().getCell(row,col), row, col);
        cell.addActionListener(this);
        cell.setMinimumSize(new Dimension(70, 70));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = col;
        constraints.gridy = row;
        constraints.fill = GridBagConstraints.BOTH;
        board.add(cell, constraints);
        board.revalidate();

    }

    private void updateBoard(){
        board.removeAll();
        for (int i = 0; i < BOARD_SIZE; i++){
            for (int j = 0; j < BOARD_SIZE; j++){
                CellGUI cell = new CellGUI(game.getBoard().getCell(i,j), i, j);
                cell.addActionListener(this);
                cell.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        super.mouseEntered(e);
                        if (tileSelectedFromRack != null)
                            cell.setBorder(BorderFactory.createBevelBorder(1, Color.green, Color.green));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        super.mouseExited(e);
                        if (tileSelectedFromRack != null)
                            cell.setBorder(BorderFactory.createRaisedBevelBorder());
                    }
                });
                //cell.setPreferredSize(new Dimension(70, 70));
                GridBagConstraints constraints = new GridBagConstraints();
                constraints.gridx = j;
                constraints.gridy = i;
                constraints.weightx = 1;
                constraints.weighty = 1;
                constraints.fill = GridBagConstraints.BOTH;
                //tile.setActionCommand(String.format("Board %d %d", i, j));
                board.add(cell, constraints);

            }
        }
        //this.pack();
        board.revalidate();
        board.repaint();
    }
    private void updateRack(){
        int i = 0;
        rack.removeAll();
        for (Tile t :game.getRack()) {

            CellGUI tile = new CellGUI(t,0,i);
            tile.addActionListener(this);
            //tile.setActionCommand("Rack " + Integer.toString(i));
            rack.add(tile);

            i++;
        }
        //this.pack();
        rack.revalidate();
        //board.revalidate();
    }

    private void placeTile(CellGUI cell){
        if (tileSelectedFromRack.getCell() instanceof Blank) {
            String letter = JOptionPane.showInputDialog(null, "Enter the letter you wish your blank to represent:");
            //if (letter.length() < 1 || !Character.isAlphabetic(letter.charAt(0))) seriously, get to this

            tileSelectedFromRack.setLetter(letter.substring(0, 1).toUpperCase());
        }
        newMove.add(new Position(cell.getRow(), cell.getCol(), tileSelectedFromRack.getLetter()));
        tileSelectedFromRack.setCol(cell.getCol());
        tileSelectedFromRack.setRow(cell.getRow());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = cell.getCol();
        constraints.gridy = cell.getRow();
        constraints.fill = GridBagConstraints.BOTH;
        board.add(tileSelectedFromRack, constraints);
        board.remove(cell);
        board.revalidate();
        board.repaint();
        tileSelectedFromRack = null;
    }

    private void removeFromBoard(CellGUI cell){ //removes selected tile from board if it has not been finalised yet
        for (int i = 0; i < newMove.size(); i++) {
            Position position = newMove.get(i);
            if (position.getRow() == cell.getRow() && position.getCol() == cell.getCol() && position.getWord().equals(cell.getLetter())) {
                rack.add(cell);
                newMove.remove(position);
                board.remove(cell);
                updateCell(cell.getRow(), cell.getCol());
                //rack.revalidate();
                break;
            }
        }
    }

    private void endTurn(){
        if (newMove.size() > 0 && !game.validateMove(newMove)) {
            JOptionPane.showMessageDialog(null, "Invalid move!");
            newMove.clear();
            updateBoard();
            updateRack();
        } else {
            if (newMove.size() > 0) {
                game.playWord(newMove);
                newMove.clear();

            }
            game.nextTurn();
            availableLetters.setValue(game.getLetterBag().numAvailable());
            updateBoard();
            updateRack();
            for (Object[] row : game.scoreData())
                scoreBoard.addRow(row);
            if (game.gameOver())
                JOptionPane.showMessageDialog(null, "Game over!");
        }
    }

    private void exchange(){
        //CAN STILL PLAY WORD AFTER EXCHANGING -- FIX!
        newMove.clear();
        updateBoard();
        updateRack();
        if (game.canExchange() && tileSelectedFromRack != null){
            game.exchangeTile(tileSelectedFromRack.getLetter().charAt(0));

            availableLetters.setValue(game.getLetterBag().numAvailable());
            updateRack();
        }
    }

    public void actionPerformed(ActionEvent e){
        if (e.getSource() instanceof CellGUI) {
            CellGUI cell = (CellGUI) e.getSource();
            if (!(cell.getCell() instanceof Tile) && cell.getParent() == board && tileSelectedFromRack != null) {
                if (tileSelectedFromRack != null) {
                    placeTile(cell);
                }
            } else if (cell.getCell() instanceof Tile && cell.getParent() == board ){
                removeFromBoard(cell);
            } else if (cell.getParent() == rack) {
                tileSelectedFromRack = cell;
            }
        } else if (e.getActionCommand().equals("End turn")){
            endTurn();
        } else if (e.getActionCommand().equals("Exchange")){
            exchange();
        }
    }
/*
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        endTurn();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
    */
}


