package nandubrajan.ceknpy.iguard;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.glxn.qrgen.android.QRCode;

import java.util.HashMap;
import java.util.Map;


public class QRGenerator extends AppCompatActivity {
RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrgenerator);
        Bitmap myBitmap = QRCode.from(constant.lockid).bitmap();
        ImageView myImage = (ImageView) findViewById(R.id.qrcode);
        myImage.setImageBitmap(myBitmap);
        requestQueue = Volley.newRequestQueue(this);
        setstatus();

    }

    private void setstatus() {
        StringRequest stringRequest = new StringRequest(1, constant.base_url + "qr.php", new Response.Listener<String>() {
            public void onResponse(String json) {
                Log.e("rez",json);
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
                params.put("status", "1");
                return params;

            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stop();
    }

    private void stop() {
        StringRequest stringRequest = new StringRequest(1, constant.base_url + "qr.php", new Response.Listener<String>() {
            public void onResponse(String json) {
                Log.e("rez",json);
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
                params.put("status", "0");
                return params;

            }
        };
        requestQueue.add(stringRequest);
    }
}
