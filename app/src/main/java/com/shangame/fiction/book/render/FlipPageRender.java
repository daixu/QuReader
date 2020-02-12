package com.shangame.fiction.book.render;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.shangame.fiction.book.drawer.ChargePageDrawer;
import com.shangame.fiction.book.drawer.CoverDrawer;
import com.shangame.fiction.book.drawer.PageDrawer;
import com.shangame.fiction.book.drawer.PageRefreshListener;
import com.shangame.fiction.book.drawer.RefreshDrawer;
import com.shangame.fiction.book.loader.ChapterLoader;
import com.shangame.fiction.book.loader.ChapterLoaderObserver;
import com.shangame.fiction.book.page.PageData;
import com.shangame.fiction.book.pageflip.Page;
import com.shangame.fiction.book.pageflip.PageFlip;
import com.shangame.fiction.book.pageflip.PageFlipState;
import com.shangame.fiction.core.utils.RedoUtil;
import com.shangame.fiction.ui.reader.ReadParameter;

import java.util.List;

/**
 * Create by Speedy on 2018/8/6
 */
public class FlipPageRender extends PageRender implements ChapterLoaderObserver {

    private static final String TAG = "FlipPageRender";

    private PageDrawer pageDrawer;

    private RefreshDrawer refreshDrawer;

    private ChargePageDrawer chargePageDrawer;

    private CoverDrawer coverDrawer;

    private RenderAdapter renderAdapter;

    private ChapterLoader chapterLoader;

    private boolean isLoading;
    private boolean isJumpBeforeChapter;//标记是否为跳转至前一章
    private FrameLayout mFrameLayout;
    private Activity mActivity;

    private boolean hasChanged = false;//标记当前 Page 页是否内容改变

    public FlipPageRender(Context context, PageFlip pageFlip, Handler handler) {
        super(context, pageFlip, handler);
        pageDrawer = new PageDrawer(mContext, mCanvas);
        refreshDrawer = new RefreshDrawer(mContext, mCanvas);
        chargePageDrawer = new ChargePageDrawer(mContext, mCanvas);
        coverDrawer = new CoverDrawer(mContext, mCanvas);
    }

