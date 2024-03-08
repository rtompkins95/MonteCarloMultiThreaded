import java.util.concurrent.ThreadLocalRandom;

class DartThreadStratifiedSampling implements DartThread {
    private final DartBoard dartBoard;
    private final long numDarts;
    private final int totalSubregions;
    public DartThreadStratifiedSampling(DartBoard dartBoard, long numDarts, int numSubregions) {
        this.dartBoard = dartBoard;
        this.numDarts = numDarts;
        this.totalSubregions = numSubregions;
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
        dartBoard.addThrows(numDarts, numHits);
        dartBoard.markFinished();
    }

    private long throwDartsInSubregion(ThreadLocalRandom random, long numDarts, int subregionX, int subregionY) {
        long numHits = 0;
        double subregionWidth = 0.1; // Width of each subregion
        double xOffset = subregionX * subregionWidth;
        double yOffset = subregionY * subregionWidth;

        for (long i = 0; i < numDarts; i++) {
            // Generate random points within the subregion
            double x = random.nextDouble(xOffset, xOffset + subregionWidth);
            double y = random.nextDouble(yOffset, yOffset + subregionWidth);
            // Check if the point falls within the circle
            if (Math.sqrt((x * x) + (y * y)) < 1) {
                numHits++;
            }
        }
        return numHits;
    }
}