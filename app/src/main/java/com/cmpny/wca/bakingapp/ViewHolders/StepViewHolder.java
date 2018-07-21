package com.cmpny.wca.bakingapp.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cmpny.wca.bakingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.steps_text)
    public TextView mTvStepOrder;

    @BindView(R.id.step_name_text)
    public TextView mTvStepName;

    public StepViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

}