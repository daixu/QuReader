package com.shangame.fiction.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.fiction.bar.R;
import com.bigkoo.convenientbanner.holder.Holder;
import com.shangame.fiction.net.response.PictureConfigResponse;
import com.shangame.fiction.widget.GlideApp;

/**
 * Create by Speedy on 2019/7/31
 */
public class NetworkImageHolderView implements Holder<PictureConfigResponse.PicItem> {
    private ImageView imageView;

    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, PictureConfigResponse.PicItem data) {
        GlideApp.with(context)
                .load(data.imgurl)
                .centerCrop()
                .placeholder(R.drawable.image_default_banner)
                .into(imageView);
    }
}
