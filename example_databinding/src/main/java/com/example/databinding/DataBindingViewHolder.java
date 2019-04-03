package com.example.databinding;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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
