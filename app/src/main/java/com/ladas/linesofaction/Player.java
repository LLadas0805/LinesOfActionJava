package com.ladas.linesofaction;

import java.util.Objects;
import java.util.Vector;

public class Player {
    private char color;
    private int score;
    private int wins;
    private int moves;

    private String selection;
    private String destination;
    private String identity;

    /**********************************************************************
     * Function Name: Player
     * Purpose: Constructor meant to initialize and define private members of the
     * Player class when created as an object
     * Parameters: argIdentity, a string passed by value meant to assign the Player
     * object with either human or computer
     * Return Value: none
     * Algorithm: none
     * Reference: none
     */
    public Player(String argIdentity) {
        identity = argIdentity;
        color = '\0';
        score = 0;
        wins = 0;
        moves = 0;
        selection = "";
        destination = "";
    }

    /**********************************************************************
     * Function Name: Player
     * Purpose: Constructor meant to initialize and define private members of the
     * Player class when created as an object
     * Parameters: argIdentity, a string passed by value meant to assign the Player
     * object with either human or computer
     * 
     * argColor, a char passed by value used to define what color of
     * pieces the player can control
     * 
     * argScore, an int passed by value used to pass on the total score
     * a player has throughout the tournament
     * 
     * argWins, an int passed by value used to pass on the total round
     * wins the player has throughout the tournament
     * 
     * Return Value: none
     * Algorithm: none
     * Reference: none
     */
    public Player(String argIdentity, char argColor, int argScore, int argWins) {
        color = argColor;
        score = argScore;
        wins = argWins;
        identity = argIdentity;

    }



    /**********************************************************************
     * Function Name: play
     * Purpose: Default definition for Player object play function, temporary and
     * meant to be overridden by children classes
     * Parameters: board, a Board object reference meant to be have it's board grid
     * modified when pieces are moved
     * 
     * self, a Player object pointer, getting the memory address of the current
     * player in a turn
     * 
     * nextPlayer, a player object pointer, getting the memory address of the player
     * who goes next turn
     * Return Value: empty string
     * Algorithm: none
     * Reference: none
     */
    String play(Board board, Player self, Player nextPlayer) {
        return "";
    }

    /**********************************************************************
     * Function Name: getColor
     * Purpose: To obtain the current player's color/side
     * Parameters: none
     * Return Value: The color associated with the player object, a char value
     * Algorithm: none
     * Reference: none
     */
    char getColor() {
        return color;
    }

    /**********************************************************************
     * Function Name: setColor
     * Purpose: modifies the current player's color
     * Parameters: argColor, a char passed by value used to be assigned to the color
     * in the Player class
     * Return Value: none
     * Algorithm: none
     * Reference: none
     */
    void setColor(char argColor) {
        color = argColor;
    }

    /**********************************************************************
     * Function Name: getMoves
     * Purpose: To obtain the current player's number of actions taken in a round
     * Parameters: none
     * Return Value: The number associated with the player actions made in a turn, a int value
     * Algorithm: none
     * Reference: none
     */
    int getMoves() {
        return moves;
    }

    /**********************************************************************
     * Function Name: setMoves
     * Purpose: modifies the current player's amount of actions made
     * Parameters: argMoves, a int passed by value used to be assigned to the moves
     * in the Player class
     * Return Value: none
     * Algorithm: none
     * Reference: none
     */
    void setMoves(int argMoves) {
        moves = argMoves;
    }

