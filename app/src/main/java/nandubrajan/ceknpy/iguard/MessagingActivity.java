package nandubrajan.ceknpy.iguard;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MessagingActivity extends AppCompatActivity {
    RecyclerView mMessagev;
    String sid;
    List<MessageModel> mMessageList = new ArrayList<>();
    MessageAdapter mMessageAdapter;
    EditText mMessageData;
    Button msendButton;
    int lastid;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        Bundle b = getIntent().getExtras();
        lastid=0;
        mMessagev = findViewById(R.id.rmessage_view);
        RecyclerView.LayoutManager l = new LinearLayoutManager(this);
        mMessagev.setLayoutManager(l);
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        String id = sharedPreferences.getString("id", "");
        msendButton = findViewById(R.id.sendbutton);
        requestQueue = Volley.newRequestQueue(MessagingActivity.this);
        mMessageData = findViewById(R.id.msg_content);
        mMessageAdapter = new MessageAdapter(mMessageList,id);
        mMessagev.setAdapter(mMessageAdapter);
        msendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
        refreshMessage();

    }

    private void sendMessage() {
        RequestQueue requestQueue = Volley.newRequestQueue(MessagingActivity.this);
        StringRequest stringRequest = new StringRequest(1, constant.base_url+"sendmessage.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("responsegcg", response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volley error", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("sid", constant.email);
                params.put("sendername", constant.name);
                params.put("lockid", constant.lockid);
                params.put("message", mMessageData.getText().toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mMessageData.setText("");
                    }
                });

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void refreshMessage() {
        TimerTask t;
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                StringRequest stringRequest = new StringRequest(1, constant.base_url+"messages.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("refreshmsg", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObject1 = jsonObject.getJSONObject("details");
                            JSONArray jsonArray = jsonObject1.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                MessageModel mConversation = new MessageModel();
                                mConversation.setSid(jsonObject2.getString("sender"));
                                mConversation.setSendername(jsonObject2.getString("name"));
                                mConversation.setMessage(jsonObject2.getString("message"));
                                mConversation.setTime(jsonObject2.getString("time"));
                                mConversation.setId(jsonObject2.getString("id"));
                                lastid = jsonObject2.getInt("id");
                                Log.e("id",jsonObject2.getString("id"));
                                mMessageList.add(mConversation);
                                mMessageAdapter.notifyDataSetChanged();
                                mMessagev.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Call smooth scroll
                                        mMessagev.smoothScrollToPosition(mMessageAdapter.getItemCount());
                                        //
                                    }
                                });
                            }
                        } catch (JSONException j) {
                            Log.e("response", j.toString());
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("volley error", error.toString());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                        String id = sharedPreferences.getString("id", "");
                        params.put("lockid", constant.lockid);
                        params.put("id", String.valueOf(lastid));
                        return params;
                    }
                };
                requestQueue.add(stringRequest);
            }
        }, 0, 1000);
    }
}
