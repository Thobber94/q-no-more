package net.bergby.qnomore.fragments;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import net.bergby.qnomore.R;
import net.glxn.qrgen.android.QRCode;

import java.util.HashMap;


public class SpecificOrderFragment extends Fragment
{

    private View view;
    private HashMap order = new HashMap<>();
    private String restaurantName;
    private String orderItems;
    private String sum;
    private String orderDate;
    private String confirmationCode;

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
        ImageView imageViewQr = (ImageView) view.findViewById(R.id.specificItemQr);
        TextView sumTextView = (TextView) view.findViewById(R.id.specificOrderSum);
        TextView orderDateTextView = (TextView) view.findViewById(R.id.specificOrderDate);
        TextView confirmationCodeTextView = (TextView) view.findViewById(R.id.specificOrderConfCodeExtra);

        order = (HashMap) getArguments().getSerializable("order");

        orderItems = String.valueOf(order.get("purchase_items"));
        restaurantName = String.valueOf(order.get("restaurant_name"));
        confirmationCode = String.valueOf(order.get("confirmation_code"));
        sum = String.valueOf(order.get("purchase_sum"));
        orderDate = String.valueOf(order.get("purchase_date"));

        Bitmap bitmap = QRCode.from(confirmationCode).bitmap();
        orderItems = orderItems.replace("#", "\n");

        imageViewQr.setImageBitmap(bitmap);
        restaurantNameTextView.setText(restaurantName);
        purchaseItemsTextView.setText(orderItems);
        sumTextView.setText("This purchase: " + "€" + sum);
        orderDateTextView.setText(orderDate);
        confirmationCodeTextView.setText(confirmationCode);

        return view;
    }

}
