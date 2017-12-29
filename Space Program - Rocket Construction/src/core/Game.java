package core;

import display.Window;
import models.ShipModel;
import models.TestSubject;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.util.Random;

public class Game extends Canvas implements Runnable {
    private static final int SCALE = 3;
    public static final int WIDTH = 320 * SCALE;
    public static final int HEIGHT = WIDTH * 9 / 12;
    private static final String TITLE = "Space Program";
    private static Random RANDOM = new Random();

    private boolean running = false;
    private Thread thread;

    private Population population;

    static ShipModel BEST_SHIP_MODEL = new ShipModel();

    static TestSubject BEST_SHIP = new TestSubject(new int[5][7]);

    public static int CURRENT_SCORE = 0;

    static int TOP_SCORE = 0;

    public static float TRANSPARENCY = 0;

    private Window window;


    public Game() throws IOException {
        population = new Population(40);
        window = new Window(WIDTH, HEIGHT, TITLE, this);
    }


    @Override
    public void run() {
        while (running) {
            long lastTime = System.nanoTime();
            double amountOfTicks = 20.0;
            double ns = 1000000000 / amountOfTicks;
            double delta = 0;
            long timer = System.currentTimeMillis();
            int frames = 0;
            while (running) {

                long now = System.nanoTime();
                delta += (now - lastTime) / ns;
                lastTime = now;

                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                while (delta >= 1) {
                    tick();
                    delta--;
                }
                if (running) {
                    render();
                }
                frames++;
                if (System.currentTimeMillis() - timer > 1000) {
                    timer += 1000;
                    frames = 0;
                }
            }
            stop();
        }
    }

    public synchronized void start() {
        if (running) {
            return;
        }
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    private synchronized void stop() {
        if (!running) {
            return;
        }
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(1);
    }

    private void tick() {
        this.population.tick();
        this.window.setTitle("Generation: " + Population.GENERATION);

    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);

        float distanceModifier = this.getPopulation().getHighestDistance() / 1500f;

        drawSky(g, distanceModifier);

        drawHeightTrackers(g);

        population.render(g);

        drawBestShip(g);

        g.dispose();
        bs.show();
    }

    private void drawBestShip(Graphics g) {
        if (Game.TRANSPARENCY > 0.5) {
            g.setColor(Color.BLACK);
        } else {
            g.setColor(Color.WHITE);
        }

        g.setFont(new Font("Arial", 0, 25));
        g.drawString("Best Ship: ", 50, 100);
        BEST_SHIP_MODEL.render(g);
    }

    private void drawHeightTrackers(Graphics g) {
        if (Game.TRANSPARENCY > 0.5) {
            g.setColor(Color.BLACK);
        } else {
            g.setColor(Color.WHITE);
        }

        for (int i = 0; i < 5; i++) {
            g.fillRect(0, ((Population.Y_OFFSET - (Game.HEIGHT / 5 * i)) % Game.HEIGHT) * -1, 50, 5);
        }
    }

    private void drawSky(Graphics g, float distanceModifier) {
        if (1f + distanceModifier < 0) {
            Game.TRANSPARENCY = 0;
        } else {
            Game.TRANSPARENCY = 1f + distanceModifier;
        }

        g.setColor(new Color(0.07450981f, 0.8392157f, 1f, Game.TRANSPARENCY));
        g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT / 4);
        g.setColor(new Color(0.19607843f, 0.92156863f, 1f, Game.TRANSPARENCY));
        g.fillRect(0, Game.HEIGHT / 4, Game.WIDTH, Game.HEIGHT / 4);
        g.setColor(new Color(0.28235295f, 0.9607843f, 1f, Game.TRANSPARENCY));
        g.fillRect(0, Game.HEIGHT / 2, Game.WIDTH, Game.HEIGHT / 4);
        g.setColor(new Color(0.39607846f, 0.9490196f, 1f, Game.TRANSPARENCY));
        g.fillRect(0, Game.HEIGHT / 4 * 3, Game.WIDTH, Game.HEIGHT / 4);

        //stars
        if(Game.TRANSPARENCY == 0){
            for (int i = 0; i < 25; i++) {
                g.setColor(Color.WHITE);
                g.fillRect(Game.RANDOM.nextInt(Game.WIDTH), Game.RANDOM.nextInt(Game.HEIGHT), 3,3);
            }
        }

    }

    public static int getSCALE() {
        return SCALE;
    }

    public static int getWIDTH() {
        return WIDTH;
    }

    public static int getHEIGHT() {
        return HEIGHT;
    }

    public static String getTITLE() {
        return TITLE;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public Population getPopulation() {
        return population;
    }

    public void setPopulation(Population population) {
        this.population = population;
    }

    public static ShipModel getBestShipModel() {
        return BEST_SHIP_MODEL;
    }

    public static void setBestShipModel(ShipModel bestShipModel) {
        BEST_SHIP_MODEL = bestShipModel;
    }

    public static TestSubject getBestShip() {
        return BEST_SHIP;
    }

    public static void setBestShip(TestSubject bestShip) {
        BEST_SHIP = bestShip;
    }

    public static int getTopScore() {
        return TOP_SCORE;
    }

    public static void setTopScore(int topScore) {
        TOP_SCORE = topScore;
    }

    public Window getWindow() {
        return window;
    }

    public void setWindow(Window window) {
        this.window = window;
    }
}


