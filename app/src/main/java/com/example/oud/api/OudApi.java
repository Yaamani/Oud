package com.example.oud.api;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;

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
    Call<LoggedInUser> getUserProfile(@Header("AUTHORIZATION")String token);

    @PATCH("me/auth/facebook")
    Call<LoginResponse> getLoginResponseFromFacebookAccessToken(@Body AccessToken accessToken);

    @PATCH("me/auth/google")
    Call<LoginResponse> getLoginResponseFromGoogleAccessToken(@Body AccessToken accessToken);

    @GET("users/{user_id}/playlists")
    Call<UserPlaylistsResponse> getUserPlaylists(@Path("user_id") String userId);

    @GET("users/{user_id}/playlists")
    Call<UserPlaylistsResponse> getMoreUserPlaylists(@Path("user_id") String userId,@Query("offset") int offset);


    @GET("users/{user_id}")
    Call<ProfilePreview> getUserById(@Header("AUTHORIZATION") String token,@Path("user_id") String userId);


    @Multipart
    @PATCH("me/profilePicture")
    Call<LoggedInUser> updateUserPicture(@Header("AUTHORIZATION") String token,@Part MultipartBody.Part images);

    @GET("users/{user_id}/followers")//todo add when back end finished
    Call<FollowingOrFollowersResponse> getFollowers(@Path("user_id")String userId,@Query("type") String type,@Query("offset") int offset);

    @GET("users/{user_id}/following")//todo add when back end finished
    Call<FollowingOrFollowersResponse> getFollowing(@Path("user_id")String userId,@Query("type") String type,@Query("offset") int offset);

    @PATCH("me/update/display")//todo add when back end finished
    Call<LoggedInUser> updateDisplayName(@Header("AUTHORIZATION") String token,@Body String displayName);





    // Home

    @Deprecated
    @GET("me/player/recently-played")
    Call<RecentlyPlayedTracks> recentlyPlayedTracks(@Header("AUTHORIZATION") String token, @Query("limit") Integer limit, @Query("after") Integer after, @Query("before") Integer before);

    @GET("me/player/recently-played")
    Call<RecentlyPlayedTracks2> recentlyPlayedTracks2(@Header("AUTHORIZATION") String token, @Query("limit") Integer limit, @Query("after") Integer after, @Query("before") Integer before);

    @GET("browse/categories")
    Call<OudList<Category>> listOfCategories(@Query("offset") Integer offset, @Query("limit") Integer limit);

    @GET("browse/categories/{categoryId}")
    Call<Category> category(@Header("AUTHORIZATION") String token, @Path("categoryId") String categoryId);

    @GET("browse/categories")
    Call<OudList<Category2>> listOfCategories2(@Query("offset") Integer offset, @Query("limit") Integer limit);

    @GET("browse/categories/{categoryId}")
    Call<Category2> category2(@Header("AUTHORIZATION") String token, @Path("categoryId") String categoryId);

    @GET("browse/categories/{categoryId}/playlists")
    Call<OudList<Playlist>> categoryPlaylists(@Header("AUTHORIZATION") String token, @Path("categoryId") String categoryId, @Query("offset") Integer offset, @Query("limit") Integer limit);

    @GET("albums/{albumId}")
    Call<Album> album(@Header("AUTHORIZATION") String token, @Path("albumId") String albumId);

    @GET("playlists/{playlistId}")
    Call<Playlist> playlist(@Header("AUTHORIZATION") String token, @Path("playlistId") String playlistId);

    @PUT("playlists/{playlistId}")
    //@PATCH("playlists/{playlistId}")
    Call<ResponseBody> reorderPlaylistTracks(@Header("AUTHORIZATION") String token, @Path("playlistId") String playlistId, @Body ReorderPlaylistPayload reorderPlaylistPayload);

    @PUT("playlists/{playlistId}")
    Call<ResponseBody> changePlaylistDetails(@Header("AUTHORIZATION") String token, @Path("playlistId") String playlistId, @Body ChangePlaylistDetailsPayload changePlaylistDetailsPayload);

    //@DELETE("playlists/{playlistId}/tracks")
    @HTTP(method = "DELETE", path = "playlists/{playlistId}/tracks", hasBody = true)
    Call<ResponseBody> removePlaylistTracks(@Header("AUTHORIZATION") String token, @Path("playlistId") String playlistId, @Body RemovePlaylistTracksPayload removePlaylistTracksPayload);

    @GET("me/tracks/contains")
    Call<IsFoundResponse> getAreTheseTracksLiked(@Header("AUTHORIZATION") String token, @Query(value = "ids", encoded = true) String ids);

    @PUT("me/tracks")
    Call<ResponseBody> addTheseTracksToLikedTracks(@Header("AUTHORIZATION") String token, @Query(value = "ids", encoded = true) String ids);

    @HTTP(method = "DELETE", path = "me/tracks", hasBody = true)
    Call<ResponseBody> removeTheseTracksFromLikedTracks(@Header("AUTHORIZATION") String token, @Query(value = "ids", encoded = true) String ids);


    @GET("playlists/{playlistId}/followers/contains")
    Call<ArrayList<Boolean>> checkIfUsersFollowPlaylist(@Header("AUTHORIZATION") String token, @Path("playlistId") String playlistId, @Query(value = "ids", encoded = true) String userIds);

    @PUT("playlists/{playlistId}/followers")
    Call<ResponseBody> followPlaylist(@Header("AUTHORIZATION") String token, @Path("playlistId") String playlistId, @Body FollowingPublicityPayload followingPublicityPayload);

    @DELETE("playlists/{playlistId}/followers")
    Call<ResponseBody> unfollowPlaylist(@Header("AUTHORIZATION") String token, @Path("playlistId") String playlistId);

    @GET("me/albums/contains")
    Call<IsFoundResponse> checkIfTheseAlbumsAreSavedByUser(@Header("AUTHORIZATION") String token, @Query(value = "ids", encoded = true) String albumIds);

    @PUT("me/albums")
    Call<ResponseBody> saveTheseAlbumsForTheCurrentUser(@Header("AUTHORIZATION") String token, @Query(value = "ids", encoded = true) String albumIds);

    @DELETE("me/albums")
    Call<ResponseBody> unsaveTheseAlbumsForTheCurrentUser(@Header("AUTHORIZATION") String token, @Query(value = "ids", encoded = true) String albumIds);

    @GET("artists/{artistId}")
    Call<Artist> artist(@Header("AUTHORIZATION") String token, @Path("artistId") String artistId);

    @GET("artists/{artistId}/albums")
    Call<OudList<Album>> artistAlbums(@Header("AUTHORIZATION") String token, @Path("artistId") String artistId, @Query("offset") Integer offset, @Query("limit") Integer limit);

    @GET("artists/{artistId}/related-artists")
    Call<RelatedArtists> similarArtists(@Header("AUTHORIZATION") String token, @Path("artistId") String artistId);



    @PUT("me/following")
    Call<Void> followUsersOrArtists (@Header("AUTHORIZATION") String token, @Query("type") String type,@Body ListOfIds listOfIds);

    @DELETE("me/following")
    Call<Void> unFollowUsersOrArtists (@Header("AUTHORIZATION") String token, @Query("type") String type,@Query("ids")String commaSeparatedUserId);

    @GET("me/following/contains")
    Call<ListOfBoolean> checkIfIFollowTheseUsersOrArtists(@Header("AUTHORIZATION") String token, @Query("type") String type, @Query("ids") String ids);

    @GET("playlists/{playlistId}/followers/contains")
    Call<ListOfBoolean> checkIfIFollowThisPlaylist(@Header("AUTHORIZATION") String token,@Path("playlistId") String playlistId,@Query("ids") String ids);

    @PUT("playlists/{playlistId}/followers")
    Call<Void> followPlaylist(@Header("AUTHORIZATION") String token,@Path("playlistId") String playlistId,@Body publicPlaylistfollow playlistfollow);

    @DELETE("playlists/{playlistId}/followers")
    Call<Void> unFollowPlaylist(@Header("AUTHORIZATION") String token,@Path("playlistId") String playlistId);



    @PUT("me/profile")
    Call<ResponseBody> updateProfile(@Header("AUTHORIZATION") String token,@Body UpdateProfileData updateProfileData);

}
