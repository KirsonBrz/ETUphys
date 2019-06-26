package com.etuspace.firebase.example.fireeats.java;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.etuspace.firebase.example.fireeats.java.LabFragments.FirstLabFragment;
import com.etuspace.firebase.example.fireeats.java.LabFragments.SecondLabFragment;
import com.etuspace.firebase.example.fireeats.java.LabFragments.ThirdLabFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.etuspace.firebase.example.fireeats.R;
import com.etuspace.firebase.example.fireeats.java.LabFragments.FourLabFragment;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.lang.reflect.Field;

public class MessagesActivity extends AppCompatActivity {


    public static final String KEY_USER_ID = "key_user_id";


    ViewGroup mEmptyView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);




        final ViewGroup containerLab = (findViewById(R.id.lab_container));

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        final FloatingActionButton fabGroup = findViewById(R.id.fabFindGroup);

        final ViewGroup spinnerContainter = findViewById(R.id.spinnerContainer);


        final MaterialSpinner spinnerFacultet = (MaterialSpinner) findViewById(R.id.spinnerFacultet);
        final MaterialSpinner spinnerCourse = (MaterialSpinner) findViewById(R.id.spinnerCourse);
        final MaterialSpinner spinnerGroup = (MaterialSpinner) findViewById(R.id.spinnerGroup);


        spinnerFacultet.setItems("Выберите факультет", "ФКТИ", "ФЭА");
        spinnerCourse.setItems("Выберите курс", "I", "II");
        final int[] positionFacultet = new int[1];

        spinnerFacultet.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

                spinnerCourse.setVisibility(View.VISIBLE);
                spinnerFacultet.setClickable(false);
                positionFacultet[0] = position;

            }
        });

        spinnerCourse.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {


                switch (position) {
                    case 0:

                        break;

                    case 1:
                        switch (positionFacultet[0]) {
                            case 0:

                                break;
                            case 1:
                                spinnerGroup.setItems("Выберите группу", "8305", "8306", "8307", "8308");
                                break;
                            case 2:
                                spinnerGroup.setItems("Выберите группу", "8491", "8492", "8493", "8494");
                                break;
                            default:
                                break;
                        }

                        break;
                    case 2:
                        switch (positionFacultet[0]) {
                            case 0:
                                break;
                            case 1:
                                spinnerGroup.setItems("Выберите группу", "7491", "7492", "7493", "7494");
                                break;
                            case 2:
                                spinnerGroup.setItems("Выберите группу", "7305", "7306", "7307", "7308");
                                break;
                            default:
                                break;
                        }

                        break;
                    default:
                        break;
                }
                spinnerGroup.setVisibility(View.VISIBLE);
                spinnerCourse.setClickable(false);


                spinnerGroup.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

                        viewPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager(), item));
                        tabLayout.setupWithViewPager(viewPager);


                        spinnerContainter
                                .animate()
                                .alpha(0.0f)
                                .translationY(300)
                                .setDuration(500)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        fabGroup.setVisibility(View.VISIBLE);
                                        containerLab.setVisibility(View.VISIBLE);
                                    }
                                });




                    }
                });


            }
        });

        fabGroup.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {

                spinnerGroup.setVisibility(View.GONE);
                spinnerCourse.setVisibility(View.GONE);


                containerLab.setVisibility(View.GONE);
                spinnerContainter
                        .animate()
                        .alpha(1.0f)
                        .translationY(0)
                        .setDuration(500)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);

                            }
                        });


                spinnerFacultet.setSelectedIndex(0);
                spinnerFacultet.setClickable(true);
                spinnerCourse.setSelectedIndex(0);
                spinnerCourse.setClickable(true);
                spinnerGroup.setSelectedIndex(0);
                fabGroup.setVisibility(View.GONE);


            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView_Bar_message);
        disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_profile:

                        Intent intent1 = new Intent(MessagesActivity.this, UserActivity.class);
                        intent1.putExtra(MainActivity.KEY_NEW_USER, "0");
                        intent1.putExtra(KEY_USER_ID, FirebaseAuth.getInstance().getCurrentUser().getUid());
                        startActivity(intent1);
                        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
                        break;

                    case R.id.ic_messages:

                        break;

                    case R.id.ic_labs:
                        Intent intent2 = new Intent(MessagesActivity.this, MainActivity.class);
                        startActivity(intent2);
                        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                        break;

                }


                return false;
            }
        });


    }


    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);

        } catch (NoSuchFieldException e) {
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e);
        }
    }


    public class SectionPagerAdapter extends FragmentPagerAdapter {
        String group;

        public SectionPagerAdapter(FragmentManager fm, String item) {
            super(fm);
            group = item;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FirstLabFragment();
                case 1:
                    return new SecondLabFragment();
                case 2:
                    return new ThirdLabFragment();
                case 3:
                default:
                    return new FourLabFragment();
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Первая работа";
                case 1:
                    return "Вторая работа";
                case 2:
                    return "Третья работа";
                default:
                    return "Четвертая работа";
            }
        }
    }

}






