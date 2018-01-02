package com.fliu98.hearthfire;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fliu98.hearthfire.model.Deck;
import com.fliu98.hearthfire.model.DeckInfo;
import com.fliu98.hearthfire.server.ServerUtils;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeckListFragment extends Fragment implements ServerUtils.OnDeckGetListener{

    private ProgressBar mProgressBar;
    private DeckListAdapter mAdapter;

    public static DeckListFragment getInstance() {
        return new DeckListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_deck_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mProgressBar = view.findViewById(R.id.loading_progress_bar);
        toggleLoading(true);

        RecyclerView rv = view.findViewById(R.id.decks_recycler);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        mAdapter = new DeckListAdapter(new ArrayList<Deck>(), getContext());
        rv.setAdapter(mAdapter);
        rv.setLayoutManager(layoutManager);

        ServerUtils.getDecks(this);
    }

    private void toggleLoading(boolean loading) {
        mProgressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDecksReceived(ArrayList<Deck> decks) {
        mAdapter.setDecks(decks);
        toggleLoading(false);
    }

    private class DeckListAdapter extends RecyclerView.Adapter<DeckListAdapter.DeckViewHolder> {

        private ArrayList<Deck> mDecks;
        private Context mContext;

        DeckListAdapter(ArrayList<Deck> decks, Context context) {
            mDecks = decks;
            mContext = context;
        }

        @Override
        public DeckViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(mContext)
                    .inflate(R.layout.layout_deck_item, parent, false);
            return new DeckViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(DeckViewHolder holder, int position) {
            Deck deck = mDecks.get(position);
            holder.deckName.setText(deck.name);
            holder.deckDescription.setText(deck.description);
            switch (deck.hero_class) {
                case 1:
                    holder.classIcon.setImageResource(R.drawable.ic_druid);
                    break;
                case 2:
                    holder.classIcon.setImageResource(R.drawable.ic_hunter);
                    break;
                case 3:
                    holder.classIcon.setImageResource(R.drawable.ic_mage);
                    break;
                case 4:
                    holder.classIcon.setImageResource(R.drawable.ic_paladin);
                    break;
                case 5:
                    holder.classIcon.setImageResource(R.drawable.ic_priest);
                    break;
                case 6:
                    holder.classIcon.setImageResource(R.drawable.ic_rogue);
                    break;
                case 7:
                    holder.classIcon.setImageResource(R.drawable.ic_shaman);
                    break;
                case 8:
                    holder.classIcon.setImageResource(R.drawable.ic_warlock);
                    break;
                case 9:
                    holder.classIcon.setImageResource(R.drawable.ic_warrior);
                    break;
                default:
                    holder.classIcon.setImageResource(R.drawable.ic_druid);
            }
        }

        @Override
        public int getItemCount() {
            return mDecks.size();
        }

        private void setDecks(ArrayList<Deck> decks) {
            mDecks = decks;
            notifyDataSetChanged();
        }

        class DeckViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            ImageView classIcon;
            TextView deckName;
            TextView deckDescription;

            DeckViewHolder(View itemView) {
                super(itemView);
                classIcon = itemView.findViewById(R.id.class_icon);
                deckName = itemView.findViewById(R.id.deck_name);
                deckDescription = itemView.findViewById(R.id.deck_description);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                Deck deck = mDecks.get(getAdapterPosition());
                FragmentTransaction ft =  getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, CardViewFragment.getInstance(
                        deck.hero_class, DeckInfo.buildFromDeckModel(deck)));
                ft.addToBackStack(null);
                ft.commit();
            }
        }
    }
}
