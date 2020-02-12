package com.shangame.fiction.ui.signin;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.net.response.RecommendBookResponse;
import com.shangame.fiction.net.response.SignInInfoResponse;
import com.shangame.fiction.net.response.SignInResponse;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/8/24
 */
public class SignInPresenter extends RxPresenter<SignInContract.View> implements SignInContract.Presenter<SignInContract.View> {

    @Override
    public void signIn(long userId) {
        if (mView != null) {
            mView.showLoading();
        }
        Observable<HttpResult<SignInResponse>> observable = ApiManager.getInstance().signIn(userId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<SignInResponse>>() {
            @Override
            public void accept(HttpResult<SignInResponse> result) throws Exception {
                if (mView != null) {
                    mView.dismissLoading();
                    if (HttpResultManager.verify(result, mView)) {
                        mView.signInSuccess(result.data);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getSignInInfo(long userId) {
        if (mView != null) {
            mView.showLoading();
        }
        Observable<HttpResult<SignInInfoResponse>> observable = ApiManager.getInstance().getSignInInfo(userId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<SignInInfoResponse>>() {
            @Override
            public void accept(HttpResult<SignInInfoResponse> result) throws Exception {
                if (mView != null) {
                    mView.dismissLoading();
                    if (HttpResultManager.verify(result, mView)) {
                        mView.getSignInInfoSuccess(result.data);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getRecommendBook(long userId, int pageSize) {
        Observable<HttpResult<RecommendBookResponse>> observable = ApiManager.getInstance().getRecommendBook(userId, pageSize);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<RecommendBookResponse>>() {
            @Override
            public void accept(HttpResult<RecommendBookResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.getRecommendBookSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }
}
