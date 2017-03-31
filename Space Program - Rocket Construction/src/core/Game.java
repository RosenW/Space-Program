package core;

import display.Window;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.IOException;

public class Game extends Canvas implements Runnable {
    public static final int SCALE = 3;
    public static final int WIDTH = 320 * SCALE;
    public static final int HEIGHT = WIDTH * 9 / 12;
    public static final String TITLE = "Space Program";

    public boolean running = false;
    private Thread thread;

    private Population population;

    public static int currentScore = 0;

    public static int TOPSCORE = 0;

    private Window window;

    public Game() throws IOException {
        population = new Population(20);
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
        this.window.setTitle("Generation: " + Population.GENERATION + " - Current Score: " + currentScore + " - Top score: " + Game.TOPSCORE);
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        population.render(g);

        g.dispose();
        bs.show();
    }
}
