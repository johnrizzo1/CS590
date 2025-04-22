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
    Map<Integer, Integer> parent = new HashMap<>(); // key: current space, value: previous space

    
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
    parent.clear(); // make sure it's clean

    // First compute dp for all nodes
    for (Integer spaceId : spaces.keySet()) {
        getScore(spaceId, dp);
    }

    // Find the node with the max score
    int maxScore = 0;
    int endNode = -1;
    for (Integer spaceId : spaces.keySet()) {
        if (dp.get(spaceId) > maxScore) {
            maxScore = dp.get(spaceId);
            endNode = spaceId;
        }
    }

    // Reconstruct the path
    List<Integer> pathList = new ArrayList<>();
    while (endNode != -1) {
        pathList.add(endNode);
        endNode = parent.getOrDefault(endNode, -1);
    }
    Collections.reverse(pathList);

    // Prepare result array
    int[] result = new int[pathList.size() + 1];
    result[0] = maxScore;
    for (int i = 0; i < pathList.size(); i++) {
        result[i + 1] = pathList.get(i);
    }

    return result;
}
    
    private int getScore(int spaceId, Map<Integer, Integer> dp) {
    if (dp.containsKey(spaceId)) {
        return dp.get(spaceId);
    }

    Space space = getSpace(spaceId);
    int maxScore = space.points;
    int bestPrev = -1;

    for (Integer neighborId : space.neighbors) {
        Space neighbor = getSpace(neighborId);
        if (neighbor.value > space.value) {
            int neighborScore = getScore(neighborId, dp);
            if (space.points + neighborScore > maxScore) {
                maxScore = space.points + neighborScore;
                bestPrev = neighborId;
            }
        }
    }

    if (bestPrev != -1) {
        parent.put(spaceId, bestPrev);
    }

    dp.put(spaceId, maxScore);
    return maxScore;
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
