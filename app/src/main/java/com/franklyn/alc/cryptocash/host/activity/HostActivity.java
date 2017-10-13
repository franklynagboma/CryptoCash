package com.franklyn.alc.cryptocash.host.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                cryptoCardFragment, CRYPTO_TAG).commit();
    }

    @Override
    public void sendToHostActivity(String cryptoName, String countryName, String cashValue) {
        if(null == getSupportFragmentManager().findFragmentByTag(CALCULATE_TAG)) {
            Bundle bundle = new Bundle();
            bundle.putString("crypto", cryptoName);
            bundle.putString("country", countryName);
            bundle.putString("cash", cashValue);
            calculateFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    calculateFragment, CALCULATE_TAG).commit();
        }
    }
}
