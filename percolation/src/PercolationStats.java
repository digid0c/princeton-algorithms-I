import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONFIDENCE_95 = 1.96;

    private double[] simulationResults;
    private int trials;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0) {
            throw new IllegalArgumentException("N must be greater than zero!");
        }
        if (trials <= 0) {
            throw new IllegalArgumentException("Number of trials must be greater than zero!");
        }

        this.trials = trials;
        simulationResults = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int row = StdRandom.uniformInt(n);
                int col = StdRandom.uniformInt(n);
                row++;
                col++;
                percolation.open(row, col);
            }
            int openSites = percolation.numberOfOpenSites();
            simulationResults[i] = (1.0 * openSites) / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(simulationResults);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(simulationResults);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - CONFIDENCE_95 * stddev() / Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + CONFIDENCE_95 * stddev() / Math.sqrt(trials);
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats percolationStats = new PercolationStats(n, trials);
        System.out.printf("mean = %f%n", percolationStats.mean());
        System.out.printf("stddev = %f%n", percolationStats.stddev());
        System.out.printf("95 confidence interval = [%f, %f]%n", percolationStats.confidenceLo(), percolationStats.confidenceHi());
    }
}
