import java.io.*;
import java.util.*;

public class Solution {
    private static class Space {
        int value;
        int points;
        List<Integer> neighbors;

        Space(int value, int points, List<Integer> neighbors) {
            this.value = value;
            this.points = points;
            this.neighbors = neighbors;
        }
    }

    private static class Board {
        int n;
        List<Space> spaces;

        Board(int n, List<Space> spaces) {
            this.n = n;
            this.spaces = spaces;
        }
    }

    /**
     * Reads the board configuration from a file.
     * 
     * @param filename Path to the input file
     * @return Board object containing the configuration
     * @throws IOException If file reading fails
     */
    private static Board readBoard(String filename) throws IOException {
        List<Space> spaces = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            // Read total number of spaces
            int n = Integer.parseInt(reader.readLine().trim());
            
            // Read each space configuration
            String line;
            while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
                String[] parts = line.trim().split("\\s+");
                int[] numbers = Arrays.stream(parts)
                    .mapToInt(Integer::parseInt)
                    .toArray();
                
                int value = numbers[0];          // Value written in the space
                int points = numbers[1];         // Points worth (1 or 2)
                int numNeighbors = numbers[2];   // Number of neighbors
                
                // Get neighbors list
                List<Integer> neighbors = new ArrayList<>();
                for (int i = 0; i < numNeighbors; i++) {
                    neighbors.add(numbers[3 + i]);
                }
                
                // Validate number of neighbors
                if (neighbors.size() != numNeighbors) {
                    throw new IllegalArgumentException(
                        String.format("Mismatch in number of neighbors: expected %d, got %d",
                            numNeighbors, neighbors.size()));
                }
                
                spaces.add(new Space(value, points, neighbors));
            }
            
            return new Board(n, spaces);
        }
    }

    /**
     * Calculates the maximum score achievable on the board using dynamic programming.
     * 
     * @param board The board configuration
     * @return Maximum score achievable
     */
    private static int maximumScore(Board board) {
        int n = board.spaces.size();
        // dp[i][j] represents max score starting at space i with previous value j
        // Use HashMap to store only necessary states
        Map<Integer, Integer>[] dp = new HashMap[n];
        for (int i = 0; i < n; i++) { dp[i] = new HashMap<>(); }
        
        // Function to get score for a specific state
        class ScoreCalculator {
            int getScore(int spaceIndex, int prevValue) {
                // If we've already computed this state, return it
                if (dp[spaceIndex].containsKey(prevValue)) {
                    System.out.println("Memoized Space[" + spaceIndex + "] score [" + dp[spaceIndex].get(prevValue) + "]");
                    return dp[spaceIndex].get(prevValue);
                }

                Space space = board.spaces.get(spaceIndex);

                // If current value matches previous value memoize and return the score
                if (prevValue == space.value) {
                    System.out.println("prev=spaceval Space[" + spaceIndex + "] value [" + space.value + "] points [" + space.points + "]");
                    dp[spaceIndex].put(space.value, space.points);
                    // return space.points;
                }
                
                System.out.println("Space[" + spaceIndex + "] value [" + space.value + "] points [" + space.points + "]");
                
                // Try all possible paths through neighbors
                int maxScore = space.points;  // Start with just this space's points
                for (int neighbor : space.neighbors) {
                    System.out.println("Getting Score for neighbor [" + neighbor + "]");
                    int neighborScore = getScore(neighbor, space.value);
                    System.out.println("Neighbor[" + neighbor + "] score [" + neighborScore + "]");
                    maxScore = Math.max(maxScore, space.points + neighborScore);
                }
                
                System.out.println("Space[" + spaceIndex + "] score [" + maxScore + "]");
                dp[spaceIndex].put(prevValue, maxScore);
                return maxScore;
            }
        }
        
        // Try starting from each space
        ScoreCalculator calculator = new ScoreCalculator();
        int maxScore = 0;
        for (int i = 0; i < n; i++) {
            maxScore = Math.max(maxScore, calculator.getScore(i, -1));
        }
        
        return maxScore;
    }

    public static void main(String[] args) {
        try {
            Board board = readBoard("small-input.txt");
            System.out.printf("Total spaces: %d%n", board.n);
            
            // Print first few spaces for verification
            for (int i = 0; i < board.spaces.size(); i++) {
                Space space = board.spaces.get(i);
                System.out.printf("Space %d:%n", i);
                System.out.printf("  Value: %d%n", space.value);
                System.out.printf("  Points: %d%n", space.points);
                System.out.printf("  Neighbors: %s%n", space.neighbors);
            }
            
            int result = maximumScore(board);
            System.out.printf("%nMaximum score: %d%n", result);
            
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
            System.exit(1);
        }
    }
}