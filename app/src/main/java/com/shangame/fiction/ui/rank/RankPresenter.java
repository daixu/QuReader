package com.shangame.fiction.ui.rank;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.net.response.RankResponse;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/8/22
 */
public class RankPresenter extends RxPresenter<RankContacts.View> implements RankContacts.Prestener<RankContacts.View> {

    @Override
    public void getRankList(int userId, int maleChannel, int dayType) {
        Observable<HttpResult<RankResponse>> observable = ApiManager.getInstance().getRankList(userId, maleChannel, dayType);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<RankResponse>>() {
            @Override
            public void accept(HttpResult<RankResponse> result) throws Exception {
                if (HttpResultManager.verify(result, mView)) {
                    mView.getRankListSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }
}
