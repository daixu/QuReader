package com.shangame.fiction.ui.reader;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.storage.model.BookMark;
import com.shangame.fiction.storage.model.BookParagraph;
import com.shangame.fiction.storage.model.ChapterInfo;

import java.util.List;

/**
 * Create by Speedy on 2018/7/26
 */
public interface BookLoadContract {

    interface View extends BaseContract.BaseView{
        void getChapterSuccess(int advertopen, ChapterInfo chapterInfo, List<BookParagraph> bookParagraphList, int type);
        void getNextChapterSuccess(int advertopen, ChapterInfo chapterInfo,List<BookParagraph> bookParagraphList);
        void getBeforeChapterSuccess(int advertopen, ChapterInfo chapterInfo, List<BookParagraph> bookParagraphList);
        void getBookMarkChapterSuccess(int advertopen, ChapterInfo chapterInfo, List<BookParagraph> bookParagraphList,BookMark bookMark);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void getChapter(long userid, long bookid , long chapterid, int type);
        void getNextChapter(long userid, long bookid , long chapterid);
        void getBeforeChapter(long userid, long bookid , long chapterid);
        void jumpToBookMarkChapter(long userid, long bookid , long chapterid,BookMark bookMark);
    }
}
