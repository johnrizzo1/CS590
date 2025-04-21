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
    Calculates the maximum score achievable on the board using an iterative approach.
    
    Args:
        spaces (list): List of tuples (value, points, neighbors) for each space
        
    Returns:
        int: Maximum score achievable
    """
    n = len(spaces)
    # dp[i][j] represents max score starting at space i with previous value j
    # -1 represents no previous value
    dp = [-1 for _ in range(n)]
    for i in range(n):
        dp[i] = {-1: 0}
    max_value = max(value for value, _, _ in spaces)
    
    def get_score(space_index, prev_value=-1):
        # If we've already computed this state, return it
        if dp[space_index] is not -1:
            return dp[space_index]
            
        value, points, neighbors = spaces[space_index]
        
        # If current value matches previous value, this path is invalid
        # if prev_value == value:
        #     dp[space_index][prev_value] = 0
        #     return 0
            
        # Try all possible paths through neighbors
        max_score = points  # Start with just this space's points
        for neighbor in neighbors:
            neighbor_score = get_score(neighbor, value)
            max_score = max(max_score, points + neighbor_score)
            print(f'Space [{space_index}]\'s score: {max_score}')
            print(f'{space_index}\'s Neighbor: {neighbor}')
            print(f'Neighbor [{neighbor}]\'s score: {neighbor_score}')
            
        dp[space_index][prev_value] = max_score
        return max_score
    
    # Try starting from each space
    return max(get_score(i) for i in range(n))


if __name__ == "__main__":
    n, spaces = read_board("Project2/example-input.txt")
    print(f"Total spaces: {n}")
    for i, (value, points, neighbors) in enumerate(spaces, 1):
        print(f"Space {i}:")
        print(f"  Value: {value}")
        print(f"  Points: {points}")
        print(f"  Neighbors: {neighbors}")

    print(f"\nMaximum score: {maximum_score(spaces)}")
