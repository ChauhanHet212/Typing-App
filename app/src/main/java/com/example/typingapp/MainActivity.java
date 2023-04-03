package com.example.typingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.typingapp.retrofit.Retro;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ImageView startBtn;
    Spinner spin;

    String[] difficulty = {"Easy", "Medium", "Hard"};
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startBtn = findViewById(R.id.startBtn);
        spin = findViewById(R.id.spin);

        adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, difficulty);
        spin.setAdapter(adapter);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = spin.getSelectedItem().toString();
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                if (str.equals("Easy")) {
                    intent.putExtra("key", 1);
                } else if (str.equals("Medium")) {
                    intent.putExtra("key", 2);
                } else {
                    intent.putExtra("key", 3);
                }
                startActivity(intent);
            }
        });
    }
}