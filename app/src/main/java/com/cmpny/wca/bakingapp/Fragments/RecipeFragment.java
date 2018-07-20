package com.cmpny.wca.bakingapp.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmpny.wca.bakingapp.Adapters.RecipesAdapter;
import com.cmpny.wca.bakingapp.Common.ItemClickListener;
import com.cmpny.wca.bakingapp.Common.RecipeClickListener;
import com.cmpny.wca.bakingapp.Models.Recipe;
import com.cmpny.wca.bakingapp.R;
import com.cmpny.wca.bakingapp.RecipeApi.RecipesApi;
import com.cmpny.wca.bakingapp.utilities.NetworkUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RecipeFragment extends Fragment{

    public static final String TAG = RecipeFragment.class.getSimpleName();

    @BindView(R.id.recipe_recycler_view)
    RecyclerView mRecipesRecyclerView;
    @BindView(R.id.pull_n_refresh)
    SwipeRefreshLayout mPullNRefresh;
    @BindView(R.id.no_data_layout)
    ConstraintLayout mNoDataLayout;

    private static String INTENT_KEY = "recipes";

    private com.cmpny.wca.bakingapp.Common.RecipeClickListener.OnRecipeClickListener mClickListener;
    private List<Recipe> mRecipes;


    //Binding Reset
    //
    //Fragments have a different view lifecycle than activities.
    // When binding a fragment in onCreateView,
    // set the views to null in onDestroyView.
    // Butter Knife returns an Unbinder instance when you call bind to do this for you.
    // Call its unbind method in the appropriate lifecycle callback.
    private Unbinder unbinder;

    //This interface must be implemented by activities that contain this fragment
    //To make communication happen between activity and fragment
    public interface RecipeClickListener {
        void RecipeSelected(Recipe recipe);
    }


    //Empty public constructor
    public RecipeFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment bind view to butter knife
        View viewRoot = inflater.inflate(R.layout.recipes_fragment, container, false);
        unbinder = ButterKnife.bind(this, viewRoot);

        mPullNRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               //Add new Method Here;
            }
        });

        mNoDataLayout.setVisibility(View.VISIBLE);
        setRecyclerViewLayout();

        //Add code for Espresso Test



        if (savedInstanceState != null && savedInstanceState.containsKey(INTENT_KEY)) {
            mRecipes = savedInstanceState.getParcelableArrayList(INTENT_KEY);

            mRecipesRecyclerView.setAdapter(new RecipesAdapter(getActivity().getApplicationContext(), mRecipes, new ItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    mClickListener.onRecipeSelected(mRecipes.get(position));
                }
            }));
            checkLoadedDataAndChangeLayout();
        }
        return viewRoot;
    }

    private void checkLoadedDataAndChangeLayout() {
        boolean loaded = mRecipes != null && mRecipes.size() > 0;
        mPullNRefresh.setRefreshing(false);

        mRecipesRecyclerView.setVisibility(loaded ? View.VISIBLE : View.GONE);
        mNoDataLayout.setVisibility(loaded ? View.GONE : View.VISIBLE);

        //Ui testing Code, idle resource

    }

    private void setRecyclerViewLayout() {
        mRecipesRecyclerView.setVisibility(View.GONE);
        mRecipesRecyclerView.setHasFixedSize(true);

        boolean twoPaneMode = getResources().getBoolean(R.bool.twoPaneMode);
        if (twoPaneMode) {

        } else {
            mRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        }

    }


    private void fetchRecipeData() {
        // Set SwipeRefreshLayout that refreshing in case that loadRecipes get called by the networkChangeReceiver
        if (NetworkUtils.isNetworkAvailable(getActivity().getApplicationContext())) {
            mPullNRefresh.setRefreshing(true);

            RecipesApi.getInstance().getRecipes(new RecipesApi.RecipesApiCallback<List<Recipe>>() {
                @Override
                public void onResponse(final List<Recipe> result) {
                    if (result != null) {
                        mRecipes = result;
                        mRecipesRecyclerView.setAdapter(new RecipesAdapter(getActivity().getApplicationContext(), mRecipes, new ItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                mClickListener.onRecipeSelected(mRecipes.get(position));
                            }
                        }));
                        //Widget Code goes here

                    } else {
                        NetworkUtils.createSnackBar(getActivity(), getView(), getString(R.string.failed_to_fetch), true);
                    }
                    checkLoadedDataAndChangeLayout();

                }

                @Override
                public void onCancel() {
                    //Check for loaded data
                    checkLoadedDataAndChangeLayout();
                }

            });
        } else {
            NetworkUtils.createSnackBar(getActivity(), getView(), getString(R.string.no_internet), true);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RecipeClickListener) {
            mClickListener = (com.cmpny.wca.bakingapp.Common.RecipeClickListener.OnRecipeClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " Fragment should implement RecipeClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mClickListener = null;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        Log.d(TAG,"onDestroyView Called");
    }

    private final BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mRecipes == null) {
                //Add Method TO Implement
                fetchRecipeData();
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();

        getActivity().registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();

        getActivity().unregisterReceiver(networkChangeReceiver);
    }


}

