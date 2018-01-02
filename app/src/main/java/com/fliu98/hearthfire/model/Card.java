package com.fliu98.hearthfire.model;

import android.support.annotation.NonNull;

/**
 * Card model.
 */
public class Card implements Comparable<Card> {

    @Override
    public int compareTo(@NonNull Card card) {
        return name.compareTo(card.name);
    }

    public enum Type {
        MINION(0), SPELL(1), WEAPON(2);
        public int numValue;
        Type(int numValue) {
            this.numValue = numValue;
        }
    }

    public enum HeroClass {
        NEUTRAL(0), DRUID(1), HUNTER(2), MAGE(3), PALADIN(4),
        PRIEST(5), ROGUE(6), SHAMAN(7), WARLOCK(8), WARRIOR(9);
        public int numValue;
        HeroClass(int numValue) {
            this.numValue = numValue;
        }
    }

    public int id;
    public Type type;
    public int mana;
    public HeroClass heroClass;
    public String name;
    public int resId;
    public boolean legendary;
}
