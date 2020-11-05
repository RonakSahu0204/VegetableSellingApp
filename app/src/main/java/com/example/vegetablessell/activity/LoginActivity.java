package com.example.vegetablessell.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.vegetablessell.R;
import com.example.vegetablessell.utils.SessionManagement;
import com.example.vegetablessell.utils.VegetablesConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmail, mPassword;
    private Button mSignInBtn;
    private TextView mRegister, mForgetPass;
    private SessionManagement session;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.usernameLogin);
        mPassword = findViewById(R.id.passwordLogin);

        mSignInBtn = findViewById(R.id.loginButton);

        requestQueue = Volley.newRequestQueue(this);

        mRegister = findViewById(R.id.newUserRegistration);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterUser.class);
                startActivity(intent);
            }
        });

        mForgetPass = findViewById(R.id.forgotPassword);

        mForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
            }
        });


        mSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        signInRequest(email, password);
                    }
                });


            }
        });
    }

    private void signInRequest(String email, String password) {

        String URL = VegetablesConstant.BASE_URL+"/user/auth/api/login";

        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("message");
                            if(!status.equals(VegetablesConstant.LOGIN_SUCCESS_MESSAGE)){
                                Toast.makeText(LoginActivity.this, status, Toast.LENGTH_SHORT).show();
                                return;
                            }

                            JSONObject userDetails = response.getJSONObject("userDetail");
                            String JWTToken = response.getString("JWTTOKEN");
                            String userName = userDetails.getString("username");

                            session.setSession(userDetails.toString());
                            SessionManagement.setJwtToken(JWTToken);

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        } catch (JSONException e) {
                            Log.e("Exception", e.toString());
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
                params.put("password", password);

                return new JSONObject(params).toString().getBytes();
            }
        };

        loginRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        requestQueue.add(loginRequest);

    }

    @Override
    protected void onStart() {
        super.onStart();
        session = new SessionManagement(this);
        if (null != session.getSession() && "" != session.getSession()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}