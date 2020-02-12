package com.shangame.fiction.ui.listen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fiction.bar.R;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shangame.fiction.adapter.BoyListenAdapter;
import com.shangame.fiction.adapter.DiscountAreaAdapter;
import com.shangame.fiction.adapter.GirlListenAdapter;
import com.shangame.fiction.adapter.MustListenAdapter;
import com.shangame.fiction.adapter.NetworkImageHolderView;
import com.shangame.fiction.adapter.OtherListenAdapter;
import com.shangame.fiction.adapter.SerializedLatestAdapter;
import com.shangame.fiction.core.base.BaseLazyFragment;
import com.shangame.fiction.net.response.AlbumDataResponse;
import com.shangame.fiction.net.response.AlbumModuleResponse;
import com.shangame.fiction.net.response.PictureConfigResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.ui.bookstore.BookStoreChannel;
import com.shangame.fiction.ui.bookstore.BookStoreFragment;
import com.shangame.fiction.ui.listen.menu.ListenMenuActivity;
import com.shangame.fiction.ui.listen.more.ListenMoreActivity;
import com.shangame.fiction.ui.search.SearchBookActivity;
import com.shangame.fiction.ui.web.WebViewActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 听书Fragment
 *
 * @author hhh
 */
public class ListenBookFragment extends BaseLazyFragment implements View.OnClickListener, ListenBookContacts.View {

    private SmartRefreshLayout mSmartRefreshLayout;
    private NestedScrollView mNestedScrollView;
    private ListenBookPresenter mPresenter;
    private View kindLayout;

    private RelativeLayout mLayoutTitle;
    private View mFakeStatusBar;
    private RelativeLayout mLayoutTop;
    private ConvenientBanner mConvenientBanner;

    private boolean hidden;
    private List<PictureConfigResponse.PicItem> mList = new ArrayList<>();

    private MustListenAdapter mustListenAdapter;
    private DiscountAreaAdapter mDiscountAdapter;
    private BoyListenAdapter mBoyListenAdapter;
    private GirlListenAdapter mGirlListenAdapter;
    private SerializedLatestAdapter mSerializedLatestAdapter;
    private OtherListenAdapter mOtherListenAdapter;
    private List<AlbumModuleResponse.CarDataBean> mustListenList = new ArrayList<>();
    private List<AlbumModuleResponse.DisDataBean> mDisDataList = new ArrayList<>();
    private List<AlbumModuleResponse.BoyDataBean> mBoyDataList = new ArrayList<>();
    private List<AlbumModuleResponse.GirlDataBean> mGirlDataList = new ArrayList<>();
    private List<AlbumModuleResponse.HotDataBean> mHotDataList = new ArrayList<>();
    private List<AlbumDataResponse.PageDataBean> mPageDataList = new ArrayList<>();

    private int updatePage;
    private int pageIndex = 1;
    private int type;

    public ListenBookFragment() {
        // Required empty public constructor
    }

