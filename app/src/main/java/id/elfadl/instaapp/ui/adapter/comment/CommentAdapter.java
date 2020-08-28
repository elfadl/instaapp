package id.elfadl.instaapp.ui.adapter.comment;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;

import id.elfadl.instaapp.R;
import id.elfadl.instaapp.databinding.LayoutCommentBinding;
import id.elfadl.instaapp.hepers.HeadersUtil;
import id.elfadl.instaapp.hepers.api.RetrofitApiSingleton;
import id.elfadl.instaapp.hepers.network.RequestObservableAPI;
import id.elfadl.instaapp.model.comment.Comment;
import id.elfadl.instaapp.model.feed.Feed;
import id.elfadl.instaapp.model.like.LikeBody;
import id.elfadl.instaapp.model.like.LikeModel;
import id.elfadl.instaapp.viewmodel.LayoutCommentViewModel;
import retrofit2.Call;

public class CommentAdapter extends PagedListAdapter<Comment, CommentAdapter.CommentViewHolder> {

    private Feed feed;

    public CommentAdapter(Feed feed) {
        super(DIFF_CALLBACK);
        this.feed = feed;
    }

    private static DiffUtil.ItemCallback<Comment> DIFF_CALLBACK = new DiffUtil.ItemCallback<Comment>() {
        @Override
        public boolean areItemsTheSame(@NonNull Comment feedOld, @NonNull Comment feedNew) {
            return feedOld.getId().equals(feedNew.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Comment feedOld, @NonNull Comment feedNew) {
            String downline1 = new Gson().toJson(feedOld);
            String downline2 = new Gson().toJson(feedNew);
            return downline1.equals(downline2);
        }
    };

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutCommentBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.layout_comment, viewGroup, false);

        return new CommentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int i) {
        Comment item = getItem(i);
        LayoutCommentViewModel viewModel = new LayoutCommentViewModel();
        viewModel.setFirst(i == 0);
        PrettyTime prettyTime = new PrettyTime();
        if(viewModel.isFirst()){
            viewModel.setImgProfileTS(feed.getImageProfile());
            viewModel.setUsernameTS(feed.getUsername());
            viewModel.setCaptionTS(feed.getCaption());
            Date d = new Date(feed.getPostAt()*1000);
            viewModel.setPostAtTS(prettyTime.format(d));
        }
        viewModel.setImgProfile(item.getImageProfile());
        String caption = item.getUsername()+" "+item.getComment();
        Spannable sb = new SpannableString(caption);
        sb.setSpan(new StyleSpan(Typeface.BOLD), 0, item.getUsername().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new StyleSpan(Typeface.NORMAL), item.getUsername().length() + 1, caption.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.binding.txtComment.setText(sb,  TextView.BufferType.SPANNABLE);
        Date d = new Date(item.getPostAt()*1000);
        viewModel.setPostAt(prettyTime.format(d));
        holder.binding.setViewmodel(viewModel);
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        LayoutCommentBinding binding;

        public CommentViewHolder(LayoutCommentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

}
