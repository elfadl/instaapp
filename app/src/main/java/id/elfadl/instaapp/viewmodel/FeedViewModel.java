package id.elfadl.instaapp.viewmodel;

import android.app.Activity;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import java.util.Map;

import id.elfadl.instaapp.model.feed.Feed;
import id.elfadl.instaapp.ui.adapter.feed.FeedDataSourceFactory;

public class FeedViewModel extends BaseObservableViewModel {

    LiveData<PagedList<Feed>> feedPagedList;
    LiveData<PageKeyedDataSource<Integer, Feed>> feedDataSource;
    FeedDataSourceFactory feedDataSourceFactory;

    public LiveData<PagedList<Feed>> getFeedPagedList() {
        return feedPagedList;
    }

    public void refresh() {
        /*if (historyDataSource != null)
            historyDataSource.getValue().invalidate();*/
        if(feedDataSourceFactory != null)
            feedDataSourceFactory.refresh();
    }

    public FeedViewModel() {
        feedDataSourceFactory = new FeedDataSourceFactory(this);
//        memberDataSourceFactory.in

        feedDataSource = feedDataSourceFactory.getFeedLiveData();

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(10)
                .setPageSize(10)
                .build();

        feedPagedList = new LivePagedListBuilder(feedDataSourceFactory, config).build();

    }

}
