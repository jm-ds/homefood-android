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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yuvalsuede.homefood.App;
import com.yuvalsuede.homefood.DishDetailActivity;
import com.yuvalsuede.homefood.FBLoginActivity;
import com.yuvalsuede.homefood.MainActivity;
import com.yuvalsuede.homefood.R;
import com.yuvalsuede.homefood.RVAdapter;
import com.yuvalsuede.homefood.SearchResultActivity;
import com.yuvalsuede.homefood.SpaceItemDecoration;
import com.yuvalsuede.homefood.UserDetailsActivity;
import com.yuvalsuede.homefood.model.Category;
import com.yuvalsuede.homefood.model.Cuisine;
import com.yuvalsuede.homefood.model.DishModel;
import com.yuvalsuede.homefood.model.Favorite;
import com.yuvalsuede.homefood.model.FavouriteDishId;
import com.yuvalsuede.homefood.model.Filter;
import com.yuvalsuede.homefood.model.SearchModel;
import com.yuvalsuede.homefood.model.UserModel;
import com.yuvalsuede.homefood.model.dishinfo.DishInfo;
import com.yuvalsuede.homefood.rest.HomeFoodService;
import com.yuvalsuede.homefood.rest.RestService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContentListFragment extends Fragment implements RVAdapter.RowViewHolderClicks {
    private static final String TAG = ContentListFragment.class.getName();

    private RecyclerView recyclerView;
    private List<Object> list = new ArrayList<>();
    private RVAdapter rvAdapter;

    private Gson gson = new Gson();
    private UserModel userModel = null;
    private List<FavouriteDishId> favouriteDishIds = new ArrayList<>();
    private HomeFoodService homeFoodService;
    private ContentListFragmentListener fragmentListener;
    private App app;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static ContentListFragment newInstance(String searchResult) {
        Bundle extras = new Bundle();
        extras.putString("result", searchResult);
        ContentListFragment fragment = new ContentListFragment();
        fragment.setArguments(extras);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        homeFoodService = RestService.getService();
        app = (App) getActivity().getApplication();
    }

    public void setAdapter() {
        rvAdapter.setItems(list);
        rvAdapter.setFavouriteDishIds(favouriteDishIds);
        rvAdapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_fragment, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        rvAdapter = new RVAdapter(getActivity(), new ArrayList<>(), new ArrayList<FavouriteDishId>(), this, app);
        recyclerView.setAdapter(rvAdapter);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                requestData();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (fragmentListener != null) {
            fragmentListener.goOnResume();
        }
        userModel = app.getUserModel();
        if (userModel != null) {
            getUserLikes();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(R.string.discover);
        LinearLayoutManager lLM = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(lLM);
        recyclerView.addItemDecoration(new SpaceItemDecoration(getActivity(), R.dimen.space_between_items));
        requestData();
    }

    private void stopRefresh() {
        if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private void requestData() {
        String result = getArguments().getString("result");

        if (result == null) {
            String currency = "USD";
            if (app.getUserConfig() != null) {
                currency = app.getUserConfig().getUserCurrency();
            }
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("type", "all");
            hashMap.put("currency_code", currency);
            hashMap.put("country_iso_code", Locale.getDefault().getCountry());
            if (app.getLocation() != null) {
                hashMap.put("lat", app.getLocation().getLatitude() + "");
                hashMap.put("lng", app.getLocation().getLongitude() + "");
            } else {
                Toast.makeText(getActivity(), R.string.locations_not_defined, Toast.LENGTH_SHORT).show();
            }
//            Log.i(TAG, "hash: " + hashMap.toString());
            Call<JsonObject> allDishes = homeFoodService.getDishesByFilter(hashMap);
            allDishes.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    JsonArray jsonArray = response.body().getAsJsonArray("data");
                    Log.i(TAG, "data: " + response.body().toString());
                    workingWithResult(jsonArray);
                    stopRefresh();
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e(TAG, "fail getDishesByFilter", t);
                    Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_LONG).show();
                    stopRefresh();
                }
            });
        } else {
            JsonParser parser = new JsonParser();
            JsonElement tradeElement = parser.parse(result);
            JsonArray jsonArray = tradeElement.getAsJsonArray();
            workingWithResult(jsonArray);
        }
    }

    public void workingWithResult(JsonArray jsonArray) {
        try {
            List<Object> itemsList = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                if (jsonObject.get("type").getAsString().equals("1")) {
                    if (App.getInstance().getCuisineType(jsonObject) != null) {
                        itemsList.add(App.getInstance().getCuisineType(jsonObject));
                    }
                } else if (jsonObject.get("type").getAsString().equals("2")) {
                    itemsList.add(App.getInstance().getCategoryType(jsonObject));
                } else {
                    itemsList.add(getDishFromJson(jsonObject));
                }
            }
            list.clear();
            list.addAll(itemsList);
            setAdapter();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        stopRefresh();
    }

    public DishModel getDishFromJson(JsonObject jsonObject) {
        return gson.fromJson(jsonObject.get("content").getAsJsonObject(), DishModel.class);
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
                rvAdapter.setFavouriteDishIds(favouriteDishIds);
                rvAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Favorite> call, Throwable t) {

            }
        });
    }

    @Override
    public void onLikeClick(final ImageView likeImage, final int position, final boolean isLiked, final ImageView imageView) {
        if (userModel != null) {
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
                        likeImage.setImageResource(R.drawable.icon_love_none);
                        int positionToRemove = -1;
                        for (int i = 0; i < favouriteDishIds.size(); i++) {
                            if (favouriteDishIds.get(i).getDishId().equals((((DishModel) list.get(position)).getDishId()))) {
                                positionToRemove = i;
                                break;
                            }
                        }
                        if (positionToRemove != -1) {
                            DishModel dishModel = (DishModel) list.get(position);
                            dishModel.setLikes(dishModel.getLikes() == null ? 0 : dishModel.getLikes() - 1);
                            favouriteDishIds.remove(positionToRemove);
                            rvAdapter.setFavouriteDishIds(favouriteDishIds);
                            rvAdapter.notifyDataSetChanged();
                        }
                    } else {
                        likeImage.setImageResource(R.drawable.icon_love_full);
                        if (favouriteDishIds != null) {
                            favouriteDishIds.add(new FavouriteDishId(((DishModel) list.get(position)).getDishId()));
                        } else {
                            favouriteDishIds = new ArrayList<>();
                            favouriteDishIds.add(new FavouriteDishId(((DishModel) list.get(position)).getDishId()));
                        }
                        DishModel dishModel = (DishModel) list.get(position);
                        dishModel.setLikes(dishModel.getLikes() == null ? 1 : dishModel.getLikes() + 1);
                        rvAdapter.setFavouriteDishIds(favouriteDishIds);
                        rvAdapter.notifyDataSetChanged();
                    }
                    imageView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    imageView.setVisibility(View.VISIBLE);
                }
            });
        } else {
            startActivityForResult(new Intent(getActivity(), FBLoginActivity.class), MainActivity.CODE_FOR_FB_ACTIVITY);
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
    public void onCategoryClick(final Category category) {
        String currency = "USD";
        if (app.getUserConfig() != null) {
            currency = app.getUserConfig().getUserCurrency();
        }
        SearchModel searchModel = new SearchModel();
        searchModel.setType("category");
        searchModel.setCurrencyCode(currency);
        searchModel.setIsoCode(Locale.getDefault().getCountry());
        searchModel.setId(category.getId().toString());
        searchModel.setPage("0");
        if (app.getLocation() != null) {
            searchModel.setLat(app.getLocation().getLatitude() + "");
            searchModel.setLng(app.getLocation().getLongitude() + "");
        }
        Filter filter = new Filter();
        searchModel.setFilter(filter);
        Call<JsonObject> allDishes = homeFoodService.getDishesByFilter(searchModel);
        allDishes.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.i(TAG, "SearchResult: " + response.body().toString());
                JsonArray jsonArray = response.body().getAsJsonArray("data");
                if (jsonArray != null) {
                    Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                    intent.putExtra("SEARCH_RESULT", jsonArray.toString());
                    intent.putExtra("TITLE", category.getCategoryName());
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "fail " + t.getMessage(), t);
            }
        });
    }

    @Override
    public void onCuisineClick(final Cuisine cuisine) {
        String currency = "USD";
        if (app.getUserConfig() != null) {
            currency = app.getUserConfig().getUserCurrency();
        }
        SearchModel searchModel = new SearchModel();
        searchModel.setType("cuisine");
        searchModel.setId(cuisine.getId().toString());
        searchModel.setPage("0");
        searchModel.setCurrencyCode(currency);
        searchModel.setIsoCode(Locale.getDefault().getCountry());
        if (app.getLocation() != null) {
            searchModel.setLat(app.getLocation().getLatitude() + "");
            searchModel.setLng(app.getLocation().getLongitude() + "");
        }
        Filter filter = new Filter();
        searchModel.setFilter(filter);
        Call<JsonObject> allDishes = homeFoodService.getDishesByFilter(searchModel);
        allDishes.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.i(TAG, "SearchResult: " + response.body().toString());
                JsonArray jsonArray = response.body().getAsJsonArray("data");
                if (jsonArray != null) {
                    Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                    intent.putExtra("SEARCH_RESULT", jsonArray.toString());
                    intent.putExtra("TITLE", cuisine.getCuisineName());
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "fail " + t.getMessage(), t);
            }
        });
    }

    public void addCreatedDish(Integer dish_id) {
        final Call<JsonObject> getDishInfo = homeFoodService.getDishInfo(dish_id.toString());
        getDishInfo.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                DishInfo mDishInfo = gson.fromJson(response.body().getAsJsonObject("data"), DishInfo.class);
                DishModel createdDish = new DishModel();
                createdDish.setBaseCurrency(mDishInfo.getDishInfo().getBaseCurrency());
                createdDish.setCountryIsoCode(mDishInfo.getDishInfo().getCountryIsoCode());
                createdDish.setCountryName(mDishInfo.getDishInfo().getCountryName());
                createdDish.setCreationTime(mDishInfo.getDishInfo().getCreationTime());
                createdDish.setCuisineId(mDishInfo.getDishInfo().getCuisineId());
                createdDish.setDescription(mDishInfo.getDishInfo().getDescription());
                createdDish.setCurrency(mDishInfo.getDishInfo().getCurrency());
                createdDish.setDishId(mDishInfo.getDishInfo().getDishId());
                createdDish.setExchangeRate(mDishInfo.getDishInfo().getExchangeRate());
                createdDish.setLastPromoteTime(mDishInfo.getDishInfo().getLastPromoteTime());
                createdDish.setLat(mDishInfo.getDishInfo().getLat());
                createdDish.setLikes(mDishInfo.getDishLikes().size());
                createdDish.setLng(mDishInfo.getDishInfo().getLng());
                createdDish.setMainPhoto(mDishInfo.getDishInfo().getMainPhoto());
                createdDish.setPrice(mDishInfo.getDishInfo().getPrice());
                createdDish.setProfileImgUrl(userModel.getProfileImgUrl());
                createdDish.setTitle(mDishInfo.getDishInfo().getTitle());
                createdDish.setUserId(mDishInfo.getDishInfo().getUserId());
                list.add(0, createdDish);
                updateAdapter();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    public void updateAdapter() {
        rvAdapter = new RVAdapter(getActivity(), list, favouriteDishIds, this, app);
        recyclerView.setAdapter(rvAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        String result = getArguments().getString("result");
        if (result == null) {
            inflater.inflate(R.menu.discover_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                if (fragmentListener != null) {
                    fragmentListener.startSearch();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setFragmentListener(ContentListFragmentListener fragmentListener) {
        this.fragmentListener = fragmentListener;
    }

    public interface ContentListFragmentListener {
        void goOnResume();

        void startSearch();
    }
}
