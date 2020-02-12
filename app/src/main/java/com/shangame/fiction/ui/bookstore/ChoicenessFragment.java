package com.shangame.fiction.ui.bookstore;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.fiction.bar.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shangame.fiction.ad.ADConfig;
import com.shangame.fiction.ad.TTAdManagerHolder;
import com.shangame.fiction.adapter.BannerAdapter;
import com.shangame.fiction.adapter.BookStoreRankAdapter;
import com.shangame.fiction.adapter.CompleteBookAdapter;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.base.BaseLazyFragment;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.net.response.AdBean;
import com.shangame.fiction.net.response.ChoicenessResponse;
import com.shangame.fiction.net.response.OthersLookResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.BookInfoEntity;
import com.shangame.fiction.storage.model.NewBookInfoRankingEntity;
import com.shangame.fiction.storage.model.UserInfo;
import com.shangame.fiction.ui.bookdetail.BookDetailActivity;
import com.shangame.fiction.ui.login.LoginActivity;
import com.shangame.fiction.ui.search.SearchBookActivity;
import com.shangame.fiction.ui.task.TaskCenterActivity;
import com.shangame.fiction.widget.SmartViewSwitcher;
import com.shangame.fiction.widget.recycler.coverflow.CoverFlowLayoutManger;
import com.shangame.fiction.widget.recycler.coverflow.RecyclerCoverFlow;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;
import java.util.List;

/**
 * 精选
 */
public class ChoicenessFragment extends BaseLazyFragment implements View.OnClickListener, ChoicenessContacts.View {

    private static final String TAG = "ChoicenessFragment";

    private static final int OPEN_TASK_CENTER = 66;
    private final static int LOAD_MORE_PAGE_SIZE = 7;
    private SmartRefreshLayout smartRefreshLayout;
    private View kindLayout;
    private SmartViewSwitcher smartViewSwitcher;
    private List<ChoicenessResponse.CardataBean> carData = new ArrayList<>();
    private List<BookInfoEntity> mPicData = new ArrayList<>();
    private List<BookInfoEntity> mCompleteData = new ArrayList<>();

    private RecyclerView highlyRecommendRecyclerView;
    private RecyclerView hotSerialRecyclerView;
    private RecyclerView finishRecyclerView;
    private RecyclerView otherLookRecyclerView;
    private HighlyRecommendAdapter highlyRecommendAdapter;
    private HotSerialAdapter hotSerialAdapter;
    private CompleteBookAdapter bookFinishAdapter;
    private OtherLookAdapter otherLookAdapter;
    private ChoicenessPresenter choicenessPresenter;
    private int updatePage;
    private int loadMorePage = 1;
    private NestedScrollView nestedScrollView;
    private NestedScrollView.OnScrollChangeListener onScrollChangeListener;

    private FrameLayout adContainer1;
    private FrameLayout adContainer2;

    // 头条穿山甲
    private TTNativeExpressAd mAd;

    private List<View> rankViewList;
    private BookStoreRankAdapter hotRankRankAdapter;
    private BookStoreRankAdapter clickRankRankAdapter;
    private BookStoreRankAdapter collectReadRankAdapter;

    private PagerAdapter pagerAdapter;

    private BannerAdapter mAdapter;

    private LinearLayout mLayoutTop;
    private TextView mTextSynopsis;

    private RecyclerCoverFlow mRecyclerBanner;

    private boolean hidden;

    private List<NewBookInfoRankingEntity> mSubData = new ArrayList<>();
    private List<NewBookInfoRankingEntity> mClickData = new ArrayList<>();
    private List<NewBookInfoRankingEntity> mCollectData = new ArrayList<>();

