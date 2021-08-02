package com.yuvalsuede.homefood;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yuvalsuede.homefood.model.Profile;
import com.yuvalsuede.homefood.rest.HomeFoodService;
import com.yuvalsuede.homefood.rest.RestService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG = EditProfileActivity.class.getName();
    private App app;
    private TextView userAbout, editAboutMe, maleLabel, femaleLabel;
    private EditText emailEt, phoneEt;
    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        app = (App) getApplication();
        profile = app.getProfile();
        setUI();
        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            profile.setAccessToken(app.getUserModel().getToken());
            HomeFoodService homeFoodService = RestService.getService();
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.waiting));
            progressDialog.show();
            Call<JsonObject> call = homeFoodService.updateProfile(profile);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    progressDialog.dismiss();
                    if (response.isSuccess()) {
                        finish();
                    } else {
                        Toast.makeText(EditProfileActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(EditProfileActivity.this, R.string.network_error, Toast.LENGTH_LONG).show();
                    Log.e(TAG, t.getMessage(), t);
                }
            });
        } else if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateUI() {
        userAbout.setText(profile.getAbout());
        emailEt.setText(profile.getEmail());
        phoneEt.setText(profile.getPhoneNumber());
        if (profile.getGender().equals("m")) {
            maleLabel.setTextColor(getResources().getColor(R.color.colorPrimary));
            femaleLabel.setTextColor(getResources().getColor(R.color.textColorGrey));
        } else {
            maleLabel.setTextColor(getResources().getColor(R.color.textColorGrey));
            femaleLabel.setTextColor(getResources().getColor(R.color.colorPrimary));

        }
    }

    private void setUI() {
        userAbout = (TextView) findViewById(R.id.userAbout);
        editAboutMe = (TextView) findViewById(R.id.editAboutMe);
        editAboutMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutDialog();
            }
        });
        maleLabel = (TextView) findViewById(R.id.maleLabel);
        maleLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile.setGender("m");
                profile.setEmail(emailEt.getText().toString());
                profile.setPhoneNumber(phoneEt.getText().toString());
                updateUI();
            }
        });
        femaleLabel = (TextView) findViewById(R.id.femaleLabel);
        femaleLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile.setGender("f");
                updateUI();
            }
        });
        emailEt = (EditText) findViewById(R.id.emailEt);
        phoneEt = (EditText) findViewById(R.id.phoneEt);
    }

    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
//        builder.setTitle(R.string.comment);

        final EditText input = new EditText(this);
        input.setSingleLine(false);
        input.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        input.setLines(5);
        input.setGravity(Gravity.LEFT | Gravity.TOP);
        input.setText(profile.getAbout());
        input.setSelection(input.getText().length());
        builder.setView(input);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                profile.setAbout(input.getText().toString());
                updateUI();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
            }
        });
    }
}
