package net.bergby.qnomore.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.bergby.qnomore.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuSelectorFragment extends Fragment
{

    View view;


    public MenuSelectorFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        view = inflater.inflate(R.layout.fragment_menu_selector, container, false);

        // Inflate the layout for this fragment
        return view;
    }

}
