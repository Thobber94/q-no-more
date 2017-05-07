package net.bergby.qnomore.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import net.bergby.qnomore.R;
import net.bergby.qnomore.helpClasses.JsonParserGetPurchases;
import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class MyOrdersFragment extends Fragment
{

    private HashMap<String, String> selectedOrderList;
    private OrderClickListener mCallback;
    private int tabPosition;

    public MyOrdersFragment()
    {
        // Required empty public constructor
    }

    public interface OrderClickListener
    {
        void onOrderClicked(HashMap<String, String> order) throws InterruptedException, ExecutionException, JSONException;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        final View view;
        view = inflater.inflate(R.layout.fragment_my_orders, container, false);

        getActivity().setTitle("My orders");

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
        //final SharedPreferences.Editor editor = preferences.edit();
        String profileIdKey = "net.bergby.qnomore.googleId";

        final ListView listView = (ListView) view.findViewById(R.id.orderListView);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.orders_tabLayout);
        final String id = preferences.getString(profileIdKey, "0");

        tabPosition = 0;
        getOrders(listView, id, view);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                tabPosition = tab.getPosition();
                getOrders(listView, id, view);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {

            }
        });


        // Inflate the layout for this fragment
        return view;
    }

    private void getOrders(ListView listView, String id, View view)
    {
        try
        {
            final JsonParserGetPurchases jsonParserGetPurchases = new JsonParserGetPurchases("https://server.bergby.net/QnoMoreAPI/api/purchases", id);

            String totalSum = jsonParserGetPurchases.getTotal_sum().toString();
            TextView totalSumView = (TextView) view.findViewById(R.id.totalSum);
            totalSumView.setText(getString(R.string.euroSymbol, totalSum));
            listView.setFocusable(true);
            listView.setDescendantFocusability(ListView.FOCUS_BLOCK_DESCENDANTS);

            if (tabPosition == 0)
            {
                ListAdapter adapter = new SimpleAdapter(
                        getActivity().getApplicationContext(),
                        jsonParserGetPurchases.getPurchaseList(),
                        R.layout.orders_text_view,
                        new String[]{"purchase_date", "restaurant_name"},
                        new int[]{R.id.order_bottom_text, R.id.order_title}
                );
                listView.setAdapter(adapter);
            }
            else if (tabPosition == 1)
            {
                ListAdapter adapter = new SimpleAdapter(
                        getActivity().getApplicationContext(),
                        jsonParserGetPurchases.getPurchaseListHistory(),
                        R.layout.orders_text_view,
                        new String[]{"purchase_date", "restaurant_name"},
                        new int[]{R.id.order_bottom_text, R.id.order_title}
                );
                listView.setAdapter(adapter);
            }
            else
            {
                System.out.println("Error");
            }


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                {
                    if (tabPosition == 0)
                    {
                        selectedOrderList = jsonParserGetPurchases.getPurchaseList().get(i);
                        try
                        {
                            mCallback.onOrderClicked(selectedOrderList);
                        } catch (InterruptedException | JSONException | ExecutionException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else if (tabPosition == 1)
                    {
                        selectedOrderList = jsonParserGetPurchases.getPurchaseListHistory().get(i);
                        try
                        {
                            mCallback.onOrderClicked(selectedOrderList);
                        } catch (InterruptedException | ExecutionException | JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            });

        } catch (JSONException | IOException | ExecutionException | InterruptedException | ParseException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        try
        {
            mCallback = (OrderClickListener) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString()
                    + " must implement OnOrderClickedListener");
        }

    }
}
