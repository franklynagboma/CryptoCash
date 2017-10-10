package com.franklyn.alc.cryptocash.crypto_card.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.franklyn.alc.cryptocash.R;
import com.franklyn.alc.cryptocash.add_card.AddCardFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by AGBOMA franklyn on 10/10/17.
 */

public class CryptoCardFragment extends Fragment implements AddCardFragment.SendAddContent,
        LoaderManager.LoaderCallbacks<Cursor>{


    private final String LOG_TAG = CryptoCardFragment.class.getSimpleName();
    private Context context;
    private AddCardFragment addCardFragment;
    private final String ADD_CARD = "add_card_fragment";
    private boolean isTab, isLand;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.items_list)
    RecyclerView itemList;

    public CryptoCardFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isTab = getResources().getBoolean(R.bool.isTab);
        isLand = getResources().getBoolean(R.bool.isLand);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View cardItems = inflater.inflate(R.layout.fragment_crypto_card, container, false);
        ButterKnife.bind(this, cardItems);

        context = getActivity();
        itemList.setHasFixedSize(true);
        if(!isTab)
            itemList.setLayoutManager(new GridLayoutManager(context, 2));
        else {
            if(isLand)
                itemList.setLayoutManager(new GridLayoutManager(context, 3));
            else
                itemList.setLayoutManager(new GridLayoutManager(context, 5));
        }

        addCardFragment = new AddCardFragment();
        addCardFragment.setSend(this);

        return cardItems;

    }

    @OnClick(R.id.fab)
    public void onFabClicked(){
        //show dialogFragment
        addCardFragment.show(getChildFragmentManager(),ADD_CARD);
    }

    @Override
    public void sendContent(String crypto, String country) {
        Log.i(LOG_TAG, "Seen:" +crypto +" "+ country);
    }


    //loader
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
