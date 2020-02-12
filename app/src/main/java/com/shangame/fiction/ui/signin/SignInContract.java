package com.shangame.fiction.ui.signin;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.RecommendBookResponse;
import com.shangame.fiction.net.response.SignInInfoResponse;
import com.shangame.fiction.net.response.SignInResponse;

/**
 * Create by Speedy on 2018/7/23
 */
public interface SignInContract {

    interface View extends BaseContract.BaseView {
        void signInSuccess(SignInResponse response);

        void getSignInInfoSuccess(SignInInfoResponse response);

        void getRecommendBookSuccess(RecommendBookResponse response);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void signIn(long userId);

        void getSignInInfo(long userId);

        void getRecommendBook(long userId, int pageSize);
    }
}
