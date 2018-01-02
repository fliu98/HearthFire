package com.fliu98.hearthfire;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.util.Log;
import android.util.SparseArray;

import com.fliu98.hearthfire.model.Card;

import java.util.Comparator;
import java.util.TreeSet;

/**
 * Cache for card data.
 */
class DataCache {
    private static final String LOG_TAG = DataCache.class.getSimpleName();
    private static DataCache sInstance;
    private static SparseArray<TreeSet<Card>> sCardsByClass;
    private static SparseArray<TreeSet<Card>> sCardsByType;
    private static SparseArray<TreeSet<Card>> sCardsByMana;
    private static SparseArray<Card> sCardsById;

    private DataCache() {}

    static void initialize(Context context) {
        Log.d(LOG_TAG, "Initializing DataCache.");
        if (sInstance != null) {
            return;
        }
        sInstance = new DataCache();
        populateCache(context);
    }

    private static void populateCache(Context context) {
        sCardsByClass = new SparseArray<>();
        sCardsByType = new SparseArray<>();
        sCardsByMana = new SparseArray<>();
        sCardsById = new SparseArray<>();
        Resources res = context.getResources();
        XmlResourceParser parser = res.getXml(R.xml.cards);
        int xmlEvent;
        try {
            while ((xmlEvent = parser.next()) != XmlResourceParser.END_DOCUMENT) {
                if (xmlEvent == XmlResourceParser.START_TAG
                        && context.getString(R.string.card).equals(parser.getName())) {
                    Card card = buildCard(res, parser, context);
                    insertCardByClass(card);
                    insertCardByType(card);
                    insertCardByMana(card);
                    sCardsById.put(card.id, card);
                }
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Cannot populate DataCache.", e);
        }
    }

    private static Card buildCard(Resources res, XmlResourceParser parser, Context context) {
        Card card = new Card();
        TypedArray arr = res.obtainAttributes(parser, R.styleable.card);
        card.id = arr.getInt(R.styleable.card_id, 0);
        card.type = Card.Type.values()[arr.getInt(R.styleable.card_type, 0)];
        card.mana = arr.getInt(R.styleable.card_mana, 0);
        card.heroClass = Card.HeroClass.values()[arr.getInt(R.styleable.card_hero_class, 0)];
        card.name = arr.getString(R.styleable.card_name);
        String resourceName = arr.getString(R.styleable.card_res_name);
        card.resId = res.getIdentifier(resourceName, "drawable", context.getPackageName());
        card.legendary = arr.getBoolean(R.styleable.card_legendary, false);
        arr.recycle();
        return card;
    }

    private static class AlphabeticalComparator implements Comparator<Card> {
        @Override
        public int compare(Card card, Card t1) {
            return card.name.compareTo(t1.name);
        }
    }

    private static void insertCardByClass(Card card) {
        TreeSet<Card> cards = sCardsByClass.get(card.heroClass.numValue);
        if (cards == null) {
            cards = new TreeSet<>(new AlphabeticalComparator());
        }
        cards.add(card);
        sCardsByClass.put(card.heroClass.numValue, cards);
    }

    private static void insertCardByType(Card card) {
        TreeSet<Card> cards = sCardsByType.get(card.type.numValue);
        if (cards == null) {
            cards = new TreeSet<>(new AlphabeticalComparator());
        }
        cards.add(card);
        sCardsByType.put(card.type.numValue, cards);
    }

    private static void insertCardByMana(Card card) {
        TreeSet<Card> cards = sCardsByMana.get(card.mana);
        if (cards == null) {
            cards = new TreeSet<>(new AlphabeticalComparator());
        }
        cards.add(card);
        sCardsByMana.put(card.mana, cards);
    }

    public static TreeSet<Card> getCardsByClass(Card.HeroClass heroClass) {
        TreeSet<Card> cards = sCardsByClass.get(heroClass.numValue);
        return cards == null ? new TreeSet<Card>() : new TreeSet<>(cards);
    }

    public static TreeSet<Card> getCardsByType(Card.Type type) {
        TreeSet<Card> cards = sCardsByType.get(type.numValue);
        return cards == null ? new TreeSet<Card>() : new TreeSet<>(cards);
    }

    public static TreeSet<Card> getCardsByMana(int mana) {
        TreeSet<Card> cards = sCardsByMana.get(mana);
        return cards == null ? new TreeSet<Card>() : new TreeSet<>(cards);
    }

    public static Card getCardById(int id) {
        return sCardsById.get(id);
    }
}
