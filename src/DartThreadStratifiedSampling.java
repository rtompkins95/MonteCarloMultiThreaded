import java.util.concurrent.ThreadLocalRandom;

class DartThreadStratifiedSampling implements DartThread {
    private final DartBoard dartBoard;
    private final long numDarts;

    public DartThreadStratifiedSampling(DartBoard dartBoard, long numDarts) {
        this.dartBoard = dartBoard;
        this.numDarts = numDarts;
    }

    public void run() {
        long numHits = 0;
        ThreadLocalRandom random = ThreadLocalRandom.current();
        long dartsPerSubregion = numDarts / 10; // Divide the square into 10x10 subregions

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
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