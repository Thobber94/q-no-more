package net.bergby.qnomore.fragments;


import android.support.v4.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import net.bergby.qnomore.R;

public class MyOrdersFragment extends Fragment
{


    public MyOrdersFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
        final SharedPreferences.Editor editor = preferences.edit();
        String profileIdKey = "net.bergby.qnomore.googleId";

        String id = preferences.getString(profileIdKey, "0");
        System.out.println("Google ID: " + id);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_orders, container, false);
    }

}
