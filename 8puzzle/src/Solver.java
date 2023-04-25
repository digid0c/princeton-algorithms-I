import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Solver {

    private SearchNode solution;

    private class SearchNode implements Comparable<SearchNode> {

        private Board board;
        private SearchNode previousNode;
        private int moves;
        private int priority;

        private SearchNode(Board board, SearchNode previousNode, int moves) {
            this.board = board;
            this.previousNode = previousNode;
            this.moves = moves;
            this.priority = moves + board.manhattan();
        }

        public int compareTo(SearchNode other) {
            return this.priority - other.priority;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("Initial board cannot be null");
        }

        MinPQ<SearchNode> mainPriorityQueue = new MinPQ<>();
        MinPQ<SearchNode> twinPriorityQueue = new MinPQ<>();

        mainPriorityQueue.insert(new SearchNode(initial, null, 0));
        twinPriorityQueue.insert(new SearchNode(initial.twin(), null, 0));

        while (true) {
            SearchNode mainSearchNode = mainPriorityQueue.delMin();
            if (mainSearchNode.board.isGoal()) {
                solution = mainSearchNode;
                break;
            }
            SearchNode twinSearchNode = twinPriorityQueue.delMin();
            if (twinSearchNode.board.isGoal()) {
                break;
            }

            for (Board board : mainSearchNode.board.neighbors()) {
                if (mainSearchNode.previousNode == null || !board.equals(mainSearchNode.previousNode.board)) {
                    mainPriorityQueue.insert(new SearchNode(board, mainSearchNode, mainSearchNode.moves + 1));
                }
            }
            for (Board board : twinSearchNode.board.neighbors()) {
                if (twinSearchNode.previousNode == null || !board.equals(twinSearchNode.previousNode.board)) {
                    twinPriorityQueue.insert(new SearchNode(board, twinSearchNode, twinSearchNode.moves + 1));
                }
            }
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solution != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return solution != null ? solution.moves : -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (solution == null) {
            return null;
        }

        List<Board> boards = new ArrayList<>();
        SearchNode searchNode = solution;
        while (searchNode != null) {
            boards.add(searchNode.board);
            searchNode = searchNode.previousNode;
        }

        Collections.reverse(boards);
        return boards;
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

        System.out.println(initial.manhattan());

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
