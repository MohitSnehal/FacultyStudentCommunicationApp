package com.example.anuj_ilm.connect_2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TeacherHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Spinner schoolSelectorSpinner;
    private String selectedSchool;
    private ListView listView ;
    private FirebaseAuth mAuth ;
    private FirebaseDatabase database ;
    public static String username ;
    private String tEmail, tName ;
    private Spinner branchSpinner ;
    private Spinner yearSpinner ;
    private int noOfSections = 0 ;
    private final int
            numberOfSections_CS_1st_year=9,
            numberOfSections_CS_2nd_year=8,
            numberOfSections_CS_3rd_year=7,
            numberOfSections_IT_1st_year=9,
            numberOfSections_IT_2nd_year=8,
            numberOfSections_IT_3rd_year=7;
    private String branch = "CS" , year = "1st year" ;
    private EditText messageValue ;
    private EditText attachmentsEditText ;
    private EditText subjectValue ;
    private ListView outboxListview ;
    private boolean facultyDetailsLoaded = false ;
    private ProgressDialog insertTeachersDetailProgressDialog ;


    public void setTitle(String title)
    {
        getSupportActionBar().setTitle(title);
    }

    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void loadOutbox()
    {
        outboxListview = (ListView)findViewById(R.id.outboxListView) ;
        if(outboxListview == null && OutboxFragment.outboxListview != null)
        {
            outboxListview =  OutboxFragment.outboxListview;
        }

        database = FirebaseDatabase.getInstance() ;
        DatabaseReference outboxMessageRef = database.getReference("facultyOutbox/" + username) ;
        Log.d("tag user" , username) ;
        outboxMessageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ArrayList<FacultyOutboxMessage>  array = new ArrayList<>();
                for(DataSnapshot mySnapshot: dataSnapshot.getChildren()) {
                    //User tmpUser;
                    FacultyOutboxMessage value = mySnapshot.getValue(FacultyOutboxMessage.class);
                    array.add(value) ;

                }
                if (array.size() != 0)
                {
                    FacultyOutboxMessage arr[] = new FacultyOutboxMessage[array.size()] ;
                    int i = array.size() - 1  ;
                    for (FacultyOutboxMessage tmp : array)
                    {
                        arr[i] = tmp ;
                        i-- ;
                    }
                    ListAdapter listAdapter = new CustomAdapterOutboxMessages(TeacherHomeActivity.this, arr);
                    outboxListview.setAdapter(listAdapter);
                }
            }


            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("recieve", "Failed to read value.", error.toException());
            }
        });


        outboxListview.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                                .beginTransaction() ;
                        Bundle args = new Bundle();
                        ListAdapter listAdapter = outboxListview.getAdapter();
                        FacultyOutboxMessage selectedMessaqe = (FacultyOutboxMessage)listAdapter.getItem(position);

                        args.putString("recepients",selectedMessaqe.getRecepients());
                        args.putString("body",selectedMessaqe.getBody());
                        args.putString("attachmentUrls" , selectedMessaqe.getAttachmentUrls());
                        args.putString("subject" , selectedMessaqe.getSubject());
                        args.putString("timestamp" , selectedMessaqe.getTimestamp());
                        OutboxMessageFragment obj = new OutboxMessageFragment();
                        Log.d("rece" , args.getString("recepients")) ;
                        obj.setArguments(args);
                        fragmentTransaction.replace(R.id.tflMain , obj) ;
                        fragmentTransaction.addToBackStack("bcd");
                        fragmentTransaction.commit() ;
                    }
                }
        );
    }

    public void sendMessage(View view)
    {
        database = FirebaseDatabase.getInstance() ;
        messageValue = (EditText)findViewById(R.id.messageValue) ;
        attachmentsEditText = (EditText)findViewById(R.id.attachmentsEditText) ;
        subjectValue = (EditText)findViewById(R.id.subjectValue) ;
        boolean successfulMessage = false ;
        boolean proceedToSend = true ;

        if( subjectValue.getText().toString().trim().equals(""))
        {
            proceedToSend = false ;
            subjectValue.setError("Subject compulsory");
        }
        else
        {
            if(attachmentsEditText.getText().toString().trim().equals("") && messageValue.getText().toString().trim().equals(""))
            {
                proceedToSend = false ;
                attachmentsEditText.setError("Subject compulsory");
                messageValue.setError("Subject compulsory");
                Toast.makeText(this, "Atleast one of marked fields is compulsory", Toast.LENGTH_SHORT).show();
            }
        }
        if(proceedToSend)
        {
            String recepients = year + " : ";
            String subject = subjectValue.getText().toString();
            String messageBody = messageValue.getText().toString();
            String rawAttachments[] = attachmentsEditText.getText().toString().split(",");
            String attachments = "";
            if (rawAttachments.length != 0) {
                attachments = rawAttachments[0].trim();
                for (int i = 1; i < rawAttachments.length; i++) {
                    String current = rawAttachments[i].trim() ;
                    if (!current.equals(""))
                        attachments = attachments + "," + current;
                }
            } else {
                attachments = attachmentsEditText.getText().toString().trim();
            }


            Date currentTime = Calendar.getInstance().getTime();
            String timestampTmp[] = currentTime.toString().split(" ");
            FacultyMessage fmessage = new FacultyMessage(tName, messageBody, attachments, subject, currentTime.toString());
            //process the date and time
            String month = timestampTmp[1];
            if (month.equals("Jan"))
                month = "01";
            else if (month.equals("Feb"))
                month = "02";
            else if (month.equals("Mar"))
                month = "03";
            else if (month.equals("Apr"))
                month = "04";
            else if (month.equals("May"))
                month = "05";
            else if (month.equals("Jun"))
                month = "06";
            else if (month.equals("Jul"))
                month = "07";
            else if (month.equals("Aug"))
                month = "08";
            else if (month.equals("Sep"))
                month = "09";
            else if (month.equals("Oct"))
                month = "10";
            else if (month.equals("Nov"))
                month = "11";
            else
                month = "12";

            String timestamp = timestampTmp[5] + month + timestampTmp[2] + timestampTmp[3];

            for (int i = 1; i <= 10; i++) {
                String checkboxName = "section" + Integer.toString(i);
                int resID = getResources().getIdentifier(checkboxName, "id", "com.example.anuj_ilm.connect_2");
                CheckBox currentCheckbox = (CheckBox) findViewById(resID);
                if (currentCheckbox.isChecked()) {
                    String section = branch + Integer.toString(i);
                    if (recepients.equals(year + " : "))
                        recepients = recepients + section;
                    else
                        recepients = recepients + " , " + section;


                    DatabaseReference messageRef = database.getReference("messages/" + branch + "/" + year + "/" +
                            section + "/" + timestamp);
                    messageRef.setValue(fmessage);
                    successfulMessage = true;
                }
            }
            if (successfulMessage) {
                FacultyOutboxMessage fOMessage = new FacultyOutboxMessage(recepients, messageBody, attachments,
                        subject, currentTime.toString());

                DatabaseReference messageRef = database.getReference("facultyOutbox/" + username + "/" + timestamp);
                messageRef.setValue(fOMessage);
                Toast.makeText(this, "Message successfully sent. :)", Toast.LENGTH_SHORT).show();

                messageValue.setText("");
                attachmentsEditText.setText("");
                subjectValue.setText("");
                for (int i = 1; i <= noOfSections; i++) {
                    String checkboxName = "section" + Integer.toString(i);
                    int resID = getResources().getIdentifier(checkboxName, "id", "com.example.anuj_ilm.connect_2");
                    CheckBox current = (CheckBox) findViewById(resID);
                    current.setText(branch + Integer.toString(i));
                    current.setChecked(false);
                    current.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void recepientsSelector()
    {
        branchSpinner = (Spinner)findViewById(R.id.branchSpinner);
        yearSpinner = (Spinner)findViewById(R.id.yearSpinner);


        ArrayAdapter<CharSequence> branchAdapter = ArrayAdapter.createFromResource(TeacherHomeActivity.this,
                R.array.branch, android.R.layout.simple_spinner_item);
        branchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branchSpinner.setAdapter(branchAdapter);

        //populating the year spinner according to branch from branch spinner
        branchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                Log.d("branchspinner" , "true") ;
                if(position == 1)
                {
                    Log.d("itselected" , "true") ;
                    branch = "IT";
                }
                else
                    branch = "CS";
                ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(TeacherHomeActivity.this,
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
                Log.d("yearspinner" , "true") ;
                ArrayAdapter<CharSequence> sectionAdapter;
                if(branch.equals("CS"))
                {
                    if (position == 1)
                    {
                        year = "2nd year";
                        noOfSections = numberOfSections_CS_2nd_year ;
                    }
                    else if (position == 2)
                    {
                        year = "3rd year";
                        noOfSections = numberOfSections_CS_3rd_year ;
                    }
                    else
                    {
                        year = "1st year";
                        noOfSections = numberOfSections_CS_1st_year ;
                    }
                }
                else
                {
                    if (position == 1)
                    {
                        Log.d("itsecondyr" , "true") ;
                        year = "2nd year";
                        noOfSections = numberOfSections_IT_2nd_year ;
                    }
                    else if (position == 2)
                    {
                        year = "3rd year";
                        noOfSections = numberOfSections_IT_3rd_year ;
                    }
                    else
                    {
                        year = "1st year";
                        noOfSections = numberOfSections_IT_1st_year ;

                    }
                }

                //setting all the respective sections visible and unchecked

                for (int i = 1 ; i<=noOfSections ; i++)
                {
                    String checkboxName = "section"+Integer.toString(i) ;
                    int resID = getResources().getIdentifier( checkboxName , "id", "com.example.anuj_ilm.connect_2");
                    CheckBox current = (CheckBox)findViewById(resID) ;
                    current.setText(branch+Integer.toString(i));
                    current.setChecked(false);
                    current.setVisibility(View.VISIBLE);
                }
                //making the rest of the checkboxes invisible
                for (int i = noOfSections+1 ; i<=10 ; i++)//10 is the total number of checkboxes we have assumed
                {
                    Log.d("forinvisible" , "true") ;
                    String checkboxName = "section"+Integer.toString(i) ;
                    int resID = getResources().getIdentifier( checkboxName , "id", "com.example.anuj_ilm.connect_2");
                    CheckBox current = (CheckBox)findViewById(resID) ;
                    current.setChecked(false);
                    current.setVisibility(View.INVISIBLE);
                }

            }
            public void onNothingSelected(AdapterView<?> parentView)
            {
            }
        });


    }

    public void insertTeachersDetail()
    {
        FirebaseUser user = mAuth.getCurrentUser();

        final TextView tNavHeaderName = (TextView)findViewById(R.id.tNavHeaderName) ;
        final TextView tNavHeaderEmail = (TextView)findViewById(R.id.tNavHeaderEmail) ;
        final TextView emailFromValue = (TextView)findViewById(R.id.messageFromValue) ;

        String emailParts[] = user.getEmail().split("@");
        Log.d("email" , emailParts[0]);

        username = emailParts[0];

        if(username.contains("."))
        {
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
        Log.d("modified" , username);

        database = FirebaseDatabase.getInstance();



        DatabaseReference ref = database.getReference("users/faculty/"+username);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for(DataSnapshot mySnapshot: dataSnapshot.getChildren()) {
                    //User tmpUser;
                    if(i==0) {
                        tNavHeaderEmail.setText(mySnapshot.getValue().toString());
                        tEmail = tNavHeaderEmail.getText().toString();
                        Log.d("tEmail", tEmail);
                    }
                    if(i==1)
                    {
                        //populating values into the Navigation Drawer's username and email
                        tNavHeaderName.setText(mySnapshot.getValue().toString());
                        emailFromValue.setText(mySnapshot.getValue().toString());
                        tName = tNavHeaderName.getText().toString();
                        Log.d("tName",tName);
                        insertTeachersDetailProgressDialog.dismiss() ;
                    }
                    i++;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Database Error", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });

    }

    public void submitSchool(View view) {

        if(selectedSchool.equals("School of Computer Engineering")) {
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.tflMain, new SceCardFragment());
            fragmentTransaction.addToBackStack("acd");
            fragmentTransaction.commit();

            new CountDownTimer(100 , 1000){
                boolean flag = true;
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {

                    listView = (ListView)findViewById(R.id.sceListView) ;
                    if(listView == null && SceCardFragment.listView != null)
                    {
                        listView =  SceCardFragment.listView;
                    }

                    FacultyInfo[] array = new FacultyInfo[90];

                    for(int i = 0 ; i < 90 ; i++)
                    {
                        String stringResourceName = "faculty_sce_" + Integer.toString(i+1);
                        int resID = getResources().getIdentifier(stringResourceName , "array", "com.example.anuj_ilm.connect_2");
                        ArrayAdapter<CharSequence> facultyAdapter = ArrayAdapter.createFromResource(TeacherHomeActivity.this,
                                resID , android.R.layout.simple_spinner_item);
                        String name = facultyAdapter.getItem(0).toString();
                        String designation = facultyAdapter.getItem(1).toString();
                        String imageName = facultyAdapter.getItem(5).toString();
                        array[i] = new FacultyInfo(name,designation,imageName);
                        Log.d("imageName : " , imageName);
                    }

                    ListAdapter listAdapter = new CustomAdapterSceCard(TeacherHomeActivity.this,array);
                    listView.setAdapter(listAdapter);


                    listView.setOnItemClickListener(
                            new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction() ;
                                    Bundle args = new Bundle();
                                    args.putString("array name", "faculty_sce_" + Integer.toString(position+1));
                                    FacultyDetailFragment obj = new FacultyDetailFragment();
                                    obj.setArguments(args);
                                    fragmentTransaction.replace(R.id.tflMain , obj) ;
                                    fragmentTransaction.addToBackStack("acd");
                                    fragmentTransaction.commit() ;
                                }
                            }
                    );
                }
            }.start();

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "For any queries mail to: kiitapp.root@gmail.com", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction() ;
        fragmentTransaction.replace(R.id.tflMain , new ComposeFragment()) ;
        fragmentTransaction.commit() ;

        navigationView.setCheckedItem(R.id.composeItem);


        if(isNetworkAvailable())
        {
            insertTeachersDetailProgressDialog = new ProgressDialog(this);
            insertTeachersDetailProgressDialog.setMessage("Loading data");
            insertTeachersDetailProgressDialog.show();

            new CountDownTimer(100 , 1000){
                boolean flag = true;
                @Override
                public void onTick(long millisUntilFinished) {
                }
                @Override
                public void onFinish() {
                    insertTeachersDetail();
                    recepientsSelector();
                    facultyDetailsLoaded = true ;
                }
            }.start();

        }
        else {
            insertTeachersDetailProgressDialog = new ProgressDialog(this);
            insertTeachersDetailProgressDialog.setMessage("Loading data");
            insertTeachersDetailProgressDialog.show();
            Toast.makeText(this, "No internet connectivity.", Toast.LENGTH_SHORT).show();
            new CountDownTimer(60000 , 1000){
                boolean flag = true;
                @Override
                public void onTick(long millisUntilFinished) {
                    if(isNetworkAvailable())
                    {
                        insertTeachersDetail();
                        recepientsSelector();
                        facultyDetailsLoaded = true;
                    }
                }
                @Override
                public void onFinish() {
                    Toast.makeText(TeacherHomeActivity.this, "Unable to load data.", Toast.LENGTH_SHORT).show();
                    insertTeachersDetailProgressDialog.dismiss();

                }
            }.start();
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.teacher_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.composeItem)
        {
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.tflMain, new ComposeFragment());
            fragmentTransaction.commit();
            if(isNetworkAvailable())
            {

                new CountDownTimer(100, 1000) {
                    boolean flag = true;

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {

                        insertTeachersDetail();
                        recepientsSelector();
                        facultyDetailsLoaded = true ;
                    }
                }.start();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "No internet connectivity.", Toast.LENGTH_SHORT).show();
            }

        }
        else if (id == R.id.outboxItem)
        {
            if(facultyDetailsLoaded && isNetworkAvailable())
            {
                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.tflMain, new OutboxFragment());
                fragmentTransaction.commit();

                new CountDownTimer(100, 1000) {
                    boolean flag = true;

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {

                        loadOutbox();
                    }
                }.start();
            }
            else
            {
                if(!isNetworkAvailable())
                    Toast.makeText(this, "No internet connectivity.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Please wait...", Toast.LENGTH_SHORT).show();

                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                navigationView.setCheckedItem(R.id.composeItem);
            }

        }
        else if (id == R.id.facultyDetailsItem)
        {
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction() ;
            fragmentTransaction.replace(R.id.tflMain , new FacultyFragment()) ;
            fragmentTransaction.commit() ;

            new CountDownTimer(100 , 1000){
                boolean flag = true;
                @Override
                public void onTick(long millisUntilFinished) {
                }
                @Override
                public void onFinish()
                {

                    schoolSelectorSpinner = (Spinner)findViewById(R.id.schoolSpinner);
                    selectedSchool = "School of Computer Engineering";

                    ArrayAdapter<CharSequence> schoolsAdapter = ArrayAdapter.createFromResource(TeacherHomeActivity.this,
                            R.array.schools, android.R.layout.simple_spinner_item);
                    schoolsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    schoolSelectorSpinner.setAdapter(schoolsAdapter);

                    schoolSelectorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {

                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
                        {
                            if(position == 0)
                            {
                                selectedSchool = "School of Computer Engineering";
                            }
                            else
                                selectedSchool = "NA";
                        }

                        public void onNothingSelected(AdapterView<?> parentView)
                        {
                        }
                    });
                }
            }.start();
        }
        
        else if (id == R.id.tSignOutItem)
        {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(TeacherHomeActivity.this , UserTypeSelectionActivity.class);
            startActivity(intent);
            finish();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
