package models;

import com.sun.org.apache.bcel.internal.generic.POP;
import core.Game;
import core.Population;

import java.awt.*;
import java.util.Random;

public class TestSubject {
    private int x;
    private int y;
    private int[][] DNA;
    private int fitness;
    private static Random RAND = new Random();
    private Color color;
    private int fuel;
    private int speedUp;
    private int speedDown;
    private int speedLeft;
    private int speedRight;
    private int weight;
    private int distance;
    private int realityMultiplier;
    private int xVelocity;
    private int yVelocity;

    public TestSubject(int[][] DNA) {
        this.DNA = DNA;
        color = new Color(RAND.nextInt(100) + 50, RAND.nextInt(100) + 50, RAND.nextInt(100) + 50);
        this.x = getRAND().nextInt(Game.WIDTH);
        this.y = Game.HEIGHT - 100;
        this.fitness = 0;
        this.fuel = 0;
        this.weight = 0;
        this.realityMultiplier = 8;

        DNA[2][4] = 9;

        constructRocket(DNA);

        fuelRocket(DNA);

        calculateSpeed(DNA);

        calculateWeight(DNA);

        calculateVelocity();

    }

    private void calculateVelocity() {
        xVelocity = speedRight - speedLeft;
        yVelocity = speedDown - speedUp + weight;
    }

