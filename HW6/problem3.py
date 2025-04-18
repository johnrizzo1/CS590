"""
Problem 3: Shortest Rook Path
"""
from queue import PriorityQueue


class Point:
        def __init__(self, x, y):
            self.x = x
            self.y = y

        def __repr__(self):
            return f"Point({self.x}, {self.y})"

        def __eq__(self, other):
            return self.x == other.x and self.y == other.y

        def __hash__(self):
            return hash((self.x, self.y))


def shortestRookPath(points):
    start = points[0]
    end = points[-1]
    
    # Build coordinate maps
    x_map = {}
    y_map = {}
    for point in points:
        if point.x not in x_map:
            x_map[point.x] = []
        x_map[point.x].append(point)        
        if point.y not in y_map:
            y_map[point.y] = []
        y_map[point.y].append(point)
    
    # Dijkstra's setup
    distances = {point: float('inf') for point in points}
    distances[start] = 0
    heap = PriorityQueue()
    heap.put((0, start))
    
    while not heap.empty():
        current_dist, current = heap.get()
        
        if current == end:
            return current_dist
            
        if current_dist > distances[current]:
            continue
            
        # Get all neighbors (same x or y)
        neighbors = []
        for point in x_map[current.x]:
            if point != current:
                neighbors.append(point)
        for point in y_map[current.y]:
            if point != current:
                neighbors.append(point)
                
        for neighbor in neighbors:
            neightbor_distance = abs(current.x - neighbor.x) + abs(current.y - neighbor.y)
            distance = current_dist + neightbor_distance
            if distance < distances[neighbor]:
                distances[neighbor] = distance
                heap.put((distance, neighbor))
    
    return float('inf')  # if no path exists

if __name__ == '__main__':
    points = [Point(0, 0), Point(10, 0), Point(10, 1),
              Point(0, 2), Point(1, 2), Point(1, 1)]
    print(shortestRookPath(points))  # Example usage