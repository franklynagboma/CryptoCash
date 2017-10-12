package com.franklyn.alc.cryptocash.constant;

import com.franklyn.alc.cryptocash.scrolling.pojo.CashValue;

import java.util.ArrayList;

/**
 * Created by AGBOMA franklyn on 10/10/17.
 */

public interface CryptoInterface {

    //ScrollingActivity
    interface ScrollingToPresenter {
        void getUSDToCountryCash();
    }
    interface PresenterToScrolling {
        void sendCashReceived();
        void sendList(ArrayList<CashValue> cashValueList);
        void sendError(String error);
    }
    interface ModelToPresenterScrolling {
        void cashReceived(String error);
    }
}
