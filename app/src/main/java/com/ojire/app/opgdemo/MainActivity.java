package com.ojire.app.opgdemo;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebMessage;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.webkit.JavaScriptReplyProxy;
import androidx.webkit.WebMessageCompat;
import androidx.webkit.WebViewCompat;

import com.ojire.sdk.opg.OPGAPIService;
import com.ojire.sdk.opg.OPGAPIClient;
import com.ojire.sdk.opg.OPGListener;
import com.ojire.sdk.opg.OPGWebView;
import com.ojire.sdk.opg.User;
import com.ojire.sdk.opg.PaymentRepository;
import com.ojire.sdk.opg.model.PaymenIntent;
import com.ojire.sdk.opg.model.PaymentIntentResponse;
import com.ojire.sdk.opg.model.PaymentMetadata;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements OPGListener {

    OPGWebView owv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        owv = findViewById(R.id.main_web_view);
        performGetToken();
    }

    @Override
    public void onSuccess(String url) {
        System.out.println("[SUCCESS] "+url);
    }

    @Override
    public void onPending(String url) {
        System.out.println("[PENDING] "+url);
    }

    @Override
    public void onFailed(String url) {
        System.out.println("[FAILED] "+url);
    }

    @Override
    public void onClose() {

    }

    public void performGetToken(){
        OPGAPIService service = OPGAPIClient.getAPIServicet();
        PaymenIntent param = new PaymenIntent();
        param.amount = 25000;
        param.currency = "IDR";
        param.customerId = "customer_test_234";
        param.description = "Test payment 234";
        param.merchantId = "949f9617-1333-4626-b29b-a049b45aa568";
        PaymentMetadata metadata = new PaymentMetadata();
        metadata.order_id = "order_234";
        param.metadata = metadata;

        PaymentRepository repo = new PaymentRepository();
        repo.doGetToken(param, new PaymentRepository.PaymentCallback() {
            @Override
            public void onSuccess(PaymentIntentResponse response) {
                Toast.makeText(getApplicationContext(), "Payment intent success: "+response.toString(), Toast.LENGTH_SHORT).show();
                System.out.println("Payment intent success: "+response.toString());
                System.out.println("Token: "+response.customerToken);
                JSONObject jsonData = new JSONObject();
                try {
                    jsonData.put("type", "INIT");
                    jsonData.put("clientSecret", response.clientSecret);
                    jsonData.put("token",response.customerToken);
                    jsonData.put("publicKey","pk_1769591280469729bd24176959128046990a6531e6a9fdf3cbd6");
                    jsonData.put("paymentId", response.id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //String webviewUrl = "https://pay-dev.ojire.com/pay/" + response.id;
                String webviewUrl = "https://pay-dev.ojire.com/pay";
                System.out.println("Payment webview url: "+webviewUrl);
                System.out.println("jsonData: "+jsonData.toString());
                //owv.loadUrl("https://www.detik.com");
                //owv.loadUrl(webviewUrl);
                //owv.postWebMessage(new WebMessage(jsonData.toString()), Uri.EMPTY);
//                WebSettings settings = owv.getSettings();
//                settings.setJavaScriptEnabled(true);
                //owv.loadUrl("https://ojire.technology/");
                WebSettings settings = owv.getSettings();
                settings.setJavaScriptEnabled(true);
                owv.loadUrl("https://pay-dev.ojire.com/pay");
                owv.setWebViewClient(new WebViewClient(){
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        System.out.println("Kelar load URL: "+url);
                        String PUBKEY = "pk_1769591280469729bd24176959128046990a6531e6a9fdf3cbd6";

                        String jsCode = String.format(
                                "const iframe = document.createElement('iframe');\n" +
                                        "iframe.id = '%s';\n" +
                                        "iframe.src = '%s';\n" +
                                        "iframe.setAttribute('allow', 'payment');\n" +
                                        "iframe.setAttribute('allow', 'clipboard-write');\n" +
                                        "iframe.addEventListener('load', () => {\n" +
                                        "    spinner.classList.add('hidden');\n" +
                                        "});\n" +
                                        "iframe.onload = () => {\n" +
                                        "    if (iframe.contentWindow) {\n" +
                                        "        iframe.contentWindow.postMessage({\n" +
                                        "            type: 'INIT',\n" +
                                        "            clientSecret: '%s',\n" +
                                        "            publicKey: '%s',\n" +
                                        "            token: '%s'\n" +
                                        "        }, '%s');\n" +
                                        "    }\n" +
                                        "};",
                                "1357",
                                "https://pay-dev.ojire.com/pay",
                                response.clientSecret,
                                PUBKEY,
                                response.customerToken,
                                "https://pay-dev.ojire.com/pay"
                        );

                        System.out.println("jsCode: "+jsCode);
                        owv.postWebMessage(new WebMessage(jsCode), Uri.parse("https://pay-dev.ojire.com/pay"));


                    }
                });
                //owv.loadUrl("https://pay-dev.ojire.com/pay/b89d1b6d-9188-4abb-9510-19dc7683c0be");
//                Set<String> allowedOriginRules = Collections.singleton(webviewUrl);
//
//                WebViewCompat.addWebMessageListener(owv, "nativeBridge", allowedOriginRules,
//                        new WebViewCompat.WebMessageListener() {
//                            @Override
//                            public void onPostMessage(WebView view, WebMessageCompat message,
//                                                      Uri sourceOrigin, boolean isMainFrame,
//                                                      JavaScriptReplyProxy replyProxy) {
//
//                                // Get the stringified JSON
//                                String jsonString = message.getData();
//
//                                try {
//                                    JSONObject json = new JSONObject(jsonString);
//                                    Log.d("NativeApp", "Received: " + json.getString("type"));
//
//                                    // You can even reply back directly!
//                                    replyProxy.postMessage("Message received by Android!");
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });

//                String jsonString = jsonData.toString();
//                String jsCode = "window.postMessage(" + jsonString + ", "+webviewUrl+");";
//                //String jsCode = "iframe.contentWindow.postMessage(" + jsonString + ", "+webviewUrl+");";
//                System.out.println("jsCode: "+jsCode);
//
//                if (owv == null){
//                    System.out.println("OPGWebView is null...");
//                }
//                else {
//                    owv.evaluateJavascript(jsCode, new ValueCallback<String>() {
//                        @Override
//                        public void onReceiveValue(String s) {
//                            if (s == null) {
//                                System.out.println("Eval JS result: " + null);
//                            } else {
//                                System.out.println("Eval JS result: " + s);
//                            }
//                        }
//
//                    });
//                }

            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(getApplicationContext(), "Payment intent error: "+errorMessage, Toast.LENGTH_SHORT).show();
                System.out.println("Payment intent error: "+errorMessage);
            }
        });
    }
}