package com.yuvalsuede.homefood;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yuvalsuede.homefood.model.UserConfig;
import com.yuvalsuede.homefood.model.UserModel;
import com.yuvalsuede.homefood.rest.HomeFoodService;
import com.yuvalsuede.homefood.rest.RestService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FBLoginActivity extends AppCompatActivity {

    private static final String TAG = FBLoginActivity.class.getName();

    private CallbackManager callbackManager;
    private HomeFoodService homeFoodService;
    private Gson gson = new Gson();
    private App app;
    private ImageButton closeButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fb_connect_activity);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        assert loginButton != null;
        loginButton.setReadPermissions(getResources().getStringArray(R.array.fb_read_permissions));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Log.e(TAG, "success " + loginResult.getAccessToken().getToken());
                        Log.e(TAG, "userid " + loginResult.getAccessToken().getUserId());
                        checkExistUser(loginResult);
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
        homeFoodService = RestService.getService();

        app = (App) getApplication();
        if (app.needRestart()) {
            finish();
            startActivity(new Intent(this, SplashActivity.class));
        }
        closeButton = (ImageButton) findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public String getPhonePrefix() {
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String countryCodeValue = tm.getNetworkCountryIso();
        return Iso2Phone.getPhone(countryCodeValue.toUpperCase());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void checkExistUser(final LoginResult loginResult) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("system_name", "facebook");
        hashMap.put("system_id", loginResult.getAccessToken().getUserId());
        hashMap.put("app_secret", "a231b09dbc4476568642414fe6b1563d");
        hashMap.put("access_token", loginResult.getAccessToken().getToken());
        Call<JsonObject> userBySystem = homeFoodService.getUserBySystem(hashMap);
        userBySystem.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.body().get("status").getAsString().equals("error")) {
                    UserModel userModel = gson.fromJson(response.body().get("data").getAsJsonObject(), UserModel.class);
                    app.setUserModel(userModel);
                    Log.i(TAG, "userbysystem " + userModel.getToken());
                    getUserConfig(userModel);
                } else {

                    Profile.fetchProfileForCurrentAccessToken();
                    Log.i(TAG, "message " + response.message());
                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {
                                    Log.i(TAG, response.toString());

                                    HashMap<String, String> newUserHashMap = new HashMap<>();
                                    try {
                                        newUserHashMap.put("system_id", object.getString("id"));
                                        newUserHashMap.put("system_name", "facebook");
                                        newUserHashMap.put("first_name", object.getString("first_name"));
                                        newUserHashMap.put("last_name", object.getString("last_name"));
                                        newUserHashMap.put("token", "0");
                                        newUserHashMap.put("profile_img_url", "https://graph.facebook.com/" +
                                                object.getString("id")
                                                + "/picture?width=640&height=640");
                                        newUserHashMap.put("country_code", getPhonePrefix());
                                        newUserHashMap.put("email", object.getString("email"));
                                        newUserHashMap.put("gender", object.getString("gender"));
                                        newUserHashMap.put("app_secret", "a231b09dbc4476568642414fe6b1563d");
                                    } catch (JSONException e) {
                                        Log.e(TAG, "jsonex " + e.getMessage(), e);
                                    }
                                    Log.i(TAG, "hash " + newUserHashMap.toString());
                                    Call<JsonObject> createUser = homeFoodService.createUserFromFB(newUserHashMap);
                                    createUser.enqueue(new Callback<JsonObject>() {
                                        @Override
                                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                            HashMap<String, String> hashMap = new HashMap<>();
                                            hashMap.put("system_name", "facebook");
                                            hashMap.put("system_id", loginResult.getAccessToken().getUserId());
                                            hashMap.put("app_secret", "a231b09dbc4476568642414fe6b1563d");
                                            hashMap.put("access_token", loginResult.getAccessToken().getToken());
                                            Call<JsonObject> userBySystem = homeFoodService.getUserBySystem(hashMap);
                                            userBySystem.enqueue(new Callback<JsonObject>() {
                                                @Override
                                                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                                    if (!response.body().get("status").getAsString().equals("error")) {
                                                        UserModel userModel = gson.fromJson(response.body().get("data").getAsJsonObject(), UserModel.class);
                                                        app.setUserModel(userModel);
                                                        Log.i(TAG, "userbysystem " + userModel.getToken());
                                                        getUserConfig(userModel);
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<JsonObject> call, Throwable t) {

                                                }
                                            });
                                        }

                                        @Override
                                        public void onFailure(Call<JsonObject> call, Throwable t) {
                                            Log.e(TAG, "error fb " + t.getMessage(), t);
                                        }
                                    });
                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,first_name,last_name,email,gender");
                    request.setParameters(parameters);
                    request.executeAsync();
                    //}
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "faileduser " + t.getMessage(), t);
            }
        });
    }

    private void getUserConfig(UserModel userModel) {
        final Gson gson = new Gson();
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
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "homeFoodService.getUserConfig eroor", t);
            }
        });
    }

}
