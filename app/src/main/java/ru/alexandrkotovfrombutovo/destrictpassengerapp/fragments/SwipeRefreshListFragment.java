package ru.alexandrkotovfrombutovo.destrictpassengerapp.fragments;

import android.app.ListFragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by alexkotov on 22.01.18.
 */

public class SwipeRefreshListFragment extends ListFragment {

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View listFragmentView = super.onCreateView(inflater, container, savedInstanceState);
        mSwipeRefreshLayout = new ListFragmentSwipeRefreshLayout(container.getContext());
        mSwipeRefreshLayout.addView(listFragmentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mSwipeRefreshLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        return mSwipeRefreshLayout;
    }

    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener listener){
        mSwipeRefreshLayout.setOnRefreshListener(listener);
    }

    public boolean isRefreshing(){
        return mSwipeRefreshLayout.isRefreshing();
    }

    public void setRefreshing(boolean refreshing){
        mSwipeRefreshLayout.setRefreshing(refreshing);
    }

    public void setColorScheme(int... col){
        mSwipeRefreshLayout.setColorSchemeResources(col);
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }

    private class ListFragmentSwipeRefreshLayout extends SwipeRefreshLayout{

        public ListFragmentSwipeRefreshLayout(Context context) {
            super(context);
        }

        @Override
        public boolean canChildScrollUp() {
            final ListView listView = getListView();
            if(listView.getVisibility()==VISIBLE){
                return canListViewScrollUp(listView);
            }else{
                return false;
            }
        }

    }
    private static boolean canListViewScrollUp(ListView listView) {
        if(Build.VERSION.SDK_INT >= 14){
            return ViewCompat.canScrollVertically(listView, -1);
        }else {
            return listView.getChildCount() > 0 && (listView.getFirstVisiblePosition() > 0 || listView.getChildAt(0).getTop() < listView.getPaddingTop());
        }
    }
}
