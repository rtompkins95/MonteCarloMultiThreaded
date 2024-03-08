class OutputThread extends Thread {
    private final DartBoardUniformSampling dartBoard;

    public OutputThread(DartBoardUniformSampling dartBoard) {
        this.dartBoard = dartBoard;
    }

    public void run() {
        try {
            while (!dartBoard.isFinished()) {
                Thread.sleep(100); // Sleep for a short time to avoid busy waiting
            }
            dartBoard.calculatePi();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}