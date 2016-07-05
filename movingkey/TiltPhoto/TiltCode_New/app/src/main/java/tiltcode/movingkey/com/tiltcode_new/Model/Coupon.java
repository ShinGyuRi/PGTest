package tiltcode.movingkey.com.tiltcode_new.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gyul on 2016-06-28.
 */
public class Coupon {

    @SerializedName("_id")
    public String _id;

    @SerializedName("title")
    public String title;

    @SerializedName("description")
    public String description;

    @SerializedName("option")
    public String option;

    @SerializedName("exp")
    public String exp;

    @SerializedName("image")
    public String image;

    @SerializedName("brandimage")
    public String brandimage;

    public String get_id() {
        return _id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getOption() {
        return option;
    }

    public String getExp() {
        return exp;
    }

    public String getImage() {
        return image;
    }

    public String getBrandimage() {
        return brandimage;
    }
}
