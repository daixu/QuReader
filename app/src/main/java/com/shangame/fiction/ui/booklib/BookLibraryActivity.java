package com.shangame.fiction.ui.booklib;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fiction.bar.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shangame.fiction.adapter.BookLibraryAdapter;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.net.response.GetBookLibraryTypeResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.ui.listen.lib.ListenLibraryDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 书库
 * Create by Speedy on 2018/8/1
 */
@Route(path = "/ss/book/library")
public class BookLibraryActivity extends BaseActivity implements View.OnClickListener, BookLibraryContracts.View {

    @Autowired
    int type;
    private LinearLayout mLayoutGirl;
    private View mViewGirl;
    private TextView mTvGirl;
    private LinearLayout mLayoutBoy;
    private View mViewBoy;
    private TextView mTvBoy;
    private LinearLayout mLayoutListen;
    private View mViewListen;
    private TextView mTvListen;

    private BookLibraryPresenter mPresenter;
    private BookLibraryAdapter mAdapter;

    private List<GetBookLibraryTypeResponse.ClassdataBean> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setStatusBarColor(R.color.colorPrimary);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_library);
        ARouter.getInstance().inject(this);

        initView();
        initPresenter();
        initData();

        Log.e("hhh", "type= " + type);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    private void initView() {
        mLayoutGirl = findViewById(R.id.layout_girl);
        mViewGirl = findViewById(R.id.view_girl);
        mTvGirl = findViewById(R.id.tv_girl);
        mLayoutBoy = findViewById(R.id.layout_boy);
        mViewBoy = findViewById(R.id.view_boy);
        mTvBoy = findViewById(R.id.tv_boy);
        mLayoutListen = findViewById(R.id.layout_listen);
        mViewListen = findViewById(R.id.view_listen);
        mTvListen = findViewById(R.id.tv_listen);
        RecyclerView recyclerSort = findViewById(R.id.recycler_sort);
        findViewById(R.id.img_back).setOnClickListener(this);
        mLayoutGirl.setOnClickListener(this);
        mLayoutBoy.setOnClickListener(this);
        mLayoutListen.setOnClickListener(this);

        recyclerSort.setLayoutManager(new GridLayoutManager(mContext, 2));
        mAdapter = new BookLibraryAdapter(R.layout.item_book_library, mList);
        recyclerSort.setAdapter(mAdapter);

        switch (type) {
            case 0:
                setGirlChoose();
                break;
            case 1:
                setBoyChoose();
                break;
            case 2:
                setListenChoose();
                break;
            default:
                break;
        }

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GetBookLibraryTypeResponse.ClassdataBean classDataBean = mList.get(position);
                if (type == 2) {
                    ListenLibraryDetailActivity.lunchActivity(mActivity, classDataBean.classid, classDataBean.classname);
                } else {
                    BookLibraryDetailActivity.lunchActivity(mActivity, classDataBean.classid, classDataBean.classname);
                }
            }
        });
    }

    private void initPresenter() {
        mPresenter = new BookLibraryPresenter();
        mPresenter.attachView(this);
    }

    private void initData() {
        long userId = UserInfoManager.getInstance(mContext).getUserid();
        int channel = 1;
        switch (type) {
            case 0:
                channel = 1;
                break;
            case 1:
                channel = 0;
                break;
            case 2:
                channel = 3;
                break;
            default:
                break;
        }
        mPresenter.getBookLibraryType(userId, channel);
    }

    private void setGirlChoose() {
        mLayoutGirl.setBackgroundColor(Color.parseColor("#FFFFFF"));
        mLayoutBoy.setBackgroundColor(Color.parseColor("#F6F6F6"));
        mLayoutListen.setBackgroundColor(Color.parseColor("#F6F6F6"));
        mTvGirl.setTextColor(Color.parseColor("#f09c55"));
        mTvBoy.setTextColor(Color.parseColor("#333333"));
        mTvListen.setTextColor(Color.parseColor("#333333"));
        mViewGirl.setVisibility(View.VISIBLE);
        mViewBoy.setVisibility(View.INVISIBLE);
        mViewListen.setVisibility(View.INVISIBLE);

        mLayoutGirl.setEnabled(false);
        mLayoutBoy.setEnabled(true);
        mLayoutListen.setEnabled(true);
    }

    private void setBoyChoose() {
        mLayoutGirl.setBackgroundColor(Color.parseColor("#F6F6F6"));
        mLayoutBoy.setBackgroundColor(Color.parseColor("#FFFFFF"));
        mLayoutListen.setBackgroundColor(Color.parseColor("#F6F6F6"));
        mTvGirl.setTextColor(Color.parseColor("#333333"));
        mTvBoy.setTextColor(Color.parseColor("#f09c55"));
        mTvListen.setTextColor(Color.parseColor("#333333"));
        mViewGirl.setVisibility(View.INVISIBLE);
        mViewBoy.setVisibility(View.VISIBLE);
        mViewListen.setVisibility(View.INVISIBLE);

        mLayoutGirl.setEnabled(true);
        mLayoutBoy.setEnabled(false);
        mLayoutListen.setEnabled(true);
    }

    private void setListenChoose() {
        mLayoutGirl.setBackgroundColor(Color.parseColor("#F6F6F6"));
        mLayoutBoy.setBackgroundColor(Color.parseColor("#F6F6F6"));
        mLayoutListen.setBackgroundColor(Color.parseColor("#FFFFFF"));
        mTvGirl.setTextColor(Color.parseColor("#333333"));
        mTvBoy.setTextColor(Color.parseColor("#333333"));
        mTvListen.setTextColor(Color.parseColor("#f09c55"));
        mViewGirl.setVisibility(View.INVISIBLE);
        mViewBoy.setVisibility(View.INVISIBLE);
        mViewListen.setVisibility(View.VISIBLE);

        mLayoutGirl.setEnabled(true);
        mLayoutBoy.setEnabled(true);
        mLayoutListen.setEnabled(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.layout_girl: {
                setGirlChoose();
                type = 0;
                long userId = UserInfoManager.getInstance(mContext).getUserid();
                mPresenter.getBookLibraryType(userId, 1);
            }
            break;
            case R.id.layout_boy: {
                setBoyChoose();
                type = 1;
                long userId = UserInfoManager.getInstance(mContext).getUserid();
                mPresenter.getBookLibraryType(userId, 0);
            }
            break;
            case R.id.layout_listen: {
                setListenChoose();
                type = 2;
                long userId = UserInfoManager.getInstance(mContext).getUserid();
                mPresenter.getBookLibraryType(userId, 3);
            }
            break;
            default:
                break;
        }
    }

    @Override
    public void getBookLibraryTypeSuccess(GetBookLibraryTypeResponse response) {
        mList.clear();
        mList.addAll(response.classdata);
        mAdapter.setNewData(mList);
    }
}
