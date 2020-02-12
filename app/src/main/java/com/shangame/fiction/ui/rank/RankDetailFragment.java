package com.shangame.fiction.ui.rank;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiction.bar.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shangame.fiction.core.base.BaseFragment;
import com.shangame.fiction.core.base.WrapRecyclerViewAdapter;
import com.shangame.fiction.core.manager.ImageLoader;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.net.response.RankResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.ui.bookdetail.BookDetailActivity;
import com.shangame.fiction.ui.bookstore.BookStoreChannel;

/**
 * 榜单 Fragment
 * Create by Speedy on 2018/7/26
 */
public class RankDetailFragment extends BaseFragment implements RankContacts.View {

    private SmartRefreshLayout smartRefreshLayout;

    private RankAdapter mAdapter;

    private RankPresenter rankPresenter;

    private int dayType;//默认周榜
    private int maleChannel;

    private int mType;

    private RankResponse rankResponse;

    public static RankDetailFragment newInstance(int maleChannel, int dayType) {
        RankDetailFragment fragment = new RankDetailFragment();
        Bundle args = new Bundle();
        args.putInt("maleChannel", maleChannel);
        args.putInt("dayType", dayType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            maleChannel = getArguments().getInt("maleChannel", BookStoreChannel.GIRL);
            dayType = getArguments().getInt("dayType", RankDayType.RANK_WEEK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_rank_detail, container, false);
        rankPresenter = new RankPresenter();
        rankPresenter.attachView(this);
        initSmartRefreshLayout(contentView);
        initRecyclerView(contentView);
        smartRefreshLayout.autoRefresh();
        return contentView;
    }

    private void initSmartRefreshLayout(View contentView) {
        smartRefreshLayout = contentView.findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadRankList();
            }
        });
        smartRefreshLayout.autoRefresh();
    }

    private void initRecyclerView(View contentView) {
        RecyclerView recyclerView = contentView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new RankAdapter();
        recyclerView.setAdapter(mAdapter);
    }

    private void loadRankList() {
        int userId = UserInfoManager.getInstance(mContext).getUserid();
        rankPresenter.getRankList(userId, maleChannel, dayType);
    }

    public void switchSelectData(int type) {
        this.mType = type;
        if (rankResponse == null) {
            return;
        }
        mAdapter.clear();
        switch (type) {
            case 0: {
                mAdapter.addAll(rankResponse.clickdata);
                mAdapter.notifyDataSetChanged();
            }
            break;
            case 1: {
                mAdapter.addAll(rankResponse.collectdata);
                mAdapter.notifyDataSetChanged();
            }
            break;
            case 2: {
                mAdapter.addAll(rankResponse.giftdata);
                mAdapter.notifyDataSetChanged();
            }
            break;
            case 3: {
                mAdapter.addAll(rankResponse.subdata);
                mAdapter.notifyDataSetChanged();
            }
            break;
            default:
                break;
        }
    }

    @Override
    public void getRankListSuccess(RankResponse rankResponse) {
        smartRefreshLayout.finishRefresh();
        this.rankResponse = rankResponse;
        mAdapter.clear();
        switch (mType) {
            case 0: {
                mAdapter.addAll(rankResponse.clickdata);
                mAdapter.notifyDataSetChanged();
            }
            break;
            case 1: {
                mAdapter.addAll(rankResponse.collectdata);
                mAdapter.notifyDataSetChanged();
            }
            break;
            case 2: {
                mAdapter.addAll(rankResponse.giftdata);
                mAdapter.notifyDataSetChanged();
            }
            break;
            case 3: {
                mAdapter.addAll(rankResponse.subdata);
                mAdapter.notifyDataSetChanged();
            }
            break;
            default:
                break;
        }

    }

    @Override
    public void showNotNetworkView() {
        super.showNotNetworkView();
        smartRefreshLayout.finishRefresh();
    }

    @Override
    public void showError(Throwable throwable) {
        super.showError(throwable);
        smartRefreshLayout.finishRefresh();
    }

    class RankViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCover;
        TextView ivRankTop;
        TextView tvBookName;
        TextView tvContent;
        TextView tvBookAuthor;

        public RankViewHolder(View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.ivCover);
            ivRankTop = itemView.findViewById(R.id.ivRankTop);
            tvBookName = itemView.findViewById(R.id.tvBookName);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvBookAuthor = itemView.findViewById(R.id.tvBookAuthor);
        }
    }

    class RankAdapter extends WrapRecyclerViewAdapter<RankResponse.RankBookBean, RankViewHolder> {

        @NonNull
        @Override
        public RankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_item, parent, false);
            RankViewHolder rankViewHolder = new RankViewHolder(view);
            return rankViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RankViewHolder holder, int position) {
            final RankResponse.RankBookBean rankBookBean = getItem(position);
            switch (position) {
                case 0:
                    holder.ivRankTop.setText(getString(R.string.rank_top_1));
                    holder.ivRankTop.setTextColor(getResources().getColor(R.color.rank_top_1));
                    break;
                case 1:
                    holder.ivRankTop.setText(getString(R.string.rank_top_2));
                    holder.ivRankTop.setTextColor(getResources().getColor(R.color.rank_top_2));
                    break;
                case 2:
                    holder.ivRankTop.setText(getString(R.string.rank_top_3));
                    holder.ivRankTop.setTextColor(getResources().getColor(R.color.rank_top_3));
                    break;
                default:
                    holder.ivRankTop.setText(String.valueOf(position + 1) + ". ");
                    holder.ivRankTop.setTextColor(getResources().getColor(R.color.primary_text));
            }

            holder.tvBookName.setText(rankBookBean.bookname);
            holder.tvContent.setText(rankBookBean.synopsis);
            holder.tvBookAuthor.setText(getString(R.string.author_zhu, rankBookBean.author));

            ImageLoader.with(mActivity).loadCover(holder.ivCover, rankBookBean.bookcover);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BookDetailActivity.lunchActivity(mActivity, rankBookBean.bookid, ApiConstant.ClickType.FROM_CLICK);
                }
            });
        }
    }
}
