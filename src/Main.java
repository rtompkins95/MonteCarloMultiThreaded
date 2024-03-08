import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {
        boolean isUniform = false; // Set to true for uniform sampling, false for stratified sampling
        int numThreads = 200;
        long dartsPerThread = 100000;

        if (args.length >= 3) {
            isUniform = Boolean.parseBoolean(args[0]);
            numThreads = Integer.parseInt(args[1]);
            dartsPerThread = Long.parseLong(args[2]);
        }

        DartBoard dartBoard = buildDartBoard(numThreads, isUniform);
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        CompletableFuture<Void>[] futures = new CompletableFuture[numThreads];
        for (int i = 0; i < numThreads; i++) {
            DartThread dartThread = buildDartThread(dartBoard, dartsPerThread, isUniform);
            futures[i] = CompletableFuture.runAsync(dartThread, executor);
        }

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures);
        allOf.thenRun(dartBoard::calculatePi)
                .join();

        executor.shutdown();
    }

    public static DartBoard buildDartBoard(int numThreads, boolean isUniform) {
        return isUniform ?
                new DartBoardUniformSampling(numThreads):
                new DartBoardStratifiedSampling(numThreads);
    }

    public static DartThread buildDartThread(DartBoard dartBoard, long dartsPerThread, boolean isUniform) {
        return isUniform ?
                new DartThreadUniformSampling(dartBoard, dartsPerThread):
                new DartThreadStratifiedSampling(dartBoard, dartsPerThread);
    }

}