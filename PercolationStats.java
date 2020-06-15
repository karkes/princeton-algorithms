/* *****************************************************************************
 *  Name: Kartik Kesarwani
 *  Date: 16/04/2020 (DD/MM/YYYY)
 *  Description: Percolation stats
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONFIDENCE_CONSTANT = 1.96;
    private final int size;
    private final int trials;
    private final double[] probability;
    private double mean;
    private double stddev;
    private boolean isMeanCalc;
    private boolean isStddevCalc;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("wrong argument");
        }
        this.size = n;
        this.trials = trials;
        probability = new double[trials];
        runTrials();
    }

    private void runTrials() {
        int turns = trials;
        while (turns > 0) {
            Percolation perc = new Percolation(size);
            while (!perc.percolates()) {
                int row = StdRandom.uniform(1, size + 1);
                int col = StdRandom.uniform(1, size + 1);
                perc.open(row, col);
            }
            turns--;
            probability[turns] = (double) perc.numberOfOpenSites() / (double) (size * size);
        }

    }

    // sample mean of percolation threshold
    public double mean() {
        if (!isMeanCalc) {
            mean = StdStats.mean(probability);
            isMeanCalc = true;
        }
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (!isStddevCalc) {
            stddev = StdStats.stddev(probability);
            isStddevCalc = true;
        }
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - ((CONFIDENCE_CONSTANT * stddev()) / Math.sqrt(trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {

        return mean() + ((CONFIDENCE_CONSTANT * stddev()) / Math.sqrt(trials));
    }

    public static void main(String[] args) {

        PercolationStats pstats = new PercolationStats(Integer.parseInt(args[0]),
                                                       Integer.parseInt(args[1]));

        System.out.println("95% confidence interval = " + pstats.confidenceLo()
                                   + ", " + pstats.confidenceHi());
        System.out.println("mean = " + pstats.mean());
        System.out.println("95% confidence interval = " + pstats.confidenceLo()
                                   + ", " + pstats.confidenceHi());
        System.out.println("stddev = " + pstats.stddev());
        System.out.println("95% confidence interval = " + pstats.confidenceLo()
                                   + ", " + pstats.confidenceHi());
        System.out.println("mean = " + pstats.mean());
        // mean() mean() confidenceHi() confidenceHi() stddev() stddev() confidenceLo() stddev()
        // confidenceHi()


    }
}
