
package id.elfadl.instaapp.model.feed;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Feed {

    @Expose
    private String caption;
    @Expose
    private int comment;
    @Expose
    private Long id;
    @SerializedName("id_user")
    private Long idUser;
    @SerializedName("image_profile")
    private String imageProfile;
    @Expose
    private String image;
    @Expose
    private String username;
    @Expose
    private int like;
    @SerializedName("post_at")
    private Long postAt;
    @SerializedName("is_like")
    private boolean isLike;
    @SerializedName("can_comment")
    private boolean canComment;

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
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

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public Long getPostAt() {
        return postAt;
    }

    public void setPostAt(Long postAt) {
        this.postAt = postAt;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public boolean isCanComment() {
        return canComment;
    }

    public void setCanComment(boolean canComment) {
        this.canComment = canComment;
    }
}
