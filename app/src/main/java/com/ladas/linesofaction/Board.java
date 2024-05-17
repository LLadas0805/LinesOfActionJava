package com.ladas.linesofaction;

import java.io.Serializable;
import java.util.Objects;
import java.util.Vector;

public class Board implements Cloneable, Serializable {

    private Piece[][] board = new Piece[8][8];

    /**
     Creates and formats a new board by placing pieces into the same piece locations of the clone argument
     @return a Board object that is a cloned version of what is passed in as an argument
     */
    @Override
    public Board clone() {
        try {
            // Perform shallow copy of the object
            Board clonedBoard = (Board) super.clone();

            // Perform deep copy for mutable fields
            clonedBoard.board = new Piece[8][8];
            for (int i = 0; i < 8; i++) {
                System.arraycopy(board[i], 0, clonedBoard.board[i], 0, board[i].length);
            }

            return clonedBoard;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Should never happen since we implement Cloneable
        }
    }

    /**
     Creates and formats a new board by placing pieces into default locations
     */
    public void initial_state() {
        // Initially fills the entire board with empty pieces
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Piece('.');
            }
        }

        // Outputs black pieces at the top and bottom of the board
        for (int i = 0; i < 8; i += 7) {
            for (int j = 1; j < 7; j++) {
                board[i][j] = new Piece('B');
            }
        }

        // Outputs white pieces at the left and right of the board
        for (int i = 1; i < 7; i++) {
            for (int j = 0; j < 8; j += 7) {
                board[i][j] = new Piece('W');
            }
        }
    }

    /**
     * default constructor for board;
     */
    public Board() {
        initial_state();
    }

    /**********************************************************************
     Checks to see if the input board coordinates are in the right format and range
     @param coordinates, a string value passed by value. Used for comparison
        relative to the board's range and syntax
     @return  The truth value for whether or not coordinates is the correct format or if
        it is out of bounds, a boolean
     */
    boolean validate_range(String coordinates) {

        if (coordinates.length() == 2) {
            // Checks to see if the row and column are either numbers and letters
            // respectively

            if (Character.isAlphabetic(coordinates.charAt(0)) && Character.isDigit(coordinates.charAt(1))) {

                int rowRange = coordinates.charAt(1) - '0';
                char colRange = coordinates.charAt(0);

                // Compares the rows and range value to the boundaries of the board
                if ((rowRange > 0 && rowRange < 9) && (colRange >= 'A' && colRange <= 'H')) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     Checks to see if the given piece at board coordinates are in range, and if they match the color of the player
     @param color, a char variable passed by value. Used for determining what
        color the current player is
     @param coordinates, a string passed by value. Used for comparison
        relative to the board's range and syntax
     @return  The truth value for whether or not coordinates is valid and
        the piece's color matches the color of the player, boolean
     */
    boolean validate_piece(char color, String coordinates) {

        if (validate_range(coordinates)) {

            Piece selected = board[8 - (coordinates.charAt(1) - '0')][(int) (coordinates.charAt(0)) - 65];
            if (color == selected.getColor()) {

                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     Handles which direction a piece should go and if it is a valid move to make
     @param origin, a string passed by value. Used for obtaining
            the start of a piece's move on the board
     @param destination, a string passed by value. Used for obtaining
            the user's end coordinates for of a piece's move on the board
     @return The truth value for whether or not destination is valid and if the move itself is
        available to perform
     */
    boolean validate_moveset(String origin, String destination) {

        if (Objects.equals(origin, destination)) {
            return false;
        }

        if (validate_range(destination)) {
            // Piece is in the same vertical line as destination
            if (origin.charAt(0) == destination.charAt(0)) {
                return validate_vertical(origin, destination);
            }

            // Piece is in the same horizontal line as destination
            else if (origin.charAt(1) == destination.charAt(1)) {
                return validate_horizontal(origin, destination);
            }
            // Piece is in the same diagonal line as destination
            else if (origin.charAt(0) != destination.charAt(0) && origin.charAt(1) != destination.charAt(1)) {

                return validate_diagonal(origin, destination);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     Gets the number of pieces in a given row on the game board
     @param row, int passed by value. Used for finding which row in the board to count the pieces from
     @return The integer number of pieces that are inside the row
     */
    int calcPiecesHori(int row) {
        int pieceCount = 0;
        for (int i = 0; i < 8; i++) {
            if (board[row][i].getColor() == 'W' || board[row][i].getColor() == 'B') {
                pieceCount += 1;
            }
        }
        return pieceCount;
    }

    /**
     Gets the number of pieces in a given column on the game board
     @param col, int passed by value. Used for finding which column in the board to count the pieces from
     @return The integer number of pieces that are inside the column
     */
    int calcPiecesVert(int col) {
        int pieceCount = 0;
        for (int i = 0; i < 8; i++) {
            if (board[i][col].getColor() == 'W' || board[i][col].getColor() == 'B') {
                pieceCount += 1;
            }
        }
        return pieceCount;
    }

    /**
     Gets the number of pieces in a given diagonal line on the game board
     @param begRow, int passed by value. Used for finding which row in the board
            to count the pieces from
     @param begCol, int passed by value. Used for finding which column in the board
            to count the pieces from
     @param slope_X, int passed by value. Used for finding which direction the line
            is heading for the rows
     @param slope_Y, int passed by value. Used for finding which direction the line
            is heading for the columns
     @return The integer number of pieces that are inside the diagonal line
     */
    int calcPiecesDiag(int begRow, int begCol, int slope_X, int slope_Y) {

        // pieceCount holds the number of pieces in a diagonal line, initially assigned
        // as 0
        int pieceCount = 0;

        // Iterate over right side of the diagonal line
        for (int i = 0; i <= 8; i++) {

            // row holds the current row for searching line, assigned current row to
            // variable
            int row = begRow + (i * slope_Y);
            // col holds the current column for searching line, assigned current column to
            // variable
            int col = begCol + (i * slope_X);

            // Ensure row and column are within the bounds of the board
            if (row >= 0 && row < 8 && col >= 0 && col < 8) {
                if (board[row][col].getColor() == 'W' || board[row][col].getColor() == 'B') {
                    // assigned pieceCount being incremented
                    pieceCount += 1;
                }
            } else {
                break;
            }
        }
        // Iterate over left side of the diagonal line
        for (int i = 0; i <= 8; i++) {
            // row holds the current row for searching line, assigned current row to
            // variable
            int row = begRow + (-1 * (i * slope_Y));
            // col holds the current column for searching line, assigned current column to
            // variable
            int col = begCol + (-1 * (i * slope_X));

            // Ensure row and column are within the bounds of the board
            if (row >= 0 && row < 8 && col >= 0 && col < 8) {
                if (board[row][col].getColor() == 'W' || board[row][col].getColor() == 'B') {
                    // assigned pieceCount being incremented
                    pieceCount += 1;
                }
            } else {
                break;
            }
        }
        return pieceCount - 1;
    }

    /**
     Handles if a vertical move given the two coordinates is possible,
     @param origin, a string passed by value. Used for obtaining
            the start of a piece's move on the board
     @param destination, a string passed by value. Used for obtaining
            the user's end coordinates for of a piece's move on the board
     @return The truth value for whether or not moving vertically is valid
     */
    boolean validate_vertical(String origin, String destination) {
        // line holds the column location in the board
        // assign column element of origin's coords, converted to integer, to line
        int line = (origin.charAt(0)) - 65;
        // pieceCount holds the amount of pieces there are in the column
        // assign zero to line since no pieces found yet
        int pieceCount = 0;
        for (int i = 0; i < 8; i++) {
            if (board[i][line].getColor() == 'W' || board[i][line].getColor() == 'B') {
                // assign the total number of pieces in the column + 1 to pieceCount
                pieceCount += 1;
            }
        }

        // direction stores if the move is going up (-1) or down (1)
        // assign zero to direction for now
        int direction = 0;
        // begRow stores the row index for where the start of the move occurs in the
        // board
        int begRow = 8 - (origin.charAt(1) - '0');
        // begRow stores the row index for where the end of the move lands in the board
        int endRow = 8 - (destination.charAt(1) - '0');

        // Direction up
        if (begRow > endRow) {
            // assign up to direction
            direction = -1;
        }
        // Direction down
        else {
            // assign down to direction
            direction = 1;
        }

        // dist holds the distance in board space between two coordinates
        // assign the difference between the beginning row and end row value to dist
        int dist = (Math.abs(begRow - endRow));

        // Checks for blocking pieces
        for (int row = 0; row < dist; row++) {
            // colorDiff holds the truth value for if the piece located on the row in the
            // given column is a different color
            boolean colorDiff = board[begRow + (direction * row)][line].getColor() != board[begRow][line].getColor();
            // emptyPos holds the truth value for if the row in the given column is empty
            // space
            boolean emptyPos = board[begRow + (direction * row)][line].getColor() != '.';

            if (colorDiff && emptyPos) {
                return false;
            }

        }

        // colorDiff holds the truth value for if the piece located on the ending row in
        // the given column is a different color
        boolean colorDiff = board[endRow][line].getColor() != board[begRow][line].getColor();
        // distCmp holds the truth value for if the distance between the two rows is the
        // same as the total number of pieces in column
        boolean distComp = (Math.abs(begRow - endRow)) == pieceCount;

        // Final check for if the spot we are landing on is the same color as intial
        // piece AND if its the right amount of space
        return colorDiff && distComp;
    }

    /**
     Handles if a horizontal move given the two coordinates is possible,
     @param origin, a string passed by value. Used for obtaining
            the start of a piece's move on the board
     @param destination, a string passed by value. Used for obtaining
            the user's end coordinates for of a piece's move on the board
     @return The truth value for whether or not moving horizontally is valid
     */
    boolean validate_horizontal(String origin, String destination) {
        // line holds the row location in the board
        // assign row element of origin's coords, converted to integer, to line
        int line = 8 - (origin.charAt(1) - '0');
        ;
        // pieceCount holds the amount of pieces there are in the row
        // assign zero to line since no pieces found yet
        int pieceCount = 0;
        for (int i = 0; i < 8; i++) {
            if (board[line][i].getColor() == 'W' || board[line][i].getColor() == 'B') {
                // assign the total number of pieces in the row + 1 to pieceCount
                pieceCount += 1;
            }
        }

        // direction stores if the move is going left (-1) or right (1)
        // assign zero to direction for now
        int direction = 0;
        // begCol stores the column index for where the start of the move occurs in the
        // board
        int begCol = (origin.charAt(0)) - 65;
        // endCol stores the column index for where the end of the move occurs in the
        // board
        int endCol = (destination.charAt(0)) - 65;

        if (begCol < endCol) {
            // assign right to direction
            direction = 1;
        } else {
            // assign down to direction
            direction = -1;
        }

        // dist holds the distance in board space between two coordinates
        // assign the difference between the beginning row and end row value to dist
        int dist = (Math.abs(begCol - endCol));

        // Checks for blocking pieces
        for (int col = 0; col < dist; col++) {
            // colorDiff holds the truth value for if the piece located on the column in the
            // given row is a different color
            boolean colorDiff = board[line][begCol + (direction * col)].getColor() != board[line][begCol].getColor();
            // emptyPos holds the truth value for if the column in the given row is empty
            // space
            boolean emptyPos = board[line][begCol + (direction * col)].getColor() != '.';

            if (colorDiff && emptyPos) {

                return false;
            }


        }
        // colorDiff holds the truth value for if the piece located on the final column
        // in the given row is a different color
        boolean colorDiff = board[line][endCol].getColor() != board[line][begCol].getColor();
        // distCmp holds the truth value for if the distance between the two columns is
        // the same as the total number of pieces in row
        boolean distComp = (Math.abs(begCol - endCol)) == pieceCount;

        // Final check for if spot we are landing on is the same color as initial piece
        // AND if its the right amount of space
        return colorDiff && distComp;

    }

    /**
     Handles if a diagonal move given the two coordinates is valid
     @param origin, a String passed by value. Used for obtaining
            the start of a piece's move on the board
     @param destination, a string passed by value. Used for obtaining
            the user's end coordinates for of a piece's move on the board
     @return The truth value for whether or not moving diagonally is valid
     */
    boolean validate_diagonal(String origin, String destination) {

        // begRow stores the row index for where the start of the move occurs in the
        // board
        int begRow = 8 - (origin.charAt(1) - '0');
        ;
        // begRow stores the row index for where the end of the move lands in the board
        int endRow = 8 - (destination.charAt(1) - '0');
        ;
        // begCol stores the column index for where the start of the move occurs in the
        // board
        int begCol = (origin.charAt(0)) - 65;
        // endCol stores the column index for where the end of the move occurs in the
        // board
        int endCol = (destination.charAt(0)) - 65;

        // pieceCount holds the amount of pieces there are in the row
        // assign zero to line since no pieces found yet
        int pieceCount = 0;

        // slope_X holds the direction for the row will go in the diagonal line (-1) up
        // or (1) down
        int slope_X = 0;
        // slope_Y holds the direction for the column will go in the diagonal line (-1)
        // left or (1) right
        int slope_Y = 0;

        // The line goes up and to the right
        if ((begRow > endRow && begCol < endCol)) {
            // assign up to slope_X
            slope_X = -1;
            // assign right to slope_Y
            slope_Y = 1;
        }
        // The line goes down to the left
        else if ((begRow < endRow && begCol > endCol)) {
            // assign down to slope_X
            slope_X = 1;
            // assign left to slope_Y
            slope_Y = -1;
        }
        // The line goes down and to the right
        else if ((begRow < endRow && begCol < endCol)) {
            // assign down to slope_X
            slope_X = 1;
            // assign right to slope_Y
            slope_Y = 1;
        }
        // The line goes up and to the left
        else if ((begRow > endRow && begCol > endCol)) {
            // assign up to slope_X
            slope_X = -1;
            // assign left to slope_Y
            slope_Y = -1;
        }

        // maxDistanceToEnd holds the max distance for iteration from beginning to end
        int maxDistanceToEnd = Math.max(8 - begCol, 8 - endCol);
        // maxDistanceToStart holds the max distance for iteration from row to column
        int maxDistanceToStart = Math.max(begRow + 1, begCol + 1);
        // maxDistance holds the max distance between the start and end distance
        int maxDistance = Math.max(maxDistanceToEnd, maxDistanceToStart);

        // Iterate over the entire diagonal line
        for (int i = -maxDistance; i <= maxDistance; i++) {
            int row = begRow + (i * slope_Y);
            int col = begCol + (i * slope_X);
            // Ensure row and column are within the bounds of the board
            if (row >= 0 && row < 8 && col >= 0 && col < 8) {
                if (board[row][col].getColor() == 'W' || board[row][col].getColor() == 'B') {
                    pieceCount += 1;
                }
            }
        }
        // dist holds the distance in board space between two coordinates
        // assign the difference between the beginning row and end row value to dist
        int dist = (Math.abs(begRow - endRow));
        // Checks for blocking pieces
        for (int diagIter = 0; diagIter < dist; diagIter++) {
            // colorDiff holds the truth value for if the piece located on the column and
            // row is a different color
            boolean colorDiff = board[begRow + (slope_X * diagIter)][begCol + (slope_Y * diagIter)]
                    .getColor() != board[begRow][begCol].getColor();
            // emptyPos holds the truth value for if the column and row is empty space
            boolean emptyPos = board[begRow + (slope_X * diagIter)][begCol + (slope_Y * diagIter)].getColor() != '.';

            if (colorDiff && emptyPos) {
                return false;
            }


        }
        // colorDiff holds the truth value for if the piece located on the final column
        // and row is a different color
        boolean colorDiff = board[endRow][endCol].getColor() != board[begRow][begCol].getColor();
        // distCmp holds the truth value for if the distance between the two columns and
        // rows is the same as the total number of pieces in row
        boolean distComp = (Math.abs(begRow - endRow) == pieceCount) && (Math.abs(begCol - endCol) == pieceCount);
        // Final check for if the spot we are landing on has the same color as the
        // initial piece AND if it's the right amount of space

        return colorDiff && distComp;
    }

    /**
     Handles the movement of a piece on the board, taking into account captures as well
     @param origin, a string passed by value. Used for obtaining
        the start of a piece's move on the board
     @param destination, a string passed by value. Used for obtaining
        the user's end coordinates for of a piece's move on the board
     */
    void move_piece(String origin, String destination) {

        int begCol = (origin.charAt(0)) - 65;
        int endCol = (destination.charAt(0)) - 65;
        int begRow = 8 - (origin.charAt(1) - '0');
        int endRow = 8 - (destination.charAt(1) - '0');

        Piece selected = board[begRow][begCol];
        board[begRow][begCol] = new Piece('.');
        board[endRow][endCol] = selected;
    }

    /**
     Handles if a diagonal move given the two coordinates is possible, checks for blocking pieces
        and if move distance was valid
     @param color, a char passed by value. Used for determining which color
        a piece should have to be grouped together
     @param coordinates, a string passed by value. Used for obtaining
        the start of a piece's move on the board
     @param pieceCount, an integer passed by value. Used for obtaining the total
        number of pieces associated with a player
     @return The truth value for whether or not the group that was found has
        the same integer value as the total amount of pieces for a player on the board
     */
    boolean winCondition(char color, String coordinates, int pieceCount) {
        boolean[][] visited = new boolean[8][8];
        int col = (coordinates.charAt(0)) - 65;
        int row = 8 - (coordinates.charAt(1) - '0');

        Vector<String> pieces = new Vector<String>();
        int groupSize = isGrouped(row, col, color, visited, pieces);
        return groupSize == pieceCount;

    }

    /**
     Stores all of the piece coordinates in a single group
     @param coordinates, a string passed by value. Used for obtaining
        the start of a piece's move on the board
     @param color, a char passed by value. Used for determining which color
        a piece should have to be grouped together
     @return A Vector<String>, storing each piece coordinate in a group
     */
    Vector<String> getGroupPieces(String coordinates, char color) {

        boolean[][] visited = new boolean[8][8];
        Vector<String> pieces = new Vector<String>();

        int row = 8 - (coordinates.charAt(1) - '0');
        int col = (coordinates.charAt(0)) - 65;

        isGrouped(row, col, color, visited, pieces);

        return pieces;

    }

    /**
     Keeps count of how many pieces of the same color are inside a single group
     @param row, an integer passed by value. Used for determining what
        row to look at for checking if a piece is in the group
     @param col, an passed by value. Used for determining which column
        to look at for checking if a piece is in the group
     @param color, a char passed by value. Used for determining which color
        a piece should have to be grouped together
     @param visited, a boolean[][] passed by reference, containing
        the truth values for if a piece on the board has been visited by the group.
        Modifies the truth values inside the elements of the array
     @param pieces, A Vector<String> passed by reference, containing the coordinates
        of each element in a group, reference modifies the string values for elements
     @return an integer value, the amount of pieces in a group,
     */
    int isGrouped(int row, int col, char color, boolean[][] visited, Vector<String> pieces) {
        // Check if the current position is within the board boundaries
        if (row < 0 || row >= 8 || col < 0 || col >= 8) {
            return 0;
        }

        // Check if the current position has already been visited
        if (visited[row][col]) {
            return 0;
        }

        // Check if the piece at the current position has the same color
        if (board[row][col].getColor() != color) {
            return 0;
        }

        // Mark the current position as visited
        visited[row][col] = true;
        // coords converts the row and column parameters into properly formatted
        // coordinates
        // assign ascii value of column + row as string to coords
        String coords = String.valueOf((char) (col + 65)) + (row + 1);
        pieces.add(coords);

        // count holds the total amount of pieces in a group, assign 1 to variable
        int count = 1;
        // assign down direction for isGrouped to count
        count += isGrouped(row + 1, col, color, visited, pieces);
        // assign up direction for isGrouped to count
        count += isGrouped(row - 1, col, color, visited, pieces);
        // assign right direction for isGrouped to count
        count += isGrouped(row, col + 1, color, visited, pieces);
        // assign left direction for isGrouped to count
        count += isGrouped(row, col - 1, color, visited, pieces);
        // assign diagonal down right for isGrouped to count
        count += isGrouped(row + 1, col + 1, color, visited, pieces);
        // assign diagonal up right for isGrouped to count
        count += isGrouped(row - 1, col + 1, color, visited, pieces);
        // assign diagonal down left for isGrouped to count
        count += isGrouped(row + 1, col - 1, color, visited, pieces);
        // assign diagonal up left for isGrouped to count
        count += isGrouped(row - 1, col - 1, color, visited, pieces);

        return count;

    }

    /**
     To obtain all of the piece's information in a board
     @return an 8x8 Piece[][] which represents the game board
     * Algorithm: none
     * Reference: none
     */
    Piece[][] getBoard() {
        return board;
    }

    /**
     To set the board member of the class to an argument of a game board
     @param inBoard, an 8x8 Piece[][] passed by reference. It holds the list of pieces to call information from
     */
    void setBoard(Piece[][] inBoard) {
        board = inBoard.clone();
    }

}
