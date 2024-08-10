package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Game extends JFrame implements MouseListener {
    private ArrayList<Ball> balls = new ArrayList<>();
    private ArrayList<Barrier> barriers = new ArrayList<>();
    private BufferedImage image;
    private int score = 0;
    private JLabel scoreLabel;
    private int imageX = 400, imageY = 300;
    private final Random random = new Random();

    public Game() {
        setTitle("Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);  // Используем абсолютное позиционирование для JLabel

        // Добавление JLabel для отображения счета
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setForeground(Color.BLACK);
        scoreLabel.setBounds(getWidth() - 150, 10, 150, 30);  // Положение JLabel
        add(scoreLabel); // Добавляем JLabel в JFrame

        // Создание барьеров
        barriers.add(new Barrier(100, 100, 100, 20, Color.BLUE));
        barriers.add(new Barrier(300, 200, 20, 100, Color.GREEN));
        barriers.add(new Barrier(500, 400, 150, 20, Color.YELLOW));

        setVisible(true);
        addMouseListener(this);

        // Загрузка изображения
        try {
            image = ImageIO.read(new File("src/main/resources/Image/klipartz.com.png")); // путь к загруженному файлу
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Запускаем игровой поток
        new GameThread().start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g); // Вызываем супер метод для базового рендеринга

        // Создаем контекст буферного изображения, чтобы избежать мерцания
        Image bufferImage = createImage(getWidth(), getHeight());
        Graphics bufferGraphics = bufferImage.getGraphics();

        // Рисуем на буфере, чтобы избежать мерцания
        bufferGraphics.setColor(Color.WHITE);
        bufferGraphics.fillRect(0, 0, getWidth(), getHeight());

        // Рисуем изображение на текущих координатах (imageX, imageY)
        if (image != null) {
            bufferGraphics.drawImage(image, imageX, imageY, null);
        }

        bufferGraphics.setColor(Color.RED);
        wall();
        collisionWithImage();
        collisionWithBarriers();

        // Рисуем барьеры
        barriers.forEach(barrier -> barrier.draw(bufferGraphics));

        // Рисуем мячики
        balls.forEach(ball -> ball.draw(bufferGraphics));

        // Рисуем все на основном графическом контексте
        g.drawImage(bufferImage, 0, 0, this);

        // Обновляем JLabel счетчика после полной отрисовки
        scoreLabel.repaint();
    }

    private void collisionWithImage() {
        ArrayList<Ball> ballsToRemove = new ArrayList<>();
        for (Ball ball : balls) {
            // Проверка на попадание мячика в изображение
            if (image != null && ball.getX() >= imageX && ball.getX() <= imageX + image.getWidth() &&
                    ball.getY() >= imageY && ball.getY() <= imageY + image.getHeight()) {
                ballsToRemove.add(ball); // Мячик удаляется
                score++; // Увеличиваем счетчик
                scoreLabel.setText("Score: " + score); // Обновляем JLabel

                // Перемещение изображения на случайную позицию в пределах окна
                moveImageToRandomPosition();
            }
        }
        balls.removeAll(ballsToRemove); // Удаляем мячики после завершения цикла
    }

    private void collisionWithBarriers() {
        for (Ball ball : balls) {
            Rectangle ballBounds = new Rectangle(ball.getX(), ball.getY(), ball.getRad(), ball.getRad());
            for (Barrier barrier : barriers) {
                if (ballBounds.intersects(barrier.getBounds())) {
                    // Изменяем направление движения мяча при столкновении
                    if (ball.getX() + ball.getRad() > barrier.getBounds().getX() &&
                            ball.getX() < barrier.getBounds().getX() + barrier.getBounds().getWidth()) {
                        ball.setxStep(-ball.getxStep());
                    }
                    if (ball.getY() + ball.getRad() > barrier.getBounds().getY() &&
                            ball.getY() < barrier.getBounds().getY() + barrier.getBounds().getHeight()) {
                        ball.setyStep(-ball.getyStep());
                    }
                    barrier.reduceNumber(); // Уменьшаем число на барьере
                }
            }
        }
    }

    private void moveImageToRandomPosition() {
        final int maxAttempts = 100; // Максимальное количество попыток для поиска свободного места
        boolean validPositionFound = false;
        int attempts = 0;

        while (!validPositionFound && attempts < maxAttempts) {
            // Генерируем случайные координаты для изображения
            int newX = random.nextInt(getWidth() - image.getWidth());
            int newY = random.nextInt(getHeight() - image.getHeight());

            // Проверяем, пересекается ли новое положение с любым из барьеров
            Rectangle newImageBounds = new Rectangle(newX, newY, image.getWidth(), image.getHeight());
            boolean intersects = false;

            for (Barrier barrier : barriers) {
                if (newImageBounds.intersects(barrier.getBounds())) {
                    intersects = true;
                    break;
                }
            }

            if (!intersects) {
                imageX = newX;
                imageY = newY;
                validPositionFound = true;
            }

            attempts++;
        }

        if (!validPositionFound) {
            System.out.println("Не удалось найти свободное место для изображения после " + maxAttempts + " попыток.");
        }
    }

    private void wall() {
        int x = 0, y = 0, width = getWidth(), height = getHeight();
        balls.forEach(ball -> {
            if (ball.getX() < x || ball.getX() > width - ball.getRad() ||
                    ball.getY() < y || ball.getY() > height - ball.getRad()) {
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
        Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        Ball ball = new Ball(x, y, xStep, yStep, color);
        balls.add(ball);
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    class GameThread extends Thread {
        @Override
        public void run() {
            while (true) {
                repaint(); // Перерисовываем окно
                try {
                    Thread.sleep(100); // Задержка для уменьшения нагрузки на процессор
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
