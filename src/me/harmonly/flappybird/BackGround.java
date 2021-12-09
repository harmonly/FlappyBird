package me.harmonly.flappybird;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class BackGround {
    private static final String[] backgrounds = {"background_day.png", "background_night.png"};

    public static BufferedImage getRandomBackground() throws IOException {
        return ImageIO.read(BackGround.class.getClassLoader().getResource(backgrounds[new Random().nextInt(2)]));
    }
}
