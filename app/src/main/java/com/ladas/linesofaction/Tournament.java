package com.ladas.linesofaction;

import java.io.IOException;
import java.util.Scanner;
import java.io.Serializable;

public class Tournament implements Serializable {
    private Player[] players = new Player[2];

    public Tournament() {
        players[0] = new Human("Human");
        players[1] = new Computer("Computer");
    }

    /**
     * Gets all players in a tournament
     * @return player object array for current player states
     */
    Player[] getPlayers() {
        return players;
    }
}
