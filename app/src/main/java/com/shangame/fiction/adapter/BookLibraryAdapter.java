package com.shangame.fiction.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.fiction.bar.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.net.response.GetBookLibraryTypeResponse;
import com.shangame.fiction.widget.GlideApp;

import java.util.List;

public class BookLibraryAdapter extends BaseQuickAdapter<GetBookLibraryTypeResponse.ClassdataBean, BaseViewHolder> {
    public BookLibraryAdapter(int layoutResId, @Nullable List<GetBookLibraryTypeResponse.ClassdataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, GetBookLibraryTypeResponse.ClassdataBean item) {
        helper.setText(R.id.text_name, item.classname);
        ImageView imageCover = helper.getView(R.id.image_cover);
        GlideApp.with(mContext)
                .load(item.classimage)
                .placeholder(R.drawable.default_cover)
                .centerCrop()
                .into(imageCover);
    }
}
