package com.fliu98.hearthfire;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fliu98.hearthfire.model.DeckInfo;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeckViewFragment extends DialogFragment {

    private static final String EXTRA_DECK_INFO = "extra_deck_info";
    private CardViewAdapter mAdapter;

    public static DeckViewFragment getInstance(DeckInfo deckInfo) {
        DeckViewFragment fragment = new DeckViewFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_DECK_INFO, deckInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_deck_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        DeckInfo deckInfo = args.getParcelable(EXTRA_DECK_INFO);

        mAdapter = new CardViewAdapter(deckInfo.deckList, getContext());

        RecyclerView rv = view.findViewById(R.id.deck_view_grid);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        rv.setAdapter(mAdapter);
        rv.setLayoutManager(layoutManager);

        if (mAdapter.getItemCount() == 0) {
            view.findViewById(R.id.empty_text).setVisibility(View.VISIBLE);
        }
    }
}
