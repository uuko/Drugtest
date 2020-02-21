package com.example.drugtest.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drugtest.databinding.ItemXBinding;
import com.example.drugtest.model.Cozaui;
import com.example.drugtest.model.Feature;

import java.util.List;

class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder<ItemXBinding>> {
    private Cozaui cozaui;
    private ItemXBinding itemXBinding;
    private List<Feature> cozauiArrayList;
    private Context c;


    public MainAdapter() {
    }

    public void setListData(List<Feature> mList) {
        this.cozauiArrayList=mList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder<ItemXBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        c = parent.getContext();
        itemXBinding = itemXBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder<>(itemXBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder<ItemXBinding> holder, int position) {
        final Feature feature=cozauiArrayList.get(position);
        holder.bind(feature);
    }

    @Override
    public int getItemCount() {
        return cozauiArrayList==null ?0:cozauiArrayList.size();
    }

    public class ViewHolder<B extends ViewDataBinding> extends RecyclerView.ViewHolder {

        private final B mViewDataBinding;

        public ViewHolder(B binding) {
            super(binding.getRoot());
            mViewDataBinding = binding;
        }

        public void bind(final Object object) {
            mViewDataBinding.setVariable(com.example.drugtest.BR.data, object);
            mViewDataBinding.executePendingBindings();
        }

    }
}
