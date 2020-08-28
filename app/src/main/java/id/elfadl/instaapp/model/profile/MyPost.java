
package id.elfadl.instaapp.model.profile;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class MyPost {

    @Expose
    private String caption;
    @Expose
    private Long comment;
    @Expose
    private Long id;
    @Expose
    private String image;
    @SerializedName("image_profile")
    private String imageProfile;
    @SerializedName("is_like")
    private Boolean isLike;
    @Expose
    private Long like;
    @SerializedName("post_at")
    private Long postAt;
    @Expose
    private String username;

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Long getComment() {
        return comment;
    }

    public void setComment(Long comment) {
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }

    public Boolean getIsLike() {
        return isLike;
    }

    public void setIsLike(Boolean isLike) {
        this.isLike = isLike;
    }

    public Long getLike() {
        return like;
    }

    public void setLike(Long like) {
        this.like = like;
    }

    public Long getPostAt() {
        return postAt;
    }

    public void setPostAt(Long postAt) {
        this.postAt = postAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
