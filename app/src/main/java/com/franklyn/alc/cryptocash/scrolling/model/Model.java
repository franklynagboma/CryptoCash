package com.franklyn.alc.cryptocash.scrolling.model;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.franklyn.alc.cryptocash.app.AppController;
import com.franklyn.alc.cryptocash.constant.CryptoInterface;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by AGBOMA franklyn on 10/10/17.
 */

public class Model {

    private final String LOG_TAG = Model.class.getSimpleName();
    private Context context;
    private CryptoInterface.ModelToPresenterScrolling modelToPresenterScrolling;

    public Model() {
    }

    public Model(Context context) {
        this.context = context;
    }

    public void setModelToPresenterScrolling(CryptoInterface.ModelToPresenterScrolling
                                                    modelToPresenterScrolling) {
        this.modelToPresenterScrolling = modelToPresenterScrolling;
    }

    public void getCashValue() {
        //get new List
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                AppController.BASE_CASH_API, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    Log.i(LOG_TAG, "Cash response: " +response.toString());
                    JSONObject rates = response.getJSONObject("rates");
                    AppController.NGN = rates.getDouble("NGN");
                    AppController.GHS= rates.getDouble("GHS");
                    AppController.CNY= rates.getDouble("CNY");
                    AppController.EUR= rates.getDouble("EUR");
                    AppController.INR= rates.getDouble("INR");
                    AppController.JRY= rates.getDouble("JRY");
                    AppController.ZAR= rates.getDouble("ZAR");
                    AppController.KES= rates.getDouble("KES");
                    AppController.ARS= rates.getDouble("ARS");
                    AppController.AUD= rates.getDouble("AUD");
                    AppController.AOA= rates.getDouble("AOA");
                    AppController.XOF= rates.getDouble("XOF");
                    AppController.XAF= rates.getDouble("XAF");
                    AppController.GMD= rates.getDouble("GMD");
                    AppController.IQD= rates.getDouble("IQD");
                    AppController.JMD= rates.getDouble("JMD");
                    AppController.BRL= rates.getDouble("BRL");
                    AppController.LRD= rates.getDouble("LRD");

                    //if all goes well this line will be reached, the set cashReceived true.
                    AppController.cashReceived = true;
                    modelToPresenterScrolling.cashReceived();

                }
                catch (JSONException i){
                    i.printStackTrace();
                    modelToPresenterScrolling.getError("Error loading, please retry");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(LOG_TAG, "Error: " +error.getMessage());
                NetworkResponse networkResponse = error.networkResponse;
                if(null != networkResponse && null != networkResponse.data){
                    int statusCode = networkResponse.statusCode;
                    String statusError = String.valueOf(networkResponse.data);
                    modelToPresenterScrolling.getError(statusCode +" and "+ statusError);
                }
            }
        }) {

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                String requestCode = String.valueOf(response.statusCode);
                Log.i(LOG_TAG, "successful, requestCode: " + requestCode);
                return super.parseNetworkResponse(response);
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10000, 3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(request, AppController.TAG);
    }
}
