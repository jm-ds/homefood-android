package com.yuvalsuede.homefood;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yuvalsuede.homefood.model.Category;
import com.yuvalsuede.homefood.model.CategoryModel;
import com.yuvalsuede.homefood.model.Cuisine;
import com.yuvalsuede.homefood.model.CuisineModel;
import com.yuvalsuede.homefood.model.Currency;
import com.yuvalsuede.homefood.model.Profile;
import com.yuvalsuede.homefood.model.UserConfig;
import com.yuvalsuede.homefood.model.UserModel;
import com.yuvalsuede.homefood.rest.HomeFoodService;
import com.yuvalsuede.homefood.rest.RestService;

import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@ReportsCrashes(mailTo = "adegtiarev@gmail.com",
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.error_toast)
public class App extends Application implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = App.class.getName();
    private static App instance;
    private SharedPreferences sharedPref;
    private List<Category> categoryList;
    private List<Cuisine> cuisineList;
    private Gson gson;
    private List<Currency> currencies;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Profile profile;

    public static App getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // The following line triggers the initialization of ACRA
//        ACRA.init(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(this);
        AppEventsLogger.activateApp(this);
        instance = this;
        gson = new Gson();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
    }

    private void startLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(500);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
    }

    @Override
    public void onTerminate() {
        Log.i(TAG, "onTerminate");
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
        mGoogleApiClient.disconnect();
        super.onTerminate();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public List<Cuisine> getCuisineList() {
        return cuisineList;
    }

    public void setCuisineList(List<Cuisine> cuisineList) {
        this.cuisineList = cuisineList;
    }

    public Category getCategoryById(int id) {
        for (int i = 0; i < categoryList.size(); i++) {
            if (categoryList.get(i).getId().equals(id)) {
                return categoryList.get(i);
            }
        }
        return null;
    }

    public Cuisine getCuisineById(int id) {
        for (int i = 0; i < cuisineList.size(); i++) {
            if (cuisineList.get(i).getId().equals(id)) {
                return cuisineList.get(i);
            }
        }
        return null;
    }

    public Category getCategoryByName(String name) {
        for (int i = 0; i < categoryList.size(); i++) {
            if (categoryList.get(i).getCategoryName().equals(name)) {
                return categoryList.get(i);
            }
        }
        return null;
    }

    public Cuisine getCuisineByName(String name) {
        for (int i = 0; i < cuisineList.size(); i++) {
            if (cuisineList.get(i).getCuisineName().equals(name)) {
                return cuisineList.get(i);
            }
        }
        return null;
    }

    public Cuisine getCuisineType(JsonObject cuisineObject) {

        CuisineModel cuisineModel = gson.fromJson(cuisineObject.get("content"), CuisineModel.class);
        if (cuisineModel.getCuisineId() != null) {
            Cuisine cuisine = getCuisineById(cuisineModel.getCuisineId());
            cuisine.count = Integer.parseInt(cuisineModel.getCount());
            return cuisine;
        }
        return null;
    }

    public Category getCategoryType(JsonObject categoryObject) {
        CategoryModel categoryModel = gson.fromJson(categoryObject.get("content"), CategoryModel.class);
        Category category = getCategoryById(categoryModel.getCategoryId());
        category.setCount(categoryModel.getCount());
        return category;
    }

    public Location getLocation() {
        if (mLastLocation != null) {
            return mLastLocation;
        }
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(false);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    public UserModel getUserModel() {
        String json = sharedPref.getString("userModel", null);
        if (json != null) {
            return gson.fromJson(json, UserModel.class);
        }
        return null;
    }

    public void setUserModel(UserModel userModel) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("userModel", gson.toJson(userModel));
        editor.apply();
    }

    public UserConfig getUserConfig() {
        UserConfig userConfig = new UserConfig();
        userConfig.setUserCurrency(sharedPref.getString("currency", "USD"));
        userConfig.setUserDistanceUnit(Integer.parseInt(sharedPref.getString("distance", "0")));
        userConfig.setUserWeightUnit(Integer.parseInt(sharedPref.getString("weight", "0")));
        userConfig.setUserLanguage(sharedPref.getString("lang", "en"));
        userConfig.setAllowPushNotification(sharedPref.getBoolean("push", false));
        return userConfig;
    }

    public void setUserConfig(UserConfig userConfig) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor
                .putString("lang", userConfig.getUserLanguage() != null && (userConfig.getUserLanguage().equals("en") || userConfig.getUserLanguage().equals("he")) ? userConfig.getUserLanguage() : "en")
                .putString("distance", userConfig.getUserDistanceUnit() != null ? "" + userConfig.getUserDistanceUnit() : "0")
                .putString("currency", userConfig.getUserCurrency() == null ? "USD" : userConfig.getUserCurrency())
                .putString("weight", (userConfig.getUserWeightUnit() != null) ? "" + userConfig.getUserWeightUnit() : "0")
                .putBoolean("push", userConfig.getAllowPushNotification())
                .apply();
        if (userConfig.getUserLanguage() == null || userConfig.getUserDistanceUnit() == null || userConfig.getUserCurrency() == null || userConfig.getUserWeightUnit() == null) {
            saveConfig();
        }
    }

    public Currency getCurrency(String name) {
        for (Currency currency : getCurrencies()) {
            if (currency.getCode().equals(name)) {
                return currency;
            }
        }
        return null;
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
    }

    public void saveConfig() {
        if (getUserModel() == null) {
            return;
        }
        HomeFoodService homeFoodService = RestService.getService();
        Call<JsonObject> setConfig = homeFoodService.setUserConfig(generateHashMapSettings());
        setConfig.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.i(TAG, "update setting: " + response.body().toString());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "setUserConfig", t);
            }
        });
    }

    private HashMap<String, String> generateHashMapSettings() {
        SharedPreferences preferences = sharedPref;
        HashMap<String, String> settingsHashMap = new HashMap<>();
        settingsHashMap.put("user_language", preferences.getString("lang", "en"));
        settingsHashMap.put("user_distance_unit", preferences.getString("distance", "0"));
        settingsHashMap.put("user_currency", preferences.getString("currency", "USD"));
        settingsHashMap.put("user_weight_unit", preferences.getString("weight", "0"));
        settingsHashMap.put("allow_push_notification", preferences.getBoolean("push", false) + "");
        UserModel user = getUserModel();
        settingsHashMap.put("access_token", user.getToken());
        settingsHashMap.put("user_id", user.getUserId());
        return settingsHashMap;
    }

    public boolean needRestart() {
        return categoryList == null || cuisineList == null || currencies == null;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
