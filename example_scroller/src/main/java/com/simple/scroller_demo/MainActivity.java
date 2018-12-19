package com.simple.scroller_demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private ScrollerView mScrollerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mScrollerView = findViewById(R.id.scrollerView);
    }

    public void onClick(View view){
        mScrollerView.smoothScroll();
    }
}
