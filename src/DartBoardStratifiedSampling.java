import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

class DartBoardStratifiedSampling implements DartBoard {
    private AtomicLong numHits = new AtomicLong(0);
    private AtomicLong numThrown = new AtomicLong(0);
    private final int totalSubregions;
    private final CountDownLatch latch;

    public DartBoardStratifiedSampling(int numThreads, int totalSubregions) {
        this.latch = new CountDownLatch(numThreads);
        this.totalSubregions = totalSubregions;
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
}