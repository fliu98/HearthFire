package com.fliu98.hearthfire;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fliu98.hearthfire.model.Card;
import com.fliu98.hearthfire.model.Deck;

import java.util.ArrayList;

/**
 * Adapter for the card view.
 */

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.CardViewHolder> {

    private ArrayList<Card> mCards;
    private SparseIntArray mDeckList;
    private Context mContext;
    private boolean mIsDeckView;
    private int mDeckCount;

    CardViewAdapter(ArrayList<Card> cards, Context context) {
        mCards = cards;
        mDeckList = new SparseIntArray();
        mContext = context;
        mIsDeckView = false;
        mDeckCount = 0;
    }

    CardViewAdapter(ArrayList<Card> cards, SparseIntArray deckList, Context context) {
        mCards = cards;
        mDeckList = deckList;
        mContext = context;
        mIsDeckView = false;
        mDeckCount = Deck.deckListToArray(deckList).size();
    }

    CardViewAdapter(SparseIntArray deckList, Context context) {
        mCards = deriveCardsFromDeckList(deckList);
        mDeckList = deckList;
        mContext = context;
        mIsDeckView = true;
        mDeckCount = Deck.deckListToArray(deckList).size();
    }

    private ArrayList<Card> deriveCardsFromDeckList(SparseIntArray deckList) {
        ArrayList<Card> cards = new ArrayList<>();
        for (int i = 0; i < deckList.size(); i++) {
            cards.add(DataCache.getCardById(deckList.keyAt(i)));
        }
        return cards;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.layout_card_item, parent, false);
        return new CardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        Card card = mCards.get(position);
        holder.imageView.setImageResource(card.resId);
        holder.cardCount.setText(String.valueOf(mDeckList.get(card.id, 0)));
    }

    @Override
    public int getItemCount() {
        return mCards.size();
    }

    public void setCards(ArrayList<Card> cards) {
        mCards = cards;
        notifyDataSetChanged();
    }

    public SparseIntArray getDeckList() {
        return mDeckList;
    }

    private void onAddToDeck(int i) {
        if (mDeckCount == 30) {
            return;
        }
        Card card = mCards.get(i);
        int count = mDeckList.get(card.id, 0);
        int max = card.legendary ? 1 : 2;
        if (count < max) {
            count++;
            mDeckCount++;
        }
        mDeckList.put(card.id, count);
        notifyItemChanged(i);
    }

    private void onRemoveFromDeck(int i) {
        Card card = mCards.get(i);
        int count = mDeckList.get(card.id, 0);
        if (count > 0) {
            count--;
            mDeckCount--;
        }
        if (count == 0) {
            mDeckList.delete(card.id);
        } else {
            mDeckList.put(card.id, count);
        }
        notifyItemChanged(i);
    }

    public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        ImageView addButton;
        ImageView removeButton;
        TextView cardCount;

        CardViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.card_view);
            addButton = itemView.findViewById(R.id.add_button);
            removeButton = itemView.findViewById(R.id.remove_button);
            cardCount = itemView.findViewById(R.id.card_count_view);
            if (mIsDeckView) {
                addButton.setVisibility(View.GONE);
                removeButton.setVisibility(View.GONE);
            } else {
                addButton.setOnClickListener(this);
                removeButton.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View view) {
            if (view.equals(addButton)) {
                onAddToDeck(getAdapterPosition());
            } else {
                onRemoveFromDeck(getAdapterPosition());
            }
        }
    }
}