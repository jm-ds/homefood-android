package com.yuvalsuede.homefood.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yuvalsuede.homefood.App;
import com.yuvalsuede.homefood.EditDishActivity;
import com.yuvalsuede.homefood.R;
import com.yuvalsuede.homefood.model.Category;
import com.yuvalsuede.homefood.model.Cuisine;
import com.yuvalsuede.homefood.model.DishCategory;

import java.util.ArrayList;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

public class SelectCuisineDialogFragment extends DialogFragment {

    private TagContainerLayout tagContainerLayout;
    private TextView okButton;
    private App app;

    public static SelectCuisineDialogFragment newInstance(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        SelectCuisineDialogFragment selectCuisineDialogFragment = new SelectCuisineDialogFragment();
        selectCuisineDialogFragment.setArguments(bundle);
        return selectCuisineDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (App) getActivity().getApplication();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments().getInt("type") == 0) {
            getDialog().setTitle(R.string.select_cuisine);
        } else {
            getDialog().setTitle(R.string.select_category);
        }
        View view = inflater.inflate(R.layout.dialog_pick_cuisine_category, null);
        tagContainerLayout = (TagContainerLayout) view.findViewById(R.id.pick_tagcontainer);
        okButton = (TextView) view.findViewById(R.id.okButton);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments().getInt("type") == 0) {
            okButton.setVisibility(View.GONE);
            List<String> items = new ArrayList<>();
            for (int i = 0; i < ((EditDishActivity) getActivity()).cuisineList.size(); i++) {
                items.add(((EditDishActivity) getActivity()).cuisineList.get(i).getCuisineName());
            }
            tagContainerLayout.setTags(items);
            tagContainerLayout.setClickable(true);
            tagContainerLayout.setIsTagViewClickable(true);
            tagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
                @Override
                public void onTagClick(int position, String text, TagView tagView) {
                    Cuisine cuisine = app.getCuisineByName(text);
                    if (cuisine != null) {
                        ((EditDishActivity) getActivity()).cuisineId = cuisine.getId().toString();
                        ((EditDishActivity) getActivity()).textCuisine.setText(getString(R.string.select_cuisine_text) + cuisine.getCuisineName());
                    }
                    getDialog().cancel();
                }

                @Override
                public void onTagLongClick(int position, String text) {

                }
            });
        } else {
            List<String> items = new ArrayList<>();
            for (int i = 0; i < ((EditDishActivity) getActivity()).categoryList.size(); i++) {
                items.add(((EditDishActivity) getActivity()).categoryList.get(i).getCategoryName());
            }
            tagContainerLayout.setTags(items);
            tagContainerLayout.setClickable(true);
            tagContainerLayout.setIsTagViewClickable(true);
            tagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
                @Override
                public void onTagClick(int position, String text, TagView tagView) {
                    tagView.setChecked();
                    List<String> list = tagContainerLayout.getCheckedTags();
                    List<DishCategory> dishCategories = new ArrayList<>();
                    for (String cat : list) {
                        Category category = app.getCategoryByName(cat);
                        DishCategory dishCategory = new DishCategory();
                        dishCategory.setCategoryId(category.getId());
                        dishCategories.add(dishCategory);
                    }
                    ((EditDishActivity) getActivity()).dishCategories = dishCategories;
                    ((EditDishActivity) getActivity()).setUpCategory();
//                    Category category = app.getCategoryByName(text);
//                    if (category != null) {
//                        ((EditDishActivity) getActivity()).textCategories.setText(getString(R.string.select_categories_text) + category.getCategoryName());
//                        JSONArray categories = new JSONArray();
//                        JSONObject object = new JSONObject();
//                        try {
//                            object.put("id", category.getId().toString());
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        categories.put(object);
//                    }
                }

                @Override
                public void onTagLongClick(int position, String text) {

                }
            });
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }
}
