package com.ojire.app.opgdemo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

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

public class MainActivity extends AppCompatActivity implements OPGListener {

    OPGWebView owv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        owv = findViewById(R.id.main_web_view);
        setContentView(R.layout.activity_main);

        performGetToken();
    }

    @Override
    public void onSuccess(String url) {

    }

    @Override
    public void onPending(String url) {

    }

    @Override
    public void onFailed(String url) {

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
                System.out.println("Payment intent success: "+response.toString());
//                JSONObject jsonData = new JSONObject();
//                try {
//                    jsonData.put("type", "INIT");
//                    jsonData.put("clientSecret", response.clientSecret);
//                    jsonData.put("publicKey","");
//                    jsonData.put("token",response.customerToken);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                String jsonString = jsonData.toString();
//                String jsCode = "window.postMessage(" + jsonString + ", '*');";
//                owv.evaluateJavascript(jsCode, null);

            }

            @Override
            public void onError(String errorMessage) {
                System.out.println("Payment intent error: "+errorMessage);
            }
        });
    }
}