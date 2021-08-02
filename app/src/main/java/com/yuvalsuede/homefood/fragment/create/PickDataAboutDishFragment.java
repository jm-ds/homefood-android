package com.yuvalsuede.homefood.fragment.create;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuvalsuede.homefood.CreateDishActivity;
import com.yuvalsuede.homefood.R;
import com.yuvalsuede.homefood.model.MakeDish;
import com.yuvalsuede.homefood.spinnerwheel.WheelHorizontalView;
import com.yuvalsuede.homefood.spinnerwheel.adapters.NumericWheelAdapter;

public class PickDataAboutDishFragment extends Fragment {

    public static PickDataAboutDishFragment newInstance() {
        return new PickDataAboutDishFragment();
    }

    WheelHorizontalView pieces, people;
    ImageView cold, warm, spicy, verySpicy;
    boolean isCold = false, isWarm = false, isSpicy = false, isVerySpicy = false;
    TextView coldText, warmText, spicyText, verySpicyText;
    TextView weightText, weightTextPlus, weightTextMinus;
    double weightDouble = 0.0;
    SwitchCompat deliverySwitch, containsMilkSwitch, vegetarianSwitch, veganSwitch, glutenFreeSwitch,
            sugarFreeSwitch, mainCourseSwitch, suitableForDessertSwitch, kosherSwitch, glatKosherSwitch;
    Button buttonNext;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            ((CreateDishActivity) getActivity()).setUI(CreateDishActivity.NavStep.LOCATION);
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_dish_about_dish, null);

        cold = (ImageView) v.findViewById(R.id.create_cold_dish);
        coldText = (TextView) v.findViewById(R.id.create_cold_dish_text);

        warm = (ImageView) v.findViewById(R.id.create_warm_dish);
        warmText = (TextView) v.findViewById(R.id.create_warm_dish_text);

        spicy = (ImageView) v.findViewById(R.id.create_spicy_dish);
        spicyText = (TextView) v.findViewById(R.id.create_dish_spicy_text);

        verySpicy = (ImageView) v.findViewById(R.id.create_very_spicy_dish);
        verySpicyText = (TextView) v.findViewById(R.id.create_very_spicy_dish_text);

        weightText = (TextView) v.findViewById(R.id.create_dish_weight_text);
        weightTextPlus = (TextView) v.findViewById(R.id.create_dish_weight_text_plus);
        weightTextMinus = (TextView) v.findViewById(R.id.create_dish_weight_text_minus);

        pieces = (WheelHorizontalView) v.findViewById(R.id.create_dish_pieces_wheel);
        NumericWheelAdapter adapter = new NumericWheelAdapter(getActivity(), 1, 15);
        adapter.setItemResource(R.layout.wheel_text_centered);
        adapter.setItemTextResource(R.id.text);
        pieces.setViewAdapter(adapter);
        people = (WheelHorizontalView) v.findViewById(R.id.create_dish_people_wheel);
        NumericWheelAdapter adapterPeople = new NumericWheelAdapter(getActivity(), 1, 25);
        adapterPeople.setItemResource(R.layout.wheel_text_centered);
        adapterPeople.setItemTextResource(R.id.text);
        people.setViewAdapter(adapterPeople);

        deliverySwitch = (SwitchCompat) v.findViewById(R.id.search_switch_delivery);
        containsMilkSwitch = (SwitchCompat) v.findViewById(R.id.search_switch_contains_milk);
        vegetarianSwitch = (SwitchCompat) v.findViewById(R.id.search_switch_vegetarian);
        veganSwitch = (SwitchCompat) v.findViewById(R.id.search_switch_vegan);
        glutenFreeSwitch = (SwitchCompat) v.findViewById(R.id.search_switch_gluten_free);
        sugarFreeSwitch = (SwitchCompat) v.findViewById(R.id.search_switch_sugar_free);
        mainCourseSwitch = (SwitchCompat) v.findViewById(R.id.search_switch_main_course);
        suitableForDessertSwitch = (SwitchCompat) v.findViewById(R.id.search_switch_suitable_for_dessert);
        kosherSwitch = (SwitchCompat) v.findViewById(R.id.search_switch_kosher);
        glatKosherSwitch = (SwitchCompat) v.findViewById(R.id.search_switch_glat_kosher);

        buttonNext = (Button) v.findViewById(R.id.button_next);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActionBar actionBar = ((CreateDishActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) actionBar.setTitle(R.string.about_dish);
        weightTextMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (weightDouble == 0.5) {
                    weightDouble = weightDouble - 0.5;
                    weightText.setText("Any");
                } else if (weightDouble != 0.0) {
                    weightDouble = weightDouble - 0.5;
                    weightText.setText(weightDouble + "");
                }
            }
        });
        weightTextPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (weightDouble != 10.0) {
                    weightDouble = weightDouble + 0.5;
                    weightText.setText(weightDouble + "");
                }
            }
        });

        cold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCold) {
                    cold.setImageResource(R.drawable.filters_cold_dish_unselected);
                    isCold = false;
                } else {
                    cold.setImageResource(R.drawable.filters_cold_dish);
                    isCold = true;
                    warm.setImageResource(R.drawable.filters_warm_dish_unselected);
                    isWarm = false;
                }
            }
        });
        warm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isWarm) {
                    warm.setImageResource(R.drawable.filters_warm_dish_unselected);
                    isWarm = false;
                } else {
                    cold.setImageResource(R.drawable.filters_cold_dish_unselected);
                    isCold = false;
                    warm.setImageResource(R.drawable.filters_warm_dish);
                    isWarm = true;
                }
            }
        });
        spicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSpicy) {
                    spicy.setImageResource(R.drawable.filters_spicy_unselected);
                    isSpicy = false;
                } else {
                    spicy.setImageResource(R.drawable.filters_spicy);
                    isSpicy = true;
                    verySpicy.setImageResource(R.drawable.filters_very_spicy_unselected);
                    isVerySpicy = false;
                }
            }
        });
        verySpicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isVerySpicy) {
                    verySpicy.setImageResource(R.drawable.filters_very_spicy_unselected);
                    isVerySpicy = false;
                } else {
                    verySpicy.setImageResource(R.drawable.filters_very_spicy);
                    isVerySpicy = true;
                    spicy.setImageResource(R.drawable.filters_spicy_unselected);
                    isSpicy = false;
                }
            }
        });
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MakeDish dish = ((CreateDishActivity) getActivity()).dish;
                dish.setDelivery(String.valueOf(deliverySwitch.isChecked()));
                dish.setColdWarm(String.valueOf(0)); //// TODO: 18.04.2016 remove hardcode ,узнать какие значения может принимать
                dish.setWeight(String.valueOf(weightDouble));
                dish.setNumOfPieces(String.valueOf(pieces.getCurrentItem()));
                dish.setNumOfPeople(String.valueOf(people.getCurrentItem()));
                dish.setKosher(String.valueOf(kosherSwitch.isChecked()));
                dish.setContainesMilk(String.valueOf(containsMilkSwitch.isChecked()));
                dish.setVegeterian(String.valueOf(vegetarianSwitch.isChecked()));
                dish.setVegan(String.valueOf(veganSwitch.isChecked()));
                dish.setGlutenFree(String.valueOf(glutenFreeSwitch.isChecked()));
                dish.setSugarFree(String.valueOf(sugarFreeSwitch.isChecked()));
                dish.setMainCourse(String.valueOf(mainCourseSwitch.isChecked()));
                dish.setGlat(String.valueOf(glatKosherSwitch.isChecked()));
                dish.setSpicyLevel(String.valueOf(0)); //// TODO: 18.04.2016 hardcode! узнать какие значения может принимать
                ((CreateDishActivity) getActivity()).setUI(CreateDishActivity.NavStep.FINAL);
            }
        });
    }
}
