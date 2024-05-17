package com.ladas.linesofaction;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Vector;

public class Serialization {

    /**
     * Default constructor for serialization class objects
     */
    public Serialization() {
        // Empty constructor
    }

    /**
     * Creates and formats a new save state text file for a tournament/round to load in
     * @param context
     * @param fileName, a string value which holds the name of a save file
     * @param players, integer array which holds player state values from tournament
     * @param nextPlayer, String array which holds next player state from tournament
     * @param board, String representing the game board to put in a text file
     * @return a boolean truth value for if a filename was valid for saving
     */
    public boolean saveGame(Context context, String fileName, int[] players, String[] nextPlayer, String board) {

        // Validate file name
        if (fileName == null || fileName.isEmpty() || fileName.length() > 32 || !isAlphaNum(fileName)) {
            return false;
        }

        // Append ".txt" extension
        fileName = fileName + ".txt";

        try {
            // Get the file directory using application context
            // Get the "saves" directory within the internal storage
            File savesDirectory = new File(context.getFilesDir(), "saves");

            // Construct the file path within the "saves" directory
            File savedFile = new File(savesDirectory, fileName);

            // Initialize FileWriter with the file
            FileWriter writer = new FileWriter(savedFile);

            // Write board state
            writer.write("Board:\n");
            int index = 0;
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    writer.write(board.charAt(index) == '.' ? "x " : Character.toLowerCase(board.charAt(index)) + " ");
                    index++;
                }
                writer.write("\n");
            }

            writer.write("\n");

            // Write player states
            writer.write("Human:\n");
            writer.write("Rounds won: " + players[1] + "\n");
            writer.write("Score: " + players[0] + "\n\n");

            writer.write("Computer:\n");
            writer.write("Rounds won: " + players[3] + "\n");
            writer.write("Score: " + players[2] + "\n\n");

            // Write next player
            writer.write("Next player: " + nextPlayer[0] + "\n");
            writer.write("Color: " + (nextPlayer[1]) + "\n\n");

            // Close the writer
            writer.close();

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Selects a save state and parses the information to store inside the arguments
     * @param context, Context object which holds the activity that called this function
     * @param players, a Player array passed by reference which is used to hold game state of tournament
     * @param nextPlayer, a Player object passed by reference, has the memory address of
     *      which Player will go next when the game loads in. Player object will be changed
     * @param board, a Board class object passed by reference, holds the state of the game board
     * @param selected, the name of the file the player selected to load in
     * @return index of the current player in the Players array
     * @throws IOException
     */
    public int loadGame(Context context, Player[] players, Player nextPlayer, Board board, String selected) throws IOException {

        int nextIndex = -1;

        String fileName = selected;

        // Get the "saves" directory within the internal storage
        File savesDirectory = new File(context.getFilesDir(), "saves");

        // Construct the file path within the "saves" directory
        File gameFile = new File(savesDirectory, fileName);


        // Open the file as an InputStream
        FileInputStream inputStream = new FileInputStream(gameFile);

        Scanner fileScanner = new Scanner(inputStream);

        System.out.println(fileScanner);
        Piece[][] tempBoard = new Piece[8][8];
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            // Goes through text file to parse game board state and set the Board object with this info
            if (line.equals("Board:")) {
                for (int i = 0; i < 8; ++i) {
                    String[] colors = fileScanner.nextLine().split(" ");
                    for (int j = 0; j < 8; ++j) {
                        char color = colors[j].charAt(0);
                        tempBoard[i][j] = new Piece(color == 'x' ? '.' : Character.toUpperCase(color));
                    }
                }
                board.setBoard(tempBoard);

            // Parses human information and puts it inside the first players element
            } else if (line.equals("Human:")) {
                int roundsWon = Integer.parseInt(fileScanner.nextLine().split(": ")[1]);
                int score = Integer.parseInt(fileScanner.nextLine().split(": ")[1]);
                players[0].setWins(roundsWon);
                players[0].setScore(score);
            // Parses computer information and puts it inside the second players element
            } else if (line.equals("Computer:")) {
                int roundsWon = Integer.parseInt(fileScanner.nextLine().split(": ")[1]);
                int score = Integer.parseInt(fileScanner.nextLine().split(": ")[1]);
                players[1].setWins(roundsWon);
                players[1].setScore(score);
            // Parses next player/current player information and puts it inside the Round's currplayer member
            } else if (line.startsWith("Next player:")) {

                String playerType = line.split(": ")[1];

                String color = fileScanner.nextLine().split(": ")[1];
                System.out.println("Type: " + playerType);
                System.out.println("Color: " + color);
                if (playerType.equals("Human")) {
                    nextIndex = 0;
                    if (color.equals("Black")) {
                        players[0].setColor('B');
                        players[1].setColor('W');
                    } else {
                        players[1].setColor('B');
                        players[0].setColor('W');
                    }
                } else {
                    nextIndex = 1;
                    if (color.equals("Black")) {
                        players[1].setColor('B');
                        players[0].setColor('W');
                    } else {
                        players[0].setColor('B');
                        players[1].setColor('W');
                    }
                }
            }

        }

        fileScanner.close();

        return nextIndex;
    }


    /**
     * To store the save text file names inside of a vector data structure for easy searching
     * @param context Context object which holds what activity causes this to be called
     * @return Vector<String> of file names in the save directory
     */
    public Vector<String> getFiles(Context context) {
        Vector<String> savedFiles = new Vector<>();

        File savesDirectory = new File(context.getFilesDir(), "saves");

        try {
            // Check if the saves directory exists
            if (savesDirectory.exists() && savesDirectory.isDirectory()) {
                // Get the list of files in the saves directory
                File[] files = savesDirectory.listFiles();
                if (files != null) {
                    for (File file : files) {
                        // Check if it's a text file before adding
                        if (file.isFile() && file.getName().toLowerCase().endsWith(".txt")) {
                            // Add the file name to the vector
                            savedFiles.add(file.getName());
                        }
                    }
                }
            } else {
                System.out.println("Saves directory doesn't exist or is not a directory.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return savedFiles;
    }

    /**
     * To check whether the name of a saved text file is comprised of letters or numbers only
     * @param fileName, a string passed by value. It holds the input
     *        of a player when asked to fill out a name for a new save file
     * @return a boolean, the truth value for whether the file name is alpha-numeric
     */
    public boolean isAlphaNum(String fileName) {
        for (char character : fileName.toCharArray()) {
            if (!Character.isLetterOrDigit(character)) {
                return false;
            }
        }
        return true;
    }



}
