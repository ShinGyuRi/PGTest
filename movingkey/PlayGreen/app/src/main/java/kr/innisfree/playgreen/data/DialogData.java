package kr.innisfree.playgreen.data;

import com.volley.network.dto.NetworkData;

import java.io.Serializable;

public class DialogData implements Serializable {
    public int dialogType;
    public int dialogButtonType;
    public int dialogSelectPosition;
    public String dialogMessage;
    public NetworkData dialogData;

    public int voteNumber;
    public String voteUserChoice;

    public int typeTheme = -1;
    public int typeGender = -1;
    public int typeOrder = -1;
    public int typeSee = -1;
    public int typeArea = -1;
    public int typeAreaRange = -1;

    public int productId;
    public String content;
    public int rating;

    public boolean isCancle;
}
