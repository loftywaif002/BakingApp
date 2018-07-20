package com.cmpny.wca.bakingapp.Common;

import com.cmpny.wca.bakingapp.Models.Recipe;

public final class RecipeClickListener {

    public interface OnRecipeClickListener {
        void onRecipeSelected(Recipe recipe);
    }
}
