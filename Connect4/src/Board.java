import java.util.ArrayList;

/**
 * Create board related functions
 * 
 * @author Aryan Patel, Samyak Metha
 *
 */
public class Board {

    /**
     * Get valid column
     * 
     * @param board the board
     * @return validLocations the array list of valid columns
     */
    public ArrayList<Integer> getValidLocation(int[][] board) {
        ArrayList<Integer> validLocations = new ArrayList<>();

     // For every column not filled to the top add it to the arrayList
        for (int i = 0; i < board[0].length; i++) {
            if (board[0][i] == 0) {
                validLocations.add(i);
            }
        }
        return validLocations;
    }

    /**
     * Get valid row given column
     * 
     * @param board the board
     * @param column the column
     * @return i the valid row
     */
    public int getOpenRow(int[][] board, int column) {
     
        int i = board.length - 1;

     // Get the last open row in a given column
        while (i >= 0) {
            if (board[i][column] == 0) {
                return i;
            }
            i -= 1;
        }
        return -1;
    }

    /**
     * Calculate best move
     * 
     * @param array
     * @param piece
     * @return score
     */
    public int evaluateWindow(int[] array, int piece) {
        int score = 0;
     
        
     // Make sure oppPiece always refers to the alternate piece 
     // as the one provided in the parameter
        int oppPiece = 1;
        if (piece == 1) {
            oppPiece = 2;
        }

        // Count the number of player pieces, AI pieces, and empty slots
        // In the given 4 array
        int pieceCount = 0;
        int emptyCount = 0;
        int oppPieceCount = 0;
        
        for (int i = 0; i < array.length; i++) {
            if (array[i] == piece) {
                pieceCount += 1;
            } else if (array[i] == 0) {
                emptyCount += 1;
            } else if (array[i] == oppPiece) {
                oppPieceCount += 1;
            }
        }

        // If it is a winning move it is prioritized
        if (pieceCount == 4) {
            score += 100;
            
        // If there are 3 pieces and an empty slot 
        } else if (pieceCount == 3 && emptyCount == 1) {
            score += 5;
        // If there are 2 pieces and 2 empty slots
        } else if (pieceCount == 2 && emptyCount == 2) {
            score += 3;
        }
        // If the opponent (Player) has an opportunity to win block it
        if (oppPieceCount == 3 && emptyCount == 1) {
            score -= 25;
        }
        return score;
    }

    /**
     * Check the score of the board at every 4 pieces [vertical/horizontal/diagonal]
     * 
     * @param board
     * @param piece
     * @return score
     */
    public int scoreCheck(int[][] board, int piece) {

        int score = 0;

         // HORIZONTAL CHECKING
        int[] temp = new int[4];
        for (int ROW = 0; ROW < board.length; ROW++) {
            for (int COLUMN = 0; COLUMN < board[0].length - 3; COLUMN++) {
                for (int k = 0; k < 4; k++) {
         // Go through every array of four on the entire board horizontally
                    temp[k] = board[ROW][COLUMN + k];
                }
         // For each array of four evaluate the score from the method above
                score += evaluateWindow(temp, piece);
            }
        }

        // VERTICAL CHECKING
        for (int COLUMN = 0; COLUMN < board[0].length; COLUMN++) {
            for (int ROW = 0; ROW < board.length - 3; ROW++) {
                for (int k = 0; k < 4; k++) {
        // Go through every array of four on the entire board vertically
                    temp[k] = board[ROW + k][COLUMN];
                }
        // For each array of four evaluate the score
                score += evaluateWindow(temp, piece);
            }
        }

        // DIAGONAL FROM TOP LEFT to BOTTOM RIGHT
        for (int ROW = 0; ROW <= 2; ROW++) {
            for (int COLUMN = 0; COLUMN <= 3; COLUMN++) {
                for (int k = 0; k < 4; k++) {
        // Go through every array of four of diagonals ranging from top left to bottom right
                    temp[k] = board[ROW + k][COLUMN + k];
                }
        // For each array of four evaluate the score
                score += evaluateWindow(temp, piece);
            }
        }

        // DIAGONAL FROM TOP RIGHT TO BOTTOM LEFT
        for (int ROW = 0; ROW <= 2; ROW++) {
            for (int COLUMN = 3; COLUMN <= board.length; COLUMN++) {
                for (int k = 0; k < 4; k++) {
        // Go through every array of four of diagonals ranging from bottom left to top right
                    temp[k] = board[ROW + k][COLUMN - k];
                }
        // For each array of four evaluate the score
                score += evaluateWindow(temp, piece);
            }
        }
        return score;
    }

    /**
     * Terminal board positions
     * 
     * @param board
     * @return boolean [true/false]
     */
    public boolean terminal_node(int[][] board) {
        return winCheck(board) == 1 || winCheck(board) == 2 || getValidLocation(board).size() == 0;
    }

