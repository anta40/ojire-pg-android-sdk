**How to use Ojire PG Android SDK (Java/Kotlin)**

> ⚠️ **Caution**
>
> You must get `customer_token`, `client_secret` and `payment_id` first at your backend to OJIRE API via request to `/v1/payment-intents` endpoint

---

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
   public class MainActivity extends AppCompatActivity {

    private final String PUBLIC_KEY = "pk_xxxxxxxxxxxxxxxxxxxx";

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

        /*
        First ask your backend to call /v1/payment-intents from OJIRE API to get the 
        PAYMENT_ID, CLIENT_SECRET, CUSTOMER_TOKEN
        */

        Intent checkoutIntent = new Intent(MainActivity.this, OPGActivity.class);
        checkoutIntent.putExtra("ENV", "DEV");
        checkoutIntent.putExtra("PUBLIC_KEY", PUBLIC_KEY);
        checkoutIntent.putExtra("CLIENT_SECRET",CLIENT_SECRET);
        checkoutIntent.putExtra("PAYMENT_ID",PAYMENT_ID;
        checkoutIntent.putExtra("CUSTOMER_TOKEN",CUSTOMER_TOKEN);
        startForResult.launch(checkoutIntent);

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

## Return Codes

Provided in `OPG_VALUE`

| Code| Description |
|--------|------------|
| `SUCCESS` | Payment is succesful |
| `PENDING` | Payment is pending |
| `FAILED`  | Payment is failed |
| `CLOSE`  | User closes payment page |

---
