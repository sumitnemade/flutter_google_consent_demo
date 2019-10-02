package com.vaghaitech.google_consent

import android.content.Context
import android.os.Bundle

import io.flutter.app.FlutterActivity
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant
import com.ayoubfletcher.consentsdk.ConsentSDK;

class MainActivity: FlutterActivity() {
  private val CHANNEL = "flutter.native/helper"
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    GeneratedPluginRegistrant.registerWith(this)
    MethodChannel(flutterView, CHANNEL).setMethodCallHandler { call, result ->
      if (call.method == "helloFromNativeCode") {

        val consentSDK = ConsentSDK.Builder(this)
                .addTestDeviceId("Add your test device") // Add your test device id "Remove addTestDeviceId on production!"
                .addCustomLogTag("CUSTOM_TAG") // Add custom tag default: ID_LOG
                .addPrivacyPolicy("Add your privacy policy url") // Add your privacy policy url
                .addPublisherId("admob publisher-id") // Add your admob publisher id
                .build()

        if(ConsentSDK.isUserLocationWithinEea(this)) {
          val choice = if (ConsentSDK.isConsentPersonalized(this)) "Personalize" else "Non-Personalize"
          consentSDK.requestConsent(object : ConsentSDK.ConsentStatusCallback() {
            override fun onResult(isRequestLocationInEeaOrUnknown: Boolean, isConsentPersonalized: Int) {
              var choice = ""
              when (isConsentPersonalized) {
                0 -> choice = "Non-Personalize"
                1 -> choice = "Personalized"
                -1 -> choice = "Error occured"
              }

              result.success(choice)
            }
          })
        }else{

          result.success("flutter nonEU user")
        }



      }
    }
  }
}
