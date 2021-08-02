package com.yuvalsuede.homefood;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.yuvalsuede.homefood.model.DishModel;
import com.yuvalsuede.homefood.model.Profile;
import com.yuvalsuede.homefood.rest.HomeFoodService;
import com.yuvalsuede.homefood.rest.RestService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDetailsActivity extends AppCompatActivity {
    private static final String TAG = UserDetailsActivity.class.getName();
    TextView userName, userMale, user_dish_count, userMail, userPhone, user_dishes_count_text;
    ImageView userImage;
    LinearLayout container_dishes;
    Gson gson = new Gson();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App app = (App) getApplication();
        if (app.needRestart()) {
            finish();
            startActivity(new Intent(this, SplashActivity.class));
        }
        setContentView(R.layout.activity_user_details);
        initializeViews();
        String user_id = getIntent().getStringExtra("user_id");

        HomeFoodService homeFoodService = RestService.getService();
        Call<JsonObject> getProfile = homeFoodService.getUserProfileById(user_id);
        getProfile.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Profile profile = gson.fromJson(response.body().get("data").getAsJsonObject(), Profile.class);
                Log.i(TAG, "profile " + profile.getFirstName());
                userName.setText(profile.getFirstName() + " " + profile.getLastName());
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(profile.getFirstName() + " " + profile.getLastName());
                }
                String gender = profile.getGender();
                if (gender.equals("m")) {
                    userMale.setText(R.string.male);
                } else if (gender.equals("f")) {
                    userMale.setText(R.string.female);
                }
                userMail.setText(profile.getEmail());
                userPhone.setText(profile.getPhoneNumber());
                Picasso.with(getBaseContext()).load(profile.getProfileImgUrl())
                        .placeholder(R.drawable.placeholder_person)
                        .into(userImage);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, t.getMessage(), t);
            }
        });
        Call<JsonObject> getUserDishes = homeFoodService.getUserDishesWithUserId(user_id);
        getUserDishes.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dishModels = gson.fromJson(response.body().getAsJsonArray("data"), new TypeToken<List<DishModel>>() {
                }.getType());
                setDishCount(dishModels.size());
                for (int i = 0; i < dishModels.size(); i++) {
                    inflateDish(dishModels.get(i), i);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    List<DishModel> dishModels = new ArrayList<>();

    public void inflateDish(final DishModel dishModel, int position) {
        final View dishView = getLayoutInflater().inflate(R.layout.activity_user_details_dish, null);
        ImageView dishImage = (ImageView) dishView.findViewById(R.id.user_details_dish_image);
        TextView dishTitle = (TextView) dishView.findViewById(R.id.user_details_dish_title);
        TextView dishDescription = (TextView) dishView.findViewById(R.id.user_details_dish_details_text);
        container_dishes.addView(dishView);
        Picasso.with(getBaseContext()).load(Cloudinary.getImageLinkFromCloudinary(dishModel.getMainPhoto()))
                .resize(600, 400)
                .centerCrop()
                .into(dishImage);
        dishTitle.setText(dishModel.getTitle());
        dishDescription.setText(dishModel.getDescription());
        dishView.setTag(position);
        dishView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), DishDetailActivity.class);
                intent.putExtra("dish_id", dishModels.get(Integer.parseInt(dishView.getTag().toString())).getDishId());
                startActivity(intent);
            }
        });
    }

    public void setDishCount(Integer count) {
        user_dish_count.setText(count.toString());
        user_dishes_count_text.setText(count + " " + getString(R.string.dishes));
    }

    public void initializeViews() {
        userName = (TextView) findViewById(R.id.user_details_name);
        userMale = (TextView) findViewById(R.id.user_details_male);
        user_dish_count = (TextView) findViewById(R.id.user_details_dish_count);
        userMail = (TextView) findViewById(R.id.user_details_email_text);
        userPhone = (TextView) findViewById(R.id.user_details_phone_text);
        userImage = (ImageView) findViewById(R.id.user_details_user_image);
        container_dishes = (LinearLayout) findViewById(R.id.user_details_linear_layout_for_dishes);
        user_dishes_count_text = (TextView) findViewById(R.id.user_details_dish_count_with_text);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
