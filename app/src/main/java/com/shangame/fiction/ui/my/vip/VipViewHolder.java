package com.shangame.fiction.ui.my.vip;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiction.bar.R;

/**
 * Create by Speedy on 2018/8/23
 */
public class VipViewHolder extends RecyclerView.ViewHolder{

    ImageView ivVipIcon;
    TextView tvVipName;
    TextView tvVipIntro;

    public VipViewHolder(View itemView) {
        super(itemView);
        ivVipIcon = itemView.findViewById(R.id.ivVipIcon);
        tvVipName = itemView.findViewById(R.id.tvVipName);
        tvVipIntro = itemView.findViewById(R.id.tvVipIntro);
    }
}
