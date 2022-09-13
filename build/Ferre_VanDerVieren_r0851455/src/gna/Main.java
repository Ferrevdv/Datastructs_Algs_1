package gna;

import libpract.PriorityFunc;
import libpract.StdIn;

class Main {
    public static void main(String[] args) {
        int N = StdIn.readInt();
        int[][] tiles = new int[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++)
                tiles[i][j] = StdIn.readInt();
        }

        Board initial = new Board(tiles);
        if (!initial.isSolvable()) {
            System.out.println("No solution possible");
        } else {
//            double startTime = System.currentTimeMillis();
            Solver solver = new Solver(initial, PriorityFunc.MANHATTAN);
//            double endTime = System.currentTimeMillis();
//            double elapsedTime = (endTime - startTime) / 1000.0;

            for (Board board : solver.solution())
                System.out.println(board);

            System.out.println("Minimum number of moves = " + Integer.toString(solver.solution().size() - 1));
//            System.out.println("Time taken: " + elapsedTime);
        }


    }
}


