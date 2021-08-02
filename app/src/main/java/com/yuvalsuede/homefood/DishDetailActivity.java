package com.yuvalsuede.homefood;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.yuvalsuede.homefood.model.Currency;
import com.yuvalsuede.homefood.model.Favorite;
import com.yuvalsuede.homefood.model.FavouriteDishId;
import com.yuvalsuede.homefood.model.Profile;
import com.yuvalsuede.homefood.model.UserConfig;
import com.yuvalsuede.homefood.model.UserModel;
import com.yuvalsuede.homefood.model.dishinfo.DishInfo;
import com.yuvalsuede.homefood.model.dishinfo.DishInfo_;
import com.yuvalsuede.homefood.model.dishinfo.DishParams;
import com.yuvalsuede.homefood.picasso.CircleTransform;
import com.yuvalsuede.homefood.rest.HomeFoodService;
import com.yuvalsuede.homefood.rest.RestService;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DishDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = DishDetailActivity.class.getName();
    private GoogleMap mMap;
    private App app;

    int mDish;
    DishInfo mDishInfo;

    TextView dishDetailsTitle, dishDetailsUserName, dishDetailsLikes,
            dishDetailsAbout, dishDetailsPrice;
    TextView dishNumOfPeople, dishNumOfPieces, dishCold, dishWeight, dishDelivery,
            dishGlutenFree, dishKosher, dishContainsMilk, dishVegetarian, dishVegan,
            dishSugarFree, dishMainCourse, dishSpicy, dishGlatKosher;
    ImageView dishDetailImage, dishDetailsUserIcon, dishDetailsLikeButton;

    Button orderNow, dishDetailsCallMe;

    Gson gson = new Gson();
    HomeFoodService homeFoodService;
    UserModel userModel = null;
    List<FavouriteDishId> favouriteDishIds = new ArrayList<>();
    boolean isLiked = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dish_details);
        app = (App) getApplication();
        if (app.needRestart()) {
            finish();
            startActivity(new Intent(this, SplashActivity.class));
        }
        initializeViews();

        mDish = getIntent().getIntExtra("dish_id", 0);
        homeFoodService = RestService.getService();

        Call<JsonObject> getDishInfo = homeFoodService.getDishInfo(mDish + "");

        getDishInfo.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.i(TAG, "dishinfo: " + response.body().toString());
                mDishInfo = gson.fromJson(response.body().getAsJsonObject("data"), DishInfo.class);
                setDataForViews(mDishInfo);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, t.getMessage(), t);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        userModel = app.getUserModel();
        if (userModel != null) {
            getUserLikes();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dish_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void getUserLikes() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("access_token", userModel.getToken());
        hashMap.put("user_id", userModel.getUserId());
        Call<Favorite> getDishesLikes = homeFoodService.getUserLikes(hashMap);
        getDishesLikes.enqueue(new Callback<Favorite>() {
            @Override
            public void onResponse(Call<Favorite> call, Response<Favorite> response) {
                Favorite dishIds = response.body();
                favouriteDishIds = dishIds.getData();
                isLiked = isLiked();
            }

            @Override
            public void onFailure(Call<Favorite> call, Throwable t) {

            }
        });
    }

    private boolean isLiked() {
        boolean liked = false;
        if (favouriteDishIds != null)
            for (int i = 0; i < favouriteDishIds.size(); i++) {
                if (favouriteDishIds.get(i).getDishId() == mDish) {
                    liked = true;
                    break;
                }
            }
        if (liked) {
            dishDetailsLikeButton.setImageResource(R.drawable.icon_love_full);
            return true;
        } else {
            dishDetailsLikeButton.setImageResource(R.drawable.icon_love_none);
            return false;
        }
    }

    public void setDataForViews(DishInfo mDishInfo) {
        DishParams dishParams = mDishInfo.getDishParams();
        if (dishParams.getNumOfPeople() != null)
            dishNumOfPeople.setText(String.format(Locale.getDefault(), "%d", dishParams.getNumOfPeople()));

        if (dishParams.getNumOfPieces() != null)
            dishNumOfPieces.setText(String.format(Locale.getDefault(), "%d", dishParams.getNumOfPieces()));

        if (dishParams.getColdWarm() == 1) {
            dishCold.setText(R.string.warm_dish);
        } else {
            dishCold.setText(R.string.cold_dish);
        }
        if (dishParams.getWeight() > 0) {
            dishWeight.setText(dishParams.getWeight() + getString(R.string.kg));
            UserConfig userConfig = app.getUserConfig();
            if (userConfig != null) {
                if (userConfig.getUserWeightUnit() == 1) {
                    dishWeight.setText(dishParams.getWeight() + getString(R.string.lb));
                }
            }
        } else {
            dishWeight.setVisibility(View.GONE);
            findViewById(R.id.dish_details_weight_title).setVisibility(View.GONE);
            findViewById(R.id.dish_details_more_5st_line).setVisibility(View.GONE);
        }
        if (dishParams.getDelivery()) {
            dishDelivery.setVisibility(View.VISIBLE);
            findViewById(R.id.dish_details_more_6st_line).setVisibility(View.VISIBLE);
        } else {
            dishDelivery.setVisibility(View.GONE);
            findViewById(R.id.dish_details_more_6st_line).setVisibility(View.GONE);
        }

        if (dishParams.getGlutenFree()) {
            findViewById(R.id.dish_details_more_7st_line).setVisibility(View.VISIBLE);
            dishGlutenFree.setVisibility(View.VISIBLE);
        } else {
            dishGlutenFree.setVisibility(View.GONE);
            findViewById(R.id.dish_details_more_7st_line).setVisibility(View.GONE);
        }

        if (dishParams.getKosher()) {
            findViewById(R.id.dish_details_more_8st_line).setVisibility(View.VISIBLE);
            dishKosher.setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.dish_details_more_8st_line).setVisibility(View.GONE);
            dishKosher.setVisibility(View.GONE);
        }

        if (dishParams.getContainesMilk()) {
            findViewById(R.id.dish_details_more_9st_line).setVisibility(View.VISIBLE);
            dishContainsMilk.setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.dish_details_more_9st_line).setVisibility(View.GONE);
            dishContainsMilk.setVisibility(View.GONE);
        }

        if (dishParams.getVegeterian()) {
            findViewById(R.id.dish_details_more_10st_line).setVisibility(View.VISIBLE);
            dishVegetarian.setVisibility(View.VISIBLE);
        } else {
            dishVegetarian.setVisibility(View.GONE);
            findViewById(R.id.dish_details_more_10st_line).setVisibility(View.GONE);
        }

        if (dishParams.getVegan()) {
            findViewById(R.id.dish_details_more_11st_line).setVisibility(View.VISIBLE);
            dishVegan.setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.dish_details_more_11st_line).setVisibility(View.GONE);
            dishVegan.setVisibility(View.GONE);
        }

        if (dishParams.getSugarFree()) {
            findViewById(R.id.dish_details_more_12st_line).setVisibility(View.VISIBLE);
            dishSugarFree.setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.dish_details_more_12st_line).setVisibility(View.GONE);
            dishSugarFree.setVisibility(View.GONE);
        }

        if (dishParams.getMainCourse()) {
            findViewById(R.id.dish_details_more_14st_line).setVisibility(View.VISIBLE);
            dishMainCourse.setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.dish_details_more_14st_line).setVisibility(View.GONE);
            dishMainCourse.setVisibility(View.GONE);
        }

        if (dishParams.getSpicyLevel() == 1) {
            dishSpicy.setText(R.string.very_spicy);
        } else if (dishParams.getSpicyLevel() == -1) {
            dishSpicy.setVisibility(View.GONE);
            findViewById(R.id.dish_details_more_13st_line).setVisibility(View.GONE);
        } else {
            dishSpicy.setText(R.string.spicy);
        }

        if (dishParams.getGlat()) {
            findViewById(R.id.dish_details_more_15st_line).setVisibility(View.VISIBLE);
            dishGlatKosher.setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.dish_details_more_15st_line).setVisibility(View.GONE);
            dishGlatKosher.setVisibility(View.GONE);
        }

        DishInfo_ dishInfo_ = mDishInfo.getDishInfo();

        if (dishInfo_.getTitle() != null) {
            dishDetailsTitle.setText(dishInfo_.getTitle());
            if (getSupportActionBar() != null) getSupportActionBar().setTitle(dishInfo_.getTitle());
        }

        if (dishInfo_.getLikes() != null && dishInfo_.getLikes() == 1) {
            dishDetailsLikes.setText(dishInfo_.getLikes() + getString(R.string.details_like));
        } else if (dishInfo_.getLikes() == null || dishInfo_.getLikes() == 0) {
            dishDetailsLikes.setText(0 + getString(R.string.details_likes));
        } else if (dishInfo_.getLikes() != null) {
            dishDetailsLikes.setText(dishInfo_.getLikes() + getString(R.string.details_likes));
        }

        if (dishInfo_.getDescription() != null) {
            dishDetailsAbout.setText(dishInfo_.getDescription());
        } else {
            dishDetailsAbout.setText("");
        }

        if (dishInfo_.getPrice() != null) {
            setPrice(dishDetailsPrice, dishInfo_);
        }
        dishDetailImage.setImageResource(R.drawable.placeholder_mana);
        if (dishInfo_.getMainPhoto() != null) {
            new DownloadImageTask(dishDetailImage).execute(Cloudinary.getImageLinkFromCloudinary(dishInfo_.getMainPhoto()));
        }

        getProfile(dishInfo_.getUserId().toString());

        LatLng latLng = new LatLng(dishInfo_.getLat(), dishInfo_.getLng());
        mMap.addMarker(new MarkerOptions().position(latLng).title(dishInfo_.getTitle()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
    }

    public void setPrice(TextView textView, DishInfo_ mDish) {
        String dishCurS = mDish.getCurrency();
        Currency dishCur = app.getCurrency(dishCurS);
        String symbol;
        if (dishCur == null) {
            symbol = mDish.getCurrency();
        } else {
            symbol = dishCur.getSymbol();
        }
        textView.setText(symbol + "" + mDish.getPrice());
        UserConfig userConfig = app.getUserConfig();
        if (userConfig != null) {
            Currency userCurrency = app.getCurrency(userConfig.getUserCurrency());
            if (userCurrency != null && dishCur != null && !dishCur.getCode().equals(userCurrency.getCode())) {

                double price = Double.parseDouble(mDish.getBaseCurrency());
                double currentUsdExchangeRate = Double.parseDouble(userCurrency.getCurrentUsdExchangeRate());
                double real = price * currentUsdExchangeRate;
                DecimalFormat formatter = new DecimalFormat("#0.0");
                String s = formatter.format(real);
                textView.setText(userCurrency.getSymbol() + "" + s);
            }
        }
    }

    public void getProfile(String id) {
        Call<JsonObject> getProfile = homeFoodService.getUserProfileById(id);
        getProfile.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.i(TAG, "profile " + response.body().toString());
                final Profile profile = gson.fromJson(response.body().getAsJsonObject("data"), Profile.class);
                if (profile.getFirstName() != null || profile.getLastName() != null) {
                    dishDetailsUserName.setText(profile.getFirstName() + " " + profile.getLastName());
                }
                if (profile.getProfileImgUrl() != null) {
                    Picasso.with(getBaseContext())
                            .load(profile.getProfileImgUrl())
                            .resize(300, 300)
                            .placeholder(R.drawable.placeholder_person)
                            .transform(new CircleTransform())
                            .into(dishDetailsUserIcon);
                }

                dishDetailsCallMe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + profile.getPhoneNumber()));
                        startActivity(intent);
                    }
                });

                orderNow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + profile.getPhoneNumber()));
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    public void initializeViews() {
        dishNumOfPeople = (TextView) findViewById(R.id.dish_details_num_of_people_text);
        dishNumOfPieces = (TextView) findViewById(R.id.dish_details_num_of_pieces_text);
        dishCold = (TextView) findViewById(R.id.dish_details_cold_dish_title);
        dishWeight = (TextView) findViewById(R.id.dish_details_weight_text);
        dishDelivery = (TextView) findViewById(R.id.dish_details_delivery_title);
        dishGlutenFree = (TextView) findViewById(R.id.dish_details_gluten_free_title);
        dishKosher = (TextView) findViewById(R.id.dish_details_kosher_title);
        dishContainsMilk = (TextView) findViewById(R.id.dish_details_contains_milk_title);
        dishVegetarian = (TextView) findViewById(R.id.dish_details_vegetarian_title);
        dishVegan = (TextView) findViewById(R.id.dish_details_vegan_title);
        dishSugarFree = (TextView) findViewById(R.id.dish_details_sugar_free_title);
        dishMainCourse = (TextView) findViewById(R.id.dish_details_main_course_title);
        dishSpicy = (TextView) findViewById(R.id.dish_details_spicy_title);
        dishGlatKosher = (TextView) findViewById(R.id.dish_details_glat_kosher_title);


        dishDetailsLikeButton = (ImageView) findViewById(R.id.dish_details_like);
        dishDetailsLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeOrUnLike();
            }
        });

        dishDetailsTitle = (TextView) findViewById(R.id.dish_details_title);
        dishDetailsUserName = (TextView) findViewById(R.id.dish_details_user_name);
        dishDetailsLikes = (TextView) findViewById(R.id.dish_details_likes_text);
        dishDetailsAbout = (TextView) findViewById(R.id.dish_details_about_text);
        dishDetailsPrice = (TextView) findViewById(R.id.dish_details_price);
        dishDetailsCallMe = (Button) findViewById(R.id.dish_details_callme);

        dishDetailImage = (ImageView) findViewById(R.id.dish_details_image);
        dishDetailsUserIcon = (ImageView) findViewById(R.id.dish_details_icon_user);
        dishDetailsUserIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DishDetailActivity.this, UserDetailsActivity.class);
                intent.putExtra("user_id", mDishInfo.getDishInfo().getUserId().toString());
                startActivity(intent);
            }
        });

        orderNow = (Button) findViewById(R.id.dish_details_ordernow);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.dish_details_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String link = "http://homefood.mobi/dish/" + mDish;
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, link);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getString(R.string.share)));
                break;
            case R.id.copyLink:

                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(link, link);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this, R.string.link_copied, Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void likeOrUnLike() {
        userModel = app.getUserModel();
        if (userModel != null) {
            HashMap<String, String> data = new HashMap<>();
            data.put("access_token", userModel.getToken());
            data.put("dish_id", mDish + "");
            data.put("from_user_id", userModel.getUserId());
            Call<JsonObject> likeOrNotLike;
            if (!isLiked) {
                likeOrNotLike = homeFoodService.setLike(data);
                isLiked = true;
            } else {
                likeOrNotLike = homeFoodService.setUnlike(data);
                isLiked = false;
            }
            dishDetailsLikeButton.setVisibility(View.GONE);
            likeOrNotLike.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.i(TAG, isLiked + "" + response.body().toString());
                    if (!isLiked) {
                        dishDetailsLikeButton.setImageResource(R.drawable.icon_love_none);
                        int positionToRemove = -1;
                        for (int i = 0; i < favouriteDishIds.size(); i++) {
                            if (favouriteDishIds.get(i).getDishId().equals((mDish))) {
                                positionToRemove = i;
                                break;
                            }
                        }
                        if (positionToRemove != -1) {
                            favouriteDishIds.remove(positionToRemove);
                        }
                        mDishInfo.getDishInfo().setLikes(mDishInfo.getDishInfo().getLikes() - 1);
                        dishDetailsLikes.setText(mDishInfo.getDishInfo().getLikes() + getString(R.string.details_like));
                    } else {
                        dishDetailsLikeButton.setImageResource(R.drawable.icon_love_full);
                        if (favouriteDishIds != null) {
                            favouriteDishIds.add(new FavouriteDishId(mDish));
                        } else {
                            favouriteDishIds = new ArrayList<>();
                            favouriteDishIds.add(new FavouriteDishId(mDish));
                        }
                        if (mDishInfo.getDishInfo().getLikes() == null) {
                            mDishInfo.getDishInfo().setLikes(0);
                        }
                        mDishInfo.getDishInfo().setLikes(mDishInfo.getDishInfo().getLikes() + 1);
                        dishDetailsLikes.setText(mDishInfo.getDishInfo().getLikes() + getString(R.string.details_like));
                    }
                    dishDetailsLikeButton.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    dishDetailsLikeButton.setVisibility(View.VISIBLE);
                }
            });
        } else {
            startActivityForResult(new Intent(this, FBLoginActivity.class), MainActivity.CODE_FOR_FB_ACTIVITY);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MainActivity.CODE_FOR_FB_ACTIVITY)
            if (resultCode == RESULT_OK) {
                SharedPreferences preferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                if (!preferences.getString("user", "").equals("")) {
                    userModel = gson.fromJson(preferences.getString("user", ""), UserModel.class);
                    getUserLikes();
                }
            }
    }
}
