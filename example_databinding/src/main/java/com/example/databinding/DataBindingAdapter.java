package com.example.databinding;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class DataBindingAdapter<E, T extends ViewDataBinding> extends RecyclerView.Adapter<DataBindingViewHolder> {

    protected abstract int getLayoutRes();

    protected abstract void onBindViewHolder(@NonNull DataBindingViewHolder<T> viewHolder,
                                             T binding,
                                             @NonNull E item);

    private List<E> items;

    public DataBindingAdapter() {
        items = new ArrayList<>();
    }

    public DataBindingAdapter(List<E> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public DataBindingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        T dataBinding = DataBindingUtil.inflate(inflater, getLayoutRes(), viewGroup, false);
        return new DataBindingViewHolder<T>(dataBinding.getRoot());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull DataBindingViewHolder viewHolder, int i) {
        if (viewHolder == null)return;

        T dataBinding = (T) viewHolder.getBinding();
        if (dataBinding == null)return;

        dataBinding.executePendingBindings();
        int position = viewHolder.getAdapterPosition();
        E item = items.get(position);
        if (item == null)return;

        onBindViewHolder(viewHolder, dataBinding, item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<E> items) {
        this.items = items;
    }
}
