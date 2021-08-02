package com.yuvalsuede.homefood;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Profile;
import com.squareup.picasso.Picasso;
import com.yuvalsuede.homefood.fragment.ContentListFragment;
import com.yuvalsuede.homefood.fragment.MyDishesFragment;
import com.yuvalsuede.homefood.fragment.MyFavoriteCookingFragment;
import com.yuvalsuede.homefood.fragment.SearchFragment;
import com.yuvalsuede.homefood.model.UserConfig;
import com.yuvalsuede.homefood.model.UserModel;
import com.yuvalsuede.homefood.picasso.CircleTransform;
import com.yuvalsuede.homefood.residemenu.ResideMenu;
import com.yuvalsuede.homefood.residemenu.ResideMenuItem;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ContentListFragment.ContentListFragmentListener {
    private static final String TAG = MainActivity.class.getName();
    public static int CREATE_DISH_CODE = 666;
    public static int CODE_FOR_FB_ACTIVITY = 155;

    private ResideMenu resideMenu;
    private ResideMenuItem menuItemDiscover;
    private ResideMenuItem menuItemMyFavoriteCooking;
    private ResideMenuItem menuItemMyDishes;
    private ResideMenuItem menuItemSearch;
    private FloatingActionButton buttonAdd;


    private ContentListFragment contentListFragment = ContentListFragment.newInstance(null);
    private MyFavoriteCookingFragment myFavoriteCookingFragment = MyFavoriteCookingFragment.newInstance();
    private MyDishesFragment myDishesFragment = MyDishesFragment.newInstance();
    private SearchFragment searchFragment = SearchFragment.newInstance();

    private TextView userName;
    private ImageView userIcon;
    private ImageView resideMenuSettingsIcon;


    private UserModel userModel;
    private App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (App) getApplication();
        if (app.needRestart()) {
            finish();
            startActivity(new Intent(this, SplashActivity.class));
        }
        userModel = app.getUserModel();

        UserConfig userConfig = app.getUserConfig();
        if (userConfig != null) {
            if (userConfig.getUserLanguage().equals("en")) {
                changeLang("en");
            } else {
                changeLang("iw");
            }
        }
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        setHomeButton(actionBar);

        buttonAdd = (FloatingActionButton) findViewById(R.id.add_dish);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userModel != null)
                    startActivityForResult(new Intent(getBaseContext(), CreateDishActivity.class), CREATE_DISH_CODE);
                else
                    startActivityForResult(new Intent(getBaseContext(), FBLoginActivity.class), CODE_FOR_FB_ACTIVITY);
            }
        });
        setUpMenu();
        contentListFragment.setFragmentListener(this);
        addFragment(contentListFragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        changeFragment(contentListFragment);
        userModel = app.getUserModel();
    }

    private void changeLang(String lang) {
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(lang.toLowerCase());
        res.updateConfiguration(conf, dm);
    }

    public void setAddButtonVisibility(boolean visibility) {
        if (visibility) {
            buttonAdd.setVisibility(View.VISIBLE);
        } else {
            buttonAdd.setVisibility(View.GONE);
        }
    }

    public ResideMenu getResideMenu() {
        return resideMenu;
    }

    public void setHomeButton(ActionBar actionBar) {
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_4);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setUpMenu() {

        // attach to current activity;
        resideMenu = new ResideMenu(this);
        resideMenu.setUse3D(true);
        resideMenu.setBackground(R.drawable.menu_back);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.7f);

        // create menu items;
        menuItemDiscover = new ResideMenuItem(this, getString(R.string.discover));
        menuItemMyFavoriteCooking = new ResideMenuItem(this, getString(R.string.my_favourite_cooking));
        menuItemMyDishes = new ResideMenuItem(this, getString(R.string.my_dishes));
        menuItemSearch = new ResideMenuItem(this, getString(R.string.search));
        menuItemDiscover.setActive();

        menuItemDiscover.setOnClickListener(this);
        menuItemMyFavoriteCooking.setOnClickListener(this);
        menuItemMyDishes.setOnClickListener(this);
        menuItemSearch.setOnClickListener(this);

        resideMenu.addMenuItem(menuItemDiscover, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(menuItemMyFavoriteCooking, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(menuItemMyDishes, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(menuItemSearch, ResideMenu.DIRECTION_LEFT);

        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

        resideMenu.setBackground(R.drawable.menu_back);

        userName = (TextView) resideMenu.getLeftMenuView().findViewById(R.id.residemenu_logintext);
        userIcon = (ImageView) resideMenu.getLeftMenuView().findViewById(R.id.residemenu_usericon);
        resideMenuSettingsIcon = (ImageView) resideMenu.getLeftMenuView().findViewById(R.id.settings_icon);
        resideMenuSettingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getApplicationContext(), SettingsActivity.class), SettingsActivity.LOGOUT_CODE_DONE);
            }
        });
        if (Profile.getCurrentProfile() != null) {
            Log.i(TAG, "logged in " + Profile.getCurrentProfile().getName());
            userName.setText(Profile.getCurrentProfile().getName());
            Picasso.with(this)
                    .load(Profile.getCurrentProfile().getProfilePictureUri(360, 360))
                    .transform(new CircleTransform())
                    .placeholder(R.drawable.placeholder_person)
                    .into(userIcon);
            userIcon.setOnClickListener(userListener);
            userName.setOnClickListener(userListener);
        } else {
            Log.i(TAG, "not logged in");
            Picasso.with(this)
                    .load(R.drawable.placeholder_person)
                    .transform(new CircleTransform())
                    .into(userIcon);
            userName.setText(R.string.login);
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivityForResult(new Intent(getBaseContext(), FBLoginActivity.class), CODE_FOR_FB_ACTIVITY);
                }
            };
            userName.setOnClickListener(listener);
            userIcon.setOnClickListener(listener);
        }
    }

    View.OnClickListener userListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, MyProfileActivity.class);
            startActivity(intent);
        }
    };
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {
        if (view == menuItemDiscover) {
            if (!menuItemDiscover.isActive()) {
                changeFragment(contentListFragment);
            }
            menuItemDiscover.setActive();
            menuItemMyDishes.setInactive();
            menuItemMyFavoriteCooking.setInactive();
            menuItemSearch.setInactive();
        } else if (view == menuItemMyFavoriteCooking) {
            if (userModel != null) {
                if (!menuItemMyFavoriteCooking.isActive()) {
                    changeFragment(myFavoriteCookingFragment);
                }
                menuItemDiscover.setInactive();
                menuItemMyDishes.setInactive();
                menuItemMyFavoriteCooking.setActive();
                menuItemSearch.setInactive();
            } else {
                startActivityForResult(new Intent(getBaseContext(), FBLoginActivity.class), CODE_FOR_FB_ACTIVITY);
            }
        } else if (view == menuItemMyDishes) {
            if (userModel != null) {
                if (!menuItemMyDishes.isActive()) {
                    changeFragment(myDishesFragment);
                }
                menuItemDiscover.setInactive();
                menuItemMyDishes.setActive();
                menuItemMyFavoriteCooking.setInactive();
                menuItemSearch.setInactive();
            } else {
                startActivityForResult(new Intent(getBaseContext(), FBLoginActivity.class), CODE_FOR_FB_ACTIVITY);
            }
        } else if (view == menuItemSearch) {
            startSearch();
        }

        resideMenu.closeMenu();
    }

    public void startSearch() {
        if (!menuItemSearch.isActive()) {
            changeFragment(searchFragment);
        }
        menuItemDiscover.setInactive();
        menuItemMyDishes.setInactive();
        menuItemMyFavoriteCooking.setInactive();
        menuItemSearch.setActive();
    }

    public void syncMenu(String simpleClassName) {
        if (simpleClassName.equals(contentListFragment.getClass().getSimpleName())) {
            menuItemDiscover.setActive();
            menuItemMyDishes.setInactive();
            menuItemMyFavoriteCooking.setInactive();
            menuItemSearch.setInactive();
        } else if (simpleClassName.equals(myFavoriteCookingFragment.getClass().getSimpleName())) {
            menuItemDiscover.setInactive();
            menuItemMyDishes.setInactive();
            menuItemMyFavoriteCooking.setActive();
            menuItemSearch.setInactive();
        } else if (simpleClassName.equals(myDishesFragment.getClass().getSimpleName())) {
            menuItemDiscover.setInactive();
            menuItemMyDishes.setActive();
            menuItemMyFavoriteCooking.setInactive();
            menuItemSearch.setInactive();
        } else if (simpleClassName.equals(searchFragment.getClass().getSimpleName())) {
            menuItemDiscover.setInactive();
            menuItemMyDishes.setInactive();
            menuItemMyFavoriteCooking.setInactive();
            menuItemSearch.setActive();
        }
    }

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {

        }

        @Override
        public void closeMenu() {

        }
    };

    public void changeFragment(Fragment targetFragment) {
        resideMenu.clearIgnoredViewList();
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_out_right)
                .replace(R.id.main_fragment, targetFragment, targetFragment.getClass().getSimpleName())
                .addToBackStack(targetFragment.getClass().getSimpleName())
                .commit();
    }

    private void addFragment(Fragment targetFragment) {
        resideMenu.clearIgnoredViewList();
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_out_right)
                .add(R.id.main_fragment, targetFragment, targetFragment.getClass().getSimpleName())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    public void setActionBarTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (resideMenu.isOpened()) {
            resideMenu.closeMenu();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_FOR_FB_ACTIVITY) {
            if (Profile.getCurrentProfile() != null) {
                Log.i(TAG, "logged in " + Profile.getCurrentProfile().getName());
                userName.setText(Profile.getCurrentProfile().getName());
                Picasso.with(this)
                        .load(Profile.getCurrentProfile().getProfilePictureUri(360, 360))
                        .transform(new CircleTransform())
                        .placeholder(R.drawable.placeholder_person)
                        .into(userIcon);
                userModel = app.getUserModel();
                resideMenuSettingsIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivityForResult(new Intent(getApplicationContext(), SettingsActivity.class), SettingsActivity.LOGOUT_CODE_DONE);
                    }
                });
                userIcon.setOnClickListener(userListener);
                userName.setOnClickListener(userListener);
            } else {
                Log.i(TAG, "not logged in");
                Picasso.with(this)
                        .load(R.drawable.placeholder_person)
                        .transform(new CircleTransform())
                        .into(userIcon);
                userName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivityForResult(new Intent(getBaseContext(), FBLoginActivity.class), CODE_FOR_FB_ACTIVITY);
                    }
                });
                resideMenuSettingsIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivityForResult(new Intent(getBaseContext(), FBLoginActivity.class), CODE_FOR_FB_ACTIVITY);
                    }
                });
                userIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivityForResult(new Intent(getBaseContext(), FBLoginActivity.class), CODE_FOR_FB_ACTIVITY);
                    }
                });
            }
        }

        if (requestCode == CREATE_DISH_CODE) {
            if (resultCode == RESULT_OK)
                contentListFragment.addCreatedDish(data.getIntExtra("create_dish_id", 0));
        }

        if (requestCode == SettingsActivity.LOGOUT_CODE_DONE) {
            if (resultCode == RESULT_OK) {
                //TODO doing something in main activity when user logout

                if (Profile.getCurrentProfile() != null) {
                    Log.e("logged in", Profile.getCurrentProfile().getName());
                    userName.setText(Profile.getCurrentProfile().getName());
                    Picasso.with(this)
                            .load(Profile.getCurrentProfile().getProfilePictureUri(360, 360))
                            .transform(new CircleTransform())
                            .placeholder(R.drawable.placeholder_person)
                            .into(userIcon);
                    userModel = app.getUserModel();
                    userName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                    resideMenuSettingsIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivityForResult(new Intent(getApplicationContext(), SettingsActivity.class), SettingsActivity.LOGOUT_CODE_DONE);
                        }
                    });
                } else {
                    Log.i("not logged in", "sry");
                    Picasso.with(this)
                            .load(R.drawable.placeholder_person)
                            .transform(new CircleTransform())
                            .into(userIcon);
                    userName.setText(R.string.login);
                    userName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivityForResult(new Intent(getBaseContext(), FBLoginActivity.class), CODE_FOR_FB_ACTIVITY);
                        }
                    });
                    userIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivityForResult(new Intent(getBaseContext(), FBLoginActivity.class), CODE_FOR_FB_ACTIVITY);
                        }
                    });
                    resideMenuSettingsIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivityForResult(new Intent(getBaseContext(), FBLoginActivity.class), CODE_FOR_FB_ACTIVITY);
                        }
                    });
                }
            }
        }
        if (resideMenu.isOpened()) resideMenu.closeMenu();
    }

    @Override
    public void goOnResume() {
        syncMenu(getClass().getSimpleName());
        setAddButtonVisibility(true);
        setActionBarTitle(getString(R.string.discover));
    }
}
