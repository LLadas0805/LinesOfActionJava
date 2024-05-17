package com.ladas.linesofaction;

import java.util.Scanner;

public class Human extends Player {

    private String origin;
    private String destination;

    public Human(String argIdentity, char argColor, int argScore, int argWins) {
        super(argIdentity, argColor, argScore, argWins);
    }

    public Human(String argIdentity) {
        super(argIdentity);
    }

    /**********************************************************************
     * Function Name: play
     * Purpose: Handles the human movement for a player
     * Parameters: board, a Board class object passed by reference, has the
     * board 2D Piece array member named piece modified. board
     * 
     * self, a Player pointer passed by value, has the memory address of
     * the current player (human)
     * 
     * nextPlayer, a Player pointer passed by value, has the memory address of
     * the next player (computer)
     * 
     * Return Value: the coordinates of where the player has moved the selected
     * piece,
     * a string value
     */
    String play(Board board, Player self, Player nextPlayer) {
        board.move_piece(getSelection(), getDestination());
        return "";
    }
}
