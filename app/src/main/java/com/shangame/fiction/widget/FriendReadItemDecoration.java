package com.shangame.fiction.widget;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Create by Speedy on 2019/3/11
 */
public class FriendReadItemDecoration extends RecyclerView.ItemDecoration {

    int mSpace;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager){
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;

            int spanCount = gridLayoutManager.getSpanCount();
            int childPosition = parent.getChildAdapterPosition(view);
            if(childPosition % 6 <= 3){

                childPosition = childPosition %6;
                int column = childPosition % spanCount; // item column


                outRect.left = column * mSpace / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = mSpace - (column + 1) * mSpace / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (childPosition >= spanCount) {
                    outRect.top = mSpace; // item top
                }
            }

        }
    }

    public FriendReadItemDecoration(int space) {
        this.mSpace = space;
    }

}
