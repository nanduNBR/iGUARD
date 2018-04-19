package nandubrajan.ceknpy.iguard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends Fragment {
    View rootView;
    EditText name, email, contact, password, confirmpassword;
    String semail, spassword, sname, scontact, sconfirmpassword, skey;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Button register;
    ProgressDialog pg;
    TextView key;
    ImageView scan;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_register, container, false);
        key = rootView.findViewById(R.id.lock_id_or_key);
        if (constant.key != null) {
            key.setText(constant.key);
        }

        scan = rootView.findViewById(R.id.scanImageView);
        email = rootView.findViewById(R.id.email);
        name = rootView.findViewById(R.id.name);
        contact = rootView.findViewById(R.id.contact_no);
        confirmpassword = rootView.findViewById(R.id.confirmpassword);
        password = rootView.findViewById(R.id.password);
        register = rootView.findViewById(R.id.register);


        pg = new ProgressDialog(getActivity());
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                skey = key.getText().toString();
                sname = name.getText().toString();
                semail = email.getText().toString();
                scontact = contact.getText().toString();
                spassword = password.getText().toString();
                sconfirmpassword = confirmpassword.getText().toString();
                if (validate(semail, scontact, spassword, sconfirmpassword) == true) {
                    pg.setTitle("Registering");
                    pg.show();
                    reg();
                }

            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), scannerClass.class);
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        if (constant.key != null) {
            key.setText(constant.key);
        }
        super.onResume();
    }

    boolean validate(String email, String contact, String password, String confirmpassword) {
        if (!email.matches(emailPattern)) {
            Toast.makeText(getActivity(), "Email Invalid", Toast.LENGTH_SHORT).show();
            return false;

        }
        if (contact.length() != 10) {
            Toast.makeText(getActivity(), "Phone No Invalid", Toast.LENGTH_SHORT).show();
            return false;

        }
        if (!password.equals(confirmpassword)) {
            Toast.makeText(getActivity(), "Password Mismatch", Toast.LENGTH_SHORT).show();
            return false;

        }
        return true;
    }

    private void reg() {
        RequestQueue rq = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.base_url+"register.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                pg.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
                pg.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("email", semail);
                params.put("lockid", skey);
                params.put("contact", scontact);
                params.put("name", sname);
                params.put("password", spassword);
                return params;
            }
        };
        rq.add(stringRequest);
    }
}

