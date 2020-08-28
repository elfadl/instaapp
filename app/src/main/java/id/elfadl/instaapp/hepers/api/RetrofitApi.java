package id.elfadl.instaapp.hepers.api;

import java.util.Map;

import id.elfadl.instaapp.application.MainApplication;
import id.elfadl.instaapp.model.comment.CommentBody;
import id.elfadl.instaapp.model.comment.CommentModel;
import id.elfadl.instaapp.model.feed.FeedModel;
import id.elfadl.instaapp.model.feed.HapusFeedBody;
import id.elfadl.instaapp.model.follower.FollowerBody;
import id.elfadl.instaapp.model.follower.FollowerModel;
import id.elfadl.instaapp.model.like.LikeBody;
import id.elfadl.instaapp.model.like.LikeModel;
import id.elfadl.instaapp.model.post.PostBody;
import id.elfadl.instaapp.model.post.PostModel;
import id.elfadl.instaapp.model.profile.ProfileBody;
import id.elfadl.instaapp.model.profile.ProfileModel;
import id.elfadl.instaapp.model.signin.SignInBody;
import id.elfadl.instaapp.model.signin.SignInModel;
import id.elfadl.instaapp.model.signup.SignUpBody;
import id.elfadl.instaapp.model.signup.SignUpModel;
import id.elfadl.instaapp.model.upload_image_profile.UploadImageProfileModel;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by elmee on 09/03/2016.
 */
public interface RetrofitApi {

//    @Headers(MainApplication.headers)

    @POST(MainApplication.URL_SIGN_IN)
    Call<SignInModel> signIn(@Body SignInBody body);

    @POST(MainApplication.URL_SIGN_UP)
    Call<SignUpModel> signUp(@Body SignUpBody body);

    @Multipart
    @POST(MainApplication.URL_UPLOAD_IMAGE_PROFILE)
    Call<UploadImageProfileModel> uploadImageProfile(@HeaderMap Map<String, String> headers, @Part MultipartBody.Part file);

    @POST(MainApplication.URL_UPDATE_PROFILE)
    Call<ProfileModel> updateProfile(@HeaderMap Map<String, String> headers, @Body ProfileBody body);

    @GET(MainApplication.URL_GET_PROFILE)
    Call<ProfileModel> getProfile(@HeaderMap Map<String, String> headers);

    @GET(MainApplication.URL_GET_PROFILE)
    Call<ProfileModel> getProfile(@HeaderMap Map<String, String> headers, @Query("id_user") Long idUser);

    @GET(MainApplication.URL_FEED)
    Call<FeedModel> getFeed(@HeaderMap Map<String, String> headers, @Query("page") int page);

    @POST(MainApplication.URL_LIKE)
    Call<LikeModel> like(@HeaderMap Map<String, String> headers, @Body LikeBody body);

    @GET(MainApplication.URL_GET_COMMENT)
    Call<CommentModel> getComment(@HeaderMap Map<String, String> headers, @Query("page") int page, @Query("id_post") Long idPost);

    @POST(MainApplication.URL_COMMENT)
    Call<CommentModel> comment(@HeaderMap Map<String, String> headers, @Body CommentBody body);

    @Multipart
    @POST(MainApplication.URL_POST)
    Call<PostModel> post(@HeaderMap Map<String, String> headers, @Part("caption") RequestBody caption, @Part MultipartBody.Part file);

    @POST(MainApplication.URL_HAPUS_FEED)
    Call<FeedModel> hapusFeed(@HeaderMap Map<String, String> headers, @Body HapusFeedBody body);

    @POST(MainApplication.URL_FOLLOW)
    Call<FollowerModel> follow(@HeaderMap Map<String, String> headers, @Body FollowerBody body);

