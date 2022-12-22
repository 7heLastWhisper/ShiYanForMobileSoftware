package com.jnu.booklistapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kongqw.rockerlibrary.view.RockerView;

public class GameActivity extends AppCompatActivity {

    public static RockerView rockerView;
    public static Button btnShoot, btnRestart, btnExit;
    public static View layoutGameOver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initView();
    }

    private void initView() {

        rockerView = findViewById(R.id.rockerView);
        rockerView.setCallBackMode(RockerView.CallBackMode.CALL_BACK_MODE_STATE_CHANGE);
        btnShoot = findViewById(R.id.btn_shoot);
        layoutGameOver = findViewById(R.id.layout_game_over);
        btnRestart = findViewById(R.id.btn_restart);
        btnExit = findViewById(R.id.btn_exit);
    }
}