package com.pi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

class DartThreadUniformSampling implements DartThread {
    private final DartBoard dartBoard;
    private final long numDarts;
    private final List<XYPair> hitCoordinates;
    private final List<XYPair> missCoordinates;

    public DartThreadUniformSampling(DartBoard dartBoard, long numDarts) {
        this.dartBoard = dartBoard;
        this.numDarts = numDarts;
        this.hitCoordinates = Collections.synchronizedList(new ArrayList<>());
        this.missCoordinates = Collections.synchronizedList(new ArrayList<>());
    }

    public void run() {
        long numHits = 0;
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (long i = 0; i < numDarts; i++) {
            float x = random.nextFloat(); // 0.0 - 1.0
            float y = random.nextFloat();
            if ((Math.sqrt((x * x) + (y * y)) <= 1)) {
                numHits++;
                this.hitCoordinates.add(new XYPair(x, y));
            } else {
                this.missCoordinates.add(new XYPair(x, y));
            }
        }
        dartBoard.addThrows(numDarts, numHits);
        dartBoard.addHitCoordinates(this.hitCoordinates);
        dartBoard.addMissCoordinates(this.missCoordinates);
        dartBoard.markFinished();
    }
}