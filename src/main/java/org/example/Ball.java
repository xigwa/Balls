package org.example;

import java.awt.*;

public class Ball {
    private int x, y, xStep, yStep;
    private int rad = 20;
    private Color color;

    public Ball(int x, int y, int xStep, int yStep, Color color) {
        this.x = x;
        this.y = y;
        this.xStep = xStep;
        this.yStep = yStep;
        this.color = color;
        new Move().start();
    }

    public int getRad() {
        return rad;
    }

    public void setRad(int rad) {
        this.rad = rad;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x, y, rad, rad);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getxStep() {
        return xStep;
    }

    public void setxStep(int xStep) {
        this.xStep = xStep;
    }

    public int getyStep() {
        return yStep;
    }

    public void setyStep(int yStep) {
        this.yStep = yStep;
    }

    public class Move extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    x += xStep;
                    y += yStep;
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
