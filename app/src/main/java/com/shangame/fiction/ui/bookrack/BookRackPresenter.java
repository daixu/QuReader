package com.shangame.fiction.ui.bookrack;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.AlbumChapterDetailResponse;
import com.shangame.fiction.net.response.AlbumChapterResponse;
import com.shangame.fiction.net.response.BookRackResponse;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.net.response.RecommendBookResponse;
import com.shangame.fiction.net.response.SignInInfoResponse;
import com.shangame.fiction.net.response.SignInResponse;
import com.shangame.fiction.storage.model.UserInfo;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/8/22
 */
public class BookRackPresenter extends RxPresenter<BookRackContacts.View> implements BookRackContacts.Presenter<BookRackContacts.View> {

    @Override
    public void getBookList(int userId, int maleChannel, int page, int pageSize) {
        Observable<HttpResult<BookRackResponse>> observable = ApiManager.getInstance().getBookRackList(userId, maleChannel, page, pageSize);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<BookRackResponse>>() {
            @Override
            public void accept(HttpResult<BookRackResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.getBookListSuccess(result.data);
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

    @Override
    public void getAlbumChapterDetail(long userId, final int albumId, final int cid, String deviceId) {
        Observable<HttpResult<AlbumChapterDetailResponse>> observable = ApiManager.getInstance().getAlbumChapterDetail(userId, albumId, cid, deviceId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<AlbumChapterDetailResponse>>() {
            @Override
            public void accept(HttpResult<AlbumChapterDetailResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.getAlbumChapterDetailSuccess(result.data, albumId, cid);
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getUserInfo(long userId) {
        Observable<HttpResult<UserInfo>> observable = ApiManager.getInstance().getUserInfo(userId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<UserInfo>>() {
            @Override
            public void accept(HttpResult<UserInfo> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.getUserInfoSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }

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
}
