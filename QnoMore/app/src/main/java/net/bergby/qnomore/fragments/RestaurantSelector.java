package net.bergby.qnomore.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import net.bergby.qnomore.R;

public class RestaurantSelector extends Fragment
{
    private View view;

    public RestaurantSelector()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_restaurant_selector, container, false);
        // Inflate the layout for this fragment
        return view;
    }
}
