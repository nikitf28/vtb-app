package ru.dolbak.vtb_auto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class EndActivity extends AppCompatActivity {
    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

    }

    public void onStartActivity(View v){
        Intent intent = new Intent(EndActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
