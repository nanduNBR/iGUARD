package nandubrajan.ceknpy.iguard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.content.Context;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class LockTrackFragment extends Fragment implements Lock.OnFragmentInteractionListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RequestQueue requestQueue ;
    Timer timer;
    private String mParam1;
    private String mParam2;
    ImageView lock;
    int lock_status = 3;
    private OnFragmentInteractionListener mListener;

    public LockTrackFragment() {

    }


    public static LockTrackFragment newInstance(String param1, String param2) {
        LockTrackFragment fragment = new LockTrackFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    class FragmentAdapter extends FragmentPagerAdapter {

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new Lock();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 1;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                //
                //Your tab titles
                //
                case 0:
                    return "Lock";
                case 1:
                    return "Track";
                default:
                    return null;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_lock_track, container, false);
      //  ViewPager vp_pages = (ViewPager) v.findViewById(R.id.vp_pages);
        PagerAdapter pagerAdapter = new FragmentAdapter(getFragmentManager());
       // vp_pages.setAdapter(pagerAdapter);

        TabLayout tbl_pages = (TabLayout) v.findViewById(R.id.tbl_pages);
       // tbl_pages.setupWithViewPager(vp_pages);
        lock = v.findViewById(R.id.lock_image);
        timer = new Timer();
        requestQueue = Volley.newRequestQueue(getActivity());
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                get_lock_status();
            }
        }, 0, 1000);
        lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togle_lock();
            }
        });
        return v;
    }
private void set_lock(final int i)
{
    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
    StringRequest stringRequest = new StringRequest(1, constant.base_url + "setlock.php", new Response.Listener<String>() {
        public void onResponse(String json) {
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
            params.put("lock_status", String.valueOf(i));
            params.put("name", constant.name);
            return params;
        }
    };
    requestQueue.add(stringRequest);
}
    private void get_lock_status() {

        StringRequest stringRequest = new StringRequest(1, constant.base_url + "lock.php", new Response.Listener<String>() {
            public void onResponse(String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                   String string_lock_status = jsonObject.getString("name");
                   lock_status = Integer.parseInt(string_lock_status);
                   if (lock_status==1)
                   {
                       lock.setImageResource(R.drawable.locked);
                   }else if (lock_status==0)
                   {
                       lock.setImageResource(R.drawable.unlocked);
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
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void togle_lock() {
        if (lock_status == 1) {
            lock_status = 0;
            set_lock(0);
            lock.setImageResource(R.drawable.unlocked);
        } else {
            lock_status = 1;
            set_lock(1);
            lock.setImageResource(R.drawable.locked);
        }
    }
}

