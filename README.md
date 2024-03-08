# Pi Estimation with Monte Carlo Method

This project provides a multithreaded Java program to estimate the value of pi using the Monte Carlo method.

## Purpose

The purpose of this project is to demonstrate a practical application of the Monte Carlo method for estimating pi. By simulating the throwing of darts at a square dartboard with a circular target inscribed within it, the program calculates an approximation of pi based on the ratio of darts landing within the circle to the total number of darts thrown.

## How to Run

To run the program:

1. Ensure you have Java installed on your system.
2. Clone this repository to your local machine.
3. Navigate to the directory containing the Java files (`Main.java`, `DartBoard.java`, `DartThread.java`, `OutputThread.java`).
4. Compile the Java files using the following command:

    ```
    javac Main.java
    ```

5. Run the compiled program with:

    ```
    java Main uniform 200 100000
    ```
   You can use these optional flags:
   1. Sampling type:
      1. uniform = Uniform Sampling, stratified = Statified Sampling
   2. Number of Threads 
   3. Darts per Thread
## Contributing

Contributions to this project are welcome! If you have any ideas for improvements, new features, or bug fixes, please feel free to open an issue or create a pull request.

## License

This project is licensed under the [MIT License](LICENSE).
