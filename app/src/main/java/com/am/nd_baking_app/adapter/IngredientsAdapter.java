package com.am.nd_baking_app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.am.nd_baking_app.databinding.ItemIngredientBinding;
import com.am.nd_baking_app.model.Ingredient;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {
    private final Context mContext;
    private final List<Ingredient> mIngredientList;
    private ItemIngredientBinding mBinding;
    private List<Ingredient> ingredientList;
    private LayoutInflater mInflater;


    public IngredientsAdapter(Context context, List<Ingredient> ingredientList) {
        this.mContext = context;
        this.mIngredientList = ingredientList;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mBinding = ItemIngredientBinding.inflate(mInflater, parent, false);
        return new ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mIngredientList != null || mIngredientList.size() != 0) {
            Ingredient ingredient = getItem(position);
            holder.bind(ingredient);
        }
    }


    @Override
    public int getItemCount() {
        return mIngredientList.size();
    }

    public Ingredient getItem(int position) {
        return mIngredientList.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemIngredientBinding mBinding;

        public ViewHolder(ItemIngredientBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }

        @SuppressLint("SetTextI18n")
        private void bind(Ingredient ingredient) {
            String ingredientType = ingredient.getIngredient();
            String measure = ingredient.getMeasure();
            int quantity = ingredient.getQuantity();
            mBinding.ingredientTextView.setText(quantity + " " + measure + " " + ingredientType);
        }
    }

}
