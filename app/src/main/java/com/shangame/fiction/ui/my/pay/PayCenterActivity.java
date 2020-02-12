package com.shangame.fiction.ui.my.pay;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiction.bar.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.base.WrapBaseAdapter;
import com.shangame.fiction.core.constant.BroadcastAction;
import com.shangame.fiction.net.response.CreatWapOrderResponse;
import com.shangame.fiction.net.response.GetPayMenthodsResponse;
import com.shangame.fiction.net.response.GetRechargeConfigResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.UserInfo;
import com.shangame.fiction.ui.my.account.PlayTourHistoryActivity;
import com.shangame.fiction.ui.my.account.coin.GiftCoinActivity;
import com.shangame.fiction.ui.my.pay.history.PayHistoryActivity;

import java.util.Map;

/**
 * 充值中心 Activity
 * Create by Speedy on 2018/7/23
 */
public class PayCenterActivity extends BaseActivity implements View.OnClickListener, PayContracts.View {

    private MyAdapter myAdapter;

    private PayPresenter payPresenter;
    private TextView mTextMoney;
    private TextView mTextCoin;

    private GetRechargeConfigResponse.RechargeBean currentRechargeBean;

    private PayPopupWindow payPopupWindow;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            showToast(getString(R.string.pay_success));
            UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
            payPresenter.getUserInfo(userInfo.userid);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setStatusBarColor(R.color.color_FFF9F1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_center);
        initView();
        initPresenter();
        initReceiver();
        loadData();
    }

    private void initView() {
        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("账户余额");

        findViewById(R.id.img_back).setOnClickListener(this);
        findViewById(R.id.tv_gift_balance).setOnClickListener(this);
        findViewById(R.id.tv_recharge_record).setOnClickListener(this);
        findViewById(R.id.tv_reward_record).setOnClickListener(this);

        mTextMoney = findViewById(R.id.text_money);
        mTextCoin = findViewById(R.id.text_coin);

        GridView gridView = findViewById(R.id.gridView);
        myAdapter = new MyAdapter(mActivity);
        gridView.setAdapter(myAdapter);
    }

    private void initPresenter() {
        payPresenter = new PayPresenter(mContext);
        payPresenter.attachView(this);
    }

    private void initReceiver() {
        IntentFilter intentFilter = new IntentFilter(BroadcastAction.PUSH_PAY_SUCCESS_ACTION);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, intentFilter);
    }

    private void loadData() {
        UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
        payPresenter.getRechargeConfig(userInfo.userid);
        payPresenter.getUserInfo(userInfo.userid);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mReceiver);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_gift_balance: {
                Intent intent = new Intent(mContext, GiftCoinActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.tv_recharge_record: {
                Intent intent = new Intent(mContext, PayHistoryActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.tv_reward_record: {
                Intent intent = new Intent(mContext, PlayTourHistoryActivity.class);
                startActivity(intent);
            }
            break;
            default:
                break;
        }
    }

    @Override
    public void getPayMethodsSuccess(final GetPayMenthodsResponse getPayMenthodsResponse) {
        payPopupWindow = new PayPopupWindow(mActivity);
        payPopupWindow.setPayItemList(getPayMenthodsResponse.paydata);
        payPopupWindow.setRechargeBean(currentRechargeBean);
        payPopupWindow.setOnPayClickListener(new PayPopupWindow.OnPayClickListener() {
            @Override
            public void onPay(Map<String, Object> map, int payMethod) {
                if (payPopupWindow != null && payPopupWindow.isShowing()) {
                    payPopupWindow.dismiss();
                }
                payPresenter.createWapOrder(map, payMethod);
            }

            @Override
            public void onPay2(String payUrl, Map<String, Object> map, int payMethod) {
                if (payPopupWindow != null && payPopupWindow.isShowing()) {
                    payPopupWindow.dismiss();
                }
                payPresenter.createWapOrder2(payUrl, map, payMethod);
            }
        });
        payPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void getRechargeConfigSuccess(GetRechargeConfigResponse getRechargeConfigResponse) {
        myAdapter.addAll(getRechargeConfigResponse.rechdata);
        currentRechargeBean = myAdapter.getItem(0);
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void wapCreateOrderSuccess(CreatWapOrderResponse creatWapOrderResponse, int payMethod) {
        payPresenter.redirectRequest(creatWapOrderResponse.skipurl, payMethod);
    }

    @Override
    public void getUserInfoSuccess(UserInfo userInfo) {
        mTextMoney.setText(userInfo.money + "");
        mTextCoin.setText(userInfo.coin + "");
        UserInfoManager.getInstance(mContext).saveUserInfo(userInfo);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvMoney;
        TextView tvCoin;
        TextView tvReward;
        ImageView imageType;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvMoney = itemView.findViewById(R.id.tvMoney);
            tvCoin = itemView.findViewById(R.id.tvCoin);
            tvReward = itemView.findViewById(R.id.tvReward);
            imageType = itemView.findViewById(R.id.image_type);
        }
    }

    class MyAdapter extends WrapBaseAdapter<GetRechargeConfigResponse.RechargeBean, MyViewHolder> {

        public MyAdapter(Activity activity) {
            super(activity);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.recharge_item, parent, false);
            MyViewHolder holder = new MyViewHolder(view);

            final GetRechargeConfigResponse.RechargeBean rechargeBean = getItem(position);
            holder.tvMoney.setText(getString(R.string.yuan, rechargeBean.price + ""));

            String strCoin = rechargeBean.remark;
            try {
                Spannable span = new SpannableString(strCoin);
                span.setSpan(new AbsoluteSizeSpan(20, true), 0, strCoin.indexOf("阅读币"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#333333"));
                span.setSpan(colorSpan, 0, strCoin.indexOf("阅读币"), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                holder.tvCoin.setText(span);
            } catch (IndexOutOfBoundsException e) {
                Log.e("hhh", e.getMessage());
                holder.tvCoin.setText(strCoin);
            }

            if (rechargeBean.rectype == 1) {
                holder.imageType.setVisibility(View.VISIBLE);
                holder.imageType.setImageResource(R.drawable.image_recharge_1);
            } else if (rechargeBean.rectype == 2) {
                holder.imageType.setVisibility(View.VISIBLE);
                holder.imageType.setImageResource(R.drawable.image_recharge_2);
            } else {
                holder.imageType.setVisibility(View.INVISIBLE);
            }

            if (rechargeBean.givenumber > 0) {
                holder.tvReward.setText("赠 " + rechargeBean.givenumber + "阅读币");
                holder.tvReward.setVisibility(View.VISIBLE);
            } else if (rechargeBean.coinnumber > 0) {
                holder.tvReward.setText("赠 " + rechargeBean.coinnumber + "阅读币");
                holder.tvReward.setVisibility(View.VISIBLE);
            } else {
                holder.tvReward.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentRechargeBean = rechargeBean;
                    payPresenter.getPayMethods();
                }
            });

            return view;
        }
    }
}
