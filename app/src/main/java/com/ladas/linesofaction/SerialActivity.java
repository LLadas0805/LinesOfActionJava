package com.ladas.linesofaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class SerialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_serial);

        Intent intent = getIntent();

        // Stores all player state information from the round model/controller
        int[] playerStates = new int[4];

        playerStates[0] = intent.getIntExtra("human_score", 0);
        playerStates[1] = intent.getIntExtra("human_wins", 0);

        playerStates[2] = intent.getIntExtra("computer_score", 0);
        playerStates[3]= intent.getIntExtra("computer_wins", 0);

        // Stores next player related state information from the round model/controller
        String[] nextPlayerState = new String[2];

        nextPlayerState[0] = intent.getStringExtra("next_player");
        nextPlayerState[1] = intent.getStringExtra("next_color");

        // Stores board state information from the round model/controller as a string
        String board = intent.getStringExtra("board_status");

        // Stores what user put in the text form to pass in for a save file name
        TextInputEditText saveText = findViewById(R.id.file_text_input);

        // Gets save button from serial view and add listener
        // Passes information from this controller into the serialization model to make a file
        Button saveGameButton = findViewById(R.id.save_game_button);
        saveGameButton.setOnClickListener( v -> {
            Serialization serial = new Serialization();

            // If a valid save went through, we return to main menu, if not we clear input
            boolean saved = serial.saveGame(this, String.valueOf(saveText.getText()), playerStates, nextPlayerState, board);

            if (saved) {
                Intent exit = new Intent(SerialActivity.this, MainActivity.class);
                startActivity(exit);
            } else {
                saveText.setText("");
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}