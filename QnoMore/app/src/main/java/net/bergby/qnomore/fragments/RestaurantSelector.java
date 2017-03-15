package net.bergby.qnomore.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import net.bergby.qnomore.R;

import java.util.ArrayList;

public class RestaurantSelector extends Fragment
{
    private View view;
    private ListView listView;
    private ArrayList<String> restaurantList = new ArrayList<>();

    public RestaurantSelector()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_restaurant_selector, container, false);

        restaurantList = getArguments().getStringArrayList("restaurantList");

        listView = (ListView) view.findViewById(R.id.restaurantSelectorListView);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getActivity().getApplicationContext(),
                R.layout.restaurant_text_view,
                restaurantList
        );

        listView.setAdapter(arrayAdapter);

        return view;
    }
}
