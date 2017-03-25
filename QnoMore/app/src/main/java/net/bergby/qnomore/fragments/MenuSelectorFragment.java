package net.bergby.qnomore.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.bergby.qnomore.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuSelectorFragment extends Fragment
{

    public interface MenuItemClickedListener
    {
        void onMenuItemClicked(String menuItem);
    }

    public MenuSelectorFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view;

        view = inflater.inflate(R.layout.fragment_menu_selector, container, false);

        // Inflate the layout for this fragment
        return view;
    }
}
