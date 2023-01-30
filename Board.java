// Snake
// Details available at https://en.wikipedia.org/wiki/Snake_(video_game_genre)

import java.awt.*;
import java.awt.event.*;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.util.ArrayDeque;
import java.util.Queue;

public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 900;  // Board width
    private final int B_HEIGHT = 600;  // Board height
    private final int DOT_SIZE = 30;  // Dot size
    private final int ALL_DOTS = (B_WIDTH/DOT_SIZE)*(B_HEIGHT/DOT_SIZE);  // Total dot count (max snake length)
    private final int RAND_POS_X = (B_WIDTH/DOT_SIZE)-1;  // Max value for random x value on board
    private final int RAND_POS_Y = (B_HEIGHT/DOT_SIZE)-1;  // Max value for random y value on board
    private final int DELAY = 140;  // Timer delay (milliseconds)

    private final int[] x = new int[ALL_DOTS];  // Array of x coordinates of each dot in snake's body
    private final int[] y = new int[ALL_DOTS];  // Array of y coordinates of each dot in snake's body

    private int dots;  // Snake length
    private int apple_x;  // X coordinate of current apple
    private int apple_y;  // Y coordinate of current apple

    private enum Movement {UP, DOWN, LEFT, RIGHT}  // Snake's movement options

    private Movement currentDirection;  // Current direction of snake's movement

    private Queue<Movement> movementQueue = new ArrayDeque<>();  // Movement queue

    private boolean inGame = true;  // Game active flag

    private Timer timer;  // Game timer

    private Image dotImg;  // Dot image
    private Image appleImg;  // Apple image
    private Image headImg;  // Head image

    public Board() {  // Default constructor
        initBoard();  // Call initialise board function
        movementQueue.offer(Movement.RIGHT);  // Add right as snake's initial direction
    }

    private void initBoard() {  // Initialise board
        addKeyListener(new TAdapter());  // Keystroke listener
        setBackground(Color.black);  // Set board background to black
        setFocusable(true);  // Activate board focus
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));  // Set board dimensions

        loadImages();  // Call load images function
        initGame();  // Call initialise game function
    }

    private void loadImages() {  // Load images
        ImageIcon imgDotIcon = new ImageIcon("src/resources/dot.png");  // Paints icon from dot.png image
        dotImg = imgDotIcon.getImage();  // Store image object from imgDotIcon
        dotImg = dotImg.getScaledInstance(DOT_SIZE, DOT_SIZE, Image.SCALE_DEFAULT);  // Resize dot image to dot size

        ImageIcon imgAppleIcon = new ImageIcon("src/resources/apple.png");  // Paints icon from apple.png image
        appleImg = imgAppleIcon.getImage();  // Store image object from imgAppleIcon
        appleImg = appleImg.getScaledInstance(DOT_SIZE, DOT_SIZE, Image.SCALE_DEFAULT);  // Resize apple image to dot size

        ImageIcon imgHeadIcon = new ImageIcon("src/resources/head.png");  // Paints icon from head.png image
        headImg = imgHeadIcon.getImage();  // Store image object from imgHeadIcon
        headImg = headImg.getScaledInstance(DOT_SIZE, DOT_SIZE, Image.SCALE_DEFAULT);  // Resize head image to dot size
    }

    private void initGame() {  // Initialise game
        dots = 3;  // Set initial snake length to 3

        for (int i = 0; i < dots; i++) {  // For each dot in snake's body
            x[i] = DOT_SIZE*6;  // Dots trail to the left of the 6th x coordinate
            y[i] = DOT_SIZE*3;  // Dots begin at 3rd y dot coordinate
        }

        createApple();  // Call create apple function

        timer = new Timer(DELAY, this);  // Create a timer which updates every DELAY milliseconds
        timer.start();  // Start timer
    }

    @Override
    public void paintComponent(Graphics g) {  // Paint components to JPanel
        super.paintComponent(g);  // Call parent object of the paintComponent method
        drawGraphics(g);  // Call draw graphics function
    }

    private void drawGraphics(Graphics g) {  // Draw graphics
        if (inGame) {  // If game is active
            g.drawImage(appleImg, apple_x, apple_y, this);  // Draw apple image at apple_x, apple_y

            for (int i = 0; i < dots; i++) {  // For each dot in snake's body
                if (i == 0) {  // If snake's head
                    g.drawImage(headImg, x[i], y[i], this);  // Draw head image
                } else {  // If snake's body
                    g.drawImage(dotImg, x[i], y[i], this);  // Draw dot image
                }
            }

            Toolkit.getDefaultToolkit().sync();  // Synchronise toolkit graphic state
        } else {  // If game is not active
            gameOver(g);  // Call game over function
        }
    }

    private void gameOver(Graphics g) {  // Game over
        String gameOverMsg = "GAME OVER";  // Game over message
        Font gameOverFont = new Font("Helvetica", Font.BOLD, 30);  // Define desired font for game over message
        FontMetrics metric = getFontMetrics(gameOverFont);  // Store info about the rendering of gameOverFont
        g.setColor(Color.white);  // Set font colour
        g.setFont(gameOverFont);  // Set font
        g.drawString(gameOverMsg, (B_WIDTH - metric.stringWidth(gameOverMsg))/2, B_HEIGHT/2 - 50);  // Draw game over message

        int score = dots-3;  // Number of apples eaten
        String scoreMsg = "score: "+score;  // Score message
        Font scoreFont = new Font("Helvetica", Font.PLAIN, 16);  // Define desired font for scoreMsg message
        metric = getFontMetrics(scoreFont);  // Store info about the rendering of font "scoreFont"
        g.setColor(Color.white);  // Set font colour
        g.setFont(scoreFont);  // Set font
        g.drawString(scoreMsg, (B_WIDTH - metric.stringWidth(scoreMsg))/2, B_HEIGHT/2 - 20);  // Draw score message
    }

    private void checkApple() {  // Check if snake head and apple collide
        if ((x[0] == apple_x) && (y[0] == apple_y)) {  // If snake's head is at same position as apple
            dots++;  // Increment snake's length
            createApple();  // Call create apple function
        }
    }

    private void move() {  // Move snake
        for(int i = dots; i > 0; i--){  // For each dot in snake's body, starting from the tail
            x[i] = x[i-1];  // Move dot to x coordinate of next dot
            y[i] = y[i-1];  // Move dot to y coordinate of next dot
        }

        Movement movement = movementQueue.poll();  // Store and remove the head of the movement queue
        currentDirection = movement;  // Store current direction

        switch(movement){  // Handle movement
            case UP:  // If desired movement is up
                y[0] -= DOT_SIZE;  // Move snake's head up by one dot
                break;  // Exit switch case
            case DOWN:  // If desired movement is down
                y[0] += DOT_SIZE;  // Move snake's head down by one dot
                break;  // Exit switch case
            case LEFT:  // If desired movement is left
                x[0] -= DOT_SIZE;  // Move snake's head left by one dot
                break;  // Exit switch case
            case RIGHT:  // If desired movement is right
                x[0] += DOT_SIZE;  // Move snake's head right by one dot
                break;  // Exit switch case
            default:  //  If no desired movement
                break;  // Exit switch case
        }
    }

    private void checkCollision() {  // Check if snake's head collides with border or body
        for (int i = dots; i > 0; i--) {  // For each dot in snake's body, starting from the tai
            if ((i > 3) && (x[0] == x[i]) && (y[0] == y[i])) {  // if snake's head collides with body
                inGame = false;  // End game
                break;  // Exit loop
            }
        }

        if (y[0] >= B_HEIGHT) {  // If snake's head collides with board's lower boarder
            inGame = false;  // End game
        }

        if (y[0] < 0) {  // If snake's head collides with board's upper boarder
            inGame = false;  // End game
        }

        if (x[0] >= B_WIDTH) {  // If snake's head collides with board's right boarder
            inGame = false;  // End game
        }

        if (x[0] < 0) {  // If snake's head collides with board's left boarder
            inGame = false;  // End game
        }

        if (!inGame) {  // If game ends
            timer.stop();  // Stop timer
        }
    }

    private void createApple() {  // Generate random coordinates for a new apple
        int r = (int) (Math.random()*RAND_POS_X);  // Random number for apple's x coordinate
        apple_x = r*DOT_SIZE;  // Store new apple's x coordinate

        r = (int) (Math.random()*RAND_POS_Y);  // Random number for apple's y coordinate
        apple_y = r*DOT_SIZE;  // Store new apple's y coordinate
    }

    @Override
    public void actionPerformed(ActionEvent e) {  // Handle user actions
        if (inGame) {  // If game active
            checkApple();  // Call check apple function
            checkCollision();  // Call check collision function
            if(movementQueue.isEmpty()){  // If movement queue is empty
                movementQueue.offer(currentDirection);  // Add current direction to queue
            }
            move();  // call move function
        }

        repaint();  // Clear screen and redraw graphics
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {  // Handle keyboard interaction

            int key = e.getKeyCode();  // Store entered key

            if((key == KeyEvent.VK_UP) && (currentDirection != Movement.DOWN)){  // If up key pressed and snake is not moving down
                movementQueue.offer(Movement.UP);  // Add up movement to queue tail
            }

            if((key == KeyEvent.VK_DOWN) && (currentDirection != Movement.UP)){  // If down key pressed and snake is not moving up
                movementQueue.offer(Movement.DOWN);  // Add down movement to queue tail
            }

            if((key == KeyEvent.VK_LEFT) && (currentDirection != Movement.RIGHT)){  // If left key pressed and snake is not moving right
                movementQueue.offer(Movement.LEFT);  // Add left movement to queue tail
            }

            if((key == KeyEvent.VK_RIGHT) && (currentDirection != Movement.LEFT)){  // If right key pressed and snake is not moving left
                movementQueue.offer(Movement.RIGHT);  // Add right movement to queue tail
            }
        }
    }
}