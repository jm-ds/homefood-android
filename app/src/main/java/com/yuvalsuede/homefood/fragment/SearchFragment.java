package com.yuvalsuede.homefood.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yuvalsuede.homefood.App;
import com.yuvalsuede.homefood.MainActivity;
import com.yuvalsuede.homefood.R;
import com.yuvalsuede.homefood.SearchResultActivity;
import com.yuvalsuede.homefood.SplashActivity;
import com.yuvalsuede.homefood.model.Category;
import com.yuvalsuede.homefood.model.Cuisine;
import com.yuvalsuede.homefood.model.Filter;
import com.yuvalsuede.homefood.model.SearchModel;
import com.yuvalsuede.homefood.model.UserConfig;
import com.yuvalsuede.homefood.rest.HomeFoodService;
import com.yuvalsuede.homefood.rest.RestService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {
    private static final String TAG = SearchFragment.class.getName();

    SeekBar searchDistanceSeekBar;
    TextView searchDistanceKm;
    TagContainerLayout searchCuisines, searchCategories;
    SwitchCompat deliverySwitch, containsMilkSwitch, vegetarianSwitch, veganSwitch, glutenFreeSwitch,
            sugarFreeSwitch, mainCourseSwitch, suitableForDessertSwitch, kosherSwitch, glatKosherSwitch;
    Button searchButton;
    HomeFoodService homeFoodService;
    private App app;
    MenuItem reset;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        homeFoodService = RestService.getService();
        app = (App) getActivity().getApplication();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.seacrh_fragment, null);
        if (app.needRestart()) {
            getActivity().finish();
            startActivity(new Intent(getActivity(), SplashActivity.class));
        }
        searchDistanceSeekBar = (SeekBar) view.findViewById(R.id.search_distance_seekBar);
        searchDistanceKm = (TextView) view.findViewById(R.id.search_distance_km);
        searchCuisines = (TagContainerLayout) view.findViewById(R.id.search_tagcontainer_cuisines);
        searchCategories = (TagContainerLayout) view.findViewById(R.id.search_tagcontainer_categories);
        deliverySwitch = (SwitchCompat) view.findViewById(R.id.search_switch_delivery);
        containsMilkSwitch = (SwitchCompat) view.findViewById(R.id.search_switch_contains_milk);
        vegetarianSwitch = (SwitchCompat) view.findViewById(R.id.search_switch_vegetarian);
        veganSwitch = (SwitchCompat) view.findViewById(R.id.search_switch_vegan);
        glutenFreeSwitch = (SwitchCompat) view.findViewById(R.id.search_switch_gluten_free);
        sugarFreeSwitch = (SwitchCompat) view.findViewById(R.id.search_switch_sugar_free);
        mainCourseSwitch = (SwitchCompat) view.findViewById(R.id.search_switch_main_course);
        suitableForDessertSwitch = (SwitchCompat) view.findViewById(R.id.search_switch_suitable_for_dessert);
        kosherSwitch = (SwitchCompat) view.findViewById(R.id.search_switch_kosher);
        glatKosherSwitch = (SwitchCompat) view.findViewById(R.id.search_switch_glat_kosher);
        searchButton = (Button) view.findViewById(R.id.search_button_ui);
        UserConfig userConfig = app.getUserConfig();
        if (userConfig != null) {
            if (userConfig.getUserDistanceUnit() == 1) {
                searchDistanceKm.setText(R.string._20_mile);
            }
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(R.string.search);
        ((MainActivity) getActivity()).getResideMenu().addIgnoredView(searchDistanceSeekBar);
        searchDistanceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                UserConfig userConfig = app.getUserConfig();
                String dist = getString(R.string.search_km_text);
                if (userConfig != null) {
                    if (userConfig.getUserDistanceUnit() == 1) {
                        dist = getString(R.string.search_miles_text);
                    }
                }
                if (i == 20)
                    searchDistanceKm.setText(i + dist + "+");
                else
                    searchDistanceKm.setText(i + dist);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        List<String> cuisineItems = new ArrayList<>();
        for (int i = 0; i < app.getCuisineList().size(); i++) {
            cuisineItems.add(app.getCuisineList().get(i).getCuisineName());
        }
        searchCuisines.setTags(cuisineItems);
        searchCuisines.setClickable(true);
        searchCuisines.setIsTagViewClickable(true);
        searchCuisines.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text, TagView tagView) {
                searchCuisines.clearChecked(tagView);
                tagView.setChecked();
            }

            @Override
            public void onTagLongClick(int position, String text) {

            }
        });

        List<String> categoryItems = new ArrayList<>();
        for (int i = 0; i < App.getInstance().getCategoryList().size(); i++) {
            categoryItems.add(App.getInstance().getCategoryList().get(i).getCategoryName());
        }
        searchCategories.setTags(categoryItems);
        searchCategories.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text, TagView tagView) {
                searchCategories.clearChecked(tagView);
                tagView.setChecked();
            }

            @Override
            public void onTagLongClick(int position, String text) {

            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchModel body = prepareToSearch();
                Call<JsonObject> allDishes = homeFoodService.getDishesByFilter(body);
                allDishes.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        Log.i(TAG, "SearchResult: " + response.body().toString());
                        JsonArray jsonArray = response.body().getAsJsonArray("data");
                        if (jsonArray != null) {
                            Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                            intent.putExtra("SEARCH_RESULT", jsonArray.toString());
                            startActivity(intent);
                        } else {
                            Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.e(TAG, "fail " + t.getMessage(), t);
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public SearchModel prepareToSearch() {
        List<String> categories = searchCategories.getCheckedTags();
        List<String> cuisines = searchCuisines.getCheckedTags();
        Log.i(TAG, categories.toString());
        SearchModel searchModel = new SearchModel();
        searchModel.setType("filter");
        searchModel.setPage("0");
        searchModel.setId("0");
        searchModel.setCurrencyCode(app.getUserConfig() != null ? app.getUserConfig().getUserCurrency() : "USD");
        searchModel.setIsoCode(Locale.getDefault().getCountry());
        if (app.getLocation() != null) {
            searchModel.setLat(app.getLocation().getLatitude() + "");
            searchModel.setLng(app.getLocation().getLongitude() + "");
        }
        Filter filter = new Filter();
        if (deliverySwitch.isChecked()) {
            filter.setDelivery("true");
        }
        if (containsMilkSwitch.isChecked()) {
            filter.setContainesMilk("true");
        }
        if (vegetarianSwitch.isChecked()) {
            filter.setVegeterian("true");
        }
        if (veganSwitch.isChecked()) {
            filter.setVegan("true");
        }
        if (glutenFreeSwitch.isChecked()) {
            filter.setGlutenFree("true");
        }
        if (sugarFreeSwitch.isChecked()) {
            filter.setSugarFree("true");
        }
        if (mainCourseSwitch.isChecked()) {
            filter.setMainCourse("true");
        }
        if (deliverySwitch.isChecked()) {
            filter.setDelivery("true");
        }
        if (kosherSwitch.isChecked()) {
            filter.setKosher("true");
        }
        if (glatKosherSwitch.isChecked()) {
            filter.setGlat("true");
        }
        if (searchDistanceSeekBar.getProgress() != 0 && searchDistanceSeekBar.getProgress() != 20) {
            filter.setDistance(String.valueOf(searchDistanceSeekBar.getProgress()));
        }
        if (categories.size() > 0) {
            String categoryName = categories.get(0);
            Category category = App.getInstance().getCategoryByName(categoryName);
            filter.setCategoryId(category.getId().toString());
        }
        if (cuisines.size() > 0) {
            String cuisineName = cuisines.get(0);
            Cuisine cuisine = App.getInstance().getCuisineByName(cuisineName);
            filter.setCuisineId(cuisine.getId().toString());
        }
        searchModel.setFilter(filter);
        return searchModel;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        reset = menu.add("Reset");
        reset.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        searchCategories.clearChecked();
        searchCuisines.clearChecked();
        deliverySwitch.setChecked(false);
        containsMilkSwitch.setChecked(false);
        vegetarianSwitch.setChecked(false);
        veganSwitch.setChecked(false);
        glutenFreeSwitch.setChecked(false);
        sugarFreeSwitch.setChecked(false);
        mainCourseSwitch.setChecked(false);
        suitableForDessertSwitch.setChecked(false);
        kosherSwitch.setChecked(false);
        glatKosherSwitch.setChecked(false);
        searchDistanceSeekBar.setProgress(20);
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).syncMenu(getClass().getSimpleName());
        ((MainActivity) getActivity()).setAddButtonVisibility(false);
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.search));
    }
}
