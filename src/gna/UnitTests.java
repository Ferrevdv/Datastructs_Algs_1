package gna;

import libpract.PriorityFunc;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

/**
 * A number of JUnit tests for Solver.
 * <p>
 * Feel free to modify these to automatically test puzzles or other functionality
 */
public class UnitTests {

    @Test
    public void test() {

        // constructor and getters
        int[][] testPuzzle = {{8, 1, 3},
                {4, 0, 2},
                {7, 6, 5}};
        Board testBoard = new Board(testPuzzle);
        assert Arrays.deepEquals(testBoard.getTiles(), testPuzzle);
        assert Arrays.equals(testBoard.getZeroCoordinates(), new int[]{1, 1});
        assert testBoard.getMoves() == 0;
        assert testBoard.getValue(1, 0) == 4;
        assert testBoard.getValue(1, 1) == 0;
        assert testBoard.isSolvable();

        int[][] testPuzzleOneMove = {{8, 1, 3},
                {0, 4, 2},
                {7, 6, 5}};
        // make a new board and set its previous board
        Board testBoardOneMove = new Board(testPuzzleOneMove, testBoard);
        assert Arrays.deepEquals(testBoardOneMove.getPreviousBoard().getTiles(), testPuzzle);
        assert testBoardOneMove.getMoves() == 1;

        // neighbors()
        Collection<Board> neighbors = testBoard.neighbors();
        int[][] neighborOneBoard = {{8, 1, 3}, {0, 4, 2}, {7, 6, 5}};
        int[][] neighborTwoBoard = {{8, 1, 3}, {4, 2, 0}, {7, 6, 5}};
        int[][] neighborThreeBoard = {{8, 0, 3}, {4, 1, 2}, {7, 6, 5}};
        int[][] neighborFourBoard = {{8, 1, 3}, {4, 6, 2}, {7, 0, 5}};
        Board neighborOne = new Board(neighborOneBoard);
        Board neighborTwo = new Board(neighborTwoBoard);
        Board neighborThree = new Board(neighborThreeBoard);
        Board neighborFour = new Board(neighborFourBoard);
        assert neighbors.contains(neighborOne);
        assert neighbors.contains(neighborTwo);
        assert neighbors.contains(neighborThree);
        assert neighbors.contains(neighborFour);
        assert neighbors.size() == 4;

        // Hamming + getMoves()
        int[][] testPuzzle28 = {{7, 8, 5},
                {4, 0, 2},
                {3, 6, 1}};
        Board testBoard28 = new Board(testPuzzle28);
        int[][] testPuzzle28OneMove = {{7, 8, 5},
                {0, 4, 2},
                {3, 6, 1}};
        Board testBoard28OneMove = new Board(testPuzzle28OneMove, testBoard28);
        assert testBoard28OneMove.getHammingPriority() + testBoard28OneMove.getMoves() == 9;
        int[][] testPuzzle28TwoMoves = {{7, 8, 5},
                {3, 4, 2},
                {0, 6, 1}};
        Board testBoard28TwoMoves = new Board(testPuzzle28TwoMoves, testBoard28OneMove);
        assert testBoard28TwoMoves.getHammingPriority() + testBoard28TwoMoves.getMoves() == 10;

        // Manhattan + getMoves()
        int[][] testPuzzle04 = {{0, 1, 3},
                {4, 2, 5},
                {7, 8, 6}};
        Board testBoard04 = new Board(testPuzzle04);
        int[][] testPuzzle04OneMove = {{1, 0, 3},
                {4, 2, 5},
                {7, 8, 6}};
        Board testBoard04OneMove = new Board(testPuzzle04OneMove, testBoard04);
        int[][] testPuzzle04TwoMoves = {{1, 2, 3},
                {4, 0, 5},
                {7, 8, 6}};
        Board testBoard04TwoMoves = new Board(testPuzzle04TwoMoves, testBoard04OneMove);
        assert testBoard04TwoMoves.getManhattanPriority() + testBoard04TwoMoves.getMoves() == (2 + 2);

        // manhattan() and hamming() for first testBoard
        assert testBoard.getHammingPriority() == 5;
        assert testBoard.getManhattanPriority() == 10;
        assert testBoardOneMove.getHammingPriority() + testBoardOneMove.getMoves() == 7;
        assert testBoardOneMove.getManhattanPriority() + testBoardOneMove.getMoves() == 12;


        // isSolvable()
        int[][] testPuzzle20 = {{1, 2, 3, 4, 5, 7, 14},
                {8, 9, 10, 11, 12, 13, 6},
                {15, 16, 17, 18, 19, 20, 21},
                {22, 23, 24, 25, 26, 27, 28},
                {29, 30, 31, 32, 0, 33, 34},
                {36, 37, 38, 39, 40, 41, 35},
                {43, 44, 45, 46, 47, 48, 42}};
        Board testBoard20 = new Board(testPuzzle20);
        assert testBoard20.isSolvable();
        int[][] testPuzzle30 = {{8, 4, 7},
                {1, 5, 6},
                {3, 2, 0}};
        Board testBoard30 = new Board(testPuzzle30);
        assert testBoard30.isSolvable();

        int[][] impossiblePuzzle = {{1, 2, 3},
                {4, 6, 5},
                {7, 8, 0}};
        Board impossibleBoard = new Board(impossiblePuzzle);
        assert !(impossibleBoard.isSolvable());


        // Solver (for both given 3x3 and 4x4 boards)
        Solver solver30 = new Solver(testBoard30, PriorityFunc.MANHATTAN);
        assert solver30.solution().size() == 31;    // correct amount of moves
        int[][] solution3x3 = {{1, 2, 3},
                {4, 5, 6},
                {7, 8, 0}};
        assert solver30.solution().get(30).equals(new Board(solution3x3));  // correct solution

        Solver solver28 = new Solver(testBoard28, PriorityFunc.MANHATTAN);
        assert solver28.solution().size() == 29; // correct amount of moves
        assert solver28.solution().get(28).equals(new Board(solution3x3)); // correct solution

        int[][] solution4x4 = {{1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 0}};

        int[][] testPuzzle32 = {{3, 1, 6, 4},
                {5, 0, 9, 7},
                {10, 2, 11, 8},
                {13, 15, 14, 12}};
        Board testBoard32 = new Board(testPuzzle32);
        Solver solver32 = new Solver(testBoard32, PriorityFunc.MANHATTAN);
        assert solver32.solution().size() == 33; // correct amount of moves
        assert solver32.solution().get(32).equals(new Board(solution4x4)); // correct solution

    }
}
