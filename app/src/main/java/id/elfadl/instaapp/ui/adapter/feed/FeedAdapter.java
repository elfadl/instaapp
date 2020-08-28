package id.elfadl.instaapp.ui.adapter.feed;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.Locale;

import id.elfadl.instaapp.R;
import id.elfadl.instaapp.application.MainApplication;
import id.elfadl.instaapp.databinding.LayoutFeedBinding;
import id.elfadl.instaapp.hepers.HeadersUtil;
import id.elfadl.instaapp.hepers.api.RetrofitApi;
import id.elfadl.instaapp.hepers.api.RetrofitApiSingleton;
import id.elfadl.instaapp.hepers.network.RequestObservableAPI;
import id.elfadl.instaapp.model.feed.Feed;
import id.elfadl.instaapp.model.feed.FeedModel;
import id.elfadl.instaapp.model.feed.HapusFeedBody;
import id.elfadl.instaapp.model.like.LikeBody;
import id.elfadl.instaapp.model.like.LikeModel;
import id.elfadl.instaapp.model.profile.Profile;
import id.elfadl.instaapp.ui.activity.Comment;
import id.elfadl.instaapp.ui.activity.ProfileMember;
import id.elfadl.instaapp.viewmodel.LayoutFeedViewModel;
import retrofit2.Call;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class FeedAdapter extends PagedListAdapter<Feed, FeedAdapter.FeedViewHolder> {

    private Context context;
    private OnDeleteListener onDeleteListener;

    public FeedAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    private static DiffUtil.ItemCallback<Feed> DIFF_CALLBACK = new DiffUtil.ItemCallback<Feed>() {
        @Override
        public boolean areItemsTheSame(@NonNull Feed feedOld, @NonNull Feed feedNew) {
            return feedOld.getId().equals(feedNew.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Feed feedOld, @NonNull Feed feedNew) {
            String downline1 = new Gson().toJson(feedOld);
            String downline2 = new Gson().toJson(feedNew);
            return downline1.equals(downline2);
        }
    };

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutFeedBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.layout_feed, viewGroup, false);

        return new FeedViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int i) {
        Feed item = getItem(i);
        Profile profile = new Gson().fromJson(MainApplication.getFromCache("profile"), Profile.class);
        LayoutFeedViewModel viewModel = new LayoutFeedViewModel();
        viewModel.setImgProfile(item.getImageProfile());
        viewModel.setUsername(item.getUsername());
        viewModel.setImagePost(item.getImage());
        viewModel.setTotalLikes(String.valueOf(item.getLike()));
        viewModel.setTotalComments(String.valueOf(item.getComment()));
        viewModel.setAdaKomentar(item.getComment() > 0);
        viewModel.setAdaLike(item.getLike() > 0);
        viewModel.setLike(item.isLike());
        Date d = new Date(item.getPostAt()*1000);
        PrettyTime prettyTime = new PrettyTime(Locale.getDefault());
        viewModel.setPostAt(prettyTime.format(d));
        String caption = item.getUsername()+" "+item.getCaption();
        Spannable sb = new SpannableString(caption);
        sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, viewModel.getUsername().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new StyleSpan(Typeface.NORMAL), viewModel.getUsername().length() + 1, caption.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.binding.txtCaption.setText(sb,  TextView.BufferType.SPANNABLE);
        holder.binding.setViewmodel(viewModel);
        final GestureDetector gd = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {

                Animation pulse_fade = AnimationUtils.loadAnimation(context, R.anim.pulse_fade_in);
                pulse_fade.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        holder.binding.love.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        holder.binding.love.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                holder.binding.love.startAnimation(pulse_fade);
                like(viewModel, item);
                notifyItemChanged(i);
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);

            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return true;
            }
        });

        holder.binding.image.setOnTouchListener((v, event) -> gd.onTouchEvent(event));

        holder.binding.btnLike.setOnClickListener(v -> {
            like(viewModel, item);
            notifyItemChanged(i);
        });
        holder.binding.btnComment.setOnClickListener(v -> {
            if(item.isCanComment() || item.getUsername().equals(profile.getUsername())) {
                holder.binding.getRoot().getContext().startActivity(new Intent(
                                holder.binding.getRoot().getContext(),
                                Comment.class
                        ).putExtra("data", new Gson().toJson(item))
                );
            }
        });
        if(item.isCanComment() || item.getUsername().equals(profile.getUsername())) {
            holder.binding.allComment.setOnClickListener(v -> holder.binding.btnComment.performClick());
            holder.binding.txtCaption.setOnClickListener(v -> holder.binding.btnComment.performClick());
            holder.binding.btnComment.setVisibility(View.VISIBLE);
        }else {
            holder.binding.btnComment.setVisibility(View.GONE);
            viewModel.setAdaKomentar(false);
        }
        holder.binding.txtUsername.setOnClickListener(v -> {
            if(!item.getUsername().equals(profile.getUsername())) {
                holder.binding.getRoot().getContext().startActivity(new Intent(
                        holder.binding.getRoot().getContext(),
                        ProfileMember.class
                ).putExtra("id_user", item.getIdUser()));
            }
        });
        holder.binding.imgUser.setOnClickListener(v -> holder.binding.txtUsername.performClick());
        Log.i("PROFILE", "onBindViewHolder: "+MainApplication.getFromCache("profile"));
        holder.binding.btnMore.setVisibility(item.getUsername().equals(profile.getUsername()) ? View.VISIBLE : View.GONE);
        holder.binding.btnMore.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(context, v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_feed, popup.getMenu());
            popup.setOnMenuItemClickListener(item1 -> {
                switch (item1.getItemId()){
                    case R.id.menu_hapus:
                        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                        dialog.setTitle("Apakah Anda yakin akan menghapus postingan ini?");
                        dialog.setPositiveButton("Yakin", (dialog1, which) -> {
                            HapusFeedBody body = new HapusFeedBody();
                            body.setIdPost(item.getId());
                            new RequestObservableAPI<FeedModel>(context, FeedModel.class, true) {
                                @Override
                                public void onResult(FeedModel result) {
                                    if(result == null){
                                        Toast.makeText(context, "Terjadi ganggauan pada server.", Toast.LENGTH_SHORT).show();
                                    }else {
                                        if(result.getStatus() != 1){
                                            Toast.makeText(context, result.getMessage(), Toast.LENGTH_SHORT).show();
                                        }else {
                                            if(onDeleteListener != null)
                                                onDeleteListener.onDelete();
                                        }
                                    }
                                }

                                @Override
                                public Call<FeedModel> createCall() {
                                    return RetrofitApiSingleton.getInstance().getApi().hapusFeed(HeadersUtil.getInstance().getHeaders(), body);
                                }
                            };
                        });
                        dialog.setNegativeButton("Batal", null);
                        dialog.show();
                        return true;
                    default:
                        return false;
                }
            });
            popup.show();
        });
    }

    private void like(LayoutFeedViewModel viewModel, Feed feed){
        if(viewModel.isLike()){
            viewModel.setTotalLikes(String.valueOf(Integer.parseInt(viewModel.getTotalLikes())-1));
        }else {
            viewModel.setTotalLikes(String.valueOf(Integer.parseInt(viewModel.getTotalLikes())+1));
        }
        viewModel.setAdaLike(Integer.parseInt(viewModel.getTotalLikes()) > 0);
        viewModel.setLike(!viewModel.isLike());
        feed.setLike(!feed.isLike());
        LikeBody body = new LikeBody();
        body.setIdPost(feed.getId());
        new RequestObservableAPI<LikeModel>(context, LikeModel.class, true, true) {
            @Override
            public void onResult(LikeModel result) {
                if(result == null){
                    Toast.makeText(context, "Terjadi kesalahan dengan Server.", Toast.LENGTH_SHORT).show();
                }else {
                    if(result.getStatus() != 1){
                        Toast.makeText(context, result.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public Call<LikeModel> createCall() {
                return RetrofitApiSingleton.getInstance().getApi().like(HeadersUtil.getInstance().getHeaders(), body);
            }
        };
    }

    public class FeedViewHolder extends RecyclerView.ViewHolder {

        LayoutFeedBinding binding;

        public FeedViewHolder(LayoutFeedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

    public interface OnDeleteListener{
        void onDelete();
    }

}