    public int[][] cloneBoard(int[][] board) {
      // Create a clone of the entire board
      // Using loops so memory address does not get duplicated
        int[][] clone = new int[6][7];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                clone[i][j] = board[i][j];
            }
        }
        return clone;
    }

    /**
     * Find the best possible positon of AI which thinks depth moves ahead
     * 
     * @param board
     * @param depth the number of moves it calculates ahead
     * @param alpha
     * @param beta
     * @param maximizingPlayer the AI
     * @return int[] the best score given the best column
     */
    public int[] miniMax(int[][] board, int depth, int alpha, int beta, boolean maximizingPlayer) {
        ArrayList<Integer> validLocations = getValidLocation(board);

        int newCol = 0;
        boolean is_terminal = terminal_node(board);
        if (depth == 0 || is_terminal) {

            if (is_terminal) {
                // 3 conditions (refer to terminal node)
                // First condition: If AI has the winning move
                if (winCheck(board) == 2) {
                    return new int[] {-1, 100000000};
                }

                // Second condition: If Player has the winning move
                else if (winCheck(board) == 1) {
                    return new int[] {-1, -100000000};
                }

                // Game is over no more valid moves
                else {
                    return new int[] {-1, 0};
                }
            }

            // Depth is zero therefore find the heuristic value of the board
            // 2 is your AI piece
            else {
                return new int[] {-1, scoreCheck(board, 2)};
            }
        }
        if (maximizingPlayer) {
            int value = -1000000000;
            newCol = validLocations.get(0);
            // Go through every possible column
            for (int col : validLocations) {
            // Get the most open row, create a clone of the board
            // And recursively call the function again with that new piece in play
              
                int row = getOpenRow(board, col);
                int[][] boardClone = cloneBoard(board);
                boardClone[row][col] = 2;
            // Simulate all possible 'x' potential moves [x = DEPTH]
                int newScore = miniMax(boardClone, depth - 1, alpha, beta, false)[1];
            // If the score is evaluated as more desirable set the column to be 
            // placed as that one
                if (newScore > value) {
                    value = newScore;
                    newCol = col;
                }
            // Use Alpha Beta pruning to support efficient searching to remove
            // unnecessary checking
                alpha = Math.max(alpha, value);
                if (alpha >= beta) {
                    break;
                }

            }
            return new int[] {newCol, value};
        }

        // Minimizing player
        else {
            int value = 1000000000;
            newCol = validLocations.get(0);

            for (int col : validLocations) {
            // Same process as above
                int row = getOpenRow(board, col);
                int[][] boardClone = cloneBoard(board);
                boardClone[row][col] = 1;
                int newScore = miniMax(boardClone, depth - 1, alpha, beta, true)[1];
            // Since it is the minimizing player we are simulating as 
            // the opponent piece so we want the least desirable option
            // [We want to assume that within the next x moves, the opponent will 
            // do the best possible move to stop us]
                if (newScore < value) {
                    value = newScore;
                    newCol = col;
                }
            // Alpha Beta pruning
                beta = Math.min(beta, value);
                if (alpha >= beta) {
                    break;
                }
            }
            return new int[] {newCol, value};
        }
    }

    /**
     * checks for winner
     * 
     * @return winner
     */
    public int winCheck(int[][] board) {

        // check horizontal
        // nested for loops iterate though board grid (until column 4) to see if there
        // are 4 in a row
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] != 0) {
                    // if there are 4 in a row horizontally, returns winner
                    if ((board[i][j] == board[i][j + 1]) && (board[i][j + 1] == board[i][j + 2])
                            && (board[i][j + 2] == board[i][j + 3])) {
                        return board[i][j];
                    }
                }
            }
        }

        // check vertical
        // nested for loops iterate though board grid (until row 3) to see if there are
        // 4 in a row
        for (int i = 0; i < board[0].length; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[j][i] != 0) {
                    // if there are 4 in a row vertically, returns winner
                    if ((board[j][i] == board[j + 1][i]) && (board[j + 1][i] == board[j + 2][i])
                            && (board[j + 2][i] == board[j + 3][i])) {
                        return board[j][i];
                    }
                }
            }
        }

        // check diagonal (left to right)
        // nested for loops iterate though board grid to see if there are 4 in a row
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[j][i] != 0) {
                    // if there are 4 in a row diagonally, returns winner
                    if ((board[j][i] == board[j + 1][i + 1]) && (board[j + 1][i + 1] == board[j + 2][i + 2])
                            && (board[j + 2][i + 2] == board[j + 3][i + 3])) {
                        return board[j][i];
                    }
                }
            }
        }

        // check diagonal(right to left)
        // nested for loops iterate though board grid to see if there are 4 in a row
        for (int i = 6; i > 2; i -= 1) {
            for (int j = 0; j < 3; j++) {
                if (board[j][i] != 0) {
                    // if there are 4 in a row diagonally, returns winner
                    if ((board[j][i] == board[j + 1][i - 1]) && (board[j + 1][i - 1] == board[j + 2][i - 2])
                            && (board[j + 2][i - 2] == board[j + 3][i - 3])) {
                        return board[j][i];
                    }
                }
            }
        }

        return 0; // returns 0 if no winner detected
    }

    /**
     * checks for ties depending on if the top row is filled
     * 
     * @return a boolean type if there is a tie or not
     */
    public boolean Tie(int[][] board, int winner) {
        int count = 0;
        // looping through the top row, if the index does not equal 0 (meaning that
        // there is a piece), adds one to count
        for (int i = 0; i < board[0].length; i++) {
            if (board[0][i] != 0) {
                count++;
            }
        }
        // if there is no current winner on the last piece placed and all of the pieces
        // at top row are full, returns true as in there is a tie
        if (count == 7 && winner == 0) {
            return true;
        }
        // else there is no tie, and returns false
        return false;
    }
}
