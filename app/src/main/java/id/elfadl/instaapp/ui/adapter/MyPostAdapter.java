package id.elfadl.instaapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import id.elfadl.instaapp.R;
import id.elfadl.instaapp.databinding.LayoutMyPostBinding;
import id.elfadl.instaapp.model.profile.MyPost;

public class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.MyPostViewHolder> {

    List<MyPost> data;

    public MyPostAdapter(List<MyPost> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public MyPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutMyPostBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_my_post, parent, false);
        return new MyPostViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPostViewHolder holder, int position) {
        MyPost item = data.get(position);
        Glide.with(holder.binding.getRoot().getContext()).load(item.getImage()).into(holder.binding.image);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyPostViewHolder extends RecyclerView.ViewHolder{

        LayoutMyPostBinding binding;

        public MyPostViewHolder(LayoutMyPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
