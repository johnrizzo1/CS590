import sys

def read_board(filename):
    """
    Reads the board configuration from a file.
    
    Args:
        filename (str): Path to the input file
        
    Returns:
        tuple: (n, spaces) where:
            - n (int): Total number of spaces on the board
            - spaces (list): List of tuples (value, points, neighbors) where:
                - value (int): Number written in the space
                - points (int): Points the space is worth
                - neighbors (list): List of connected space indices
    """
    spaces = []
    
    with open(filename, 'r') as f:
        # Read total number of spaces
        n = int(f.readline().strip())
        
        # Read each space configuration
        for _ in range(n):
            line = f.readline().strip().split()
            
            # Convert all values to integers
            numbers = [int(x) for x in line]
            
            value = numbers[0]          # Value written in the space
            points = numbers[1]         # Points worth (1 or 2)
            num_neighbors = numbers[2]  # Number of neighbors
            neighbors = numbers[3:]     # List of neighbor indices
            
            # Validate number of neighbors matches the specified count
            if len(neighbors) != num_neighbors:
                raise ValueError(f"Mismatch in number of neighbors: expected {num_neighbors}, got {len(neighbors)}")
 
            spaces.append((value, points, neighbors))
            
    return n, spaces


def maximum_score(spaces):
    """
    Calculates the maximum score achievable on the board.
    
    Args:
        spaces (list): List of tuples (value, points, neighbors) for each space
        
    Returns:
        int: Maximum score achievable
    """
    # Initialize memoization table
    memo = {}

    def dfs(space_index):
        if space_index in memo:
            return memo[space_index]

        value, points, neighbors = spaces[space_index]
        max_score = points

        for neighbor in neighbors:
            if spaces[neighbor][0] != value:
                max_score = max(max_score, points + dfs(neighbor))

        memo[space_index] = max_score
        return max_score

    # Calculate maximum score for each space and return the maximum
    return max(dfs(i) for i in range(len(spaces)))


if __name__ == "__main__":
    n, spaces = read_board("Project2/example-input.txt")
    print(f"Total spaces: {n}")
    print("\nFirst few spaces:")
    for i, (value, points, neighbors) in enumerate(spaces, 1):
        print(f"Space {i}:")
        print(f"  Value: {value}")
        print(f"  Points: {points}")
        print(f"  Neighbors: {neighbors}")

    print(f"\nMaximum score: {maximum_score(spaces)}")
