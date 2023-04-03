package com.example.typingapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.typingapp.retrofit.Retro;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameActivity extends AppCompatActivity {

    EditText edt;
    TextView text, timeTxtv, pointsTxtv;
    ImageView backBtn;

    int i, time, t = 4;
    String[] allWords = new String[0];
    String ans;
    int count = 0;
    boolean stop = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Retro.getInstance().retroAPI.getAllWords().enqueue(new Callback<String[]>() {
            @Override
            public void onResponse(Call<String[]> call, Response<String[]> response) {
                allWords = response.body();
            }

            @Override
            public void onFailure(Call<String[]> call, Throwable t) {

            }
        });

        i = getIntent().getIntExtra("key", 0);

        if (i == 1){
            time = 120;
        } else if (i == 2){
            time = 60;
        } else if (i == 3){
            time = 30;
        }

        edt = findViewById(R.id.edt);
        text = findViewById(R.id.text);
        timeTxtv = findViewById(R.id.timeTxtv);
        pointsTxtv = findViewById(R.id.pointsTxtv);
        backBtn = findViewById(R.id.backBtn);

        timeTxtv.setText(String.valueOf(time));

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        edt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return true;
            }
        });

        Dialog dialog = new Dialog(GameActivity.this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.start_dialog);

        TextView dialog_textv = dialog.findViewById(R.id.dialog_txtv);
        dialog_textv.setText(String.valueOf(t));

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                t --;
                dialog_textv.setText(String.valueOf(t));
                handler.postDelayed(this::run, 1000);
            }
        };
        handler.post(runnable);
        dialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                text.setVisibility(View.VISIBLE);
                dialog.dismiss();
                ans = setRandomWord();

                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (time != 0) {
                            time --;
                            timeTxtv.setText(String.valueOf(time));
                            Animation animation = AnimationUtils.loadAnimation(GameActivity.this, R.anim.down_to_up_anim);
                            timeTxtv.startAnimation(animation);
                        } else {
                            if (!isFinishing() && stop) {
                                stop = false;
                                new AlertDialog.Builder(GameActivity.this)
                                        .setCancelable(false)
                                        .setTitle("Game Over")
                                        .setMessage("Your points = "+count+"\nDo you want to play again?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                                edt.setText("");
                                                recreate();
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                                finish();
                                            }
                                        })
                                        .show();
                            }
                        }
                        handler.postDelayed(this::run, 1000);
                    }
                };
                handler.post(runnable);

                edt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (charSequence.toString().equals(ans)){
                            count ++;
                            pointsTxtv.setText(String.valueOf(count));
                            edt.setText("");
                            ans = setRandomWord();

                            Animation animation = AnimationUtils.loadAnimation(GameActivity.this, R.anim.down_to_up_anim);
                            pointsTxtv.startAnimation(animation);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }
        }, 4000);
    }

    private String setRandomWord() {
        int ran = new Random().nextInt(allWords.length);
        text.setText(allWords[ran]);

        return allWords[ran];
    }
}