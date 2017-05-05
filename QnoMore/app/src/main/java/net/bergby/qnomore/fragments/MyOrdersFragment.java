package net.bergby.qnomore.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

    public MyOrdersFragment()
    {
        // Required empty public constructor
    }

    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view;
        view = inflater.inflate(R.layout.fragment_my_orders, container, false);
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
        //final SharedPreferences.Editor editor = preferences.edit();
        String profileIdKey = "net.bergby.qnomore.googleId";

        ListView listView = (ListView) view.findViewById(R.id.orderListView);

        String id = preferences.getString(profileIdKey, "0");
        System.out.println("Google ID: " + id);

        try
        {
            final JsonParserGetPurchases jsonParserGetPurchases = new JsonParserGetPurchases("https://server.bergby.net/QnoMoreAPI/api/purchases", id);

            ListAdapter adapter = new SimpleAdapter(
                    getActivity().getApplicationContext(),
                    jsonParserGetPurchases.getPurchaseList(),
                    R.layout.orders_text_view,
                    new String[]{"purchase_date", "restaurant_name"},
                    new int[]{R.id.order_bottom_text, R.id.order_title}
            );

            listView.setAdapter(adapter);

            String totalSum = jsonParserGetPurchases.getTotal_sum().toString();

            TextView totalSumView = (TextView) view.findViewById(R.id.totalSum);
            totalSumView.setText(getString(R.string.euroSymbol, totalSum));

            listView.setFocusable(true);
            listView.setDescendantFocusability(ListView.FOCUS_BLOCK_DESCENDANTS);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                {
                    selectedOrderList = jsonParserGetPurchases.getPurchaseList().get(i);
                }
            });

        } catch (JSONException | IOException | ExecutionException | InterruptedException | ParseException e)
        {
            e.printStackTrace();
        }

        /*
        Bitmap bitmap = QRCode.from("Hello world! \n \n This is a test.").bitmap();
        ImageView imageView = (ImageView) view.findViewById(R.id.testImageVIew);
        imageView.setImageBitmap(bitmap);
        */

        // Inflate the layout for this fragment
        return view;
    }

}
