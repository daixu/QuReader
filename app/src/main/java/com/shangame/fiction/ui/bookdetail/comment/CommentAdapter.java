package com.shangame.fiction.ui.bookdetail.comment;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fiction.bar.R;
import com.shangame.fiction.core.base.WrapRecyclerViewAdapter;
import com.shangame.fiction.net.response.BookComment;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.widget.GlideApp;

import java.util.HashMap;
import java.util.Map;

/**
 * 评论Adapter
 * Create by Speedy on 2018/7/30
 */
public class CommentAdapter extends WrapRecyclerViewAdapter<BookComment, CommentViewHolder> {

    private Activity mActivity;

    private CommentPresenter commentPresenter;

    private long bookId;

    public CommentAdapter(Activity activity, CommentPresenter commentPresenter, long bookId) {
        mActivity = activity;
        this.commentPresenter = commentPresenter;
        this.bookId = bookId;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        CommentViewHolder commentViewHolder = new CommentViewHolder(view);
        return commentViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder viewHolder, int position) {
        final BookComment bookComment = data.get(position);
        viewHolder.tvCommentUserName.setText(bookComment.nickname);
        viewHolder.tvCommentContent.setText(bookComment.comment);
        viewHolder.tvCommentContent.setMaxLines(2);
        viewHolder.tvCommentTime.setText(bookComment.creatortime);
        viewHolder.tvCommentCount.setText(String.valueOf(bookComment.replycount));
        viewHolder.tvCommentLike.setText(String.valueOf(bookComment.pracount));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelpyCommentActivity.lunchActivity(mActivity, bookComment, bookId);
            }
        });
        viewHolder.tvCommentCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelpyCommentActivity.lunchActivity(mActivity, bookComment, bookId);
            }
        });

        if (bookComment.state == 1) {
            Drawable drawable = mActivity.getResources().getDrawable(R.drawable.like_s);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            viewHolder.tvCommentLike.setCompoundDrawables(drawable, null, null, null);
        } else {
            Drawable drawable = mActivity.getResources().getDrawable(R.drawable.like_n);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            viewHolder.tvCommentLike.setCompoundDrawables(drawable, null, null, null);
        }

        GlideApp.with(mActivity)
                .load(bookComment.headimgurl)
                .placeholder(R.drawable.default_head)
                .centerCrop()
                .into(viewHolder.ivCommentUserIcon);

        viewHolder.tvCommentLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<>();
                map.put("cid", bookComment.comid);
                map.put("userid", UserInfoManager.getInstance(mActivity).getUserid());
                map.put("bookid", bookId);
                map.put("chapterid", 0);
                map.put("comuserid", bookComment.userid);
                commentPresenter.sendLike(map);
            }
        });

    }
}
