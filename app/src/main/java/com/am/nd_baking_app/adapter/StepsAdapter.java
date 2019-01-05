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
import com.am.nd_baking_app.model.Step;
import com.am.nd_baking_app.util.Listeners;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepViewHolder> {
    private List<Step> mStepList;
    private Listeners.OnItemClickListener mOnItemClickListener;

    public StepsAdapter(List<Step> stepList, Listeners.OnItemClickListener onItemClickListener) {
        this.mStepList = stepList;
        this.mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new StepViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_step, parent, false));

    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, final int position) {
        Step step = mStepList.get(position );
        holder.bind(step);
        holder.itemView.setOnClickListener(v -> {
            if (mOnItemClickListener != null)
                mOnItemClickListener.onItemClick(position);
        });
    }




    @Override
    public int getItemCount() {
        return mStepList != null ? mStepList.size() : 0;
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

        private void bind(Step step) {
            mTvStepOrder.setText(String.valueOf(getAdapterPosition()));
            mTvStepName.setText(step.getShortDescription());
        }

    }

}
