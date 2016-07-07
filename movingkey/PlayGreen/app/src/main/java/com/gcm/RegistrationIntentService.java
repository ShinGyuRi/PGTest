/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.gson.Gson;
import com.moyusoft.util.JYLog;
import com.moyusoft.util.TextUtil;
import com.navercorp.volleyextensions.volleyer.Volleyer;
import com.navercorp.volleyextensions.volleyer.builder.PostBuilder;
import com.navercorp.volleyextensions.volleyer.factory.DefaultRequestQueueFactory;
import com.volley.network.NetworkConstantUtil;
import com.volley.network.dto.NetworkJson;

import java.io.IOException;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.common.PlaygreenManager;


public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};

    public RegistrationIntentService() {
        super(TAG);
    }

    private String token;

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            JYLog.D("GCM Registration Token: " + token, new Throwable());
            sendRegistrationToServer(token);
        } catch (Exception e) {
            JYLog.E("Failed to complete token refresh " + e.getMessage(), new Throwable());
        }
        Intent registrationComplete = new Intent(GcmUtil.REGISTRATION_COMPLETE);
        intent.putExtra("token", token);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Persist registration to third-party servers.
     * <p/>
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        JYLog.D(TAG + "::sendRegistrationToServer", new Throwable());
        final RequestQueue q = DefaultRequestQueueFactory.create(this);
        q.start();
        PostBuilder pb = Volleyer.volleyer(q).post(NetworkConstantUtil.URLS.UPDATE_MEMBER)
                .addStringPart("AUTH_TOKEN", PlaygreenManager.getAuthToken())
                .addStringPart("PUSH_KEY", token);
        if (PlaygreenManager.getUserInfo() != null && !TextUtil.isNull(PlaygreenManager.getUserInfo().EMAIL))
            pb.addStringPart("EMAIL", PlaygreenManager.getUserInfo().EMAIL);
        pb.withListener(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JYLog.D(TAG + "::NETWORK", response.trim(), new Throwable());
                Gson gson = new Gson();
                NetworkJson networkJson = gson.fromJson(response, NetworkJson.class);
                q.stop();
            }
        }).withErrorListener(new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {
                JYLog.D(TAG + "::NETWORK", arg0.getMessage(), new Throwable());
                q.stop();
            }
        }).execute();
    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }

}
