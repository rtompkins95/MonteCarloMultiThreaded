package com.pi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

class DartBoardStratifiedSampling implements DartBoard {
    private final AtomicLong numHits = new AtomicLong(0);
    private final AtomicLong numThrown = new AtomicLong(0);
    private final List<XYPair> hitCoordinates;
    private final List<XYPair> missCoordinates;
    private final int totalSubregions;
    private final CountDownLatch latch;

    public DartBoardStratifiedSampling(int numThreads, int totalSubregions) {
        this.latch = new CountDownLatch(numThreads);
        this.totalSubregions = totalSubregions;
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

    /**
     * Adjusts the ratio of hits to throws by the area of each subregion in stratified sampling to ensure accurate estimation of pi.
     *
     * In stratified sampling, the unit square is divided into subregions, and sampling occurs more densely within each subregion.
     * The adjustment factor compensates for the varying sampling density across subregions.
     * The adjustment factor is the reciprocal of the total number of subregions, ensuring that the estimation of pi is appropriately scaled to account for the stratified sampling scheme.
     *
     * @return The adjustment factor for estimating pi using stratified sampling.
     */

    public double calculatePi() {
        double totalHits = numHits.get();
        double totalThrows = numThrown.get();
        double pi = 4.0 * (totalHits / totalThrows) / totalSubregions;

        System.out.printf("Num Subregions: %d%n", totalSubregions);
        System.out.printf("Num Thrown: %d%n", numThrown.get());
        System.out.printf("Num Hit: %d%n", numHits.get());
        System.out.printf("Pi: %1.10f%n", pi);
        return pi;
    }

    private double adjustmentFactor(int totalSubregions) {
        if (totalSubregions == 10) return 1.0/10;
        if (totalSubregions == 100) return 100.0/100;
        if (totalSubregions == 1000) return 10000.0/1000;
        if (totalSubregions == 10000) return 1000000.0/10000;
        return totalSubregions;
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