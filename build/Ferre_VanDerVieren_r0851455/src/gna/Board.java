package gna;

import java.util.*;

public class Board {

    private int[][] tiles;
    private final int N;
    private int[] zeroCoordinates;

    private int moves;
    private Board previousBoard;
    private int hammingPriority;
    private int manhattanPriority;

    public int getHammingPriority() {
        return hammingPriority;
    }

    public int getManhattanPriority() {
        return manhattanPriority;
    }

    public int getMoves() {
        return moves;
    }

    public int[][] getTiles() {
        return tiles.clone();
    }

    public int getValue(int x, int y) {
        return tiles[x][y];
    }

    public int[] getZeroCoordinates() {
        return zeroCoordinates.clone();
    }

    public Board getPreviousBoard() {
        return previousBoard;
    }

    private void setValue(int x, int y, int value) {
        tiles[x][y] = value;
    }

    private void setZeroCoordinates(int x, int y) {
        zeroCoordinates[0] = x;
        zeroCoordinates[1] = y;
    }


    // construct a board from an N-by-N array of tiles
    public Board(int[][] tiles) {
        N = tiles.length;
        int[][] board = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                board[i][j] = tiles[i][j];
                if (board[i][j] == 0) {
                    zeroCoordinates = new int[]{i, j};
                }
            }
        }
        this.tiles = board;
        this.previousBoard = null;
        this.moves = 0;
        this.hammingPriority = this.hamming();
        this.manhattanPriority = this.manhattan();
    }

    // polymorphism: constructor for solver.java
    // this way hammingPriority and manhattanPriority doesn't have to be recalculated for every board to re-order the priority list
    public Board(int[][] tiles, Board previousBoard) {
        N = tiles.length;
        int[][] board = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                board[i][j] = tiles[i][j];
                if (board[i][j] == 0) {
                    zeroCoordinates = new int[]{i, j};
                }
            }
        }
        this.tiles = board;
        this.previousBoard = previousBoard;
        this.moves = previousBoard.getMoves() + 1;
        this.hammingPriority = this.hamming();
        this.manhattanPriority = this.manhattan();
    }

    // return number of blocks out of place
    public int hamming() {
        int totalValue = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int value = tiles[i][j];
                if (value != (N * i) + j + 1 && value != 0)
                    totalValue++;
            }
        }
        return totalValue;
    }

    // return sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int totalValue = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int value = tiles[i][j];
                int currentRow = i;
                int currentColumn = j;
                int correctRow = (int) Math.floor(value / N);
                int correctColumn = (value % N) - 1;

                // special case 1
                if (value == 0) {
                    correctRow = currentRow;
                    correctColumn = currentColumn;
                }
                // special case 2
                if (value % N == 0 && value != 0) {
                    correctRow -= 1;
                    correctColumn = N - 1;
                }
                if (correctRow != currentRow || correctColumn != currentColumn)
                    totalValue += Math.abs(currentRow - correctRow) + Math.abs(currentColumn - correctColumn);
            }
        }
        return totalValue;
    }


    // return a Collection of all neighboring board positions
    public Collection<Board> neighbors() {
        Set<Board> neighbors = new HashSet<>();
        int xCoordinate = zeroCoordinates[0];
        int yCoordinate = zeroCoordinates[1];

        if (xCoordinate > 0) {
            Board board = new Board(tiles);
            board.setValue(xCoordinate, yCoordinate, getValue(xCoordinate - 1, yCoordinate));
            board.setValue(xCoordinate - 1, yCoordinate, 0);
            neighbors.add(board);
        }

        if (xCoordinate < N - 1) {
            Board board = new Board(tiles);
            board.setValue(xCoordinate, yCoordinate, getValue(xCoordinate + 1, yCoordinate));
            board.setValue(xCoordinate + 1, yCoordinate, 0);
            neighbors.add(board);
        }

        if (yCoordinate > 0) {
            Board board = new Board(tiles);
            board.setValue(xCoordinate, yCoordinate, getValue(xCoordinate, yCoordinate - 1));
            board.setValue(xCoordinate, yCoordinate - 1, 0);
            neighbors.add(board);
        }
        if (yCoordinate < N - 1) {
            Board board = new Board(tiles);
            board.setValue(xCoordinate, yCoordinate, getValue(xCoordinate, yCoordinate + 1));
            board.setValue(xCoordinate, yCoordinate + 1, 0);
            neighbors.add(board);
        }
        return neighbors;
    }

    // is the initial board solvable? Note that the empty tile must first be moved to its correct position.
    public boolean isSolvable() {
        int[] originalPosition = new int[2];
        originalPosition[0] = getZeroCoordinates()[0];
        originalPosition[1] = getZeroCoordinates()[1];
        if (!isZeroBottomRight()) {
            // move the zero to its correct position and call isSolvable() on the newly acquired board
            while (!(this.isZeroBottomRight())) {
                int xCoordinate = this.getZeroCoordinates()[0];
                int yCoordinate = this.getZeroCoordinates()[1];
                if (xCoordinate != N - 1) {
                    // swap zero with the tile right of it
                    this.setValue(xCoordinate, yCoordinate, this.getValue(xCoordinate + 1, yCoordinate));
                    this.setValue(xCoordinate + 1, yCoordinate, 0);
                    this.setZeroCoordinates(xCoordinate + 1, yCoordinate);
                } else if (yCoordinate != N - 1) {
                    // swap zero with the tile under it
                    this.setValue(xCoordinate, yCoordinate, this.getValue(xCoordinate, yCoordinate + 1));
                    this.setValue(xCoordinate, yCoordinate + 1, 0);
                    this.setZeroCoordinates(xCoordinate, yCoordinate + 1);
                }
            }
        }
        List<Integer> stream = getStream();
        // calculate equation and return true if larger than zero, else return false
        double result = 1;
        // start from one (tile with value zero is not needed in calculation)
        for (int j = 1; j < N * N; j++)
            for (int i = 1; i < j; i++)
                result *= ((stream.indexOf(j) + 1) - (stream.indexOf(i) + 1));

        // reset board
        // move zero above
        while (getZeroCoordinates()[1] != originalPosition[1]) {
            int xCoordinate = this.getZeroCoordinates()[0];
            int yCoordinate = this.getZeroCoordinates()[1];
            this.setValue(xCoordinate, yCoordinate, this.getValue(xCoordinate, yCoordinate - 1));
            this.setValue(xCoordinate, yCoordinate - 1, 0);
            this.setZeroCoordinates(xCoordinate, yCoordinate - 1);
        }
        // move zero to left
        while (getZeroCoordinates()[0] != originalPosition[0]) {
            int xCoordinate = this.getZeroCoordinates()[0];
            int yCoordinate = this.getZeroCoordinates()[1];
            this.setValue(xCoordinate, yCoordinate, this.getValue(xCoordinate - 1, yCoordinate));
            this.setValue(xCoordinate - 1, yCoordinate, 0);
            this.setZeroCoordinates(xCoordinate - 1, yCoordinate);
        }

        return (result >= 0);
    }


    // returns stream (1D list) of the matrix (2D) containing the tiles
    private List<Integer> getStream() {
        List<Integer> stream = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                stream.add(tiles[i][j]);
            }
        }
        return stream;
    }

    // helper function for isSolvable()
    private boolean isZeroBottomRight() {
        return (zeroCoordinates[0] == N - 1 && zeroCoordinates[1] == N - 1);
    }

    // return a string representation of the board
    public String toString() {
        String result = "\n";
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++)
                result += " " + tiles[i][j];
            result += "\n";
        }
        return result;
    }

    // Does this board equal y. Two boards are equal when they both were constructed
    // using tiles[][] arrays that contained the same values.
    @Override
    public boolean equals(Object y) {
        if (!(y instanceof Board))
            return false;

        Board other = (Board) y;
        return Arrays.deepEquals(tiles, other.tiles);
    }

    // Since we override equals(), we must also override hashCode(). When two objects are
    // equal according to equals() they must return the same hashCode.
    @Override
    public int hashCode() {
        return Arrays.deepHashCode(tiles);
    }

}

