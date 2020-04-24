package com.example.oud;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.oud.api.OudApi;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.example.oud.api.StatusMessageResponse;
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.huxq17.download.Pump;
import com.huxq17.download.PumpFactory;
import com.huxq17.download.core.DownloadInfo;
import com.huxq17.download.core.service.IDownloadManager;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import androidx.annotation.Nullable;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class OudUtils {

    private static final String TAG = OudUtils.class.getSimpleName();

    private static OkHttpClient OK_HTTP_CLIENT;

    public static OudApi instantiateRetrofitOudApi(String baseUrl) {

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(OudUtils.getGson()))
                .build();


        return retrofit.create(OudApi.class);

    }


    public static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {

            long t1 = System.nanoTime();
            Request request = chain.request();

            if ((Constants.SERVER_CONNECTION_AWARE_LOG_SETTINGS & Constants.SENDING) == Constants.SENDING) {

                final Buffer buffer = new Buffer();
                if (request.body() != null)
                    request.body().writeTo(buffer);


                Log.i(TAG, String.format("Sending request %s on %s%n Headers: %s%n Body: %s",
                        request.url(), chain.connection(), request.headers(), buffer.readUtf8()));

            }

            long t2 = System.nanoTime();
            Response response = chain.proceed(request);

            if ((Constants.SERVER_CONNECTION_AWARE_LOG_SETTINGS & Constants.RECEIVING) == Constants.RECEIVING) {
                Log.i(TAG, String.format("Received response for %s in %.1fms%n%s",
                        response.request().url(), (t2 - t1) / 1e6d, response.headers()));
            }


            final String responseString = new String(response.body().bytes());
            if ((Constants.SERVER_CONNECTION_AWARE_LOG_SETTINGS & Constants.JSON_RESPONSE) == Constants.JSON_RESPONSE) {
                Log.i(TAG, "Response for " + response.request().url()+ ": " + responseString);
            }

            return  response.newBuilder()
                    .body(ResponseBody.create(response.body().contentType(), responseString))
                    .build();
        }
    }

    public static <T> String commaSeparatedListQueryParameter(ArrayList<T> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (T item : list) {
            stringBuilder.append(item.toString());
            stringBuilder.append(',');
        }
        if (stringBuilder.length() > 0)
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    public static <T> String commaSeparatedListQueryParameter(T[] list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (T item : list) {
            stringBuilder.append(item.toString());
            stringBuilder.append(',');
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    public static void saveUserData(View v, String token, String userId) {

        SharedPreferences prefs = v.getContext().getSharedPreferences(Constants.SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);

        token = "Bearer " + token;

        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.SHARED_PREFERENCES_TOKEN_NAME, token);
        prefsEditor.putString(Constants.SHARED_PREFERENCES_USER_ID_NAME, userId);

        prefsEditor.apply();    //token saved in shared preferences

    }

    public static void saveUserData(Context context, String token, String userId) {

        SharedPreferences prefs = context.getSharedPreferences(Constants.SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);

        token = "Bearer " + token;

        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.SHARED_PREFERENCES_TOKEN_NAME, token);
        prefsEditor.putString(Constants.SHARED_PREFERENCES_USER_ID_NAME, userId);
        prefsEditor.apply();    //token saved in shared preferences

    }

    public static String getToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
        return prefs.getString(Constants.SHARED_PREFERENCES_TOKEN_NAME, "");

    }

    public static String getUserId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
        return prefs.getString(Constants.SHARED_PREFERENCES_USER_ID_NAME, "");

    }

    public static Gson getGson() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .create();
        return gson;
    }
    public static boolean isNewUser(Context context){

        SharedPreferences prefs = context.getSharedPreferences(Constants.SHARED_PREFERENCES_PLAYER_FILE_NAME, MODE_PRIVATE);
        return prefs.getBoolean(Constants.NEW_USER,true);
    }
    /** new user or not */
    public static void setSateOfUser(Context context,boolean isNewUser){

        SharedPreferences prefs = context.getSharedPreferences(Constants.SHARED_PREFERENCES_PLAYER_FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();


        prefsEditor.putBoolean(Constants.NEW_USER ,isNewUser);
        prefsEditor.apply();
    }

    public static void firstLaunch(Context context, boolean firstLaunch){

        SharedPreferences prefs = context.getSharedPreferences(Constants.SHARED_PREFERENCES_PLAYER_FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();

        prefsEditor.putBoolean(Constants.FIRST_LAUNCH ,firstLaunch);
        prefsEditor.apply();
    }

    public static boolean isFirstLaunch(Context context) {

        SharedPreferences prefs = context.getSharedPreferences(Constants.SHARED_PREFERENCES_PLAYER_FILE_NAME, MODE_PRIVATE);
        return prefs.getBoolean(Constants.FIRST_LAUNCH, true);
    }
    public static boolean isAutoPlayback(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
        if(!prefs.contains(Constants.SHARED_PREFERENCES_IS_AUTO_PLAY_NAME)){
            SharedPreferences.Editor prefsEditor = prefs.edit();
            prefsEditor.putBoolean(Constants.SHARED_PREFERENCES_IS_AUTO_PLAY_NAME,true);
            prefsEditor.commit();
        }

        return prefs.getBoolean(Constants.SHARED_PREFERENCES_IS_AUTO_PLAY_NAME,true);
    }

    public static void setIsAutoPlayback(Context context,boolean isAutoPlayback){
        SharedPreferences prefs = context.getSharedPreferences(Constants.SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putBoolean(Constants.SHARED_PREFERENCES_IS_AUTO_PLAY_NAME,isAutoPlayback);
        prefsEditor.commit();
    }

    public static boolean isNotificationAllowed(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
        if(!prefs.contains(Constants.SHARED_PREFERENCES_IS_NOTIFICATION_ALLOWED_NAME)){
            SharedPreferences.Editor prefsEditor = prefs.edit();
            prefsEditor.putBoolean(Constants.SHARED_PREFERENCES_IS_NOTIFICATION_ALLOWED_NAME,true);
            prefsEditor.commit();
        }


        return prefs.getBoolean(Constants.SHARED_PREFERENCES_IS_NOTIFICATION_ALLOWED_NAME,true);
    }

    public static void setIsNotificationAllowed(Context context,boolean isAllowed){
        SharedPreferences prefs = context.getSharedPreferences(Constants.SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putBoolean(Constants.SHARED_PREFERENCES_IS_NOTIFICATION_ALLOWED_NAME,isAllowed);
        prefsEditor.commit();
    }

    public static String getUserTypeForPremiumFeature(Context context, String defValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.SHARED_PREFERENCES_USER_TYPE, "");
    }

    public static void setUserTypeForPremiumFeature(Context context, String value) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.SHARED_PREFERENCES_USER_TYPE, value);
        prefsEditor.apply();
    }

    public static String convertImageToFullUrl(String imageUrl) {

        if (imageUrl == null)
            return null;

        if(imageUrl.contains("http"))
            return imageUrl;

        imageUrl = (Constants.IMAGES_BASE_URL + imageUrl);

        for (int i = 0; i < imageUrl.length(); i++) {
            if (imageUrl.charAt(i) == (char) 92) {
                // Log.e(TAG, "convertImageToFullUrl: " + imageUrl.charAt(i) + " at position: " + i);
                StringBuilder tempString = new StringBuilder(imageUrl);
                tempString.setCharAt(i, '/');
                imageUrl = tempString.toString();
            }
        }

        return imageUrl;
    }

    public static DownloadInfo getTrackDownloadInfo(String loggedInUserId, String trackId) {
        List<DownloadInfo> downloadInfoList = Pump.getDownloadListByTag(loggedInUserId);
        DownloadInfo trackDownloadInfo = null;
        for (DownloadInfo downloadInfo : downloadInfoList) {
            if (downloadInfo.getId().equals(trackId))
                trackDownloadInfo = downloadInfo;
        }

        return trackDownloadInfo;
    }

    public static RequestBuilder<? extends Drawable> glideBuilder(Context context, String imageUrl){
        return glideBuilder(context, imageUrl, new RequestListener() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        });
    }

    public static RequestBuilder<? extends Drawable> glideBuilder(Context context, String imageUrl, RequestListener listener) {
        if (imageUrl != null)
            if (imageUrl.contains(".svg")) {
                return GlideToVectorYou
                        .init()
                        .with(context)
                        .getRequestBuilder()
                        .load(imageUrl)
                        .addListener(new RequestListener<PictureDrawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<PictureDrawable> target, boolean isFirstResource) {
                                return listener.onLoadFailed(e, model, target, isFirstResource);
                            }

                            @Override
                            public boolean onResourceReady(PictureDrawable resource, Object model, Target<PictureDrawable> target, DataSource dataSource, boolean isFirstResource) {
                                return listener.onResourceReady(resource, model, target, dataSource, isFirstResource);
                            }
                        });
            }

        return Glide
                .with(context)
                //.as(PictureDrawable.class)
                .load(imageUrl)
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return listener.onLoadFailed(e, model, target, isFirstResource);
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return listener.onResourceReady(resource, model, target, dataSource, isFirstResource);
                    }
                });

    }

    public static OkHttpClient getIgnoreCertificateOkHttpClient() {
        if (OK_HTTP_CLIENT == null) {
            OkHttpClient.Builder builder = new OkHttpClient().newBuilder()
//                .cache(cache)
                    .followRedirects(true)
                    .retryOnConnectionFailure(true)
                    .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .connectTimeout(15, TimeUnit.SECONDS);

            try {
                // Create a trust manager that does not validate certificate chains
                final TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
                                    throws CertificateException {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
                                    throws CertificateException {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        }
                };

                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                // Create an ssl socket factory with our all-trusting manager
                final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
                builder.hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            OK_HTTP_CLIENT = builder.build();
        }

        return OK_HTTP_CLIENT;
    }

    public interface ServerSuccessResponseListener<T> {

        /**
         * Called when the response code is in the range [200, 300).
         * @param code
         * @param responseBody
         */
        void respondsWithSuccessCode(int code, T responseBody);

    }

    public interface ServerFailureResponseListener {
        /**
         * Called when the response code is NOT in the range [200, 300).
         * @param code
         * @param statusMessageResponse
         */
        void respondsWithFailureCode(int code, StatusMessageResponse statusMessageResponse);
    }
}