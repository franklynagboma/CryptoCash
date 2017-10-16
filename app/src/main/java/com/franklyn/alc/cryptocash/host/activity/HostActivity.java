package com.franklyn.alc.cryptocash.host.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.franklyn.alc.cryptocash.R;
import com.franklyn.alc.cryptocash.app.AppController;
import com.franklyn.alc.cryptocash.calculate.fragment.CalculateFragment;
import com.franklyn.alc.cryptocash.constant.CryptoInterface;
import com.franklyn.alc.cryptocash.crypto_card.fragment.CryptoCardFragment;
import com.franklyn.alc.cryptocash.host.pojo.CashValue;
import com.franklyn.alc.cryptocash.host.presenter.Presenter;

import java.util.ArrayList;

import butterknife.ButterKnife;


public class HostActivity extends AppCompatActivity
        implements CryptoInterface.PresenterToHost, CryptoCardFragment.SendResponse{


    private final String LOG_TAG = HostActivity.class.getSimpleName();
    private CryptoCardFragment cryptoCardFragment;
    private final String CRYPTO_TAG = "crypto_tag";
    private CalculateFragment calculateFragment;
    private final String CALCULATE_TAG = "calculate_tag";
    private CryptoInterface.HostToPresenter hostToPresenter;
    private Presenter presenter;
    private String currentFragment ="";
    private final String CURRENT_FRAGMENT = "";
    private String cryptoName ="", countryName ="", cashSymbol ="", cashValue ="";
    private final String CRYPTO_NAME = "crypto", COUNTRY_NAME ="country",
            CASH_SYMBOL ="symbol", CASH_VALUE ="cash";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        ButterKnife.bind(this);
        presenter = new Presenter(this, this);
        setHostToPresenter(presenter);
        cryptoCardFragment = new CryptoCardFragment();
        calculateFragment = new CalculateFragment();

    }

    public void setHostToPresenter(CryptoInterface.HostToPresenter
                                           hostToPresenter) {
        this.hostToPresenter = hostToPresenter;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(CURRENT_FRAGMENT, currentFragment);
        outState.putString(CRYPTO_NAME, cryptoName);
        outState.putString(COUNTRY_NAME, countryName);
        outState.putString(CASH_SYMBOL, cashSymbol);
        outState.putString(CASH_VALUE, cashValue);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentFragment = savedInstanceState.getString(CURRENT_FRAGMENT, "");
        if(currentFragment.equals(CRYPTO_TAG))
            callCryptoFragment();
        if(currentFragment.equals(CALCULATE_TAG)) {
            cryptoName = savedInstanceState.getString(CRYPTO_NAME, "");
            countryName = savedInstanceState.getString(COUNTRY_NAME, "");
            cashSymbol = savedInstanceState.getString(CASH_SYMBOL, "");
            cashValue = savedInstanceState.getString(CASH_VALUE, "");
            sendToHostActivity(cryptoName, countryName, cashSymbol, cashValue);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(currentFragment.isEmpty()){
            //get cashValue list;
            AppController.getInstance().startProgress("Loading...", this);
            presenter.getUSDToCountryCash();
        }
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
        callCryptoFragment();
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
        callCryptoFragment();
    }

    private void callCryptoFragment(){
        if(null == getSupportFragmentManager().findFragmentByTag(CRYPTO_TAG)){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    cryptoCardFragment, CRYPTO_TAG).commit();
            currentFragment = CRYPTO_TAG;
        }
    }

    @Override
    public void sendToHostActivity(String cryptoName, String countryName,
                                   String cashSymbol, String cashValue) {
        if(null == getSupportFragmentManager().findFragmentByTag(CALCULATE_TAG)) {
            this.cryptoName = cryptoName;
            this.countryName = countryName;
            this.cashSymbol = cashSymbol;
            this.cashValue = cashValue;
            Bundle bundle = new Bundle();
            bundle.putString("crypto", cryptoName);
            bundle.putString("country", countryName);
            bundle.putString("symbol", cashSymbol);
            bundle.putString("cash", cashValue);
            calculateFragment.setArguments(bundle);
            Log.i(LOG_TAG, "show calculateFragment");
            //call calculateFragment
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    calculateFragment, CALCULATE_TAG).commit();
            currentFragment = CALCULATE_TAG;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //if calculate fragment was up, show cryptoCardFragment
        if(currentFragment.equals(CALCULATE_TAG))
            callCryptoFragment();
        else
            this.finish();
    }
}
