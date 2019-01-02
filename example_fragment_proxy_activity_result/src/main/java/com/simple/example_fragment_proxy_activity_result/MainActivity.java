package com.simple.example_fragment_proxy_activity_result;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onClick(View view) {

       ActivityResultProxy.with(this)
                .setToActivity(ToActivity.class)
                .setRequestCode(100)
                .startActivityForResult(new OnResultListener() {
                    @Override
                    public void onActivityResult(int requestCode, int resultCode, Intent data) {

                        Log.e("simple", "requestCode ==" + requestCode);
                        Log.e("simple", "resultCode ==" + resultCode);
                        Log.e("simple", "data ==" + data);
                    }
                })
                .start();

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.e("simple", "requestCode ==" + requestCode);
//        Log.e("simple", "resultCode ==" + resultCode);
//        Log.e("simple", "data ==" + data);
//    }
}
