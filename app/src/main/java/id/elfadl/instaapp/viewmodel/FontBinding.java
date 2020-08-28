package id.elfadl.instaapp.viewmodel;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;

import id.elfadl.instaapp.hepers.FontCache;
import id.elfadl.instaapp.hepers.prefs.Prefs;

/**
 * Created by kamal on 24/11/16.
 */

public class FontBinding {

    @BindingAdapter({"bind:font"})
    public static void setFont(TextView textView, String fontName) {
        textView.setTypeface(FontCache.getInstance(textView.getContext()).get(fontName));
    }

    @BindingAdapter({"bind:img"})
    public static void setImg(ImageView imageView, String image) {
        Glide.with(imageView.getContext()).load(image).centerCrop().into(imageView);
    }

    @BindingAdapter({"bind:imgrounded"})
    public static void setImgRounded(ImageView imageView, String image) {
        Glide.with(imageView.getContext()).load(image).apply(RequestOptions.bitmapTransform(new RoundedCorners(4))).into(imageView);
    }

    @BindingAdapter({"bind:imgproduk"})
    public static void setImgProduk(ImageView imageView, String image) {
        Glide.with(imageView.getContext()).load(image).apply(RequestOptions.bitmapTransform(new RoundedCorners(19))).into(imageView);
    }

    @BindingAdapter({"bind:imgprodukres"})
    public static void setImgProdukRes(ImageView imageView, int image) {
        Glide.with(imageView.getContext()).load(image).apply(RequestOptions.bitmapTransform(new RoundedCorners(19))).into(imageView);
    }

    @BindingAdapter({"bind:imgprofil"})
    public static void setImgProfil(ImageView imageView, String image) {
        Glide.with(imageView.getContext()).load(image)
                /*.diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)*/
                .signature(new ObjectKey(Prefs.getLong("update_foto", 0l) != 0l ? Prefs.getLong("update_foto", 0l) : "null"))
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(imageView);
    }

    @BindingAdapter({"bind:imguser"})
    public static void setImgUser(ImageView imageView, String image) {
//        Glide.with(imageView.getContext()).load(image).apply(RequestOptions.bitmapTransform(new CircleCrop()).placeholder(R.drawable.ic_placeholder_photo_user)).into(imageView);
    }

    @BindingAdapter("layout_height")
    public static void setLayoutHeight(View view, float height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) height;
        view.setLayoutParams(layoutParams);
    }

}
