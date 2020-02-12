package com.shangame.fiction.ui.signin;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiction.bar.R;
import com.lxj.xpopup.core.CenterPopupView;
import com.shangame.fiction.core.constant.BroadcastAction;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.net.response.RecommendBookResponse;
import com.shangame.fiction.net.response.SignInInfoResponse;
import com.shangame.fiction.net.response.SignInResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.ui.bookdetail.BookDetailActivity;
import com.shangame.fiction.widget.GlideApp;

/**
 * @author hhh
 */
public class SignInPopupWindow extends CenterPopupView implements SignInContract.View {
    private SignInResponse mResponse;
    private RecommendBookResponse.RecDataBean mRecDataBean;
    private SignInPresenter mPresenter;
    private Context mContext;
    private ImageView mImageCover;
    private TextView mTextBookName;
    private TextView mTextSynopsis;
    private TextView mTextInfo;

    public SignInPopupWindow(@NonNull Context context) {
        super(context);
    }

    public SignInPopupWindow(@NonNull Context context, SignInResponse response) {
        super(context);
        mContext = context;
        mResponse = response;
    }

    private void initPresenter() {
        mPresenter = new SignInPresenter();
        mPresenter.attachView(this);
    }

    @Override
    protected void onShow() {
        super.onShow();
        initPresenter();
        long userId = UserInfoManager.getInstance(mContext).getUserid();
        mPresenter.getRecommendBook(userId, 1);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.popup_window_sign_in;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
        initListener();
    }

    private void initView() {
        ImageView imageDayCount = findViewById(R.id.image_day_count);
        TextView textSignSynopsis = findViewById(R.id.text_sign_synopsis);
        if (null != mResponse) {
            textSignSynopsis.setText(mResponse.msg);
            switch (mResponse.days) {
                case 1:
                    imageDayCount.setImageResource(R.drawable.image_day_1);
                    break;
                case 2:
                    imageDayCount.setImageResource(R.drawable.image_day_2);
                    break;
                case 3:
                    imageDayCount.setImageResource(R.drawable.image_day_3);
                    break;
                case 4:
                    imageDayCount.setImageResource(R.drawable.image_day_4);
                    break;
                case 5:
                    imageDayCount.setImageResource(R.drawable.image_day_5);
                    break;
                case 6:
                    imageDayCount.setImageResource(R.drawable.image_day_6);
                    break;
                case 7:
                    imageDayCount.setImageResource(R.drawable.image_day_7);
                    break;
                default:
                    imageDayCount.setImageResource(R.drawable.image_day_1);
                    break;
            }
        }

        mImageCover = findViewById(R.id.image_cover);
        mTextBookName = findViewById(R.id.text_book_name);
        mTextSynopsis = findViewById(R.id.text_synopsis);
        mTextInfo = findViewById(R.id.text_info);
    }

    private void initListener() {
        findViewById(R.id.img_sign_in_completed).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        findViewById(R.id.layout_book_info).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                BookDetailActivity.lunchActivity(mContext, mRecDataBean.bookid, ApiConstant.ClickType.FROM_CLICK);
                dismiss();
            }
        });
    }

    @Override
    public void signInSuccess(SignInResponse response) {

    }

    @Override
    public void getSignInInfoSuccess(SignInInfoResponse response) {

    }

    @Override
    public void getRecommendBookSuccess(RecommendBookResponse response) {
        if (response.recdata != null && response.recdata.size() > 0) {
            mRecDataBean = response.recdata.get(0);
            if (null != mRecDataBean) {
                mTextBookName.setText(mRecDataBean.bookname);
                mTextSynopsis.setText(mRecDataBean.synopsis);

                mTextInfo.setText(mRecDataBean.author + "·" + mRecDataBean.classname);

                GlideApp.with(mContext)
                        .load(mRecDataBean.bookcover)
                        .placeholder(R.drawable.default_cover)
                        .centerCrop()
                        .into(mImageCover);
            }
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(BroadcastAction.SIGN_SUCCESS_ACTION));
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
