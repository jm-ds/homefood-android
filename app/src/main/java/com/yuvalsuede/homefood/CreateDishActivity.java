package com.yuvalsuede.homefood;

import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.gson.JsonObject;
import com.gun0912.tedpicker.Config;
import com.gun0912.tedpicker.ImagePickerActivity;
import com.soundcloud.android.crop.Crop;
import com.yuvalsuede.homefood.fragment.create.MapLocationFragment;
import com.yuvalsuede.homefood.fragment.create.PickCuisineAndCategoryFragment;
import com.yuvalsuede.homefood.fragment.create.PickDataAboutDishFragment;
import com.yuvalsuede.homefood.fragment.create.PublishFragment;
import com.yuvalsuede.homefood.model.MakeDish;
import com.yuvalsuede.homefood.model.UserModel;
import com.yuvalsuede.homefood.rest.HomeFoodService;
import com.yuvalsuede.homefood.rest.RestService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateDishActivity extends AppCompatActivity {

    public enum NavStep {
        PHOTO, CUISINE, CATEGORY, LOCATION, ABOUT, FINAL
    }

    public final static int PICK_IMAGE_CODE = 348;
    private static final String TAG = CreateDishActivity.class.getName();

    public File fileImage;
    public HomeFoodService homeFoodService;
    private UserModel userModel;
    public MakeDish dish = new MakeDish();
    public ProgressDialog progressDialog;
    private App app;
    private NavStep currentStep = NavStep.PHOTO;
    private Button buttonNext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_dish);
        app = (App) getApplication();
        if (app.needRestart()) {
            finish();
            startActivity(new Intent(this, SplashActivity.class));
        }
        userModel = app.getUserModel();
        dish.setUserId(userModel.getUserId());
        dish.setAccessToken(userModel.getToken());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        homeFoodService = RestService.getService();
        buttonNext = (Button) findViewById(R.id.button_next);
        buttonNext.setVisibility(View.GONE);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapLocationFragment mapFragment = (MapLocationFragment) getSupportFragmentManager().findFragmentById(R.id.create_dish_container);
                if (mapFragment != null) {
                    dish.setLat(Double.toString(mapFragment.getSelectedLocation().latitude));
                    dish.setLng(Double.toString(mapFragment.getSelectedLocation().longitude));
                    setUI(NavStep.ABOUT);
                }
            }
        });
        addFragment(PickCuisineAndCategoryFragment.newInstance(0));
        startPhoto();
    }

    public void setUI(NavStep navStep) {
        buttonNext.setVisibility(View.GONE);
        currentStep = navStep;
        if (navStep == NavStep.PHOTO) {
            startPhoto();
            addFragment(PickCuisineAndCategoryFragment.newInstance(0));
        } else if (navStep == NavStep.CUISINE) {
            addFragment(PickCuisineAndCategoryFragment.newInstance(0));
        } else if (navStep == NavStep.CATEGORY) {
            addFragment(PickCuisineAndCategoryFragment.newInstance(1));
        } else if (navStep == NavStep.LOCATION) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) actionBar.setTitle("Location");
            addFragment(new MapLocationFragment());
            buttonNext.setVisibility(View.VISIBLE);
        } else if (navStep == NavStep.ABOUT) {
            addFragment(PickDataAboutDishFragment.newInstance());
        } else if (navStep == NavStep.FINAL) {
            addFragment(PublishFragment.newInstance());
        }
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed");
        if (currentStep == NavStep.FINAL) {
            setUI(NavStep.ABOUT);
        } else if (currentStep == NavStep.ABOUT) {
            setUI(NavStep.LOCATION);
        } else if (currentStep == NavStep.LOCATION) {
            setUI(NavStep.CATEGORY);
        } else if (currentStep == NavStep.CATEGORY) {
            setUI(NavStep.CUISINE);
        } else if (currentStep == NavStep.CUISINE) {
            setUI(NavStep.PHOTO);
        } else {
            super.onBackPressed();
        }
    }

    public void startPhoto() {
        int numCameras = Camera.getNumberOfCameras();
        if (numCameras > 0) {
            Config config = new Config();
            config.setSelectionMin(1);
            config.setSelectionLimit(1);
            ImagePickerActivity.setConfig(config);
            Intent intent = new Intent(getBaseContext(), ImagePickerActivity.class);
            startActivityForResult(intent, PICK_IMAGE_CODE);
        } else {
            fileImage = null;
        }
    }

    public void createDish() {
//        hashMap.put("app_secret", "a231b09dbc4476568642414fe6b1563d");
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.waiting));
        progressDialog.setCancelable(false);
        progressDialog.show();
        Call<JsonObject> createDish = homeFoodService.createDishForUser(dish);
        createDish.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                if (response.errorBody() == null) {
                    Log.i(TAG, "creating " + response.body().toString());
                    int dish_id = response.body().get("data").getAsInt();
                    Intent result = new Intent();
                    result.putExtra("create_dish_id", dish_id);
                    setResult(MainActivity.CREATE_DISH_CODE, result);
                    finish();
                } else {
                    String s;
                    try {
                        s = response.errorBody().string();
                        Toast.makeText(CreateDishActivity.this, s, Toast.LENGTH_LONG).show();
                        Log.e(TAG, " fail " + s);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, " fail ", e);
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, t.getLocalizedMessage(), t);
                Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
            }
        });
    }

//    public void setMapLayout() {
//        setContentView(R.layout.fragment_create_dish_pick_location);
//        final MapLocationFragment mapFragment = (MapLocationFragment) getSupportFragmentManager().findFragmentById(R.id.dish_details_map);
//        Button buttonNext = (Button) findViewById(R.id.button_next);
//        if (buttonNext != null)
//            buttonNext.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    dish.setLat(Double.toString(mapFragment.getSelectedLocation().latitude));
//                    dish.setLng(Double.toString(mapFragment.getSelectedLocation().longitude));
//                    setContentView(R.layout.activity_create_dish);
//                    setUI(NavStep.ABOUT);
//                }
//            });
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) actionBar.setTitle("Location");
//    }

    public void addFragment(Fragment targetFragment) {
        getSupportFragmentManager()
                .beginTransaction()
//                .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_right)
                .replace(R.id.create_dish_container, targetFragment, "fragment")
                .commit();
        Log.d(TAG, "Succefully changed");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "request " + requestCode + ", result " + resultCode);
        if (requestCode == PICK_IMAGE_CODE) {
            if (resultCode == RESULT_OK) {
                ArrayList<Uri> image_uris = data.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
                File requestFile = new File(image_uris.get(0).getEncodedPath());
                fileImage = new File(getExternalFilesDir("image"), Calendar.getInstance().getTimeInMillis() + ".jpg");
                Crop.of(Uri.fromFile(requestFile), Uri.fromFile(fileImage)).withAspect(4, 3).start(this);
            } else if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }

        if (requestCode == Crop.REQUEST_CROP) {
            if (resultCode == RESULT_CANCELED)
                finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        FacebookSdk.sdkInitialize(this);
    }
}
