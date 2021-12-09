package me.harmonly.flappybird;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Medals {

    private static final String[] medals = {
            "medals_whitegold.png",
            "medals_bronze.png",
            "medals_silver.png",
            "medals_gold.png"
    };

    public static BufferedImage getMedal(int score) throws IOException {
        int index = -1;
        if (score >= 10) index++;
        if (score >= 30) index++;
        if (score >= 50) index++;
        if (score >= 100) index++;
        if (index == -1) return null;
        return ImageIO.read(Medals.class.getClassLoader().getResource(medals[index]));

    }
}
