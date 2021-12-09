package me.harmonly.flappybird;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Ground {
    private BufferedImage image;
    private int x, y;
    private int width, height;

    public Ground() throws IOException {
        image = ImageIO.read(getClass().getClassLoader().getResource("background_ground.png"));
        width = image.getWidth();
        height = image.getHeight();
        x = 0;
        y = 448;
    }

    //向左移动
    public void step() {
        if (--x == -width)
            x = 0;
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
