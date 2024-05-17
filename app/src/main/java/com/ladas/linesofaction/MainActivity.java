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

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Gets button state from main menu view and adds listeners
        Button newTournamentButton = findViewById(R.id.new_game_button);
        Button loadTournamentButton = findViewById(R.id.load_button);

        // Passes along that this tournament will be new to match activity
        newTournamentButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MatchActivity.class);
            intent.putExtra("tournament_selection", "new");
            startActivity(intent);
        });

        // Passes along that this tournament will be loaded in to match activity
        loadTournamentButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MatchActivity.class);
            intent.putExtra("tournament_selection", "load");
            startActivity(intent);
        });
    }


}