package com.shangame.fiction.book.drawer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.bytedance.sdk.openadsdk.TTNativeAd;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.fiction.bar.R;
import com.shangame.fiction.ad.ADConfig;
import com.shangame.fiction.ad.FeedAdBean;
import com.shangame.fiction.ad.ReadADLoader;
import com.shangame.fiction.ad.TTAdManagerHolder;
import com.shangame.fiction.ad.TTNativeExpressAdBean;
import com.shangame.fiction.book.config.PageConfig;
import com.shangame.fiction.book.helper.FontHelper;
import com.shangame.fiction.book.helper.PaintHelper;
import com.shangame.fiction.book.page.Line;
import com.shangame.fiction.book.page.PageData;
import com.shangame.fiction.core.utils.BitmapUtils;
import com.shangame.fiction.core.utils.ScreenUtils;
import com.shangame.fiction.storage.manager.FileManager;
import com.shangame.fiction.ui.reader.BatteryReceiver;
import com.shangame.fiction.widget.GlideApp;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import notchtools.geek.com.notchtools.NotchTools;

/**
 * Create by Speedy on 2018/8/30
 */
public class PageDrawer implements Drawer {

    private static DecimalFormat decimalFormat = new DecimalFormat("#0.00");
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private int batteryWidth;
    private int batteryHeight;
    private Context mContext;
    private Activity mActivity;
    private Canvas mCanvas;
    private int mWidth;
    private int mHeight;
    private PageData pageData;
    private Paint headerPaint;
    private Paint chapterPaint;
    private Paint contentPaint;
    private Paint footerPaint;
    private Paint batteryPaint;
    private Paint adPaint;
    private int currentY;
    private int contentTextHeight;
    private int textIndentWidth;
    private int padding;
    private int headerHeight;
    private int footerHeight;
    private PageConfig mPageConfig;

    private Region showMenuRegion;

    private Region mRegion;

    private View.OnClickListener onMenuRegionClickListener;
    private View.OnClickListener onOpenVideoRegionClickListener1;

    private FrameLayout mFrameLayout;
    private ReadADLoader readADLoader;

    public PageDrawer(Context context, Canvas canvas) {
        this.mContext = context;
        this.mCanvas = canvas;
        initPageConfig(context);
        readADLoader = new ReadADLoader(context);
    }

    private void initPageConfig(Context context) {
        mPageConfig = PageConfig.getInstance(mContext);
        padding = (int) ScreenUtils.spToPx(context, PageConfig.padding);
        headerHeight = (int) ScreenUtils.spToPx(context, PageConfig.headerHeight);
        footerHeight = (int) ScreenUtils.spToPx(context, PageConfig.footerHeight);
        batteryWidth = (int) ScreenUtils.spToPx(context, PageConfig.batteryWidth);
        batteryHeight = (int) ScreenUtils.spToPx(context, PageConfig.batteryHeight);
    }

    public void updateConfig() {
        initPageConfig(mContext);
        PaintHelper.clear();//刷新笔
    }

    public void initPage(PageData pageData, PageRefreshListener pageRefreshListener) {
        mWidth = mCanvas.getWidth();
        mHeight = mCanvas.getHeight();

        int radius = ScreenUtils.dpToPxInt(mContext, 100);
        int left = mWidth / 2 - radius;
        int top = mHeight / 2 - radius;
        int right = mWidth / 2 + radius;
        int bottom = mHeight / 2 + radius;

        Rect showMenuRect = new Rect(left, top, right, bottom);
        showMenuRegion = new Region(showMenuRect);

        this.pageData = pageData;

        headerPaint = PaintHelper.getHeaderPaint(mContext);
        int fontSize = mPageConfig.fontSize;
        chapterPaint = PaintHelper.getChapterPaint(fontSize);
        contentPaint = PaintHelper.getContentPaint(fontSize);
        footerPaint = PaintHelper.getFooterPaint(mContext);
        batteryPaint = PaintHelper.getBatteryPaint(mContext);
        adPaint = PaintHelper.getAdPaint(mContext);

        if (mPageConfig.dayMode == PageConfig.DayMode.NIGHT_MODE) {
            int color = Color.parseColor("#ffffff");
            headerPaint.setColor(color);
            chapterPaint.setColor(color);
            contentPaint.setColor(color);
            footerPaint.setColor(color);
            batteryPaint.setColor(color);
            adPaint.setColor(color);
        }

        Rect rect = FontHelper.measureText("小说", contentPaint);
        contentTextHeight = rect.height();
        textIndentWidth = rect.width();
    }

    public void setActivity(Activity activity) {
        this.mActivity = activity;
        readADLoader.setActivity(activity);
    }

    public void setLayoutView(FrameLayout frameLayout) {
        this.mFrameLayout = frameLayout;
        readADLoader.setLayoutView(frameLayout);
    }

