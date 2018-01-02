package com.fliu98.hearthfire;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import com.fliu98.hearthfire.model.Card;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewDeckFragment extends Fragment {


    public static NewDeckFragment getInstance() {
        return new NewDeckFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_deck, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GridLayout gridLayout = view.findViewById(R.id.class_grid);
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            gridLayout.getChildAt(i).setOnClickListener(new OnGridClickListener());
        }
    }

    private class OnGridClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int heroClass = 0;
            switch (view.getId()) {
                case R.id.druid:
                    heroClass = Card.HeroClass.DRUID.numValue;
                    break;
                case R.id.hunter:
                    heroClass = Card.HeroClass.HUNTER.numValue;
                    break;
                case R.id.mage:
                    heroClass = Card.HeroClass.MAGE.numValue;
                    break;
                case R.id.paladin:
                    heroClass = Card.HeroClass.PALADIN.numValue;
                    break;
                case R.id.priest:
                    heroClass = Card.HeroClass.PRIEST.numValue;
                    break;
                case R.id.rogue:
                    heroClass = Card.HeroClass.ROGUE.numValue;
                    break;
                case R.id.shaman:
                    heroClass = Card.HeroClass.SHAMAN.numValue;
                    break;
                case R.id.warlock:
                    heroClass = Card.HeroClass.WARLOCK.numValue;
                    break;
                case R.id.warrior:
                    heroClass = Card.HeroClass.WARRIOR.numValue;
                    break;
                default:
                    break;
            }
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, CardViewFragment.getInstance(heroClass, null));
            ft.addToBackStack(null);
            ft.commit();
        }
    }
}
