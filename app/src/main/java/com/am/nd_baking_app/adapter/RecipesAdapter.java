package com.am.nd_baking_app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.am.nd_baking_app.databinding.ItemRecipeBinding;
import com.am.nd_baking_app.R;
import com.am.nd_baking_app.model.Recipe;
import com.am.nd_baking_app.util.FUNC;
import com.am.nd_baking_app.util.GlideApp;
import com.am.nd_baking_app.util.Listeners;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

import static com.am.nd_baking_app.util.FUNC.retriveVideoFrameFromVideo;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {
    private Context mContext;
    private List<Recipe> mRecipes;
    private LayoutInflater mInflater;
    private ItemRecipeBinding mBinding;
    private Listeners.OnItemClickListener mOnItemClickListener;

    Bitmap bitmap;


    public RecipesAdapter(Context context, List<Recipe> recipes, Listeners.OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.mRecipes = recipes;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mOnItemClickListener = onItemClickListener;

    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mBinding = ItemRecipeBinding.inflate(mInflater, parent, false);
        return new RecipeViewHolder(mBinding);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, final int position) {

        Recipe recipe = mRecipes.get(position);
        holder.bind(recipe);

    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        private ItemRecipeBinding mBinding;

        RecipeViewHolder(ItemRecipeBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }

        @SuppressLint("SetTextI18n")
        private void bind(Recipe recipe) {
                GlideApp.with(mContext)
                        .load(recipe.getImage())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.image_dessert)
                        .placeholder(R.drawable.image_dessert)
                        .into(mBinding.recipeImageView);

            mBinding.recipeNameTextView.setText(recipe.getName());
            mBinding.servingsTextView.setText(recipe.getServings() + "");

            mBinding.getRoot().setOnClickListener(v -> {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemClick(getAdapterPosition());
            });

        }

    }



}
