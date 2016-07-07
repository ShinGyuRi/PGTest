package com.volley.network;

import com.volley.network.NetworkConstantUtil.NETWORK_RESULT_CODE;
import com.volley.network.dto.NetworkJson;

/**
 * Created by preparkha on 15. 6. 1..
 */
public class NetworkErrorUtill {

    public static final String KEY_VOLLEY_ERROR = "VE";

    public static final int IDX_VOLLEY_ERROR = 0;
    public static final int IDX_CONTEXT_IS_NULL = 1;

    public static String getErrorMessage(int idx) {

        String errorMessage = null;

        switch (idx) {
            case IDX_CONTEXT_IS_NULL:
                errorMessage = "context is null";
                break;
            default:
                break;
        }

        return errorMessage;
    }

    public static boolean isJsonResultCheck(NetworkJson json) {
        if (isJsonCheck(json) && json.RESULT_CODE != null) {
            if (json.RESULT_CODE == NETWORK_RESULT_CODE.RESULT_OK)
                return true;
        }
        return false;
    }

    public static boolean isJsonCheck(NetworkJson json) {
        boolean isJsonCheck = true;
        if (json == null) {
            isJsonCheck = false;
        }
        return isJsonCheck;
    }

}
