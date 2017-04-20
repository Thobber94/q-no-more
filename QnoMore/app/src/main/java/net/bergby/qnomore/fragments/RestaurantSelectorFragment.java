package net.bergby.qnomore.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import net.bergby.qnomore.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RestaurantSelectorFragment extends Fragment
{
    private ArrayList<String> restaurantList;
    private String itemClicked;
    private RestaurantItemClickedListener mCallback;
    private ArrayList<String> restaurantListNames = new ArrayList<>();
    public int counter = 0;

    public interface RestaurantItemClickedListener
    {
        void onRestaurantItemClicked(String restaurant);
    }

    public RestaurantSelectorFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_restaurant_selector, container, false);

        if (restaurantListNames.isEmpty())
        {
            restaurantList = getArguments().getStringArrayList("restaurantList");

            for (String s : restaurantList)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(s);
                    restaurantListNames.add(jsonObject.optString("restaurant_name"));
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }

        ListView listView = (ListView) view.findViewById(R.id.restaurantSelectorListView);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getActivity().getApplicationContext(),
                R.layout.standard_text_view,
                restaurantListNames
        );

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                itemClicked = restaurantList.get(i);
                mCallback.onRestaurantItemClicked(itemClicked);
            }
        });


        return view;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        try
        {
            mCallback = (RestaurantItemClickedListener) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString()
                    + " must implement RestaurantItemClickedListener");
        }
    }
}
