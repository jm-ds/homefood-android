package com.yuvalsuede.homefood;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.yuvalsuede.homefood.fragment.ContentListFragment;

public class SearchResultActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        App app = (App) getApplication();
        if (app.needRestart()) {
            finish();
            startActivity(new Intent(this, SplashActivity.class));
        }
        Intent intent = getIntent();
        String searchResultJson = intent.getStringExtra("SEARCH_RESULT");

        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = ContentListFragment.newInstance(searchResultJson);
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        String title = intent.getStringExtra("TITLE");
        if (title == null) {
            setActionBarTitle(getString(R.string.discover));
        } else {
            setActionBarTitle(title);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setActionBarTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }
}
