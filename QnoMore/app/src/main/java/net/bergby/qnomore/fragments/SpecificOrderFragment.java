package net.bergby.qnomore.fragments;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import net.bergby.qnomore.R;
import net.glxn.qrgen.android.QRCode;

import java.util.HashMap;


public class SpecificOrderFragment extends Fragment implements View.OnClickListener
{

    public SpecificOrderFragment()
    {
        // Required empty public constructor
    }

    private NfcAdapter nfcAdapter;
    private String confirmationCode;

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
        TextView isSoldTextView = (TextView) view.findViewById(R.id.specificOrderIsSold);
        Button nfcButton = (Button) view.findViewById(R.id.nfcButton);

        nfcButton.setOnClickListener(this);

        HashMap order = (HashMap) getArguments().getSerializable("order");

        Boolean isSold = Boolean.valueOf(String.valueOf(order.get("is_sold")));
        String orderItems = String.valueOf(order.get("purchase_items"));
        String restaurantName = String.valueOf(order.get("restaurant_name"));
        confirmationCode = String.valueOf(order.get("confirmation_code"));
        String sum = String.valueOf(order.get("purchase_sum"));
        String orderDate = String.valueOf(order.get("purchase_date"));

        Bitmap bitmap = QRCode.from(confirmationCode).bitmap();
        orderItems = orderItems.replace("#", "\n");

        if (isSold)
        {
            isSoldTextView.setText(R.string.delivered_text);
            isSoldTextView.setTextColor(Color.parseColor("#FF8100"));
        }
        else
        {
            isSoldTextView.setText(R.string.in_progress);
            isSoldTextView.setTextColor(Color.parseColor("#57E310"));
        }

        imageViewQr.setImageBitmap(bitmap);
        restaurantNameTextView.setText(restaurantName);
        purchaseItemsTextView.setText(orderItems);
        sumTextView.setText("This purchase: " + "€" + sum);
        orderDateTextView.setText(orderDate);
        confirmationCodeTextView.setText(confirmationCode);

        // NFC Handling
        PackageManager pm = getActivity().getPackageManager();
        if (!pm.hasSystemFeature(PackageManager.FEATURE_NFC))
        {
            Toast.makeText(getContext(), "This device does not support NFC", Toast.LENGTH_SHORT).show();
        }
        else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
        {
            Toast.makeText(getContext(), "This device does not support Android Beam", Toast.LENGTH_SHORT).show();
        }


        return view;
    }

    @Override
    public void onClick(View view)
    {
        sendFile(view);
    }

    private void sendFile(View view)
    {
        nfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());

        if (nfcAdapter != null)
        {
            if (!nfcAdapter.isEnabled())
            {
                Toast.makeText(getContext(), "Please enable NFC!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
            }
            else if (!nfcAdapter.isNdefPushEnabled())
            {
                Toast.makeText(getContext(), "Please enable Android Beam!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Settings.ACTION_NFCSHARING_SETTINGS));
            }
            else
            {
                String textToSend = confirmationCode;

                NdefMessage msg = new NdefMessage(
                     new NdefRecord[]{NdefRecord.createMime(
                            "application/net.bergby.qnomore.android.beam", textToSend.getBytes()
                    )}
                );

            nfcAdapter.setNdefPushMessage(msg, getActivity());
            }
        }
    }
}
