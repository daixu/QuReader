package com.shangame.fiction.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiction.bar.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.net.response.GetRechargeConfigResponse;

import java.util.List;

public class RechargeAdapter extends BaseQuickAdapter<GetRechargeConfigResponse.RechargeBean, BaseViewHolder> {
    public RechargeAdapter(int layoutResId, @Nullable List<GetRechargeConfigResponse.RechargeBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, GetRechargeConfigResponse.RechargeBean item) {
        ImageView imageType = helper.getView(R.id.image_type);
        helper.setText(R.id.tvMoney, mContext.getString(R.string.yuan, item.price + ""));
        TextView tvReward = helper.getView(R.id.tvReward);

        String strCoin = item.remark;
        try {
            Spannable span = new SpannableString(strCoin);
            span.setSpan(new AbsoluteSizeSpan(20, true), 0, strCoin.indexOf("阅读币"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#333333"));
            span.setSpan(colorSpan, 0, strCoin.indexOf("阅读币"), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            helper.setText(R.id.tvCoin, span);
        } catch (IndexOutOfBoundsException e) {
            Log.e("hhh", e.getMessage());
            helper.setText(R.id.tvCoin, strCoin);
        }

        if (item.rectype == 1) {
            imageType.setVisibility(View.VISIBLE);
            imageType.setImageResource(R.drawable.image_recharge_1);
        } else if (item.rectype == 2) {
            imageType.setVisibility(View.VISIBLE);
            imageType.setImageResource(R.drawable.image_recharge_2);
        } else {
            imageType.setVisibility(View.INVISIBLE);
        }

        if (item.givenumber > 0) {
            tvReward.setText("赠 " + item.givenumber + "阅读币");
            tvReward.setVisibility(View.VISIBLE);
        } else if (item.coinnumber > 0) {
            tvReward.setText("赠 " + item.coinnumber + "阅读币");
            tvReward.setVisibility(View.VISIBLE);
        } else {
            tvReward.setVisibility(View.GONE);
        }
    }
}
