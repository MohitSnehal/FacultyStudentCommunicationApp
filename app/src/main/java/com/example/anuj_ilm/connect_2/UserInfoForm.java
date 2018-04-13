package com.example.anuj_ilm.connect_2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserInfoForm extends AppCompatActivity {


    private User newUser;
    private String name,roll,year,section,branch;
    private Spinner sectionSpinner,branchSpinner,yearSpinner;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_form);

        database = FirebaseDatabase.getInstance();

        //getting data from intent of sign up
        Intent fromSignupActivity = getIntent();
        name = fromSignupActivity.getStringExtra("name");
        roll = fromSignupActivity.getStringExtra("roll");

        sectionSpinner = (Spinner) findViewById(R.id.sectionSpinner);
        branchSpinner = (Spinner) findViewById(R.id.branchSpinner);
        yearSpinner = (Spinner) findViewById(R.id.yearSpinner);

        //populating the branch spinner
        ArrayAdapter<CharSequence> branchAdapter = ArrayAdapter.createFromResource(this,
                R.array.branch, android.R.layout.simple_spinner_item);
        branchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branchSpinner.setAdapter(branchAdapter);

        //populating the year spinner according to branch from branch spinner
        branchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                if(position == 1)
                {
                    branch = "IT";
                }
                else
                    branch = "CS";
                ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(UserInfoForm.this,
                        R.array.year, android.R.layout.simple_spinner_item);
                yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                yearSpinner.setAdapter(yearAdapter);
            }

            public void onNothingSelected(AdapterView<?> parentView)
            {
                //do nothing
            }
        });

        //populating the section spinner according to the selection from year spinner and branch spinner
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                ArrayAdapter<CharSequence> sectionAdapter;
                if(branch.equals("CS"))
                {
                    if (position == 1)
                    {
                        year = "2nd year";
                        sectionAdapter = ArrayAdapter.createFromResource(UserInfoForm.this,
                                R.array.CS_Year_2, android.R.layout.simple_spinner_item);
                    }
                    else if (position == 2)
                    {
                        year = "3rd year";
                        sectionAdapter = ArrayAdapter.createFromResource(UserInfoForm.this,
                                R.array.CS_Year_3, android.R.layout.simple_spinner_item);
                    }
                    else
                    {
                        year = "1st year";
                        sectionAdapter = ArrayAdapter.createFromResource(UserInfoForm.this,
                                R.array.CS_Year_1, android.R.layout.simple_spinner_item);
                    }
                }
                else
                {
                    if (position == 1)
                    {
                        year = "2nd year";
                        sectionAdapter = ArrayAdapter.createFromResource(UserInfoForm.this,
                                R.array.IT_Year_2, android.R.layout.simple_spinner_item);
                    }
                    else if (position == 2)
                    {
                        year = "3rd year";
                        sectionAdapter= ArrayAdapter.createFromResource(UserInfoForm.this,
                                R.array.IT_Year_3, android.R.layout.simple_spinner_item);
                    }
                    else
                    {
                        year = "1st year";
                        sectionAdapter = ArrayAdapter.createFromResource(UserInfoForm.this,
                                R.array.IT_Year_1, android.R.layout.simple_spinner_item);
                    }
                }
                sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Spinner sectionSpinner = (Spinner)findViewById(R.id.sectionSpinner) ;
                sectionSpinner.setAdapter(sectionAdapter);
            }

            public void onNothingSelected(AdapterView<?> parentView)
            {
            }
        });

        //storing the value for section according to the section selected in section spinner
        sectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                if(branch.equals("CS"))
                {
                    section = "CS" + Integer.toString(++position);
                }
                else
                {
                    section = "IT" + Integer.toString(++position);
                }
            }

            public void onNothingSelected(AdapterView<?> parentView)
            {
            }
        });

    }

    public void storeUserInfo(View view)
    {
        newUser = new User(name,year,section,branch,"na");

        //updating the Firebase Database at ref : users/students/<roll number>
        myRef = database.getReference("users/students");
        myRef.child(roll).setValue(newUser);


        //Intent back to SignUpActivity for sign in after email verification
        Intent goToSignUpActivity =  new Intent(UserInfoForm.this,SignupActivity.class);
        goToSignUpActivity.putExtra("flag","Yes"); //sending flag to determine @SignupActivity intent is from signUpActivity only.
        startActivity(goToSignUpActivity);
        finish();
    }

    public void resendEmailVerification(View view)
    {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(UserInfoForm.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("Email verification", "sendEmailVerification", task.getException());
                            Toast.makeText(UserInfoForm.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                            Toast.makeText(UserInfoForm.this,
                                    "Click on the 'Resend Email Verification' Button.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
