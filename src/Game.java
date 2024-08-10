
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;


public class Game extends JFrame implements MouseListener {
    private ArrayList<Ball> balls = new ArrayList<>();

    public Game() {
        setTitle("Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        addMouseListener(this);
        new GameThread().start();

    }


    @Override
    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.RED);
        wall();
        collizion();
        balls.forEach(ball -> ball.draw(g));

    }

    private void collizion() {
        for (int i = 0; i < balls.size(); i++) {
            for (int j = i+1; j < balls.size(); j++) {
                int a = Math.abs(balls.get(i).getX() - balls.get(j).getX());
                int b = Math.abs(balls.get(i).getY() - balls.get(j).getY());
                int c = (int) Math.sqrt(a*a + b*b);
                if (c <= balls.get(i).getRad()+balls.get(j).getRad()){
                    balls.get(i).setxStep(-balls.get(i).getxStep());
                    balls.get(i).setyStep(-balls.get(i).getyStep());
                    balls.get(j).setxStep(-balls.get(j).getxStep());
                    balls.get(j).setyStep(-balls.get(j).getyStep());
                }
            }
        }
    }

    private void wall() {
        int x = 0, y = 0, width = getWidth(), height = getHeight();
        balls.forEach(ball -> {
            if (ball.getX() < x || ball.getX() > width - 20 || ball.getY() < y || ball.getY() > height - 20) {
                ball.setxStep(-ball.getxStep());
                ball.setyStep(-ball.getyStep());
            }
        });
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        Random random = new Random();
        int xStep = random.nextInt(20) - 10;
        int yStep = random.nextInt(20) - 10;
        Color  color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        Ball ball = new Ball(x, y, xStep, yStep,color);
        balls.add(ball);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    class GameThread extends Thread {
        @Override
        public void run() {
            while (true) {
                repaint();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
