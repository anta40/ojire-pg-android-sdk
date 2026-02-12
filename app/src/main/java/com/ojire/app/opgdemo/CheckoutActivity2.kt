package com.ojire.app.opgdemo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ojire.sdk.opg.OPGConfig
import com.ojire.sdk.opg.OPGConfig.ConfigBuilder
import com.ojire.sdk.opg.OPGEnvType
import com.ojire.sdk.opg.OPGListener
import com.ojire.sdk.opg.OPGWebView
import com.ojire.sdk.opg.model.PaymentIntentParam
import com.ojire.sdk.opg.model.PaymentMetadata
import java.util.concurrent.ThreadLocalRandom

class CheckoutActivity2 : AppCompatActivity(), OPGListener {

    var webView: OPGWebView? = null
    private val tvPaymentId: TextView? = null

    private val CLIENT_SECRET = "sk_1769591280469729bd24176959128046989e6f78b694f70b4131"
    private val PUBLIC_KEY = "pk_1769591280469729bd24176959128046990a6531e6a9fdf3cbd6"
    private var TOTAL_CHECKOUT = 0
    private var ENV_TYPE = 0
    private var config: OPGConfig? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout2)
        TOTAL_CHECKOUT = getIntent().getIntExtra("TOTAL_CHECKOUT", 0)
        ENV_TYPE = getIntent().getIntExtra("ENV_TYPE", 0)
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

        if (ENV_TYPE == 0) {
            config = ConfigBuilder().setClientSecret(CLIENT_SECRET)
                .setPublicKey(PUBLIC_KEY)
                .setEnv(OPGEnvType.Env.DEV)
                .build()
        } else if (ENV_TYPE == 1) {
            config = ConfigBuilder().setClientSecret(CLIENT_SECRET)
                .setPublicKey(PUBLIC_KEY)
                .setEnv(OPGEnvType.Env.SANDBOX)
                .build()
        } else if (ENV_TYPE == 2) {
            config = ConfigBuilder().setClientSecret(CLIENT_SECRET)
                .setPublicKey(PUBLIC_KEY)
                .setEnv(OPGEnvType.Env.PROD)
                .build()
        }

        webView!!.initPayment(PUBLIC_KEY, config, param)
    }

    override fun onSuccess(url: String?) {
        Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
            override fun run() {
                val returnIntent = Intent()
                returnIntent.putExtra("payment_msg", "Pembayaran berhasil!")
                setResult(RESULT_OK, returnIntent)
                finish()
            }
        }, 5000)
    }

    override fun onPending(url: String?) {
        Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
            override fun run() {
                val returnIntent = Intent()
                returnIntent.putExtra("payment_msg", "Pembayaran pending...")
                setResult(RESULT_OK, returnIntent)
                finish()
            }
        }, 5000)
    }

    override fun onFailed(url: String?) {
        Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
            override fun run() {
                val returnIntent = Intent()
                returnIntent.putExtra("payment_msg", "Pembayaran gagal :(")
                setResult(RESULT_OK, returnIntent)
                finish()
            }
        }, 5000)
    }

    override fun onClose() {

    }
}