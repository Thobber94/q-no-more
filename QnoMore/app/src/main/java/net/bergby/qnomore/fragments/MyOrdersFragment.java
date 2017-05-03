package net.bergby.qnomore.fragments;


import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import net.bergby.qnomore.R;
import net.glxn.qrgen.android.QRCode;

public class MyOrdersFragment extends Fragment
{


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

        String id = preferences.getString(profileIdKey, "0");
        System.out.println("Google ID: " + id);

        /*
        Bitmap bitmap = QRCode.from("Hello world! \n \n This is a test.").bitmap();
        ImageView imageView = (ImageView) view.findViewById(R.id.testImageVIew);
        imageView.setImageBitmap(bitmap);
        */

        // Inflate the layout for this fragment
        return view;
    }

}
