package com.ladas.linesofaction;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

public class BoardView {
    /**
     * View/Controller of the board grid for the game's GUI. Handles piece locations and movement
     * @param context
     * @param round, a Round object which holds the game state during a tournament
     * @param board, a Board object which holds board related information during a round
     * @param tableLayout, a TableLayout object which holds a reference to the board layout in
     *        activity_match
     */
    public void display_board(Context context, Round round, Board board, TableLayout tableLayout) {
        // Clear existing views
        tableLayout.removeAllViews();

        // Iterate through each cell of the board
        for (int row = 0; row < 9; row++) {
            TableRow tableRow = new TableRow(context);
            tableRow.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT
            ));

            for (int col = 0; col < 9; col++) {
                // Create a button for each cell
                Button button = new Button(context);

                // Create an array of drawables to be layered, for tile images and pieces
                Drawable[] layers = new Drawable[2];
                ColorDrawable transparentLayer = new ColorDrawable(Color.TRANSPARENT);

                if ((col % 2 == 0 && row % 2 == 0) || (col % 2 != 0 && row % 2 != 0)) {
                    transparentLayer.setColor(ContextCompat.getColor(context, R.color.board));
                } else {
                    transparentLayer.setColor(ContextCompat.getColor(context, R.color.board_alt));
                }

                if (col == 0 || row == 0) {
                    transparentLayer.setColor(ContextCompat.getColor(context, R.color.white));
                    button.setClickable(false);

                    if (col == 0 && row > 0) {
                        button.setText(String.valueOf(8 - (row - 1)));
                    } else if (col > 0 && row == 0) {
                        button.setText(String.valueOf((char) ((col - 1) + 65)));
                    }

                    button.setTextSize(24);
                }

                layers[0] = transparentLayer;

                if (col != 0 && row != 0) {
                    if (board.getBoard()[row - 1][col - 1].getColor() == 'B') {
                        layers[1] = ContextCompat.getDrawable(context, R.drawable.blackpiece);
                    } else if (board.getBoard()[row - 1][col - 1].getColor() == 'W') {
                        layers[1] = ContextCompat.getDrawable(context, R.drawable.whitepiece);
                    } else {
                        layers[1] = ContextCompat.getDrawable(context, R.drawable.emptypiece);
                    }
                } else {
                    layers[1] = ContextCompat.getDrawable(context, R.drawable.emptypiece);
                }

                // Create a LayerDrawable with the array of drawables
                LayerDrawable layerDrawable = new LayerDrawable(layers);

                // Set the LayerDrawable as the background of the button
                button.setBackground(layerDrawable);

                String position = String.valueOf((char) ((col - 1) + 65)) + (8 - (row - 1));


                int finalCol = col;
                int finalRow = row;
                // Create an OnClickListener for all buttons on the board
                button.setOnClickListener(v -> {
                    // Handles button click
                    if (finalCol != 0 && finalRow != 0) {
                        // Handles what action a human player is making
                        switch (round.getCurrPlayer().getMoves()) {
                            // First selection means, a valid piece that is selected to be moved
                            case 0:
                                if (board.validate_piece(round.getCurrPlayer().getColor(), position)) {
                                    // Change the color of the transparent layer
                                    transparentLayer.setColor(ContextCompat.getColor(context, R.color.valid));
                                    // Redraw the button background
                                    button.setBackground(layerDrawable);
                                    round.getCurrPlayer().setMoves(1);
                                    round.getCurrPlayer().setSelection(position);
                                    Snackbar snackbar = Snackbar.make(((Activity) context).findViewById(android.R.id.content),
                                            round.getCurrPlayer().getIdentity() + " selected piece at " + position, Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                    Log.AddMessage(round.getCurrPlayer().getIdentity() + " selected piece at " + position);

                                } else {
                                    display_board(context, round,  board, tableLayout);
                                    Snackbar snackbar = Snackbar.make(((Activity) context).findViewById(android.R.id.content), "Cannot select piece at " + position, Snackbar.LENGTH_LONG);
                                    Log.AddMessage("Cannot select piece at " + position);

                                    snackbar.show();
                                    round.getCurrPlayer().setSelection("");
                                    round.getCurrPlayer().setDestination("");
                                }

                                break;
                            // Second selection moves the selected piece to a valid destination
                            case 1:
                                if (board.validate_moveset( round.getCurrPlayer().getSelection(), position)) {
                                    round.getCurrPlayer().setDestination(position);
                                    round.getCurrPlayer().play(board, round.getCurrPlayer(), null);
                                    boolean won = round.isRoundWon(board);

                                    round.getCurrPlayer().setMoves(2);

                                    display_board(context, round,  board, tableLayout);


                                    Snackbar snackbar = Snackbar.make(((Activity) context).findViewById(android.R.id.content),
                                            round.getCurrPlayer().getIdentity() + " moved piece at " + round.getCurrPlayer().getSelection() + " to destination " + position,
                                            Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                    Log.AddMessage(round.getCurrPlayer().getIdentity() + " moved piece at " + round.getCurrPlayer().getSelection() + " to destination " + position);

                                    // Checks to see if the move a human has made has lead to a win
                                    if (won) {
                                        // Update game state and update the GUI
                                        round.setWinStates(board);

                                        TextView humanScore = ((Activity) context).findViewById(R.id.humanscore);
                                        humanScore.setText(String.valueOf(round.getPlayers()[0].getScore()));

                                        TextView humanWins = ((Activity) context).findViewById(R.id.humanwins);
                                        humanWins.setText(String.valueOf(round.getPlayers()[0].getWins()));

                                        TextView computerScore = ((Activity) context).findViewById(R.id.computerscore);
                                        computerScore.setText(String.valueOf(round.getPlayers()[1].getScore()));

                                        TextView computerWins = ((Activity) context).findViewById(R.id.computerwins);
                                        computerWins.setText(String.valueOf(round.getPlayers()[1].getWins()));

                                        Log.AddMessage(round.getCurrPlayer().getIdentity() + " wins the round!");
                                        Log.AddMessage("Press continue to start next round or press exit to end the tournament");

                                    } else {
                                        Log.AddMessage("Press continue to start next turn or press exit to save and take a break");
                                    }

                                } else {
                                    display_board(context, round,  board, tableLayout);
                                    Snackbar snackbar = Snackbar.make(((Activity) context).findViewById(android.R.id.content),
                                            "Cannot move piece " + round.getCurrPlayer().getSelection() + " to destination " + position, Snackbar.LENGTH_LONG);
                                    snackbar.show();

                                    Log.AddMessage("Cannot move piece " + round.getCurrPlayer().getSelection() + "to destination " + position);
                                    round.getCurrPlayer().setSelection("");
                                    round.getCurrPlayer().setDestination("");
                                    round.getCurrPlayer().setMoves(0);

                                }
                                break;
                        }

                        TextView logText = ((Activity) context).findViewById(R.id.log_text);
                        logText.setText(Log.FormatLog());
                    }
                });
                // Set tag to identify position
                button.setTag(position);
                // Add button to TableRow
                tableRow.addView(button);
            }
            // Add TableRow to TableLayout
            tableLayout.addView(tableRow);
        }
    }




}
