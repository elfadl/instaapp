
package id.elfadl.instaapp.model.profile;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Profile {

    @Expose
    private String bio;
    @Expose
    private String foto;
    @SerializedName("jenis_kelamin")
    private int jenisKelamin;
    @SerializedName("my_post")
    private List<MyPost> myPost;
    @Expose
    private String nama;
    @SerializedName("no_hp")
    private String noHp;
    @SerializedName("total_comment")
    private String totalComment;
    @SerializedName("total_follower")
    private String totalFollower;
    @SerializedName("total_following")
    private String totalFollowing;
    @SerializedName("total_post")
    private String totalPost;
    @Expose
    private String username;
    @Expose
    private boolean followed;

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(int jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public List<MyPost> getMyPost() {
        return myPost;
    }

    public void setMyPost(List<MyPost> myPost) {
        this.myPost = myPost;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getTotalComment() {
        return totalComment;
    }

    public void setTotalComment(String totalComment) {
        this.totalComment = totalComment;
    }

    public String getTotalPost() {
        return totalPost;
    }

    public void setTotalPost(String totalPost) {
        this.totalPost = totalPost;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isFollowed() {
        return followed;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }

    public String getTotalFollower() {
        return totalFollower;
    }

    public void setTotalFollower(String totalFollower) {
        this.totalFollower = totalFollower;
    }

    public String getTotalFollowing() {
        return totalFollowing;
    }

    public void setTotalFollowing(String totalFollowing) {
        this.totalFollowing = totalFollowing;
    }
}
