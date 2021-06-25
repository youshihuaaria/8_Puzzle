import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private final int[] board;
    private final int dim;
    private int blank;
    
    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        int n = tiles.length;
        this.board = new int[n * n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i * n + j] = tiles[i][j];
                if (tiles[i][j] == 0) {
                    blank = i * n + j;
                }
            }
        }
        this.dim = n;
    }
    
    private Board(int[] tiles, int dim, int blankSquare) {
        this.board = tiles.clone();
        this.dim = dim;
        this.blank = blankSquare;
    }
                                           
    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(dim + "\n");
        for (int i = 0; i < dim * dim; i++) {
            sb.append(board[i] + " ");
            if (board[i] % dim == 0) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return this.dim;
    }

    // number of tiles out of place
    public int hamming() {
        int hamming = 0;
        for (int i = 0; i < dim * dim; i++) {
            if (board[i] != 0 && board[i] != i + 1) {
                hamming++;
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < dim * dim; i++) {
                if (board[i] != i + 1 && board[i] != 0) { 
                    int row = (board[i] - 1) / dim - i / dim; 
                    int col = (board[i] - 1) % dim - i % dim; 
                    manhattan += Math.abs(row) + Math.abs(col);
                }
            }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return (hamming() == 0);
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) return false;
        if (this == y) return true;
        if (this.getClass() != y.getClass()) return false;
        
        Board that = (Board) y;
        return Arrays.equals(this.board, that.board);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> lst = new ArrayList<Board>();
        
        if (blank / dim != 0) {
            int index = blank - this.dim;
            lst.add(new Board(exchange(board.clone(), blank, index), this.dim, index));
        }
        
        if (blank / dim != dim - 1) {
            int index = blank + this.dim;
            lst.add(new Board(exchange(board.clone(), blank, index), this.dim, index));
        }
        
        if (blank % dim != dim - 1) {
            lst.add(new Board(exchange(board.clone(), blank, blank + 1), this.dim, blank + 1));
        }
        
        if (blank % dim != 0) {
            lst.add(new Board(exchange(board.clone(), blank, blank - 1), this.dim, blank - 1));
        }
        
        return lst;
    }
    
    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        if (this.dim == 1) {
            return null;
        }
        int[] twin = this.board.clone();
        if (twin[0] != 0 && twin[1] != 0) {
            return new Board(exchange(twin, 0, 1), this.dim, this.blank);
        } else {
            return new Board(exchange(twin, dim, dim + 1), this.dim, this.blank);
        }
    }
    
    private int[] exchange(int[] lst, int a, int b) {
        int temp = lst[a];
        lst[a] = lst[b];
        lst[b] = temp;
        return lst;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        Board board = new Board(new int[][]{{5, 8, 7}, {1, 4, 6}, {3, 0, 2}});
        System.out.println(board.manhattan());
        for (Board board1 :
                board.neighbors()) {
            System.out.println(board1);
        }
    }
}
