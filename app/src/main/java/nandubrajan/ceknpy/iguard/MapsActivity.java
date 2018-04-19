package nandubrajan.ceknpy.iguard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                loadmap();
            }
        }, 0, 5000);
    }

    private void loadmap() {

        RequestQueue requestQueue = Volley.newRequestQueue(MapsActivity.this);
        StringRequest stringRequest = new StringRequest(1, constant.base_url+"track.php", new Response.Listener<String>() {
            public void onResponse(String json) {
                try {
                    JSONObject j = new JSONObject(json);
                    JSONArray jsonObject = j.getJSONArray("data");

                    for (int i = 0; i < jsonObject.length(); i++) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject(i);
                        String name = jsonObject1.getString("name");
                        String lattitude = jsonObject1.getString("lat");
                        String longitude = jsonObject1.getString("lng");

                        LatLng location = new LatLng(Double.parseDouble(lattitude), Double.parseDouble(longitude));
                        mMap.addMarker(new MarkerOptions().position(location).title(name));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 12.0f));
                        

                    }

                } catch (JSONException j) {

                }
            }
        }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }

    }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("lockid",constant.lockid);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


}