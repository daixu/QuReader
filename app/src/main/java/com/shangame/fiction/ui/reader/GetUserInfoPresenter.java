package com.shangame.fiction.ui.reader;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.net.response.ReadTimeResponse;
import com.shangame.fiction.net.response.TaskAwardResponse;
import com.shangame.fiction.storage.model.UserInfo;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/9/3
 */
public class GetUserInfoPresenter extends RxPresenter<GetUserInfoContracts.View> implements GetUserInfoContracts.Presenter<GetUserInfoContracts.View> {

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
    public void setAddAdvertLog(long userId, long bookId, final long chapterId) {
        Observable<HttpResult<Object>> observable = ApiManager.getInstance().setAddAdvertLog(userId, bookId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<Object>>() {
            @Override
            public void accept(HttpResult<Object> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.setAddAdvertLogSuccess(chapterId);
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void setReceiveLog(long userId, int taskLogId) {
        Observable<HttpResult<TaskAwardResponse>> observable = ApiManager.getInstance().setReceiveLog(userId, taskLogId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<TaskAwardResponse>>() {
            @Override
            public void accept(HttpResult<TaskAwardResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.setReceiveLogSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void sendReadTime(long userId, int type) {
        Observable<HttpResult<ReadTimeResponse>> observable = ApiManager.getInstance().sendReadTime(userId, 0, type);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<ReadTimeResponse>>() {
            @Override
            public void accept(HttpResult<ReadTimeResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.sendReadTimeSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void sendOfflineReadTime(int type) {
        Observable<HttpResult<ReadTimeResponse>> observable = ApiManager.getInstance().sendOfflineReadTime(0, type);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<ReadTimeResponse>>() {
            @Override
            public void accept(HttpResult<ReadTimeResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.sendReadTimeSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }
}
