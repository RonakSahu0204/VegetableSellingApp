package com.example.vegetablessell.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.vegetablessell.R;
import com.example.vegetablessell.utils.VegetablesConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ForgetPasswordActivity extends AppCompatActivity {

    private EditText mEmail;
    private Button mSendOtpBtn, mVerifyOtpBtn, mUpdatePass;
    private EditText mOtp, mPass, mConfPass;

    private RelativeLayout mOtpLayout, mChangePassLayout;

    private RequestQueue requestQueue;

    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        mEmail = findViewById(R.id.forgot_password_email);
        mSendOtpBtn = findViewById(R.id.sendOtp);
        mPass = findViewById(R.id.passChange);
        mConfPass = findViewById(R.id.changeConfPass);

        mUpdatePass = findViewById(R.id.updatePass);

        mOtpLayout = findViewById(R.id.otpLayout);
        mChangePassLayout = findViewById(R.id.changePasswordLayout);

        requestQueue = Volley.newRequestQueue(this);

        mVerifyOtpBtn = findViewById(R.id.verifyOtp);

        mOtp = findViewById(R.id.Otp);

        mSendOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmail.getText().toString();

                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        sendOtpReq(email);
                        userEmail = email;
                    }
                });
            }
        });

        mVerifyOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString();
                String otp = mOtp.getText().toString();

                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        verifyOtpReq(email, otp);
                        userEmail = email;
                    }
                });
            }
        });

        mUpdatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = mPass.getText().toString();
                String confirmPass = mConfPass.getText().toString();

                if(!password.trim().equals(confirmPass.trim())) {
                    Toast.makeText(ForgetPasswordActivity.this, "confirm password and password should be same", Toast.LENGTH_SHORT).show();
                }

                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        updatePassReq(userEmail, password, confirmPass);
                    }
                });
            }
        });

    }

    private void updatePassReq(String userEmail, String password, String confirmPass) {

        String URL = VegetablesConstant.BASE_URL+"/auth/api/changePassword";

        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String message = null;
                        try {
                            message = response.getString("status");
                            if("PASSWORDD SUCCESSFULLY CHANGED".equals(message)) {
                               Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                               intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                               startActivity(intent);

                            }
                            Toast.makeText(ForgetPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            public byte[] getBody() {
                HashMap<String, String> params = new HashMap<>();
                params.put("email", userEmail);
                params.put("password", password);
                params.put("confirmPassword", confirmPass);

                return new JSONObject(params).toString().getBytes();
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                int statusCode = response.statusCode;
                if(statusCode != 200) {
                    Toast.makeText(ForgetPasswordActivity.this, "Server Error !!!", Toast.LENGTH_SHORT).show();
                }
                return super.parseNetworkResponse(response);
            }
        };

        loginRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        requestQueue.add(loginRequest);

    }

    private void verifyOtpReq(String email, String otp) {

        String URL = VegetablesConstant.BASE_URL+"/auth/api/verifyOtp";

        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String message = null;
                        try {
                            message = response.getString("status");
                            if("OTP VERIFIED".equals(message)) {
                                mOtp.setEnabled(false);
                                mSendOtpBtn.setVisibility(View.GONE);
                                mVerifyOtpBtn.setVisibility(View.GONE);
                                mEmail.setEnabled(false);
                                mOtpLayout.setVisibility(View.GONE);
                                mChangePassLayout.setVisibility(View.VISIBLE);

                            }
                            Toast.makeText(ForgetPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            public byte[] getBody() {
                HashMap<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("otp", otp);

                return new JSONObject(params).toString().getBytes();
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                int statusCode = response.statusCode;
                if(statusCode != 200) {
                    Toast.makeText(ForgetPasswordActivity.this, "Server Error !!!", Toast.LENGTH_SHORT).show();
                }
                return super.parseNetworkResponse(response);
            }
        };

        loginRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        requestQueue.add(loginRequest);


    }

    private void sendOtpReq(String email) {

        String URL = VegetablesConstant.BASE_URL+"/auth/api/sendOtp";

        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String message = null;
                        try {
                            message = response.getString("status");
                            if("OTP SENT TO YOUR EMAIL".equals(message)) {
                                mOtp.setEnabled(true);
                                mSendOtpBtn.setVisibility(View.GONE);
                                mVerifyOtpBtn.setVisibility(View.VISIBLE);
                                mEmail.setEnabled(false);

                            }
                            Toast.makeText(ForgetPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            public byte[] getBody() {
                HashMap<String, String> params = new HashMap<>();
                params.put("email", email);

                return new JSONObject(params).toString().getBytes();
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
               int statusCode = response.statusCode;
               if(statusCode != 200) {
                   Toast.makeText(ForgetPasswordActivity.this, "Server Error !!!", Toast.LENGTH_SHORT).show();
               }
                return super.parseNetworkResponse(response);
            }
        };

        loginRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        requestQueue.add(loginRequest);

    }
}