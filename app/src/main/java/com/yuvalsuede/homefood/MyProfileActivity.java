package com.yuvalsuede.homefood;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.yuvalsuede.homefood.model.Profile;
import com.yuvalsuede.homefood.rest.HomeFoodService;
import com.yuvalsuede.homefood.rest.RestService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProfileActivity extends AppCompatActivity {

    private static final String TAG = MyProfileActivity.class.getName();
    private App app;
    private Gson gson;

    private TextView userName, userMale, userMail, userPhone, userAbout;
    private ImageView userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        app = (App) getApplication();
        gson = new Gson();
        initUI();
    }

    private void initUI() {
        userName = (TextView) findViewById(R.id.user_details_name);
        userMale = (TextView) findViewById(R.id.user_details_male);
        userMail = (TextView) findViewById(R.id.user_details_email_text);
        userPhone = (TextView) findViewById(R.id.user_details_phone_text);
        userImage = (ImageView) findViewById(R.id.user_details_user_image);
        userAbout = (TextView) findViewById(R.id.aboutUser);

    }

    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        HomeFoodService homeFoodService = RestService.getService();
        Call<JsonObject> getProfile = homeFoodService.getUserProfileById(app.getUserModel().getUserId());
        getProfile.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.i(TAG, "request is " + response.isSuccess());
                if (response.isSuccess()) {
                    Profile profile = gson.fromJson(response.body().getAsJsonObject("data"), Profile.class);
                    app.setProfile(profile);
                    userName.setText(profile.getFirstName() + " " + profile.getLastName());
                    String gender = profile.getGender();
                    if (gender.equals("m")) {
                        userMale.setText(R.string.male);
                    } else if (gender.equals("f")) {
                        userMale.setText(R.string.female);
                    }
                    userMail.setText(profile.getEmail());
                    userPhone.setText(profile.getPhoneNumber());
                    userAbout.setText(profile.getAbout());
                    Picasso.with(getBaseContext()).load(profile.getProfileImgUrl())
                            .placeholder(R.drawable.placeholder_person)
                            .into(userImage);
                } else {
                    Toast.makeText(MyProfileActivity.this, R.string.error, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MyProfileActivity.this, R.string.network_error, Toast.LENGTH_LONG).show();
                Log.e(TAG, t.getMessage(), t);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit) {
            if (app.getProfile() != null) {
                Intent intent = new Intent(this, EditProfileActivity.class);
                startActivity(intent);
            }
        } else if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
