package com.etuspace.firebase.example.fireeats.java;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.etuspace.firebase.example.fireeats.java.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.etuspace.firebase.example.fireeats.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class UserActivity extends AppCompatActivity {

    private static final String TAG = "UserActivity";
    private FirebaseFirestore mFirestore;
    private Query mQuery;
    public static final String KEY_NEW_USER = "key_new_user";


    Toolbar mToolbar;

    AppCompatEditText userNameEdit;
    AppCompatEditText userSurnameEdit;
    AppCompatEditText userEmailEdit;
    AppCompatEditText userGroupEdit;

    AppCompatTextView userName;
    AppCompatTextView userSurname;
    AppCompatTextView userEmail;
    AppCompatTextView userPosition;
    AppCompatTextView userGroup;

    AppCompatImageButton photo;
    FloatingActionButton fab;

    RadioGroup radioGroup;

    RelativeLayout containerCreate;
    RelativeLayout containerLoaded;

    Button createButton;

    KeyDialogFragment mKeyDialog;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        setSupportActionBar(mToolbar);
        mFirestore = FirebaseFirestore.getInstance();


        // Get new user from extras
        String newUser = getIntent().getExtras().getString(KEY_NEW_USER);
        String userId = getIntent().getExtras().getString(MainActivity.KEY_USER_ID);

        userName = findViewById(R.id.userName);
        userSurname = findViewById(R.id.userSurname);
        userEmail = findViewById(R.id.userEmail);
        userPosition = findViewById(R.id.userPosition);
        userGroup = findViewById(R.id.userGroup);

        userNameEdit = findViewById(R.id.userNameEdit);
        userSurnameEdit = findViewById(R.id.userSurnameEdit);
        userEmailEdit = findViewById(R.id.userEmailEdit);
        userGroupEdit = findViewById(R.id.userGroupEdit);

        photo = findViewById(R.id.userImage);
        fab = findViewById(R.id.fabSendMessage);

        containerCreate = findViewById(R.id.container_create);
        containerLoaded = findViewById(R.id.container_loaded);
        radioGroup = findViewById(R.id.radioUser);

        mKeyDialog = new KeyDialogFragment();


        if (newUser.equals("1")) {
            containerCreate.setVisibility(View.VISIBLE);
            containerLoaded.setVisibility(View.GONE);
            fab.setVisibility(View.INVISIBLE);


        } else {


            DocumentReference docRef = mFirestore.collection("users").document(userId);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    User user = documentSnapshot.toObject(User.class);
                    loadInfo(user);
                }
            });

        }


        createButton = findViewById(R.id.button_create_user);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (userNameEdit.getText().toString().equals("") || userSurnameEdit.getText().toString().equals("") || userEmailEdit.getText().toString().equals("")) {
                    Toast.makeText(UserActivity.this, "Одно из полей введено неверно, повторите попытку", Toast.LENGTH_SHORT).show();

                } else if (mKeyDialog.checkTrue == 0 && userGroupEdit.getText().toString().equals("")) {
                    Toast.makeText(UserActivity.this, "Вы должны ввести ключ доступа, чтобы зарегестрироваться как преподаватель", Toast.LENGTH_SHORT).show();
                    mKeyDialog.show(getSupportFragmentManager(), KeyDialogFragment.TAG);

                } else {
                    createUser();

                    Intent intent = new Intent(UserActivity.this, MainActivity.class);

                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                }


            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.radioStudent:

                        userGroupEdit.setVisibility(View.VISIBLE);
                        break;
                    case R.id.radioTeacher:

                        userGroupEdit.setVisibility(View.INVISIBLE);
                        break;

                    default:
                        break;
                }
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserActivity.this, "Привет!", Toast.LENGTH_SHORT).show();


            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserActivity.this, "Отправить сообщение", Toast.LENGTH_SHORT).show();
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView_Bar_user);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        MessagesActivity.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_profile:

                        break;


                    case R.id.ic_messages:

                        Intent intent1 = new Intent(UserActivity.this, MessagesActivity.class);
                        startActivity(intent1);
                        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                        break;

                    case R.id.ic_labs:

                        Intent intent2 = new Intent(UserActivity.this, MainActivity.class);
                        startActivity(intent2);
                        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);

                        break;

                }


                return false;
            }
        });


    }


    private void createUser() {


        String position = "";
        String group = "";

        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.radioTeacher:
                position = "Преподаватель";
                group = "-";
                break;
            case R.id.radioStudent:
                position = "Студент";
                group = userGroupEdit.getText().toString();
                break;
            default:
                break;
        }


        // Create user
        User user = new User(userNameEdit.getText().toString()
                , userSurnameEdit.getText().toString()
                , userEmailEdit.getText().toString()
                , position
                , group, "0", "0", "0", "0");


            mFirestore.collection("users").document(FirebaseAuth.getInstance().getUid())
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(UserActivity.this, "Вы успешно зарегестрировались!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });





    }


    private void loadInfo(User user) {

        userName.setText(user.getName());
        userSurname.setText(user.getSurname());
        userEmail.setText(user.getEmail());
        userPosition.setText("Должность: " + user.getPosition());
        userGroup.setText("Группа " + user.getGroup());


    }


}
