package com.example.oud.api;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OudApi {

    // Authentication

    @POST("/users/signUp")
    Call<LoginResponse> signup(@Body SignupUser signupUser);

    @POST("/users/login")
    Call<LoginResponse> login(@Body LoginUserInfo loginUserInfo);

    @POST("/users/forgotPassword")
    Call<StatusMessageResponse> forgetPasswordRequest(@Body ForgetPasswordRequestBody forgetPasswordRequestBody);

    @PATCH("/users/resetPassword/{token}")
    Call<LoginResponse> resetPassword(@Path("token") String token, @Body ResetPasswordBody resetPasswordBody);

    @PATCH("/users/verify/{token}")
    Call<LoginResponse> verifyEmail(@Path("token") String token);

    @POST("/auth/facebook")
    Call<LoginResponse> authenticateWithFacebook(@Body AccessToken accessToken);

    @POST("/auth/google")
    Call<LoginResponse> authenticateWithGoogle(@Body AccessToken accessToken);

    @GET("/me")
    Call<LoggedInUser> getUserProfile(@Header("AUTHORIZATIONS")String token);




    @GET("/me/player/recently-played")
    Call<RecentlyPlayedTracks> recentlyPlayedTracks(@Query("limit") Integer limit, @Query("after") Integer after, @Query("before") Integer before);

    @GET("/browse/categories")
    Call<OudList<Category>> listOfCategories(@Query("offset") Integer offset, @Query("limit") Integer limit);

    @GET("/browse/categories/{categoryId}")
    Call<Category> category(@Path("categoryId") String categoryId);

    @GET("/albums/{albumId}")
    Call<Album> album(@Path("albumId") String albumId);

    @GET("/playlists/{playlistId}")
    Call<Playlist> playlist(@Path("playlistId") String playlistId);

}