    private void calculateWeight(int[][] DNA) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                if (DNA[i][j] == 9 || DNA[i][j] == 4) {
                    weight++;
                }
            }
        }
    }

    private void calculateSpeed(int[][] DNA) {
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 7; y++) {
                if (DNA[x][y] == 2) {
                    if (y > 0) {
                        if (DNA[x][y - 1] == 4) {
                            speedUp += 5;
                            continue;
                        }
                    }
                    if (x < 4) {
                        if (DNA[x + 1][y] == 4) {
                            speedRight += 3;
                            continue;
                        }
                    }
                    if (x > 0) {
                        if (DNA[x - 1][y] == 4) {
                            speedLeft += 3;
                            continue;
                        }
                    }
                    if (y < 6) {
                        if (DNA[x][y + 1] == 4) {
                            speedDown += 3;
                            continue;
                        }
                    }
                }
            }
        }
    }

    private void fuelRocket(int[][] DNA) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                if (DNA[i][j] == 3) {
                    fuel += 50;
                }
            }
        }
    }

    private void constructRocket(int[][] DNA) {
        for (int i = 0; i < 20; i++) {
            for (int x = 0; x < 5; x++) {
                for (int y = 0; y < 7; y++) {
                    if (DNA[x][y] == 9 || DNA[x][y] == 4) {
                        if (x > 0) {
                            if (DNA[x - 1][y] == 1) {
                                DNA[x - 1][y] = 4;
                            }
                        }
                        if (y > 0) {
                            if (DNA[x][y - 1] == 1) {
                                DNA[x][y - 1] = 4;
                            }
                        }
                        if (x < 4) {
                            if (DNA[x + 1][y] == 1) {
                                DNA[x + 1][y] = 4;
                            }
                        }
                        if (y < 6) {
                            if (DNA[x][y + 1] == 1) {
                                DNA[x][y + 1] = 4;
                            }
                        }
                    }
                }
            }
        }

        cleanDNA(DNA);
    }

    private void cleanDNA(int[][] DNA) {
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 7; k++) {
                    boolean delete = checkChromosome(DNA, j, k);
                    if (delete) {
                        DNA[j][k] = 0;
                    }
                }
            }
        }
    }

    private boolean checkChromosome(int[][] DNA, int x, int y) {
        boolean delete = true;
        switch (DNA[x][y]) {
            case 0:
                delete = true;
                break;
            case 1:
                delete = true;
                break;
            case 2:
                if (x > 0) {
                    if (DNA[x - 1][y] == 4) {
                        delete = false;
                    }
                }
                if (y > 0) {
                    if (DNA[x][y - 1] == 4) {
                        delete = false;
                    }
                }
                if (x < 4) {
                    if (DNA[x + 1][y] == 4) {
                        delete = false;
                    }
                }
                if (y < 6) {
                    if (DNA[x][y + 1] == 4) {
                        delete = false;
                    }
                }
                break;
            case 3:
                if (x > 0) {
                    if (DNA[x - 1][y] == 4) {
                        delete = false;
                    }
                }
                if (y > 0) {
                    if (DNA[x][y - 1] == 4) {
                        delete = false;
                    }
                }
                if (x < 4) {
                    if (DNA[x + 1][y] == 4) {
                        delete = false;
                    }
                }
                if (y < 6) {
                    if (DNA[x][y + 1] == 4) {
                        delete = false;
                    }
                }
                break;
            default:
                delete = false;
                break;

        }
        return delete;
    }

    public void mutate() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                if (DNA[i][j] == 4) {
                    DNA[i][j] = 1;
                }
            }
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                int chance = RAND.nextInt(100);
                int calcChance = 25;
                if (chance < calcChance) {
                    this.DNA[i][j] = RAND.nextInt(4);
                }
            }
        }
        constructRocket(DNA);
    }

    public void tick() {
        if (this.getX() < -50) {
            this.setX(Game.WIDTH);
        }
        if (this.getX() > Game.WIDTH) {
            this.setX(-50);
        }
        if ((Game.HEIGHT - this.getY() - 100) * realityMultiplier * weight > this.fitness) {
            this.setDistance((Game.HEIGHT - this.getY() - 100) * realityMultiplier);
            this.setFitness(this.getDistance() * this.getWeight());
        }
        if (this.getFitness() > Game.currentScore) {
            Game.currentScore = this.getFitness();
        }
        fuel--;
        if (fuel > 0) {
            x += xVelocity;
            y += yVelocity;
        } else {
            y += weight;
        }
    }

    public void render(Graphics g) {
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 7; y++) {
                if (DNA[x][y] == 4) {
                    g.setColor(this.color);
                }
                if (DNA[x][y] == 2) {
                    g.setColor(Color.red);
                }
                if (DNA[x][y] == 3) {
                    g.setColor(Color.orange);
                }
                if (DNA[x][y] == 9) {
                    g.setColor(Color.yellow);
                }
                if (DNA[x][y] != 0) {
                    if ((DNA[x][y] == 2 && this.fuel > 0) || DNA[x][y] == 3) {
                        if (y > 0) {
                            if (DNA[x][y - 1] == 4) {
                                g.fillRect(x * 10 + this.x + 1, y * 10 + this.y - Population.Y_OFFSET, 8, 5);
                                continue;
                            }
                        }
                        if (x < 4) {
                            if (DNA[x + 1][y] == 4) {
                                g.fillRect(x * 10 + this.x + 5, y * 10 + this.y + 1 - Population.Y_OFFSET, 5, 8);
                                continue;
                            }
                        }
                        if (x > 0) {
                            if (DNA[x - 1][y] == 4) {
                                g.fillRect(x * 10 + this.x, y * 10 + this.y + 1 - Population.Y_OFFSET, 5, 8);
                                continue;
                            }
                        }
                        if (y < 6) {
                            if (DNA[x][y + 1] == 4) {
                                g.fillRect(x * 10 + this.x + 1, y * 10 + this.y + 5 - Population.Y_OFFSET, 8, 5);
                                continue;
                            }
                        }
                    }
                    if (DNA[x][y] == 4 || DNA[x][y] == 9) {
                        g.fillRect(x * 10 + this.x, y * 10 + this.y - Population.Y_OFFSET, 10, 10);
                    }
                }
            }
        }
        g.setColor(Color.black);
        g.setFont(new Font("Arial", 0, 11));

        g.drawString("Score: " + this.getFitness(), this.x, this.y - 10 - Population.Y_OFFSET);
        if (fuel > 0) {
            g.drawString("Fuel: " + this.fuel, this.x, this.y - 20 - Population.Y_OFFSET);
        } else {
            g.drawString("Fuel: 0", this.x, this.y - 20 - Population.Y_OFFSET);
        }
        g.drawString("Distance: " + this.getDistance(), this.x, this.y - 30 - Population.Y_OFFSET);
        g.drawString("Passengers: " + this.getWeight() * 10, this.x, this.y - 40 - Population.Y_OFFSET);

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

    public int getFuel() {
        return fuel;
    }

    public void setFuel(int fuel) {
        this.fuel = fuel;
    }

    public int getSpeedUp() {
        return speedUp;
    }

    public void setSpeedUp(int speedUp) {
        this.speedUp = speedUp;
    }

    public int getSpeedDown() {
        return speedDown;
    }

    public void setSpeedDown(int speedDown) {
        this.speedDown = speedDown;
    }

    public int getSpeedLeft() {
        return speedLeft;
    }

    public void setSpeedLeft(int speedLeft) {
        this.speedLeft = speedLeft;
    }

    public int getSpeedRight() {
        return speedRight;
    }

    public int getRealityMultiplier() {
        return realityMultiplier;
    }

    public void setRealityMultiplier(int realityMultiplier) {
        this.realityMultiplier = realityMultiplier;
    }

    public void setSpeedRight(int speedRight) {
        this.speedRight = speedRight;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getxVelocity() {
        return xVelocity;
    }

    public void setxVelocity(int xVelocity) {
        this.xVelocity = xVelocity;
    }

    public int getyVelocity() {
        return yVelocity;
    }

    public void setyVelocity(int yVelocity) {
        this.yVelocity = yVelocity;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int[][] getDNA() {
        int[][] newDNA = new int[5][7];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                newDNA[i][j] = DNA[i][j];
            }
        }
        return newDNA;
    }

    public void setDNA(int[][] DNA) {
        this.DNA = DNA;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public static Random getRAND() {
        return RAND;
    }

    public static void setRAND(Random RAND) {
        TestSubject.RAND = RAND;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }


}


