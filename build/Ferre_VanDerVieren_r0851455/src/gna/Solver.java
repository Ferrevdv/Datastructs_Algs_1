package gna;

import libpract.PriorityFunc;

import java.util.*;

public class Solver {

    public List<Board> solutionBoards = new ArrayList<Board>();

    /**
     * Finds a solution to the initial board.
     *
     * @param priority is either PriorityFunc.HAMMING or PriorityFunc.MANHATTAN
     */

    // Use the given priority function (either PriorityFunc.HAMMING
    // or PriorityFunc.MANHATTAN) to solve the puzzle.
    public Solver(Board initial, PriorityFunc priority) {
        Board solution = null;
        int moves = 0;
        if (priority == PriorityFunc.HAMMING) {
            PriorityQueue<Board> queue = new PriorityQueue<Board>(new HammingComparator());
            queue.add(initial);
            while (queue.peek().hamming() != 0) {
                Board currentMinBoard = queue.poll();            // board with min. priority
                Collection<Board> neighbors = currentMinBoard.neighbors();
                for (Board board : neighbors) {
                    if (currentMinBoard.getPreviousBoard() == null || !(currentMinBoard.getPreviousBoard().equals(board))) {
                        Board neighbor = new Board(board.getTiles(), currentMinBoard);
                        queue.add(neighbor);
                    }
                }
                solution = queue.peek();
                moves = queue.peek().getMoves();
            }

        } else if (priority == PriorityFunc.MANHATTAN) {
            PriorityQueue<Board> queue = new PriorityQueue<Board>(new ManhattanComparator());
            queue.add(initial);
            while (queue.peek().manhattan() != 0) {
                Board currentMinBoard = queue.poll();            // board with min. priority
                Collection<Board> neighbors = currentMinBoard.neighbors();
                for (Board board : neighbors) {
                    if (currentMinBoard.getPreviousBoard() == null || !(currentMinBoard.getPreviousBoard().equals(board))) {
                        Board neighbor = new Board(board.getTiles(), currentMinBoard);
                        queue.add(neighbor);
                    }
                }
                solution = queue.peek();
                moves = queue.peek().getMoves();
            }
        } else {
            throw new IllegalArgumentException("Priority function not supported");
        }

        Board current = solution;
        while (current.getPreviousBoard() != null) {
            solutionBoards.add(0, current);
            current = current.getPreviousBoard();
        }
        solutionBoards.add(0, current);
    }

    // Comparator classes for the priority functions: descending order in priority queue!
    static class HammingComparator implements Comparator<Board> {
        public int compare(Board board1, Board board2) {
            if (board1.getHammingPriority() + board1.getMoves() > board2.getHammingPriority() + board2.getMoves()) {
                return 1;
            }
            if (board1.getHammingPriority() + board1.getMoves() < board2.getHammingPriority() + board2.getMoves()) {
                return -1;
            }
            return 0;
        }
    }

    static class ManhattanComparator implements Comparator<Board> {
        public int compare(Board board1, Board board2) {
            if (board1.getManhattanPriority() + board1.getMoves() > board2.getManhattanPriority() + board2.getMoves()) {
                return 1;
            }
            if (board1.getManhattanPriority() + board1.getMoves() < board2.getManhattanPriority() + board2.getMoves()) {
                return -1;
            }
            return 0;
        }
    }

    /**
     * Returns a List of board positions as the solution. It should contain the initial
     * Board as well as the solution (if these are equal only one Board is returned).
     */
    public List<Board> solution() {
        return solutionBoards;
    }
}


