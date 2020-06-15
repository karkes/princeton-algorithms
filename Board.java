/* *****************************************************************************
 *  Name: Kartik Kesarwani
 *  Date: 09/05/2020 DD/MM/YYYY
 *  Description: Puzzle Board api
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public final class Board {

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    private final int[][] board;
    private final int size;

    public Board(int[][] tiles) {

        size = tiles.length;
        board = new int[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                board[row][col] = tiles[row][col];
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(size);
        sb.append("\n");
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                sb.append(String.format("%2d ", board[row][col]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return size;
    }

    // number of tiles out of place
    public int hamming() {
        int distance = 0;
        int goalValue = 1;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (board[row][col] != 0 && goalValue != board[row][col]) {
                    distance++;
                }
                goalValue++;
            }
        }
        return distance;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int distance = 0;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (board[row][col] != 0) {
                    int goalRow = (board[row][col] - 1) / size;
                    int goalCol = (board[row][col] - 1) % size;
                    distance += Math.abs(row - goalRow) + Math.abs(col - goalCol);
                }
            }
        }
        return distance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        int goalValue = 1;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (goalValue != (size * size) && board[row][col] != goalValue) {
                    return false;
                }
                goalValue++;
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;
        Board bd = (Board) other;
        if (this.dimension() != bd.dimension()) {
            return false;
        }
        int sz = dimension();
        for (int row = 0; row < sz; row++) {
            for (int col = 0; col < sz; col++) {
                if (this.board[row][col] != bd.board[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> neighBoardQ = new Queue<>();
        int sz = dimension();
        int blankRow = -1;
        int blankCol = -1;
        boolean left = false;
        boolean right = false;
        boolean up = false;
        boolean down = false;
        int[][] neighbd = new int[sz][sz];
        for (int row = 0; row < sz; row++) {
            for (int col = 0; col < sz; col++) {
                neighbd[row][col] = board[row][col];
                if (board[row][col] == 0) {
                    blankRow = row;
                    blankCol = col;
                }
            }
        }
        // a board can have 2, 3, or 4 neighbors
        if (blankCol - 1 >= 0) left = true;
        if (blankCol + 1 < sz) right = true;
        if (blankRow - 1 >= 0) up = true;
        if (blankRow + 1 < sz) down = true;

        if (left) {
            addNeighborToQ(neighBoardQ, neighbd, blankRow, blankCol, blankRow, blankCol - 1);
        }
        if (right) {
            addNeighborToQ(neighBoardQ, neighbd, blankRow, blankCol, blankRow, blankCol + 1);
        }
        if (up) {
            addNeighborToQ(neighBoardQ, neighbd, blankRow, blankCol, blankRow - 1, blankCol);
        }
        if (down) {
            addNeighborToQ(neighBoardQ, neighbd, blankRow, blankCol, blankRow + 1, blankCol);
        }
        return neighBoardQ;
    }

    private void addNeighborToQ(Queue<Board> neighBoardQ, int[][] neighbd, int blankRow,
                                int blankCol, int adjRow, int adjCol) {
        // swap
        neighbd[blankRow][blankCol] = neighbd[adjRow][adjCol];
        neighbd[adjRow][adjCol] = 0;
        Board bd = new Board(neighbd);
        neighBoardQ.enqueue(bd);
        // revert
        neighbd[adjRow][adjCol] = neighbd[blankRow][blankCol];
        neighbd[blankRow][blankCol] = 0;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int sz = dimension();
        int[][] twinboard = new int[sz][sz];
        for (int row = 0; row < sz; row++) {
            for (int col = 0; col < sz; col++) {
                twinboard[row][col] = board[row][col];
            }
        }
        int tileVal1 = twinboard[0][0];
        int tileVal2 = twinboard[0][1];
        int tileVal3 = twinboard[1][0];
        if (tileVal1 != 0 && tileVal2 != 0) {
            int tmp = twinboard[0][0];
            twinboard[0][0] = twinboard[0][1];
            twinboard[0][1] = tmp;
        }
        else if (tileVal2 != 0 && tileVal3 != 0) {
            int tmp = twinboard[0][1];
            twinboard[0][1] = twinboard[1][0];
            twinboard[1][0] = tmp;
        }
        else {
            int tmp = twinboard[0][0];
            twinboard[0][0] = twinboard[1][0];
            twinboard[1][0] = tmp;
        }
        Board twinbd = new Board(twinboard);
        return twinbd;
    }

    // unit testing (not graded)
    public static void main(String[] args) {

        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        StdOut.println("Size of Board : " + initial.dimension());
        StdOut.println("to String of Board : " + initial);
        StdOut.println("Haming : " + initial.hamming());
        StdOut.println("Manhattan : " + initial.manhattan());
        StdOut.println("Goal? : " + initial.isGoal());
        Iterable<Board> board = initial.neighbors();
        for (Board value : board) {
            StdOut.println("Neighbor Board : " + value);
            StdOut.println("Haming : " + value.hamming());
            StdOut.println("Manhattan : " + value.manhattan());
        }
        /* in = new In("puzzle05.txt");
        n = in.readInt();
        tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board goal = new Board(tiles);
        StdOut.println("----------------------------------");
        StdOut.println("Size of Board : " + goal.dimension());
        StdOut.println("to String of Board : " + goal);
        StdOut.println("Haming : " + goal.hamming());
        StdOut.println("Manhattan : " + goal.manhattan());
        StdOut.println("Goal? : " + goal.isGoal());
        StdOut.println("Equal? initial and goal : " + goal.equals(initial));
        Board bd1 = goal.twin();
        Board bd2 = initial.twin();
        StdOut.println("----------------------------------");
        StdOut.println("twin of goal Board : " + bd1);
        StdOut.println("twin of initial Board : " + bd2);
        StdOut.println("Equal? twins : " + bd1.equals(bd2));
        StdOut.println("----------------------------------");
        Iterable<Board> board = goal.neighbors();
        for (Board value : board) {
            StdOut.println("Neighbor Board : " + value);
        } */

    }
}
