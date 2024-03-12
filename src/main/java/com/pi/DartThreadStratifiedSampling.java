package com.pi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

class DartThreadStratifiedSampling implements DartThread {
    private final DartBoard dartBoard;
    private final long numDarts;
    private final int totalSubregions;

    private List<XYPair> hitCoordinates;
    private List<XYPair> missCoordinates;

    public DartThreadStratifiedSampling(DartBoard dartBoard, long numDarts, int numSubregions) {
        this.dartBoard = dartBoard;
        this.numDarts = numDarts;
        this.totalSubregions = numSubregions;
        this.hitCoordinates = Collections.synchronizedList(new ArrayList<>());
        this.missCoordinates = Collections.synchronizedList(new ArrayList<>());
    }

    public DartThreadStratifiedSampling(DartBoard dartBoard, long numDarts) {
        this(dartBoard, numDarts, Constants.DEFAULT_TOTAL_SUBREGIONS);
    }

    public void run() {
        long numHits = 0;
        ThreadLocalRandom random = ThreadLocalRandom.current();
        long dartsPerSubregion = numDarts / totalSubregions; // Divide the square into 10x10 subregions

        for (int i = 0; i < totalSubregions; i++) {
            for (int j = 0; j < totalSubregions; j++) {
                numHits += throwDartsInSubregion(random, dartsPerSubregion, i, j);
            }
        }

        dartBoard.addHitCoordinates(this.hitCoordinates);
        dartBoard.addMissCoordinates(this.missCoordinates);
        dartBoard.addThrows(numDarts, numHits);
        dartBoard.markFinished();
    }

    private long throwDartsInSubregion(ThreadLocalRandom random,
                                       long numDarts,
                                       int subregionX,
                                       int subregionY) {
        long numHits = 0;
        double subregionWidth = 0.1; // Width of each subregion
        double xOffset = subregionX * subregionWidth;
        double yOffset = subregionY * subregionWidth;

        for (long i = 0; i < numDarts; i++) {
            // Generate random points within the subregion
            double x = random.nextDouble(xOffset, xOffset + subregionWidth);
            double y = random.nextDouble(yOffset, yOffset + subregionWidth);
            // Check if the point falls within the circle
            if ((Math.sqrt((x * x) + (y * y)) <= 1)) {
                numHits++;
                this.hitCoordinates.add(new XYPair(x, y));
            } else {
                this.missCoordinates.add(new XYPair(x, y));
            }
        }
        return numHits;
    }
}