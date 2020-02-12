package com.shangame.fiction.ui.bookstore;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fiction.bar.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shangame.fiction.ad.ADConfig;
import com.shangame.fiction.ad.TTAdManagerHolder;
import com.shangame.fiction.adapter.BannerAdapter;
import com.shangame.fiction.adapter.BookGridAdapter;
import com.shangame.fiction.adapter.BookStoreRankAdapter;
import com.shangame.fiction.adapter.BoyTwoLinesAdapter;
import com.shangame.fiction.adapter.EditRecommendAdapter;
import com.shangame.fiction.adapter.NetworkImageHolderView;
import com.shangame.fiction.core.base.BaseLazyFragment;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.net.response.AdBean;
import com.shangame.fiction.net.response.FriendReadResponse;
import com.shangame.fiction.net.response.MaleChannelResponse;
import com.shangame.fiction.net.response.PictureConfigResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.BookInfoEntity;
import com.shangame.fiction.storage.model.NewBookInfoRankingEntity;
import com.shangame.fiction.ui.bookdetail.BookDetailActivity;
import com.shangame.fiction.ui.search.SearchBookActivity;
import com.shangame.fiction.ui.web.WebViewActivity;
import com.shangame.fiction.widget.recycler.coverflow.CoverFlowLayoutManger;
import com.shangame.fiction.widget.recycler.coverflow.RecyclerCoverFlow;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;
import java.util.List;

/**
 * 男生
 * Create by Speedy on 2018/7/25
 */
public class BoyPageFragment extends BaseLazyFragment implements View.OnClickListener, BookStoreContacts.View {

    private static final String TAG = "GirlPageFragment";
    private final static int LOAD_MORE_PAGE_SIZE = 7;
    private SmartRefreshLayout smartRefreshLayout;
    private View kindLayout;
    private RecyclerView editorRecommendRecyclerView;
    private RecyclerView finishRecyclerView;
    private RecyclerView ancientRecyclerView;
    private RecyclerView modernRecyclerView;
    private RecyclerView friendReadRecyclerView;
    private List<View> rankViewList;
    private RecyclerView hotRankRecyclerView;
    private RecyclerView clickRankReadRecyclerView;
    private RecyclerView collectReadRecyclerView;
    private BookStoreRankAdapter hotRankRankAdapter;
    private BookStoreRankAdapter clickRankRankAdapter;
    private BookStoreRankAdapter collectReadRankAdapter;
    private MagicIndicator magicIndicator;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private EditRecommendAdapter editRecommendAdapter;
    private BookWithTitleAdapter bookFinishAdapter;
    private BoyTwoLinesAdapter ancientAdapter;
    private BookGridAdapter modernAdapter;
    private FriendReadAdapter friendReadAdapter;
    private DividerItemDecoration dividerItemDecoration;
    private BookStorePresenter bookStorePresenter;
    private int updatePage = 1;
    private int loadMorePage = 1;
    private NestedScrollView nestedScrollView;

    private FrameLayout adContainer1;
    private FrameLayout adContainer2;

    // 头条穿山甲
    private List<TTNativeExpressAd> mTTAdList;

    private RelativeLayout mLayoutTop;
    private ConvenientBanner mConvenientBanner;

    private BannerAdapter mAdapter;
    private RecyclerCoverFlow mRecyclerBanner;
    private TextView mTextSynopsis;
    private TextView mTextBookName;
    private TextView mTextAuthor;

    private boolean hidden;

    private List<BookInfoEntity> mSearchData = new ArrayList<>();
    private List<BookInfoEntity> modernData = new ArrayList<>();

    private List<NewBookInfoRankingEntity> mSubData = new ArrayList<>();
    private List<NewBookInfoRankingEntity> mClickData = new ArrayList<>();
    private List<NewBookInfoRankingEntity> mCollectData = new ArrayList<>();

    private List<BookInfoEntity> mRecData = new ArrayList<>();
    private List<PictureConfigResponse.PicItem> mList = new ArrayList<>();

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
    public void onDestroyView() {
        super.onDestroyView();
        bookStorePresenter.detachView();
    }

    @Override
    protected void onLazyLoad() {
        initPresenter();
        // smartRefreshLayout.autoRefresh();
        loadData();
        loadMoreData();
        mLayoutTop.setBackgroundColor(Color.parseColor("#822E30"));
    }