    /*@POST(MainApplication.URL_LOGIN)
    Call<LoginModel> login(@Body LoginBody body);

    @GET(MainApplication.URL_DATA_GURU)
    Call<DataGuruModel> getDataGuru(@HeaderMap Map<String, String> headers);

    @GET(MainApplication.URL_JADWAL)
    Call<JadwalModel> getJadwal(@Query("hari") int hari, @HeaderMap Map<String, String> headers);

    @GET(MainApplication.URL_GET_ABSENSI_SISWA)
    Call<DataAbsensiModel> getAbsensiSiswa(@Query("id_kelas") Long idKelas, @Query("id_jadwal") Long idJadwal, @Query("tanggal") String tanggal, @HeaderMap Map<String, String> headers);

    @GET(MainApplication.URL_GET_DATA_ABSENSI)
    Call<JadwalAbsensiModel> getDataAbsensi(@Query("tanggal") String tanggal, @HeaderMap Map<String, String> headers);

    @POST(MainApplication.URL_ABSENSI)
    Call<AbsensiModel> absensi(@Body AbsensiBody body, @HeaderMap Map<String, String> headers);

    @GET(MainApplication.URL_GET_DATA_SISWA)
    Call<DataSiswaModel> getDataSiswa(@HeaderMap Map<String, String> headers, @Query("page") int page);

    @GET(MainApplication.URL_GET_BENTUK_PELANGGARAN)
    Call<BentukPelanggaranModel> getGetBentukPelanggaran(@HeaderMap Map<String, String> headers);

    @GET(MainApplication.URL_GET_DATA_SISWA_BY_NISN)
    Call<DataSiswaByNisn> getDataSiswaByNisn(@Query("nisn") String nisn, @HeaderMap Map<String, String> headers);

    @POST(MainApplication.URL_PELANGGARAN)
    Call<PelanggaranModel> pelanggaran(@Body PelanggaranBody body, @HeaderMap Map<String, String> headers);

    @GET(MainApplication.URL_PELANGGARAN_SISWA)
    Call<PelanggaranSiswaModel> getPelanggaranSiswa(@Query("id_siswa") Long idSiswa, @HeaderMap Map<String, String> headers, @Query("page") int page);

    @GET(MainApplication.URL_GET_DATA_KELAS)
    Call<DataKelasModel> getDataKelas(@Query("id_siswa") Long idSiswa, @HeaderMap Map<String, String> headers);

    @GET(MainApplication.URL_GET_DETAIL_KELAS)
    Call<DetailKelasSiswaModel> getDetailKelasSiswa(@Query("id_kelas") Long idKelas, @Query("id_siswa") Long idSiswa, @Query("tanggal") String tanggal, @HeaderMap Map<String, String> headers);*/

/*

    @POST()
    Call<VerifikasiResponse> verifikasi(@Url String url, @HeaderMap Map<String, String> headers, @Body VerifikasiBody body);

    @POST()
    Call<VerifikasiResetPinResponse> verifikasiResetPin(@Url String url, @HeaderMap Map<String, String> headers, @Body VerifikasiResetPinBody body);

    @GET()
    Call<List<WilayahModel>> getWilayah(@Url String url, @Nullable @Query("kode") String kode);

    @POST()
    Call<UpdateProfileResponse> updateProfile(@Url String url, @HeaderMap Map<String, String> headers, @Body UpdateProfileBody body);

    @POST()
    Call<ResetPinResponse> resetPin(@Url String url, @HeaderMap Map<String, String> headers, @Body ResetPinBody body);

    @POST()
    Call<CekPinResponse> cekPin(@Url String url, @HeaderMap Map<String, String> headers, @Body CekPinBody body);

    @GET
    Call<RequestSaldoModel> requestSaldo(@Url String url, @HeaderMap Map<String, String> headers);

    @GET
    Call<MenuModel> requestMenu(@Url String url, @HeaderMap Map<String, String> headers);

    @GET
    Call<ModelGroupProduk> requestProduk(@Url String url, @HeaderMap Map<String, String> headers);

    @GET
    Call<ModelGroupProduk> requestProdukKolektif(@Url String url, @HeaderMap Map<String, String> headers, @Query("tipe") String tipeProduk);

    @POST
    Call<ReferrerResponse> getReferrer(@Url String url, @HeaderMap Map<String, String> headers, @Body ReferrerBody body);

    @POST
    Call<ReferrerAddResponse> addReferrer(@Url String url, @HeaderMap Map<String, String> headers, @Body ReferrerBody body);

    @POST
    Call<UbahMarkupResponse> ubahMarkup(@Url String url, @HeaderMap Map<String, String> headers, @Body UbahMarkupBody body);

    @GET
    Call<DataMemberModel> getDataMember(@Url String url, @HeaderMap Map<String, String> headers);

    @POST
    Call<DataMemberModel> getDataMember(@Url String url, @HeaderMap Map<String, String> headers, @Query("page") int page, @Body DataMemberBody body);

    @GET
    Call<RiwayatBonusModel> getRiwayatBonus(@Url String url, @HeaderMap Map<String, String> headers);

    @POST
    Call<RiwayatBonusModel> getRiwayatBonus(@Url String url, @HeaderMap Map<String, String> headers, @Query("page") int page, @Body RiwayatBonusBody body);

    @GET
    Call<DaftarTiketModel> getListTiket(@Url String url, @HeaderMap Map<String, String> headers);

    @POST
    Call<TopUpResponse> topUp(@Url String url, @HeaderMap Map<String, String> headers, @Body TopUpBody body);

    @POST
    Call<TransferModel> transfer(@Url String url, @HeaderMap Map<String, String> headers, @Body TransferBody body);

    @POST
    Call<MutasiResponse> getMutasi(@Url String url, @HeaderMap Map<String, String> headers, @Body MutasiBody body, @Query("page") int page);

    @GET
    Call<TransaksiPendingResponse> getTransaksiPending(@Url String url, @HeaderMap Map<String, String> headers, @Query("page") int page);

    @POST
    Call<SemuaTransaksiResponse> getSemuaTransaksi(@Url String url, @HeaderMap Map<String, String> headers, @Body SemuaTransaksiBody body, @Query("page") int page);

    @POST
    Call<PopupHistoryTransaksiResponse> semuaTransaksi(@Url String url, @HeaderMap Map<String, String> headers, @Body SemuaTransaksiBody body);

    @POST
    Call<PelangganResponse> getPelanggan(@Url String url, @HeaderMap Map<String, String> headers, @Body PelangganBody body);

    @POST
    Call<PelangganResponse> getPelanggan(@Url String url, @HeaderMap Map<String, String> headers, @Body HapusPelangganBody body);

    @POST
    Call<PelangganResponse> deletePelanggan(@Url String url, @HeaderMap Map<String, String> headers, @Body PelangganBody body);

    *//*@POST
    Call<PelangganResponse> getPelanggan(@Url String url, @HeaderMap Map<String, String> headers, @Body PelangganDeleteBody body);*//*

    @POST
    Call<BeritaResponse> getBerita(@Url String url, @HeaderMap Map<String, String> headers, @Body BeritaBody body);

    @POST
    Call<BeritaResponse> getBerita(@Url String url, @HeaderMap Map<String, String> headers);

    @POST
    Call<FirebaseResponse> updateFirebase(@Url String url, @HeaderMap Map<String, String> headers, @Body FirebaseBody body);

    @GET
    Call<AkunResponse> getAkun(@Url String url, @HeaderMap Map<String, String> headers);

    @GET
    Call<StatistikResponse> getStatistik(@Url String url, @HeaderMap Map<String, String> headers);

    @Multipart
    @POST
    Call<FotoProfilResponse> updateFotoProfil(@Url String url, @HeaderMap Map<String, String> headers, @Part MultipartBody.Part foto);

    @POST
    Call<HargaProdukResponse> getHargaProduk(@Url String url, @HeaderMap Map<String, String> headers, @Body HargaProdukBody body, @Query("page") int page);

    @GET
    Call<ProviderResponse> getProvider(@Url String url, @HeaderMap Map<String, String> headers);

    @POST
    Call<ProviderResponse> getProvider(@Url String url, @HeaderMap Map<String, String> headers, @Body ProviderBody body);

    @POST
    Call<ProdukPrepaidResponse> getProdukPrepaid(@Url String url, @HeaderMap Map<String, String> headers, @Body ProdukPrepaidBody body, @Query("page") int page);

    @POST
    Call<ProdukPrepaidResponse> getProdukPrepaid(@Url String url, @HeaderMap Map<String, String> headers, @Body ProdukPrepaidBody body);

    @GET
    Call<ListPengirimResponse> getListPengirim(@Url String url, @HeaderMap Map<String, String> headers);

    @POST
    Call<ModelProduk> getProduk(@Url String url, @HeaderMap Map<String, String> headers, @Body ProdukBody body, @Query("page") int page);

    @POST
    Call<ModelLogPelanggan> getLogPelanggan(@Url String url, @HeaderMap Map<String, String> headers, @Body LogPelangganBody body);

    @POST
    Call<ModelKolektif> getKolektif(@Url String url, @HeaderMap Map<String, String> headers, @Body KolektifBody body);

    @POST
    Call<OtpManualResponse> otpManual(@Url String url, @HeaderMap Map<String, String> headers, @Body OtpManualBody body);

    @POST
    Call<TransaksiResponse> transaksi(@Url String url, @HeaderMap Map<String, String> headers, @Body TransaksiBody body);

    @POST
    Call<TransaksiResponse> struk(@Url String url, @HeaderMap Map<String, String> headers, @Body StrukBody body);

    @POST
    Call<ChatResponse> chat(@Url String url, @HeaderMap Map<String, String> headers, @Body ChatBody body);

    @POST
    Call<ChatInputResponse> chatInput(@Url String url, @HeaderMap Map<String, String> headers, @Body ChatBody body);

    @GET
    Call<NotificationResponse> getNotifikasi(@Url String url, @HeaderMap Map<String, String> headers);

    @POST
    Call<NotificationResponse> readNotif(@Url String url, @HeaderMap Map<String, String> headers, @Body NotifikasiBody body);

    @POST
    Call<NotificationResponse> readNotif(@Url String url, @HeaderMap Map<String, String> headers, @Body NotifReadAllBody body);

    @GET
    Call<BantuanResponse> getBantuan(@Url String url);

    @Streaming
    @POST
    Call<ResponseBody> rekapTagihan(@Url String url, @Body RekapTagihanBody body);*/

}