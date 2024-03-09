package com.pi;

import java.util.concurrent.ThreadLocalRandom;

class DartThreadUniformSampling implements DartThread {
    private final DartBoard dartBoard;
    private final long numDarts;

    public DartThreadUniformSampling(DartBoard dartBoard, long numDarts) {
        this.dartBoard = dartBoard;
        this.numDarts = numDarts;
    }

    public void run() {
        long numHits = 0;
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (long i = 0; i < numDarts; i++) {
            float x = random.nextFloat(); // 0.0 - 1.0
            float y = random.nextFloat();
            if ((Math.sqrt((x * x) + (y * y)) < 1)) {
                numHits++;
            }
        }
        dartBoard.addThrows(numDarts, numHits);
        dartBoard.markFinished();
    }
}