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

   public class MainActivity extends AppCompatActivity {

    private final String CLIENT_SECRET = "sk_1769591280469729bd24176959128046989e6f78b694f70b4131";
    private final String PUBLIC_KEY = "pk_1769591280469729bd24176959128046990a6531e6a9fdf3cbd6";
    private final String MERCHANT_ID = "949f9617-1333-4626-b29b-a049b45aa568";

    private final ActivityResultLauncher<Intent> startForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        String payment_msg = data.getStringExtra("OPG_RESULT");
                        showAlert(payment_msg);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TOTAL_CHECKOUT = 38500;

        PaymentIntentParam param = new PaymentIntentParam();
        int randomNum = ThreadLocalRandom.current().nextInt(10000, 100000);
        param.amount = TOTAL_CHECKOUT;
        param.currency = "IDR";
        param.customerId = "customer_"+randomNum;
        param.description = "Test payment "+randomNum;
        param.merchantId = MERCHANT_ID;
        PaymentMetadata metadata = new PaymentMetadata();
        metadata.orderId = "order_"+randomNum;
        param.metadata = metadata;

        OPGProcessor repo = new OPGProcessor(MainActivity.this, "DEV", CLIENT_SECRET);
        repo.doGetToken(param, new OPGProcessor.PaymentCallback() {
            @Override
            public void onSuccess(PaymentIntentResponse response) {
                Intent checkoutIntent = new Intent(MainActivity.this, OPGActivity.class);
                checkoutIntent.putExtra("ENV", "DEV");
                checkoutIntent.putExtra("PUBLIC_KEY", PUBLIC_KEY);
                checkoutIntent.putExtra("CLIENT_SECRET",response.clientSecret);
                checkoutIntent.putExtra("PAYMENT_ID",response.id);
                checkoutIntent.putExtra("CUSTOMER_TOKEN",response.customerToken);
                startForResult.launch(checkoutIntent);
            }

            @Override
                public void onError(String errorMessage) {
                    System.out.println("Create payment intent error: "+errorMessage);
                    Toast.makeText(MainActivity.this, "Create payment intent error: "+errorMessage, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
```


---

## Properties

| Name | Type | Required | Description |
|-----|-----|---------|-------------|
| `paymentId` | `String` | ✅ | Payment intent ID|
| `clientSecret` | `String` | ✅ | Client secret provided from backend|
| `publicKey` | `String` | ✅ | Merchant's public key |
| `token` | `String` | ✅ | Customer token |
| `env` | `String` | ✅  | Environment (`DEV`, `SANDBOX`, or `PROD`) |


---

## Callbacks

| Callback | Params | Description |
|--------|-------|------------|
| `onSuccess` | `String` | Payment is succesful |
| `onPending` | `String` | Payment is pending |
| `onFailed` | `String` | Payment is failed |
| `onClose` | `void` | User closes payment page |

---
