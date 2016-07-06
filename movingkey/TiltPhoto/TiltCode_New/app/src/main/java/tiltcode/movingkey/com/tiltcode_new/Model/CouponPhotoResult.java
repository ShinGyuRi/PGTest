package tiltcode.movingkey.com.tiltcode_new.Model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Gyul on 2016-06-20.
 */
public class CouponPhotoResult {

    @SerializedName("coupon")
    public Object coupon;

    @SerializedName("photo")
    public Object photo;

    @SerializedName("result")
    public String result;

    @SerializedName("message")
    public String message;

    public Object getCoupon() {
        Gson gson = new Gson();

        return gson.toJson(coupon);
    }
}
