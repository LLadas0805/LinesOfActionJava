package com.ladas.linesofaction;
public class Piece {

    private char color;
    private String strategy;
    private String stratCoords;
    private String origin;



    /**
     * To initialize private members of the Piece class when a Piece object is created
     * @param argColor , a character passed by value which holds what team a piece
     *      is associated with
     */
    public Piece(char argColor) {
        color = argColor;
        strategy = "";
        stratCoords = "";
        origin = "";
    }

    /**
     * To obtain the color that a piece is associated with
     * @return The team/side a piece is on, a char value
     */
    char getColor() {
        return color;
    }


    /**
     * To find the best move on the board that a piece can take
     * @return The method in which the piece will take in a given turn
     *         a string value
     */
    String getStrategy() {
        return strategy;
    }

    /**
     * To assign the best strategy to the strategy private member
     * @param argStrategy , a string passed by value, which holds the
     *        strategy that will be assigned to the private member
     */
    void setStrategy(String argStrategy) {
        strategy = argStrategy;
    }

    /**
     * To obtain the location on the board where the strategy will
     * cause the piece to move to
     * @return The coordinates associated with the best strategy, a string value
     */
    String getStratCoords() {
        return stratCoords;
    }

    /**
     * To assign a destination value to the stratCoords private member string
     * @param argStratCoords, a string passed by value. It holds the coordinates
     *        that are associated with where a strategy will take a piece on
     *        the board
     */
    void setStratCoords(String argStratCoords) {
        stratCoords = argStratCoords;
    }

    /**
     * To obtain the start location of a piece on the board before making a move
     * @return The coordinates associated with the starting position of a piece
     *         before a strategy is acted on, a string value
     */
    String getOrigin() {
        return origin;
    }

    /**
     * To set a new coordinates reference to the piece on the board before it makes a move
     * @param argOrigin, a string passed by value, it holds the initial location
     *        of the piece before it acts on a strategy
     */
    void setOrigin(String argOrigin) {
        origin = argOrigin;
    }

}
