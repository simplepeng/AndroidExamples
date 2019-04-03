package com.example.databinding;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class DataBindingViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {

    private T dataBinding;

    public DataBindingViewHolder(@NonNull View itemView) {
        super(itemView);
        dataBinding = DataBindingUtil.getBinding(itemView);
    }

    public T getDataBinding() {
        return dataBinding;
    }
}
