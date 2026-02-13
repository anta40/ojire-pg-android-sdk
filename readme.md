How to use Ojire PG Android SDK (Java/Kotlin)

## Installation 

First adjust your **settings.gradle.kts**:
```
dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url = uri("https://jitpack.io") }
		}
	}
```
Then add this line into your app **build.gradle.kts**:
```
dependencies {
	        implementation("com.github.anta40:ojire-pg-android-sdk:%VERSION%")
	}
```

Replace `%VERSION%` with the actual version number, e.g `0.2.0` or `0.1.1`, for example.

---

## Basic Usage (Java)

```java
public class CheckoutActivity extends AppCompatActivity implements OPGListener {

    private OPGWebView webView;
    private final String CLIENT_SECRET = "xxxxxxxxxxxxxxxxxxxx";
    private final String PUBLIC_KEY = "xxxxxxxxxxxxxxxxxxxx;
    private final int TOTAL_CHECKOUT = 35000
    private OPGConfig config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        webView = findViewById(R.id.main_web_view);
        webView.setListener(this);

        initiatePayment();
    }

    void initiatePayment(){
        PaymentIntentParam param = new PaymentIntentParam();
        int randomNum = ThreadLocalRandom.current().nextInt(10000, 100000);
        param.amount = TOTAL_CHECKOUT;
        param.currency = "IDR";
        param.customerId = "customer_"+randomNum;
        param.description = "Test payment "+randomNum;
        param.merchantId = getString(R.string.MERCHANT_ID);
        PaymentMetadata metadata = new PaymentMetadata();
        metadata.orderId = "order_"+randomNum;
        param.metadata = metadata;


        OPGConfig config = new OPGConfig.ConfigBuilder().setClientSecret(CLIENT_SECRET)
                    .setPublicKey(PUBLIC_KEY)
                    .setEnv(OPGEnvType.Env.DEV)
                    .build();

        webView.initPayment(PUBLIC_KEY, config, param);
    }

    @Override
    public void onSuccess(String url) {
        Log.i(TAG, "The transaction is successful");
    }

    @Override
    public void onPending(String url) {
        Log.i(TAG, "The transaction is pending.");
    }

    @Override
    public void onFailed(String url) {
       Log.i(TAG, "The transaction is failed.");
    }
}
```

## Basic Usage (Kotlin)

```kotlin
pclass CheckoutActivity : AppCompatActivity(), OPGListener {

    private var webView: OPGWebView? = null
    private val CLIENT_SECRET = "sk_1769591280469729bd24176959128046989e6f78b694f70b4131"
    private val PUBLIC_KEY = "pk_1769591280469729bd24176959128046990a6531e6a9fdf3cbd6"
    private var TOTAL_CHECKOUT = 35000
    private var config: OPGConfig? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout2)
        webView = findViewById<OPGWebView?>(R.id.main_web_view)
        webView!!.setListener(this)

        initiatePayment()
    }

    fun initiatePayment() {
        val param = PaymentIntentParam()
        val randomNum = ThreadLocalRandom.current().nextInt(10000, 100000)
        param.amount = TOTAL_CHECKOUT
        param.currency = "IDR"
        param.customerId = "customer_" + randomNum
        param.description = "Test payment " + randomNum
        param.merchantId = getString(R.string.MERCHANT_ID)
        val metadata = PaymentMetadata()
        metadata.orderId = "order_" + randomNum
        param.metadata = metadata

        config = ConfigBuilder().setClientSecret(CLIENT_SECRET)
                .setPublicKey(PUBLIC_KEY)
                .setEnv(OPGEnvType.Env.DEV)
                .build()
       

        webView!!.initPayment(PUBLIC_KEY, config, param)
    }

    override fun onSuccess(url: String?) {
       Log.i(TAG, "The transaction is successful");
    }

    override fun onPending(url: String?) {
        Log.i(TAG, "The transaction is pending.");
    }

    override fun onFailed(url: String?) {
        Log.i(TAG, "The transaction is failed.");
    }

    override fun onClose() {

    }
}
```

---

## Properties

| Name | Type | Required | Description |
|-----|-----|---------|-------------|
| `clientSecret` | `String` | ✅ | Client secret provided from backed|
| `publicKey` | `String` | ✅ | Merchant's public key |
| `env` | `OPGEnvType.Env` | ✅  | Environment (`DEV`, `SANDBOX`, or `PROD`) |


---

## Callbacks

| Callback | Params | Description |
|--------|-------|------------|
| `onSuccess` | `String` | Payment is succesful |
| `onPending` | `String` | Payment is pending |
| `onFailed` | `String` | Payment is failed |
| `onClose` | `void` | User closes payment page |

---
