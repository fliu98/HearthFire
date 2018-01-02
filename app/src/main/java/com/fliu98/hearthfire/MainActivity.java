package com.fliu98.hearthfire;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.MenuItem;

import com.fliu98.hearthfire.model.Card;
import com.fliu98.hearthfire.server.ServerUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private DrawerLayout mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        mDrawer = findViewById(R.id.drawer_layout);

//        ft.replace(R.id.content_frame, CardViewFragment
//                .getInstance(Card.HeroClass.MAGE.numValue, null));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (item.getItemId()) {
            case R.id.main_landing:
                break;
            case R.id.my_decks:
                ft.replace(R.id.content_frame, DeckListFragment.getInstance());
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.create_deck:
                ft.replace(R.id.content_frame, NewDeckFragment.getInstance());
                ft.addToBackStack(null);
                ft.commit();
                break;
            default:
                break;
        }

        mDrawer.closeDrawers();
        return true;
    }
}
