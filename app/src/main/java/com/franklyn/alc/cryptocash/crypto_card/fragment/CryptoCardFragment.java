package com.franklyn.alc.cryptocash.crypto_card.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.franklyn.alc.cryptocash.R;
import com.franklyn.alc.cryptocash.add_card.AddCardFragment;
import com.franklyn.alc.cryptocash.app.AppController;
import com.franklyn.alc.cryptocash.crypto_card.adapter.CustomAdapter;
import com.franklyn.alc.cryptocash.db_lite.CryptoContract;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by AGBOMA franklyn on 10/10/17.
 */

public class CryptoCardFragment extends Fragment implements AddCardFragment.SendAddContent{


    private final String LOG_TAG = CryptoCardFragment.class.getSimpleName();
    private Context context;
    private AddCardFragment addCardFragment;
    private final String ADD_CARD = "add_card_fragment";
    private boolean isTab, isLand;
    private CustomAdapter customAdapter;
    /*@BindView(R.id.app_name)
    TextView appName;*/
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.items_list)
    RecyclerView itemList;

    public CryptoCardFragment() {
    }

    private SendResponse send;
    public interface SendResponse{
        void sendToHostActivity(String cryptoName, String countryName,
                                String cashSymbol, String cashValue);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            send = (SendResponse) context;
        }
        catch (ClassCastException i){
            throw new ClassCastException(context.toString());
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isTab = getResources().getBoolean(R.bool.isTab);
        isLand = getResources().getBoolean(R.bool.isLand);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View cardItems = inflater.inflate(R.layout.fragment_crypto_card, container, false);
        ButterKnife.bind(this, cardItems);

        context = getActivity();
        //appName.setTypeface(AppController.getSegoeBold(context));

        itemList.setHasFixedSize(true);
        if(!isTab)
            itemList.setLayoutManager(new GridLayoutManager(context, 2));
        else {
            if(!isLand)
                itemList.setLayoutManager(new GridLayoutManager(context, 3));
            else
                itemList.setLayoutManager(new GridLayoutManager(context, 4));
        }

        customAdapter = new CustomAdapter(
                context,
                this,
                CryptoContract.CardAdded.CONTENT_URI,
                CryptoContract.CardAdded.PROJECTIONS,
                CryptoContract.CardAdded.SORT_ORDER);

        addCardFragment = new AddCardFragment();
        addCardFragment.setSend(this);

        itemList.setAdapter(customAdapter);

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
        ContentValues contentValues = new ContentValues();
        contentValues.put(CryptoContract.CardAdded.CRYPTO_TYPE, crypto);
        contentValues.put(CryptoContract.CardAdded.COUNTRY_TYPE, country);
        context.getContentResolver().insert(CryptoContract.CardAdded.CONTENT_URI, contentValues);
        contentValues.clear();
        //reinitialize adapter for new data to be visible to user.
        customAdapter = new CustomAdapter(
                context,
                this,
                CryptoContract.CardAdded.CONTENT_URI,
                CryptoContract.CardAdded.PROJECTIONS,
                CryptoContract.CardAdded.SORT_ORDER);

        itemList.setAdapter(customAdapter);
    }

    public void getClickedContent(String cryptoName, String countryName,
                                  String cashSymbol, String cashValue){
        send.sendToHostActivity(cryptoName, countryName, cashSymbol, cashValue);
    }

    public void getDBId(String id){}
}
