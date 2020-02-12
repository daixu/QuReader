package com.shangame.fiction.ui.bookstore;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fiction.bar.R;
import com.shangame.fiction.core.base.WrapRecyclerViewAdapter;
import com.shangame.fiction.core.manager.ImageLoader;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.storage.model.BookInfoEntity;
import com.shangame.fiction.ui.bookdetail.BookDetailActivity;

/**
 * Create by Speedy on 2018/7/27
 */
public class HotSerialAdapter extends WrapRecyclerViewAdapter<BookInfoEntity, BookWithContentViewHolder> {

    private int clickType;
    private Activity mActivity;

    public HotSerialAdapter(Activity activity) {
        mActivity = activity;
        clickType = ApiConstant.ClickType.FROM_CLICK;//默认点击
    }

    public HotSerialAdapter(Activity activity, int clickType) {
        this.clickType = clickType;
        mActivity = activity;
    }

    @NonNull
    @Override
    public BookWithContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_boutique_book, parent, false);
        return new BookWithContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookWithContentViewHolder holder, int position) {
        final BookInfoEntity bookInfoEntity = getItem(position);
        if (bookInfoEntity != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BookDetailActivity.lunchActivity(mActivity, bookInfoEntity.bookid, clickType);
                }
            });

            int random = position % 3;
            switch (random) {
                case 0:
                    holder.mLayoutBottom.setBackgroundColor(Color.parseColor("#91A285"));
                    break;
                case 1:
                    holder.mLayoutBottom.setBackgroundColor(Color.parseColor("#5E707E"));
                    break;
                default:
                    holder.mLayoutBottom.setBackgroundColor(Color.parseColor("#535973"));
                    break;
            }

            ImageLoader.with(mActivity).loadCover(holder.ivCover, bookInfoEntity.bookcover);
            holder.ivFlag.setVisibility(View.VISIBLE);
            holder.tvBookName.setText(bookInfoEntity.bookname);
        }
    }
}
