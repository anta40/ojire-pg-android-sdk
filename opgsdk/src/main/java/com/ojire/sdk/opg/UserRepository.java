package com.ojire.sdk.opg;

import com.ojire.sdk.opg.model.PaymenIntent;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private OPGAPIService apiService;
    public interface UserCallback {
        void onSuccess(User user);
        void onError(String errorMessage);
    }

    public UserRepository() {
        this.apiService = OPGAPIClient.getAPIServicet();
    }

    public void doGetToken(PaymenIntent param, UserCallback callback){

        apiService.createPaymentIntent(param).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Server error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
}
