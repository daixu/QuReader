package com.shangame.fiction.ui.bookdetail.comment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiction.bar.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.manager.ImageLoader;
import com.shangame.fiction.core.utils.BigMagicIndicatorAdapter;
import com.shangame.fiction.core.utils.NormalMagicIndicatorAdapter;
import com.shangame.fiction.core.utils.StatusBarUtil;
import com.shangame.fiction.net.response.TaskAwardResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.BookDetail;
import com.shangame.fiction.ui.task.TaskAwardContacts;
import com.shangame.fiction.ui.task.TaskAwardPresenter;
import com.shangame.fiction.ui.task.TaskId;
import com.shangame.fiction.ui.task.TaskRewardPopupWindow;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * 书籍评论
 * Create by Speedy on 2018/7/25
 */
public class CommentActivity extends BaseActivity implements View.OnClickListener, TaskAwardContacts.View {

    private static final int HOT_COMMENT_TYPE = 1;
    private static final int NEW_COMMENT_TYPE = 0;

    private static final int SEND_COMMENT = 835;

    private ImageView ivBookCover;
    private ImageView ivBack;
    private TextView tvBookName;
    private TextView tvAuthorName;
    private TextView tvCommentCount;

    private MagicIndicator magicIndicator;
    private ViewPager mViewPager;
    private MyPagerAdapter myPagerAdapter;
    private List<Fragment> viewList = new ArrayList<Fragment>();

    private BookDetail bookDetail;
    private int commentCount;

    private CommentByTypeFragment hotCommentByTypeFragment;
    private CommentByTypeFragment newCommentByTypeFragment;

    private TaskAwardPresenter taskAwardPresenter;

    public static void lunchActivity(Activity activity, BookDetail bookDetail, int commentCount) {
        Intent intent = new Intent(activity, CommentActivity.class);
        intent.putExtra("BookDetail", bookDetail);
        intent.putExtra("commentCount", commentCount);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        StatusBarUtil.setTranslucentForImageView(this, 0, null);
        bookDetail = getIntent().getParcelableExtra("BookDetail");
        commentCount = getIntent().getIntExtra("commentCount", 0);
        initView();
        initBookData();
        taskAwardPresenter = new TaskAwardPresenter();
        taskAwardPresenter.attachView(this);
    }

    private void initView() {
        findViewById(R.id.ivPublicBack).setOnClickListener(this);

        TextView tvPublicTitle = findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText(R.string.book_comment);

        ivBookCover = findViewById(R.id.ivBookCover);
        ivBack = findViewById(R.id.ivBack);
        tvBookName = findViewById(R.id.tvBookName);
        tvAuthorName = findViewById(R.id.tvAuthorName);
        tvCommentCount = findViewById(R.id.tvCommentCount);

        hotCommentByTypeFragment = CommentByTypeFragment.newInstance(HOT_COMMENT_TYPE, bookDetail.bookid);
        newCommentByTypeFragment = CommentByTypeFragment.newInstance(NEW_COMMENT_TYPE, bookDetail.bookid);

        viewList.add(newCommentByTypeFragment);
        viewList.add(hotCommentByTypeFragment);

        mViewPager = findViewById(R.id.viewPager);

        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(myPagerAdapter);
        initMagicIndicator();

        findViewById(R.id.tvWriteComment).setOnClickListener(this);
    }

    private void initBookData() {
        tvBookName.setText(bookDetail.bookname);
        tvAuthorName.setText(getString(R.string.author_name, bookDetail.author));
        tvCommentCount.setText(getString(R.string.comment_count, commentCount));

        ImageLoader.with(mActivity)
                .loadCover(ivBookCover, bookDetail.bookcover);

        final RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.default_cover);
        options.transform(new BlurTransformation(100));

        Glide.with(mActivity)
                .asBitmap()
                .load(bookDetail.bookcover)
                .apply(options)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        ivBack.setBackground(new BitmapDrawable(resource));
                    }
                });
    }

    private void initMagicIndicator() {
        magicIndicator = findViewById(R.id.magic_indicator);
        final List<String> titleList = new ArrayList<>(2);
        titleList.add("最热书评");
        titleList.add("最新书评");
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        NormalMagicIndicatorAdapter bigMagicIndicatorAdapter = new NormalMagicIndicatorAdapter(mContext, mViewPager, ContextCompat.getColor(mContext, R.color.colorPrimary));
        bigMagicIndicatorAdapter.setTitleList(titleList);
        commonNavigator.setAdapter(bigMagicIndicatorAdapter);
        magicIndicator.setNavigator(commonNavigator);

        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }

    @Override
    public void getTaskAwardSuccess(TaskAwardResponse taskAwardResponse, int taskid) {
        TaskRewardPopupWindow taskRewardPopupWindow = new TaskRewardPopupWindow(mActivity);
        taskRewardPopupWindow.show(taskAwardResponse.desc, taskAwardResponse.number + "");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivPublicBack:
                finish();
                break;
            case R.id.tvWriteComment:
                SendCommentActivity.lunchActivityForResult(mActivity, bookDetail.bookid, 0, 0, SEND_COMMENT);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SEND_COMMENT) {
                hotCommentByTypeFragment.refresh();
                int receive = data.getIntExtra("receive", 1);
                if (receive == 0) {
                    long userid = UserInfoManager.getInstance(mContext).getUserid();
                    taskAwardPresenter.getTaskAward(userid, TaskId.COMMENT_BOOK, false);
                }
            }
        }
    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return getString(R.string.newest_comment);
            } else {
                return getString(R.string.hotest_comment);
            }
        }

        @Override
        public Fragment getItem(int position) {
            return viewList.get(position);
        }
    }
}
