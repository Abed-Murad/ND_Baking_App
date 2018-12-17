package com.am.nd_baking_app.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.am.nd_baking_app.R;
import com.am.nd_baking_app.adapter.IngredientsAdapter;
import com.am.nd_baking_app.databinding.FragmentIngredientsBinding;

import java.util.ArrayList;

public class IngredientsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private FragmentIngredientsBinding mBinding;

    public IngredientsFragment() {
    }

    public static IngredientsFragment newInstance(String param1, String param2) {
        IngredientsFragment fragment = new IngredientsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentIngredientsBinding.inflate(inflater, container, false);
        mBinding.ingredientsRecyclerView.setAdapter(new IngredientsAdapter(getContext(), new ArrayList<>()));

        return mBinding.getRoot();
    }

}
