package net.bergby.qnomore.fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;
import net.bergby.qnomore.R;
import net.bergby.qnomore.helpClasses.NewItem;
import net.bergby.qnomore.helpClasses.NewItemAdapter;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuSelectorFragment extends Fragment implements View.OnClickListener
{

    private String restaurant;
    private JSONObject restaurantObject;
    private MenuItemClickedListener mCallback;
    private double sumFroMAdapter;
    private ArrayList itemList = new ArrayList();

    public MenuSelectorFragment()
    {
        // Required empty public constructor
    }

    private NewItemAdapter.NewItemAdapterInterface listener = new NewItemAdapter.NewItemAdapterInterface()
    {
        @Override
        public void onClick(Double sum, ArrayList item)
        {
            sumFroMAdapter = sum;
            itemList = item;
        }
    };

    public interface MenuItemClickedListener
    {
        void onMenuItemClicked(double sum, ArrayList items);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View view;
        view = inflater.inflate(R.layout.fragment_menu_selector, container, false);

        Button continueButton = (Button) view.findViewById(R.id.checkoutContBut);
        continueButton.setOnClickListener(this);

        restaurant = getArguments().getString("specificRestaurant");
        try
        {
            restaurantObject = new JSONObject(restaurant);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        System.out.println(restaurantObject.optString("_id"));

        JSONObject jsonMenu = null;
        try
        {
            jsonMenu = new JSONObject(restaurantObject.optString("menu"));
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        assert jsonMenu != null;
        List<NewItem> newItems = new ArrayList<>();
        Iterator<String> keys = jsonMenu.keys();
        while (keys.hasNext())
        {
            String str_name = keys.next();
            String value = jsonMenu.optString(str_name);
            NewItem item = new NewItem(1, str_name, jsonMenu, 1, Double.valueOf(value), Double.valueOf(value));
            newItems.add(item);
        }

        ListView listView = (ListView) view.findViewById(R.id.list_item);

        NewItemAdapter adapter = new NewItemAdapter(
                getActivity().getApplicationContext(),
                R.layout.menu_text_view,
                newItems,
                listener
        );
        listView.setAdapter(adapter);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onClick(View view)
    {
        mCallback.onMenuItemClicked(sumFroMAdapter, itemList);
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        try
        {
            mCallback = (MenuItemClickedListener ) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString()
                    + " must implement MenuItem");
        }
    }

}
