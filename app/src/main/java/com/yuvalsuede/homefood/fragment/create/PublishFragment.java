package com.yuvalsuede.homefood.fragment.create;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yuvalsuede.homefood.App;
import com.yuvalsuede.homefood.Cloudinary;
import com.yuvalsuede.homefood.CreateDishActivity;
import com.yuvalsuede.homefood.R;
import com.yuvalsuede.homefood.model.Currency;
import com.yuvalsuede.homefood.model.MakeDish;
import com.yuvalsuede.homefood.model.MakePhoto;
import com.yuvalsuede.homefood.model.UserConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PublishFragment extends Fragment implements TextWatcher {
    private static final String TAG = PublishFragment.class.getName();

    public static PublishFragment newInstance() {
        return new PublishFragment();
    }

    LinearLayout linearLayout;
    ImageView imageBeforePublish;
    EditText dishTitle, dishPrice, dishPhone;
    Button buttonPublish;
    TextView currencySimbol, dishDescription;

    private App app;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
        app = (App) getActivity().getApplication();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            ((CreateDishActivity) getActivity()).setUI(CreateDishActivity.NavStep.ABOUT);
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_dish_before_publish, null);
        imageBeforePublish = (ImageView) v.findViewById(R.id.image_before_publish);
        dishTitle = (EditText) v.findViewById(R.id.editTextTitle);
        dishDescription = (TextView) v.findViewById(R.id.editTextDescription);
        dishDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDescription();
            }
        });
        dishPrice = (EditText) v.findViewById(R.id.editTextPrice);
        dishPhone = (EditText) v.findViewById(R.id.editTextPhone);
        linearLayout = (LinearLayout) v.findViewById(R.id.linear_publish);
        buttonPublish = (Button) v.findViewById(R.id.button_publish);
        currencySimbol = (TextView) v.findViewById(R.id.currencySimbol);
        return v;
    }

    private void addDescription() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle(R.string.comment);

        final EditText input = new EditText(getActivity());
        input.setSingleLine(false);
        input.setHint(R.string.write_a_description);
        input.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        input.setLines(5);
        input.setText(dishDescription.getText().toString());
        input.setGravity(Gravity.LEFT | Gravity.TOP);
        input.setSelection(input.getText().length());
        builder.setView(input);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dishDescription.setText(input.getText().toString());
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                dialog.dismiss();

            }
        });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dishPhone.setText(app.getUserModel().getPhoneNumber().toString());
        if (((CreateDishActivity) getActivity()).fileImage != null)
            imageBeforePublish.setImageURI(Uri.fromFile(((CreateDishActivity) getActivity()).fileImage));
        dishTitle.addTextChangedListener(this);
        dishDescription.addTextChangedListener(this);
        dishPrice.addTextChangedListener(this);
        dishPhone.addTextChangedListener(this);
        buttonPublish.setClickable(true);
        App app = (App) getActivity().getApplication();
        UserConfig userConfig = app.getUserConfig();
        String cur = userConfig.getUserCurrency();
        final Currency currency = app.getCurrency(cur);
        currencySimbol.setText(currency.getSymbol());
        buttonPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "publ click");
                if (textExist(dishTitle.getText().toString()) && textExist(dishDescription.getText().toString()) && textExist(dishPrice.getText().toString()) && textExist(dishPhone.getText().toString())) {
                    final MakeDish dish = ((CreateDishActivity) getActivity()).dish;
                    dish.setTitle(dishTitle.getText().toString());
                    dish.setDescription(dishDescription.getText().toString());
                    dish.setPrice(dishPrice.getText().toString());
                    dish.setPhoneNumber(dishPhone.getText().toString());
                    dish.setCountryIsoCode(Locale.getDefault().getCountry());
                    dish.setCurrency(currency.getCode());
                    dish.setCountryName(Locale.getDefault().getDisplayCountry());
                    new UploadPhotoTask().execute();
                } else {
                    Toast.makeText(getActivity(), R.string.all_fields_required, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean textExist(String s) {
        return s != null && s.length() > 0;
    }

    private class UploadPhotoTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog progressDialog = ((CreateDishActivity) getActivity()).progressDialog;
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            ((CreateDishActivity) getActivity()).progressDialog = new ProgressDialog(getActivity());
            ((CreateDishActivity) getActivity()).progressDialog.setCancelable(false);
            ((CreateDishActivity) getActivity()).progressDialog.setMessage(getString(R.string.waiting));
            ((CreateDishActivity) getActivity()).progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String photo_id = null;
            if (((CreateDishActivity) getActivity()).fileImage != null) {
                try {
                    photo_id = Cloudinary.uploadImageAndGetId(((CreateDishActivity) getActivity()).fileImage);
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
            ProgressDialog progressDialog = ((CreateDishActivity) getActivity()).progressDialog;
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (photoId != null) {
                MakeDish dish = ((CreateDishActivity) getActivity()).dish;
                MakePhoto makePhoto = new MakePhoto();
                makePhoto.setId(photoId);
                List<MakePhoto> photos = new ArrayList<>();
                photos.add(makePhoto);
                dish.setPhotos(photos);
                ((CreateDishActivity) getActivity()).createDish();
            } else {
                Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (dishTitle.getText().length() > 0 && dishDescription.getText().length() > 0
                && dishPrice.getText().length() > 0 && dishPhone.getText().length() > 0) {
            linearLayout.setVisibility(View.VISIBLE);
        } else {
            linearLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (dishTitle.getText().length() > 0 && dishDescription.getText().length() > 0
                && dishPrice.getText().length() > 0 && dishPhone.getText().length() > 0) {
            linearLayout.setVisibility(View.VISIBLE);
        } else {
            linearLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (dishTitle.getText().length() > 0 && dishDescription.getText().length() > 0
                && dishPrice.getText().length() > 0 && dishPhone.getText().length() > 0) {
            linearLayout.setVisibility(View.VISIBLE);
        } else {
            linearLayout.setVisibility(View.GONE);
        }
    }
}
