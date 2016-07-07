package kr.innisfree.playgreen.listener;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by preparkha on 2015. 11. 12..
 */
public abstract class RecyclerOnScrollListener extends RecyclerView.OnScrollListener {

    /* paging */
    private boolean loading = true; // after updating, true and during updating, false
    private int pastVisiblesItem, visibleItemCount, totalItemCount;
    private int currentPage = 0;

    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;

    public RecyclerOnScrollListener() {
        this.currentPage = currentPage;
    }

    public void setGridLayoutManager(GridLayoutManager gridLayoutManager) {
        this.gridLayoutManager = gridLayoutManager;
        linearLayoutManager = null;
    }

    public void setLinearLayoutManager(LinearLayoutManager manager) {
        this.linearLayoutManager = manager;
        gridLayoutManager = null;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (linearLayoutManager == null && gridLayoutManager == null) return;

        if (gridLayoutManager != null) {
            visibleItemCount = gridLayoutManager.getChildCount();
            totalItemCount = gridLayoutManager.getItemCount();
            pastVisiblesItem = gridLayoutManager.findFirstVisibleItemPosition();
        } else {
            visibleItemCount = linearLayoutManager.getChildCount();
            totalItemCount = linearLayoutManager.getItemCount();
            pastVisiblesItem = linearLayoutManager.findFirstVisibleItemPosition();
        }

       // JYLog.D("1:" + (visibleItemCount + pastVisiblesItem) + ", 2:" + totalItemCount, new Throwable());

        if (loading) {
            if ((visibleItemCount + pastVisiblesItem) >= totalItemCount) {
                loading = false;
                //currentPage++;
                onLoadMore();
            }
        }
    }

    public abstract void onLoadMore();

}
