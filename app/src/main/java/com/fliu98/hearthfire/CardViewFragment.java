package com.fliu98.hearthfire;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fliu98.hearthfire.model.Card;
import com.fliu98.hearthfire.model.DeckInfo;

/**
 * Fragment to display cards.
 */
public class CardViewFragment extends Fragment {

    private static final String EXTRA_HERO_CLASS = "extra_hero_class";
    private static final String EXTRA_DECK_INFO = "extra_deck_info";
    private CardFilter mFilter;
    private CardViewAdapter mAdapter;

    public static CardViewFragment getInstance(int heroClassInt, DeckInfo deck) {
        CardViewFragment fragment = new CardViewFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_HERO_CLASS, heroClassInt);
        args.putParcelable(EXTRA_DECK_INFO, deck);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_card_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        final int heroClass = args.getInt(EXTRA_HERO_CLASS);
        final DeckInfo deckInfo = args.getParcelable(EXTRA_DECK_INFO);

        mFilter = new CardFilter(Card.HeroClass.values()[heroClass]);
        if (mAdapter == null) {
            if (deckInfo == null) {
                // Create a new deck
                mAdapter = new CardViewAdapter(mFilter.getCards(), getContext());
            } else {
                // Display saved deck
                mAdapter = new CardViewAdapter(
                        mFilter.getCards(), deckInfo.deckList, getContext());
            }
        }

        RecyclerView rv = view.findViewById(R.id.card_view_grid);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        rv.setAdapter(mAdapter);
        rv.setLayoutManager(layoutManager);

        FloatingActionButton fab = view.findViewById(R.id.view_deck_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (deckInfo == null) {
                    DeckInfo editDeckInfo = new DeckInfo();
                    editDeckInfo.updateDeckList(mAdapter.getDeckList());
                    editDeckInfo.heroClass = heroClass;
                    DeckPageFragment f = DeckPageFragment.getInstance(editDeckInfo);
                    f.show(getActivity().getSupportFragmentManager(), null);
                } else {
                    deckInfo.updateDeckList(mAdapter.getDeckList());
                    DeckPageFragment f = DeckPageFragment.getInstance(deckInfo);
                    f.show(getActivity().getSupportFragmentManager(), null);
                }
            }
        });

        if (mAdapter.getItemCount() == 0) {
            view.findViewById(R.id.empty_text).setVisibility(View.VISIBLE);
        }
    }
}
