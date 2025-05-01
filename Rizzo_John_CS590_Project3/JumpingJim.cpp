// JumpingJim.cpp
// C++ version of JumpingJim and BoardDFS using Boost for filesystem and string utilities

#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <sstream>
#include <boost/algorithm/string.hpp>
#include <boost/filesystem.hpp>

class BoardDFS {
    std::vector<std::vector<int>> board;
    std::vector<std::vector<bool>> visited;
    int rows, cols;

public:
    BoardDFS(const std::vector<std::vector<int>>& b)
        : board(b), rows(b.size()), cols(b.empty() ? 0 : b[0].size()), visited(rows, std::vector<bool>(cols, false)) {}

    int dfs(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) return 0;
        if (visited[row][col]) return 0;
        visited[row][col] = true;

        if (board[row][col] == 0) {
            std::cout << "Found 0 at row " << row << " and col " << col << std::endl;
            return -1;
        }

        int jump = board[row][col];
        // Up W
        if (dfs(row - jump, col) == -1) {
            std::cout << "W ";
            return -1;
        }
        // Down E
        if (dfs(row + jump, col) == -1) {
            std::cout << "E ";
            return -1;
        }
        // Left N
        if (dfs(row, col - jump) == -1) {
            std::cout << "N ";
            return -1;
        }
        // Right S
        if (dfs(row, col + jump) == -1) {
            std::cout << "S ";
            return -1;
        }
        return 0;
    }

    void resetVisited() {
        for (auto& row : visited) {
            std::fill(row.begin(), row.end(), false);
        }
    }
};

std::vector<std::vector<int>> readInput(const std::string& filename) {
    std::ifstream infile(filename);
    if (!infile) {
        throw std::runtime_error("Cannot open file: " + filename);
    }
    std::string line;
    std::getline(infile, line);
    boost::algorithm::trim(line);
    std::vector<std::string> dims;
    boost::split(dims, line, boost::is_any_of(" \t"), boost::token_compress_on);
    int rows = std::stoi(dims[0]);
    int cols = std::stoi(dims[1]);
    std::vector<std::vector<int>> board(rows, std::vector<int>(cols));
    for (int i = 0; i < rows; ++i) {
        std::getline(infile, line);
        boost::algorithm::trim(line);
        std::vector<std::string> tokens;
        boost::split(tokens, line, boost::is_any_of(" \t"), boost::token_compress_on);
        for (int j = 0; j < cols; ++j) {
            board[i][j] = std::stoi(tokens[j]);
        }
    }
    return board;
}

void printBoard(const std::vector<std::vector<int>>& board) {
    for (const auto& row : board) {
        for (int val : row) {
            std::cout << val << " ";
        }
        std::cout << std::endl;
    }
}

int main(int argc, char* argv[]) {
    std::string filename = (argc > 1 && argv[1] && std::string(argv[1]).size() > 0) ? argv[1] : "jim-input.txt";
    try {
        auto board = readInput(filename);
        printBoard(board);

        BoardDFS dfs(board);
        for (int i = 0; i < board.size(); ++i) {
            for (int j = 0; j < board[i].size(); ++j) {
                std::cout << "Searching row[" << i << "] col[" << j << "] - [" << board[i][j] << "]" << std::endl;
                dfs.dfs(i, j);
                std::cout << std::endl << std::endl;
                dfs.resetVisited();
            }
        }
    } catch (const std::exception& e) {
        std::cerr << "Error reading input file: " << filename << std::endl;
        std::cerr << e.what() << std::endl;
        return 1;
    }
    return 0;
}