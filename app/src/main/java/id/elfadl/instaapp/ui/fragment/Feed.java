package id.elfadl.instaapp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import id.elfadl.instaapp.R;
import id.elfadl.instaapp.databinding.FeedBinding;
import id.elfadl.instaapp.ui.adapter.feed.FeedAdapter;
import id.elfadl.instaapp.viewmodel.FeedViewModel;

public class Feed extends BaseFragment {

    private FeedBinding binding;
    private FeedViewModel viewModel;
    private FeedAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.feed, container, false);
        viewModel = new ViewModelProvider(this).get(FeedViewModel.class);
        binding.setViewmodel(viewModel);

        adapter = new FeedAdapter(getContext());
        binding.recyclerFeed.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerFeed.setAdapter(adapter);

        init();

        adapter.setOnDeleteListener(() -> viewModel.refresh());

        return binding.getRoot();
    }

    private void init() {
        viewModel.getFeedPagedList().observe(getViewLifecycleOwner(), feeds -> adapter.submitList(feeds));
    }

    public void refresh(){
        viewModel.refresh();
    }

}
