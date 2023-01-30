// Snake
// Details available at https://en.wikipedia.org/wiki/Snake_(video_game_genre)

import java.awt.EventQueue;
import javax.swing.JFrame;

public class Snake extends JFrame {

    public Snake() {  // Default constructor
        initUI();  // Call initialise UI function
    }

    private void initUI() {  // Initialise UI
        add(new Board());  // Create new board

        setResizable(false);  // JFrame cannot be resized
        pack();  // Size JFrame so that all contents are at their defined sizes

        setTitle("Snake");  // Set window title
        setLocationRelativeTo(null);  // No relative position of JFrame on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Exit application when close button pressed
    }

    public static void main(String[] args) {  // Main method for running game
        EventQueue.invokeLater(() -> {  // Post event at end of event queue
            JFrame ex = new Snake();  // Create a new JFrame calling Snake default constructor
            ex.setVisible(true);  // Set JFrame as visible
        });
    }
}