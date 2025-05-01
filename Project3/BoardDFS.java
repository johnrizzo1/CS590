import java.util.*;

public class BoardDFS {
    private int[][] board;
    private boolean[][] visited;
    private int rows, cols;
    public String path;

    public BoardDFS(int[][] board) {
        this.board = board;
        this.rows = board.length;
        this.cols = board[0].length;
        this.visited = new boolean[rows][cols];
        this.path = "";
    }

    // DFS from (row, col)
    public int dfs(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) return 0;
        if (visited[row][col]) return 0;
        visited[row][col] = true;
        // System.out.println("Visited: (" + row + ", " + col + ") [" + board[row][col] + "]");

        if (board[row][col] == 0) {
            System.out.println("Found 0 at row " + row + " and col " + col);
            return -1;
        }

        int jump = board[row][col];
        // Up W
        if (dfs(row - jump, col) == -1) {
            // System.out.print("N ");
            this.path += "N";
            return -1;
        }

        // Down E
        if (dfs(row + jump, col) == -1) {
            // System.out.print("S ");
            this.path += "S";
            return -1;
        }

        // Left N
        if (dfs(row, col - jump) == -1) {
            // System.out.print("W ");
            this.path += "W";
            return -1;
        }

        // Right S
        if (dfs(row, col + jump) == -1) {
            // System.out.print("E ");
            this.path += "E";
            return -1;
        }

        return 0;
    }

    // Optional: Reset visited for another search
    public void resetVisited() {
        for (int i = 0; i < rows; i++) {
            Arrays.fill(visited[i], false);
        }
    }
}