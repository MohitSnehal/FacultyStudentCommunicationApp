package com.example.anuj_ilm.connect_2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetails extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private FirebaseAuth mAuth ;
    private FirebaseDatabase database ;
    private DatabaseReference mDatabase;
    public static String name , roll, year, section , branch ;
    private User user;
    public WebView timeTableWebView;
    private String selectedSchool;
    private Spinner schoolSelectorSpinner;
    static ListView listView;
    private ListView inboxListView ;
    private static final int GALLERY_INTENT = 2 ;
    private ProgressDialog imageProgressDialog ;
    private ProgressDialog insertUserDetailsProgressDialog ;
    private DatabaseReference myRef;
    private StorageReference mStorageRef ;
    private Uri downloadUri ;
    private CircleImageView profilePictureImageView ;
    private String profileimageurl;


    //opening attachment
    public void openAttachment(View view)
    {
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction() ;
        Bundle args = new Bundle() ;
        args.putString("url" , InboxMessageFragment.selectedAttachmentUrl);
        AttachmentWebViewFragment obj = new AttachmentWebViewFragment() ;
        obj.setArguments(args) ;
        fragmentTransaction.replace(R.id.flMain , obj) ;
        fragmentTransaction.addToBackStack("bcd");
        fragmentTransaction.commit() ;
    }

    //loading inbox fragment
    public void loadInbox()
    {
        inboxListView = (ListView)findViewById(R.id.inboxListView) ;
        if(inboxListView == null && InboxFragment.inboxListView != null)
        {
            inboxListView =  OutboxFragment.outboxListview;
        }
        database = FirebaseDatabase.getInstance() ;
        DatabaseReference inboxMessageRef = database.getReference("messages/" + branch + "/" + year + "/" + section) ;
        inboxMessageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ArrayList<FacultyMessage> array = new ArrayList<>();
                for(DataSnapshot mySnapshot: dataSnapshot.getChildren()) {
                    //User tmpUser;
                    FacultyMessage value = mySnapshot.getValue(FacultyMessage.class);
                    array.add(value) ;

                }
                if (array.size() != 0)
                {
                    FacultyMessage arr[] = new FacultyMessage[array.size()] ;
                    int i = array.size() - 1  ;
                    for (FacultyMessage tmp : array)
                    {
                        arr[i] = tmp ;
                        i-- ;
                    }
                    ListAdapter listAdapter = new CustomAdapterInboxMessages(UserDetails.this, arr);
                    inboxListView.setAdapter(listAdapter);
                }
            }


            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("recieve", "Failed to read value.", error.toException());
            }
        });


        inboxListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                                .beginTransaction() ;
                        Bundle args = new Bundle();
                        ListAdapter listAdapter = inboxListView.getAdapter();
                        FacultyMessage selectedMessaqe = (FacultyMessage)listAdapter.getItem(position);

                        args.putString("sender",selectedMessaqe.getSender());
                        args.putString("body",selectedMessaqe.getBody());
                        args.putString("attachmentUrls" , selectedMessaqe.getAttachmentUrls());
                        args.putString("subject" , selectedMessaqe.getSubject());
                        args.putString("timestamp" , selectedMessaqe.getTimestamp());
                        InboxMessageFragment obj = new InboxMessageFragment();
                        Log.d("rece" , args.getString("sender")) ;
                        obj.setArguments(args);
                        fragmentTransaction.replace(R.id.flMain , obj) ;
                        fragmentTransaction.addToBackStack("bcd");
                        fragmentTransaction.commit() ;
                    }
                }
        );
    }

    //uploading profile image start
    public void uploadProfilePicture(View view)
    {
        Intent in = new Intent(Intent.ACTION_PICK) ;
        in.setType("image/*") ;
        startActivityForResult(in , GALLERY_INTENT);
    }

    //for picture upload
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_INTENT && resultCode==RESULT_OK)
        {
            imageProgressDialog = new ProgressDialog(this) ;
            imageProgressDialog.setMessage("uploading");
            imageProgressDialog.show();
            Uri uri = data.getData() ;
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("users/students/" + roll);

            mStorageRef = FirebaseStorage.getInstance().getReference();
            StorageReference path = mStorageRef.child("userProfilePictures").child(roll) ;
            //saving the image in firebase storage
            path.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(UserDetails.this, "upload successful", Toast.LENGTH_LONG).show();

                    profilePictureImageView = (CircleImageView)findViewById(R.id.profilePictureImageView) ;
                    //retrieving the image url from cloud storage
                    downloadUri = taskSnapshot.getDownloadUrl() ;
                    String linkToImage = downloadUri.toString() ;
                    System.out.println(linkToImage);
                    //creating an updated user(imageurl changed) and updating it in firebase database
                    User updatedUser = new User(name, year ,section, branch, linkToImage);
                    myRef.setValue(updatedUser);

                    //setting imageView with selected profile picture
                    Picasso.with(UserDetails.this).load(downloadUri).fit().centerCrop().into(profilePictureImageView);

                    imageProgressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UserDetails.this, "uploaded unsuccessful", Toast.LENGTH_LONG).show();
                    imageProgressDialog.dismiss();
                }
            });
        }


    }

    //uploading profile image end

    public static String getBranch() {
        return branch;
    }

    //onClick function of refresh imageButton in timeTable fragment
    public void showTimeTableHelper(View view)
    {
        try {
            showTimeTable();
        }catch(java.net.MalformedURLException malformedURLException){
            Log.d("MalformedURLFound",malformedURLException.getMessage());
        }
    }

    public void showTimeTable() throws java.net.MalformedURLException
    {

        //retreiving url from database
        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("timetable/" + branch + "/" + year);
        System.out.println("users/" + branch + "/" + year );
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot mySnapshot: dataSnapshot.getChildren()) {
                    //User tmpUser;
                    final String timeTableURL = mySnapshot.getValue().toString();
                    Log.d("timetablepdf value : ",timeTableURL);
                    timeTableWebView = (WebView) findViewById(R.id.timeTableWebView);
                    timeTableWebView.getSettings().setJavaScriptEnabled(true);
                    timeTableWebView.setWebViewClient(new WebViewClient() {
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            return false;
                        }
                    });
                    timeTableWebView.loadUrl(timeTableURL);
                    Toast.makeText(UserDetails.this, "Loading...", Toast.LENGTH_SHORT).show();

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
            fragmentTransaction.replace(R.id.flMain, new SceCardFragment());
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
                        int resID = getResources().getIdentifier(stringResourceName , "array",
                                "com.example.anuj_ilm.connect_2");
                        ArrayAdapter<CharSequence> facultyAdapter = ArrayAdapter.createFromResource(UserDetails.this,
                                resID , android.R.layout.simple_spinner_item);
                        String name = facultyAdapter.getItem(0).toString();
                        String designation = facultyAdapter.getItem(1).toString();
                        String imageName = facultyAdapter.getItem(5).toString();
                        array[i] = new FacultyInfo(name,designation,imageName);
                        Log.d("imageName : " , imageName);
                    }

                    ListAdapter listAdapter = new CustomAdapterSceCard(UserDetails.this,array);
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
                                    fragmentTransaction.replace(R.id.flMain , obj) ;
                                    fragmentTransaction.addToBackStack("acd");
                                    fragmentTransaction.commit() ;
                                }
                            }
                    );
                }
            }.start();

        }
    }

    //after successfully signing in, this function will populate the user details' fields
    public void insertUserDetails()
    {

        //starting progress dialog box


        //since using in inner class we need to make these final
        final TextView nameField = (TextView)findViewById(R.id.usernameField) ;
        final TextView sectionField = (TextView)findViewById(R.id.sectionField) ;
        final TextView rollNumberField = (TextView)findViewById(R.id.rollNumberField) ;
        final TextView branchField = (TextView)findViewById(R.id.branchField) ;
        final TextView yearField = (TextView)findViewById(R.id.yearField) ;

        //default text in the user details' fields
        nameField.setText("XXXXX");
        sectionField.setText("XXXXX");
        branchField.setText("XXXXX");
        yearField.setText("XXXXX");
        rollNumberField.setText(roll);


        FirebaseUser user = mAuth.getCurrentUser();
        String emailParts[] = user.getEmail().split("@");
        roll = emailParts[0];
        database = FirebaseDatabase.getInstance();
        //final boolean userInfoUpdated = false;


        DatabaseReference ref = database.getReference("users/students/"+roll);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for(DataSnapshot mySnapshot: dataSnapshot.getChildren()) {
                    //User tmpUser;
                    if(i==0) {
                        branchField.setText(mySnapshot.getValue().toString());
                        branch = branchField.getText().toString();
                        Log.d("branch",branch);
                    }
                    if(i==1) {
                        //populating values into the Navigation Drawer's username and email
                        TextView navHeaderUserName = (TextView) findViewById(R.id.navHeaderUserName);
                        nameField.setText(mySnapshot.getValue().toString());
                        navHeaderUserName.setText(mySnapshot.getValue().toString());
                        TextView navHeaderEmail = (TextView) findViewById(R.id.navHeaderEmail);
                        navHeaderEmail.setText(roll + "@kiit.ac.in");
                        name = nameField.getText().toString();
                        Log.d("name",name);
                    }
                    if(i==3) {
                        sectionField.setText(mySnapshot.getValue().toString());
                        section = sectionField.getText().toString();
                        Log.d("section",section);
                    }
                    if(i==4) {
                        yearField.setText(mySnapshot.getValue().toString());
                        year = yearField.getText().toString();
                        Log.d("year",year);
                        insertUserDetailsProgressDialog.dismiss();
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
       rollNumberField.setText(roll);
       if(branch != null)
       {

       }

       //populating info into variables?


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        mAuth = FirebaseAuth.getInstance();
        timeTableWebView = (WebView) findViewById(R.id.timeTableWebView);



        //initialising all the UI components for the Navigation Drawer
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "For any queries mail to : kiitapp.root@gmail.com", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(UserDetails.this);



        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction() ;
        fragmentTransaction.replace(R.id.flMain , new ProfileFragment()) ;
        fragmentTransaction.commit() ;

        navigationView.setCheckedItem(R.id.profileItem);


        insertUserDetailsProgressDialog = new ProgressDialog(this) ;
        insertUserDetailsProgressDialog.setMessage("Loading data");
        insertUserDetailsProgressDialog.show();


        new CountDownTimer(100 , 1000){
            boolean flag = true;
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                insertUserDetails();
                onProfileItemSelectedHelper();
            }
        }.start();

        //faculty fragment spinner start

        //faculty fragment spinner end

    }

    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            Log.d("backStackEntryCount", Integer.toString(count));
            super.onBackPressed();
            //additional code
        } else {
            Log.d("backStackEntryCount", Integer.toString(count));
            getFragmentManager().popBackStack();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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


    public void onProfileItemSelectedHelper()
    {
        insertUserDetails();
        profilePictureImageView = (CircleImageView)findViewById(R.id.profilePictureImageView) ;

        //retrieving image url from firebase database and using that url for setting profile picture from firebase storage
        database = FirebaseDatabase.getInstance();


        DatabaseReference ref = database.getReference("users/students/" + roll);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for(DataSnapshot mySnapshot: dataSnapshot.getChildren()) {

                    if(i==2)
                    {
                        profileimageurl = mySnapshot.getValue().toString();
                        System.out.print("lodu"+profileimageurl);
                        if (!profileimageurl.equals("na")) {
                            profilePictureImageView = (CircleImageView)findViewById(R.id.profilePictureImageView) ;
                            Picasso.with(UserDetails.this).load(profileimageurl).fit().centerCrop().into(profilePictureImageView);
                        }
                    }
                    i++ ;
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profileItem)
        {

            //Toast.makeText(this, "profile item", Toast.LENGTH_SHORT).show();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction() ;
            fragmentTransaction.replace(R.id.flMain , new ProfileFragment()) ;
            fragmentTransaction.commit() ;

            new CountDownTimer(100 , 1000){
                boolean flag = true;
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {

                    imageProgressDialog = new ProgressDialog(UserDetails.this) ;
                    onProfileItemSelectedHelper();

                }
            }.start();

        }
        else if (id == R.id.inboxItem)
        {
            //Toast.makeText(this, "inbox", Toast.LENGTH_SHORT).show();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction() ;
            fragmentTransaction.replace(R.id.flMain , new InboxFragment()) ;
            fragmentTransaction.commit() ;

            new CountDownTimer(100 , 1000){
                boolean flag = true;
                @Override
                public void onTick(long millisUntilFinished) {
                }
                @Override
                public void onFinish() {
                    loadInbox() ;
                }
            }.start();
        }
        else if (id == R.id.findFaciltyDetailsItem) //change this id as typo
        {
            //Toast.makeText(this, "faculty details", Toast.LENGTH_SHORT).show();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction() ;
            fragmentTransaction.replace(R.id.flMain , new FacultyFragment()) ;
            fragmentTransaction.commit() ;

            new CountDownTimer(100 , 1000){
                boolean flag = true;
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {

                    schoolSelectorSpinner = (Spinner)findViewById(R.id.schoolSpinner);
                    selectedSchool = "School of Computer Engineering";

                    ArrayAdapter<CharSequence> schoolsAdapter = ArrayAdapter.createFromResource(UserDetails.this,
                            R.array.schools, android.R.layout.simple_spinner_item);
                    schoolsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    schoolSelectorSpinner.setAdapter(schoolsAdapter);

                    schoolSelectorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

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
        else if (id == R.id.timetableItem)
        {
            //Toast.makeText(this, "faculty details", Toast.LENGTH_SHORT).show();
            //Displaying the time table in the timeTableWebView
            try {
                showTimeTable();
            }catch(java.net.MalformedURLException malformedURLException){
                Log.d("MalformedURLFound",malformedURLException.getMessage());
            }

            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction() ;
            fragmentTransaction.replace(R.id.flMain , new TimeTableFragment()) ;
            fragmentTransaction.commit() ;

        }
        else if (id == R.id.signoutItem)
        {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(UserDetails.this,UserTypeSelectionActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
