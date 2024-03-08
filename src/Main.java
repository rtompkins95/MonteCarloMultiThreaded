import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {
        int numThreads = 256;
        long dartsPerThread = 10000000;
        boolean isUniform = false;

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