    @Override
    public void onDraw() {
        drawBackground();
        if (pageData != null) {
            if (pageData.isADPage) {
                drawAdPage();
            } else {
                drawHeader();
                drawContent();
                drawFooter();
            }
        }
    }

    @Override
    public boolean handleClick(float x, float y) {
        if (pageData.isADPage) {
            if (null != mRegion && mRegion.contains((int) x, (int) y)) {
                Log.e("hhh", "mRegion= " + mRegion);
                Log.i("hhh", "handleClick onTouchEvent: not contains");
                if (onOpenVideoRegionClickListener1 != null) {
                    onOpenVideoRegionClickListener1.onClick(null);
                }
                return true;
            }
        } else {
            if (showMenuRegion != null && showMenuRegion.contains((int) x, (int) y)) {
                if (onMenuRegionClickListener != null) {
                    onMenuRegionClickListener.onClick(null);
                    return true;
                }
            }
        }
        return false;
    }

    private void drawBackground() {
        mCanvas.drawColor(mContext.getResources().getColor(mPageConfig.backgroundColor));
    }

    private void drawAdPage() {
        // 画广告背景
        final Bitmap defaultBg = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ad_bg);
        int bgHeight = (mHeight / 4) + 130;
        mCanvas.drawBitmap(defaultBg, 0, bgHeight, contentPaint);

        int adBgHeight = 0;

        boolean isNotchScreen = NotchTools.getFullScreenTools().isNotchScreen(mActivity.getWindow());
        Log.e("hhh", "isNotchScreen= " + isNotchScreen);

        if (isNotchScreen) {
            adBgHeight = bgHeight + (int) mContext.getResources().getDimension(R.dimen.statusbar_view_height);
        } else {
            adBgHeight = bgHeight;
        }

        // 画免广告按钮
        int btnHeight = bgHeight + defaultBg.getHeight();
        final Bitmap noAdBtn = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_no_ad);
        int textWidth = (mWidth - noAdBtn.getWidth()) / 2;
        mCanvas.drawBitmap(noAdBtn, textWidth, btnHeight + 100, contentPaint);
        // mCanvas.drawText("看视频免15分钟广告", textWidth, y + 350, contentPaint);

        int right = textWidth + noAdBtn.getWidth();
        int bottom = btnHeight + 100 + noAdBtn.getHeight();
        Rect rect = new Rect(textWidth, btnHeight + 100, right, bottom);
        mRegion = new Region(rect);

        readADLoader.loadAD(new ReadADLoader.ADLoadCallback() {
            @Override
            public void onADLoaded(List<TTNativeExpressAd> ads) {
                onDraw();
            }

            @Override
            public void onADError(int code, String message) {
                Log.i("hhh", "load error 1 : " + code + ", " + message);
            }
        }, adBgHeight, mWidth, defaultBg.getHeight());
    }

    private void drawHeader() {
        Rect rect = new Rect();
        headerPaint.getTextBounds(pageData.chapterName, 0, pageData.chapterName.length(), rect);
        int x = padding;
        int y = padding + rect.height();
        mCanvas.drawText(pageData.chapterName, x, y, headerPaint);
    }

    private void drawContent() {
        int x = padding;
        int y = headerHeight + padding;
        currentY = y;
        if (pageData.index == 0) {
            Rect rect = FontHelper.measureText(pageData.chapterName, chapterPaint);
            int pageWidth = mWidth - PageConfig.padding - PageConfig.padding;
            // 章节名太长，分两行显示，如果两行还显示不够，那就不是我的错了，不管了，爱咋地咋地，反正我只显示两行
            if (rect.width() < pageWidth) {
                mCanvas.drawText(pageData.chapterName, x, y + contentTextHeight, chapterPaint);
                currentY = currentY + 2 * contentTextHeight + mPageConfig.lineSpace;
            } else {
                //两行显示，适当缩小字体太小
                chapterPaint.setTextSize((float) (chapterPaint.getTextSize() * 0.9));
                int paintSize = chapterPaint.breakText(pageData.chapterName, true, pageWidth - PageConfig.padding, null);
                String chapter1 = pageData.chapterName.substring(0, paintSize);
                mCanvas.drawText(chapter1, x, y, chapterPaint);

                currentY = y + rect.height() + (mPageConfig.lineSpace / 2);

                String chapter2 = pageData.chapterName.substring(paintSize);
                mCanvas.drawText(chapter2, x, currentY, chapterPaint);

                currentY = y + rect.height() + (mPageConfig.lineSpace / 2) + contentTextHeight;
            }
            for (Line line : pageData.lineList) {
                y = currentY + contentTextHeight;
                mCanvas.drawText(line.text, x, y, contentPaint);
                currentY = y + mPageConfig.lineSpace;
            }
        } else {
            if (null != pageData.lineList) {
                if (pageData.lineList.size() > 4) {
                    for (int i = 0; i < 4; i++) {
                        Line line = pageData.lineList.get(i);
                        y = currentY + contentTextHeight;
                        mCanvas.drawText(line.text, x, y, contentPaint);
                        currentY = y + mPageConfig.lineSpace;
                    }

                    if (pageData.hasAdPage) {
                        Log.e("hhh", "draw---111---");
//                        TTNativeExpressAd ttNativeExpressAd = TTNativeExpressAdBean.getInstance().getTTNativeExpressAd();
//                        if (null != ttNativeExpressAd) {
//                            drawAd(ttNativeExpressAd);
//                        }
                        drawAd();
                    }

                    for (int i = 4; i < pageData.lineList.size(); i++) {
                        Line line = pageData.lineList.get(i);
                        y = currentY + contentTextHeight;
                        mCanvas.drawText(line.text, x, y, contentPaint);
                        currentY = y + mPageConfig.lineSpace;
                    }
                } else {
                    for (Line line : pageData.lineList) {
                        y = currentY + contentTextHeight;
                        mCanvas.drawText(line.text, x, y, contentPaint);
                        currentY = y + mPageConfig.lineSpace;
                    }
                }
            }
        }
    }

    private void drawFooter() {
        drawTime();
        drawBatteryView();
        drawPercent();
    }

    private void drawAd(TTNativeExpressAd ttNativeExpressAd) {
        // 画广告背景
        final Bitmap defaultBg = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ad_bg);
        int bgHeight = currentY;
        mCanvas.drawBitmap(defaultBg, 0, bgHeight, adPaint);

        currentY = currentY + defaultBg.getHeight() + mPageConfig.lineSpace;

        readADLoader.loadAD(null, ttNativeExpressAd);
