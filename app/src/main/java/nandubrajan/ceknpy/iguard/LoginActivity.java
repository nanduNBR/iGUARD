package nandubrajan.ceknpy.iguard;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.service.voice.VoiceInteractionSession;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class LoginActivity extends Fragment {
    EditText usernameEditText, passwordeditText;
    String username, password;
    Button loginButton;
    ProgressDialog pg;
    TextView forget_password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_login, container, false);
        usernameEditText = rootView.findViewById(R.id.usernameEditText);
        passwordeditText = rootView.findViewById(R.id.passwordEditText);
        loginButton = rootView.findViewById(R.id.loginButton);
        forget_password = rootView.findViewById(R.id.forgot_password);
        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = usernameEditText.getText().toString();
                if (username != null) {
                    ForgetPassword();
                }else {
                    Toast.makeText(getActivity(), "Enter a username", Toast.LENGTH_SHORT).show();
                }
            }
        });
        SharedPreferences sh = getActivity().getSharedPreferences("userdata",Context.MODE_PRIVATE);
        String status = sh.getString("status","null");
        if (status.equals("logged"))
        {
            constant.name=sh.getString("name","error");
            constant.email=sh.getString("email","error");
            constant.role =sh.getString("role","error");
            constant.lockid =sh.getString("lockid","error");
            Intent intent = new Intent(getActivity(), VerificationActivity.class);
            startActivity(intent);
        }
        pg = new ProgressDialog(getActivity());
        loginButton.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               username = usernameEditText.getText().toString();
                                               password = passwordeditText.getText().toString();
                                               if (username != null && password != null) {
                                                   login();
                                               }
                                           }
                                       }
        );

        return rootView;


    }

    private void ForgetPassword() {
        RequestQueue rq = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.base_url+"sentpassword.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("login responzse", response);
                Toast.makeText(getActivity(), "Check your inbox", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("email", username);
                return params;
            }
        };
        rq.add(stringRequest);
    }

    private void login() {
        RequestQueue rq = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.base_url+"login.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("login responzse", response);
                try {
                    JSONObject j = new JSONObject(response);
                    JSONObject jsonObject = j.getJSONObject("data");
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        String lockid = jsonObject1.getString("lockid");
                        String name = jsonObject1.getString("name");
                        String email = jsonObject1.getString("email");
                        String contact = jsonObject1.getString("contact");
                        String role = jsonObject1.getString("role");
                        String password = jsonObject1.getString("password");

                        SharedPreferences sh = getActivity().getSharedPreferences("userdata", Context.MODE_PRIVATE);
                        SharedPreferences.Editor she = sh.edit();
                        she.putString("lockid", lockid);
                        she.putString("name", name);
                        she.putString("email", email);
                        she.putString("contact", contact);
                        she.putString("role", role);
                        she.putString("password", password);
                        she.commit();
                        constant.name=sh.getString("name","error");
                        constant.email=sh.getString("email","error");
                        constant.role =sh.getString("role","error");
                        constant.lockid=sh.getString("lockid","error");

                        if (constant.role.equals("admin")){
                            SharedPreferences shi = getActivity().getSharedPreferences("userdata", Context.MODE_PRIVATE);
                            SharedPreferences.Editor shei = shi.edit();
                            shei.putString("status", "logged");
                            shei.commit();
                        }
                        final String otp = jsonObject1.getString("otp");
                        String verifylockid = jsonObject1.getString("verifylockid");
                        if (verifylockid.equals("1"))
                        {
                            Intent intent = new Intent(getActivity(), VerificationActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }else {
                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            final EditText edittext = new EditText(getActivity());
                            alert.setMessage("Enter Your Otp");
                            alert.setTitle("Otp verification");

                            alert.setView(edittext);

                            alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    //What ever you want to do with the value
                                   // Editable YouEditTextValue = edittext.getText();
                                    //OR
                                    String YouEditTextValue = edittext.getText().toString();
                                    if (YouEditTextValue.equals(otp))
                                    {
                                        verify();
                                    }
                                    else {
                                        Toast.makeText(getActivity(),"otp mis match", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            alert.setNegativeButton("No Option", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // what ever you want to do with No option.
                                }
                            });

                            alert.show();
                        }


                    } else {
                        Toast.makeText(getActivity(), "login Failed", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException j) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("email", username);
                params.put("password", password);
                return params;
            }
        };
        rq.add(stringRequest);
    }

     void verify() {
        RequestQueue rq = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.base_url+"verified.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("login responzse", response);
                SharedPreferences sh = getActivity().getSharedPreferences("userdata", Context.MODE_PRIVATE);
                SharedPreferences.Editor she = sh.edit();
                she.putString("status", "logged");
                she.commit();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("email", constant.email);
                params.put("lockid", constant.lockid);
                return params;
            }
        };
        rq.add(stringRequest);
    }
}

