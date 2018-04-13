package com.example.anuj_ilm.connect_2;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
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

public class SignupActivity extends AppCompatActivity {

    EditText pswdfield ;
    EditText pswdfield1 ;
    Button but ;
    Button but1 ;

    RelativeLayout signUpLayout;
    RelativeLayout signInLayout;
    Button signupButton ;
    Button signinButton ;
    TextView layoutSwitchSignUp ;
    TextView layoutSwitchSignIn ;

    private FirebaseAuth mAuth;


    private String roll,name,section,branch,year;

    int show=0 ;        // 0 states that password is hidden and 1 states password is visible
    int show1=0 ;       //for signin layout

    public void showHelpLayout(View view)
    {
        Snackbar.make(view, "For any queries mail to: kiitapp.root@gmail.com", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public void showPasswordSignUp(View view)
    {

        // pswdfield = (EditText)findViewById(R.id.password);
        pswdfield = (EditText)findViewById(R.id.studentsPasswordSignUp);

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

    public void showPasswordSignIn(View view)
    {

        // pswdfield = (EditText)findViewById(R.id.password);
        pswdfield1 = (EditText)findViewById(R.id.studentsPasswordSignIn);

        if(show1==0)
        {
            pswdfield1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            show1=1;
        }
        else
        {
            pswdfield1.setTransformationMethod(PasswordTransformationMethod.getInstance());
            show1=0;
        }

    }

    public void goToSignInLayout(View view){

        signUpLayout.setVisibility(View.INVISIBLE);
        signInLayout.setVisibility(View.VISIBLE);
        signinButton.setVisibility(View.VISIBLE);
        signupButton.setVisibility(View.INVISIBLE);
        layoutSwitchSignIn.setVisibility(View.VISIBLE);
        layoutSwitchSignUp.setVisibility(View.INVISIBLE);

    }

    public void goToSignUpLayout(View view){

        signInLayout.setVisibility(View.INVISIBLE);
        signUpLayout.setVisibility(View.VISIBLE);
        signinButton.setVisibility(View.INVISIBLE);
        signupButton.setVisibility(View.VISIBLE);
        layoutSwitchSignIn.setVisibility(View.VISIBLE);
        layoutSwitchSignUp.setVisibility(View.INVISIBLE);
        layoutSwitchSignIn.setVisibility(View.INVISIBLE);
        layoutSwitchSignUp.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //but = (Button)findViewById(R.id.signupbutton1);

        //UI set up
        //Typeface custom_font = Typeface.createFromAsset(getAssets(),"fonts/qontra.otf");

        //but.setTypeface(custom_font);

        signUpLayout = (RelativeLayout)findViewById(R.id.signUpLayout) ;
        signInLayout = (RelativeLayout)findViewById(R.id.signInLayout) ;
        signinButton = (Button)findViewById(R.id.signInButton) ;
        signupButton = (Button)findViewById(R.id.signUpButton) ;
        layoutSwitchSignUp  = (TextView)findViewById(R.id.layoutSwitchSignUp) ;
        layoutSwitchSignIn = (TextView)findViewById(R.id.layoutSwitchSignIn) ;

        //sign in and sign up
        mAuth = FirebaseAuth.getInstance();



    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        Intent fromUserInfoForm = getIntent();
        String flag = fromUserInfoForm.getStringExtra("flag");
        if(flag != null)
        {
            if(flag.equals("Yes"))
                updateUI(currentUser,false,true);
        }
        else
            updateUI(currentUser,false,false);


    }

    public void updateUI(FirebaseUser currentUser , boolean fromSignUpFunction , boolean fromUserInfoForm)
    {
        if(currentUser != null)
        {
           FirebaseUser currUser = mAuth.getCurrentUser();
           if(fromSignUpFunction)
           {
                Intent toUserInfoForm = new Intent(SignupActivity.this,UserInfoForm.class);
                toUserInfoForm.putExtra("roll" , roll);
                toUserInfoForm.putExtra("name", name);
                finish();
                startActivity(toUserInfoForm);
           }
           else if(!fromUserInfoForm)
           {
               if(currUser.isEmailVerified()) {
                   Intent toUserDetails = new Intent(SignupActivity.this, UserDetails.class);
                   startActivity(toUserDetails);
                   finish();
               }
               else
                   Toast.makeText(this, "Login error.\nPlease verify your email or your roll number is invalid.", Toast.LENGTH_SHORT).show();
           }

       }
    }



    public void studentsSignInFunction(View view)
    {

        EditText rollNumberField = (EditText)findViewById(R.id.studentsUsernameSignIn);
        EditText passwordField = (EditText)findViewById(R.id.studentsPasswordSignIn);

        if( rollNumberField.getText().toString().trim().equals("") ||
                passwordField.getText().toString().trim().equals(""))
        {
            Toast.makeText(getApplicationContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
            if( rollNumberField.getText().toString().trim().equals(""))
                rollNumberField.setError("Username required");
            else
                passwordField.setError("Password required");
        }
        else
        {
            roll = rollNumberField.getText().toString();
                String email = roll + "@kiit.ac.in";
                String password = passwordField.getText().toString();
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("Sign in", "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    updateUI(user, false, false);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("Sign in", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(SignupActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null, false, false);
                                }

                                // ...
                            }
                        });

        }
    }

    public void studentsSignUpFunction(View view) {
        final EditText nameField = (EditText) findViewById(R.id.studentsNameSignUp);
        EditText rollNumberField = (EditText) findViewById(R.id.studentsUsernameSignUp);
        EditText passwordField = (EditText) findViewById(R.id.studentsPasswordSignUp);

        if (nameField.getText().toString().trim().equals("")
                || rollNumberField.getText().toString().trim().equals("")
                || passwordField.getText().toString().trim().equals(""))
        {
            Toast.makeText(getApplicationContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
            if (nameField.getText().toString().trim().equals(""))
                nameField.setError("Name required");
            else if(rollNumberField.getText().toString().trim().equals(""))
                rollNumberField.setError("Username required");
            else
                passwordField.setError("Password required");
        }
        else
        {
            final String rollNumber = rollNumberField.getText().toString();
            String password = passwordField.getText().toString();
            String email = rollNumber + "@kiit.ac.in";
            boolean rollNumberIsValid = true;
            if (rollNumber.length() != 7 || !rollNumber.startsWith("1")) {
                //considering for years upto 2020 where 2020 not included
                rollNumberIsValid = false;

            }

            if (rollNumberIsValid) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, call update UI function to intent into User Info Form
                                    Log.d("Sign up", "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    name = nameField.getText().toString();
                                    roll = rollNumber;
                                    updateUI(user, true, false);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("Sign up", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignupActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null, false, false);
                                }

                                // ...
                            }
                        });


                //since signed up email verification is done below
                new CountDownTimer(1000 , 1000){
                    boolean flag = true;
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        sendVerificationEmail() ;
                    }
                }.start();

            } else {
                Toast.makeText(this, "Roll number not valid.", Toast.LENGTH_SHORT).show();
            }

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
                                Toast.makeText(SignupActivity.this,
                                        "Verification email sent to " + user.getEmail(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("     ", "sendEmailVerification", task.getException());
                                Toast.makeText(SignupActivity.this,
                                        "Failed to send verification email.",
                                        Toast.LENGTH_SHORT).show();
                                Toast.makeText(SignupActivity.this,
                                        "Click on the 'Resend Email Verification' Button.",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Please click on the resend email verification button.", Toast.LENGTH_SHORT).show();
        }
    }

}
