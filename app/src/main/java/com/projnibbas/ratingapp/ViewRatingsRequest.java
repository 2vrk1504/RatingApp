package com.projnibbas.ratingapp;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ViewRatingsRequest extends StringRequest {
    private SessionManager sessionManager;
    public ViewRatingsRequest(Context context, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(Method.GET, NetworkingConstants.VIEW_REVIEWS_URL, listener, errorListener);
        sessionManager = new SessionManager(context);
        setRetryPolicy(new DefaultRetryPolicy(
                120000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        sessionManager.checkAndSetCookie(response.headers);
        return super.parseNetworkResponse(response);
    }

    @Override
    public Map<String, String> getHeaders(){
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Cookie", sessionManager.getCookie());
        return headers;
    }

}
