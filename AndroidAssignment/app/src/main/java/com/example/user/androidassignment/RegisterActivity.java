package com.example.user.androidassignment;

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
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText mobile,password,name,email;
    Button register;
    private static String URL="http://qa.homechef.pk/api/v1/register";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name=(EditText) findViewById(R.id.name);
        mobile=(EditText)findViewById(R.id.mobile);
        password=(EditText)findViewById(R.id.password);
        email=(EditText) findViewById(R.id.email);
        register=(Button)findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Registration();
            }
        });
    }

    public void Registration(){



        final String Name = name.getText().toString().trim();
        final String Mobile = mobile.getText().toString().trim();
        final String Password = password.getText().toString().trim();
        final String Email = email.getText().toString().trim();

        if(mobile.getText().toString().isEmpty() || password.getText().toString().isEmpty() || name.getText().toString().isEmpty() || email.getText().toString().isEmpty())
        {
            Toast.makeText(RegisterActivity.this,"Please filled required fields",Toast.LENGTH_LONG).show();
        }
        else if(mobile.length()<11  || mobile.length()<10){
            Toast.makeText(RegisterActivity.this,"Invalid Mobile Number",Toast.LENGTH_LONG).show();
        }
        else {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jsonObject = new JSONObject(response);

                        String special_msg= jsonObject.getString("special_msg").trim();
                        String uid= jsonObject.getString("uid").trim();
                        String message= jsonObject.getString("message").trim();

                        Log.d("uid: ",uid);
                        Log.d("special_msg: ",special_msg);
                        Log.d("message: ",message);


                        Intent i = new Intent(getApplicationContext(), MainActivity.class);

                            Toast.makeText(RegisterActivity.this, "Signup successful", Toast.LENGTH_LONG).show();
                           startActivity(i);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(RegisterActivity.this, "Registration Failed ", Toast.LENGTH_LONG).show();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(RegisterActivity.this, "Registration Failed ", Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    params.put("name", Name);
                    params.put("mobile", Mobile);
                    params.put("password", Password);
                    params.put("email", Email);

                    params.put("Content-Type", "application/json");
                    return params;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }



    }
}
