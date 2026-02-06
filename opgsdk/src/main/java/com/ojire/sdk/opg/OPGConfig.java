package com.ojire.sdk.opg;

public class OPGConfig {

    private final String clientSecret;
    private final String publicKey;
    private final String baseApiUrl;
    private final String basePaymentUrl;

    private OPGConfig(ConfigBuilder builder) {
        this.clientSecret = builder.clientSecret;
        this.publicKey = builder.publicKey;
        this.baseApiUrl = builder.baseApiUrl;
        this.basePaymentUrl = builder.basePaymentUrl;
    }

    public String getBaseAPIUrl() {
        return baseApiUrl;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getBasePaymentURL() {
        return basePaymentUrl;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public static class ConfigBuilder {
        private String clientSecret;
        private String publicKey;
        private String baseApiUrl;
        private String basePaymentUrl;

        public ConfigBuilder() {
        }

        public ConfigBuilder setClientSecret(String cs) {
            this.clientSecret = cs;
            return this;
        }

        public ConfigBuilder setPublicKey(String pk) {
            this.publicKey = pk;
            return this;
        }

        public ConfigBuilder setEnv(OPGEnvType.Env env) {
            switch (env) {
                case DEV:
                    this.baseApiUrl = "https://api-dev.ojire.com/";
                    this.basePaymentUrl = "https://pay-dev.ojire.com/pay/";
                    break;
                case SANDBOX:
                    this.baseApiUrl = "https://api-sandbox.ojire.com/";
                    this.basePaymentUrl = "https://pay-sandbox.ojire.com/pay/";
                    break;
                case PROD:
                    this.baseApiUrl = "https://api.ojire.online/";
                    this.basePaymentUrl = "https://pay.ojire.online/pay/";
                    break;
            }
            return this;
        }

        public OPGConfig build() {
            if (publicKey == null || clientSecret == null) {
                throw new IllegalStateException("Public key/client secret cannot be null");
            }
            if (baseApiUrl == null || basePaymentUrl == null) {
                throw new IllegalStateException("env must be set");
            }
            return new OPGConfig(this);
        }
    }
}
