import java.util.Scanner;
import java.awt.Point;

class TicTacToe {

    // Stores the game board
    class GameState {
        // total squares filled
        int total = 0;
        
        // owner of each tile (No owner = 0, user = 1, computer = 2)
        int grid[][] = new int[3][3];
        
        // default constructor
        GameState() {}

        // constructor clones an older gameState
        GameState(GameState game) {
            for (int i = 0; i < 3; i++) {grid[i] = game.grid[i].clone();}
            this.total = game.total;
        }

        // set a point
        void set(Point cnt,int owner) {
            grid[cnt.x][cnt.y] = owner;
            total++;
        }

        // display the board
        void board() {
            //System.out.println("∎∎∎∎∎∎∎");
            for (int i = 0; i < 3; i++) {
                System.out.print("∎");
                for (int j = 0; j < 3; j++) {
                    switch (grid[j][i]) {
                        case 1: System.out.print("O"); break;
                        case 2: System.out.print("X"); break;
                        default: System.out.print("."); break;
                    }
                    System.out.print("∎");
                }
                System.out.println();
            }
        }

        // displays the board in error stream
        void debug() {
            System.err.println("Debug grid:");
            for (int i = 0; i < 3; i++) {
                System.err.print("|");
                for (int j = 0; j < 3; j++) {
                    switch (grid[j][i]) {
                        case 1: System.err.print("O"); break;
                        case 2: System.err.print("X"); break;
                        default: System.err.print("."); break;
                    }
                    System.err.print("|");
                }
                System.err.println();
            }
        }

        // returns winner of the game (-1 if no result, 0 if draw, 1 for user, 2 for computer)
        int getWinner() {
            for (int i = 0; i < 3; i++) {
                if (grid[i][0] != 0 && grid[i][0] == grid[i][1] && grid[i][1] == grid[i][2]) return grid[i][1];
                if (grid[0][i] != 0 && grid[0][i] == grid[1][i] && grid[2][i] == grid[1][i]) return grid[1][i];
            }

            if (grid[1][1] != 0 && grid[0][0] == grid[1][1] && grid[1][1] == grid[2][2]) return grid[1][1];
            if (grid[1][1] != 0 && grid[2][0] == grid[1][1] && grid[1][1] == grid[0][2]) return grid[1][1];

            if (total >= 9) {return 0;}
            return -1;
        }
    }

    int maxDepth = 9;
    int bestMove;

    // minimax method
    int minimax(GameState state, int player, int depth, int beta, int alpha) {
        // check for winner before goind further
        int winner = state.getWinner();
        if (winner == 0) {
            //state.debug();
            //System.err.println("At depth = "+(maxDepth - depth)+" winner = "+winner);
        }
        if (winner == 1) return -1;
        else if (winner == 2) return +1;
        else if (winner == 0) return 0;

        int best = player == 1 ? 2 : -2;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (state.grid[j][i] != 0) {continue;}
                GameState newState = new GameState(state); // clone the game state
                newState.set(new Point(j,i),player); // set at position
                int tempScore = minimax(newState,2-player+1,depth-1,beta,alpha); // get score by minimax
                if (player == 2 && tempScore > best) { // for max-node
                    if (tempScore >= beta) {return beta;} // alpha-beta pruning
                    if (depth == maxDepth) {bestMove = j*10+i;}
                    if (tempScore > alpha) {alpha = tempScore;}
                    best = tempScore;
                } else if (player == 1 && tempScore < best) { // for min node
                    if (tempScore <= alpha) {return alpha;} // alpha-beta pruning
                    if (tempScore < beta) {beta = tempScore;}
                    best = tempScore;
                }
            }
        }

        return best;
    }

    public void main1(Scanner in) {
        GameState game = new GameState();
        game.board();
        while (true) {
            System.out.print("Enter move: ");
            int x = in.nextInt();
            int y = in.nextInt();
            if (game.grid[x][y] != 0 ) {System.out.println("You CHEATED! Game will terminate -_-"); return;}
            game.set(new Point(x,y) , 1);

            game.board();
            if (game.total == 9) {System.out.println("Match tied! ;)"); return;}

            int winner = game.getWinner();
            if (winner == 1) {System.out.println("You beat my AI!!"); return;}
            else if (winner == 2) {System.out.println("My AI beat you!!"); return;} 

            TicTacToe AI = new TicTacToe();
            int best = AI.minimax(new GameState(game), 2, AI.maxDepth, 1000, -1000);
            int move = AI.bestMove;
            x = move/10;
            y = move%10;
            System.out.println("Computer gives: "+x+" "+y);
            if (game.grid[x][y] != 0 ) {System.out.println("Computer CHEATED! Game will terminate :/"); return;}
            game.set(new Point(x,y) , 2);

            game.board();

            winner = game.getWinner();
            if (winner == 1) {System.out.println("You beat my AI!!"); return;}
            else if (winner == 2) {System.out.println("My AI beat you!!"); return;} 
        }
    }

    public void main2(Scanner in) {
        GameState game = new GameState();
        game.board();
        while (true) {
            TicTacToe AI = new TicTacToe();
            int best = AI.minimax(new GameState(game), 2, AI.maxDepth, 1000, -1000);
            int move = AI.bestMove;
            int x = move/10;
            int y = move%10;
            System.out.println("Computer gives: "+x+" "+y);
            if (game.grid[x][y] != 0 ) {System.out.println("Computer CHEATED! Game will terminate :/"); return;}
            game.set(new Point(x,y) , 2);
            
            game.board();
            if (game.total == 9) {System.out.println("Match tied! ;)"); return;}

            int winner = game.getWinner();
            if (winner == 1) {System.out.println("You beat my AI!!"); return;}
            else if (winner == 2) {System.out.println("My AI beat you!!"); return;} 

            System.out.print("Enter move: ");
            x = in.nextInt();
            y = in.nextInt();
            if (game.grid[x][y] != 0 ) {System.out.println("You CHEATED! Game will terminate -_-"); return;}
            game.set(new Point(x,y) , 1);

            game.board();

            winner = game.getWinner();
            if (winner == 1) {System.out.println("You beat my AI!!"); return;}
            else if (winner == 2) {System.out.println("My AI beat you!!"); return;} 
        }
    }
    
    public static void main(String[] args) {
        System.out.println("Tic Tac Toe: \n\nRules:\n1. Enter your coordinates as \"x y\"\n2. Coordinates are numbered from (0,0) to (2,2)\n3. Entering invalid coordinates will make the game terminate.\n\n\nPlay:");
        Scanner in = new Scanner(System.in);
        TicTacToe game = new TicTacToe();
        System.out.print("1. You start\n2. Computer starts\nEnter choice: ");
        int ch = in.nextInt();
        System.out.println();
        switch (ch) {
            case 1: game.main1(in); return;
            case 2: game.main2(in); return;
            default: System.out.println("Wrong choice -_-");
        }
    }
}
