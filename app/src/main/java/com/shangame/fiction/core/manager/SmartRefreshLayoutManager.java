package com.shangame.fiction.core.manager;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.fiction.bar.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.internal.InternalAbstract;

/**
 * Create by Speedy on 2018/8/13
 */
public class SmartRefreshLayoutManager {

    public static void initRefreshLayout() {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary);
//                layout.setHeaderHeight(60);
                // return new AnmaHeader(context);
                ClassicsHeader refreshHeader = new ClassicsHeader(context);
                refreshHeader.setAccentColor(Color.parseColor("#ffffff"));
                return refreshHeader;
            }
        });
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                return new ClassicsFooter2(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });
    }

    private static class AnmaHeader extends InternalAbstract implements RefreshHeader {
        private Context mContext;
        private AnimationDrawable animationDrawable;

        public AnmaHeader(Context context) {
            super(context, null, 0);
            mContext = context;

            View view = LayoutInflater.from(mContext).inflate(R.layout.anma_header, this, false);
            ImageView ivHorse = view.findViewById(R.id.ivHorse);
            animationDrawable = (AnimationDrawable) ivHorse.getDrawable();
            addView(view);
        }

        @NonNull
        @Override
        public View getView() {
            return this;
        }

        @Override
        public int onFinish(@NonNull RefreshLayout layout, boolean success) {
            animationDrawable.stop();
            return 500;
        }

        @NonNull
        @Override
        public SpinnerStyle getSpinnerStyle() {
            return SpinnerStyle.Translate;
        }

        @Override
        public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
            animationDrawable.start();
        }
    }
}
