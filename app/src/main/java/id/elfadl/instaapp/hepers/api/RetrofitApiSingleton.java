package id.elfadl.instaapp.hepers.api;

import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.concurrent.TimeUnit;

import id.elfadl.instaapp.application.MainApplication;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by elmee on 09/03/2016.
 */
public class RetrofitApiSingleton {

    private static RetrofitApiSingleton singleton;
    private RetrofitApi retrofitApi;

    public static RetrofitApiSingleton getInstance() {
        if (null == singleton) {
            singleton = new RetrofitApiSingleton();
        }
        return singleton;
    }

    public void init() {

        //MainApplication apps = new MainApplication();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        /*HttpLoggingInterceptor logging2 = new HttpLoggingInterceptor();
        logging2.setLevel(HttpLoggingInterceptor.Level.HEADERS);*/

        /*String authToken = Credentials.basic(MainApplication.API_KEY_ADMIN, MainApplication.API_PASSWORD);
        AuthenticationInterceptor interceptor =
                new AuthenticationInterceptor(authToken);*/

        String UA = System.getProperty("http.agent");  // Get android user agent.

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        if (MainApplication.DEBUG_MODE)
            httpClient.addInterceptor(logging);
//        httpClient.addInterceptor(logging2);
//        httpClient.addInterceptor(new UserAgentInterceptor(UA));
        httpClient.addInterceptor(chain -> {
            Request request = chain.request();
            Request requestWithUserAgent = request.newBuilder()
                    .header("User-Agent", UA)
                    .build();
            Response response = chain.proceed(requestWithUserAgent);
            if (response.code() != 200) {
                Intent intent = new Intent("dbmadeha_network_error");
                // You can also include some extra data.
                intent.putExtra("message", response.message());
                intent.putExtra("code", response.code());
                LocalBroadcastManager.getInstance(MainApplication.getContext()).sendBroadcast(intent);
            }else {
                /*if(!Utils.isJSONValid(response.message())){
                    Intent intent = new Intent("mpn_network_error");
                    // You can also include some extra data.
                    intent.putExtra("message", response.message());
                    intent.putExtra("code", response.code());
                    LocalBroadcastManager.getInstance(MainApplication.getContext()).sendBroadcast(intent);
                }*/
            }
            return response;
        });
        //httpClient.addInterceptor(interceptor);
        //httpClient.networkInterceptors().add(new CachingControlInterceptor());
        httpClient.connectTimeout(30, TimeUnit.SECONDS);
        httpClient.readTimeout(30, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainApplication.MAIN_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        retrofitApi = retrofit.create(RetrofitApi.class);
    }

    /*public RetrofitApi getApiAuth(){

        //MainApplication apps = new MainApplication();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        String authToken = Credentials.basic(MainApplication.API_KEY_ADMIN, MainApplication.API_PASSWORD);
        AuthenticationInterceptor interceptor =
                new AuthenticationInterceptor(authToken);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        httpClient.addInterceptor(interceptor);
        //httpClient.networkInterceptors().add(new CachingControlInterceptor());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainApplication.MAIN_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        return retrofit.create(RetrofitApi.class);
    }

    public RetrofitApi getApiXendit(){

        //MainApplication apps = new MainApplication();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        String authToken = Credentials.basic(MainApplication.XENDIT_API_KEY, "");
        AuthenticationInterceptor interceptor =
                new AuthenticationInterceptor(authToken);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        httpClient.addInterceptor(interceptor);
        //httpClient.networkInterceptors().add(new CachingControlInterceptor());

        *//*OkHttpClient serviceClient = httpClient.build();
        try {
            serviceClient.cache().delete();
        } catch (IOException e) {
            e.printStackTrace();
        }*//*

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainApplication.MAIN_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        return retrofit.create(RetrofitApi.class);
    }*/

    public RetrofitApi getApi() {
        return retrofitApi;
    }

}
