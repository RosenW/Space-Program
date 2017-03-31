package models;

import java.awt.*;

public class ShipModel {
    private static int x = 180;
    private static int y = 50;
    private int[][] DNA;
    private Color color;
    private int score;

    public ShipModel() {
        DNA = new int[5][7];
        color = Color.black;
    }

    public ShipModel(int[][] DNA, Color color, int score) {
        this.DNA = DNA;
        this.color = color;
        this.score = score;
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
                    if (DNA[x][y] == 2 || DNA[x][y] == 3) {
                        if (y > 0) {
                            if (DNA[x][y - 1] == 4) {
                                g.fillRect(x * 10 + this.x + 1, y * 10 + this.y, 8, 5);
                                continue;
                            }
                        }
                        if (x < 4) {
                            if (DNA[x + 1][y] == 4) {
                                g.fillRect(x * 10 + this.x + 5, y * 10 + this.y + 1, 5, 8);
                                continue;
                            }
                        }
                        if (x > 0) {
                            if (DNA[x - 1][y] == 4) {
                                g.fillRect(x * 10 + this.x, y * 10 + this.y + 1, 5, 8);
                                continue;
                            }
                        }
                        if (y < 6) {
                            if (DNA[x][y + 1] == 4) {
                                g.fillRect(x * 10 + this.x + 1, y * 10 + this.y + 5, 8, 5);
                                continue;
                            }
                        }
                    }
                    if (DNA[x][y] == 4 || DNA[x][y] == 9) {
                        g.fillRect(x * 10 + this.x, y * 10 + this.y, 10, 10);
                    }
                }
            }
        }
        g.setColor(Color.black);
        g.drawString(String.format("(%s)", this.score), 250, 100);
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

    public int[][] getDNA() {
        return DNA;
    }

    public void setDNA(int[][] DNA) {
        this.DNA = DNA;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
