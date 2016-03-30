package com.mothership.tvhome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import com.mothership.tvhome.app.KeyboardFragment;
import com.mothership.tvhome.app.SearchResultFragment;

/**
 * 搜索界面
 *
 * Created by zhuzhenhua on 15-12-22.
 */
public class SearchActivity extends FragmentActivity
{

    private KeyboardFragment mKeyboardFragment;
    private SearchResultFragment mSearchResultFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initViews();
    }

    private void initViews() {
        FragmentManager manager = getSupportFragmentManager();
        mSearchResultFragment = (SearchResultFragment) manager.findFragmentById(R.id.fragment_search_result);
        mKeyboardFragment = (KeyboardFragment) manager.findFragmentById(R.id.fragment_keyboard);
        mKeyboardFragment.setOnTextChangedListener(new KeyboardFragment.OnTextChangedListener() {

            @Override
            public void onTextChanged(String text)
            {
                mSearchResultFragment.search(text);
            }
        });
    }
}
