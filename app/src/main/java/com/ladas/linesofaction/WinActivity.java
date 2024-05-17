package com.ladas.linesofaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class WinActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_win);

        // Stores player state information from the round model/controller
        Intent intent = getIntent();
        int humanScore = intent.getIntExtra("human_score", 0);
        int humanWins = intent.getIntExtra("human_wins", 0);

        int computerScore = intent.getIntExtra("computer_score", 0);
        int computerWins = intent.getIntExtra("computer_wins", 0);

        TextView winState = findViewById(R.id.win_state);

        // Displays winner of the tournament, if no winner then must be a tie
        String result;
        if (humanWins > computerWins) {
            result = "Human wins the tournament!";
        } else if (computerWins > humanWins) {
            result = "Computer wins the tournament!";
        } else {
            result = "The tournament was a tie!";
        }

        winState.setText(result);

        // Binds text in the GUI to the player state variables
        TextView humanScoreText = findViewById(R.id.humanscore);
        humanScoreText.setText(String.valueOf(humanScore));

        TextView humanWinstext = findViewById(R.id.humanwins);
        humanWinstext.setText(String.valueOf(humanWins));

        TextView computerScoreText = findViewById(R.id.computerscore);
        computerScoreText.setText(String.valueOf(computerScore));

        TextView computerWinstext = findViewById(R.id.computerwins);
        computerWinstext.setText(String.valueOf(computerWins));

        // Creates an event listener for button in View to return to menu after tournament ends
        Button menuButton = findViewById(R.id.menu_button);
        menuButton.setOnClickListener(v -> {
            Intent main_menu = new Intent(WinActivity.this, MainActivity.class);
            startActivity(main_menu);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}