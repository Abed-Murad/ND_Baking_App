package com.am.nd_baking_app.adapter;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.am.nd_baking_app.R;
import com.am.nd_baking_app.model.Ingredient;
import com.am.nd_baking_app.model.Recipe;
import com.am.nd_baking_app.util.Listeners;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Recipe mRecipe;
    private Listeners.OnItemClickListener mOnItemClickListener;

    public RecipeAdapter(Recipe recipe, Listeners.OnItemClickListener onItemClickListener) {
        this.mRecipe = recipe;
        this.mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) { // Ingredient
            return new IngredientsViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_ingredient_list_item, parent, false));
        } else { // Steps
            return new StepViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_step, parent, false));
        }

    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof IngredientsViewHolder) {
            IngredientsViewHolder viewHolder = (IngredientsViewHolder) holder;
            StringBuilder ingValue = new StringBuilder();
            for (int i = 0; i < mRecipe.getIngredients().size(); i++) {
                Ingredient ingredient = mRecipe.getIngredients().get(i);
                int k = i + 1;
                ingValue.append(String.format(Locale.getDefault(), "- (%d %s) %s", ingredient.getQuantity(), ingredient.getMeasure(), ingredient.getIngredient()));
                if (i != mRecipe.getIngredients().size() - 1)
                    ingValue.append("\n");
            }

//            viewHolder.mTvIngredients.setText(ingValue.toString());
        } else if (holder instanceof StepViewHolder) {
            StepViewHolder viewHolder = (StepViewHolder) holder;
            viewHolder.mTvStepOrder.setText(String.valueOf(position));
            viewHolder.mTvStepName.setText(mRecipe.getSteps().get(position - 1).getShortDescription());

            holder.itemView.setOnClickListener(v -> {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemClick(position - 1);
            });
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return 0;
        else
            return 1;
    }

    @Override
    public int getItemCount() {
        return mRecipe.getSteps().size() + 1;
    }


    public class IngredientsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ingredients_text)
        public TextView mTvIngredients;

        public IngredientsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }


    public class StepViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.step_order_text)
        public TextView mTvStepOrder;

        @BindView(R.id.step_name_text)
        public TextView mTvStepName;

        public StepViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

    }

}
