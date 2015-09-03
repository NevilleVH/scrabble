import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by NevilleVH on 2015-09-03.
 */
public class NewGameDialog extends JFrame implements ActionListener {
    private GUI parent;
    private JSpinner numAI;
    private ButtonGroup chooseDifficulty;
    public NewGameDialog(GUI parent){
        this.parent = parent;
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setTitle("New game");
        this.setSize(250, 150);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        JPanel contentPane = new JPanel();
        this.add(contentPane);
        contentPane.setLayout(new FlowLayout());//BoxLayout(contentPane, BoxLayout.PAGE_AXIS));

        SpinnerNumberModel model = new SpinnerNumberModel(1,1,3,1);
        JLabel numAIlabel = new JLabel("Number of opponents:");
        numAIlabel.setMaximumSize(numAIlabel.getPreferredSize());
        contentPane.add(numAIlabel);
        
        numAI = new JSpinner(model);/*{
            @Override
            public Dimension getMaximumSize() {
                Dimension dim = super.getMaximumSize();
                dim.height = getPreferredSize().height;
                return dim;
            }
        };*/
        numAI.setEditor(new JSpinner.DefaultEditor(numAI));
        numAI.setPreferredSize(new Dimension(100, 30));

        //numAI.setMaximumSize(numAI.getPreferredSize());

        contentPane.add(numAI);
        chooseDifficulty = new ButtonGroup();
        JRadioButton easy = new JRadioButton("Easy");
        JRadioButton intermediate = new JRadioButton("Intermediate");
        JRadioButton hard = new JRadioButton("Hard");
        easy.setActionCommand("EASY");
        intermediate.setActionCommand("INTERMEDIATE");
        hard.setActionCommand("HARD");
        chooseDifficulty.add(easy);
        chooseDifficulty.add(intermediate);
        chooseDifficulty.add(hard);
        contentPane.add(easy);
        contentPane.add(intermediate);
        contentPane.add(hard);
        easy.doClick();
        JButton confirm = new JButton("Confirm");
        contentPane.add(confirm);
        confirm.addActionListener(this);

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        parent.newGame((int) numAI.getValue(), AIPlayer.Difficulty.valueOf(chooseDifficulty.getSelection().getActionCommand()));

        this.dispose();
    }
}
