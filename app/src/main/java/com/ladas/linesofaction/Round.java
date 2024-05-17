package com.ladas.linesofaction;

import static java.lang.System.exit;

import java.io.IOException;
import java.io.Serializable;
import java.util.Random;
import java.util.Scanner;

public class Round implements Serializable {

    private Player currPlayer;
    private Player[] players;

    /**
     * To initialize private members of the Round class when a Round object is created
     * @param argPlayers, a double Player pointer passed by value which stores the memory
     *      address
     */
    Round(Player[] argPlayers) {
        players = argPlayers;
        currPlayer = null;

    }

    /**
     * Sets the current player if a user starts a match from a load
     * @param index, holds what player should be selected as the current player
     */
    void save_player(int index) {
        currPlayer = players[index];
    }

    /**
     * Determines which player will start first at the beginning of a round
     * @return a boolean value, true if not a tie, false if current wins is a tie
     */
    boolean starting_player() {

        // Picks first player in the tournament roster if they have the most round wins
        if (players[0].getWins() > players[1].getWins()) {
            players[0].setColor('B');
            players[1].setColor('W');
            currPlayer = players[0];
            return true;
        }
        // Picks second player in the tournament roster if they have the most round wins
        else if (players[0].getWins() < players[1].getWins()) {
            players[0].setColor('W');
            players[1].setColor('B');
            currPlayer = players[1];
            return true;
        }
        // If a tie, coin flip will occur to determine first turn
        else {
            return false;
        }
    }

    /**
     * Handles what player should go based on correct guesses in a coin flip
     * @param coinflip, an int value which represents heads (1) or tails (2) guess from player
     */
    void flip (int coinflip) {
        // Seeds random number generator with current time
        Random random = new Random(System.currentTimeMillis());
        // Declaring the variable coin, assigning it to a random integer 1 or 2 (1)
        // heads or (2) tails
        int coin = random.nextInt(2) + 1;
        // Holds the predicted side for a coin toss (1) heads or (2) tails


        if (coin == 2) {
            Log.AddMessage("Coin landed on tails, ");
        } else {
            Log.AddMessage("Coin landed on heads, ");
        }

        // If player has guessed correctly, they will go first
        if (coinflip == coin) {
            Log.AddMessage("Human goes first\n");
            players[0].setColor('B');
            players[1].setColor('W');
            currPlayer = players[0];
        }
        // If player has guessed incorrectly, the opponent goes first
        else {
            Log.AddMessage("Computer goes first\n");
            players[0].setColor('W');
            players[1].setColor('B');
            currPlayer = players[1];
        }
    }

    /**
     * Handles which player will have the next turn in a round.
     * @return A Player class pointer, a memory address associated with a declared player object
     */
    Player swap() {

        if (currPlayer == players[0]) {
            return players[1];
        } else {
            return players[0];
        }

    }

    /**
     * Updates player information after a win has been reached
     * @param board is a board object which holds board game state
     */
    void setWinStates(Board board) {
        currPlayer.setWins(currPlayer.getWins() + 1);
        int score = currPlayer.getPieces(board.getBoard()) - swap().getPieces(board.getBoard());
        currPlayer.setScore(currPlayer.getScore() + score);
    }

    /**
     * To obtain the current round's win state
     * @param board is a board object which holds board game state
     * @return The win condition of the current round, a truth value
     */
    boolean isRoundWon(Board board) {

        return (currPlayer.getPieces(board.getBoard())) <= 0 || board.winCondition(currPlayer.getColor(),
                currPlayer.getDestination(), currPlayer.getPieces(board.getBoard()));

    }

    /**
     * Gets current player in a round
     * @return player object for current player state
     */
    Player getCurrPlayer() {
        return currPlayer;
    }

    /**
     * Sets current player in a round
     * @param argPlayer player object for current player state
     */
    void setCurrPlayer(Player argPlayer) {
        currPlayer = argPlayer;
    }

    /**
     * Gets all players in a round
     * @return player object array for current player states
     */
    Player[] getPlayers() {
        return players;
    }

}
