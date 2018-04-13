package com.example.anuj_ilm.connect_2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TeacherSignupSignInActivity extends AppCompatActivity {

    private EditText pswdfield ;
    private RelativeLayout tSignInLayout ;
    private RelativeLayout tSignUpLayout ;
    private Button tSignInButton ;
    private Button tSignUpButton ;
    private TextView tLayoutSwitchSignIn ;
    private TextView tLayoutSwitchSignUp ;
    private String facultyUsername, facultyName;
    private FirebaseAuth mAuth;
    private boolean successfullSignup;
    private String username ;
    private String name ;
    private String email ;

    int show=0 ;        // 0 states that password is hidden and 1 states password is visible
    int show1=0 ;       //for signin layout


    public void updateUI(FirebaseUser currentUser , boolean fromSignUpFunction)
    {
        FirebaseUser currUser = mAuth.getCurrentUser();
        if(currentUser != null)
        {
            currUser = mAuth.getCurrentUser();
            if(fromSignUpFunction)
            {
                if (!currUser.isEmailVerified()) {
                        Toast.makeText(this, "Login error.\n" +
                                "Please verify your email.", Toast.LENGTH_SHORT).show();
                        sendVerificationEmail();
                    }
            }
            else
            {
                currUser = mAuth.getCurrentUser();
                if (currUser.isEmailVerified()) {
                    Log.d("verified email","YES");
                    Intent toTeacherHomeActivity = new Intent(TeacherSignupSignInActivity.this,
                            TeacherHomeActivity.class);
                    toTeacherHomeActivity.putExtra("username", email);
                    toTeacherHomeActivity.putExtra("name", facultyName);
                    finish();
                    startActivity(toTeacherHomeActivity);

                }
                else{
                    Toast.makeText(this, "Login error.\n" +
                            "Please verify your email or entered email id is invalid.", Toast.LENGTH_SHORT).show();
                    sendVerificationEmail();
                }
            }
        }
    }


    public void showPasswordSignIn(View view)
    {

        pswdfield = (EditText)findViewById(R.id.teachersPasswordSignIn);

        if(show==0)
        {
            pswdfield.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            show=1;
        }
        else
        {
            pswdfield.setTransformationMethod(PasswordTransformationMethod.getInstance());
            show=0;
        }

    }

    public void showPasswordSignUp(View view)
    {

        pswdfield = (EditText)findViewById(R.id.teachersPasswordSignUp);

        if(show1==0)
        {
            pswdfield.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            show1=1;
        }
        else
        {
            pswdfield.setTransformationMethod(PasswordTransformationMethod.getInstance());
            show1=0;
        }

    }

    public void teachersSignInFunction(View view)
    {

        EditText teachersUsernameSignIn = (EditText)findViewById(R.id.teachersUsernameSignIn);
        EditText teachersPasswordSignIn = (EditText)findViewById(R.id.teachersPasswordSignIn);

        if( teachersUsernameSignIn.getText().toString().trim().equals("") ||
                teachersPasswordSignIn.getText().toString().trim().equals(""))
        {
            Toast.makeText(getApplicationContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
            if( teachersUsernameSignIn.getText().toString().trim().equals(""))
                teachersUsernameSignIn.setError("Username required");
            else
                teachersPasswordSignIn.setError("Password required");
        }
        else
        {
            email = teachersUsernameSignIn.getText().toString(); // + "@kiit.ac.in";
            String password = teachersPasswordSignIn.getText().toString();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("faculty sign in", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user,false);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("faculty sign in", "signInWithEmail:failure", task.getException());
                                Toast.makeText(TeacherSignupSignInActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null,false);
                            }

                            // ...
                        }
                    });
        }

    }

    public void teachersSignUpFunction(View view)
      {

        final EditText teachersNameSignUp = (EditText) findViewById(R.id.teachersNameSignUp);
        final EditText teachersUsernameSignUp = (EditText) findViewById(R.id.teachersUsernameSignUp);
        EditText teachersPasswordSignUp = (EditText) findViewById(R.id.teachersPasswordSignUp);

        //checking for any field left empty
        if (teachersNameSignUp.getText().toString().trim().equals("")
                || teachersUsernameSignUp.getText().toString().trim().equals("")
                || teachersPasswordSignUp.getText().toString().trim().equals(""))
        {
            Toast.makeText(getApplicationContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
            if (teachersNameSignUp.getText().toString().trim().equals(""))
                teachersNameSignUp.setError("Name required");
            else if(teachersUsernameSignUp.getText().toString().trim().equals(""))
                teachersUsernameSignUp.setError("Username required");
            else
                teachersPasswordSignUp.setError("Password required");
        }
        else //ALL Fields filled
        {
            username= teachersUsernameSignUp.getText().toString();
            name = teachersNameSignUp.getText().toString();
            String password = teachersPasswordSignUp.getText().toString();
            successfullSignup = false;
            boolean isCorrectUsername = true;

            //now checking if the username is faculty only not any student
            //for which just checking if the entered username contains any number/s or not
            /*
            char facultyUsername[] = username.toCharArray();
            boolean isCorrectUsername = true;
            for(int i = 0 ; i < facultyUsername.length ; i++)
            {
                if(facultyUsername[i] >= '0' && facultyUsername[i] <= '9')
                {
                    isCorrectUsername = false;
                    break;
                }
            }
            */
            if(isCorrectUsername)
            {

                String email = username;// + "@kiit.ac.in";  this comment is to be uncommented
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, call update UI function to intent into User Info Form
                                    Log.d("Sign up", "FACULTY createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    String email = teachersUsernameSignUp.getText().toString();
                                    String value[] = email.split("@");
                                    String username = value[0];

                                    if(username.contains("."))
                                    {
                                        Log.d("value " , username);
                                        char usernameChar[] = username.toCharArray();
                                        username = "";
                                        for(int i = 0 ; i < usernameChar.length ; i++)
                                        {
                                            if(usernameChar[i] == '.')
                                            {
                                                username = username + "_";
                                            }
                                            else
                                            {
                                                username = username + Character.toString(usernameChar[i]);
                                            }
                                        }
                                    }
                                    String name = teachersNameSignUp.getText().toString();
                                    Log.d("username(if modified)",username);
                                    DatabaseReference myRef = database.getReference("users/faculty/"
                                            + username);

                                    FacultyInstance newUser = new FacultyInstance(name,email);
                                    myRef.setValue(newUser);
                                    successfullSignup = true;
                                    updateUI(user, true);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("Sign up", "FACULTY createUserWithEmail:failure", task.getException());
                                    Toast.makeText(TeacherSignupSignInActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    successfullSignup = false;
                                    updateUI(null, false);
                                }

                                // ...
                            }
                        });


            }
            else
            {
                Toast.makeText(this, "Please enter a valid faculty username.", Toast.LENGTH_SHORT).show();
            }
        }
        if(successfullSignup)
        {
            new CountDownTimer(100, 1000) {
                boolean flag = true;

                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {

                    sendVerificationEmail();

                }
            }.start();
        }

    }


    public void sendVerificationEmail()
    {
        final FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            System.out.println("asdfg"); //check if working
            user.sendEmailVerification()
                    .addOnCompleteListener(this, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(TeacherSignupSignInActivity.this,
                                        "Verification email sent to " + user.getEmail(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("     ", "sendEmailVerification", task.getException());
                                Toast.makeText(TeacherSignupSignInActivity.this,
                                        "Failed to send verification email.",
                                        Toast.LENGTH_SHORT).show();
                                Toast.makeText(TeacherSignupSignInActivity.this,
                                        "Click on the 'Resend Email Verification' Button.",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Please click on the resend email verification button.", Toast.LENGTH_SHORT).show();
        }
    }

    public void tGoToSignInLayout(View view)
    {
        //Toast.makeText(this, "click in sign up", Toast.LENGTH_SHORT).show();
        tSignInLayout.setVisibility(View.VISIBLE);
        tSignUpLayout.setVisibility(View.INVISIBLE);
        tSignInButton.setVisibility(View.VISIBLE);
        tSignUpButton.setVisibility(View.INVISIBLE);
        tLayoutSwitchSignIn.setVisibility(View.VISIBLE);
        tLayoutSwitchSignUp.setVisibility(View.INVISIBLE);
    }

    public void tGoToSignUpLayout(View view)
    {
        //Toast.makeText(this, "click in sign in", Toast.LENGTH_SHORT).show();
        tSignInLayout.setVisibility(View.INVISIBLE);
        tSignUpLayout.setVisibility(View.VISIBLE);
        tSignInButton.setVisibility(View.INVISIBLE);
        tSignUpButton.setVisibility(View.VISIBLE);
        tLayoutSwitchSignIn.setVisibility(View.INVISIBLE);
        tLayoutSwitchSignUp.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_signup_sign_in);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "For any queries mail to: kiitapp.root@gmail.com", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        tSignInLayout = (RelativeLayout)findViewById(R.id.tSignInLayout);
        tSignUpLayout = (RelativeLayout)findViewById(R.id.tSignUpLayout);
        tSignInButton = (Button)findViewById(R.id.tSignInButton);
        tSignUpButton = (Button)findViewById(R.id.tSignUpButton);
        tLayoutSwitchSignIn = (TextView)findViewById(R.id.tLayoutSwitchSignIn);
        tLayoutSwitchSignUp = (TextView)findViewById(R.id.tLayoutSwitchSignUp);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        updateUI(user,false);

    }

}
