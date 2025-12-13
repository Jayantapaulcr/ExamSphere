package com.noveletytech.examsphere.ads

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.nativead.NativeAdView

@Composable
fun NativeAd(modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { context ->
            val adView = NativeAdView(context)
            val adLoader = AdLoader.Builder(context, "ca-app-pub-7065743985138066/5962729883")
                .forNativeAd {
                    // You can create a custom layout for the native ad here
                    // For now, we'll use a simple one provided by the SDK
                    adView.setNativeAd(it)
                }
                .build()
            adLoader.loadAd(AdRequest.Builder().build())
            adView
        }
    )
}
