package core;

import models.TestSubject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Population {
    private List<TestSubject> testSubjects;
    private static Random RAND = new Random();
    private int time;
    private int populationCount;
    private int index;

    public static int GENERATION;

    public Population(int count) {
        index = 0;
        GENERATION = 1;
        this.populationCount = count;
        this.testSubjects = new ArrayList<>();
        for (int i = 0; i < populationCount; i++) {
            int[][] curDNA = new int[5][7];
            for (int j = 0; j < curDNA.length; j++) {
                for (int k = 0; k < 7; k++) {
                    curDNA[j][k] = RAND.nextInt(3) + 1;
                }
            }
            TestSubject curTestSubject = new TestSubject(curDNA);
            testSubjects.add(curTestSubject);
        }
    }

    public void tick() {
        if (testSubjects.get(index).getFuel() > -50 && (testSubjects.get(index).getX() > -50 && testSubjects.get(index).getX() < Game.WIDTH + 50 && testSubjects.get(index).getY() < Game.HEIGHT)) {
            testSubjects.get(index).tick();
        } else {
            if (Game.currentScore > Game.TOPSCORE) {
                Game.TOPSCORE = Game.currentScore;
            }
            Game.currentScore = 0;
            index++;
            if (index == populationCount) {
                index = 0;
                List<TestSubject> nextGen = getNextGenerationKids();
                List<TestSubject> mutatedTestSubjects = this.mutate(nextGen);
                this.testSubjects.clear();
                for (TestSubject mutatedTestSubject : mutatedTestSubjects) {
                    this.testSubjects.add(new TestSubject(mutatedTestSubject.getDNA()));
                }
                GENERATION++;
            }
        }
    }

    private List<TestSubject> mutate(List<TestSubject> matingPool) {
        List<TestSubject> mutatedTestSubjects = new ArrayList<>();
        for (TestSubject testSubject : matingPool) {
            testSubject.mutate();
            mutatedTestSubjects.add(new TestSubject(testSubject.getDNA()));
        }
        return mutatedTestSubjects;
    }

    private List<TestSubject> getNextGenerationKids() {
        List<TestSubject> matingPool = new ArrayList<>();
        testSubjects.stream().sorted((o1, o2) -> Integer.compare(o2.getFitness(), o1.getFitness())).limit(populationCount / 2 + 1).forEach(matingPool::add);

        List<TestSubject> nextGen = new ArrayList<>();

        while (nextGen.size() < populationCount) {
            for (int i = 0; i < populationCount / 3; i++) {
                nextGen.add(new TestSubject(matingPool.get(0).getDNA()));
            }
            for (int i = 0; i < populationCount / 5; i++) {
                nextGen.add(new TestSubject(matingPool.get(1).getDNA()));
            }
            for (int i = 0; i < populationCount / 10; i++) {
                nextGen.add(new TestSubject(matingPool.get(2).getDNA()));
            }
            for (int i = 0; i < populationCount / 10; i++) {
                nextGen.add(new TestSubject(matingPool.get(3).getDNA()));
            }
            for (int i = 0; i < populationCount / 20; i++) {
                nextGen.add(new TestSubject(matingPool.get(4).getDNA()));
            }
            for (int i = 0; i < populationCount / 20; i++) {
                nextGen.add(new TestSubject(matingPool.get(5).getDNA()));
            }
            for (int i = 0; i < populationCount / 20; i++) {
                nextGen.add(new TestSubject(matingPool.get(6).getDNA()));
            }
            for (int i = 0; i < populationCount / 20; i++) {
                nextGen.add(new TestSubject(matingPool.get(7).getDNA()));
            }
            for (int i = 0; i < populationCount / 20; i++) {
                nextGen.add(new TestSubject(matingPool.get(8).getDNA()));
            }
            for (int i = 0; i < populationCount / 20; i++) {
                nextGen.add(new TestSubject(matingPool.get(9).getDNA()));
            }
            if (this.testSubjects.size() < populationCount) {
                nextGen.add(new TestSubject(matingPool.get(RAND.nextInt(populationCount / 4 + 1)).getDNA()));
            }
        }

        return nextGen;
    }

    public void render(Graphics g) {
        testSubjects.get(index).render(g);
    }


    public List<TestSubject> getTestSubjects() {
        return testSubjects;
    }

    public void setTestSubjects(List<TestSubject> testSubjects) {
        this.testSubjects = testSubjects;
    }

    public static Random getRAND() {
        return RAND;
    }

    public static void setRAND(Random RAND) {
        Population.RAND = RAND;
    }

}
