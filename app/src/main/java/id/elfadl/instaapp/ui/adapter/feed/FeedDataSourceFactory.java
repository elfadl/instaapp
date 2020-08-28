package id.elfadl.instaapp.ui.adapter.feed;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import id.elfadl.instaapp.model.feed.Feed;
import id.elfadl.instaapp.viewmodel.BaseObservableViewModel;

public class FeedDataSourceFactory extends DataSource.Factory<Integer, Feed> {

    private BaseObservableViewModel viewModel;
    private FeedDataSource feedDataSource;
    private MutableLiveData<PageKeyedDataSource<Integer, Feed>> feedLiveData = new MutableLiveData<>();

    public FeedDataSourceFactory(BaseObservableViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public DataSource<Integer, Feed> create() {
        feedDataSource = new FeedDataSource(viewModel);

        feedLiveData.postValue(feedDataSource);

        return feedDataSource;
    }

    public void refresh() {
        if (feedDataSource != null)
            feedDataSource.invalidate();
    }

    public MutableLiveData<PageKeyedDataSource<Integer, Feed>> getFeedLiveData() {
        return feedLiveData;
    }
}
