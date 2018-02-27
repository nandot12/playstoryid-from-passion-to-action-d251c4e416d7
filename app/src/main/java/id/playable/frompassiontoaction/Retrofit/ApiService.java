package id.playable.frompassiontoaction.Retrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import id.playable.frompassiontoaction.Fragment.ResponsePremium;
import id.playable.frompassiontoaction.Model.ResponseComment;
import id.playable.frompassiontoaction.Model.ResponseInsert;
import id.playable.frompassiontoaction.Model.ResponseLike;


/**
 * Created by Playable on 10/13/2017.
 */

public interface ApiService {

    @FormUrlEncoded
    @POST("insert_comment")
    Call<ResponseInsert> action_insertcomment(
            @Field("name") String name ,
            @Field("text") String text,
            @Field("date") String date,

            @Field("image") String image,
            @Field("buku") String buku
    );


    @FormUrlEncoded
    @POST("update_like2")
    Call<ResponseInsert> update_like(
            @Field("like") String name ,
            @Field("dislike") String text
    );

    @FormUrlEncoded
    @POST("check_premium_code")
    Call<ResponsePremium> data_code(
            @Field("code") String name

    );


    @FormUrlEncoded
    @POST("data_comment")
    Call<ResponseComment> action_getcomment(
            @Field("buku") String buku


    );

    @GET("data_like3")
    Call<ResponseLike> action_like(

    );

}
