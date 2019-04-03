package com.example.databinding;

import android.database.DatabaseUtils;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public abstract class DataBindingActivity<T extends ViewDataBinding> extends AppCompatActivity {

    protected abstract int getLayoutRes();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        T dataBinding = DataBindingUtil.setContentView(this, getLayoutRes());
    }
}
