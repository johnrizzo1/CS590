/*
 * Course: CS590
 * Professor: William Hendricks
 * Authors: Ao Wang, Yiyang Gao, John Rizzo
 * 
 * Project 2
 * Due: 4/23/2025
 */

import java.io.*;
import java.util.*;


public class Solution {
    static class Node {
        public int id; 
        public int value; 
        public int point; 
        public int childCnt; 
        public List<Integer> neighbors;

        public Node(int id, int point, int childCnt, List<Integer> neighbors) {
            this.id = id;
            this.point = point;
            this.childCnt = childCnt;
            this.neighbors = neighbors;
        }

        public Node(int id, int value, int point, int childCnt, List<Integer> neighbors) {
            this.id = id;
            this.value = value;
            this.point = point;
            this.childCnt = childCnt;
            this.neighbors = neighbors;
        }
    }

    private static int[] memo; 
    private static int[] prev; 
    private static final Map<Integer, Node> nodeMap = new HashMap<>(); 

    public static void main(String[] args) {
        try {
            // read node data
            BufferedReader br = new BufferedReader(new FileReader("example-input.txt"));
            int n = Integer.parseInt(br.readLine());// Total number of nodes
            List<Node> nodeList = new ArrayList<Node>();

            for (int i = 0; i < n; i++) {
                String[] split = br.readLine().split("\\s+");
                int value = Integer.parseInt(split[0]);
                int point = Integer.parseInt(split[1]);
                int childCnt = Integer.parseInt(split[2]);
                List<Integer> neighbors = new ArrayList<>();
                for (int j = 0; j < childCnt; j++) {
                    neighbors.add(Integer.parseInt(split[3+j]));
                }
                Node node = new Node(i+1, value, point, childCnt, neighbors);
                nodeList.add(node);
                nodeMap.put(i+1, node);
            }
            br.close();

            // Initialize DP arrays and predecessor array
            memo = new int[n+1];
            prev = new int[n+1];
            Arrays.fill(memo, -1);
            Arrays.fill(prev, -1);

            // Calculate MaxScore
            int maxScore = 0;
            int maxNodeId = 0;
            for (Node node : nodeList) {
                int currentScore = maxScore(node.id);
                if (currentScore > maxScore) {
                    maxScore = currentScore;
                    maxNodeId = node.id;
                }
            }

            // Reconstruct the optimal path
            List<Node> path = new ArrayList<>();
            int curNodeId = maxNodeId;
            while(curNodeId != -1) {
                path.add(nodeMap.get(curNodeId));
                curNodeId = prev[curNodeId];
            }
            Collections.reverse(path);

            // Write results to output file
            BufferedWriter bw = new BufferedWriter(new FileWriter("example-output.txt"));
            bw.write(String.valueOf(maxScore));
            bw.newLine();
            for (Node node : path) {
                bw.write(node.value + " ");
            }
            bw.close();


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Recursive memorization function
    private static int maxScore(int nodeId) {
        if (memo[nodeId] != -1) {
            return memo[nodeId];
        }

        Node node = nodeMap.get(nodeId);
        int maxVal = node.point; 

        for (int neighborId : node.neighbors) {
            Node neighbor = nodeMap.get(neighborId);
            if (neighbor.value < node.value) {
                int current = maxScore(neighborId) + node.point;
                if (current > maxVal) {
                    maxVal = current;
                    prev[nodeId] = neighborId; // Update the predecessor node
                }
            }
        }

        memo[nodeId] = maxVal; 
        return maxVal;
    }

}
