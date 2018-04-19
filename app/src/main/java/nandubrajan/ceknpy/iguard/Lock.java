package nandubrajan.ceknpy.iguard;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Lock.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class Lock extends Fragment {
    Timer t;
    TimerTask tt;
    ImageView img;
    int status = 0;
    private OnFragmentInteractionListener mListener;

    public Lock() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_lock, container, false);
      //  img = v.findViewById(R.id.lockImage);
        t = new Timer(true);
        tt = new TimerTask() {
            @Override
            public void run() {
                getstatus();
            }
        };
        t.schedule(tt, 0, 500);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStatus();
            }
        });
        return v;
    }

    private void setStatus() {
        if (status==0)
        {
            status=1;
        }
        else {
            status =0;
        }
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(1, "http://192.168.43.222/iguard/status.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                img.setImageDrawable(getResources().getDrawable(R.drawable.unlocked));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("lockid",constant.lockid);
                params.put("email",constant.email);
                params.put("status",String.valueOf(status));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void getstatus() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(1, "http://192.168.43.222/iguard/image.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                status = Integer.parseInt(response);
                if(response.equals("0"))
                {

                    img.setImageDrawable(getResources().getDrawable(R.drawable.locked));
                }

                if(response.equals("1"))
                {
                    img.setImageDrawable(getResources().getDrawable(R.drawable.unlocked));
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
                params.put("lockid",constant.lockid);
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        t.cancel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        t.cancel();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
