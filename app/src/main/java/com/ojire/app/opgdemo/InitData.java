package com.ojire.app.opgdemo;

public class InitData {
    String type = "INIT";
    String clientSecret;
    String publicKey;
    String token;

    // Constructor
    public InitData(String clientSecret, String publicKey, String token) {
        this.clientSecret = clientSecret;
        this.publicKey = publicKey;
        this.token = token;
    }
}