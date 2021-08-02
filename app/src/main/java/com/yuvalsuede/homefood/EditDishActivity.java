package com.yuvalsuede.homefood;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.gun0912.tedpicker.Config;
import com.gun0912.tedpicker.ImagePickerActivity;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;
import com.yuvalsuede.homefood.fragment.SelectCuisineDialogFragment;
import com.yuvalsuede.homefood.model.Category;
import com.yuvalsuede.homefood.model.Cuisine;
import com.yuvalsuede.homefood.model.Currency;
import com.yuvalsuede.homefood.model.DishCategory;
import com.yuvalsuede.homefood.model.EditedCategory;
import com.yuvalsuede.homefood.model.UserModel;
import com.yuvalsuede.homefood.model.dishinfo.DishInfo;
import com.yuvalsuede.homefood.rest.HomeFoodService;
import com.yuvalsuede.homefood.rest.RestService;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditDishActivity extends AppCompatActivity {
    private static final String TAG = EditDishActivity.class.getName();
    public final static int PICK_IMAGE_CODE = 348;
    public EditText titleEdit, descriptionEdit, editPrice, editPhone;
    public TextView textCuisine, textCategories;
    public RelativeLayout imageDish;
    public ImageView imageDishMain;

    public List<Category> categoryList;
    public List<Cuisine> cuisineList;
    private Gson gson = new Gson();

    public String cuisineId = null;
    public UserModel userModel = null;
    public HomeFoodService homeFoodService;
    public DishInfo mDishInfo = null;
    public File fileImage = null;
    public List<DishCategory> dishCategories = null;
    public ProgressDialog progressDialog;
    private App app;
    private TextView currencySymbol;
    private MenuItem itemSave;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dish);
        app = (App) getApplication();
        if (app.needRestart()) {
            finish();
            startActivity(new Intent(this, SplashActivity.class));
        }
        getSupportActionBar().setTitle(R.string.edit_dish);
        App app = (App) getApplication();
        categoryList = app.getCategoryList();
        cuisineList = app.getCuisineList();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.waiting));
        userModel = app.getUserModel();

        homeFoodService = RestService.getService();

        Call<JsonObject> getDishInfo = homeFoodService.getDishInfo(getIntent().getExtras().getString("dish_id"));

        titleEdit = (EditText) findViewById(R.id.editTextTitle);
        descriptionEdit = (EditText) findViewById(R.id.editTextDescription);
        editPrice = (EditText) findViewById(R.id.editTextPrice);
        editPhone = (EditText) findViewById(R.id.editTextPhone);
        textCuisine = (TextView) findViewById(R.id.edit_cuisine);
        textCuisine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectCuisineDialogFragment selecCuisineDialogFragment = SelectCuisineDialogFragment.newInstance(0);
                selecCuisineDialogFragment.show(getFragmentManager(), "cuisine");
            }
        });
        textCategories = (TextView) findViewById(R.id.edit_category);
        textCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectCuisineDialogFragment selecCuisineDialogFragment = SelectCuisineDialogFragment.newInstance(1);
                selecCuisineDialogFragment.show(getFragmentManager(), "categories");
            }
        });
        imageDishMain = (ImageView) findViewById(R.id.edit_dish_image);

        getDishInfo.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("message", response.message());
                Log.e("dishinfo", response.body().toString());
                mDishInfo = gson.fromJson(response.body().getAsJsonObject("data"), DishInfo.class);
                setUpData();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
        Call<JsonObject> getDishCategories = homeFoodService.getDishCategories(getIntent().getExtras().getString("dish_id"));
        getDishCategories.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("categories", response.body().toString());
                dishCategories = gson.fromJson(response.body().getAsJsonArray("data"),
                        new TypeToken<List<DishCategory>>() {
                        }.getType());
                if (dishCategories != null) {
                    setUpCategory();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

        imageDish = (RelativeLayout) findViewById(R.id.edit_relative_image);
        imageDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Config config = new Config();
                config.setSelectionMin(1);
                config.setSelectionLimit(1);
                ImagePickerActivity.setConfig(config);
                Intent intent = new Intent(getBaseContext(), ImagePickerActivity.class);
                startActivityForResult(intent, PICK_IMAGE_CODE);
            }
        });

        currencySymbol = (TextView) findViewById(R.id.currencySimbol);
    }

    public void setUpData() {
        if (mDishInfo != null) {
            Picasso.with(this).load(Cloudinary.getImageLinkFromCloudinary(mDishInfo.getDishInfo().getMainPhoto()))
                    .resize(600, 400)
                    .centerCrop()
                    .placeholder(R.drawable.placeholder_mana)
                    .into(imageDishMain);

            if (mDishInfo.getDishInfo().getTitle() != null)
                titleEdit.setText(mDishInfo.getDishInfo().getTitle());
            if (mDishInfo.getDishInfo().getDescription() != null)
                descriptionEdit.setText(mDishInfo.getDishInfo().getDescription());
            if (mDishInfo.getDishInfo().getPrice() != null)
                editPrice.setText(mDishInfo.getDishInfo().getPrice());
            if (userModel.getPhoneNumber() != null)
                editPhone.setText(userModel.getPhoneNumber().toString());
            if (mDishInfo.getDishInfo().getCuisineId() != null) {
                textCuisine.setText(getString(R.string.select_cuisine_text) + app.getCuisineById(mDishInfo.getDishInfo().getCuisineId()).getCuisineName());
                cuisineId = Integer.toString(mDishInfo.getDishInfo().getCuisineId());
            }
            Currency currency = app.getCurrency(app.getUserConfig().getUserCurrency());
            currencySymbol.setText(currency.getSymbol());
        }
    }

    public void setUpCategory() {
        String s = getString(R.string.select_categories_text);
        for (DishCategory dishCategory : dishCategories) {
            s += app.getCategoryById(dishCategory.getCategoryId()).getCategoryName() + " ";
        }
        textCategories.setText(s);
//        categoryId = dishCategories.get(0).getCategoryId().toString();
    }

    public void saveDish() throws JSONException {
        HashMap<String, String> updatedDishInfo = new HashMap<>();
        updatedDishInfo.put("access_token", userModel.getToken());
        updatedDishInfo.put("user_id", userModel.getUserId());
        updatedDishInfo.put("dish_id", mDishInfo.getDishInfo().getDishId().toString());
        updatedDishInfo.put("price", editPrice.getText().toString());
        updatedDishInfo.put("title", titleEdit.getText().toString());
        updatedDishInfo.put("description", descriptionEdit.getText().toString());
        updatedDishInfo.put("currency", app.getUserConfig().getUserCurrency());
        updatedDishInfo.put("phone_number", editPhone.getText().toString());
        updatedDishInfo.put("cuisine_id", cuisineId);
        Call<JsonObject> updateDish = homeFoodService.updateDish(updatedDishInfo);
        progressDialog.show();
        updateDish.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.i(TAG, "dish saved " + response.body().toString());
                List<String> categories = new ArrayList<>();
                for (DishCategory dishCategory : dishCategories) {
                    categories.add(Integer.toString(dishCategory.getCategoryId()));
                }

                EditedCategory editedCategory = new EditedCategory();
                editedCategory.setUserId(userModel.getUserId());
                editedCategory.setDishId(mDishInfo.getDishInfo().getDishId().toString());
                editedCategory.setAccessToken(userModel.getToken());
                editedCategory.setCategories(categories);

                Call<JsonObject> updateDishCategories = homeFoodService.updateDishCategories(editedCategory);
                updateDishCategories.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        Log.i(TAG, "upd cat: " + response.body().toString());
                        new UploadPhotoTask().execute();

                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.e(TAG, t.getMessage(), t);
                        progressDialog.dismiss();
                    }
                });
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, t.getMessage(), t);
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        itemSave = menu.add("Save");
        itemSave.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == itemSave.getItemId()) {
            Log.e("save", "saving");
            try {
                saveDish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_CODE) {
            if (resultCode == RESULT_OK) {
                ArrayList<Uri> image_uris = data.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
                File requestFile = new File(image_uris.get(0).getEncodedPath());
                fileImage = new File(getExternalFilesDir("image"), Calendar.getInstance().getTimeInMillis() + ".jpg");
                Crop.of(Uri.fromFile(requestFile), Uri.fromFile(fileImage)).withAspect(4, 3).start(this);
            }
        }

        if (requestCode == Crop.REQUEST_CROP) {
            Picasso.with(this).load(fileImage).resize(600, 400).centerCrop().into(imageDishMain);
            //changeFragment(PickCuisineAndCategoryFragment.newInstance(0));
        }
    }

    private class UploadPhotoTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*if (progressDialog!=null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            progressDialog.show();*/
        }

        @Override
        protected String doInBackground(String... params) {
            String photo_id = null;
            if (fileImage != null) {
                try {
                    photo_id = Cloudinary.uploadImageAndGetId(fileImage);
                    Log.i(TAG, "PHOTO_ID: " + photo_id);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
            return photo_id;
        }

        @Override
        protected void onPostExecute(String photoId) {
            super.onPostExecute(photoId);
            /*if (progressDialog!=null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }*/
            if (photoId != null) {
                HashMap<String, String> photoHashMap = new HashMap<>();
                photoHashMap.put("access_token", userModel.getToken());
                photoHashMap.put("user_id", userModel.getUserId());
                photoHashMap.put("main_photo", photoId);
                photoHashMap.put("dish_id", mDishInfo.getDishInfo().getDishId().toString());
                Call<JsonObject> updatePhoto = homeFoodService.updateDishPhoto(photoHashMap);
                updatePhoto.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        Log.e(TAG, response.body().toString());
                        progressDialog.dismiss();
                        finish();
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
                        Log.e(TAG, t.getMessage(), t);
                    }
                });
            } else {
                progressDialog.dismiss();
                finish();
            }
        }
    }
}