//        TTNativeExpressAd ad = ttNativeExpressAd;
//        bindAdListener(ad, mFrameLayout);
//        ad.render();
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

    private void drawAd() {
        // 画广告背景
        final Bitmap defaultBg = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ad_bg);
        int bgHeight = currentY;
        int adBgHeight = 0;

        boolean isNotchScreen = NotchTools.getFullScreenTools().isNotchScreen(mActivity.getWindow());
        Log.e("hhh", "isNotchScreen= " + isNotchScreen);

        if (isNotchScreen) {
            adBgHeight = currentY + (int) mContext.getResources().getDimension(R.dimen.statusbar_view_height);
        } else {
            adBgHeight = currentY;
        }

        mCanvas.drawBitmap(defaultBg, 0, bgHeight, adPaint);

        currentY = currentY + defaultBg.getHeight() + mPageConfig.lineSpace;

        readADLoader.loadAD(new ReadADLoader.ADLoadCallback() {
            @Override
            public void onADLoaded(List<TTNativeExpressAd> ads) {
                onDraw();
            }

            @Override
            public void onADError(int code, String message) {
                Log.i("hhh", "load error 1 : " + code + ", " + message);
            }
        }, adBgHeight, mWidth, defaultBg.getHeight());
    }

    private void drawTime() {
        String mTime = dateFormat.format(new Date());
        int x = padding + batteryWidth + 20;
        int y = mHeight - padding;
        mCanvas.drawText(mTime, x, y, footerPaint);
    }

    private void drawBatteryView() {
        int left = padding;
        int bottom = mHeight - padding;
        int top = bottom - batteryHeight;
        int right = left + batteryWidth;

        batteryPaint.setStyle(Paint.Style.STROKE);
        Rect rect = new Rect(left, top, right, bottom);
        mCanvas.drawRect(rect, batteryPaint);

        batteryPaint.setStyle(Paint.Style.FILL);
        Rect rect2 = new Rect(right + 1, top + rect.height() / 2 - 6, right + 5, top + rect.height() / 2 + 6);
        mCanvas.drawRect(rect2, batteryPaint);

        //实际计算电量值为：44-2-2 = 40，2为左右边距
        int length = (int) (BatteryReceiver.batterPercent * (batteryWidth - 4));

        batteryPaint.setStyle(Paint.Style.FILL);
        Rect rec2 = new Rect(left + 2, top + 2, left + 2 + length, bottom - 2);
        mCanvas.drawRect(rec2, batteryPaint);
    }

    private void drawPercent() {
        int percentLen = (int) footerPaint.measureText("00.00%");
        int x = mWidth - percentLen - padding;
        int y = mHeight - padding;

        if (pageData.bookPercent > 100) {
            mCanvas.drawText("100%", x, y, footerPaint);
        } else {
            mCanvas.drawText(decimalFormat.format(pageData.bookPercent) + "%", x, y, footerPaint);
        }
    }

    public void setOnMenuRegionClickListener(View.OnClickListener onMenuRegionClickListener) {
        this.onMenuRegionClickListener = onMenuRegionClickListener;
    }

    public void setOnOpenVideoRegionClickListener1(View.OnClickListener onOpenVideoRegionClickListener1) {
        this.onOpenVideoRegionClickListener1 = onOpenVideoRegionClickListener1;
    }
}
