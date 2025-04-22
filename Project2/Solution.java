import java.io.*;
import java.util.*;

class Space {
    int value;
    int points;
    int maxPoints;
    List<Integer> neighbors;
    
    public Space(int value, int points, List<Integer> neighbors) {
        this.value = value;
        this.points = points;
        this.maxPoints = 0;
        this.neighbors = neighbors;
    }
}

class Board {
    int n;
    Map<Integer, Space> spaces;
    
    public Board() {
        n = 0;
        spaces = new HashMap<>();
    }
    
    public void readBoard(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        
        // Read total number of spaces
        n = Integer.parseInt(reader.readLine().trim());
        
        // Read each space configuration
        for (int i = 0; i < n; i++) {
            String[] line = reader.readLine().trim().split("\\s+");
            
            // Convert all values to integers
            int[] numbers = new int[line.length];
            for (int j = 0; j < line.length; j++) {
                numbers[j] = Integer.parseInt(line[j]);
            }
            
            int spaceValue = numbers[0];     // Value written in the space
            int points = numbers[1];         // Points worth (1 or 2)
            int numNeighbors = numbers[2];   // Number of neighbors
            
            // Extract neighbors
            List<Integer> neighbors = new ArrayList<>();
            for (int j = 3; j < numbers.length; j++) {
                neighbors.add(numbers[j]);
            }
            
            // Validate number of neighbors matches the specified count
            if (neighbors.size() != numNeighbors) {
                throw new IllegalArgumentException("Mismatch in number of neighbors: expected " + 
                                                  numNeighbors + ", got " + neighbors.size());
            }
            
            Space space = new Space(spaceValue, points, neighbors);
            spaces.put(space.value, space);
        }
        
        reader.close();
    }
    
    public Space getSpace(int spaceId) {
        return spaces.get(spaceId);
    }
    
    public void printBoard() {
        System.out.println("Total spaces: " + n);
        int i = 1;
        for (Integer key : spaces.keySet()) {
            System.out.println("Space " + i + ":");
            System.out.println("  Value: " + spaces.get(key).value);
            System.out.println("  Points: " + spaces.get(key).points);
            System.out.println("  Neighbors: " + spaces.get(key).neighbors);
            i++;
        }
    }
    
    public void writeBoard(String filename) throws IOException {
        int[] result = maximumScore();
        int maximumScore = result[0];
        int[] path = Arrays.copyOfRange(result, 1, result.length);
        
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        writer.write(String.valueOf(maximumScore));
        writer.newLine();
        
        StringBuilder sb = new StringBuilder();
        for (int val : path) {
            sb.append(val).append(" ");
        }
        writer.write(sb.toString().trim());
        writer.newLine();
        writer.close();
        
        System.out.println("\nMaximum score: " + maximumScore);
        System.out.println(sb.toString().trim());
    }
    
    public int[] maximumScore() {
        Map<Integer, Integer> dp = new HashMap<>();
        
        // Get score function - implemented as a separate method since Java doesn't support nested functions
        for (Integer spaceId : spaces.keySet()) {
            getScore(spaces.get(spaceId), dp);
        }
        
        // Try starting from each space
        List<Integer> pathList = new ArrayList<>();
        for (Integer spaceId : spaces.keySet()) {
            pathList.add(getScore(spaces.get(spaceId), dp));
        }
        
        int max = Collections.max(pathList);
        
        // Create result array with max score followed by path
        int[] result = new int[pathList.size() + 1];
        result[0] = max;
        for (int i = 0; i < pathList.size(); i++) {
            result[i + 1] = pathList.get(i);
        }
        
        return result;
    }
    
    private int getScore(Space space, Map<Integer, Integer> dp) {
        // If we've already computed this state, return it
        if (dp.containsKey(space.value)) {
            return dp.get(space.value);
        }
        
        // Try all possible paths through neighbors
        int maxScore = space.points;
        
        for (Integer neighborId : space.neighbors) {
            Space neighbor = getSpace(neighborId);
            if (space.value > neighbor.value) {
                int neighborScore = getScore(neighbor, dp);
                maxScore = Math.max(maxScore, space.points + neighborScore);
            }
        }
        
        dp.put(space.value, maxScore);
        return maxScore;
    }
}

public class Solution {
    public static void main(String[] args) {
        try {
            Board board = new Board();
            board.readBoard("small-input.txt");
            board.printBoard();
            board.writeBoard("small-output.mine.txt");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}