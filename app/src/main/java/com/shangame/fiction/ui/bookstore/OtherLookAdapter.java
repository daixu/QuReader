package com.shangame.fiction.ui.bookstore;

import android.app.Activity;
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
public class OtherLookAdapter extends WrapRecyclerViewAdapter<BookInfoEntity, BookWithContentViewHolder> {

    private int clickType;
    private Activity mActivity;

    public OtherLookAdapter(Activity activity) {
        mActivity = activity;
        clickType = ApiConstant.ClickType.FROM_CLICK;//默认点击
    }

    @NonNull
    @Override
    public BookWithContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_store_item_with_content, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_store_item_with_title, parent, false);
        }
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

            ImageLoader.with(mActivity).loadCover(holder.ivCover, bookInfoEntity.bookcover);
            holder.tvBookName.setText(bookInfoEntity.bookname);
            String content = bookInfoEntity.author + "·" + bookInfoEntity.classname + "·" + bookInfoEntity.serstatus + "·" + bookInfoEntity.wordnumbers;
            holder.tvBookAuthor.setText(content);
            int viewType = getItemViewType(position);
            if (viewType == 0) {
                holder.tvContent.setText(bookInfoEntity.synopsis);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 7 < 3) {
            return 0;
        } else {
            return 1;
        }
    }
}
