
package id.elfadl.instaapp.model.comment;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Comment {

    @Expose
    private String comment;
    @Expose
    private Long id;
    @SerializedName("image_profile")
    private String imageProfile;
    @SerializedName("post_at")
    private Long postAt;
    @Expose
    private String username;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
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
