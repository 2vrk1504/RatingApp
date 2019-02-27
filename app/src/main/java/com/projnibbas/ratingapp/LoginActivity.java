package com.projnibbas.ratingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText et_username;
    private EditText et_password;
    private EditText et_name;
    private EditText et_email;
    private EditText et_confirm_password;
    private Button b_login;
    private TextView tv_toggle;
    private TextView tv_some_text;
    private ProgressDialog progressDialog;
    private int state; //1: Login, 0: Sign Up

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        state = 1;
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_confirm_password = findViewById(R.id.et_confirm_password);
        b_login = findViewById(R.id.b_login);
        tv_toggle = findViewById(R.id.tv_sign_up);
        tv_some_text = findViewById(R.id.tv_some_text);
        tv_toggle.setOnClickListener(toggleListener);
        b_login.setOnClickListener(buttonListener);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
    }

    View.OnClickListener toggleListener =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(state == 1) {
                state = 0;
                tv_toggle.setText("Sign In");
                tv_some_text.setText("Already a user?");
                b_login.setText("Sign Up");
                et_name.setVisibility(View.VISIBLE);
                et_email.setVisibility(View.VISIBLE);
                et_confirm_password.setVisibility(View.VISIBLE);
            }
            else{
                state = 1;
                tv_toggle.setText("Sign Up");
                tv_some_text.setText("Not registered?");
                b_login.setText("Sign In");
                et_name.setVisibility(View.GONE);
                et_email.setVisibility(View.GONE);
                et_confirm_password.setVisibility(View.GONE);
            }
        }
    };

    View.OnClickListener buttonListener =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            b_login.setEnabled(false);
            Response.Listener<String> listener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    b_login.setEnabled(true);
                    if(response.contains("TypeError"))
                        showToast("Username does not exist!");
                    else if(response.contains("Invalid login"))
                        showToast("Wrong Password!");
                    else{
                        Log.d("Vallabh", "Successful login");
                        Log.d("Vallabh", response);
                        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    b_login.setEnabled(true);
                    Log.e("Vallabh", "lol", error);
                    showToast("An unexpected error occurred!");
                }
            };
            if(state == 1) {
                //Make Login Request
                progressDialog.show();
                Log.d("Vallabh", "In sign in");
                HashMap<String, String> params = new HashMap<>();
                String s = "";
                params.put("username", et_username.getText().toString().trim());
                params.put("password", et_password.getText().toString().trim());
                for(String key : params.keySet())
                    s += (key + ": " + params.get(key) + "\n");
                Log.d("Vallabh", "req: " + s);
                LoginRequest loginRequest = new LoginRequest(getApplicationContext(), listener, errorListener, params);
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(loginRequest);
            }
            else{
                //Make Sign up Request
                progressDialog.show();
                Log.d("Vallabh", "In sign up");
                HashMap<String, String> params = new HashMap<>();
                params.put("username", et_username.getText().toString());
                params.put("password", et_password.getText().toString());
                params.put("confirm", et_confirm_password.getText().toString());
                params.put("name", et_name.getText().toString());
                params.put("email", et_email.getText().toString());
                SignUpRequest signUpRequest = new SignUpRequest(getApplicationContext(), listener, errorListener, params);
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(signUpRequest);
            }
        }
    };


    private void showToast(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
