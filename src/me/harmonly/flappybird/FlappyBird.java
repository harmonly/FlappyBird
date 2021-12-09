package me.harmonly.flappybird;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

//游戏界面
public class FlappyBird extends JPanel {

    private Executor executor = Executors.newSingleThreadExecutor();

    private BufferedImage background;
    private BufferedImage startImage;
    private BufferedImage game_overImage;
    private BufferedImage recordImage;
    private BufferedImage restartImage;

    private Ground ground;
    private Pillar[] pillars;
    private Bird bird;

    private int score;
    private int best;
    private State state;

    public FlappyBird() throws Exception {
        background = ImageIO.read(getClass().getClassLoader().getResource("background_day.png"));
        startImage = ImageIO.read(getClass().getClassLoader().getResource("other_start.png"));
        game_overImage = ImageIO.read(getClass().getClassLoader().getResource("other_gameover.png"));
        recordImage = ImageIO.read(getClass().getClassLoader().getResource("other_record.png"));
        restartImage = ImageIO.read(getClass().getClassLoader().getResource("restart.png"));

        ground = new Ground();
        pillars = new Pillar[2];
        setPillars();
        bird = new Bird();
        score = 0;
        best = score;
        state = State.START;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                switch (state) {
                    case START -> {
                        int x = e.getX(), y = e.getY();
                        if (x >= 40 && x <= 167
                                && y >= 376 && y <= 441) state = State.RUNNING;
                    }
                    case GAME_OVER -> {
                        int x = e.getX(), y = e.getY();
                        if (x >= 105 && x <= 264
                                && y >= 290 && y <= 325) {
                            try {
                                restart();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            state = State.START;
                        }
                    }
                    case RUNNING -> {
                        bird.flappy();
                        //播放弹跳音乐
                        executor.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    URL cb;
                                    File f = new File(getClass().getClassLoader().getResource("jump.wav").getPath());
                                    cb = f.toURL();
                                    AudioClip aau;
                                    aau = Applet.newAudioClip(cb);//加载音频
                                    aau.play(); //播放音频
                                } catch (MalformedURLException m) {
                                    m.printStackTrace();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    public void restart() throws IOException {
        pillars = new Pillar[2];
        setPillars();
        bird = new Bird();
        score = 0;
        background = BackGround.getRandomBackground();
    }

    public void setPillars() throws IOException {
        for (int i = 0; i < pillars.length; i++)
            pillars[i] = new Pillar(i);
    }

    @Override
    public void paint(Graphics g) {
        //绘制背景
        g.drawImage(background, 0, 0, null);

        if (state != State.START) {
            //绘制小鸟
            Graphics2D g2 = (Graphics2D) g;
            g2.rotate(-bird.getAlpha(), bird.getX() + bird.getWidth() / 2.0, bird.getY() + bird.getHeight() / 2.0);
            g.drawImage(bird.getImage(), bird.getX(), bird.getY(), null);
            g2.rotate(bird.getAlpha(), bird.getX() + bird.getWidth() / 2.0, bird.getY() + bird.getHeight() / 2.0);

            //绘制柱子
            for (Pillar pillar : pillars) {
                g.drawImage(pillar.getImage_down(), pillar.getX(), pillar.getY_down(), null);
                g.drawImage(pillar.getImage_up(), pillar.getX(), pillar.getY_up(), null);
            }

            if (state == State.RUNNING) {
                //绘制实时分数
                Font score_font = new Font(Font.SANS_SERIF, Font.BOLD, 40);
                g.setFont(score_font);
                g.drawString("" + score, 40, 60);
                g.setColor(Color.WHITE);
                g.drawString("" + score, 40 - 3, 60 - 3);
            } else if (state == State.GAME_OVER) {
                //game over
                g.drawImage(game_overImage, ((getWidth() - game_overImage.getWidth()) / 2), 30, null);
                //分数记录record
                g.drawImage(recordImage, (getWidth() - recordImage.getWidth()) / 2, 113, null);
                //分数score
                Font score_font = new Font(Font.SANS_SERIF, Font.BOLD, 20);
                g.setFont(score_font);
                g.drawString("" + score, (getWidth() - recordImage.getWidth()) / 2 + 215, 113 + 65);
                g.setColor(Color.WHITE);
                g.drawString("" + score, (getWidth() - recordImage.getWidth()) / 2 + 215 - 3, 113 + 65 - 3);

                //最佳分数best
                g.setColor(Color.BLACK);
                g.drawString("" + best, (getWidth() - recordImage.getWidth()) / 2 + 215, 113 + 125);
                g.setColor(Color.WHITE);
                g.drawString("" + best, (getWidth() - recordImage.getWidth()) / 2 + 215 - 3, 113 + 125 - 3);

                //奖牌medal
                BufferedImage medal = null;
                try {
                    medal = Medals.getMedal(score);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (medal != null)
                    g.drawImage(medal, (getWidth() - recordImage.getWidth()) / 2 + 50, 113 + 65, null);

                //重新开始restart
                g.drawImage(restartImage, (getWidth() - restartImage.getWidth()) / 2, 280, null);
            }
        } else {
            g.drawImage(startImage, 0, 0, null);
        }
        //绘制地面
        g.drawImage(ground.getImage(), ground.getX(), ground.getY(), null);
        g.drawImage(ground.getImage(), ground.getX() + ground.getWidth(), ground.getY(), null);
    }

    public void run() throws InterruptedException {
        while (true) {
            switch (state) {
                case START -> ground.step();
                case RUNNING -> {
                    ground.step();
                    for (Pillar pillar : pillars)
                        pillar.step();

                    bird.fly();
                    bird.step();
                    for (Pillar pillar : pillars) {
                        if (bird.getX() == pillar.getX())
                            score++;
                        if (bird.hitGround(ground) || bird.hitPillarDown(pillar) || bird.hitPillarUp(pillar)) {
                            state = State.GAME_OVER;
                            best = Math.max(best, score);
                        }
                    }

                }
                case GAME_OVER -> {
                    if (!bird.hitGround(ground))
                        bird.fall();
                }
            }
            repaint();
            Thread.sleep(1000 / 60);
        }
    }

    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("Flappy Bird");
        FlappyBird game = new FlappyBird();
        frame.add(game);
        frame.setSize(384, 448 + 64 + 37);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        game.run();
    }
}
