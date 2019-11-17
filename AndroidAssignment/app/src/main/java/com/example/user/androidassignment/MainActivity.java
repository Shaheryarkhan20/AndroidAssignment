package com.example.user.androidassignment;

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
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class MainActivity extends AppCompatActivity {

    EditText mobile,password;
    Button eIdButton;
    TextView linkRegister;
    private static String URL="http://qa.homechef.pk/api/v1/login ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mobile=(EditText)findViewById(R.id.mobile);
        password=(EditText)findViewById(R.id.password);
        eIdButton=(Button)findViewById(R.id.eIdButton);
        linkRegister=(TextView) findViewById(R.id.linkRegister);

        linkRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });

        eIdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });
    }

    private void Login(){
        final String mobileno=mobile.getText().toString().trim();
        final String Password=password.getText().toString().trim();

        if(mobile.getText().toString().isEmpty() || password.getText().toString().isEmpty())
        {
            Toast.makeText(MainActivity.this,"Please filled required fields",Toast.LENGTH_LONG).show();
        }
        else if(mobile.length()<11  || mobile.length()<10){
            Toast.makeText(MainActivity.this,"Invalid Mobile Number",Toast.LENGTH_LONG).show();
        }
        else
        {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                            String token= jsonObject.getString("token").trim();
                            String uid= jsonObject.getString("uid").trim();
                            String usertype= jsonObject.getString("usertype").trim();
                            String email= jsonObject.getString("email").trim();
                            String mobile= jsonObject.getString("mobile").trim();
                            String name= jsonObject.getString("name").trim();



                            if(mobile.equals("") && name.equals("") ){
                                Toast.makeText(MainActivity.this,"The mobile number or password you entered is incorrect. Please try again",Toast.LENGTH_SHORT).show();
                            }

                            else{

                                 Toast.makeText(MainActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
                                 Intent intent = new Intent(getApplicationContext(),welcome.class);
                                 startActivity(intent);
                            }

                    } catch (JSONException e) {
                        e.printStackTrace();

                        Toast.makeText(MainActivity.this,"Login Failed due to Mismatch Mobile Number and/or Password "+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {


                            Toast.makeText(MainActivity.this,"Login Failed ",Toast.LENGTH_SHORT).show();
                        }
                    })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    Log.d("mobile No: ",mobileno);
                    Log.d("mPasswordo: ",Password);
                    params.put("mobile",mobileno);
                    params.put("password",Password);

                    params.put("Content-Type", "application/json");
                    return params;
                }

            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        }

    }
}
