package com.example.anuj_ilm.connect_2;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LaunchActivity extends AppCompatActivity {


    private FirebaseAuth mAuth ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar) ;


        new CountDownTimer(1800 , 1000){
            boolean flag = true;
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                Intent toUSerTypeSelection = new Intent(getApplicationContext() , UserTypeSelectionActivity.class) ;
                startActivity(toUSerTypeSelection);
                finish();

            }
        }.start();
    }
}

    //Intent toSignUpLayout = new Intent(getApplicationContext() , SignupActivity.class) ;
    //startActivity(toSignUpLayout);
    //finish();