    public static ListenBookFragment newInstance(int type) {
        ListenBookFragment fragment = new ListenBookFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    public void scrollToTop() {
        mNestedScrollView.scrollTo(0, 0);
    }

    @Override
    public void getAlbumModuleSuccess(AlbumModuleResponse response) {
        kindLayout.setVisibility(View.VISIBLE);
        mSmartRefreshLayout.finishRefresh();
        if (null != response) {
            if (null != response.cardata) {
                mustListenList.clear();
                mustListenList.addAll(response.cardata);
                mustListenAdapter.setNewData(mustListenList);
            }

            if (null != response.disdata) {
                mDisDataList.clear();
                List<AlbumModuleResponse.DisDataBean> list = new ArrayList<>();
                for (int i = 0; i < response.disdata.size(); i++) {
                    AlbumModuleResponse.DisDataBean dataBean = response.disdata.get(i);
                    if (i == 0) {
                        dataBean.type = 1;
                    } else {
                        dataBean.type = 2;
                    }
                    list.add(dataBean);
                }
                mDisDataList.addAll(list);
                mDiscountAdapter.setNewData(mDisDataList);
            }

            if (null != response.boydata) {
                mBoyDataList.clear();
                mBoyDataList.addAll(response.boydata);
                mBoyListenAdapter.setNewData(mBoyDataList);
            }

            if (null != response.grildata) {
                mGirlDataList.clear();
                mGirlDataList.addAll(response.grildata);
                mGirlListenAdapter.setNewData(mGirlDataList);
            }

            if (null != response.hotdata) {
                mHotDataList.clear();
                List<AlbumModuleResponse.HotDataBean> list = new ArrayList<>();
                for (int i = 0; i < response.hotdata.size(); i++) {
                    AlbumModuleResponse.HotDataBean dataBean = response.hotdata.get(i);
                    if (i == 0) {
                        dataBean.type = 1;
                    } else {
                        dataBean.type = 2;
                    }
                    list.add(dataBean);
                }
                mHotDataList.addAll(list);
                mSerializedLatestAdapter.setNewData(mHotDataList);
            }
        }
    }

    @Override
    public void getAlbumModuleFailure(String msg) {
        mSmartRefreshLayout.finishRefresh();
        mSmartRefreshLayout.finishLoadMore();
    }

    @Override
    public void getAlbumDataSuccess(AlbumDataResponse response) {
        mSmartRefreshLayout.finishRefresh();
        if (null != response) {
            mSmartRefreshLayout.finishLoadMore();
            mPageDataList.clear();
            pageIndex++;
            mPageDataList.addAll(response.pagedata);
            mOtherListenAdapter.setNewData(mPageDataList);
        }
    }

    @Override
    public void getAlbumDataFailure(String msg) {
        mSmartRefreshLayout.finishRefresh();
        mSmartRefreshLayout.finishLoadMore();
    }

    @Override
    public void getPictureConfigSuccess(final PictureConfigResponse response) {
        mList.addAll(response.picdata);
        mConvenientBanner.setPages(new CBViewHolderCreator() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, response.picdata)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        PictureConfigResponse.PicItem item = response.picdata.get(position);
                        if (item.bookid == 0) {
                            if (!TextUtils.isEmpty(item.linkurl)) {
                                WebViewActivity.lunchActivity(mActivity, "", item.linkurl);
                            }
                        } else {
                            ARouter.getInstance()
                                    .build("/ss/listen/detail")
                                    .withInt("albumId", item.bookid)
                                    .navigation();
                        }
                    }
                })
                .setCanLoop(response.picdata.size() > 1);

        if (response.picdata.size() > 1) {
            mConvenientBanner.startTurning(5000);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        hidden = getUserVisibleHint();
        Log.e("hhh", "hidden= " + hidden);
        if (mConvenientBanner != null) {
            if (isVisibleToUser) {
                mConvenientBanner.startTurning(5000);
            } else {
                mConvenientBanner.stopTurning();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_listen_book, container, false);
        initView(contentView);
        initListener(contentView);
        initBanner(contentView);
        return contentView;
    }

    private void initView(View contentView) {
        mSmartRefreshLayout = contentView.findViewById(R.id.smart_refresh_layout);
        mSmartRefreshLayout.setEnableLoadMore(false);
        mNestedScrollView = contentView.findViewById(R.id.nested_scroll_view);
        kindLayout = contentView.findViewById(R.id.kindLayout);

        mLayoutTitle = contentView.findViewById(R.id.layout_title);
        mFakeStatusBar = contentView.findViewById(R.id.fake_status_bar);
        if (type == 1) {
            mLayoutTitle.setVisibility(View.VISIBLE);
            mFakeStatusBar.setVisibility(View.VISIBLE);
        } else {
            mLayoutTitle.setVisibility(View.GONE);
            mFakeStatusBar.setVisibility(View.GONE);
        }

        RecyclerView recyclerMustListen = contentView.findViewById(R.id.recycler_must_listen);
        recyclerMustListen.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mustListenAdapter = new MustListenAdapter(R.layout.item_must_listen, mustListenList);
        recyclerMustListen.setAdapter(mustListenAdapter);

        RecyclerView recyclerDiscountArea = contentView.findViewById(R.id.recycler_discount_area);
        recyclerDiscountArea.setLayoutManager(new GridLayoutManager(mContext, 3));
        mDiscountAdapter = new DiscountAreaAdapter(mDisDataList);
        mDiscountAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                if (mDiscountAdapter.getItemViewType(position) == 1) {
                    return 3;
                }
                return 1;
            }
        });
        recyclerDiscountArea.setAdapter(mDiscountAdapter);

        RecyclerView recyclerSerializedLatest = contentView.findViewById(R.id.recycler_serialized_latest);
        recyclerSerializedLatest.setLayoutManager(new GridLayoutManager(mContext, 3));
        mSerializedLatestAdapter = new SerializedLatestAdapter(R.layout.item_listen_simple, mHotDataList);
        recyclerSerializedLatest.setAdapter(mSerializedLatestAdapter);

        RecyclerView recyclerGirlBoutique = contentView.findViewById(R.id.recycler_girl_boutique);
        recyclerGirlBoutique.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mGirlListenAdapter = new GirlListenAdapter(R.layout.item_must_listen, mGirlDataList);
        recyclerGirlBoutique.setAdapter(mGirlListenAdapter);

        RecyclerView recyclerBoyBoutique = contentView.findViewById(R.id.recycler_boy_boutique);
        recyclerBoyBoutique.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mBoyListenAdapter = new BoyListenAdapter(R.layout.item_must_listen, mBoyDataList);
        recyclerBoyBoutique.setAdapter(mBoyListenAdapter);

        RecyclerView recyclerOtherListen = contentView.findViewById(R.id.recycler_other_listen);
        recyclerOtherListen.setLayoutManager(new GridLayoutManager(mContext, 3));
        mOtherListenAdapter = new OtherListenAdapter(R.layout.item_listen_simple, mPageDataList);
        recyclerOtherListen.setAdapter(mOtherListenAdapter);
    }

    private void initListener(View contentView) {
        contentView.findViewById(R.id.tv_serialized_latest_more).setOnClickListener(this);
        contentView.findViewById(R.id.tv_girl_boutique_more).setOnClickListener(this);
        contentView.findViewById(R.id.tv_boy_boutique_more).setOnClickListener(this);

        contentView.findViewById(R.id.tv_must_listen_more).setOnClickListener(this);
        contentView.findViewById(R.id.tv_discount_area_more).setOnClickListener(this);

        contentView.findViewById(R.id.text_sort).setOnClickListener(this);
        contentView.findViewById(R.id.menu_rank_listen).setOnClickListener(this);
        contentView.findViewById(R.id.menu_complete_listen).setOnClickListener(this);
        contentView.findViewById(R.id.menu_end_listen).setOnClickListener(this);
        contentView.findViewById(R.id.menu_free_listen).setOnClickListener(this);
        contentView.findViewById(R.id.tv_search).setOnClickListener(this);

        contentView.findViewById(R.id.layout_refresh).setOnClickListener(this);

        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadData();
            }
        });

        mustListenAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mustListenAdapterItemClick(position);
            }
        });

        mDiscountAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                disDataAdapterItemClick(position);
            }
        });

        mBoyListenAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                boyListenAdapterItemClick(position);
            }
        });

        mGirlListenAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                girlListenAdapterItemClick(position);
            }
        });

        mSerializedLatestAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                serializedLatestAdapterItemClick(position);
            }
        });

        mOtherListenAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                otherListenAdapterItemClick(position);
            }
        });
    }

    private void initBanner(View view) {
        mLayoutTop = view.findViewById(R.id.layout_top);
        mConvenientBanner = view.findViewById(R.id.convenientBanner);

        mConvenientBanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float v, int i1) {
            }

            @Override
            public void onPageSelected(int position) {
                Log.e("hhh", "listen onPageSelected position= " + position);
                if (null != mList) {
                    PictureConfigResponse.PicItem picItem = mList.get(position);
                    if (TextUtils.isEmpty(picItem.color) || picItem.color.trim().length() != 7) {
                        mLayoutTop.setBackgroundColor(Color.parseColor("#822E30"));
                        if (type == 1) {
                            mLayoutTitle.setBackgroundColor(Color.parseColor("#822E30"));
                            mFakeStatusBar.setBackgroundColor(Color.parseColor("#822E30"));
                        } else {
                            if (null != getParentFragment()) {
                                if (hidden) {
                                    ((BookStoreFragment) getParentFragment()).setAppBarColor("#822E30");
                                }
                            }
                        }
                    } else {
                        mLayoutTop.setBackgroundColor(Color.parseColor(picItem.color));
                        if (type == 1) {
                            mLayoutTitle.setBackgroundColor(Color.parseColor(picItem.color));
                            mFakeStatusBar.setBackgroundColor(Color.parseColor(picItem.color));
                        } else {
                            if (null != getParentFragment()) {
                                if (hidden) {
                                    ((BookStoreFragment) getParentFragment()).setAppBarColor(picItem.color);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void loadData() {
        long userId = UserInfoManager.getInstance(mContext).getUserid();
        if (null != mPresenter) {
            mPresenter.getAlbumModule(userId, updatePage++);
            mPresenter.getPictureConfig(userId, BookStoreChannel.LISTEN);
        }
    }

    private void mustListenAdapterItemClick(int position) {
        if (null != mustListenList && mustListenList.size() > position) {
            AlbumModuleResponse.CarDataBean bean = mustListenList.get(position);
            if (null != bean) {
                jumpToDetail(bean.albumid);
            }
        }
    }

    private void disDataAdapterItemClick(int position) {
        if (null != mDisDataList && mDisDataList.size() > position) {
            AlbumModuleResponse.DisDataBean bean = mDisDataList.get(position);
            if (null != bean) {
                jumpToDetail(bean.albumid);
            }
        }
    }

    private void boyListenAdapterItemClick(int position) {
        if (null != mBoyDataList && mBoyDataList.size() > position) {
            AlbumModuleResponse.BoyDataBean bean = mBoyDataList.get(position);
            if (null != bean) {
                jumpToDetail(bean.albumid);
            }
        }
    }

    private void girlListenAdapterItemClick(int position) {
        if (null != mGirlDataList && mGirlDataList.size() > position) {
            AlbumModuleResponse.GirlDataBean bean = mGirlDataList.get(position);
            if (null != bean) {
                jumpToDetail(bean.albumid);
            }
        }
    }

    private void serializedLatestAdapterItemClick(int position) {
        if (null != mHotDataList && mHotDataList.size() > position) {
            AlbumModuleResponse.HotDataBean bean = mHotDataList.get(position);
            if (null != bean) {
                jumpToDetail(bean.albumid);
            }
        }
    }

    private void otherListenAdapterItemClick(int position) {
        if (null != mPageDataList && mPageDataList.size() > position) {
            AlbumDataResponse.PageDataBean bean = mPageDataList.get(position);
            if (null != bean) {
                jumpToDetail(bean.albumid);
            }
        }
    }

    private void jumpToDetail(int albumId) {
        ARouter.getInstance()
                .build("/ss/listen/detail")
                .withInt("albumId", albumId)
                .navigation();
    }

    public void initData() {
        loadData();
        loadMoreData();
        mLayoutTop.setBackgroundColor(Color.parseColor("#822E30"));
    }

    private void loadMoreData() {
        long userId = UserInfoManager.getInstance(mContext).getUserid();
        if (null != mPresenter) {
            mPresenter.getAlbumData(userId, pageIndex, 6, 0);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt("type");
        }
    }

    @Override
    protected void onLazyLoad() {
        initPresenter();
        loadData();
        // mSmartRefreshLayout.autoRefresh();
        loadMoreData();
    }

    private void initPresenter() {
        mPresenter = new ListenBookPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_sort: {
                ARouter.getInstance()
                        .build("/ss/book/library")
                        .withInt("type", 2)
                        .navigation();
            }
            break;
            case R.id.menu_rank_listen: {
                ARouter.getInstance()
                        .build("/ss/book/rank")
                        .withInt("type", 2)
                        .navigation();
            }
            break;
            case R.id.menu_complete_listen: {
                Intent intent = new Intent(mContext, ListenMenuActivity.class);
                intent.putExtra("status", 1);
                startActivity(intent);
            }
            break;
            case R.id.menu_end_listen: {
                Intent intent = new Intent(mContext, ListenMenuActivity.class);
                intent.putExtra("status", 2);
                startActivity(intent);
            }
            break;
            case R.id.menu_free_listen: {
                Intent intent = new Intent(mContext, ListenMenuActivity.class);
                intent.putExtra("status", 3);
                startActivity(intent);
            }
            break;
            case R.id.layout_refresh: {
                loadMoreData();
            }
            break;
            case R.id.tv_must_listen_more: {
                jumpToMore(1);
            }
            break;
            case R.id.tv_discount_area_more: {
                jumpToMore(2);
            }
            break;
            case R.id.tv_boy_boutique_more: {
                jumpToMore(3);
            }
            break;
            case R.id.tv_girl_boutique_more: {
                jumpToMore(4);
            }
            break;
            case R.id.tv_serialized_latest_more: {
                jumpToMore(5);
            }
            break;
            case R.id.tv_search: {
                Intent intent = new Intent(mContext, SearchBookActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.tv_rank_more:
                ARouter.getInstance()
                        .build("/ss/book/rank")
                        .withInt("type", 0)
                        .navigation();
                break;
            default:
                break;
        }
    }

    private void jumpToMore(int moduleId) {
        Intent intent = new Intent(mContext, ListenMoreActivity.class);
        intent.putExtra("moduleId", moduleId);
        startActivity(intent);
    }
}
