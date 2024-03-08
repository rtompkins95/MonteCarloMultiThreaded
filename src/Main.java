import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {
        boolean samplingMethod = Constants.STRATIFIED_SAMPLING;
        int numThreads = Constants.DEFAULT_THREAD_COUNT;
        long dartsPerThread = Constants.DEFAULT_DARTS_PER_THREAD;

        if (args.length >= 3) {
            samplingMethod = Boolean.parseBoolean(args[0]);
            numThreads = Integer.parseInt(args[1]);
            dartsPerThread = Long.parseLong(args[2]);
        }

        DartBoard dartBoard = buildDartBoard(numThreads, samplingMethod);
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        CompletableFuture<Void>[] futures = new CompletableFuture[numThreads];
        for (int i = 0; i < numThreads; i++) {
            DartThread dartThread = buildDartThread(dartBoard, dartsPerThread, samplingMethod);
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
                new DartBoardStratifiedSampling(numThreads, Constants.DEFAULT_TOTAL_SUBREGIONS);
    }

    public static DartThread buildDartThread(DartBoard dartBoard, long dartsPerThread, boolean isUniform) {
        return isUniform ?
                new DartThreadUniformSampling(dartBoard, dartsPerThread):
                new DartThreadStratifiedSampling(dartBoard, dartsPerThread);
    }
}