package com.cmpny.wca.bakingapp.Adapters;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmpny.wca.bakingapp.Common.ItemClickListener;
import com.cmpny.wca.bakingapp.Models.Ingredients;
import com.cmpny.wca.bakingapp.Models.Recipe;
import com.cmpny.wca.bakingapp.R;
import com.cmpny.wca.bakingapp.ViewHolders.IngredientsViewHolder;
import com.cmpny.wca.bakingapp.ViewHolders.StepViewHolder;

import java.util.Locale;

public class SingleRecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Recipe mRecipe;
    private ItemClickListener.OnItemClickListener mOnItemClickListener;

    public SingleRecipeAdapter(Recipe recipe, ItemClickListener.OnItemClickListener onItemClickListener) {
        this.mRecipe = recipe;
        this.mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) { // Ingredients
            return new IngredientsViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ingredient_list_item, parent, false));
        } else { // Steps
            return new StepViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_step_item, parent, false));
        }

    }

    @Override
    public int getItemCount() {
        return mRecipe.getSteps().size() + 1;
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof IngredientsViewHolder) {
            IngredientsViewHolder viewHolder = (IngredientsViewHolder) holder;

            StringBuilder ingValue = new StringBuilder();
            for (int i = 0; i < mRecipe.getIngredients().size(); i++) {
                Ingredients ingredients = mRecipe.getIngredients().get(i);
                ingValue.append(String.format(Locale.getDefault(), "• %s (%d %s)", ingredients.getIngredient(), ingredients.getQuantity(), ingredients.getMeasure()));
                if (i != mRecipe.getIngredients().size() - 1)
                    ingValue.append("\n");
            }

            viewHolder.mTvIngredients.setText(ingValue.toString());
        } else if (holder instanceof StepViewHolder) {
            StepViewHolder viewHolder = (StepViewHolder) holder;
            viewHolder.mTvStepOrder.setText(String.valueOf(position - 1) + ".");
            viewHolder.mTvStepName.setText(mRecipe.getSteps().get(position - 1).getShortDescription());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null)
                        mOnItemClickListener.onItemClick(position - 1);
                }
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

}
