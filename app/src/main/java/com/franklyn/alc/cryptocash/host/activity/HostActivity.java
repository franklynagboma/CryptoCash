package com.franklyn.alc.cryptocash.host.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.franklyn.alc.cryptocash.R;
import com.franklyn.alc.cryptocash.app.AppController;
import com.franklyn.alc.cryptocash.calculate.fragment.CalculateFragment;
import com.franklyn.alc.cryptocash.connection.ConnectionReceiver;
import com.franklyn.alc.cryptocash.constant.CryptoInterface;
import com.franklyn.alc.cryptocash.crypto_card.fragment.CryptoCardFragment;
import com.franklyn.alc.cryptocash.host.pojo.CashValue;
import com.franklyn.alc.cryptocash.host.presenter.Presenter;

import java.util.ArrayList;

import butterknife.BindView;
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
    private final int NETWORK_CODE = 100;
    private MaterialDialog build;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        if(!ConnectionReceiver.isConnected())
            showConnectionDialog();

        if(!currentFragment.equals(CRYPTO_TAG)){
            //get cashValue list;
            AppController.getInstance().startProgress("Loading...", this);
            presenter.getUSDToCountryCash();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(null != build && build.isShowing())
            build.dismiss();
    }

    private void showConnectionDialog() {
        View settings = LayoutInflater.from(this).inflate(R.layout.settings_dialog, null, false);
        TextView settingsTxt = (TextView) settings.findViewById(R.id.settings);
        settingsTxt.setTypeface(AppController.getSegoeNormal(this));

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
        builder.autoDismiss(true)
                .customView(settings, true)
                .backgroundColorRes(R.color.item_color);
        builder.positiveText("Settings")
                .positiveColorRes(R.color.colorPrimaryDark)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                        if(intent.resolveActivity(HostActivity.this.getPackageManager()) != null)
                            startActivityForResult(intent, NETWORK_CODE);
                    }
                });
        builder.negativeText("Cancel")
                .negativeColorRes(R.color.colorPrimary)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                });
        build = builder.build();
        build.setCanceledOnTouchOutside(true);
        build.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menuItem = menu.findItem(R.id.action_refresh);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == android.R.id.home){
            onBackPressed();
        }
        if (id == R.id.action_refresh) {
            if(!AppController.cashReceived) {
                //if connection is Okay, as user to check connection and refresh
                //else ask user to turn on internet connection.
                //call onResume to get Cash API and load Fragment
                onResume();
            }
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
    }

    private void callCryptoFragment(){
        if(null == getSupportFragmentManager().findFragmentByTag(CRYPTO_TAG)){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    cryptoCardFragment, CRYPTO_TAG).commit();
            currentFragment = CRYPTO_TAG;
            if(null != menuItem)
                menuItem.setVisible(true);
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
            if(null != menuItem)
                menuItem.setVisible(false);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == NETWORK_CODE && resultCode == RESULT_OK) {
            //call onResume to get Cash API and load Fragment
            onResume();
        }
        else {
            //check connection.
            if(!ConnectionReceiver.isConnected())
                showConnectionDialog();//show dialog
        }
    }
}
