package com.fliu98.hearthfire;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fliu98.hearthfire.model.Deck;
import com.fliu98.hearthfire.model.DeckInfo;
import com.fliu98.hearthfire.server.ServerUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeckSaveFragment extends Fragment implements ServerUtils.OnDeckSaveListener {

    private static final String EXTRA_DECK_INFO = "extra_deck_info";

    private EditText mTitle;
    private EditText mDescription;
    private Button mSaveButton;
    private ProgressBar mProgressBar;

    public static DeckSaveFragment getInstance(DeckInfo deckInfo) {
        DeckSaveFragment f = new DeckSaveFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_DECK_INFO, deckInfo);
        f.setArguments(args);
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_deck_save, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        final DeckInfo deckInfo = args.getParcelable(EXTRA_DECK_INFO);

        mTitle = view.findViewById(R.id.deck_title);
        mDescription = view.findViewById(R.id.deck_description);
        mTitle.setText(deckInfo.name == null ? "" : deckInfo.name);
        mDescription.setText(deckInfo.description == null ? "" : deckInfo.description);
        final int deckCount = Deck.deckListToArray(deckInfo.deckList).size();

        mProgressBar = view.findViewById(R.id.loading_progress_bar);

        mSaveButton = view.findViewById(R.id.button_save_deck);
        mSaveButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String title = mTitle.getText().toString();
                String description = mDescription.getText().toString();
                if (TextUtils.isEmpty(title)) {
                    Toast.makeText(getContext(),
                            getString(R.string.empty_title), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (deckCount != 30) {
                    Toast.makeText(getContext(),
                            getString(R.string.incomplete_deck), Toast.LENGTH_SHORT).show();
                    return;
                }
                deckInfo.name = title;
                deckInfo.description = description;
                startUpload(deckInfo);
            }
        });
    }

    private void startUpload(DeckInfo deckInfo) {
        ServerUtils.uploadDeck(deckInfo, this);
        mSaveButton.setEnabled(false);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveSuccess() {
        Toast.makeText(getContext(), R.string.deck_save_success, Toast.LENGTH_SHORT).show();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStack();
        ((DialogFragment) getParentFragment()).dismiss();
    }

    @Override
    public void onSaveFailure() {
        Toast.makeText(getContext(), R.string.deck_save_failed, Toast.LENGTH_SHORT).show();
        mSaveButton.setEnabled(true);
        mProgressBar.setVisibility(View.GONE);
    }
}
