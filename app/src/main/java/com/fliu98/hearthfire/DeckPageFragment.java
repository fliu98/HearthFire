package com.fliu98.hearthfire;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fliu98.hearthfire.model.DeckInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeckPageFragment extends DialogFragment {

    private static final String EXTRA_DECK_INFO = "extra_deck_info";

    private ViewPager mPager;
    private DeckPagerAdapter mPagerAdapter;

    public static DeckPageFragment getInstance(DeckInfo deck) {
        DeckPageFragment f = new DeckPageFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_DECK_INFO, deck);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_deck_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        DeckInfo deckInfo = args.getParcelable(EXTRA_DECK_INFO);

        mPager = view.findViewById(R.id.deck_view_pager);
        mPagerAdapter = new DeckPagerAdapter(getChildFragmentManager());

        mPagerAdapter.addFragment(DeckViewFragment
                .getInstance(deckInfo), getString(R.string.deck));
        mPagerAdapter.addFragment(DeckSaveFragment
                .getInstance(deckInfo), getString(R.string.info));

        mPager.setAdapter(mPagerAdapter);

        TabLayout titleTabs = view.findViewById(R.id.pager_title_tabs);
        titleTabs.setupWithViewPager(mPager);
    }

    private class DeckPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mTitleList = new ArrayList<>();

        DeckPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mTitleList.add(title);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);
        }
    }
}