    public void setLayoutView(FrameLayout frameLayout) {
        mFrameLayout = frameLayout;
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    public List<PageData> getChapterPageData(long chapterId) {
        return renderAdapter.getChapterPageData(chapterId);
    }

    public void resetPage() {
        PageData pageData = renderAdapter.getItem(mPageIndex);
        if (pageData != null) {
            mPageIndex = pageData.index;
            renderAdapter.resetPage(pageData);
        }
    }

    public void setRenderAdapter(RenderAdapter renderAdapter) {
        this.renderAdapter = renderAdapter;
        this.renderAdapter.registerRenderObserver(this);
    }

    public int getProgress() {
        PageData pageData = renderAdapter.getItem(mPageIndex);
        if (pageData != null) {
            return (int) pageData.percent;
        } else {
            return 0;
        }
    }

    public void setProgress(int progress) {
        PageData pageData = renderAdapter.getItem(mPageIndex);
        if (pageData != null) {
            mPageIndex = renderAdapter.getPageByProgress(pageData.chapterId, progress);
            hasChanged = true;
            refreshPage();
        }
    }

    public void refreshPage() {
        Message msg = Message.obtain();
        msg.what = MSG_REFRESH_FRAME;
        msg.arg1 = DRAW_FULL_PAGE;
        mHandler.sendMessage(msg);
    }

    public boolean showNextPage() {
        if (canFlipForward()) {
            mPageIndex++;
            refreshPage();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canFlipForward() {
        boolean hanNext = mPageIndex < getPageCount();
        if (getPageCount() - mPageIndex < 3) {
            if (chapterLoader != null) {
                if (mPageIndex < getPageCount() - 1) {//预加载
                    PageData pageData = renderAdapter.getItem(renderAdapter.getPageCount() - 1);
                    if (pageData != null && pageData.nextChapterId != 0) {
                        if (!isLoading && !RedoUtil.isQuickDoubleClick(System.currentTimeMillis())) {
                            isLoading = true;
                            chapterLoader.loadNextChapter(pageData.bookId, pageData.nextChapterId, false, 0);//提前预加载
                        }
                    } else {
                        isLoading = false;
                        return true;
                    }
                } else if (getPageCount() >= 1) {
                    PageData lastPageData = renderAdapter.getItem(mPageIndex);
                    if (lastPageData != null) {
                        if (lastPageData.nextChapterId != 0) {
                            isLoading = true;
                            chapterLoader.loadNextChapter(lastPageData.bookId, lastPageData.nextChapterId, true, 0);
                        } else {
                            isLoading = false;
                            chapterLoader.loadNextChapter(lastPageData.bookId, 0, false, 0);
                        }
                    } else {
                        isLoading = false;
                        chapterLoader.loadNextChapter(0, 0, false, 0);
                    }
                    return false;
                } else {
                    return false;
                }
            }
        }
        return hanNext;
    }

    @Override
    public int getPageCount() {
        return renderAdapter.getPageCount();
    }

    @Override
    public void onDrawFrame() {
        mPageFlip.deleteUnusedTextures();
        final Page page = mPageFlip.getFirstPage();
        if (mDrawCommand == DRAW_MOVING_FRAME ||
                mDrawCommand == DRAW_ANIMATING_FRAME) {
            if (mPageFlip.getFlipState() == PageFlipState.FORWARD_FLIP) {
                // check if second texture of first page is valid, if not,
                // create new one
                if (!page.isSecondTextureSet()) {
                    drawPage(mPageIndex + 1, 0);
                    page.setSecondTexture(mBitmap);
                }
            }
            // in backward flip, check first texture of first page is valid
            else if (!page.isFirstTextureSet()) {
                drawPage(--mPageIndex, 1);
                page.setFirstTexture(mBitmap);
            }
            // draw frame for page flip
            mPageFlip.drawFlipFrame();
        }
        // draw stationary page without flipping
        else if (mDrawCommand == DRAW_FULL_PAGE) {
            if (hasChanged || !page.isFirstTextureSet()) {
                hasChanged = false;
                drawPage(mPageIndex, -1);
                page.setFirstTexture(mBitmap);
            }
            mPageFlip.drawPageFrame();

            Message msg = Message.obtain();
            msg.what = MSG_CHANGE_FRAME;
            mHandler.sendMessage(msg);
        }
        // 3. send message to main thread to notify drawing is ended so that
        // we can continue to calculate next animation frame if need.
        // Remember: the drawing operation is always in GL thread instead of
        // main thread
        Message msg = Message.obtain();
        msg.what = MSG_ENDED_DRAWING_FRAME;
        msg.arg1 = mDrawCommand;
        mHandler.sendMessage(msg);
    }

    private void drawPage(int pageIndex, int method) {
        if (renderAdapter != null) {
            PageData pageData = renderAdapter.getItem(pageIndex);
            if (pageData != null) {
                if (pageData.isCoverPage) {
                    coverDrawer.initPage(pageData, new PageRefreshListener() {
                        @Override
                        public void onPageRefresh(PageData oldData) {
                            refreshPage();
                        }
                    });
                    coverDrawer.onDraw();
                } else if (pageData.isVipPage) {
                    chargePageDrawer.initPage(pageData);
                    chargePageDrawer.onDraw();
                } else {
                    pageDrawer.initPage(pageData, new PageRefreshListener() {
                        @Override
                        public void onPageRefresh(PageData oldData) {
                            // refreshPage();
                        }
                    });
                    pageDrawer.setActivity(mActivity);
                    pageDrawer.setLayoutView(mFrameLayout);
                    Log.e("hhh", "onDraw---111---");
                    pageDrawer.onDraw();
                }
            } else {
                refreshDrawer.init();
                refreshDrawer.onDraw();
            }
        } else {
            refreshDrawer.init();
            refreshDrawer.onDraw();
        }
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        if (mBitmap != null) {
            mBitmap.recycle();
        }
        Page page = mPageFlip.getFirstPage();
        mBitmap = Bitmap.createBitmap((int) page.width(), (int) page.height(),
                Bitmap.Config.RGB_565);
        mCanvas.setBitmap(mBitmap);
    }

    @Override
    public boolean onEndedDrawing(int what) {
        if (what == DRAW_ANIMATING_FRAME) {
            boolean isAnimating = mPageFlip.animating();
            // continue animating
            if (isAnimating) {
                mDrawCommand = DRAW_ANIMATING_FRAME;
                return true;
            }
            // animation is finished
            else {
                final PageFlipState state = mPageFlip.getFlipState();
                // update page number for backward flip
                if (state == PageFlipState.END_WITH_BACKWARD) {
                    // don't do anything on page number since mPageIndex is always
                    // represents the FIRST_TEXTURE no;
                }
                // update page number and switch textures for forward flip
                else if (state == PageFlipState.END_WITH_FORWARD) {
                    mPageFlip.getFirstPage().setFirstTextureWithSecond();
                    mPageIndex++;
                    mPageFlip.setFlipState(PageFlipState.END_WITH_FINISH);
                }
                mDrawCommand = DRAW_FULL_PAGE;
                ReadParameter.getInstance().setResume(false);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean handleClick(float x, float y) {
        PageData pageData = getCurrentPageData();
        if (pageData != null) {
            if (pageData.isVipPage) {
                return chargePageDrawer.handleClick(x, y);
            } else if (pageData.isCoverPage) {
                return false;
            } else if (pageDrawer != null && !pageData.hasAdPage) {
                return pageDrawer.handleClick(x, y);
            }
        }
        return false;
    }

    public PageData getCurrentPageData() {
        if (null != renderAdapter) {
            PageData pageData = renderAdapter.getItem(mPageIndex);
            if (null != pageData) {
                return pageData;
            } else {
                pageData = new PageData();
                return pageData;
            }
        } else {
            PageData pageData = new PageData();
            return pageData;
        }
        // return renderAdapter.getItem(mPageIndex);
    }

    @Override
    public boolean canFlipBackward() {
        if (mPageIndex > 0) {
            mPageFlip.getFirstPage().setSecondTextureWithFirst();
            return true;
        } else {
            if (chapterLoader != null) {
                PageData pageData = renderAdapter.getItem(0);
                if (pageData != null) {
                    if (!isLoading && !RedoUtil.isQuickDoubleClick(System.currentTimeMillis())) {
                        isLoading = true;
                        chapterLoader.loadBeforeChapter(pageData.bookId, pageData.beforeChapterId);
                    }
                } else {
                    isJumpBeforeChapter = false;
                    isLoading = true;
                    chapterLoader.loadBeforeChapter(0, 0);
                }
            }
            return false;
        }
    }

    public boolean showBeforePage() {
        if (canFlipBackward()) {
            mPageIndex--;
            refreshPage();
            return true;
        } else {
            return false;
        }
    }

    public void setChapterLoader(ChapterLoader chapterLoader) {
        this.chapterLoader = chapterLoader;
    }

    public void jumpToNextChapter() {
        PageData pageData = renderAdapter.getItem(mPageIndex);
        if (pageData != null) {
            int newIndex = mPageIndex;
            for (int i = mPageIndex; i < getPageCount(); i++) {
                if (pageData.chapterId != renderAdapter.getItem(i).chapterId) {
                    break;
                }
                newIndex++;
            }
            if (newIndex < getPageCount()) {
                mPageIndex = newIndex;
                refreshPage();
            } else {
                mPageIndex = getPageCount();
                isLoading = true;
                chapterLoader.loadNextChapter(pageData.bookId, pageData.nextChapterId, true, 0);
            }
        }
    }

    public void jumpToBeforeChapter() {
        PageData pageData = renderAdapter.getItem(mPageIndex);
        if (pageData != null) {
            int newIndex = mPageIndex;
            for (int i = mPageIndex; i >= 0; i--) {
                PageData temp = renderAdapter.getItem(i);
                if (pageData.chapterId != temp.chapterId && temp.index == 0) {
                    break;
                }
                newIndex--;
            }

            PageData beforePageData = renderAdapter.getItem(newIndex);
            if (beforePageData != null) {
                mPageIndex = newIndex;
                refreshPage();
            } else {
                isJumpBeforeChapter = true;
                isLoading = true;
                mPageIndex = 0;
                chapterLoader.loadBeforeChapter(pageData.bookId, pageData.beforeChapterId);
            }
        }
    }

    @Override
    public void notifyChapterLoadFinished(int showPageIndex, int type) {
        mPageIndex = showPageIndex;
        finishLoading();
        if (mPageIndex == getPageCount() - 1) {
            PageData pageData = getCurrentPageData();
            chapterLoader.loadNextChapter(pageData.bookId, pageData.nextChapterId, false, type);//提前预加载
        }
    }

    @Override
    public void notifyNextChapterLoadFinished(int newCount, int oldCount) {
        if (newCount > 100) {
            List<PageData> pageDataList = renderAdapter.getPageDataList();
            PageData pageData = renderAdapter.getItem(mPageIndex);
            for (int i = 0; i < pageDataList.size(); i++) {
                PageData temp = pageDataList.get(i);
                if (temp.chapterId == pageData.chapterId) {
                    break;
                } else {
                    pageDataList.remove(temp);
                    mPageIndex--;
                }
            }

        }
        finishLoading();
    }

    @Override
    public void notifyBeforeChapterLoadFinished(int newCount, int oldCount) {
        if (isJumpBeforeChapter) {
            mPageIndex = 0;
        } else {
            mPageIndex = newCount - oldCount - 1;//更新索引位置
            if (mPageIndex < 0) {
                mPageIndex = 0;
            }
        }
        finishLoading();
    }

    @Override
    public void notifyResetPageSuccess() {
        updateConfig();
        Page firstPage = mPageFlip.getFirstPage();
        if (firstPage != null) {
            firstPage.deleteAllTextures();
        }
        refreshPage();
    }

    public void updateConfig() {
        hasChanged = true;
        pageDrawer.updateConfig();
        refreshPage();
    }

    @Override
    public void notifyJumpToBookMarkPage(int index) {
        mPageIndex = index;
        refreshPage();
    }

    @Override
    public void notifyReplaceChargePage() {
        mPageIndex = 0;
        hasChanged = true;
        refreshPage();
    }

    public void finishLoading() {
        isLoading = false;
        hasChanged = true;
        refreshPage();
    }

    public void setOnPayChapterClickListener(View.OnClickListener onPayChapterClickListener) {
        this.chargePageDrawer.setOnPayChapterClickListener(onPayChapterClickListener);
    }

    public void setOnPayMoreChapterClickListener(View.OnClickListener onPayMoreChapterClickListener) {
        this.chargePageDrawer.setOnPayMoreChapterClickListener(onPayMoreChapterClickListener);
    }

    public void setOnAutoPayNextChapterListener(View.OnClickListener onAutoPayNextChapterListener) {
        this.chargePageDrawer.setOnAutoPayNextChapterListener(onAutoPayNextChapterListener);
    }

    public void setOnMenuRegionClickListener(View.OnClickListener onMenuRegionClickListener) {
        this.pageDrawer.setOnMenuRegionClickListener(onMenuRegionClickListener);
    }

    public void setOnOpenVideoRegionClickListener1(View.OnClickListener onOpenVideoRegionClickListener) {
        this.pageDrawer.setOnOpenVideoRegionClickListener1(onOpenVideoRegionClickListener);
    }

    public void setOnOpenMenuRegionClickListener(View.OnClickListener onOpenMenuRegionClickListener) {
        this.chargePageDrawer.setOnMenuRegionClickListener(onOpenMenuRegionClickListener);
    }
}
