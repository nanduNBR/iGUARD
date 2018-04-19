package nandubrajan.ceknpy.iguard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
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

public class Family_Activity extends AppCompatActivity {
    RecyclerView family;
    List<Model_family> mfamily = new ArrayList<>();
    adapter adapterM;
    int click;
    int clicked_idex;
    RequestQueue rq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_);
        family = findViewById(R.id.family);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        family.setLayoutManager(layoutManager);
        rq = Volley.newRequestQueue(Family_Activity.this);
        family.setItemAnimator(new DefaultItemAnimator());
        getdata();
        adapterM = new adapter(new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.e("evide", "jkjkj");
                if (constant.role.equals("admin"))
                {
                    click = click + 1;
                    if (clicked_idex == position && click == 2) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Family_Activity.this, "The Member Will Be Removed", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Remove_user(mfamily.get(position).getEmail());
                    } else if (clicked_idex == position) {
                        click = click++;
                    } else {
                        clicked_idex = position;
                        click = 0;
                    }
                }

            }

            @Override
            public void onClicke(View view, int position) {
                Log.e("evide", "jkjkj");
                if (constant.role.equals("admin"))
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Family_Activity.this, "New Admin Member Added", Toast.LENGTH_SHORT).show();
                        }

                    });
                    Make_admin(mfamily.get(position).getEmail());
                }


            }
        });
        family.setAdapter(adapterM);
        Toast.makeText(this, "Long Click To Make Multiple Admin And Tripple Tap To Remove", Toast.LENGTH_SHORT).show();


    }

    void Make_admin(final String s) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.base_url + "makeadmin.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("login responzse", response);
                mfamily.clear();
                getdata();

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
                params.put("lockid", constant.lockid);
                params.put("email", s);
                return params;
            }
        };
        rq.add(stringRequest);
    }

    void Remove_user(final String s) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.base_url + "delete_user.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("login responzse", response);
                mfamily.clear();
                getdata();

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
                params.put("lockid", constant.lockid);
                params.put("email", s);
                return params;
            }
        };
        rq.add(stringRequest);
    }

    private void getdata() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.base_url + "family_memebers.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("login responzse", response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Model_family model_family = new Model_family();
                        model_family.setContact(jsonObject.getString("contact"));
                        model_family.setEmail(jsonObject.getString("email"));
                        model_family.setName(jsonObject.getString("name"));
                        model_family.setRole(jsonObject.getString("role"));
                        mfamily.add(model_family);
                        adapterM.notifyDataSetChanged();
                    }

                } catch (JSONException j) {
                    j.printStackTrace();
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
                params.put("lock_id", constant.lockid);
                return params;
            }
        };
        rq.add(stringRequest);
    }


    class adapter extends RecyclerView.Adapter<adapter.viewholder> {
        RecyclerViewClickListener mListener;

        public adapter(RecyclerViewClickListener mListener) {
            this.mListener = mListener;
        }

        @Override
        public viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.family_inflator, parent, false);
            return new viewholder(v);
        }

        @Override
        public void onBindViewHolder(viewholder holder, int position) {
            Model_family model_family = mfamily.get(position);
            if (!model_family.getEmail().equals(constant.email)) {
                holder.name.setText("Name : " + model_family.getName());
                holder.email.setText("Email : " + model_family.getEmail());
                holder.phone.setText("Phone : " + model_family.getContact());
                holder.role.setText("Role : " + model_family.getRole());
            }

        }

        @Override
        public int getItemCount() {
            return mfamily.size();
        }

        class viewholder extends RecyclerView.ViewHolder {
            TextView name, email, phone, role;

            public viewholder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.name);
                email = itemView.findViewById(R.id.email);
                phone = itemView.findViewById(R.id.phone);
                role = itemView.findViewById(R.id.role);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListener.onClick(view, getAdapterPosition());
                    }
                });
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        mListener.onClicke(view, getAdapterPosition());
                        return false;
                    }
                });
            }
        }

    }


}
