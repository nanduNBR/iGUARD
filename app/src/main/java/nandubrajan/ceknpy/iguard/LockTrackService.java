package nandubrajan.ceknpy.iguard;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import fr.quentinklein.slt.LocationTracker;
import fr.quentinklein.slt.TrackerSettings;

public class LockTrackService extends Service {
    public LockTrackService() {
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();
        TrackerSettings settings =
                new TrackerSettings()
                        .setUseGPS(true)
                        .setUseNetwork(true)
                        .setUsePassive(true)
                        .setTimeBetweenUpdates(1 * 60 * 1000)
                        .setMetersBetweenUpdates(100);

        LocationTracker tracker = new LocationTracker(getBaseContext(), settings) {

            @Override
            public void onLocationFound(Location location) {
                locationUpdate(location);
                // Do some stuff when a new location has been found.
            }

            @Override
            public void onTimeout() {

            }
        };
        tracker.startListening();
    }

    private void locationUpdate(Location location) {
        Log.e("location","das");
        final Double lattitude = location.getLatitude();
        final Double longitude = location.getLongitude();
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        StringRequest stringRequest = new StringRequest(1, constant.base_url+"location.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("location",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email",constant.email);
                params.put("lockid",constant.lockid);
                params.put("lattitude",String.valueOf(lattitude));
                params.put("longitude",String.valueOf(longitude));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
