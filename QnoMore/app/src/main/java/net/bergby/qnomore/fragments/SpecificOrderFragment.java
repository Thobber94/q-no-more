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

    public SpecificOrderFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_specific_order, container, false);

        TextView restaurantNameTextView = (TextView) view.findViewById(R.id.specificOrderRestaurant);
        TextView purchaseItemsTextView = (TextView) view.findViewById(R.id.specificOrderItems);
        ImageView imageViewQr = (ImageView) view.findViewById(R.id.specificItemQr);
        TextView sumTextView = (TextView) view.findViewById(R.id.specificOrderSum);
        TextView orderDateTextView = (TextView) view.findViewById(R.id.specificOrderDate);
        TextView confirmationCodeTextView = (TextView) view.findViewById(R.id.specificOrderConfCodeExtra);

        HashMap order = (HashMap) getArguments().getSerializable("order");

        String orderItems = String.valueOf(order.get("purchase_items"));
        String restaurantName = String.valueOf(order.get("restaurant_name"));
        String confirmationCode = String.valueOf(order.get("confirmation_code"));
        String sum = String.valueOf(order.get("purchase_sum"));
        String orderDate = String.valueOf(order.get("purchase_date"));

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
