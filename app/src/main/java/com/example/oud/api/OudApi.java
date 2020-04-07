package com.example.oud.api;

import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OudApi {

    // Authentication

    @POST("users/signUp")
    Call<LoginResponse> signup(@Body SignupUser signupUser);

    @POST("users/login")
    Call<LoginResponse> login(@Body LoginUserInfo loginUserInfo);

    @POST("users/forgotPassword")
    Call<StatusMessageResponse> forgetPasswordRequest(@Body ForgetPasswordRequestBody forgetPasswordRequestBody);

    @PATCH("users/resetPassword/{token}")
    Call<LoginResponse> resetPassword(@Path("token") String token, @Body ResetPasswordBody resetPasswordBody);

    @PATCH("users/verify/{token}")
    Call<LoginResponse> verifyEmail(@Path("token") String token);

    @POST("auth/facebook")
    Call<ResponseBody> authenticateWithFacebook(@Body AccessToken accessToken);

    @POST("auth/google")
    Call<ResponseBody> authenticateWithGoogle(@Body AccessToken accessToken);

    @GET("me")
    Call<LoggedInUser> getUserProfile(@Header("AUTHORIZATIONS")String token);

    @PATCH("me/auth/facebook")
    Call<LoginResponse> getLoginResponseFromFacebookAccessToken(@Body AccessToken accessToken);

    @PATCH("me/auth/google")
    Call<LoginResponse> getLoginResponseFromGoogleAccessToken(@Body AccessToken accessToken);

    @GET("users/{user_id}/playlists")
    Call<UserPlaylistsResponse> getUserPlaylists(@Path("user_id") String userId);

    @GET("users/{user_id}/playlists")
    Call<UserPlaylistsResponse> getMoreUserPlaylists(@Path("user_id") String userId,@Query("offset") int offset);


    @GET("users/{user_id}")
    Call<ProfilePreview> getUserById(@Path("user_id") String userId);


    @Multipart
    @PATCH("me/profilePicure")
    Call<LoggedInUser> updateUserPicture(@Header("AUTHORIZATIONS") String token,@Part MultipartBody.Part image);

    @GET("users/{user_id}/followers")//todo add when back end finished
    Call<FollowingOrFollowersResponse> getFollowers(@Path("user_id")String userId,@Query("type") String type,@Query("offset") int offset);

    @GET("users/{user_id}/following")//todo add when back end finished
    Call<FollowingOrFollowersResponse> getFollowing(@Path("user_id")String userId,@Query("type") String type,@Query("offset") int offset);

    @PATCH("me/update/display")//todo add when back end finished
    Call<LoggedInUser> updateDisplayName(@Header("AUTHORIZATIONS") String token,@Body String displayName);




    // Home

    @Deprecated
    @GET("me/player/recently-played")
    Call<RecentlyPlayedTracks> recentlyPlayedTracks(@Header("AUTHORIZATIONS") String token, @Query("limit") Integer limit, @Query("after") Integer after, @Query("before") Integer before);

    @GET("me/player/recently-played")
    Call<RecentlyPlayedTracks2> recentlyPlayedTracks2(@Header("AUTHORIZATIONS") String token, @Query("limit") Integer limit, @Query("after") Integer after, @Query("before") Integer before);

    @GET("browse/categories")
    Call<OudList<Category>> listOfCategories(@Query("offset") Integer offset, @Query("limit") Integer limit);

    @GET("browse/categories/{categoryId}")
    Call<Category> category(@Header("AUTHORIZATIONS") String token, @Path("categoryId") String categoryId);

    @GET("browse/categories")
    Call<OudList<Category2>> listOfCategories2(@Query("offset") Integer offset, @Query("limit") Integer limit);

    @GET("browse/categories/{categoryId}")
    Call<Category2> category2(@Header("AUTHORIZATIONS") String token, @Path("categoryId") String categoryId);

    @GET("browse/categories/{categoryId}/playlists")
    Call<OudList<Playlist>> categoryPlaylist(@Header("AUTHORIZATIONS") String token, @Path("categoryId") String categoryId, @Query("offset") Integer offset, @Query("limit") Integer limit);

    @GET("albums/{albumId}")
    Call<Album> album(@Header("AUTHORIZATIONS") String token, @Path("albumId") String albumId);

    @GET("playlists/{playlistId}")
    Call<Playlist> playlist(@Header("AUTHORIZATIONS") String token, @Path("playlistId") String playlistId);

    /*@HTTP(method = "DELETE", path = "playlists/{playlistId}", hasBody = true)
    Call<ResponseBody> removeTracksFromPlaylist(@Path("playlistId") String playlistId, @Body ArrayList<String> ids);*/

    @PUT("playlists/{playlistId}")
    //@PATCH("playlists/{playlistId}")
    Call<ResponseBody> reorderPlaylistTracks(@Header("AUTHORIZATIONS") String token, @Path("playlistId") String playlistId, @Body ReorderPlaylistPayload reorderPlaylistPayload);

    @PUT("playlists/{playlistId}")
    Call<ResponseBody> changePlaylistDetails(@Header("AUTHORIZATIONS") String token, @Path("playlistId") String playlistId, @Body ChangePlaylistDetailsPayload changePlaylistDetailsPayload);

    @GET("artists/{artistId}")
    Call<Artist> artist(@Header("AUTHORIZATIONS") String token, @Path("artistId") String artistId);

    @GET("artists/{artistId}/albums")
    Call<OudList<Album>> artistAlbums(@Header("AUTHORIZATIONS") String token, @Path("artistId") String artistId, @Query("offset") Integer offset, @Query("limit") Integer limit);

    @GET("artists/{artistId}/related-artists")
    Call<RelatedArtists> similarArtists(@Header("AUTHORIZATIONS") String token, @Path("artistId") String artistId);
}
