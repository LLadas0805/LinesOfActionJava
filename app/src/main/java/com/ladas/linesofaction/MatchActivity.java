package com.ladas.linesofaction;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class MatchActivity extends AppCompatActivity {


    /**
     * Displays a popup to play heads and tails for determining starting player in a game
     * @param activity, is a Activity object which holds a reference to match controller
     * @param callback, CoinFlipCallback interface reference
     */
    private void coinPopup(Activity activity, CoinFlipCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Coin Flip");
        builder.setMessage("Choose heads or tails:");
        Log.AddMessage("Coin Flip, Choose heads or tails");
        builder.setCancelable(false);


        builder.setPositiveButton("Heads", (dialog, which) -> {
            // Invoke the callback with the result
            Log.AddMessage("Human guessed heads");
            callback.onCoinFlipped(1);


            // Dismiss the dialog
            dialog.dismiss();
        });

        builder.setNegativeButton("Tails", (dialog, which) -> {
            // Invoke the callback with the result
            Log.AddMessage("Human guessed tails");
            callback.onCoinFlipped(2);



            // Dismiss the dialog
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public interface CoinFlipCallback {
        void onCoinFlipped(int guess);
    }

    /**
     * Popup for user when they click on load game button in main menu, asks user to pick a save
     * @param activity, is a Activity object which holds a reference to match controller
     * @param round, is a Round object which contains player and game related states
     * @param callback, LoadPopupCallback interface reference
     */
    private void loadPopup(Activity activity, Round round, LoadPopupCallback callback) {
        Serialization serial = new Serialization();
        String[] fileNames = serial.getFiles(activity).toArray(new String[0]);

        for (String fileName : fileNames) {
            System.out.println(fileName);
        }

        Board board = new Board();

        if (fileNames.length > 0) {
            // Create the dialog with close functionality
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Select Save File");

            Log.AddMessage("Please select Save File:");
            TextView logText = findViewById(R.id.log_text);
            logText.setText(Log.FormatLog());
            builder.setItems(fileNames, (dialog, which) -> {
                // Load logic for the selected file
                String selectedFile = fileNames[which];

                try {
                    int index = serial.loadGame(MatchActivity.this, round.getPlayers(), round.getCurrPlayer(), board, selectedFile);
                    round.save_player(index);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // Close the dialog after loading
                dialog.dismiss();

                // Pass the loaded board to the callback
                callback.onBoardLoaded(board);
            });

            // Set dialog to not be cancelable by clicking outside of it
            builder.setCancelable(false);

            builder.show();
        } else {
            board.initial_state();
            callback.onBoardLoaded(board);
        }
    }

    public interface LoadPopupCallback {
        void onBoardLoaded(Board board);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_match);

        // Gets whether this tournament should be new or loaded in
        String tournamentType = getIntent().getStringExtra("tournament_selection");
        Tournament tournament = new Tournament();
        Round currRound = new Round(tournament.getPlayers());

        TextView logText = findViewById(R.id.log_text);


        TableLayout tableLayout = findViewById(R.id.tableLayout);
        final Board[] currentBoard = new Board[1];

        // Callback function for
        LoadPopupCallback callback = board -> {
            currentBoard[0] = board;

            // Display board grid and update the piece locations
            BoardView boardView = new BoardView();
            boardView.display_board(MatchActivity.this, currRound, board, tableLayout);

            currentBoard[0] = board;

            TextView currPlayer = findViewById(R.id.currplayer);
            // Set current player info on the match view
            if (currRound.getCurrPlayer() != null) {
                String currPlayerText = currRound.getCurrPlayer().getIdentity() + "'s turn - ";

                if (currRound.getCurrPlayer().getColor() == 'B') {
                    currPlayerText += "Black";
                } else {
                    currPlayerText += "White";
                }
                currPlayer.setText(currPlayerText);
                Log.AddMessage(currPlayerText);
            } else {
                currRound.setCurrPlayer(currRound.getPlayers()[0]);
            }
            // If computer is current player, let it make its move and update board
            if (currRound.getCurrPlayer().getIdentity().equals("Computer")) {

                String strategy = currRound.getCurrPlayer().play(currentBoard[0], currRound.getCurrPlayer(), currRound.swap());
                currRound.getCurrPlayer().setMoves(2);
                String strategyText = "-";
                strategyText = "Move piece at " + currRound.getCurrPlayer().getSelection() + " to space " +
                        currRound.getCurrPlayer().getDestination() + " (" + strategy + ")\n";
                TextView helpText = findViewById(R.id.help_text);
                helpText.setText(strategyText);

                boardView.display_board(MatchActivity.this, currRound, currentBoard[0], tableLayout);
                // Check to see if win condition has been met
                if (currRound.isRoundWon(currentBoard[0])) {
                    currRound.setWinStates(currentBoard[0]);

                    TextView humanScore = findViewById(R.id.humanscore);
                    humanScore.setText(String.valueOf(tournament.getPlayers()[0].getScore()));

                    TextView humanWins = findViewById(R.id.humanwins);
                    humanWins.setText(String.valueOf(tournament.getPlayers()[0].getWins()));

                    TextView computerScore = findViewById(R.id.computerscore);
                    computerScore.setText(String.valueOf(tournament.getPlayers()[1].getScore()));

                    TextView computerWins = findViewById(R.id.computerwins);
                    computerWins.setText(String.valueOf(tournament.getPlayers()[1].getWins()));

                    Log.AddMessage(currRound.getCurrPlayer().getIdentity() + " wins the round!");
                    Log.AddMessage("Press continue to start next round or press exit to end the tournament");
                }
                Log.AddMessage("Press continue to start next turn or press exit to save and take a break");
            }


            // Update player score on the match view
            TextView humanScore = findViewById(R.id.humanscore);
            humanScore.setText(String.valueOf(tournament.getPlayers()[0].getScore()));

            TextView humanWins = findViewById(R.id.humanwins);
            humanWins.setText(String.valueOf(tournament.getPlayers()[0].getWins()));

            TextView computerScore = findViewById(R.id.computerscore);
            computerScore.setText(String.valueOf(tournament.getPlayers()[1].getScore()));

            TextView computerWins = findViewById(R.id.computerwins);
            computerWins.setText(String.valueOf(tournament.getPlayers()[1].getWins()));

            logText.setText(Log.FormatLog());

        };

        // Function for dealing with starting player when tie
        CoinFlipCallback coinCall = guess -> {
            // Calculate starting player from coin flip guess
            currRound.flip(guess);
            // Update game state info on the match view
            String currPlayerText = currRound.getCurrPlayer().getIdentity() + "'s turn - ";
            if (currRound.getCurrPlayer().getColor() == 'B') {
                currPlayerText += "Black";
            } else {
                currPlayerText += "White";
            }
            TextView currPlayer = findViewById(R.id.currplayer);
            currPlayer.setText(currPlayerText);
            logText.setText(Log.FormatLog());
            // Checks to see if current player is computer, if so, make the moves
            if (currRound.getCurrPlayer().getIdentity().equals("Computer")) {
                String strategy = currRound.getCurrPlayer().play(currentBoard[0], currRound.getCurrPlayer(), currRound.swap());
                currRound.getCurrPlayer().setMoves(2);
                String strategyText = "-";
                strategyText = "Move piece at " + currRound.getCurrPlayer().getSelection() + " to space " +
                        currRound.getCurrPlayer().getDestination() + " (" + strategy + ")\n";
                TextView helpText = findViewById(R.id.help_text);
                helpText.setText(strategyText);
                BoardView boardView = new BoardView();
                boardView.display_board(MatchActivity.this, currRound, currentBoard[0], tableLayout);
                // Checks to see if win condition has been met or not
                if (currRound.isRoundWon(currentBoard[0])) {
                    currRound.setWinStates(currentBoard[0]);

                    TextView humanScore = findViewById(R.id.humanscore);
                    humanScore.setText(String.valueOf(tournament.getPlayers()[0].getScore()));

                    TextView humanWins = findViewById(R.id.humanwins);
                    humanWins.setText(String.valueOf(tournament.getPlayers()[0].getWins()));

                    TextView computerScore = findViewById(R.id.computerscore);
                    computerScore.setText(String.valueOf(tournament.getPlayers()[1].getScore()));

                    TextView computerWins = findViewById(R.id.computerwins);
                    computerWins.setText(String.valueOf(tournament.getPlayers()[1].getWins()));

                    Log.AddMessage(currRound.getCurrPlayer().getIdentity() + " wins the round!");
                    Log.AddMessage("Press continue to start next round or press exit to end the tournament");
                }
                Log.AddMessage("Press continue to start next turn or press exit to save and take a break");
            }
        };

        // Handles what to do depending on a new game or loaded game
        assert tournamentType != null;
        if (tournamentType.equals("load")) {
            loadPopup(this, currRound, callback);
            tournamentType = "";
        } else {
            Board board = new Board();
            board.initial_state();

            if (!currRound.starting_player()) {
                coinPopup(this, coinCall);
            }

            callback.onBoardLoaded(board);
        }

        // Add event listeners to the buttons


        Button helpButton = findViewById(R.id.help_button);

        // Display strategies to the game log and views in the GUI, also highlight pieces on board
        helpButton.setOnClickListener(view -> {
            // Cannot select button if player moves were made
            if (currRound.getCurrPlayer().getMoves() != 2) {

                TextView helpText = findViewById(R.id.help_text);
                Piece strategy = currRound.getCurrPlayer().strategize(currentBoard[0], currRound.getCurrPlayer(), currRound.swap());
                String strategyText = "Move piece at " + strategy.getOrigin() + " to space " + strategy.getStratCoords() + " (" + strategy.getStrategy() + ")\n";
                helpText.setText(strategyText);

                Button origButton = tableLayout.findViewWithTag(strategy.getOrigin());
                Button destButton = tableLayout.findViewWithTag(strategy.getStratCoords());

                // Get the existing LayerDrawable from the button
                LayerDrawable origLayerDrawable = (LayerDrawable) origButton.getBackground();
                LayerDrawable destLayerDrawable = (LayerDrawable) destButton.getBackground();

                // Get the index of the background layer in the LayerDrawable
                int backgroundIndex = 0;

                // Change the background color of the buttons without modifying the LayerDrawable
                Drawable origBackgroundDrawable = origLayerDrawable.getDrawable(backgroundIndex);
                if (origBackgroundDrawable instanceof ColorDrawable) {
                    ((ColorDrawable) origBackgroundDrawable).setColor(ContextCompat.getColor(this, R.color.selected));
                }

                Drawable destBackgroundDrawable = destLayerDrawable.getDrawable(backgroundIndex);
                if (destBackgroundDrawable instanceof ColorDrawable) {
                    ((ColorDrawable) destBackgroundDrawable).setColor(ContextCompat.getColor(this, R.color.selected));
                }

                logText.setText(Log.FormatLog());

            }

        });

        // Add listener for exiting a tournament or saving a tournament
        Button exitButton = findViewById(R.id.exit_button);

        exitButton.setOnClickListener(view -> {
            // Only able to select exit when player has finished moves
            if (currRound.getCurrPlayer().getMoves() == 2) {
                // If round has been won, this triggers end of tournament and moves to new activity
                if (currRound.isRoundWon(currentBoard[0])) {

                    Intent intent = new Intent(MatchActivity.this, WinActivity.class);
                    intent.putExtra("human_score", currRound.getPlayers()[0].getScore());
                    intent.putExtra("human_wins", currRound.getPlayers()[0].getWins());
                    intent.putExtra("computer_score", currRound.getPlayers()[1].getScore());
                    intent.putExtra("computer_wins", currRound.getPlayers()[1].getWins());
                    Log.ClearLog();
                    startActivity(intent);

                // If round hasn't been won, we get game information and move to the save activity
                } else {
                    Intent intent = new Intent(MatchActivity.this, SerialActivity.class);

                    intent.putExtra("human_score", currRound.getPlayers()[0].getScore());
                    intent.putExtra("human_wins", currRound.getPlayers()[0].getWins());
                    intent.putExtra("computer_score", currRound.getPlayers()[1].getScore());
                    intent.putExtra("computer_wins", currRound.getPlayers()[1].getWins());

                    intent.putExtra("next_player", currRound.swap().getIdentity());

                    if (currRound.swap().getColor() == 'B') {
                        intent.putExtra("next_color", "Black");
                    } else {
                        intent.putExtra("next_color", "White");
                    }

                    String boardVals = "";
                    for (Piece[] row : currentBoard[0].getBoard()) {
                        for (Piece space : row ) {
                            boardVals += space.getColor();
                        }
                    }

                    intent.putExtra("board_status", boardVals);

                    Log.ClearLog();
                    startActivity(intent);
                }

            }
        });

        // Continue listener being made
        Button continueButton = findViewById(R.id.continue_button);
        // Handles when player wants to start a new turn
        continueButton.setOnClickListener(view -> {
            // Only able to be clicked if moves were made
            if (currRound.getCurrPlayer().getMoves() == 2) {
                // If round has been won, continue initializes a new round
                if (currRound.isRoundWon(currentBoard[0])) {
                    Board board = new Board();
                    board.initial_state();

                    currRound.getPlayers()[0].setMoves(0);
                    currRound.getPlayers()[1].setMoves(0);

                    if (!currRound.starting_player()) {
                        coinPopup(this, coinCall);
                    }

                    callback.onBoardLoaded(board);
                // If round still is ongoing, continue swaps players, and starts new turn
                } else {

                    TextView helpText = findViewById(R.id.help_text);

                    String strategyText = "-";
                    helpText.setText(strategyText);

                    currRound.getCurrPlayer().setMoves(0);
                    currRound.setCurrPlayer(currRound.swap());

                    String currPlayerText = currRound.getCurrPlayer().getIdentity() + "'s turn - ";
                    if (currRound.getCurrPlayer().getColor() == 'B') {
                        currPlayerText += "Black";
                    } else {
                        currPlayerText += "White";
                    }
                    TextView currPlayer = findViewById(R.id.currplayer);
                    currPlayer.setText(currPlayerText);
                    logText.setText(Log.FormatLog());
                    // If it is computer's turn, we need to let it play itself out
                    if (currRound.getCurrPlayer().getIdentity().equals("Computer")) {
                        String strategy = currRound.getCurrPlayer().play(currentBoard[0], currRound.getCurrPlayer(), currRound.swap());
                        currRound.getCurrPlayer().setMoves(2);
                        strategyText = "Move piece at " + currRound.getCurrPlayer().getSelection() + " to space " +
                                currRound.getCurrPlayer().getDestination() + " (" + strategy + ")\n";
                        helpText.setText(strategyText);
                        BoardView boardView = new BoardView();
                        boardView.display_board(MatchActivity.this, currRound, currentBoard[0], tableLayout);

                        if (currRound.isRoundWon(currentBoard[0])) {
                            currRound.setWinStates(currentBoard[0]);

                            TextView humanScore = findViewById(R.id.humanscore);
                            humanScore.setText(String.valueOf(tournament.getPlayers()[0].getScore()));

                            TextView humanWins = findViewById(R.id.humanwins);
                            humanWins.setText(String.valueOf(tournament.getPlayers()[0].getWins()));

                            TextView computerScore = findViewById(R.id.computerscore);
                            computerScore.setText(String.valueOf(tournament.getPlayers()[1].getScore()));

                            TextView computerWins = findViewById(R.id.computerwins);
                            computerWins.setText(String.valueOf(tournament.getPlayers()[1].getWins()));

                            Log.AddMessage(currRound.getCurrPlayer().getIdentity() + " wins the round!");
                            Log.AddMessage("Press continue to start next round or press exit to end the tournament");
                        }
                        Log.AddMessage("Press continue to start next turn or press exit to save and take a break");
                    }
                }
                logText.setText(Log.FormatLog());

            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}