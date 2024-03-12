package com.pi;

import javax.swing.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.*;

public class Main {

    public static String samplingMethod = Constants.DEFAULT_SAMPLING;
    public static int numThreads = Constants.DEFAULT_THREAD_COUNT;
    public static long dartsPerThread = Constants.DEFAULT_DARTS_PER_THREAD;
    public static int totalSubregions = Constants.DEFAULT_TOTAL_SUBREGIONS; // for Stratified sampling
    public static boolean showChart = Constants.DISPLAY_CHART; // for Stratified sampling

    public static void main(String[] args) {

        System.out.println(Arrays.toString(args));

        validateArgs(args);

        DartBoard dartBoard = buildDartBoard();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        CompletableFuture<Void>[] futures = new CompletableFuture[numThreads];
        for (int i = 0; i < numThreads; i++) {
            DartThread dartThread = buildDartThread(dartBoard);
            futures[i] = CompletableFuture.runAsync(dartThread, executor);
        }

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures);
        allOf.thenRun(dartBoard::calculatePi).join();

        if (showChart) {
            DartBoardVisualization dartBoardVisualization = new DartBoardVisualization(dartBoard.getHitCoordinates(), dartBoard.getMissCoordinates());
            dartBoardVisualization.render();
        }

        executor.shutdown();
    }

    public static DartBoard buildDartBoard() {
        return samplingMethod.equals(Constants.UNIFORM_SAMPLING) ?
                new DartBoardUniformSampling(numThreads):
                new DartBoardStratifiedSampling(numThreads, totalSubregions);
    }

    public static DartThread buildDartThread(DartBoard dartBoard) {
        return samplingMethod.equals(Constants.UNIFORM_SAMPLING) ?
                new DartThreadUniformSampling(dartBoard, dartsPerThread):
                new DartThreadStratifiedSampling(dartBoard, dartsPerThread, totalSubregions);
    }

    private static boolean samplingIsValid(String samplingMethod) {
        return Constants.VALID_SAMPLING_METHODS.contains(samplingMethod.toUpperCase());
    }

    private static void validateArgs(String[] args) {
        if (args.length >= 1) samplingMethod = args[0].toUpperCase();
        if (args.length >= 2) numThreads = Integer.parseInt(args[1]);
        if (args.length >= 3) dartsPerThread = Long.parseLong(args[2]);
        if (args.length >= 4) totalSubregions = Integer.parseInt(args[3]);
        if (args.length >= 5) showChart = Boolean.parseBoolean(args[4]);

        if (!samplingIsValid(samplingMethod)) {
            System.err.printf("Invalid Sampling Method: %s\n", samplingMethod);
            System.exit(-1);
        }

        if (numThreads < 1) {
            System.err.println("At least one thread required for execution");
            System.exit(-1);
        }

        if (dartsPerThread < 0) {
            System.out.println("Number of darts cannot be negative");
            System.exit(-1);
        }

        if (totalSubregions < 1 || totalSubregions > 1000) {
            System.out.println("Valid Subregions range is [1-1000]");
            System.exit(-1);
        }
    }
}