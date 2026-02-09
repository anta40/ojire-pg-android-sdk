package com.ojire.sdk.opg.model;

import android.util.Log;
import android.webkit.JavascriptInterface;

public class WebAppInterface {
    @JavascriptInterface
    public void processCurl(String method, String url, String headers, String body) {
        StringBuilder curl = new StringBuilder("curl -X " + method);

        // Headers are passed as a JSON string from JS
        curl.append(" ").append(headers);

        if (body != null && !body.isEmpty() && !body.equals("undefined")) {
            curl.append(" --data '").append(body).append("'");
        }

        curl.append(" \"").append(url).append("\"");

        Log.d("WebViewCurl", "GENERATED CURL: " + curl.toString());
    }
}
