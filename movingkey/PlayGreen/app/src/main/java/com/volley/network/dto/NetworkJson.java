package com.volley.network.dto;

import java.io.Serializable;
import java.util.ArrayList;

public class NetworkJson implements Serializable {
	public String RESULT;
	public Integer RESULT_CODE;
	public String msg;

	public NetworkData DATA;
	public ArrayList<NetworkData> LIST;
	public NetworkData PAGING;

	public String TARGET_ID;
	public String LIKE_ID;


	public String MEMB_NAME;
	public String PG21_TITLE;
	public String EARTHBOX_IMG;
	public String TODAY_YN;
	public String POPUP_DEPT;
	public String SUPERGREENER_ENTER_ID;
	public String SUPERGREENER_ENTER_YN;
	public String ENTER_YN;
	public long START_DT;
	public long END_DT;
	public int ENTER_COUNT;
	public int SUPERGREENER_RANK;
	public int MEMB_POINT;
	public int TOTAL_COUNT;
	public int TIMELINE_COUNT;
	public int INFO_COUNT;

	/** google place api **/
	public ArrayList<NetworkData> html_attributions;
	public String next_page_token;
	public ArrayList<NetworkData> results;

	@Override
	public String toString() {
		return "NetworkResponse{" + "result='" + RESULT + '\'' + ", resultCode=" + RESULT_CODE + ", networkData=" + DATA + '}';
	}
}
