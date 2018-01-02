package com.fliu98.hearthfire;

import com.fliu98.hearthfire.model.Card;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Filters cards according to parameters.
 */

public class CardFilter {
    private Card.HeroClass mHeroClass;
    private Card.Type mType;
    private Integer mMana;

    public CardFilter(Card.HeroClass heroClass) {
        mHeroClass = heroClass;
    }

    public void specifyType(Card.Type type) {
        mType = type;
    }

    public void specifyMana(int mana) {
        mMana = mana;
    }

    public ArrayList<Card> getCards() {
        TreeSet<Card> cards = DataCache.getCardsByClass(mHeroClass);
        cards.addAll(DataCache.getCardsByClass(Card.HeroClass.NEUTRAL));
        if (mType != null) {
            cards.retainAll(DataCache.getCardsByType(mType));
        }
        if (mMana != null) {
            cards.retainAll(DataCache.getCardsByMana(mMana));
        }
        return new ArrayList<>(cards);
    }
}
