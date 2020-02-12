package com.shangame.fiction.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiction.bar.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.core.manager.ImageLoader;
import com.shangame.fiction.storage.model.NewBookInfoRankingEntity;

import java.util.List;

/**
 * Create by Speedy on 2019/3/5
 */
public class BookStoreRankAdapter extends BaseQuickAdapter<NewBookInfoRankingEntity, BaseViewHolder> {

    public BookStoreRankAdapter(int layoutResId, @Nullable List<NewBookInfoRankingEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, NewBookInfoRankingEntity item) {
        ImageView imageCover = helper.getView(R.id.image_cover);

        ImageView imageRanking = helper.getView(R.id.image_ranking);
        TextView textRanking = helper.getView(R.id.text_ranking);
        ImageLoader.with(mContext).loadCover(imageCover, item.bookcover);
        helper.setText(R.id.text_book_name, item.bookname);
        String strNumber = "推荐:" + item.numbers;
        helper.setText(R.id.text_number, strNumber);
        switch (item.ranking) {
            case 1:
                imageRanking.setVisibility(View.VISIBLE);
                textRanking.setVisibility(View.GONE);
                imageRanking.setImageResource(R.drawable.image_first);
                break;
            case 2:
                imageRanking.setVisibility(View.VISIBLE);
                textRanking.setVisibility(View.GONE);
                imageRanking.setImageResource(R.drawable.image_second);
                break;
            case 3:
                imageRanking.setVisibility(View.VISIBLE);
                textRanking.setVisibility(View.GONE);
                imageRanking.setImageResource(R.drawable.image_third);
                break;
            default:
                imageRanking.setVisibility(View.GONE);
                textRanking.setVisibility(View.VISIBLE);
                String ranking = item.ranking + "";
                textRanking.setText(ranking);
                break;
        }
    }
}
