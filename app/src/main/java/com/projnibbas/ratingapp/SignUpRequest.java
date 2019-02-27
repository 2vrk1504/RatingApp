package com.projnibbas.ratingapp;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SignUpRequest extends StringRequest {
    private Map<String, String> params;
    private SessionManager sessionManager;
    public SignUpRequest(Context context, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener, HashMap<String, String> params) {
        super(Method.POST, NetworkingConstants.REGISTER_URL, listener, errorListener);
        this.params = params;
        sessionManager = new SessionManager(context);
        setRetryPolicy(new DefaultRetryPolicy(
                120000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    protected Map<String, String> getParams(){
        return params;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        sessionManager.checkAndSetCookie(response.headers);
        return super.parseNetworkResponse(response);
    }

    @Override
    public Map<String, String> getHeaders(){
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        return headers;
    }

}
