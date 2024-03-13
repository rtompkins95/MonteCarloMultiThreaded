package com.pi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

class DartBoardUniformSampling implements DartBoard {
    private final AtomicLong numHits = new AtomicLong(0);
    private final AtomicLong numThrown = new AtomicLong(0);
    private final List<XYPair> hitCoordinates;
    private final List<XYPair> missCoordinates;
    private final CountDownLatch latch;

    public DartBoardUniformSampling(int numThreads) {
        this.latch = new CountDownLatch(numThreads);
        this.hitCoordinates = Collections.synchronizedList(new ArrayList<>());
        this.missCoordinates = Collections.synchronizedList(new ArrayList<>());
    }

    public void addThrows(long numThrown, long numHits) {
        this.numThrown.addAndGet(numThrown);
        this.numHits.addAndGet(numHits);
    }

    public void markFinished() {
        latch.countDown();
    }

    public boolean isFinished() {
        return latch.getCount() == 0;
    }

    public double calculatePi() {
        double totalHits = numHits.get();
        double totalThrows = numThrown.get();
        double pi = 4.0 * (totalHits / totalThrows);

        System.out.printf("Num Thrown: %d%n", numThrown.get());
        System.out.printf("Num Hit: %d%n", numHits.get());
        System.out.printf("Pi: %1.10f%n", pi);
        return pi;
    }

    public long getTotalThrow() {
        return this.numThrown.get();
    }

    public long getTotalHits() {
        return this.numHits.get();
    }

    public void addHitCoordinates(List<XYPair> hitCoordinates) {
        this.hitCoordinates.addAll(hitCoordinates);
    }

    public void addMissCoordinates(List<XYPair> missCoordinates) {
        this.missCoordinates.addAll(missCoordinates);
    }

    public List<XYPair> getHitCoordinates() {
        return this.hitCoordinates;
    }

    public List<XYPair> getMissCoordinates() {
        return this.missCoordinates;
    }
}