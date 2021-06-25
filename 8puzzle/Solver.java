import java.util.Comparator;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {
    private int count;
    private Node goal;
    
    private class Node {
        private final Board board;
        private final int moves, priority;
        private final Node prev;
        
        Node(Board board, int move, Node prev) {
            this.board = board;
            this.moves = move;
            this.prev = prev;
            this.priority = board.manhattan() + this.moves;
        }
    }
    
    private class PriorityComparator implements Comparator<Node> {
        public int compare(Node a, Node b) {
            int ap = a.priority;
            int bp = b.priority;
            if (ap > bp) return +1;
            if (ap < bp) return -1;
            return 0;
        }
    }
    
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        
        this.count = 0;
        this.goal = null;
        
        PriorityComparator pc = new PriorityComparator();
        Board twin = initial.twin();
        
        MinPQ<Node> q1 = new MinPQ<Node>(10, pc);
        MinPQ<Node> q2 = new MinPQ<Node>(10, pc);
        
        Node board = new Node(initial, 0, null);
        Node twins = new Node(twin, 0, null);
        
        q1.insert(board);
        q2.insert(twins);
        
        Node curr1, curr2;
        
        while (!q1.min().board.isGoal() && !q2.min().board.isGoal()) {
            curr1 = q1.delMin();
            curr2 = q2.delMin();
            
            for (Board b: curr1.board.neighbors()) {
                if (curr1.prev == null || !b.equals(curr1.prev.board))  {
                    q1.insert(new Node(b, curr1.moves + 1, curr1));
                }
            }
            
            for (Board b: curr2.board.neighbors()) {
                if (curr2.prev == null || !b.equals(curr2.prev.board))  {
                    q2.insert(new Node(b, curr2.moves + 1, curr2));
                }
            }
        }
        if (q1.min().board.isGoal()) {
            count = q1.min().moves;
            goal = q1.min();
        } else {
            count = -1;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return count != -1;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return count;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        }
        
        Stack<Board> s = new Stack<Board>();
        Node b = goal;
        while (b != null) {
            s.push(b.board);
            b = b.prev;
        }
        
        return s;
    }

    public static void main(String[] args) {
        
    }

}
