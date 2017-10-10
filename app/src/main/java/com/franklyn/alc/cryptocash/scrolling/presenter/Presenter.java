package com.franklyn.alc.cryptocash.scrolling.presenter;

import android.content.Context;

import com.franklyn.alc.cryptocash.app.AppController;
import com.franklyn.alc.cryptocash.constant.CryptoInterface;
import com.franklyn.alc.cryptocash.scrolling.activity.ScrollingActivity;
import com.franklyn.alc.cryptocash.scrolling.model.Model;

/**
 * Created by AGBOMA franklyn on 10/10/17.
 */

public class Presenter implements CryptoInterface.ScrollingToPresenter,
        CryptoInterface.ModelToPresenterScrolling {

    private final String LOG_TAG = Presenter.class.getSimpleName();
    private Context context;
    private CryptoInterface.PresenterToScrolling presenterToScrolling;
    private Model model;

    public Presenter() {
    }

    public Presenter(Context context, ScrollingActivity activity) {
        this.context = context;
        setPresenterToScrolling(activity);
        model = new Model(context);
        model.setModelToPresenterScrolling(this);

    }

    private void setPresenterToScrolling(CryptoInterface.PresenterToScrolling
                                                 presenterToScrolling) {
        this.presenterToScrolling = presenterToScrolling;
    }

    @Override
    public void getUSDToCountryCash() {
        //check if cashReceived is false,
        // this means the app was open for the first time ever or after activity destroys
        if(!AppController.cashReceived)
            model.getCashValue();
        else
            cashReceived();
    }

    @Override
    public void cashReceived() {
        //save all cash equivalent to 1 USD
        if(AppController.cashReceived)
            presenterToScrolling.sendCashReceived();
        else
            presenterToScrolling.sendError("Please refresh page");
    }

    @Override
    public void getError(String error) {

    }
}
