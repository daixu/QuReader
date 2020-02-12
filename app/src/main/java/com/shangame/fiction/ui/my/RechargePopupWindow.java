package com.shangame.fiction.ui.my;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.fiction.bar.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lxj.xpopup.core.CenterPopupView;
import com.shangame.fiction.adapter.RechargeAdapter;
import com.shangame.fiction.net.response.CreatWapOrderResponse;
import com.shangame.fiction.net.response.GetPayMenthodsResponse;
import com.shangame.fiction.net.response.GetRechargeConfigResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.UserInfo;
import com.shangame.fiction.ui.my.pay.PayPopupWindow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 充值弹框
 *
 * @author hhh
 */
public class RechargePopupWindow extends CenterPopupView implements RechargeContracts.View {
    private Context mContext;
    private Activity mActivity;
    private RechargePresenter mPresenter;
    private RechargeAdapter mAdapter;
    private List<GetRechargeConfigResponse.RechargeBean> mData = new ArrayList<>();
    private GetRechargeConfigResponse.RechargeBean currentRechargeBean;
    private TextView mTextAccountBalance;

    private PayPopupWindow payPopupWindow;

    public RechargePopupWindow(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public RechargePopupWindow(@NonNull Context context, @NonNull Activity activity) {
        super(context);
        this.mContext = context;
        this.mActivity = activity;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.popup_window_recharge;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
    }

    @Override
    protected void onShow() {
        super.onShow();
        initPresenter();
        long userId = UserInfoManager.getInstance(mContext).getUserid();
        mPresenter.getRechargeConfig(userId);
        mPresenter.getUserInfo(userId);
    }

    private void initPresenter() {
        mPresenter = new RechargePresenter(mContext);
        mPresenter.attachView(this);
    }

    private void initView() {
        mTextAccountBalance = findViewById(R.id.text_account_balance);
        RecyclerView recyclerRecharge = findViewById(R.id.recycler_recharge);
        recyclerRecharge.setLayoutManager(new GridLayoutManager(mContext, 2));
        mAdapter = new RechargeAdapter(R.layout.item_recharge, mData);
        recyclerRecharge.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GetRechargeConfigResponse.RechargeBean rechargeBean = mData.get(position);
                currentRechargeBean = rechargeBean;
                mPresenter.getPayMethods();
            }
        });

        findViewById(R.id.img_close).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void getPayMethodsSuccess(GetPayMenthodsResponse response) {
        payPopupWindow = new PayPopupWindow(mActivity);
        payPopupWindow.setPayItemList(response.paydata);
        payPopupWindow.setRechargeBean(currentRechargeBean);
        payPopupWindow.setOnPayClickListener(new PayPopupWindow.OnPayClickListener() {
            @Override
            public void onPay(Map<String, Object> map, int payMethod) {
                if (payPopupWindow != null && payPopupWindow.isShowing()) {
                    payPopupWindow.dismiss();
                }
                mPresenter.createWapOrder(map, payMethod);
            }

            @Override
            public void onPay2(String payUrl, Map<String, Object> map, int payMethod) {
                if (payPopupWindow != null && payPopupWindow.isShowing()) {
                    payPopupWindow.dismiss();
                }
                mPresenter.createWapOrder2(payUrl, map, payMethod);
            }
        });
        payPopupWindow.showAtLocation(mActivity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void getRechargeConfigSuccess(GetRechargeConfigResponse response) {
        mData.clear();
        mData.addAll(response.rechdata);
        mAdapter.setNewData(mData);
    }

    @Override
    public void wapCreateOrderSuccess(CreatWapOrderResponse response, int payMethod) {
        mPresenter.redirectRequest(response.skipurl, payMethod);
    }

    @Override
    public void getUserInfoSuccess(UserInfo userInfo) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("账户余额: ");
        long money = userInfo.money + userInfo.coin;
        stringBuilder.append(money);
        stringBuilder.append("阅读币");
        mTextAccountBalance.setText(stringBuilder);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void showNotNetworkView() {

    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showError(Throwable throwable) {

    }

    @Override
    public void lunchLoginActivity() {

    }
}
