package com.projnibbas.ratingapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {
    private Map<String, String> params;
    private SessionManager sessionManager;
    public LoginRequest(Context context, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener, HashMap<String, String> params) {
        super(Method.POST, NetworkingConstants.LOGIN_URL, listener, errorListener);
        this.params = params;
        sessionManager = new SessionManager(context);
        setRetryPolicy(new DefaultRetryPolicy(
                120000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    protected Map<String, String> getParams(){
        String s = "";
        for(String key : params.keySet())
            s += (key + ": " + params.get(key) + "\n");
        Log.d("Vallabh", "In getParams()\n" + s);
        return params;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        Log.d("Vallabh", "headers: " + response.headers);
        sessionManager.checkAndSetCookie(response.headers);
        return super.parseNetworkResponse(response);
    }

    @Override
    public Map<String, String> getHeaders(){
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        return headers;
    }
}

