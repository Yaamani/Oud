package com.example.oud.api;

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
    Call<LoginResponse> signup(@Body SignupBody signupBody);

    @POST("/users/login")
    Call<LoginResponse> login(@Body LoginBody loginBody);

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


    // Home

    @GET("me/player/recently-played")
    Call<RecentlyPlayedTracks> recentlyPlayedTracks(@Query("limit") Integer limit, @Query("after") Integer after, @Query("before") Integer before);

    @GET("browse/categories")
    Call<ListOfCategories> listOfCategories(@Query("offset") Integer offset, @Query("limit") Integer limit);


}
