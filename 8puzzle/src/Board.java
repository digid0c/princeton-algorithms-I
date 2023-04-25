import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {

    private int[][] tiles;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        int n = tiles.length;
        this.tiles = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.tiles[i][j] = tiles[i][j];
            }
        }
    }

    // string representation of this board
    public String toString() {
        int n = dimension();
        StringBuilder s = new StringBuilder();
        s.append(n).append("\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return tiles.length;
    }

    // number of tiles out of place
    public int hamming() {
        int result = 0;
        int n = dimension();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int expectedTile = i * n + j + 1;
                if (tiles[i][j] != expectedTile && isTile(i, j)) {
                    result++;
                }
            }
        }

        return result;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int result = 0;
        int n = dimension();
        int expectedTile = 1;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] != expectedTile) {
                    List<Integer> tileCoordinates = findTileCoordinates(expectedTile);
                    int x = tileCoordinates.get(0);
                    int y = tileCoordinates.get(1);
                    int manhattan = Math.abs(i - x) + Math.abs(j - y);
                    result += manhattan;
                }
                expectedTile++;
                if (expectedTile == n * n) {
                    return result;
                }
            }
        }

        return result;
    }

    // is this board the goal board?
    public boolean isGoal() {
        int n = dimension();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int expectedTile = i * n + j + 1;
                if (tiles[i][j] != expectedTile && isTile(i, j)) {
                    return false;
                }
            }
        }

        return true;
    }

    // does this board equal y?
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;
        Board that = (Board) other;
        return this.dimension() == that.dimension() && Arrays.deepEquals(this.tiles, that.tiles);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int n = dimension();
        List<Board> result = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (!isTile(i, j)) {
                    if (j > 0) {
                        swap(i, j, i, j - 1);
                        result.add(new Board(tiles));
                        swap(i, j, i, j - 1);
                    }
                    if (j < tiles.length - 1) {
                        swap(i, j, i, j + 1);
                        result.add(new Board(tiles));
                        swap(i, j, i, j + 1);
                    }
                    if (i > 0) {
                        swap(i, j, i - 1, j);
                        result.add(new Board(tiles));
                        swap(i, j, i - 1, j);
                    }
                    if (i < tiles.length - 1) {
                        swap(i, j, i + 1, j);
                        result.add(new Board(tiles));
                        swap(i, j, i + 1, j);
                    }
                    return result;
                }
            }
        }

        return result;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int i1 = -1;
        int i2 = -1;
        int j1 = -1;
        int j2 = -1;
        int n = dimension();
        Board result = null;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (isTile(i, j)) {
                    if (i1 == -1) {
                        i1 = i;
                        j1 = j;
                    } else if (i2 == -1) {
                        i2 = i;
                        j2 = j;
                    } else {
                        swap(i1, j1, i2, j2);
                        result = new Board(tiles);
                        swap(i1, j1, i2, j2);
                        return result;
                    }
                }
            }
        }

        return result;
    }

    private boolean isTile(int i, int j) {
        return tiles[i][j] != 0;
    }

    private List<Integer> findTileCoordinates(int expectedTile) {
        List<Integer> result = new ArrayList<>();
        int n = dimension();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == expectedTile) {
                    result.add(i);
                    result.add(j);
                    return result;
                }
            }
        }
        return result;
    }

    private void swap(int i1, int j1, int i2, int j2) {
        int temp = tiles[i1][j1];
        tiles[i1][j1] = tiles[i2][j2];
        tiles[i2][j2] = temp;
    }

    // unit testing (not graded)
    public static void main(String[] args) {

    }

}
