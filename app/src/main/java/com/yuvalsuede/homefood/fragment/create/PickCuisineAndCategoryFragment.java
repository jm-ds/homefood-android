package com.yuvalsuede.homefood.fragment.create;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yuvalsuede.homefood.App;
import com.yuvalsuede.homefood.CreateDishActivity;
import com.yuvalsuede.homefood.R;
import com.yuvalsuede.homefood.model.Category;
import com.yuvalsuede.homefood.model.Cuisine;
import com.yuvalsuede.homefood.model.MakeCategory;
import com.yuvalsuede.homefood.model.MakeDish;

import java.util.ArrayList;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

public class PickCuisineAndCategoryFragment extends Fragment {

    public static PickCuisineAndCategoryFragment newInstance(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        PickCuisineAndCategoryFragment pickCuisineAndCategoryFragment = new PickCuisineAndCategoryFragment();
        pickCuisineAndCategoryFragment.setArguments(bundle);
        return pickCuisineAndCategoryFragment;
    }

    private TextView title;
    private TagContainerLayout tagContainerLayout;
    private Button buttonNext;
    private App app;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (App) getActivity().getApplication();
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (getArguments().getInt("type") == 0) {
                ((CreateDishActivity) getActivity()).setUI(CreateDishActivity.NavStep.PHOTO);
            } else {
                ((CreateDishActivity) getActivity()).setUI(CreateDishActivity.NavStep.CUISINE);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem mm = menu.findItem(R.id.action_search);
        if (mm != null) {
            mm.setVisible(false);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_dish_pick_cuisine_and_category, null);
        title = (TextView) view.findViewById(R.id.pick_cuisine_category_title);
        tagContainerLayout = (TagContainerLayout) view.findViewById(R.id.pick_tagcontainer);
        buttonNext = (Button) view.findViewById(R.id.button_next);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActionBar actionBar = ((CreateDishActivity) getActivity()).getSupportActionBar();
        final MakeDish dish = ((CreateDishActivity) getActivity()).dish;
        if (getArguments().getInt("type") == 0) {
            if (actionBar != null) {
                actionBar.setTitle(R.string.cuisine_type);
            }
            title.setText(R.string.what_is_cuisine_type);
            List<String> items = new ArrayList<>();
            for (int i = 0; i < app.getCuisineList().size(); i++) {
                items.add(app.getCuisineList().get(i).getCuisineName());
            }
            tagContainerLayout.setTags(items);
            tagContainerLayout.setClickable(true);
            tagContainerLayout.setIsTagViewClickable(true);
            tagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
                @Override
                public void onTagClick(int position, String text, TagView tagView) {
                    tagView.setChecked();
                    Cuisine cuisine = app.getCuisineByName(text);
                    if (cuisine != null) {
                        dish.setCuisineId(String.valueOf(cuisine.getId()));
                    }
                    ((CreateDishActivity) getActivity()).setUI(CreateDishActivity.NavStep.CATEGORY);
                }

                @Override
                public void onTagLongClick(int position, String text) {

                }
            });
            String id = dish.getCuisineId();
            if (id != null && id.length() > 0) {
                String tagName = app.getCuisineById(Integer.parseInt(id)).getCuisineName();
                TagView tagView = tagContainerLayout.getTagViewByName(tagName);
                if (tagView != null) {
                    tagView.setChecked();
                }
            }
        } else {
            if (actionBar != null) {
                actionBar.setTitle(R.string.select_category);
            }
            title.setText(R.string.select_category);
            List<String> items = new ArrayList<>();
            for (int i = 0; i < app.getCategoryList().size(); i++) {
                items.add(app.getCategoryList().get(i).getCategoryName());
            }
            tagContainerLayout.setTags(items);
            tagContainerLayout.setClickable(true);
            tagContainerLayout.setIsTagViewClickable(true);
            tagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
                @Override
                public void onTagClick(int position, String text, TagView tagView) {
                    tagView.setChecked();
                    List<String> tags = tagContainerLayout.getCheckedTags();
                    if (tags.size() == 0) {
                        buttonNext.setVisibility(View.GONE);
                    } else {
                        buttonNext.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onTagLongClick(int position, String text) {

                }
            });
            buttonNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonNext.setVisibility(View.GONE);
                    List<String> tags = tagContainerLayout.getCheckedTags();
                    List<MakeCategory> categories = new ArrayList<>();
                    for (String text : tags) {
                        Category category = app.getCategoryByName(text);
                        if (category != null) {
                            MakeCategory makeCategory = new MakeCategory();
                            makeCategory.setId(category.getId().toString());
                            categories.add(makeCategory);
                        }
                    }
                    dish.setCategories(categories);
                    ((CreateDishActivity) getActivity()).setUI(CreateDishActivity.NavStep.LOCATION);
                }
            });
            List<MakeCategory> categories = dish.getCategories();
            if (categories != null && categories.size() > 0) {
                for (MakeCategory makeCategory : categories) {
                    Category category = app.getCategoryById(Integer.parseInt(makeCategory.getId()));
                    TagView tagView = tagContainerLayout.getTagViewByName(category.getCategoryName());
                    if (tagView != null) {
                        tagView.setChecked();
                    }
                }
                List<String> tags = tagContainerLayout.getCheckedTags();
                if (tags.size() == 0) {
                    buttonNext.setVisibility(View.GONE);
                } else {
                    buttonNext.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
