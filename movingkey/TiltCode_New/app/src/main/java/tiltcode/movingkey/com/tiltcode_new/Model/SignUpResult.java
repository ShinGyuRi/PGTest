package tiltcode.movingkey.com.tiltcode_new.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gyul on 2016-06-22.
 */
public class SignUpResult {

    @SerializedName("result")
    public String result;

    @SerializedName("message")
    public String message;

    @SerializedName("user")
    public User user;

}
