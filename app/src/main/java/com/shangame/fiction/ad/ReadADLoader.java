package com.shangame.fiction.ad;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Create by Speedy on 2019/5/8
 */
public class ReadADLoader {

    private static final String TAG = "ReadADLoader";

    private Context mContext;
    private FrameLayout adContainer;
    private Activity mActivity;

    public ReadADLoader(Context mContext) {
        this.mContext = mContext;
    }

    public void setLayoutView(FrameLayout frameLayout) {
        adContainer = frameLayout;
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    public void loadAD(final ADLoadCallback adLoadCallback, final int top, final int width, final int height) {
        Flowable.just("").map(new Function<String, Boolean>() {
            @Override
            public Boolean apply(String ad) throws Exception {

                return true;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, height);
                lp.leftMargin = 0;
                lp.gravity = Gravity.CENTER_HORIZONTAL;
                lp.topMargin = top;
                adContainer.setLayoutParams(lp);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e(TAG, "hhh ", throwable);
            }
        });
        TTAdNative mAdNative = TTAdManagerHolder.get().createAdNative(mContext);
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(ADConfig.CSJAdPositionId.READ_PAGE_ID) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(350, 0) //期望模板广告view的size,单位dp
                .setImageAcceptedSize(690, 388)
                .build();

        //step5:请求广告，对请求回调的广告作渲染处理
        mAdNative.loadBannerExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
                Log.i("hhh", "load error 1 : " + code + ", " + message);
                adContainer.removeAllViews();
                // adLoadCallback.onADError(code, message);
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0) {
                    return;
                }

                adContainer.removeAllViews();
                // adLoadCallback.onADLoaded(ads);
                TTNativeExpressAd ad = ads.get(0);
                // ad.setSlideIntervalTime(30 * 1000);
                bindAdListener(ad, adContainer);
                ad.render();
            }
        });
    }

    public void loadAD(final ADLoadCallback adLoadCallback, final TTNativeExpressAd ad) {
        Flowable.just(ad).map(new Function<TTNativeExpressAd, Boolean>() {
            @Override
            public Boolean apply(TTNativeExpressAd ad) throws Exception {
                bindAdListener(ad, adContainer);
                return true;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                ad.render();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e(TAG, "hhh ", throwable);
            }
        });

//        mActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                bindAdListener(ad, adContainer);
//                try {
//                    ad.render();
//                } catch (Exception e) {
//                    Log.e("hhh", "exception= " + e.getMessage());
//                }
//            }
//        });
    }

    private void bindAdListener(TTNativeExpressAd ad, final FrameLayout frameLayout) {
        ad.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
            @Override
            public void onAdClicked(View view, int type) {
                Log.i("hhh", "广告被点击");
            }

            @Override
            public void onAdShow(View view, int type) {
                Log.i("hhh", "广告展示");
            }

            @Override
            public void onRenderFail(View view, String msg, int code) {
                Log.i("hhh", msg + " code:" + code);
            }

            @Override
            public void onRenderSuccess(View view, float width, float height) {
                //返回view的宽高 单位 dp
                Log.i("hhh", "渲染成功");
                frameLayout.removeAllViews();
                frameLayout.addView(view);
            }
        });
    }

    public interface ADLoadCallback {
        void onADLoaded(List<TTNativeExpressAd> ads);

        void onADError(int code, String message);
    }
}
