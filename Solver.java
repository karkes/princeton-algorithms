/* *****************************************************************************
 *  Name: Kartik Kesarwani
 *  Date: 09/05/2020 DD/MM/YYYY
 *  Description: Algorithm to solve a puzzle board
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public final class Solver {

    private final class SearchNode implements Comparable<SearchNode> {
        private final SearchNode prevNode;
        private final Board currBoard;
        private final int moveCount;
        private final int manDist;

        public SearchNode(SearchNode prev, Board curr, int c) {
            prevNode = prev;
            currBoard = curr;
            moveCount = c;
            manDist = currBoard.manhattan();
        }

        public int compareTo(SearchNode other) {
            int manhattanPriorityFuncOther = other.moveCount + other.manDist;
            int manhattanPriorityFuncThis = this.moveCount + this.manDist;

            if (manhattanPriorityFuncThis != manhattanPriorityFuncOther)
                return manhattanPriorityFuncThis - manhattanPriorityFuncOther;

            return this.manDist - other.manDist;
        }

        public boolean isGoal() {
            return currBoard.isGoal();
        }

        public Board getCurrBoard() {
            return currBoard;
        }

        public int getMoveCount() {
            return moveCount;
        }

        public SearchNode getPrevNode() {
            return prevNode;
        }
    }

    private final Stack<Board> solnStack;
    private final int moves;
    private final boolean solvable;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("wrong argument");
        Board twinbd = initial.twin();
        solnStack = new Stack<>();
        Board startBoard = solveBoardAlgo(initial, twinbd);

        if (startBoard == twinbd) {
            solvable = false;
            moves = -1;
        }
        else {
            solvable = true;
            moves = solnStack.size() - 1;
        }
    }

    private Board solveBoardAlgo(Board initial, Board twinbd) {
        MinPQ<SearchNode> gameStatePQ = new MinPQ<>();
        SearchNode node = new SearchNode(null, initial, 0);
        SearchNode twinnode = new SearchNode(null, twinbd, 0);
        gameStatePQ.insert(node);
        gameStatePQ.insert(twinnode);
        SearchNode currMinNode = gameStatePQ.delMin();
        // StdOut.println("Game board : " + currMinNode.showCurrBoard());

        while (!currMinNode.isGoal()) {
            for (Board neighbd : currMinNode.getCurrBoard().neighbors()) {
                if (currMinNode.getPrevNode() == null || !neighbd
                        .equals(currMinNode.getPrevNode().getCurrBoard())) {
                    gameStatePQ.insert(new SearchNode(currMinNode, neighbd,
                                                      currMinNode.getMoveCount() + 1));
                }
            }
            currMinNode = gameStatePQ.delMin();
            // StdOut.println("Size of PQ : " + gameStatePQ.size());
            // StdOut.println("Game board : " + currMinNode.showCurrBoard());
        }
        Board startBoard = addSolutionToStack(currMinNode);
        return startBoard;

        // StdOut.println("----------------Start Board-------------------");
        // StdOut.println(startBoard);
        // StdOut.println("----------------------------------------------");
    }

    private Board addSolutionToStack(SearchNode lastNode) {
        Board startBoard = null;
        while (lastNode != null) {
            if (lastNode.prevNode == null) {
                startBoard = lastNode.getCurrBoard();
            }
            solnStack.push(lastNode.getCurrBoard());
            lastNode = lastNode.prevNode;
        }
        return startBoard;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        if (isSolvable()) {
            return solnStack;
        }
        return null;
    }

    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable()) {
            StdOut.println("No solution possible");
            StdOut.println("Minimum number of moves = " + solver.moves());
        }
        else {
            StdOut.println("----------------------SOLUTION------------------------");
            StdOut.println("Minimum number of moves = " + solver.moves());
        }
        if (solver.solution() != null) {
            for (Board bd : solver.solution()) {
                StdOut.println(bd);
            }
        }

    }
}
