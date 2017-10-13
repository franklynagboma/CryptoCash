package com.franklyn.alc.cryptocash.constant;

import com.franklyn.alc.cryptocash.host.pojo.CashValue;

import java.util.ArrayList;

/**
 * Created by AGBOMA franklyn on 10/10/17.
 */

public interface CryptoInterface {

    //HostActivity
    interface HostToPresenter {
        void getUSDToCountryCash();
    }
    interface PresenterToHost {
        void sendCashReceived();
        void sendList(ArrayList<CashValue> cashValueList);
        void sendError(String error);
    }
    interface ModelToPresenterHost {
        void cashReceived(String error);
    }
}
