package com.shangame.fiction.ui.bookstore;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.ChoicenessResponse;
import com.shangame.fiction.net.response.OthersLookResponse;

/**
 * Create by Speedy on 2018/8/13
 */
public interface ChoicenessContacts {

    interface View extends BaseContract.BaseView {
        void getOthersLookDataSuccess(OthersLookResponse othersLookResponse);

        void getChoicenessDataSuccess(ChoicenessResponse choicenessResponse);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void othersLookData(int userid, int malechannel, int page, int pagesize, int status);

        void getChoicenessData(int userid, int pagecount);
    }
}
