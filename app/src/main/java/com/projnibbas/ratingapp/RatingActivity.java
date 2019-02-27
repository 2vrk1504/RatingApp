package com.projnibbas.ratingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;

public class RatingActivity extends AppCompatActivity {

    private static final String CATEGORIES[] = {"Garbage", "Sewage", "Potholes"};
    private Spinner spinner;
    private EditText title;
    private EditText description;
    private RatingBar ratingBar;
    private ProgressDialog progressDialog;

    private Intent intentReceived;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        spinner = findViewById(R.id.spi_category);
        spinner.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_category_item, CATEGORIES));
        title = findViewById(R.id.et_title);
        ratingBar = findViewById(R.id.rating);
        description = findViewById(R.id.description);
        ratingBar.setRating(1);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        intentReceived = getIntent();
        findViewById(R.id.b_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> params = new HashMap<String, String>();
                int checker = 1;
                if(title.getText().toString().isEmpty()) {
                    checker = 0;
                    title.setError("Cannot be empty.");
                }
                if (checker == 1) {
                    progressDialog.show();
                    params.put("area", title.getText().toString());
                    params.put("comments", "<p>" + description.getText().toString() + "</p>");
                    params.put("category", CATEGORIES[spinner.getSelectedItemPosition()]);
                    params.put("latitude_list", Double.toString(intentReceived.getDoubleExtra("latitude", 0.0)));
                    params.put("longitude_list", Double.toString(intentReceived.getDoubleExtra("longitude", 0.0)));
                    params.put("rating", Float.toString(ratingBar.getRating()));

                    VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(new SubmitRatingRequest(
                            getApplicationContext(),
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressDialog.dismiss();
                                    if (response.contains("review updated")) {
                                        showToast("Review successfully submitted!");
                                        finish();
                                    } else
                                        showToast("An error1 occurred!");
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.dismiss();
                                    showToast("An error2 occurred!");
                                    Log.e("Vallabh", "LOL", error);
                                }
                            },
                            params
                    ));
                }
            }
        });
    }
    private void showToast(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
