package tiltcode.movingkey.com.tiltcode_new.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gyul on 2016-06-22.
 */
public class User {

    @SerializedName("email")
    public String email;

    @SerializedName("username")
    public String username;

    @SerializedName("firstname")
    public String firstname;

    @SerializedName("lastname")
    public String lastname;

    @SerializedName("location")
    public String location;
}
