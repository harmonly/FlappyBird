package me.harmonly.flappybird;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class Pillar {
    private BufferedImage image_down, image_up;
    private int x, y_down, y_up;
    private int width, height;
    private int distance;

    private int gap;
    private Random rand = new Random();

    public Pillar(int n) throws IOException {
        image_down = ImageIO.read(getClass().getClassLoader().getResource("pillar_downside.png"));
        image_up = ImageIO.read(getClass().getClassLoader().getResource("pillar_upside.png"));
        width = image_down.getWidth();
        height = image_down.getHeight();
        gap = 144;
        distance = 245;

        x = 500 + (n - 1) * distance;
        y_down = 448 - 200;
        y_up = -150;
    }

    //柱子向左移动
    public void step() {
        if (--x == -width) {
            x = distance * 2 - width / 2;
            y_up = rand.nextInt(151) - 200;
            y_down = y_up + rand.nextInt(150) + 100 + 250;
        }
    }

    public BufferedImage getImage_down() {
        return image_down;
    }

    public BufferedImage getImage_up() {
        return image_up;
    }

    public int getX() {
        return x;
    }

    public int getY_down() {
        return y_down;
    }

    public int getY_up() {
        return y_up;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
