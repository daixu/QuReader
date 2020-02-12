package com.shangame.fiction.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.fiction.bar.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.storage.model.BookInfoEntity;
import com.shangame.fiction.widget.GlideApp;

import java.util.List;

public class BannerAdapter extends BaseQuickAdapter<BookInfoEntity, BaseViewHolder> {
    public BannerAdapter(int layoutResId, @Nullable List<BookInfoEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, BookInfoEntity item) {
        ImageView imageBanner = helper.getView(R.id.image_banner);
        GlideApp.with(mContext)
                .load(item.bookcover)
                .placeholder(R.drawable.default_cover)
                .centerCrop()
                .into(imageBanner);
    }
}
