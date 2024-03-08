public interface DartBoard {
    /**
     * Adds the number of darts thrown and the number of hits to the board.
     *
     * @param numThrown the number of darts thrown
     * @param numHits the number of hits within the circle
     */
    void addThrows(long numThrown, long numHits);

    /**
     * Marks the current thread as finished.
     */
    void markFinished();

    /**
     * Checks if all threads have finished their execution.
     *
     * @return true if all threads have finished, false otherwise
     */
    boolean isFinished();

    /**
     * Calculates the value of pi based on the hits and throws and returns it.
     *
     * @return the value of pi
     */
    double calculatePi();
}
