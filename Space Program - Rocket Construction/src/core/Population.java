package core;

import models.ShipModel;
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
    private int mostFueledShip = 50;

    public static int GENERATION = 1;
    public static int Y_OFFSET = 0;

    public Population(int count) {
        time = 0;
        index = 0;
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
        setMostFueledShip();
    }

    public void tick() {
        ///////////////////////ALL ROCKETS AT ONCE//////(should change render() too)///////////////////
        if (time < mostFueledShip) {
            int highestY = 0;
            for (TestSubject testSubject : testSubjects) {

                if (testSubject.getY() - 180 < highestY) {
                    highestY = testSubject.getY()-180;
                }

                testSubject.tick();
            }
            Population.Y_OFFSET = highestY;
            time++;
        } else {
            int currentMax = 0;
            int maxShipIndex = 0;
            for (int i = 0; i < testSubjects.size(); i++) {
                TestSubject testSubject = testSubjects.get(i);
                if (testSubject.getFitness() > currentMax) {
                    currentMax = testSubject.getFitness();
                    maxShipIndex = i;
                }
            }
            if (currentMax > Game.TOP_SCORE) {
                Game.TOP_SCORE = currentMax;
                TestSubject ship = testSubjects.get(maxShipIndex);
                Game.BEST_SHIP_MODEL = new ShipModel(ship.getDNA(), ship.getColor(), ship.getFitness(), ship.getWeight(), ship.getDistance());
                Game.BEST_SHIP = ship;
            }
            Game.CURRENT_SCORE = 0;
            List<TestSubject> nextGen = getNextGenerationKids();
            List<TestSubject> mutatedTestSubjects = this.mutate(nextGen);
            this.testSubjects.clear();
            for (TestSubject mutatedTestSubject : mutatedTestSubjects) {
                this.testSubjects.add(new TestSubject(mutatedTestSubject.getDNA()));
            }
            setMostFueledShip();
            time = 0;
            GENERATION++;
        }
        ///////////////////////ONE ROCKET AT A TIME//////(should change render() too)///////////////
//        if (testSubjects.get(index).getFuel() > -50 && (testSubjects.get(index).getX() > -50 && testSubjects.get(index).getX() < Game.WIDTH + 50 && testSubjects.get(index).getY() < Game.HEIGHT)) {
//            testSubjects.get(index).tick();
//        } else {
//            if (Game.currentScore > Game.TOP_SCORE) {
//                Game.TOP_SCORE = Game.currentScore;
//                Game.BEST_SHIP_MODEL = new ShipModel(testSubjects.get(index).getDNA(), testSubjects.get(index).getColor());
//            }
//            Game.currentScore = 0;
//            index++;
//            if (index == populationCount) {
//                index = 0;
//                List<TestSubject> nextGen = getNextGenerationKids();
//                List<TestSubject> mutatedTestSubjects = this.mutate(nextGen);
//                this.testSubjects.clear();
//                for (TestSubject mutatedTestSubject : mutatedTestSubjects) {
//                    this.testSubjects.add(new TestSubject(mutatedTestSubject.getDNA()));
//                }
//                GENERATION++;
//            }
//        }
    }

    private void setMostFueledShip() {
        mostFueledShip = 0;
        for (TestSubject testSubject : testSubjects) {
            if (testSubject.getFuel() > mostFueledShip) {
                mostFueledShip = testSubject.getFuel();
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
            for (int i = 0; i < populationCount / 4; i++) {
                nextGen.add(Game.BEST_SHIP);
            }
            if (this.testSubjects.size() < populationCount) {
                nextGen.add(new TestSubject(matingPool.get(RAND.nextInt(populationCount / 4 + 1)).getDNA()));
            }
        }

        return nextGen;
    }

    public void render(Graphics g) {
        for (TestSubject testSubject : testSubjects) {
            testSubject.render(g);
        }
//        testSubjects.get(index).render(g);
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

    public int getHighestDistance(){
        int highestDistance = 0;
        for (TestSubject testSubject : testSubjects) {
            if (testSubject.getY()< highestDistance){
                highestDistance = testSubject.getY();
            }
        }
        return highestDistance;
    }

}
