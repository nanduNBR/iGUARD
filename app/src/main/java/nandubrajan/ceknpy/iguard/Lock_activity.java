package nandubrajan.ceknpy.iguard;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

public class Lock_activity extends AppCompatActivity {
    RecyclerView loglist;
    List<Model_notification> llist = new ArrayList<>();
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_activity);
        loglist = findViewById(R.id.log_list);
        adapter = new Adapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        loglist.setLayoutManager(layoutManager);
        loglist.setAdapter(adapter);

        final RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        StringRequest stringRequest = new StringRequest(1, constant.base_url + "get_log.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("log", response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Model_notification m = new Model_notification();
                        if (jsonObject.getString("intruder")!=null&&jsonObject.getString("intruder")!="")
                        {
                            m.setIntruder(Base64.decode(jsonObject.getString("intruder"),0));
                        }
                        m.setTime_(jsonObject.getString("time_"));
                        m.setName(jsonObject.getString("name"));
                        m.setStatus(jsonObject.getString("status"));
                        llist.add(m);
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException j) {
                    j.printStackTrace();
                }
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

    class Adapter extends RecyclerView.Adapter<Adapter.viewholder> {
        @Override
        public viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.logerctivity, parent, false);
            return new viewholder(v);
        }

        @Override
        public void onBindViewHolder(viewholder holder, int position) {
            Model_notification m = llist.get(position);
            holder.name.setText("NAME : " + m.getName());
            holder.time.setText("TIME : " + m.getTime_());
            holder.status.setText("STATUS : " + m.getStatus());
            holder.image.setImageBitmap(BitmapFactory.decodeByteArray(m.getIntruder(),0,m.getIntruder().length));
//holder.image.setText("name : "+m.getName());
        }

        @Override
        public int getItemCount() {
            return llist.size();
        }

        class viewholder extends RecyclerView.ViewHolder {
            ImageView image;
            TextView name, time, status;

            public viewholder(View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.image);
                name = itemView.findViewById(R.id.name);
                time = itemView.findViewById(R.id.time);
                status = itemView.findViewById(R.id.status);
            }
        }
    }
}
