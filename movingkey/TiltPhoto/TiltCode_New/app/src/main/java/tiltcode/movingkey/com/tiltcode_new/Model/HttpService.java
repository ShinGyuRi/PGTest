package tiltcode.movingkey.com.tiltcode_new.Model;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Gyul on 2016-06-20.
 */
public interface HttpService {

    @FormUrlEncoded
    @POST("/users/signIn")
    void signIn(@Field("username") String username,
                @Field("pw") String pw,
                Callback<SignInResult> ret);

    @FormUrlEncoded
    @POST("/users/enterUser")
    void enterUser(@Field("udid") String udid,
                   @Field("model_name") String model_name,
                   @Field("push_token") String push_token,
                   @Field("language") String language,
                   @Field("latitude") String latitude,
                   @Field("longitude") String longitude,
                   Callback<SignInResult> ret);

    @FormUrlEncoded
    @POST("/users/signUp")
    void signUp(@Field("latitude") String latitude,
                @Field("longitude") String longitude,
                @Field("image") byte[] image,
                @Field("gender") String gender,
                @Field("dob") String dob,
                @Field("snsID") String snsID,
                @Field("snsType") String snsType,
                @Field("email") String email,
                @Field("username") String username,
                @Field("firstname") String firstname,
                @Field("lastname") String lastname,
                @Field("pw") String pw,
                Callback<SignUpResult> ret);

    @FormUrlEncoded
    @POST("/getCouponOrPhoto")
    void getCouponOrPhoto(@Field("username") String username,
                          @Field("loginType") String loginType,
                          @Field("degree") String degree,
                          @Field("latitude") String latitude,
                          @Field("longitude") String longitude,
                          @Field("option") String option,
                          Callback<CouponPhotoResult> ret);
}
