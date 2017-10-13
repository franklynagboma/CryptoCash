package com.franklyn.alc.cryptocash.crypto_card.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.franklyn.alc.cryptocash.R;
import com.franklyn.alc.cryptocash.app.AppController;
import com.franklyn.alc.cryptocash.crypto_card.fragment.CryptoCardFragment;
import com.franklyn.alc.cryptocash.db_lite.CryptoContract;

import java.util.ArrayList;

/**
 * Created by AGBOMA franklyn on 10/12/17.
 */

public class CustomAdapter extends RecyclerViewCursorAdapter<CustomAdapter.ItemHolder> {

    private final String LOG_TAG = CustomAdapter.class.getSimpleName();
    private Context context;
    private Cursor cursor;
    private CryptoCardFragment cryptoCardFragment;

    public CustomAdapter(Context context, CryptoCardFragment cryptoCardFragment, Uri cursorUri,
                         String[] projections, String sortOrder) {
        super(null);
        this.context = context;
        this.cryptoCardFragment = cryptoCardFragment;
        cursor = context.getContentResolver().query(cursorUri, projections, null, null, sortOrder);
        swapCursor(cursor);
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_items, parent, false);
        return new ItemHolder(view);
    }

    @Override
    protected void onBindViewHolder(final ItemHolder holder, Cursor cursor) {

        //get data ids from database should user want to delete card
        String getId = cursor.getString(cursor.getColumnIndex(CryptoContract.CardAdded._ID));
        Log.i(LOG_TAG, "ids: " +getId);

        String getCrypto = cursor.getString(cursor.getColumnIndex(
                CryptoContract.CardAdded.CRYPTO_TYPE));
        String getCountry = cursor.getString(cursor.getColumnIndex(
                CryptoContract.CardAdded.COUNTRY_TYPE));
        //add id
        holder.idNo.setText(getId);

        //set type face here

        //crypto and country name.
        holder.cryptoName.setText(getCrypto);
        holder.countryName.setText(getCountry);

        if(getCrypto.equalsIgnoreCase("btc"))
            holder.cryptoColour.setImageResource(R.drawable.ic_btc_arrow);
        if(getCrypto.equalsIgnoreCase("eth"))
            holder.cryptoColour.setImageResource(R.drawable.ic_eth_arrow);

        //Loop and compare country with list of respective cashValue
        for(String keyValue: AppController.cashValueList.keySet()){
            if(getCountry.toUpperCase().contains(keyValue)){
                String getAmountWithCommas = AppController.getInstance()
                        .getAmountFormat(String.valueOf(
                                AppController.cashValueList.get(keyValue)));
                holder.countryValue.setText(getAmountWithCommas);
                break;
            }
        }

        //when clicked.
        holder.hostCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG_TAG, "onClicked");
                //open conversion screen.
                cryptoCardFragment.getClickedContent(holder.cryptoName.getText().toString(),
                        holder.countryName.getText().toString(),
                        holder.countryValue.getText().toString());
            }
        });
        //when long pressed.
        holder.hostCard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.i(LOG_TAG, "onLongClicked");
                //return id number
                cryptoCardFragment.getDBId(holder.idNo.getText().toString());
                //pop up dialog to delete card and context from database.
                return true;
            }
        });
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        private RelativeLayout hostCard;
        private TextView idNo, cryptoNo, cryptoName, countryName, countryValue;
        private ImageView cryptoColour;

        public ItemHolder(View itemView) {
            super(itemView);
            hostCard = (RelativeLayout) itemView.findViewById(R.id.host_card);
            idNo = (TextView) itemView.findViewById(R.id.id_no);
            cryptoNo = (TextView) itemView.findViewById(R.id.crypto_no);
            cryptoName = (TextView) itemView.findViewById(R.id.crypto_name);
            cryptoColour = (ImageView) itemView.findViewById(R.id.crypto_colour);
            countryName = (TextView) itemView.findViewById(R.id.country_name);
            countryValue = (TextView) itemView.findViewById(R.id.country_value);
        }
    }
}
