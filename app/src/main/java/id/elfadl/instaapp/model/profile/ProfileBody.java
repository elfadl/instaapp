
package id.elfadl.instaapp.model.profile;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class ProfileBody {

    @Expose
    private String bio;
    @SerializedName("jenis_kelamin")
    private int jenisKelamin;
    @Expose
    private String nama;
    @SerializedName("no_hp")
    private String noHp;

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(int jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
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

}
