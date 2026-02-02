package com.ojire.sdk.opg;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

import org.json.JSONException;
import org.json.JSONObject;

public class WebAppInterface {
    Context mContext;

    WebAppInterface(Context c) {
        mContext = c;
    }

    /** This matches the function called from JavaScript */
    @JavascriptInterface
    public void postMessage(String jsonString) {
        System.out.println("Raw JSON string: "+jsonString);

    }
}
