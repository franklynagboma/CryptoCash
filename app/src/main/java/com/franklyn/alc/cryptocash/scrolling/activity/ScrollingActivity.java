package com.franklyn.alc.cryptocash.scrolling.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.franklyn.alc.cryptocash.R;
import com.franklyn.alc.cryptocash.app.AppController;
import com.franklyn.alc.cryptocash.constant.CryptoInterface;
import com.franklyn.alc.cryptocash.crypto_card.fragment.CryptoCardFragment;
import com.franklyn.alc.cryptocash.scrolling.pojo.CashValue;
import com.franklyn.alc.cryptocash.scrolling.presenter.Presenter;

import java.util.ArrayList;

import butterknife.ButterKnife;


public class ScrollingActivity extends AppCompatActivity
        implements CryptoInterface.PresenterToScrolling{


    private final String LOG_TAG = ScrollingActivity.class.getSimpleName();
    private final String FRAGMENT_TAG = "fragment";
    private CryptoCardFragment cryptoCardFragment;
    private CryptoInterface.ScrollingToPresenter scrollingToPresenter;
    private Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        ButterKnife.bind(this);
        presenter = new Presenter(this, this);
        setScrollingToPresenter(presenter);
        cryptoCardFragment = new CryptoCardFragment();

    }

    public void setScrollingToPresenter(CryptoInterface.ScrollingToPresenter
                                                scrollingToPresenter) {
        this.scrollingToPresenter = scrollingToPresenter;
    }


    @Override
    protected void onResume() {
        super.onResume();
        //get cashValue list;
        AppController.getInstance().startProgress("Loading...", this);
        presenter.getUSDToCountryCash();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void sendCashReceived() {
        AppController.getInstance().stopProgress();
        //call fragment to show list
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                cryptoCardFragment, FRAGMENT_TAG).commit();
    }

    @Override
    public void sendList(ArrayList<CashValue> cashValueList) {
    }

    @Override
    public void sendError(String error) {
        /*if(error.equalsIgnoreCase("Please refresh page"){
            //call app refresh to call getUSDToCountryCash().
        }*/
        AppController.getInstance().stopProgress();
        AppController.getInstance().toastMsg(this, error);
    }
}
