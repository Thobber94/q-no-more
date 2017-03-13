package net.bergby.qnomore.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.bergby.qnomore.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WarmColdFragment extends Fragment
{


    public WarmColdFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_warm_cold, container, false);
    }

}
