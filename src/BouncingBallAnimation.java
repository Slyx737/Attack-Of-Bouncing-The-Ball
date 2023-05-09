import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BouncingBallAnimation extends JPanel implements KeyListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int BALL_SIZE = 50;
    private static final int BALL_SPEED = 5;
    private int ballX;
    private int ballY;
    private int ballXdirection;
    private int ballYDirection;
    private boolean isAnimating;
    private static final int EASING_FACTOR = 2;

    public BouncingBallAnimation() {
        ballX = WIDTH / 2 - BALL_SIZE / 2;
        ballY = HEIGHT / 2 - BALL_SIZE / 2;
        ballXdirection = 1;
        ballYDirection = 1;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ballXdirection *= -1;
                ballYDirection *= -1;
            }
        });

        addKeyListener(this);
        setFocusable(true);

        // Start The Animation
        startAnimation();
    }

    private void startAnimation() {
        isAnimating = true;

        // Start The Game Loop In A Separate Thread
        new Thread(() -> {
            while (isAnimating) {
                moveBall();
                repaint(); // Redraw The Ball At Its New Position
                try {
                    Thread.sleep(10); // Pause The Loop For A Short Time To Create The Animation Effect
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void stopAnimation() {
        isAnimating = false;
    }

    private void moveBall() {
        ballX += BALL_SPEED * ballXdirection / EASING_FACTOR;
        ballY += BALL_SPEED * ballYDirection / EASING_FACTOR;

        // Handle Collisions With The Screen Edges
        if (ballX <= 0 || ballX >= WIDTH - BALL_SIZE) {
            ballXdirection *= -1;
        }
        if (ballY <= 0 || ballY >= HEIGHT - BALL_SIZE) {
            ballYDirection *= -1;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        g.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not Used
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (isAnimating) {
                stopAnimation();
            } else {
                startAnimation();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not Used
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Bouncing Ball Animation");
            BouncingBallAnimation animation = new BouncingBallAnimation();
            frame.add(animation);
            frame.setSize(WIDTH, HEIGHT);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
