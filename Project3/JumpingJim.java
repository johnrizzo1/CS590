import java.io.*;

public class JumpingJim {
    public int[][] readInput(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String[] dims = reader.readLine().trim().split("\\s+");
        int rows = Integer.parseInt(dims[0]);
        int cols = Integer.parseInt(dims[1]);
        int[][] board = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            String[] tokens = reader.readLine().trim().split("\\s+");
            for (int j = 0; j < cols; j++) {
                board[i][j] = Integer.parseInt(tokens[j]);
            }
        }
        reader.close();
        return board;
    }

    public void printBoard(int[][] board) {
        for (int i=0; i<board.length; i++) {
            for (int j=0; j< board[i].length; j++){
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        // Take the file as input or default to jim-input.txt
        String filename = (args.length > 0 && args[0] != null && !args[0].isEmpty()) ? args[0] : "jim-input.txt";

        // Utility class to read/print the file
        JumpingJim jumpingJim = new JumpingJim();
        int[][] board;
        try {
            board = jumpingJim.readInput(filename);
            jumpingJim.printBoard(board);

            BoardDFS dfs = new BoardDFS(board);
            dfs.dfs(0, 0);
            System.out.println("DFS Path: [" + new StringBuilder(dfs.path).reverse().toString() + "]");
            // dfs.resetVisited();
        } catch (IOException e) {
            System.err.println("Error reading input file: " + filename);
            e.printStackTrace();
        }
    }
}