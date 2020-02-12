package com.shangame.fiction.ui.rank;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.fiction.bar.R;
import com.shangame.fiction.core.base.BaseFragment;
import com.shangame.fiction.ui.bookstore.BookStoreChannel;
import com.shangame.fiction.ui.listen.rank.ListenRankFragment;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * 榜单 Fragment
 * Create by Speedy on 2018/7/26
 */
public class RankFragment extends BaseFragment {

    private final int[] TAB_IMGS = new int[]{R.drawable.tab_girl_selector, R.drawable.tab_boy_selector, R.drawable.tab_listen_selector};
    private ViewPager mViewPager;
    private List<Fragment> mList;

    private int dayType;
    private int mType;
    private RankDetailFragment mBoyRankDetailFragment;
    private RankDetailFragment mGirlRankDetailFragment;
    private ListenRankFragment mListenRankDetailFragment;

    private int mPosition;
    private int mCurrentClick;

    public static RankFragment newInstance(int dayType, int type) {
        RankFragment fragment = new RankFragment();
        Bundle args = new Bundle();
        args.putInt("dayType", dayType);
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            dayType = getArguments().getInt("dayType", RankDayType.RANK_WEEK);
            mType = getArguments().getInt("type", 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_rank, container, false);

        mViewPager = contentView.findViewById(R.id.viewPager);

        initPager();
        initMagicIndicator(contentView);
        initRadioGroup(contentView);

        mViewPager.setCurrentItem(mType);
        return contentView;
    }

    private void initPager() {
        mBoyRankDetailFragment = RankDetailFragment.newInstance(BookStoreChannel.BOY, dayType);
        mGirlRankDetailFragment = RankDetailFragment.newInstance(BookStoreChannel.GIRL, dayType);
        mListenRankDetailFragment = ListenRankFragment.newInstance(dayType);

        mList = new ArrayList<>(2);
        mList.add(mGirlRankDetailFragment);
        mList.add(mBoyRankDetailFragment);
        mList.add(mListenRankDetailFragment);
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public int getCount() {
                return mList.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mList.get(position);
            }
        };
        mViewPager.setAdapter(adapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPosition = position;
                Log.e("hhh", "position= " + position);
                //mViewPager.setCurrentItem(position, false);
                switch (mPosition) {
                    case 0:
                        mGirlRankDetailFragment.switchSelectData(mCurrentClick);
                        break;
                    case 1:
                        mBoyRankDetailFragment.switchSelectData(mCurrentClick);
                        break;
                    case 2:
                        mListenRankDetailFragment.switchSelectData(mCurrentClick);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void initMagicIndicator(View contentView) {
        MagicIndicator magicIndicator = contentView.findViewById(R.id.magic_indicator);
        magicIndicator.setBackgroundColor(Color.WHITE);
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                CommonPagerTitleView commonPagerTitleView = new CommonPagerTitleView(context);

                // load custom layout
                View customLayout = LayoutInflater.from(context).inflate(R.layout.item_rank_menu, null);
                final ImageView titleImg = customLayout.findViewById(R.id.img_tab);
                titleImg.setImageResource(TAB_IMGS[index]);
                commonPagerTitleView.setContentView(customLayout);

                commonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {

                    @Override
                    public void onSelected(int index, int totalCount) {
                        titleImg.setSelected(true);
                    }

                    @Override
                    public void onDeselected(int index, int totalCount) {
                        titleImg.setSelected(false);
                    }

                    @Override
                    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
                    }

                    @Override
                    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
                    }
                });

                commonPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });

                return commonPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }

    private void initRadioGroup(View contentView) {
        RadioGroup radioGroup = contentView.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                switch (checkId) {
                    case R.id.tv_rank_click: {
                        mCurrentClick = 0;
                        switchClickItem(0);
                    }
                    break;
                    case R.id.tv_rank_collect: {
                        mCurrentClick = 1;
                        switchClickItem(1);
                    }
                    break;
                    case R.id.tv_rank_gift: {
                        mCurrentClick = 2;
                        switchClickItem(2);
                    }
                    break;
                    case R.id.tv_rank_hot_sell: {
                        mCurrentClick = 3;
                        switchClickItem(3);
                    }
                    break;
                    default:
                        break;
                }
            }
        });
    }

    private void switchClickItem(int type) {
        switch (mPosition) {
            case 0:
                mGirlRankDetailFragment.switchSelectData(type);
                break;
            case 1:
                mBoyRankDetailFragment.switchSelectData(type);
                break;
            case 2:
                mListenRankDetailFragment.switchSelectData(type);
                break;
            default:
                break;
        }
    }
}
