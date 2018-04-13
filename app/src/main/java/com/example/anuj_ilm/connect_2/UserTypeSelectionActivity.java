package com.example.anuj_ilm.connect_2;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.*;

public class UserTypeSelectionActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private final String TAG = "UserTypeSelectionAct";

    private RelativeLayout rootLayout ;
    private Button studentButton ;
    private Button facultyButton ;
    private TextView proceedText ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type_selection);
        mAuth = FirebaseAuth.getInstance();

        studentButton = (Button)findViewById(R.id.studentButton) ;
        facultyButton = (Button)findViewById(R.id.facultyButton) ;
        rootLayout = (RelativeLayout)findViewById(R.id.rootLayout) ;
        proceedText = (TextView)findViewById(R.id.proceedText) ;

        studentButton.setTranslationX(1000f) ;
        facultyButton.setTranslationX(-1000f) ;
        rootLayout.setTranslationY(-1000f) ;
        proceedText.setAlpha(0f);



    }

    public void updateUI(FirebaseUser currentUser)
    {

        Log.d(TAG ,"uoloadUI()");
        if(currentUser != null)
        {
            FirebaseUser currUser  = mAuth.getCurrentUser();
            String email = currUser.getEmail().toString();
            String emailParts[] = email.split("@");
            String username = emailParts[0];
            Log.d(" check 2 username",emailParts[0]);
            if(username.charAt(0) >= '0' &&  username.charAt(0) <= '9') //already logged in student
            {
                Intent toSignupActivity = new Intent(UserTypeSelectionActivity.this, SignupActivity.class);
                startActivity(toSignupActivity);
                finish();

            }
            else
            {
                Intent toTeacherSignupSignin = new Intent(UserTypeSelectionActivity.this, TeacherSignupSignInActivity.class);
                startActivity(toTeacherSignupSignin);
                finish();

            }
        }
    }

    public void toFacultySigninSignupActivity(View view)
    {
        studentButton.animate().translationXBy(1000f).setDuration(1000) ;
        facultyButton.animate().translationXBy(-1000f).setDuration(1000) ;
        rootLayout.animate().translationYBy(-1000f).setDuration(1000) ;
        proceedText.animate().alpha(0f).setDuration(1000) ;

        new CountDownTimer(1000 , 1000)
        {

            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                Intent toTeacherSignupSignin = new Intent(UserTypeSelectionActivity.this, TeacherSignupSignInActivity.class);
                startActivity(toTeacherSignupSignin);
                //finish();

            }
        }.start() ;
    }

    public void toSignupActivity(View view)
    {
        studentButton.animate().translationXBy(1000f).setDuration(1000) ;
        facultyButton.animate().translationXBy(-1000f).setDuration(1000) ;
        rootLayout.animate().translationYBy(-1000f).setDuration(1000) ;
        proceedText.animate().alpha(0f).setDuration(1000) ;

        new CountDownTimer(1000 , 1000)
        {

            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                Intent toSignupActivity = new Intent(UserTypeSelectionActivity.this, SignupActivity.class);
                startActivity(toSignupActivity);
                //finish();

            }
        }.start() ;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

        studentButton.animate().translationXBy(-1000f).setDuration(1000) ;
        facultyButton.animate().translationXBy(1000f).setDuration(1000) ;
        rootLayout.animate().translationYBy(1000f).setDuration(1000) ;
        proceedText.animate().alpha(1f).setDuration(1000) ;

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        studentButton.animate().translationXBy(-1000f).setDuration(1000) ;
        facultyButton.animate().translationXBy(1000f).setDuration(1000) ;
        rootLayout.animate().translationYBy(1000f).setDuration(1000) ;
        proceedText.animate().alpha(1f).setDuration(1000) ;

    }
}
