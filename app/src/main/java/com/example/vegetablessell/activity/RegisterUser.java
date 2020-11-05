package com.example.vegetablessell.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class RegisterUser extends AppCompatActivity {

    private EditText mFullName, mContactNo, mEmail, mPassword, mConfirmPass;

    private Button mRegisterBtn;

    RequestQueue requestQueue;

    AlertDialog.Builder dialog;

    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mFullName = findViewById(R.id.firstName);
        mContactNo = findViewById(R.id.phoneNumber);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.passowrd);
        mConfirmPass = findViewById(R.id.confirmPass);

        session = new SessionManagement(this);

        mRegisterBtn = findViewById(R.id.registerButton);

        dialog = new AlertDialog.Builder(this);

        requestQueue = Volley.newRequestQueue(this);

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mFullName.getText().toString();
                String contactNo = mContactNo.getText().toString();
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();
                String confirmPass = mConfirmPass.getText().toString();

                Handler handler = new Handler();

                Runnable registerCode = new Runnable() {
                    @Override
                    public void run() {
                        callApi(username, email, contactNo, password);
                    }
                };

                handler.post(registerCode);




            }
        });
    }

    private void callApi(String username, String email, String contactNo, String password) {

        String URL = VegetablesConstant.BASE_URL+"/auth/api/register";

        JsonObjectRequest arrReq = new JsonObjectRequest(Request.Method.POST, URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("response ", response.toString());
                            String status = response.getString("message");
                            if(!VegetablesConstant.REGISTERATION_SUCCESS_MESSAGE.equals(status)) {
                                Toast.makeText(RegisterUser.this, status, Toast.LENGTH_SHORT).show();
                                return;
                            }

                            JSONObject userDetails = response.getJSONObject("userDetail");
                            String JWTToken = response.getString("JWTTOKEN");
                            String userName = userDetails.getString("username");
                            session.setSession(userDetails.toString());
                            SessionManagement.setJwtToken(JWTToken);

                            Intent intent = new Intent(RegisterUser.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("json err", error.toString());
                    }
                }) {
            @Override
            public byte[] getBody() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("contactNo", contactNo);
                params.put("email", email);
                params.put("password", password);

                return new JSONObject(params).toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        arrReq.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        requestQueue.add(arrReq);

    }


}