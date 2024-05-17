package com.ladas.linesofaction;

public class Computer extends Player  {


    public Computer(String argIdentity, char argColor, int argScore, int argWins) {
        super(argIdentity, argColor, argScore, argWins);
    }

    public Computer(String argIdentity) {
        super(argIdentity);
    }

   


    /**
     * Handles the computer movement for a player
     * @param board, a Board class object passed by reference, has the
     *      board 2D Piece array member named piece modified. board
     * @param self a Player pointer passed by value, has the memory address of
     *      the current player (computer)
     * @param nextPlayer, nextPlayer, a Player pointer passed by value, has the memory address of
     *      the next player (human)
     * @return the coordinates of where the player has moved the selected piece
     */
    String play(Board board, Player self, Player nextPlayer) {

        Piece bestPiece = strategize(board, self, nextPlayer);
        // Makes a move based on the piece with the best strategy
        board.move_piece(bestPiece.getOrigin(), bestPiece.getStratCoords());
        setSelection(bestPiece.getOrigin());
        setDestination(bestPiece.getStratCoords());

        return bestPiece.getStrategy();
    }
}
