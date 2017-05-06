package net.bergby.qnomore.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import net.bergby.qnomore.R;

import java.util.HashMap;


public class SpecificOrderFragment extends Fragment
{

    private View view;
    private HashMap order = new HashMap<>();
    private String restaurantName;
    private String orderItems;

    public SpecificOrderFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_specific_order, container, false);

        TextView restaurantNameTextView = (TextView) view.findViewById(R.id.specificOrderRestaurant);
        TextView purchaseItemsTextView = (TextView) view.findViewById(R.id.specificOrderItems);

        order = (HashMap) getArguments().getSerializable("order");

        orderItems = String.valueOf(order.get("purchase_items"));
        restaurantName = String.valueOf(order.get("restaurant_name"));

        orderItems = orderItems.replace("#", "\n");


        restaurantNameTextView.setText(restaurantName);
        purchaseItemsTextView.setText(orderItems);

        return view;
    }

}
