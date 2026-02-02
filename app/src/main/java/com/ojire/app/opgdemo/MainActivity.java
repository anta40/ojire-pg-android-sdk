package com.ojire.app.opgdemo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ojire.sdk.opg.OPGAPIService;
import com.ojire.sdk.opg.OPGAPIClient;
import com.ojire.sdk.opg.OPGListener;
import com.ojire.sdk.opg.OPGWebView;
import com.ojire.sdk.opg.User;
import com.ojire.sdk.opg.UserRepository;
import com.ojire.sdk.opg.model.PaymenIntent;
import com.ojire.sdk.opg.model.PaymentMetadata;

public class MainActivity extends AppCompatActivity implements OPGListener {

    OPGWebView owv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        UserRepository repo = new UserRepository();
        repo.doGetToken(param, new UserRepository.UserCallback() {
            @Override
            public void onSuccess(User user) {

            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }
}