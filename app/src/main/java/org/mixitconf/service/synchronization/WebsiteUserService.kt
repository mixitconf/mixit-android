package org.mixitconf.service.synchronization

import org.mixitconf.service.synchronization.dto.FavoriteDto
import org.mixitconf.service.synchronization.dto.SpeakerApiDto
import org.mixitconf.service.synchronization.dto.WebsiteResponseDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface WebsiteUserService {

    /**
     * Send a request to send a token to a user by email. When a user want to refresh his data this token is required. This request can respond with
     * <ul>
     *     <li>200 : {"message":"A token was send by email. Please check your mailbox and send it in the future request"}</li>
     *     <li>400 : {"message":"This email is not known. You have to create an account on our website if you want to use this functionnality"}</li>
     *     <li>400 : {"message":"Credentials are invalids"}</li>
     *     <li>500 : {"message":"An expected error occured on email sent"}</li>
     * </ul>
     */
    @GET("external/token")
    fun askForToken(@Query("email") email: String): Call<WebsiteResponseDto>

    /**
     * Check if a token is valid (used before a askForToken)
     * <ul>
     *     <li>200 : {"message":"Credentials are valids"}</li>
     *     <li>400 : {"message":"Credentials are invalids"}</li>
     * </ul>
     */
    @GET("token/check")
    fun checkToken(@Query("email") email: String, @Query("token") token: String): Call<WebsiteResponseDto>


    /**
     * Read user profile
     * <ul>
     *     <li>200 : a user</li>
     *     <li>400 : {"message":"Credentials are invalids"}</li>
     * </ul>
     */
    @GET("me")
    fun profile(@Query("email") email: String, @Query("token") token: String): Call<SpeakerApiDto>


    /**
     * Read user favorites
     * <ul>
     *     <li>200 : favorites</li>
     *     <li>400 : {"message":"Credentials are invalids"}</li>
     * </ul>
     */
    @GET("favorites")
    fun favorites(@Query("email") email: String, @Query("token") token: String): Call<List<FavoriteDto>>


    /**
     * Read if a talk is a favorite
     * <ul>
     *     <li>200 : favorite</li>
     *     <li>400 : {"message":"Credentials are invalids"}</li>
     * </ul>
     */
    @GET("favorites/talks/{id}")
    fun favorite(@Query("id") talkId: String, @Query("email") email: String, @Query("token") token: String): Call<FavoriteDto>

    /**
     * Toggle a favorite
     * <ul>
     *     <li>200 : favorite</li>
     *     <li>400 : {"message":"Credentials are invalids"}</li>
     * </ul>
     */
    @POST("favorites/talks/{id}")
    fun toggleFavorite(@Query("id") talkId: String, @Query("email") email: String, @Query("token") token: String): Call<FavoriteDto>
}