package com.etuspace.firebase.example.fireeats.java;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.etuspace.firebase.example.fireeats.java.adapter.AllLabAdapter;
import com.etuspace.firebase.example.fireeats.java.model.Lab;
import com.etuspace.firebase.example.fireeats.java.model.Rating;
import com.etuspace.firebase.example.fireeats.java.util.RatingUtil;
import com.etuspace.firebase.example.fireeats.java.util.RestaurantUtil;
import com.etuspace.firebase.example.fireeats.java.viewmodel.MainActivityViewModel;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.etuspace.firebase.example.fireeats.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements
        FilterDialogFragment.FilterListener,
        AllLabAdapter.OnRestaurantSelectedListener {

    public int k = 1;

    private static final String TAG = "MainActivity";

    private static final int RC_SIGN_IN = 9001;

    String checkNew = "0";

    private static final int LIMIT = 50;
    int cou = 0;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.textCurrentSearch)
    TextView mCurrentSearchView;

    @BindView(R.id.textCurrentSortBy)
    TextView mCurrentSortByView;

    @BindView(R.id.recyclerRestaurants)
    RecyclerView mRestaurantsRecycler;


    @BindView(R.id.viewEmpty)
    ViewGroup mEmptyView;

    private FirebaseFirestore mFirestore;
    private Query mQuery;

    public static final String KEY_NEW_USER = "key_new_user";
    public static final String KEY_USER_ID = "key_user_id";


    private FilterDialogFragment mFilterDialog;
    private AllLabAdapter mAdapter;

    private MainActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);


        // View model
        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);


        // Firestore
        mFirestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        mFirestore.setFirestoreSettings(settings);

        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true);

        // Get ${LIMIT} restaurants
        mQuery = mFirestore.collection("restaurants")
                .orderBy("avgRating", Query.Direction.DESCENDING)
                .limit(LIMIT);

        // RecyclerView
        mAdapter = new AllLabAdapter(mQuery, this) {
            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    mRestaurantsRecycler.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                } else {
                    mRestaurantsRecycler.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors

            }
        };

        mRestaurantsRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRestaurantsRecycler.setAdapter(mAdapter);

        // Filter Dialog
        mFilterDialog = new FilterDialogFragment();


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView_Bar);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        MessagesActivity.disableShiftMode(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.ic_profile:

                        Intent intent3 = new Intent(MainActivity.this, UserActivity.class);
                        intent3.putExtra(KEY_NEW_USER, checkNew);
                        intent3.putExtra(KEY_USER_ID, FirebaseAuth.getInstance().getCurrentUser().getUid());

                        startActivity(intent3);
                        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
                        break;

                    case R.id.ic_messages:
                        Intent intent1 = new Intent(MainActivity.this, MessagesActivity.class);
                        startActivity(intent1);
                        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
                        break;

                    case R.id.ic_labs:

                        break;

                }

                return false;
            }
        });


    }

    private ArrayList<String> getFans() {

        ArrayList<String> fan = new ArrayList<>();

        fan.add("Свобода творчества — свобода делать ошибки. Петр Капица");
        fan.add("Ничто не мешает человеку завтра стать умнее, чем он был вчера. Петр Капица");
        fan.add("Человек молод, когда он еще не боится делать глупости. Петр Капица");
        fan.add("Главный признак таланта — это когда человек знает, чего он хочет. Петр Капица");
        fan.add("Если тебя квантовая физика не испугала, значит, ты ничего в ней не понял. Нильс Бор");
        fan.add("Только дурак нуждается в порядке — гений господствует над хаосом. Альберт Энштейн");
        fan.add("Самое непостижимое в этом мире — это то, что он постижим. Альберт Энштейн");
        fan.add("Чтобы пробить стену лбом, нужен или большой разбег, или много лбов. Альберт Энштейн");
        fan.add("Гений есть терпение мысли, сосредоточенной в известном направлении. Исаак Ньютон");
        fan.add("Если я видел дальше других, то потому, что стоял на плечах гигантов. Исаак Ньютон");
        fan.add("Природа проста и не роскошествует излишними причинами. Исаак Ньютон");
        fan.add("Вы думаете, всё так просто? Да, всё просто. Но совсем не так. Альберт Энштейн");
        fan.add("Кто никогда не совершал ошибок, тот никогда не пробовал что-то новое. Альберт Энштейн");
        fan.add("Физика — это наука понимать природу. Эрик Роджерс");
        fan.add("Существует лишь то, что можно измерить. Марк Планк");
        fan.add("В сущности, теоретическая физика слишком трудна для физиков. Давид Гильберт");

        return fan;
    }


    @Override
    public void onStart() {
        super.onStart();

        // Start sign in if necessary
        if (shouldStartSignIn()) {
            startSignIn();
            return;
        }

        Snackbar.make(findViewById(android.R.id.content),
                getFans().get((int) (Math.random() * 16)), 2500).show();


        // Apply filters
        onFilter(mViewModel.getFilters());

        // Start listening for Firestore updates
        if (mAdapter != null) {
            mAdapter.startListening();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_items:
                //onAddItemsClicked();
                String text;

                switch (cou) {
                    case 0:
                        text = "Ну за такое дядюшка Ньютон точно поругал бы, сказано же, не жать!";
                        break;

                    case 1:
                        text = "Не ну ты серьёзно?";
                        break;
                    case 2:
                        text = "Еще один раз, и всё вылетит";
                        break;
                    case 3:
                        this.finish();
                    default:
                        AuthUI.getInstance().signOut(this);
                        text = "Что посеешь, то и пожнёшь!";
                }

                Toast.makeText(this,
                        text, Toast.LENGTH_LONG).show();

                cou++;

                break;
            case R.id.menu_sign_out:
                AuthUI.getInstance().signOut(this);
                startSignIn();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            mViewModel.setIsSigningIn(false);


            if (resultCode != RESULT_OK) {
                if (response == null) {
                    // User pressed the back button.
                    finish();
                } else if (response.getError() != null
                        && response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSignInErrorDialog(R.string.message_no_network);
                } else {
                    showSignInErrorDialog(R.string.message_unknown);
                }
            }

        }

        Intent intentnew = new Intent(MainActivity.this, UserActivity.class);
        intentnew.putExtra(KEY_NEW_USER, checkNew);
        intentnew.putExtra(KEY_USER_ID, FirebaseAuth.getInstance().getCurrentUser().getUid());

        startActivity(intentnew);
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        checkNew = "0";

    }

    @OnClick(R.id.filterBar)
    public void onFilterClicked() {
        // Show the dialog containing filter options
        mFilterDialog.show(getSupportFragmentManager(), FilterDialogFragment.TAG);
    }

    @OnClick(R.id.buttonClearFilter)
    public void onClearFilterClicked() {
        mFilterDialog.resetFilters();

        onFilter(Filters.getDefault());
    }

    @Override
    public void onRestaurantSelected(DocumentSnapshot restaurant) {
        // Go to the details page for the selected restaurant
        Intent intent = new Intent(this, LabInfoActivity.class);
        intent.putExtra(LabInfoActivity.KEY_RESTAURANT_ID, restaurant.getId());

        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
    }

    @Override
    public void onFilter(Filters filters) {
        // Construct query basic query
        Query query = mFirestore.collection("restaurants");

        // Category (equality filter)
        if (filters.hasCategory()) {
            query = query.whereEqualTo(Lab.FIELD_CATEGORY, filters.getCategory());
        }

        // City (equality filter)
        if (filters.hasCity()) {
            query = query.whereEqualTo(Lab.FIELD_CITY, filters.getCity());
        }

        // Price (equality filter)
        if (filters.hasPrice()) {
            query = query.whereEqualTo(Lab.FIELD_PRICE, filters.getPrice());
        }

        // Sort by (orderBy with direction)
        if (filters.hasSortBy()) {
            query = query.orderBy(filters.getSortBy(), filters.getSortDirection());
        }

        // Limit items
        query = query.limit(LIMIT);

        // Update the query
        mAdapter.setQuery(query);

        // Set header
        mCurrentSearchView.setText(Html.fromHtml(filters.getSearchDescription(this)));
        mCurrentSortByView.setText(filters.getOrderDescription(this));

        // Save filters
        mViewModel.setFilters(filters);
    }

    private boolean shouldStartSignIn() {

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            checkNew = "1";
        }

        return (!mViewModel.getIsSigningIn() && FirebaseAuth.getInstance().getCurrentUser() == null);

    }

    private void startSignIn() {

        // Sign in with FirebaseUI
        Intent intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(Collections.singletonList(
                        new AuthUI.IdpConfig.PhoneBuilder().build()))
                .setIsSmartLockEnabled(false)
                .build();


        startActivityForResult(intent, RC_SIGN_IN);


        mViewModel.setIsSigningIn(true);


    }

    private void onAddItemsClicked() {
        // Add a bunch of random restaurants
        WriteBatch batch = mFirestore.batch();
        for (int i = 0; i < 10; i++) {
            DocumentReference restRef = mFirestore.collection("restaurants").document();

            // Create random restaurant / ratings
            Lab randomLab = RestaurantUtil.getRandom(this);
            List<Rating> randomRatings = RatingUtil.getRandomList(randomLab.getNumRatings());
            randomLab.setAvgRating(RatingUtil.getAverageRating(randomRatings));

            Toast.makeText(this, FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
            // Add restaurant
            batch.set(restRef, randomLab);

            // Add ratings to subcollection
            for (Rating rating : randomRatings) {
                batch.set(restRef.collection("ratings").document(), rating);
            }
        }

        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Write batch succeeded.");
                } else {
                    Log.w(TAG, "write batch failed.", task.getException());
                }
            }
        });
    }

    private void showSignInErrorDialog(@StringRes int message) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.title_sign_in_error)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.option_retry, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startSignIn();
                    }
                })
                .setNegativeButton(R.string.option_exit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).create();

        dialog.show();
    }
}


