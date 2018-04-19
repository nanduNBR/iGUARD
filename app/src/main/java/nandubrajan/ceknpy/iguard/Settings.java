package nandubrajan.ceknpy.iguard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        findViewById(R.id.clear_log).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sh = getSharedPreferences("userdata", Context.MODE_PRIVATE);
                if (sh.getString("role","").equals("admin"))
                {
                    StringRequest stringRequest = new StringRequest(1, constant.base_url + "deletelog.php", new Response.Listener<String>() {
                        public void onResponse(String json) {
                            Log.e("rez",json);
                            Toast.makeText(Settings.this, "Log cleared", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }

                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("lockid", constant.lockid);
                            return params;

                        }
                    };
                    requestQueue.add(stringRequest);
                }

            }
        });
        findViewById(R.id.clear_msg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sh = getSharedPreferences("userdata", Context.MODE_PRIVATE);
                if (sh.getString("role","").equals("admin")) {
                    StringRequest stringRequest = new StringRequest(1, constant.base_url + "deletemsg.php", new Response.Listener<String>() {
                        public void onResponse(String json) {
                            Log.e("rez", json);
                            Toast.makeText(Settings.this, "Message cleared", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }

                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("lockid", constant.lockid);
                            return params;

                        }
                    };
                    requestQueue.add(stringRequest);
                }
            }
        });

    }
}
