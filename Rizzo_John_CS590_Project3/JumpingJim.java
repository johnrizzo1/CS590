/*
 * CS590: Project 3 - Graph modeling and graph algorithms
 * Student: John Rizzo
 * Professor: Dr. William Hendricks
 * Due: May 7, 2025
 */
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
        String outputFile = (args.length > 1 && args[1] != null && !args[1].isEmpty()) ? args[1] : "jim-output.txt";

        // Utility class to read/print the file
        JumpingJim jumpingJim = new JumpingJim();
        int[][] board;
        try {
            board = jumpingJim.readInput(filename);
            jumpingJim.printBoard(board);

            BoardDFS dfs = new BoardDFS(board);
            dfs.dfs(0, 0);
            // We need to reverse the string and add spaces
            String path = (new StringBuilder(dfs.path).reverse().toString());
            String spacedPath = String.join(" ", path.split(""));
            System.out.println("DFS Path: [" + spacedPath + "]");

            // Write spacedPath to output file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                writer.write(spacedPath);
                writer.newLine();
            } catch (IOException e) {
                System.err.println("Error writing output file: " + outputFile);
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.err.println("Error reading input file: " + filename);
            e.printStackTrace();
        }
    }
}