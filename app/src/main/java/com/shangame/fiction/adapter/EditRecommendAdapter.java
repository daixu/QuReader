package com.shangame.fiction.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fiction.bar.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.core.manager.ImageLoader;
import com.shangame.fiction.storage.model.BookInfoEntity;

import java.util.List;

/**
 * Create by Speedy on 2018/7/27
 */
public class EditRecommendAdapter extends BaseQuickAdapter<BookInfoEntity, BaseViewHolder> {
    public EditRecommendAdapter(int layoutResId, @Nullable List<BookInfoEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, BookInfoEntity item) {
        ImageView imageCover = helper.getView(R.id.image_cover);
        ImageView imgFlag = helper.getView(R.id.img_flag);
        ImageLoader.with(mContext).loadCover(imageCover, item.bookcover);
        imgFlag.setVisibility(View.VISIBLE);
        helper.setText(R.id.text_book_name, item.bookname);

        LinearLayout layoutBottom = helper.getView(R.id.layout_bottom);

        int position = helper.getAdapterPosition();
        int random = position % 3;
        switch (random) {
            case 0:
                layoutBottom.setBackgroundColor(Color.parseColor("#91A285"));
                break;
            case 1:
                layoutBottom.setBackgroundColor(Color.parseColor("#5E707E"));
                break;
            default:
                layoutBottom.setBackgroundColor(Color.parseColor("#535973"));
                break;
        }
    }
}