    /**********************************************************************
     * Function Name: getPieces
     * Purpose: To obtain the number of pieces associated with the player's color
     * Parameters:
     * board, a 2D array of Piece class objects passed by value.
     * It holds all of the board grid information on pieces
     * Return Value: The total count of pieces on the board matching with the
     * player's color
     * Algorithm: none
     * Reference: none
     */
    int getPieces(Piece[][] board) {

        int pieces = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j].getColor() == color) {
                    pieces++;
                }
            }
        }
        return pieces;
    }

    /**********************************************************************
     * Function Name: getScore
     * Purpose: To obtain the current player's score in the tournament
     * Parameters: none
     * Return Value: The integer value of the player's score
     * Algorithm: none
     * Reference: none
     */
    int getScore() {
        return score;
    }

    /**********************************************************************
     * Function Name: setScore
     * Purpose: modifies the current player's score
     * Parameters: argScore, an int passed by value used to be assigned to the score
     * in the Player class
     * Return Value: none
     * Algorithm: none
     * Reference: none
     */
    void setScore(int argScore) {
        score = argScore;
    }

    /**********************************************************************
     * Function Name: getWins
     * Purpose: To obtain the current player's wins in the tournament
     * Parameters: none
     * Return Value: The integer value of the player's wins
     * Algorithm: none
     * Reference: none
     */
    int getWins() {
        return wins;
    }

    /**********************************************************************
     * Function Name: setWins
     * Purpose: modifies the current player's wins in the tournament
     * Parameters: argWins, an int passed by value used to be assigned to the wins
     * in the Player class
     * Return Value: none
     * Algorithm: none
     * Reference: none
     */
    void setWins(int argWins) {
        wins = argWins;
    }

    /**********************************************************************
     * Function Name: getIdentity
     * Purpose: To obtain the current player's identity in a tournament
     * Parameters: none
     * Return Value: The string value associated with a player's identity
     * Algorithm: none
     * Reference: none
     */
    String getIdentity() {
        return identity;
    }

    /**
     * Gives a player a selected origin piece coordinate
     * @param argSelection, a string value represented selected coordinates on the board
     */
    void setSelection(String argSelection) {selection = argSelection;}

    /**
     * Gets the origin coordinates of a player's move
     * @return a String value, origin coordinates of move
     */
    String getSelection() {
        return selection;
    }

    /**
     * Gives a player a selected destination piece coordinate
     * @param argDestination, a string value represented selected coordinates on the board
     */
    void setDestination(String argDestination) {destination = argDestination;}
    /**
     * Gets the destination coordinates of a player's move
     * @return a String value, destination coordinates of move
     */
    String getDestination() {
        return destination;
    }

    /**
     * For finding the best piece a player can move to in a given turn
     * @param board, a Board class object passed by value
     * @param self , a Player object pointer, getting the memory address of the current
     *        player in a turn
     * @param nextPlayer, a player object pointer, getting the memory address of the player
     *        who goes next turn
     * @return A Piece class object of the best piece and their best strategy
     */
    Piece strategize(Board board, Player self, Player nextPlayer) {

        Board tempBoard = board.clone();
        ;

        if (Objects.equals(self.getIdentity(), "Human")) {
            Log.AddMessage("");
            Log.AddMessage("Available Strategies:\n");
        }
        // playerPieces holds all of the pieces associated with current player
        Vector<Piece> playerPieces = new Vector<Piece>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // coordColor holds the current color of a piece in the current nested loop
                // iteration of the board
                char coordColor = tempBoard.getBoard()[i][j].getColor();
                if (coordColor == color && color != '.') {
                    // coords holds the conversion of the iteration values into the formatted input
                    // for selecting pieces
                    String coords = String.valueOf((char) (j + 65)) + (8 - i);
                    // currPiece holds the given piece at this loop iterator's element and the best
                    // strategy it has
                    System.out.print(coords);
                    Piece currPiece = pieceStrategy(tempBoard, coords, self, nextPlayer);
                    // only adds pieces with available strategies
                    if (!"".equals(currPiece.getStrategy())) {
                        // assigns where the piece was first found in the board
                        currPiece.setOrigin(coords);
                        // add the current piece to the playerPieces vector
                        playerPieces.add(currPiece);

                        Log.AddMessage(" - Best move for " + coords + ": Move from " + coords + " to " + currPiece.getStratCoords() + " ("
                                + currPiece.getStrategy() + ")");

                    }
                }
            }
        }

        // sorts playerPieces from best strategy to worst strategy
        for (int i = 0; i < playerPieces.size(); ++i) {
            for (int j = 0; j < playerPieces.size() - i - 1; ++j) {
                if (!comparePiecesByStrategy(playerPieces.elementAt(j), playerPieces.elementAt(j + 1))) {
                    // Swap pieces if they are not in the correct order
                    Piece temp = playerPieces.elementAt(j);
                    playerPieces.set(j, playerPieces.elementAt(j + 1));
                    playerPieces.set(j + 1, temp);
                }
            }
        }

        // bestPiece holds the best possible piece and strategy to make out of all the
        // player pieces
        // assign first element of sorted player Pieces to bestPiece
        Piece bestPiece = playerPieces.elementAt(0);

        Log.AddMessage("");

        if (Objects.equals(self.getIdentity(), "Human")) {

            Log.AddMessage("Recommended Strategy:\n");
        } else {

            Log.AddMessage("Computer's Strategy:\n");
        }
        Log.AddMessage(" - Moves piece from " + bestPiece.getOrigin() + " to " + bestPiece.getStratCoords() + " ("
                + bestPiece.getStrategy() + ")\n");


        return bestPiece;
    }

    /**
     * Comparing two pieces to rank which strategy is higher priority
     * @param piece1, a Piece class object passed by reference, used for left
     *       hand comparison
     * @param piece2, a Piece class object passed by reference, used for right hand
     *        comparison
     * @return the truth value of if the first piece's strategy is worse than the second
     */
    boolean comparePiecesByStrategy(Piece piece1, Piece piece2) {
        int rank1 = strategyToRank(piece1.getStrategy());
        int rank2 = strategyToRank(piece2.getStrategy());
        return rank1 < rank2;
    }

    /**
     * For finding the best strategy a piece can make in a given turn
     * @param board, a Board class object passed by reference, allows for grid
     *        access and piece modification
     * @param origin, a string value holding the formatted current location of a piece
     * @param self, a Player object pointer, getting the memory address of the current
     *        player in a turn
     * @param nextPlayer, a player object pointer, getting the memory address of the player
     *       who goes next turn
     * @return A Piece class object of the best piece and their best strategy
     */
    Piece pieceStrategy(Board board, String origin, Player self, Player nextPlayer) {
        // row holds the specific row index value of the origin coordinates
        int row = 8 - (origin.charAt(1) - '0');
        // col holds the specific column index value of the origin coordinates
        int col = (origin.charAt(0)) - 65;

        // boardGrid holds the 2D Piece array that is contained inside the board class
        // reference
        Piece[][] boardGrid = board.getBoard();
        // bestStrategy holds the integer value for the best strategy that can be made
        int bestStrategy = -1;
        // bestCoords holds the string coordinate value position associated with the
        // best strategy
        String bestCoords = "";

        // Get each direction's possible distance
        // vertCount holds the distance a piece can move vertically
        int vertCount = board.calcPiecesVert(col);
        // horiCount holds the distance a piece can move horizontally
        int horiCount = board.calcPiecesHori(row);
        // diagPosCount holds the distance a piece can move diagonally with positive
        // slope
        int diagPosCount = board.calcPiecesDiag(row, col, 1, -1);
        // diagNegCount holds the distance a piece can move diagonally with negative
        // slope
        int diagNegCount = board.calcPiecesDiag(row, col, 1, 1);

        // destinations is a size 8 array of strings for
        // holding the formatted coordinates for each possible direction a player can
        // move to
        String destinations[] = {
                // upwards direction
                String.valueOf((char) (col + 65)) + (8 - (row + (-1 * vertCount))),
                // downwards direction
                String.valueOf((char) (col + 65)) + (8 - (row + vertCount)),
                // right direction
                String.valueOf((char) ((col + horiCount) + 65)) + (8 - row),
                // left direction
                String.valueOf((char) ((col) + (-1 * horiCount) + 65)) + (8 - row),
                // upright direction
                String.valueOf((char) ((col + diagPosCount) + 65)) + (8 - (row + (-1 * diagPosCount))),
                // downleft direction
                String.valueOf((char) ((col + (diagPosCount * -1)) + 65)) + (8 - (row + diagPosCount)),
                // downright direction
                String.valueOf((char) ((col + diagNegCount) + 65)) + (8 - (row + diagNegCount)),
                // upleft direction
                String.valueOf((char) ((col + (diagNegCount * -1)) + 65)) + (8 - (row + (-1 * diagNegCount)))
        };

        // check each piece direction for move strategies
        for (int i = 0; i < 8; i++) {

            // Go through each destination
            String destination = destinations[i];

            // currStrategy holds which strategy a direction will select
            // assigned at -1 for now (not possible strategy)
            int currStrategy = -1;

            // Checks to see if the move direction is possible in the firstplace
            if (board.validate_moveset(origin, destination)) {

                if (canWin(board, origin, destination)) {

                    if (isOccupied(boardGrid, destination)) {
                        // assigns win & capture to currStrategy
                        currStrategy = 7;
                    }
                    // assigns win to currStrategy
                    currStrategy = 6;
                } else if (canThwart(board, destination, self, nextPlayer)) {
                    // assigns thwart to currStrategy
                    currStrategy = 5;
                } else if (canDelay(bestStrategy, boardGrid, nextPlayer)) {
                    // assigns delay to currStrategy
                    currStrategy = 4;
                } else if (canCapture(boardGrid, destination, nextPlayer)) {
                    // assigns capture to currStrategy
                    currStrategy = 3;
                } else if (canGroup(board, origin, destination)) {
                    // assigns group to currStrategy
                    currStrategy = 2;
                } else if (canBlock(boardGrid, destination)) {
                    // assigns block to currStrategy
                    currStrategy = 1;
                } else {
                    // assigns move to currStrategy
                    currStrategy = 0;
                }

                Log.AddMessage("       - " + origin + " to " + destination + " (" + rankToStrategy(currStrategy) + ")");

                // Compares strategies for finding best move
                if (currStrategy >= bestStrategy) {
                    // assign current strategy that is better to bestStrategy
                    bestStrategy = currStrategy;
                    // assign the best strategy's destination to bestCoords
                    bestCoords = destination;
                }
            }


        }

        // no strategy will be stored if somehow no possible moves could be made
        if (bestStrategy > -1) {
            // Piece at given coordinates has strategy and coordinates modified to the best
            // strategy
            boardGrid[row][col].setStrategy(rankToStrategy(bestStrategy));
            boardGrid[row][col].setStratCoords(bestCoords);
        }

        return boardGrid[row][col];

    }

    /**
     * Converting a piece's strategy into their numerical ranking
     * @param strategy, a string value passed by value, used for converting into rank
     * @return the integer value of the rank associated with strategy
     */
    int strategyToRank(String strategy) {

        String[] strategies = { "win & capture", "win", "thwart", "delay", "capture", "group up with friendly pieces",
                "block", "move" };

        for (int i = 0; i < 8; i++) {

            if (Objects.equals(strategy, strategies[i])) {
                return i;
            }
        }

        return 0;

    }

    /**
     * Converting a piece's rank into their categorical strategy
     * @param rank, an int value used for switch statement comparison
     * @return the string value of the strategy associated with rank
     */
    String rankToStrategy(int rank) {

        // Compares parameter with all cases to return proper strategy
        switch (rank) {

            case 7:
                return "win & capture";
            case 6:
                return "win";
            case 5:
                return "thwart";

            case 4:
                return "delay";

            case 3:
                return "capture";

            case 2:
                return "group up with friendly pieces";

            case 1:
                return "block";

            case 0:
                return "move";

            default:
                return "";
        }
    }

    /**
     * Compares the two player's scores in a round
     * @param board, an 8x8 2D Piece array passed by value
     * @param nextPlayer, a player object pointer, getting the memory address of the player who goes next turn
     * @return the truth value of if player's score in a round is <= opponent
     */
    boolean scoreCheck(Piece[][] board, Player nextPlayer) {
        int selfScore = getPieces(board) - nextPlayer.getPieces(board);
        int oppScore = nextPlayer.getPieces(board) - getPieces(board);
        return selfScore < oppScore;
    }

    /**
     * Converting a piece's rank into their categorical strategy
     * @param coordColor a char passed by value used for comparing with own player's color
     * @return the truth value if coordColor is not equal to player's color and if color not empty
     */
    boolean colorCheck(char coordColor) {
        return (coordColor != color && coordColor != '.');
    }

    /**
     * Checks to see if piece at coordinates in the board is from the opponent
     * @param board, an 8x8 2D Piece array passed by value
     * @param coordinates, a string passed by value used for finding if color
     *      at given point in board is different than the player color
     * @return the truth value of coordColor being different than player color and not empty
     */
    boolean isOccupied(Piece[][] board, String coordinates) {
        int row = 8 - (coordinates.charAt(1) - '0');
        int col = (coordinates.charAt(0)) - 65;

        char coordColor = board[row][col].getColor();

        return colorCheck(coordColor);
    }

    /**
     * Checks to see if player's piece should capture an opponent's piece
     * @param board, an 8x8 2D Piece array passed by value
     * @param coordinates a string passed by value used for finding if color
     *      at given point in board is different than the player color
     * @param nextPlayer, a player object pointer, getting the memory address of the player
     *      who goes next turn
     * @return the truth value of the coordinates being occupied and if the player score
     *      is less than or equal to the opponent score in a round
     */
    boolean canCapture(Piece[][] board, String coordinates, Player nextPlayer) {

        return (isOccupied(board, coordinates) && scoreCheck(board, nextPlayer));
    }

    /**
     * Checking if the piece, when moved will be grouped up with all other
     * @param board, an 8x8 2D Piece array passed by value
     * @param origin, a string passed by value used for getting the formatted starting
     *        coordinates of the current piece
     * @param destination, a string passed by value used for getting the formatted ending
     *        coordinates of the current piece
     * @return the truth value of if the moved piece will meet the win conditions stated in the Board class
     */
    boolean canWin(Board board, String origin, String destination) {
        Board tempBoard = board.clone();
        ;
        tempBoard.move_piece(origin, destination);
        return tempBoard.winCondition(color, destination, getPieces(tempBoard.getBoard()));

    }

    /**
     * Checks to see if the player should delay a win to get more score
     * @param strategy  an int value used for getting best strategy at the current iteration
     * @param board, an 8x8 2D Piece array passed by value
     * @param nextPlayer, a player object pointer, getting the memory address of the player
     *        who goes next turn
     * @return the truth value of if the piece has a low enough score and if
     *         the best strategy is winning
     */
    boolean canDelay(int strategy, Piece[][] board, Player nextPlayer) {

        return (scoreCheck(board, nextPlayer) && strategy == 6);
    }

    /**
     * Checks if current player piece's move coordinates will be able to block border pieces
     * @param board, an 8x8 2D Piece array passed by value
     * @param coordinates a string passed by value used for getting location of
     *        piece on the board grid
     * @return Returns the truth value of if the piece location of the current player is
     *         at the penultimate row and column or second row and columns. AND
     *         if the piece will block a opponent piece in the first and last rows and
     *         columns
     */
    boolean canBlock(Piece[][] board, String coordinates) {

        // row holds the index value associated with the array's rows
        int row = 8 - (coordinates.charAt(1) - '0');
        // col holds the index value associated with the array's columns
        int col = (coordinates.charAt(0)) - 65;

        // Check for top row blocking
        if (row == 1) {
            for (int i = -1; i < 2; i++) {
                if (col + i < 8 && col + i >= 0) {
                    if (colorCheck(board[0][col + i].getColor()) == true) {
                        return true;
                    }
                }
            }
            return false;
        }
        // check for bottom row blocking
        else if (row == 6) {
            for (int i = -1; i < 2; i++) {
                if (col + i < 8 && col + i >= 0) {
                    if (colorCheck(board[7][col + i].getColor()) == true) {
                        return true;
                    }
                }

            }
            return false;
        }
        // Check for first column blocking
        else if (col == 1) {
            for (int i = -1; i < 2; i++) {
                if (row + i < 8 && row + i >= 0) {
                    if (colorCheck(board[row + i][0].getColor()) == true) {
                        return true;
                    }
                }
            }
            return false;
        }
        // Check for last column blocking
        else if (col == 6) {
            for (int i = -1; i < 2; i++) {
                if (row + i < 8 && row + i >= 0) {
                    if (colorCheck(board[row + i][7].getColor()) == true) {
                        return true;
                    }
                }
            }
            return false;
        } else {
            return false;
        }
    }

    /**
     * Checking if player will join a new group when moving to location
     * @param board, a Board class object used to call board information and
     *      get the pieces grid
     * @param origin, a string passed by value used for getting the formatted starting
     *      coordinates of the current piece
     * @param destination, a string passed by value used for getting the formatted ending
     *      coordinates of the current piece
     * @return the truth value for if when the piece moves, it leaves the original group
     */
    boolean canGroup(Board board, String origin, String destination) {
        Board tempBoard = board.clone();
        // Gets both groups and compares same occuring pieces
        Vector<String> firstGroup = tempBoard.getGroupPieces(origin, color);
        tempBoard.move_piece(origin, destination);
        Vector<String> secondGroup = tempBoard.getGroupPieces(destination, color);

        for (int i = 0; i < firstGroup.size(); i++) {
            for (int j = 0; j < secondGroup.size(); j++) {

                if (Objects.equals(firstGroup.elementAt(i), secondGroup.elementAt(j)) || secondGroup.size() <= 1) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Checking if each direction of a player can cause a win
     * @param board, a Board class object used to call board information and
     *        get the pieces grid
     * @param winningCoords, a string vector passed by reference
     *        modifies the elements inside the vector
     * @return the truth value for a win occurring at a certain direction
     */
    boolean imminentWin(Board board, Vector<String> winningCoords) {


        // loop through entire board
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // coordColor holds the piece color at the given indices in board grid
                char coordColor = board.getBoard()[i][j].getColor();
                if (coordColor == color) {
                    // coords holds the formatted coordinates for board actions, assigned to
                    // iterators
                    // converted to string
                    String coords = String.valueOf((char) (j + 65)) + (8 - i);

                    // row holds the index value associated with the array's rows
                    int row = 8 - (coords.charAt(1) - '0');
                    // col holds the index value associated with the array's columns
                    int col = (coords.charAt(0)) - 65;

                    // Get each direction's possible distance
                    // vertCount holds the distance a piece can move vertically
                    int vertCount = board.calcPiecesVert(col);
                    // horiCount holds the distance a piece can move horizontally
                    int horiCount = board.calcPiecesHori(row);
                    // diagPosCount holds the distance a piece can move diagonally with positive
                    // slope
                    int diagPosCount = board.calcPiecesDiag(row, col, 1, -1);
                    // diagNegCount holds the distance a piece can move diagonally with negative
                    // slope
                    int diagNegCount = board.calcPiecesDiag(row, col, 1, 1);

                    // destinations is a size 8 array of strings for
                    // holding the formatted coordinates for each possible direction a player can
                    // move to
                    Vector<String> destinations = new Vector<String>();
                    // upwards direction
                    destinations.add(String.valueOf((char) (col + 65)) + (8 - (row + (-1 * vertCount))));
                    // downwards direction
                    destinations.add(String.valueOf((char) (col + 65)) + (8 - (row + vertCount)));
                    // right direction
                    destinations.add(String.valueOf((char) ((col + horiCount) + 65)) + (8 - row));
                    // left direction
                    destinations.add(String.valueOf((char) ((col) + (-1 * horiCount) + 65)) + (8 - row));
                    // upright direction
                    destinations.add(
                            String.valueOf((char) ((col + diagPosCount) + 65)) + (8 - (row + (-1 * diagPosCount))));
                    // downleft direction
                    destinations.add(
                            String.valueOf((char) ((col + (diagPosCount * -1)) + 65)) + (8 - (row + diagPosCount)));
                    // downright direction
                    destinations.add(String.valueOf((char) ((col + diagNegCount) + 65)) + (8 - (row + diagNegCount)));
                    // upleft direction
                    destinations.add(String.valueOf((char) ((col + (diagNegCount * -1)) + 65))
                            + (8 - (row + (-1 * diagNegCount))));

                    // check each piece direction for move strategies
                    for (int k = 0; k < 8; k++) {
                        // destination holds current destination being iterated through
                        String destination = destinations.elementAt(k);
                        if (board.validate_moveset(coords, destination)) {
                            // Is win going to happen in this future move
                            if (canWin(board, coords, destination)) {

                                winningCoords.add(destination);

                            }
                        }
                    }

                }
            }
        }

        return !winningCoords.isEmpty();

    }

    /**
     * Checks if row and column being searched through are out of bounds relative to 8x8 board
     * @param row, an int value used for checking row coordinate for board
     * @param col, an int value used for checking col coordinate for board
     * @return truth value if row and column are less than 8 and greater than or equal to 0
     */
    boolean boundsCheck(int row, int col) {
        return ((row < 8 && row >= 0) && (col < 8 && col >= 0));
    }

    /**
     * Checks to see if player can block an opponent's win from happening
     * @param board, a Board class object used to call board information and
     *        get the pieces grid
     * @param coordinates, a string passed by value used for getting location of
     *        piece on the board grid
     * @param self, a Player object pointer, getting the memory address of the current
     *        player in a turn
     * @param nextPlayer, a player object pointer, getting the memory address of the player
     *        who goes next turn
     * @return the truth value for if an opponent win is imminent and if
     *        the current player piece can block an opponent piece
     */
    boolean canThwart(Board board, String coordinates, Player self, Player nextPlayer) {
        // Gets all winning strategies from the opponent and stores them in vector
        Vector<String> winningCoords = new Vector<String>();
        boolean isWin = nextPlayer.imminentWin(board, winningCoords);
        // Loops through winning coordinates to compare them with destination
        for (String winningPos : winningCoords) {
            if (Objects.equals(winningPos, coordinates)) {
                return true;
            }
        }

        return false;
    }

}
