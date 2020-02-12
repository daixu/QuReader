package com.shangame.fiction.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.fiction.bar.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.net.response.AlbumModuleResponse;
import com.shangame.fiction.widget.GlideApp;

import java.util.List;

public class SerializedLatestAdapter extends BaseQuickAdapter<AlbumModuleResponse.HotDataBean, BaseViewHolder> {

    public SerializedLatestAdapter(int layoutResId, @Nullable List<AlbumModuleResponse.HotDataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AlbumModuleResponse.HotDataBean item) {
        helper.setText(R.id.text_book_name, item.albumName);
        if (!TextUtils.isEmpty(item.author)) {
            helper.setText(R.id.text_book_author, item.author);
        }

        ImageView imageCover = helper.getView(R.id.image_cover);
        GlideApp.with(mContext)
                .load(item.bookcover)
                .placeholder(R.drawable.default_cover)
                .centerCrop()
                .into(imageCover);
    }
}
