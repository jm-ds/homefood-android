package com.yuvalsuede.homefood;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.yuvalsuede.homefood.model.Category;
import com.yuvalsuede.homefood.model.Cuisine;
import com.yuvalsuede.homefood.model.Currency;
import com.yuvalsuede.homefood.model.UserConfig;
import com.yuvalsuede.homefood.model.UserModel;
import com.yuvalsuede.homefood.rest.HomeFoodService;
import com.yuvalsuede.homefood.rest.RestService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getName();

    private Gson gson;
    private HomeFoodService homeFoodService;
    private App app;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        app = (App) getApplication();
        gson = new Gson();

        homeFoodService = RestService.getService();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkLocationServiceEnabled()) {
            UserModel userModel = app.getUserModel();
            if (userModel != null) {
                getUserConfig(userModel);
            } else {
                getCurrencies();
            }
        }
    }

    private void getCategories() {
        String lang = "en";
        UserConfig userConfig = app.getUserConfig();
        if (userConfig != null) {
            lang = userConfig.getUserLanguage();
        }
        Call<JsonObject> categories = homeFoodService.getCategories(lang);
        categories.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.i(TAG, "categories: " + response.body().toString());
                JsonArray arrayCategory = response.body().getAsJsonArray("data");
                List<Category> list = gson.fromJson(arrayCategory, new TypeToken<List<Category>>() {
                }.getType());
                App.getInstance().setCategoryList(list);
                getCuisines();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "homeFoodService.getCategories error", t);
                Toast.makeText(SplashActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCuisines() {
        String lang = "en";
        UserConfig userConfig = app.getUserConfig();
        if (userConfig != null) {
            lang = userConfig.getUserLanguage();
        }
        Call<JsonObject> cuisines = homeFoodService.getCuisines(lang);
        cuisines.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.i(TAG, "cuisines: " + response.body().toString());
                JsonArray arrayCuisines = response.body().getAsJsonArray("data");
                List<Cuisine> list = gson.fromJson(arrayCuisines, new TypeToken<List<Cuisine>>() {
                }.getType());
                App.getInstance().setCuisineList(list);
                startActivity(new Intent(getBaseContext(), MainActivity.class));
                finish();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "homeFoodService.getCuisines eroor", t);
                Toast.makeText(SplashActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCurrencies() {
        Call<JsonObject> getCurrencies = homeFoodService.getCurrencies();
        getCurrencies.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.i(TAG, "currency: " + response.body().toString());
                JsonArray array = response.body().getAsJsonArray("data");
                List<Currency> currencies = new ArrayList<>();
                for (int i = 0; i < array.size(); i++) {
                    JsonObject object = array.get(i).getAsJsonObject();
                    Currency currency = gson.fromJson(object, Currency.class);
                    currencies.add(currency);
                }
                app.setCurrencies(currencies);
                getCategories();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "homeFoodService.getCurrencies eroor", t);
                Toast.makeText(SplashActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getUserConfig(UserModel userModel) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("user_id", userModel.getUserId());
        hashMap.put("access_token", userModel.getToken());
        Call<JsonObject> getUserConfig = homeFoodService.getUserConfig(hashMap);
        getUserConfig.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.i(TAG, "userconfig: " + response.body().toString());
                UserConfig userConfig = gson.fromJson(response.body().getAsJsonObject("data"), UserConfig.class);
                app.setUserConfig(userConfig);
                getCurrencies();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "homeFoodService.getUserConfig eroor", t);
                Toast.makeText(SplashActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public boolean checkLocationServiceEnabled() {
        if (!isLocationEnabled(this)) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(R.string.gps_network_not_enabled_title);
            dialog.setCancelable(false);
            dialog.setMessage(R.string.gps_network_not_enabled);
            dialog.setPositiveButton(R.string.open_location_settings, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                    paramDialogInterface.dismiss();
                }
            });
            dialog.show();
            return false;
        } else {
            return true;
        }
    }

    public boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }
}
