package com.yuvalsuede.homefood.fragment;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.yuvalsuede.homefood.App;
import com.yuvalsuede.homefood.CreateDishActivity;
import com.yuvalsuede.homefood.DishDetailActivity;
import com.yuvalsuede.homefood.EditDishActivity;
import com.yuvalsuede.homefood.MainActivity;
import com.yuvalsuede.homefood.MyDishesAdapter;
import com.yuvalsuede.homefood.R;
import com.yuvalsuede.homefood.model.DishModel;
import com.yuvalsuede.homefood.model.UserModel;
import com.yuvalsuede.homefood.rest.HomeFoodService;
import com.yuvalsuede.homefood.rest.RestService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyDishesFragment extends Fragment {

    public static MyDishesFragment newInstance() {
        return new MyDishesFragment();
    }

    private static final String TAG = MyDishesFragment.class.getName();
    private SwipeMenuListView listView;
    private List<DishModel> dishModels = new ArrayList<>();
    private Gson gson;
    private UserModel userModel;
    private HomeFoodService homeFoodService;
    private App app;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        gson = new Gson();
        app = (App) getActivity().getApplication();
        homeFoodService = RestService.getService();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.my_dishes_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                startActivityForResult(new Intent(getActivity(), CreateDishActivity.class), MainActivity.CREATE_DISH_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_dishes, null);
        listView = (SwipeMenuListView) v.findViewById(R.id.swipe_listView);
        ((MainActivity) getActivity()).getResideMenu().addIgnoredView(listView);
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) actionBar.setTitle(R.string.my_dishes);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.parseColor("#d1494b")));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setTitle(getString(R.string.delete));

                deleteItem.setTitleSize(20);
                // set item title font color
                deleteItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(deleteItem);

                SwipeMenuItem jumpItem = new SwipeMenuItem(
                        getActivity());
                // set item background
                jumpItem.setBackground(new ColorDrawable(Color.parseColor("#aaaaaa")));
                // set item width
                jumpItem.setWidth(dp2px(90));
                // set item title
                jumpItem.setTitle(getString(R.string.jump));
                // set item title fontsize
                jumpItem.setTitleSize(20);
                // set item title font color
                jumpItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(jumpItem);

                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getActivity());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.parseColor("#4793cf")));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle(getString(R.string.edit));
                // set item title fontsize
                openItem.setTitleSize(20);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);
            }
        };
        listView.setMenuCreator(creator);
        userModel = app.getUserModel();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), DishDetailActivity.class);
                intent.putExtra("dish_id", dishModels.get(i).getDishId());
                startActivity(intent);
            }
        });
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("access_token", userModel.getToken());
                        hashMap.put("dish_id", dishModels.get(position).getDishId().toString());
                        hashMap.put("user_id", userModel.getUserId());
                        Call<JsonObject> deleteDish = homeFoodService.deleteDish(hashMap);
                        deleteDish.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                Log.e("deleted", response.body().toString());
                                dishModels.remove(position);
                                MyDishesAdapter adapter = new MyDishesAdapter(getActivity(), dishModels);
                                if (dishModels.size() > 0)
                                    listView.setAdapter(adapter);
                                else
                                    listView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1));
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {

                            }
                        });
                        // delete
                        break;
                    case 1:
                        try {
                            if (shouldPromoteDish(dishModels.get(position).getLastPromoteTime())) {
                                Log.e("SHOULD_PROMOTE", "YES");
                                HashMap<String, String> jumpMap = new HashMap<>();
                                jumpMap.put("access_token", userModel.getToken());
                                jumpMap.put("dish_id", dishModels.get(position).getDishId().toString());
                                jumpMap.put("user_id", userModel.getUserId());
                                Call<JsonObject> popDish = homeFoodService.popDish(jumpMap);
                                popDish.enqueue(new Callback<JsonObject>() {
                                    @Override
                                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                        Toast.makeText(getActivity(), R.string.dish_successfully_promoted, Toast.LENGTH_SHORT).show();
                                        updateData();
                                    }

                                    @Override
                                    public void onFailure(Call<JsonObject> call, Throwable t) {

                                    }
                                });
                            } else {
                                Log.e("SHOULD_PROMOTE", "NO");
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle(getString(R.string.warning))
                                        .setMessage(getString(R.string.dish_promote_text) +
                                                hoursToPromote(dishModels.get(position).getLastPromoteTime()) + getString(R.string.hours))
                                        .setCancelable(false)
                                        .setNegativeButton(getString(R.string.ok),
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        // jump
                        break;
                    case 2:
                        Intent intent = new Intent(getActivity(), EditDishActivity.class);
                        intent.putExtra("dish_id", dishModels.get(position).getDishId().toString());
                        startActivity(intent);
                        // edit
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
        return v;
    }

    private int dp2px(int dp) {
        float scale = getResources().getDisplayMetrics().density;
        int pixels = (int) (dp * scale + 0.5f);
        return pixels;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).syncMenu(getClass().getSimpleName());
        ((MainActivity) getActivity()).setAddButtonVisibility(false);
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.my_dishes));
        updateData();
    }

    public void updateData() {
        Call<JsonObject> getUserDishes = homeFoodService.getUserDishesWithUserId(userModel.getUserId());
        getUserDishes.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dishModels = gson.fromJson(response.body().getAsJsonArray("data"),
                        new TypeToken<List<DishModel>>() {
                        }.getType());
                MyDishesAdapter myDishesAdapter = new MyDishesAdapter(getActivity(), dishModels);
                if (dishModels != null) {
                    if (dishModels.size() > 0)
                        listView.setAdapter(myDishesAdapter);
                    else
                        listView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    public boolean shouldPromoteDish(String lastPromoteData) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        Date date = df.parse(lastPromoteData);
        long last_promote_date = date.getTime();
        Log.e("SHOULD last promote", last_promote_date + "");
        long current_time = System.currentTimeMillis();
        Log.e("SHOULD current", current_time + "");
        long distanceBetweenDates = current_time - last_promote_date;
        Log.e("SHOULD between", distanceBetweenDates + "");
        double millisecondsInAnHour = 3600000;
        double hoursBetweenDates = distanceBetweenDates / millisecondsInAnHour;
        Log.e("hours between", hoursBetweenDates + "");
        return hoursBetweenDates >= 24;
    }

    public Integer hoursToPromote(String lastPromoteData) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        Date date = df.parse(lastPromoteData);
        long last_promote_date = date.getTime();
        Log.e("SHOULD last promote", last_promote_date + "");
        long current_time = System.currentTimeMillis();
        Log.e("SHOULD current", current_time + "");
        long distanceBetweenDates = current_time - last_promote_date;
        Log.e("SHOULD between", distanceBetweenDates + "");
        double millisecondsInAnHour = 3600000;
        double hoursBetweenDates = distanceBetweenDates / millisecondsInAnHour;
        Log.e("hours between", hoursBetweenDates + "");
        return (int) (24 - hoursBetweenDates);
    }
}
