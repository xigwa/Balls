package org.example;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Barrier {
    private int x, y, width, height;
    private Color color;
    private int number;  // Случайное число на барьере
    private Random random = new Random();

    public Barrier(int x, int y, int width, int height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.number = random.nextInt(14) + 2; // Случайное число от 2 до 15
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawString(String.valueOf(number), x + width / 2 - 10, y + height / 2 + 5);
    }

    public void reduceNumber() {
        number--;
        if (number <= 0) {
            // Перемещаем барьер и присваиваем новое случайное число
            moveToRandomPosition();
            number = random.nextInt(14) + 2; // Случайное число от 2 до 15
        }
    }

    private void moveToRandomPosition() {
        // Генерация случайного положения для барьера
        // В этом примере мы просто изменяем координаты барьера на случайные
        // Вы можете изменить логику, чтобы учесть размеры игрового окна и другие барьеры
        x = random.nextInt(800 - width);
        y = random.nextInt(600 - height);
    }
}
