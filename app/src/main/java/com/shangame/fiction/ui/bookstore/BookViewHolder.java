package com.shangame.fiction.ui.bookstore;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiction.bar.R;

/**
 * Create by Speedy on 2018/7/27
 */
public class BookViewHolder extends RecyclerView.ViewHolder{

    public View itemView;
    public ImageView ivCover;
    public TextView tvBookName;
    public TextView tvContent;
    public TextView tvBookAuthor;

    public BookViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        this.ivCover = itemView.findViewById(R.id.ivCover);
        this.tvBookName = itemView.findViewById(R.id.tvBookName);
        this.tvContent = itemView.findViewById(R.id.tvContent);
        this.tvBookAuthor = itemView.findViewById(R.id.tvBookAuthor);
    }
}
