/* *****************************************************************************
 *  Name: Kartik Kesarwani
 *  Date: 17/04/2020 (DD/MM/YYYY)
 *  Description: Percolation data type
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final WeightedQuickUnionUF weightedQuickUnionUF;
    // private final boolean[] fill;
    private final byte[] fill;
    private final boolean[] bottom;
    private final int size;
    private int numberOfOpenSites;
    private boolean percolates;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("wrong arguments");
        weightedQuickUnionUF = new WeightedQuickUnionUF(n * n + 1);
        // fill = new boolean[n * n + 1];
        fill = new byte[n * n + 1];
        bottom = new boolean[n * n + 1];
        numberOfOpenSites = 0;
        size = n;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validateIdx(row, col);
        int i = xyTo1D(row, col);
        if (fill[i] != 0)
            return;
        fill[i] = 1;
        numberOfOpenSites++;

        int lastRowStart = (size * size) - size + 1, lastRowEnd = size * size;
        int topVirtualPoint = 0;
        int lefti = xyTo1D(row, col - 1);
        int righti = xyTo1D(row, col + 1);
        int topi = xyTo1D(row - 1, col);
        int downi = xyTo1D(row + 1, col);
        boolean leftConnBottom = false, rightConnBottom = false, topConnBottom = false,
                downConnBottom = false;

        if (i >= 1 && i <= size) {
            weightedQuickUnionUF.union(i, topVirtualPoint);
        }
        if (lefti != -1 && fill[lefti] != 0) {
            leftConnBottom = bottom[weightedQuickUnionUF.find(lefti)];
            weightedQuickUnionUF.union(i, lefti);
        }
        if (righti != -1 && fill[righti] != 0) {
            if (!leftConnBottom)
                rightConnBottom = bottom[weightedQuickUnionUF.find(righti)];
            weightedQuickUnionUF.union(i, righti);
        }
        if (topi != -1 && fill[topi] != 0) {
            if (!leftConnBottom && !rightConnBottom)
                topConnBottom = bottom[weightedQuickUnionUF.find(topi)];
            weightedQuickUnionUF.union(i, topi);
        }
        if (downi != -1 && fill[downi] != 0) {
            if (!leftConnBottom && !rightConnBottom && !topConnBottom)
                downConnBottom = bottom[weightedQuickUnionUF.find(downi)];
            weightedQuickUnionUF.union(i, downi);
        }

        int currRoot = weightedQuickUnionUF.find(i);

        if (leftConnBottom || rightConnBottom || topConnBottom || downConnBottom) {
            // System.out.println("final root : " + currRoot + "connected to bottom.");
            bottom[currRoot] = true;
        }
        else if (i >= lastRowStart && i <= lastRowEnd) {
            // System.out.println("Opening site at last row with no neighbour has root : " + currRoot);
            bottom[currRoot] = true;
        }

        if (!percolates) {
            boolean topConnected = connected(topVirtualPoint, i);
            percolates = topConnected && bottom[currRoot];
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validateIdx(row, col);
        int i = xyTo1D(row, col);
        if (fill[i] != 0)
            return true;
        return false;
        // return fill[i];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validateIdx(row, col);
        int i = xyTo1D(row, col);
        int topVirtualPoint = 0;
        if (fill[i] == 2 || connected(topVirtualPoint, i)) {
            fill[i] = 2;
            return true;
        }
        return false;
        // return connected(topVirtualPoint, i);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return percolates;
    }

    private int xyTo1D(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size) {
            return -1;
        }
        int idx = ((row - 1) * size) + col;
        return idx;
    }

    private void validateIdx(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size) {
            throw new IllegalArgumentException("wrong arguments");
        }
    }

    private boolean connected(int a, int b) {
        return weightedQuickUnionUF.find(a) == weightedQuickUnionUF.find(b);
    }

    // test client (optional)
    public static void main(String[] args) {
        // client code goes here

        byte[] fill = new byte[10];

        for (int i = 0; i < fill.length; i++)
            fill[i] = 20;

        for (byte a : fill)
            System.out.println(a);
    }


}
