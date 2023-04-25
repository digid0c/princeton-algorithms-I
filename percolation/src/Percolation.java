import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int openSites;
    private boolean[][] grid;
    private WeightedQuickUnionUF uf;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("N must be greater than zero!");
        }

        grid = new boolean[n][n];
        // add top and bottom virtual sites
        uf = new WeightedQuickUnionUF(n*n + 2);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        row--;
        col--;
        validate(row, col);

        if (!grid[row][col]) {
            grid[row][col] = true;
            openSites++;
            int currentIndex = calculateIndex(row, col);
            // if it is top row site then connect it to top virtual site
            if (row == 0 && currentIndex > 1) {
                uf.union(0, currentIndex);
            }
            // if it is bottom row site then connect it to bottom virtual site
            if (row == grid.length - 1) {
                uf.union(1, currentIndex);
            }
            // connect to all neighbours if they exist and open
            if (isValid(row - 1) && grid[row - 1][col]) {
                uf.union(currentIndex, calculateIndex(row - 1, col));
            }
            if (isValid(row + 1) && grid[row + 1][col]) {
                uf.union(currentIndex, calculateIndex(row + 1, col));
            }
            if (isValid(col - 1) && grid[row][col - 1]) {
                uf.union(currentIndex, calculateIndex(row, col - 1));
            }
            if (isValid(col + 1) && grid[row][col + 1]) {
                uf.union(currentIndex, calculateIndex(row, col + 1));
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        row--;
        col--;
        validate(row, col);
        return grid[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        row--;
        col--;
        validate(row, col);
        return uf.find(calculateIndex(row, col)) == uf.find(0);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
       return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(0) == uf.find(1);
    }

    private int calculateIndex(int row, int col) {
        // increase index by 2 as 0-index is top virtual site and 1-index is bottom virtual site
        return row * grid.length + col + 2;
    }

    private void validate(int row, int col) {
        if (!isValid(row)) {
            throw new IllegalArgumentException("Row index is outside of its prescribed range!");
        }
        if (!isValid(col)) {
            throw new IllegalArgumentException("Column index is outside of its prescribed range!");
        }
    }

    private boolean isValid(int param) {
        return param >= 0 && param < grid.length;
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation p = new Percolation(3);
        p.open(3, 1);
        p.open(1, 1);
        p.open(3, 3);
        p.open(1, 3);
        System.out.println(p.isOpen(2, 1));
        System.out.println(p.isFull(2, 1));
        System.out.println(p.percolates());
        System.out.println(p.isOpen(3, 1));
        System.out.println(p.isFull(3, 1));

        p.open(2, 1);
        System.out.println(p.percolates());
    }
}
