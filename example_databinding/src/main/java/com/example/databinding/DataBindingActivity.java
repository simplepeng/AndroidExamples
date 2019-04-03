package com.example.databinding;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class DataBindingActivity<T extends ViewDataBinding> extends AppCompatActivity {

    protected abstract int getLayoutRes();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        T dataBinding = DataBindingUtil.setContentView(this, getLayoutRes());
    }
}
