package com.shangame.fiction.ui.bookrack;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.AlbumChapterDetailResponse;
import com.shangame.fiction.net.response.BookRackResponse;
import com.shangame.fiction.net.response.RecommendBookResponse;
import com.shangame.fiction.net.response.SignInInfoResponse;
import com.shangame.fiction.net.response.SignInResponse;
import com.shangame.fiction.storage.model.UserInfo;

/**
 * Create by Speedy on 2018/8/22
 */
public interface BookRackContacts {

    interface View extends BaseContract.BaseView {
        void getBookListSuccess(BookRackResponse response);

        void getRecommendBookSuccess(RecommendBookResponse response);

        void getSignInInfoSuccess(SignInInfoResponse response);

        void getAlbumChapterDetailSuccess(AlbumChapterDetailResponse response, int albumId, int cid);

        void getAlbumChapterDetailFailure(String msg);

        void getUserInfoSuccess(UserInfo userInfo);

        void signInSuccess(SignInResponse response);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void getBookList(int userId, int maleChannel, int page, int pageSize);

        void getRecommendBook(long userId, int pageSize);

        void getAlbumChapterDetail(long userId, int albumId, int cid, String deviceId);

        void getSignInInfo(long userId);

        void getUserInfo(long userId);

        void signIn(long userId);
    }
}
