package com.volley.network;

import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.moyusoft.util.JYLog;
import com.volley.network.dto.NetworkJson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by preparkha on 15. 6. 1..
 */
public class NetworkRequestUtil {

    private final String TAG = "NETWORK";

    private NetworkJson networkJson = null;
    private HashMap<String, String> requestParams = null;
    private NetworkListener networkListener = null;

    public NetworkRequestUtil(NetworkListener listener) {
        this.networkListener = listener;
    }

    /**
     * to request url that is using GET Method
     *
     * @param requestParams
     * @return
     */
    public String createParams(HashMap<String, String> requestParams) {

        if (requestParams == null)
            return "";

        int index = 0;
        String params = null;
        StringBuilder builder = new StringBuilder();

        String key = null;
        String value = null;
        for (Map.Entry<String, String> entry : requestParams.entrySet()) {
            key = null;
            value = null;

            key = entry.getKey();
            value = entry.getValue();

            if (index == 0)
                builder.append("?");
            else
                builder.append("&");

            JYLog.D(TAG, "key::" + key + "======" + "value::" + value, new Throwable());
            builder.append(key);
            builder.append("=");
            builder.append(value);
            index++;

        }
        params = builder.toString();
        builder.delete(0, builder.capacity());
        builder = null;
        return params;
    }

    /**
     * @param api_idx
     * @param url
     * @param params
     * @return
     */
    public StringRequest requestGet(final int api_idx, String url, HashMap<String, String> params) {

        StringRequest stringRequest = null;
        this.requestParams = params;

        if (params != null) {
            url += createParams(this.requestParams);
        }

        if (networkListener != null) {
            networkListener.onNetworkStart(api_idx);
        }
        JYLog.D(TAG, "URL=====> " + url, null);

        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JYLog.D(TAG, "Response=====> " + response, null);

                try {
                    Gson gson = new Gson();
                    networkJson = gson.fromJson(response, NetworkJson.class);
                    if (networkListener != null) {
                        if (NetworkErrorUtill.isJsonResultCheck(networkJson))
                            networkListener.onNetworkResult(api_idx, networkJson);
                        else {
                            networkListener.onNetworkError(api_idx, null, networkJson);
                        }

                    }
                } catch (JsonSyntaxException e) {
                    JYLog.E("response>>>>>>>>>>>>>>>>>>> " + response, new Throwable());
                    JYLog.E(e.toString(), new Throwable());
                    if (networkListener != null) {
                        networkListener.onNetworkError(api_idx, new VolleyError("ERROR_RESULT_FORM"), null);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    //  Toast.makeText(BaseApplication.getContext(), R.string.str_error_network_timeout, Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO
                } else if (error instanceof NetworkError) {
                    //TODO
                } else if (error instanceof ParseError) {
                    //TODO
                }

                if (networkListener != null)
                    networkListener.onNetworkError(api_idx, error, null);
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        return stringRequest;
    }


    /**
     * @param api_idx
     * @param url
     * @param params
     * @return
     */
    public StringRequest requestPost(final int api_idx, String url, HashMap<String, String> params) {

        StringRequest stringRequest = null;
        this.requestParams = params;

        if (networkListener != null) networkListener.onNetworkStart(api_idx);
        JYLog.D(TAG, "URL=====> " + url, null);
        createParams(requestParams);

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JYLog.D(TAG, "Response=====> " + response.trim(), new Throwable());
                if (response.contains("DOCTYPE") || response.contains("doctype")) {
                    if (networkListener != null) {
                        networkListener.onNetworkError(api_idx, new VolleyError("ERROR_RESULT_FORM"), null);
                    }
                    return;
                }
                try {
                    Gson gson = new Gson();
                    networkJson = gson.fromJson(response, NetworkJson.class);
                    if (networkListener != null) {
                        if (NetworkErrorUtill.isJsonResultCheck(networkJson)) {
                            networkListener.onNetworkResult(api_idx, networkJson);
                        } else {
                            networkListener.onNetworkError(api_idx, null, networkJson);
                        }
                    }
                } catch (Exception e) {
                    JYLog.E(TAG, e.toString(), new Throwable());
                    //TODO 리턴 형식이 안맞을때 에러처리
                    if (networkListener != null) {
                        networkListener.onNetworkError(api_idx, new VolleyError("ERROR_RESULT_FORM"), null);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    //     Toast.makeText(BaseApplication.getContext(), R.string.str_error_network_timeout, Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO
                } else if (error instanceof NetworkError) {
                    //TODO
                } else if (error instanceof ParseError) {
                    //TODO
                }

                if (networkListener != null)
                    networkListener.onNetworkError(api_idx, error, null);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> tempParams = requestParams;
                return tempParams;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> tempParams = new HashMap<String, String>();
                tempParams.put("Content-Type", "application/x-www-form-urlencoded");
                return tempParams;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        return stringRequest;
    }

}
