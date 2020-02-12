package com.shangame.fiction.ad;

import com.bytedance.sdk.openadsdk.TTNativeExpressAd;

public class TTNativeExpressAdBean {
    private TTNativeExpressAd nativeAd;
    private static TTNativeExpressAdBean instance = new TTNativeExpressAdBean();

    private TTNativeExpressAdBean() {
    }

    public static TTNativeExpressAdBean getInstance() {
        return instance;
    }

    public TTNativeExpressAd getTTNativeExpressAd() {
        return nativeAd;
    }

    public void setTTNativeExpressAd(TTNativeExpressAd nativeAd) {
        this.nativeAd = nativeAd;
    }
}
