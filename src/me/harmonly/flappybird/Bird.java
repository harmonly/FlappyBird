package me.harmonly.flappybird;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Bird {
    private BufferedImage image;
    private int x, y;
    private int width, height;
    private int size;
    private double gravity;
    private double t;
    private final double origin_speed;
    private double speed;
    private double s;
    private double alpha;

    private BufferedImage[] images;
    private int index;

    public Bird() throws IOException {
        image = ImageIO.read(getClass().getClassLoader().getResource("bird_middle.png"));
        width = image.getWidth();
        height = image.getHeight();
        x = 132;
        y = 280;
        size = 40;

        gravity = 4;
        origin_speed = 20;
        t = 0.25;
        speed = origin_speed;
        s = 0;
        alpha = 0;

        images = new BufferedImage[]{
                ImageIO.read(getClass().getClassLoader().getResource("bird_up.png")),
                ImageIO.read(getClass().getClassLoader().getResource("bird_middle.png")),
                ImageIO.read(getClass().getClassLoader().getResource("bird_down.png"))
        };
        index = 0;
    }

    public void fly() {
        index++;
        image = images[(index / 12) % 3];
    }

    public void step() {
        double v0 = speed;
        s = v0 * t + gravity * t * t / 2;  // 计算上抛运动位移
        y = y - (int) s;  // 计算鸟的坐标位置
        speed = v0 - gravity * t;  // 计算下次移动速度
        if (y < 0) {
            y = 0;
            speed = 0;
        }
        alpha = Math.atan(s / 8);  // 计算倾角（反正切函数）
    }

    public void fall() {
        double v0 = 0;
        s = v0 * t + gravity * t * t / 2;  // 计算上抛运动位移
        y = y - (int) s;  // 计算鸟的坐标位置
        alpha = -Math.PI / 2;
    }

    public void flappy() {
        speed = origin_speed;
    }

    public boolean hitGround(Ground ground) {
        boolean hit = y + size > ground.getY();
        if (hit) {
            y = ground.getY() - size;
            alpha = -Math.PI / 2;
        }
        return hit;
    }

    public boolean hitPillarDown(Pillar pillar) {
        int tw = this.width;
        int th = this.height;
        int rw = pillar.getWidth();
        int rh = pillar.getHeight();
        if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) {
            return false;
        }
        int tx = this.x;
        int ty = this.y;
        int rx = pillar.getX();
        int ry = pillar.getY_down();
        rw += rx;
        rh += ry;
        tw += tx;
        th += ty;
        //      overflow || intersect
        return ((rw < rx || rw > tx) &&
                (rh < ry || rh > ty) &&
                (tw < tx || tw > rx) &&
                (th < ty || th > ry));
    }

    public boolean hitPillarUp(Pillar pillar) {
        int tw = this.width;
        int th = this.height;
        int rw = pillar.getWidth();
        int rh = pillar.getHeight();
        if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) {
            return false;
        }
        int tx = this.x;
        int ty = this.y;
        int rx = pillar.getX();
        int ry = pillar.getY_up();
        rw += rx;
        rh += ry;
        tw += tx;
        th += ty;
        //      overflow || intersect
        return ((rw < rx || rw > tx) &&
                (rh < ry || rh > ty) &&
                (tw < tx || tw > rx) &&
                (th < ty || th > ry));
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

    public int getSize() {
        return size;
    }

    public double getGravity() {
        return gravity;
    }

    public double getT() {
        return t;
    }

    public double getOrigin_speed() {
        return origin_speed;
    }

    public double getSpeed() {
        return speed;
    }

    public double getS() {
        return s;
    }

    public double getAlpha() {
        return alpha;
    }

    public BufferedImage[] getImages() {
        return images;
    }

    public int getIndex() {
        return index;
    }
}
