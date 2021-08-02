package com.yuvalsuede.homefood.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yuvalsuede.homefood.App;
import com.yuvalsuede.homefood.DishDetailActivity;
import com.yuvalsuede.homefood.MainActivity;
import com.yuvalsuede.homefood.R;
import com.yuvalsuede.homefood.RVAdapter;
import com.yuvalsuede.homefood.SpaceItemDecoration;
import com.yuvalsuede.homefood.UserDetailsActivity;
import com.yuvalsuede.homefood.model.Category;
import com.yuvalsuede.homefood.model.Cuisine;
import com.yuvalsuede.homefood.model.DishModel;
import com.yuvalsuede.homefood.model.FavoriteDishes;
import com.yuvalsuede.homefood.model.FavouriteDishId;
import com.yuvalsuede.homefood.model.UserModel;
import com.yuvalsuede.homefood.rest.HomeFoodService;
import com.yuvalsuede.homefood.rest.RestService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFavoriteCookingFragment extends Fragment implements RVAdapter.RowViewHolderClicks {
    private static final String TAG = MyFavoriteCookingFragment.class.getName();

    public static MyFavoriteCookingFragment newInstance() {

        Bundle args = new Bundle();

        MyFavoriteCookingFragment fragment = new MyFavoriteCookingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    Gson gson = new Gson();
    List<FavouriteDishId> favouriteDishIds = new ArrayList<>();
    HomeFoodService homeFoodService;
    private App app;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeFoodService = RestService.getService();
        app = (App) getActivity().getApplication();
    }

    private RecyclerView recyclerView;
    private List<Object> list = new ArrayList<>();
    private RVAdapter rvAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_fragment, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                getData();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(R.string.my_favourite_cooking);
        LinearLayoutManager lLM = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(lLM);
        recyclerView.addItemDecoration(new SpaceItemDecoration(getActivity(), R.dimen.space_between_items));
        rvAdapter = new RVAdapter(getActivity(), list, favouriteDishIds, this, app);
        recyclerView.setAdapter(rvAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).syncMenu(getClass().getSimpleName());
        ((MainActivity) getActivity()).setAddButtonVisibility(false);
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.my_favourite_cooking));
        getData();
    }

    private void getData() {
        UserModel userModel = app.getUserModel();
        if (userModel != null) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("access_token", userModel.getToken());
            hashMap.put("user_id", userModel.getUserId());
//            hashMap.put("page", "0");
            Call<FavoriteDishes> call = homeFoodService.getLikedDishes(hashMap);
            call.enqueue(new Callback<FavoriteDishes>() {
                @Override
                public void onResponse(Call<FavoriteDishes> call, Response<FavoriteDishes> response) {
                    if (response.isSuccess()) {
                        FavoriteDishes favoriteDishes = response.body();
                        list.clear();
                        favouriteDishIds.clear();
                        for (DishModel dishModel : favoriteDishes.getData()) {
                            FavouriteDishId favouriteDishId = new FavouriteDishId(dishModel.getDishId());
                            favouriteDishIds.add(favouriteDishId);
                            list.add(dishModel);
                        }
                        rvAdapter.setFavouriteDishIds(favouriteDishIds);
                        rvAdapter.notifyDataSetChanged();
                    } else {
                        showError(null);
                    }
                    stopRefresh();
                }

                @Override
                public void onFailure(Call<FavoriteDishes> call, Throwable t) {
                    stopRefresh();
                    showError(t);
                }
            });
        }
    }

    private void showError(Throwable t) {
        Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
        Log.e(TAG, "getLikedDishes", t);
    }

    @Override
    public void onLikeClick(final ImageView likeImage, final int position, final boolean isLiked, final ImageView imageView) {
        UserModel userModel = app.getUserModel();
        HashMap<String, String> data = new HashMap<>();
        data.put("access_token", userModel.getToken());
        data.put("dish_id", ((DishModel) list.get(position)).getDishId().toString());
        data.put("from_user_id", userModel.getUserId());
        Call<JsonObject> likeOrNotLike;
        if (!isLiked) {
            likeOrNotLike = homeFoodService.setLike(data);
            Log.i(TAG, "setLike like");
        } else {
            likeOrNotLike = homeFoodService.setUnlike(data);
            Log.i(TAG, "setUnlike unlike");
        }
        imageView.setVisibility(View.GONE);
        likeOrNotLike.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.i(TAG, "resp " + response.body().toString());
                if (isLiked) {
                    getData();
                }
                imageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                imageView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void stopRefresh() {
        if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onUserImageClick(ImageView userImage, int position) {
        Intent intent = new Intent(getActivity(), UserDetailsActivity.class);
        intent.putExtra("user_id", ((DishModel) list.get(position)).getUserId().toString());
        startActivity(intent);
    }

    @Override
    public void onDishClick(View view, int position) {
        Intent intent = new Intent(getActivity(), DishDetailActivity.class);
        intent.putExtra("dish_id", ((DishModel) list.get(position)).getDishId());
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(Category category) {

    }

    @Override
    public void onCuisineClick(Cuisine cuisine) {

    }
}
