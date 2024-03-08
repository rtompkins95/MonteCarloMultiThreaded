import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {
        String samplingMethod = Constants.UNIFORM_SAMPLING;
        int numThreads = Constants.DEFAULT_THREAD_COUNT;
        long dartsPerThread = Constants.DEFAULT_DARTS_PER_THREAD;

        if (args.length >= 3) {
            samplingMethod = args[0].toUpperCase();
            numThreads = Integer.parseInt(args[1]);
            dartsPerThread = Long.parseLong(args[2]);
        }

        if (!samplingIsValid(samplingMethod)) {
            System.err.printf("Invalid Sampling Method: %s\n", samplingMethod);
            System.exit(-1);
        }

        if (numThreads < 1) {
            System.err.printf("At least one thread required for execution\n", samplingMethod);
            System.exit(-1);
        }

        if (dartsPerThread < 0) {
            System.err.printf("Number of darts cannot be negative\n", samplingMethod);
            System.exit(-1);
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

    public static DartBoard buildDartBoard(int numThreads, String samplingMethod) {
        return samplingMethod.equals(Constants.UNIFORM_SAMPLING) ?
                new DartBoardUniformSampling(numThreads):
                new DartBoardStratifiedSampling(numThreads, Constants.DEFAULT_TOTAL_SUBREGIONS);
    }

    public static DartThread buildDartThread(DartBoard dartBoard, long dartsPerThread, String samplingMethod) {
        return samplingMethod.equals(Constants.UNIFORM_SAMPLING) ?
                new DartThreadUniformSampling(dartBoard, dartsPerThread):
                new DartThreadStratifiedSampling(dartBoard, dartsPerThread);
    }

    private static boolean samplingIsValid(String samplingMethod) {
        return Constants.VALID_SAMPLING_METHODS.contains(samplingMethod);
    }
}