    private SmartViewSwitcher.ViewSwitcherAdapter viewSwitcherAdapter = new SmartViewSwitcher.ViewSwitcherAdapter() {

        @Override
        public void onBindView(int position, View view) {
            ChoicenessResponse.CardataBean bean = carData.get(position);

            TextView tvClassName = view.findViewById(R.id.tvClassName);
            TextView tvContents = view.findViewById(R.id.tvContents);
            tvClassName.setText(bean.classname);
            tvContents.setText(bean.contents);
        }

        @Override
        public Object getItemData(int position) {
            return carData.get(position);
        }

        @Override
        public int getItemCount() {
            return carData.size();
        }
    };

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        hidden = getUserVisibleHint();
        Log.e("hhh", "hidden= " + hidden);
    }

    @Override
    protected void onLazyLoad() {
        initPresenter();
        // smartRefreshLayout.autoRefresh();
        loadData();
        loadMoreData();
    }

    private void initPresenter() {
        choicenessPresenter = new ChoicenessPresenter();
        choicenessPresenter.attachView(this);
    }

    private void loadMoreData() {
        int userId = UserInfoManager.getInstance(mContext).getUserid();
        if (null != choicenessPresenter) {
            choicenessPresenter.othersLookData(userId, BookStoreChannel.CHOICENESS, loadMorePage, LOAD_MORE_PAGE_SIZE, 0);
        }
    }

    private void bindAdListener(TTNativeExpressAd ad, final FrameLayout frameLayout) {
        ad.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
            @Override
            public void onAdClicked(View view, int type) {
                Log.i("hhh", "广告被点击");
            }

            @Override
            public void onAdShow(View view, int type) {
                Log.i("hhh", "广告展示");
            }

            @Override
            public void onRenderFail(View view, String msg, int code) {
                Log.i("hhh", msg + " code:" + code);
            }

            @Override
            public void onRenderSuccess(View view, float width, float height) {
                //返回view的宽高 单位 dp
                Log.i("hhh", "渲染成功");
                frameLayout.removeAllViews();
                frameLayout.addView(view);
            }
        });
        //dislike设置
        bindDislike(ad, frameLayout);
    }

    /**
     * 设置广告的不喜欢, 注意：强烈建议设置该逻辑，如果不设置dislike处理逻辑，则模板广告中的 dislike区域不响应dislike事件。
     *
     * @param ad
     */
    private void bindDislike(TTNativeExpressAd ad, final FrameLayout frameLayout) {
        //使用默认模板中默认dislike弹出样式
        ad.setDislikeCallback(getActivity(), new TTAdDislike.DislikeInteractionCallback() {
            @Override
            public void onSelected(int position, String value) {
                Log.i("hhh", "点击 " + value);
                //用户选择不喜欢原因后，移除广告展示
                frameLayout.removeAllViews();
            }

            @Override
            public void onCancel() {
                Log.i("hhh", "点击取消");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == OPEN_TASK_CENTER) {
            Intent intent = new Intent(mContext, TaskCenterActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_choiceness, container, false);
        int verify = AdBean.getInstance().getVerify();
        if (verify == 0) {
            initCsjAd(contentView);
        }
        initSmartRefreshLayout(contentView);
        initBanner(contentView);
        initMenu(contentView);
        initCarousel(contentView);
        initHighlyRecommend(contentView);
        initHotSerial(contentView);
        initBookFinish(contentView);
        initRank(contentView);
        initOtherLook(contentView);
        return contentView;
    }

    private void initCsjAd(View view) {
        adContainer1 = view.findViewById(R.id.adContainer1);
        adContainer2 = view.findViewById(R.id.adContainer2);

        initCsjAd(adContainer1, ADConfig.CSJAdPositionId.BOOK_STORE_CHOICENESS_ID1);
        initCsjAd(adContainer2, ADConfig.CSJAdPositionId.BOOK_STORE_CHOICENESS_ID2);
    }

    private void initSmartRefreshLayout(View view) {
        smartRefreshLayout = view.findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadData();
            }
        });

        smartRefreshLayout.setEnableLoadMore(false);
    }

    private void initBanner(final View contentView) {
        mLayoutTop = contentView.findViewById(R.id.layout_top);
        mTextSynopsis = contentView.findViewById(R.id.text_synopsis);

        mRecyclerBanner = contentView.findViewById(R.id.recycler_banner);
//        mRecyclerBanner.setFlatFlow(true); //平面滚动
//        mRecyclerBanner.setGreyItem(true); //设置灰度渐变
//        mRecyclerBanner.setAlphaItem(true); //设置半透渐变
        mAdapter = new BannerAdapter(R.layout.item_banner, mPicData);
        mRecyclerBanner.setAdapter(mAdapter);

        mRecyclerBanner.setOnItemSelectedListener(new CoverFlowLayoutManger.OnSelected() {
            @Override
            public void onItemSelected(int position) {
                BookInfoEntity adItem = mPicData.get(position);
                mTextSynopsis.setText(adItem.synopsis);

                Log.e("hhh", "color= " + adItem.color);
                if (TextUtils.isEmpty(adItem.color.trim()) || adItem.color.trim().length() != 7) {
                    mLayoutTop.setBackgroundColor(Color.parseColor("#EC9A54"));
                    if (null != getParentFragment()) {
                        ((BookStoreFragment) getParentFragment()).setAppBarColor("#EC9A54");
                    }
                } else {
                    mLayoutTop.setBackgroundColor(Color.parseColor(adItem.color.trim()));
                    if (null != getParentFragment()) {
                        ((BookStoreFragment) getParentFragment()).setAppBarColor(adItem.color.trim());
                    }
                }
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BookInfoEntity adItem = mPicData.get(position);
                if (adItem == null) {
                    return;
                }
                BookDetailActivity.lunchActivity(mActivity, adItem.bookid, ApiConstant.ClickType.FROM_CLICK);
            }
        });

        contentView.findViewById(R.id.image_go).setOnClickListener(this);
        contentView.findViewById(R.id.image_red_package).setOnClickListener(this);
    }

    private void initMenu(View contentView) {
        contentView.findViewById(R.id.menu_classify).setOnClickListener(this);
        contentView.findViewById(R.id.menu_red).setOnClickListener(this);
        contentView.findViewById(R.id.menu_new_book).setOnClickListener(this);
        contentView.findViewById(R.id.menu_booklist).setOnClickListener(this);
        contentView.findViewById(R.id.menu_end).setOnClickListener(this);

        contentView.findViewById(R.id.tvHighlyRecommendMore).setOnClickListener(this);
        contentView.findViewById(R.id.tvHotSerialMore).setOnClickListener(this);
        contentView.findViewById(R.id.tvFinishMore).setOnClickListener(this);
        contentView.findViewById(R.id.tv_search).setOnClickListener(this);
        contentView.findViewById(R.id.text_sort).setOnClickListener(this);
        contentView.findViewById(R.id.img_rank).setOnClickListener(this);
        contentView.findViewById(R.id.tv_rank_more).setOnClickListener(this);
        contentView.findViewById(R.id.img_end).setOnClickListener(this);

        kindLayout = contentView.findViewById(R.id.kindLayout);

        nestedScrollView = contentView.findViewById(R.id.nestedScrollView);
        nestedScrollView.setOnScrollChangeListener(onScrollChangeListener);
    }

    private void initCarousel(View contentView) {
        smartViewSwitcher = contentView.findViewById(R.id.smartViewSwitcher);
        smartViewSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                View view = getLayoutInflater().inflate(R.layout.choice_carouse_item, null);
                return view;
            }
        });

        smartViewSwitcher.setAdapter(viewSwitcherAdapter);
        smartViewSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChoicenessResponse.CardataBean carDataBean = (ChoicenessResponse.CardataBean) smartViewSwitcher.getCurrentData();
                BookDetailActivity.lunchActivity(mActivity, carDataBean.bookid, ApiConstant.ClickType.FROM_CLICK);
            }
        });
    }

    private void initHighlyRecommend(View view) {
        highlyRecommendRecyclerView = view.findViewById(R.id.highlyRecommendListView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return 4;
                } else {
                    return 1;
                }
            }
        });

        highlyRecommendRecyclerView.setLayoutManager(gridLayoutManager);
        // highlyRecommendRecyclerView.addItemDecoration(new RecommendSpaceItemDecoration(35));
        highlyRecommendAdapter = new HighlyRecommendAdapter(mActivity);
        highlyRecommendRecyclerView.setAdapter(highlyRecommendAdapter);
    }

    private void initHotSerial(View contentView) {
        hotSerialRecyclerView = contentView.findViewById(R.id.hotSerialRecyclerView);

        hotSerialRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));

        hotSerialAdapter = new HotSerialAdapter(mActivity);
        hotSerialRecyclerView.setAdapter(hotSerialAdapter);
    }

    private void initBookFinish(View contentView) {
        finishRecyclerView = contentView.findViewById(R.id.finishRecyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        finishRecyclerView.setLayoutManager(gridLayoutManager);

        // finishRecyclerView.addItemDecoration(new SpaceItemDecoration(35));

        bookFinishAdapter = new CompleteBookAdapter(R.layout.item_complete_book, mCompleteData);
        finishRecyclerView.setAdapter(bookFinishAdapter);

        bookFinishAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BookInfoEntity infoEntity = mCompleteData.get(position);
                BookDetailActivity.lunchActivity(mActivity, infoEntity.bookid, ApiConstant.ClickType.FROM_CLICK);
            }
        });
    }

    private void initRank(View contentView) {
        MagicIndicator magicIndicator = contentView.findViewById(R.id.magic_indicator);
        ViewPager viewPager = contentView.findViewById(R.id.viewPager);

        rankViewList = new ArrayList<>(3);

        RecyclerView clickRankReadRecyclerView = (RecyclerView) getLayoutInflater().inflate(R.layout.book_store_rank_list, null);
        RecyclerView collectReadRecyclerView = (RecyclerView) getLayoutInflater().inflate(R.layout.book_store_rank_list, null);
        RecyclerView hotRankRecyclerView = (RecyclerView) getLayoutInflater().inflate(R.layout.book_store_rank_list, null);

        clickRankRankAdapter = new BookStoreRankAdapter(R.layout.item_book_store_rank, mClickData);
        collectReadRankAdapter = new BookStoreRankAdapter(R.layout.item_book_store_rank, mCollectData);
        hotRankRankAdapter = new BookStoreRankAdapter(R.layout.item_book_store_rank, mSubData);

        clickRankReadRecyclerView.setAdapter(clickRankRankAdapter);
        collectReadRecyclerView.setAdapter(collectReadRankAdapter);
        hotRankRecyclerView.setAdapter(hotRankRankAdapter);

        clickRankRankAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                NewBookInfoRankingEntity infoRankingEntity = mClickData.get(position);
                BookDetailActivity.lunchActivity(mActivity, infoRankingEntity.bookid, ApiConstant.ClickType.FROM_CLICK);
            }
        });

        collectReadRankAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                NewBookInfoRankingEntity infoRankingEntity = mCollectData.get(position);
                BookDetailActivity.lunchActivity(mActivity, infoRankingEntity.bookid, ApiConstant.ClickType.FROM_CLICK);
            }
        });

        hotRankRankAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                NewBookInfoRankingEntity infoRankingEntity = mSubData.get(position);
                BookDetailActivity.lunchActivity(mActivity, infoRankingEntity.bookid, ApiConstant.ClickType.FROM_CLICK);
            }
        });

        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(mContext, 2);
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(mContext, 2);
        GridLayoutManager gridLayoutManager3 = new GridLayoutManager(mContext, 2);

        clickRankReadRecyclerView.setLayoutManager(gridLayoutManager1);
        collectReadRecyclerView.setLayoutManager(gridLayoutManager2);
        hotRankRecyclerView.setLayoutManager(gridLayoutManager3);

        rankViewList.add(clickRankReadRecyclerView);
        rankViewList.add(collectReadRecyclerView);
        rankViewList.add(hotRankRecyclerView);

        final List<String> titleList = new ArrayList<>(2);
        titleList.add("点击榜");
        titleList.add("收藏榜");
        titleList.add("礼物榜");
        MagicIndicatorAdapter magicIndicatorAdapter = new MagicIndicatorAdapter(mContext, viewPager);
        magicIndicatorAdapter.setTitleList(titleList);

        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setAdapter(magicIndicatorAdapter);

        magicIndicator.setNavigator(commonNavigator);

        pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return 3;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                View view = rankViewList.get(position);
                ViewPager.LayoutParams layoutParams = new ViewPager.LayoutParams();
                layoutParams.width = ViewPager.LayoutParams.MATCH_PARENT;
                layoutParams.height = ViewPager.LayoutParams.WRAP_CONTENT;
                view.setLayoutParams(layoutParams);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) { //
                container.removeView((View) object);
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

        };
        viewPager.setAdapter(pagerAdapter);

        ViewPagerHelper.bind(magicIndicator, viewPager);
    }

    private void initOtherLook(View contentView) {
        otherLookRecyclerView = contentView.findViewById(R.id.otherLookRecyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position % 7 < 3) {
                    return 4;
                } else {
                    return 1;
                }
            }
        });

        otherLookRecyclerView.setLayoutManager(gridLayoutManager);

        // otherLookRecyclerView.addItemDecoration(new OtherLookItemDecoration(18));
        otherLookAdapter = new OtherLookAdapter(mActivity);
        otherLookRecyclerView.setAdapter(otherLookAdapter);

        contentView.findViewById(R.id.layout_refresh).setOnClickListener(this);
    }

    private void initCsjAd(final FrameLayout frameLayout, String codeId) {
        frameLayout.removeAllViews();

        //step2:创建TTAdNative对象，createAdNative(Context context) banner广告context需要传入Activity对象
        TTAdNative adNative = TTAdManagerHolder.get().createAdNative(getContext());
        //step3:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary(getContext());

        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(350, 0) //期望模板广告view的size,单位dp
                .setImageAcceptedSize(640, 320)
                .build();
        //step5:请求广告，对请求回调的广告作渲染处理
        adNative.loadBannerExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
                Log.i("hhh", "load error 1 : " + code + ", " + message);
                frameLayout.removeAllViews();
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0) {
                    return;
                }
                mAd = ads.get(0);
                // mTTAd.setSlideIntervalTime(30 * 1000);
                bindAdListener(mAd, frameLayout);
                mAd.render();
            }
        });
    }

    private void loadData() {
        int userId = UserInfoManager.getInstance(mContext).getUserid();
        if (null != choicenessPresenter) {
            choicenessPresenter.getChoicenessData(userId, updatePage++);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        smartViewSwitcher.stopLooping();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_end: {
                Intent intent = new Intent(mActivity, BookEndActivity.class);
                intent.putExtra("bookStoreChannel", BookStoreChannel.CHOICENESS);
                startActivity(intent);
            }
            break;
            case R.id.tvHighlyRecommendMore:
                BookListByTypeActivity.lunchActivity(mActivity, BookStoreType.HighlyRecommend, "独家爆款", BookStoreChannel.CHOICENESS);
                break;
            case R.id.tvHotSerialMore:
                BookListByTypeActivity.lunchActivity(mActivity, BookStoreType.HotSerialMore, "精品新书", BookStoreChannel.CHOICENESS);
                break;
            case R.id.tvFinishMore:
                BookListByTypeActivity.lunchActivity(mActivity, BookStoreType.FinishMore, "完本佳作", BookStoreChannel.CHOICENESS);
                break;
            case R.id.image_go: {
                long userId = UserInfoManager.getInstance(mContext).getUserid();
                if (userId == 0) {
                    Intent mIntent = new Intent(mActivity, LoginActivity.class);
                    startActivityForResult(mIntent, BaseActivity.LUNCH_LOGIN_ACTIVITY_REQUEST_CODE);
                } else {
                    UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
                    int agentGrade = userInfo.agentGrade;
                    if (agentGrade > 0) {
                        ARouter.getInstance()
                                .build("/ss/sales/partner/manage/home")
                                .navigation();
                    } else {
                        ARouter.getInstance()
                                .build("/ss/sales/partner/upgrade/silver")
                                .navigation();
                    }
                }
            }
            break;
            case R.id.image_red_package: {
                Intent intent = new Intent(mContext, TaskCenterActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.layout_refresh:
                loadMoreData();
                break;
            case R.id.tv_search: {
                Intent intent = new Intent(mContext, SearchBookActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.img_rank:
            case R.id.tv_rank_more:
                ARouter.getInstance()
                        .build("/ss/book/rank")
                        .withInt("type", 0)
                        .navigation();
                break;
            case R.id.img_end: {
                Intent intent = new Intent(mContext, BookEndActivity.class);
                intent.putExtra("bookStoreChannel", BookStoreChannel.CHOICENESS);
                startActivity(intent);
            }
            break;
            case R.id.text_sort:
                ARouter.getInstance()
                        .build("/ss/book/library")
                        .withInt("type", 0)
                        .navigation();
                break;
            default:
                break;
        }
    }

    @Override
    public void getOthersLookDataSuccess(OthersLookResponse othersLookResponse) {
        smartRefreshLayout.finishLoadMore();
        loadMorePage++;
        otherLookAdapter.clear();
        otherLookAdapter.addAll(othersLookResponse.pagedata);
        otherLookAdapter.notifyDataSetChanged();
    }

    @Override
    public void getChoicenessDataSuccess(ChoicenessResponse choicenessResponse) {
        smartRefreshLayout.finishRefresh();

        highlyRecommendAdapter.clear();
        hotSerialAdapter.clear();
        // bookFinishAdapter.clear();
        mCompleteData.clear();

        highlyRecommendAdapter.addAll(choicenessResponse.heavydata);
        highlyRecommendAdapter.notifyDataSetChanged();

        if (null != choicenessResponse.choicedata) {
            mPicData.clear();
            mPicData.addAll(choicenessResponse.choicedata);
            mAdapter.setNewData(mPicData);

            if (mPicData.size() > 0) {
                if (TextUtils.isEmpty(mPicData.get(0).color.trim()) || mPicData.get(0).color.trim().length() != 7) {
                    mLayoutTop.setBackgroundColor(Color.parseColor("#EC9A54"));
                    if (null != getParentFragment()) {
                        if (hidden) {
                            ((BookStoreFragment) getParentFragment()).setAppBarColor("#EC9A54");
                        }
                    }
                } else {
                    mLayoutTop.setBackgroundColor(Color.parseColor(mPicData.get(0).color));
                    if (null != getParentFragment()) {
                        if (hidden) {
                            ((BookStoreFragment) getParentFragment()).setAppBarColor(mPicData.get(0).color);
                        }
                    }
                }

                if (mPicData.size() > 2) {
                    mRecyclerBanner.smoothScrollToPosition(2);
                    mTextSynopsis.setText(mPicData.get(2).synopsis);
                }
            }
        }

        hotSerialAdapter.addAll(choicenessResponse.hotdata);
        hotSerialAdapter.notifyDataSetChanged();

        mCompleteData.addAll(choicenessResponse.completedata);
        bookFinishAdapter.setNewData(mCompleteData);

        carData.addAll(choicenessResponse.cardata);
        smartViewSwitcher.notifyDataChange();
        smartViewSwitcher.setVisibility(View.VISIBLE);
        smartViewSwitcher.startLooping();

        mSubData.clear();
        mClickData.clear();
        mCollectData.clear();
        int halfLength = choicenessResponse.subdata.size() / 2;
        NewBookInfoRankingEntity newBookInforankingEntity;
        for (int i = 0; i < halfLength; i++) {
            newBookInforankingEntity = choicenessResponse.subdata.get(i);
            newBookInforankingEntity.ranking = i + 1;
            mSubData.add(newBookInforankingEntity);

            newBookInforankingEntity = choicenessResponse.subdata.get(i + halfLength);
            newBookInforankingEntity.ranking = i + halfLength + 1;
            mSubData.add(newBookInforankingEntity);
        }

        int halfLength2 = choicenessResponse.clickdata.size() / 2;
        NewBookInfoRankingEntity newBookInfoRankingEntity2;
        for (int i = 0; i < halfLength2; i++) {
            newBookInfoRankingEntity2 = choicenessResponse.clickdata.get(i);
            newBookInfoRankingEntity2.ranking = i + 1;
            mClickData.add(newBookInfoRankingEntity2);

            newBookInfoRankingEntity2 = choicenessResponse.clickdata.get(i + halfLength2);
            newBookInfoRankingEntity2.ranking = i + halfLength2 + 1;
            mClickData.add(newBookInfoRankingEntity2);
        }

        int halfLength3 = choicenessResponse.collectdata.size() / 2;
        NewBookInfoRankingEntity newBookInfoRankingEntity3;
        for (int i = 0; i < halfLength3; i++) {
            newBookInfoRankingEntity3 = choicenessResponse.collectdata.get(i);
            newBookInfoRankingEntity3.ranking = i + 1;
            mCollectData.add(newBookInfoRankingEntity3);

            newBookInfoRankingEntity3 = choicenessResponse.collectdata.get(i + halfLength3);
            newBookInfoRankingEntity3.ranking = i + halfLength3 + 1;
            mCollectData.add(newBookInfoRankingEntity3);
        }

        hotRankRankAdapter.setNewData(mSubData);
        clickRankRankAdapter.setNewData(mClickData);
        collectReadRankAdapter.setNewData(mCollectData);

        pagerAdapter.notifyDataSetChanged();

        kindLayout.setVisibility(View.VISIBLE);
    }

    public void initData() {
        loadData();
        loadMoreData();
        mLayoutTop.setBackgroundColor(Color.parseColor("#EC9A54"));
    }

    public void scrollToTop() {
        if (null != nestedScrollView) {
            nestedScrollView.scrollTo(0, 0);
        }
    }

    public void setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener onScrollChangeListener) {
        this.onScrollChangeListener = onScrollChangeListener;
    }
}