    private void initPresenter() {
        bookStorePresenter = new BookStorePresenter();
        bookStorePresenter.attachView(this);
    }

    private void loadMoreData() {
        int userId = UserInfoManager.getInstance(mContext).getUserid();
        if (null != bookStorePresenter) {
            bookStorePresenter.getFriendRead(userId, BookStoreChannel.BOY, loadMorePage, LOAD_MORE_PAGE_SIZE, 0);
        }
    }

    private void initCsjAd(final FrameLayout frameLayout, String codeId) {
        frameLayout.removeAllViews();

        //step2:创建TTAdNative对象，createAdNative(Context context) banner广告context需要传入Activity对象
        TTAdNative adNative = TTAdManagerHolder.get().createAdNative(getContext());
        //step3:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary(getContext());

        mTTAdList = new ArrayList<>();
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
                Log.i("hhh", "load error 3 : " + code + ", " + message);
                frameLayout.removeAllViews();
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0) {
                    return;
                }
                TTNativeExpressAd mTTAd = ads.get(0);
                mTTAdList.add(mTTAd);
                // mTTAd.setSlideIntervalTime(30 * 1000);
                bindAdListener(mTTAd, frameLayout);
                mTTAd.render();
            }
        });
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
        if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            return;
        }
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_boy_page, container, false);
        int verify = AdBean.getInstance().getVerify();
        if (verify == 0) {
            initCsjAd(view);
        }
        initSmartRefreshLayout(view);
        initBanner(view);
        initMenu(view);
        initEditorRecommend(view);
        initHotSearch(view);
        initBookFinish(view);
        initRank(view);
        initAncient(view);
        initModern(view);
        initFriendRead(view);
        initPresenter();
        return view;
    }

    private void initCsjAd(View view) {
        adContainer1 = view.findViewById(R.id.adContainer1);
        adContainer2 = view.findViewById(R.id.adContainer2);

        initCsjAd(adContainer1, ADConfig.CSJAdPositionId.BOOK_STORE_BOY_ID1);
        initCsjAd(adContainer2, ADConfig.CSJAdPositionId.BOOK_STORE_BOY_ID2);
    }

    private void initSmartRefreshLayout(View view) {
        smartRefreshLayout = view.findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadData();
            }
        });

        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadMoreData();
            }
        });
        // smartRefreshLayout.setEnableLoadMore(false);
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
                Log.e("hhh", "boy onPageSelected position= " + position);
                if (null != mList) {
                    PictureConfigResponse.PicItem picItem = mList.get(position);
                    if (TextUtils.isEmpty(picItem.color) || picItem.color.trim().length() != 7) {
                        mLayoutTop.setBackgroundColor(Color.parseColor("#822E30"));
                        if (null != getParentFragment()) {
                            if (hidden) {
                                ((BookStoreFragment) getParentFragment()).setAppBarColor("#822E30");
                            }
                        }
                    } else {
                        mLayoutTop.setBackgroundColor(Color.parseColor(picItem.color));
                        if (null != getParentFragment()) {
                            if (hidden) {
                                ((BookStoreFragment) getParentFragment()).setAppBarColor(picItem.color);
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

    private void initMenu(View view) {
        view.findViewById(R.id.tvFinishMore).setOnClickListener(this);
        view.findViewById(R.id.tvAncientMore).setOnClickListener(this);
        view.findViewById(R.id.tvModernMore).setOnClickListener(this);
        view.findViewById(R.id.tv_search).setOnClickListener(this);
        view.findViewById(R.id.text_sort).setOnClickListener(this);
        view.findViewById(R.id.tv_rank_more).setOnClickListener(this);

        view.findViewById(R.id.tv_editor_more).setOnClickListener(this);

        kindLayout = view.findViewById(R.id.kindLayout);

        nestedScrollView = view.findViewById(R.id.nestedScrollView);
    }

    private void initEditorRecommend(View contentView) {
        editorRecommendRecyclerView = contentView.findViewById(R.id.editorRecommendRecyclerView);
        editorRecommendRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));

        editRecommendAdapter = new EditRecommendAdapter(R.layout.item_edit_recommend, mRecData);
        editorRecommendRecyclerView.setAdapter(editRecommendAdapter);

        editRecommendAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BookInfoEntity infoEntity = mRecData.get(position);
                BookDetailActivity.lunchActivity(mActivity, infoEntity.bookid, ApiConstant.ClickType.FROM_CLICK);
            }
        });
    }

    private void initHotSearch(View contentView) {
        mTextBookName = contentView.findViewById(R.id.text_book_name);
        mTextAuthor = contentView.findViewById(R.id.text_author);
        mTextSynopsis = contentView.findViewById(R.id.text_synopsis);
        mRecyclerBanner = contentView.findViewById(R.id.recycler_banner);

        contentView.findViewById(R.id.tv_recommend_more).setOnClickListener(this);
//        mRecyclerBanner.setFlatFlow(true); //平面滚动
//        mRecyclerBanner.setGreyItem(true); //设置灰度渐变
//        mRecyclerBanner.setAlphaItem(true); //设置半透渐变
        mAdapter = new BannerAdapter(R.layout.item_banner, mSearchData);
        mRecyclerBanner.setAdapter(mAdapter);

        mRecyclerBanner.setOnItemSelectedListener(new CoverFlowLayoutManger.OnSelected() {
            @Override
            public void onItemSelected(int position) {
                BookInfoEntity adItem = mSearchData.get(position);
                mTextBookName.setText(adItem.bookname);
                mTextAuthor.setText(adItem.author);
                mTextSynopsis.setText(adItem.synopsis);
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BookInfoEntity adItem = mSearchData.get(position);
                if (adItem == null) {
                    return;
                }
                BookDetailActivity.lunchActivity(mActivity, adItem.bookid, ApiConstant.ClickType.FROM_CLICK);
            }
        });
    }

    private void initBookFinish(View contentView) {
        TextView textLabelComplete = contentView.findViewById(R.id.text_label_complete);
        textLabelComplete.setText("经典完本");
        finishRecyclerView = contentView.findViewById(R.id.finishRecyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4);
        finishRecyclerView.setLayoutManager(gridLayoutManager);
        // finishRecyclerView.addItemDecoration(new SpaceItemDecoration(35));
        // finishRecyclerView.addItemDecoration(dividerItemDecoration);
        bookFinishAdapter = new BookWithTitleAdapter(mActivity);
        finishRecyclerView.setAdapter(bookFinishAdapter);
    }

    private void initRank(View contentView) {
        magicIndicator = contentView.findViewById(R.id.magic_indicator);
        viewPager = contentView.findViewById(R.id.viewPager);

        rankViewList = new ArrayList<>(3);

        hotRankRecyclerView = (RecyclerView) getLayoutInflater().inflate(R.layout.book_store_rank_list, null);
        clickRankReadRecyclerView = (RecyclerView) getLayoutInflater().inflate(R.layout.book_store_rank_list, null);
        collectReadRecyclerView = (RecyclerView) getLayoutInflater().inflate(R.layout.book_store_rank_list, null);

        clickRankRankAdapter = new BookStoreRankAdapter(R.layout.item_book_store_rank, mClickData);
        collectReadRankAdapter = new BookStoreRankAdapter(R.layout.item_book_store_rank, mCollectData);
        hotRankRankAdapter = new BookStoreRankAdapter(R.layout.item_book_store_rank, mSubData);

        clickRankReadRecyclerView.setAdapter(clickRankRankAdapter);
        collectReadRecyclerView.setAdapter(collectReadRankAdapter);
        hotRankRecyclerView.setAdapter(hotRankRankAdapter);

        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(mContext, 2);
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(mContext, 2);
        GridLayoutManager gridLayoutManager3 = new GridLayoutManager(mContext, 2);

        clickRankReadRecyclerView.setLayoutManager(gridLayoutManager2);
        collectReadRecyclerView.setLayoutManager(gridLayoutManager3);
        hotRankRecyclerView.setLayoutManager(gridLayoutManager1);

        rankViewList.add(clickRankReadRecyclerView);
        rankViewList.add(collectReadRecyclerView);
        rankViewList.add(hotRankRecyclerView);

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

    private void initAncient(View contentView) {
        TextView tvAncient = contentView.findViewById(R.id.tvAncient);
        tvAncient.setText("都市经典");
        TextView textSubtitle = contentView.findViewById(R.id.text_subtitle);
        textSubtitle.setText("看的都停不下来");
        ancientRecyclerView = contentView.findViewById(R.id.ancientRecyclerView);

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

        ancientRecyclerView.setLayoutManager(gridLayoutManager);

        // ancientRecyclerView.addItemDecoration(new RecommendSpaceItemDecoration(35));
        ancientAdapter = new BoyTwoLinesAdapter(mActivity);
        ancientRecyclerView.setAdapter(ancientAdapter);
    }

    private void initModern(View contentView) {
        TextView tvModern = contentView.findViewById(R.id.tvModern);
        tvModern.setText("玄幻力作");
        TextView textSubtitle = contentView.findViewById(R.id.text_subtitle_1);
        textSubtitle.setText("玄而幻之，脑洞新奇");

        modernRecyclerView = contentView.findViewById(R.id.modernRecyclerView);

        modernRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        // modernRecyclerView.addItemDecoration(new SpaceItemDecoration(35));

        modernAdapter = new BookGridAdapter(R.layout.item_complete_book, modernData);
        modernRecyclerView.setAdapter(modernAdapter);

        modernAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BookInfoEntity infoEntity = modernData.get(position);
                BookDetailActivity.lunchActivity(mActivity, infoEntity.bookid, ApiConstant.ClickType.FROM_CLICK);
            }
        });
    }

    private void initFriendRead(View view) {
        friendReadRecyclerView = view.findViewById(R.id.friendReadRecyclerView);

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

        friendReadRecyclerView.setLayoutManager(gridLayoutManager);
        // friendReadRecyclerView.addItemDecoration(new FriendReadItemDecoration(35));
        friendReadAdapter = new FriendReadAdapter(mActivity);
        friendReadRecyclerView.setAdapter(friendReadAdapter);
    }

    private void loadData() {
        int userId = UserInfoManager.getInstance(mContext).getUserid();
        if (null != bookStorePresenter) {
            bookStorePresenter.getBookData(userId, updatePage, BookStoreChannel.BOY);
            bookStorePresenter.getPictureConfig(userId, BookStoreChannel.BOY);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.public_line_horizontal));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvFinishMore:
                BookListByTypeActivity.lunchActivity(mActivity, BookStoreType.FinishMore, "经典完本", BookStoreChannel.BOY);
                break;
            case R.id.tvAncientMore:
                BookListByTypeActivity.lunchActivity(mActivity, BookStoreType.AncientMore, "都市经典", BookStoreChannel.BOY);
                break;
            case R.id.tvModernMore:
                BookListByTypeActivity.lunchActivity(mActivity, BookStoreType.ModernMore, "玄幻力作", BookStoreChannel.BOY);
                break;
            case R.id.tv_search: {
                Intent intent = new Intent(mContext, SearchBookActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.text_sort: {
                ARouter.getInstance()
                        .build("/ss/book/library")
                        .withInt("type", 1)
                        .navigation();
            }
            break;
            case R.id.tv_recommend_more: {
                BookListByTypeActivity.lunchActivity(mActivity, BookStoreType.HotSearachMore, "今日推荐", BookStoreChannel.BOY);
            }
            break;
            case R.id.tv_rank_more:
                ARouter.getInstance()
                        .build("/ss/book/rank")
                        .withInt("type", 1)
                        .navigation();
                break;
            case R.id.tv_editor_more:
                BookListByTypeActivity.lunchActivity(mActivity, BookStoreType.EditorRecommendMore, "主编力荐", BookStoreChannel.BOY);
                break;
            default:
                break;
        }
    }

    public void initData() {
        loadData();
        // loadMoreData();
        mLayoutTop.setBackgroundColor(Color.parseColor("#822E30"));
    }

    @Override
    public void getFriendReadSuccess(FriendReadResponse friendReadResponse) {
        smartRefreshLayout.finishLoadMore();
        if (loadMorePage == 1) {
            friendReadAdapter.clear();
        }
        loadMorePage++;
        friendReadAdapter.addAll(friendReadResponse.pagedata);
        friendReadAdapter.notifyDataSetChanged();
    }

    @Override
    public void getBookDataSuccess(MaleChannelResponse maleChannelResponse) {
        smartRefreshLayout.finishRefresh();
        updatePage++;
        bookFinishAdapter.clear();
        ancientAdapter.clear();

        mRecData.clear();
        mRecData.addAll(maleChannelResponse.recdata);
        editRecommendAdapter.setNewData(mRecData);

        mSearchData.clear();
        mSearchData.addAll(maleChannelResponse.searchdata);
        mAdapter.setNewData(mSearchData);

        if (mSearchData.size() > 2) {
            mRecyclerBanner.smoothScrollToPosition(2);
            mTextBookName.setText(mSearchData.get(2).bookname);
            mTextAuthor.setText(mSearchData.get(2).author);
            mTextSynopsis.setText(mSearchData.get(2).synopsis);
        } else if (mSearchData.size() > 0) {
            mRecyclerBanner.smoothScrollToPosition(0);
            mTextBookName.setText(mSearchData.get(0).bookname);
            mTextAuthor.setText(mSearchData.get(0).author);
            mTextSynopsis.setText(mSearchData.get(0).synopsis);
        }

        bookFinishAdapter.addAll(maleChannelResponse.completedata);
        ancientAdapter.addAll(maleChannelResponse.class1data);

        modernData.clear();
        modernData.addAll(maleChannelResponse.class2data);
        modernAdapter.setNewData(modernData);

        editRecommendAdapter.notifyDataSetChanged();
        bookFinishAdapter.notifyDataSetChanged();
        ancientAdapter.notifyDataSetChanged();
        modernAdapter.notifyDataSetChanged();

        mSubData.clear();
        mClickData.clear();
        mCollectData.clear();
        int halfLength = maleChannelResponse.subdata.size() / 2;
        NewBookInfoRankingEntity newBookInforankingEntity;
        for (int i = 0; i < halfLength; i++) {
            newBookInforankingEntity = maleChannelResponse.subdata.get(i);
            newBookInforankingEntity.ranking = i + 1;
            mSubData.add(newBookInforankingEntity);

            newBookInforankingEntity = maleChannelResponse.subdata.get(i + halfLength);
            newBookInforankingEntity.ranking = i + halfLength + 1;
            mSubData.add(newBookInforankingEntity);
        }

        int halfLength2 = maleChannelResponse.clickdata.size() / 2;
        NewBookInfoRankingEntity newBookInfoRankingEntity2;
        for (int i = 0; i < halfLength2; i++) {
            newBookInfoRankingEntity2 = maleChannelResponse.clickdata.get(i);
            newBookInfoRankingEntity2.ranking = i + 1;
            mClickData.add(newBookInfoRankingEntity2);

            newBookInfoRankingEntity2 = maleChannelResponse.clickdata.get(i + halfLength2);
            newBookInfoRankingEntity2.ranking = i + halfLength2 + 1;
            mClickData.add(newBookInfoRankingEntity2);
        }

        int halfLength3 = maleChannelResponse.collectdata.size() / 2;
        NewBookInfoRankingEntity newBookInfoRankingEntity3;
        for (int i = 0; i < halfLength2; i++) {
            newBookInfoRankingEntity3 = maleChannelResponse.collectdata.get(i);
            newBookInfoRankingEntity3.ranking = i + 1;
            mCollectData.add(newBookInfoRankingEntity3);

            newBookInfoRankingEntity3 = maleChannelResponse.collectdata.get(i + halfLength3);
            newBookInfoRankingEntity3.ranking = i + halfLength3 + 1;
            mCollectData.add(newBookInfoRankingEntity3);
        }

        hotRankRankAdapter.setNewData(mSubData);
        clickRankRankAdapter.setNewData(mClickData);
        collectReadRankAdapter.setNewData(mCollectData);

        pagerAdapter.notifyDataSetChanged();
        kindLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void getPictureConfigSuccess(final PictureConfigResponse pictureConfigResponse) {
        mList.addAll(pictureConfigResponse.picdata);
        mConvenientBanner.setPages(new CBViewHolderCreator() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, pictureConfigResponse.picdata)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        PictureConfigResponse.PicItem item = pictureConfigResponse.picdata.get(position);
                        if (item.bookid == 0) {
                            if (!TextUtils.isEmpty(item.linkurl)) {
                                WebViewActivity.lunchActivity(mActivity, "", item.linkurl);
                            }
                        } else {
                            BookDetailActivity.lunchActivity(mActivity, item.bookid, ApiConstant.ClickType.FROM_CLICK);
                        }
                    }
                })
                .setCanLoop(pictureConfigResponse.picdata.size() > 1);

        if (pictureConfigResponse.picdata.size() > 1) {
            mConvenientBanner.startTurning(5000);
        }
    }

    @Override
    public void dismissLoading() {
        super.dismissLoading();
        smartRefreshLayout.finishRefresh();
    }

    @Override
    public void showError(Throwable throwable) {
        super.showError(throwable);
        smartRefreshLayout.finishRefresh();
    }

    public void scrollToTop() {
        nestedScrollView.scrollTo(0, 0);
    }
}
