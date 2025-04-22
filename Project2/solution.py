class Space:
    def __init__(self, value=0, points=0, max_points=0, neighbors = []):
        self.value = value
        self.points = points
        self.max_points = max_points
        self.neighbors = neighbors


class Board:
    def __init__(self):
        self.n = 0
        self.spaces = {}

    def read_board(self, filename):
        with open(filename, 'r') as f:
            # Read total number of spaces
            self.n = int(f.readline().strip())
        
            # Read each space configuration
            for _ in range(self.n):
                line = f.readline().strip().split()
            
                # Convert all values to integers
                numbers = [int(x) for x in line]
            
                space_value = numbers[0]          # Value written in the space
                points = numbers[1]         # Points worth (1 or 2)
                num_neighbors = numbers[2]        # Number of neighbors
                neighbors = numbers[3:]     # List of neighbor indices
            
                # Validate number of neighbors matches the specified count
                if len(neighbors) != num_neighbors:
                    raise ValueError(f"Mismatch in number of neighbors: expected {num_neighbors}, got {len(neighbors)}")
 
                space = Space(value=space_value, points=points, neighbors=neighbors)
                self.spaces[space.value] = space

    def get_space(self, space_id):
        return self.spaces[space_id]

    def print_board(self):
        print(f"Total spaces: {self.n}")
        for i, key in enumerate(self.spaces.keys(), 1):
            print(f"Space {i}:")
            print(f"  Value: {self.spaces[key].value}")
            print(f"  Points: {self.spaces[key].points}")
            print(f"  Neighbors: {self.spaces[key].neighbors}")
    
    def write_board(self, filename):
        (maximum_score, path) = board.maximum_score()
        with open(filename, 'w') as f:
            f.write(str(maximum_score) + "\n")
            f.write(" ".join(map(str, path)) + "\n")
        
        print(f"\nMaximum score: {maximum_score}")
        print(" ".join(map(str, path)))

    def maximum_score(self):
        dp = {}

        def get_score(space):
            # If we've already computed this state, return it
            if space.value in dp.keys():
                return dp[space.value]
            
            # Try all possible paths through neighbors
            max_score = space.points

            for neighbor_id in space.neighbors:
                neighbor = self.get_space(neighbor_id)
                if space.value > neighbor.value:
                    neighbor_score = get_score(neighbor)
                    max_score = max(max_score, space.points + neighbor_score)
            
            dp[space.value] = max_score
            return max_score
    
        # Try starting from each space
        path = [get_score(self.spaces[space_id]) for space_id in self.spaces]
        return(max(path), path)


if __name__ == "__main__":
    board = Board()
    board.read_board("small-input.txt")
    board.print_board()
    board.write_board("small-output.mine.txt")
