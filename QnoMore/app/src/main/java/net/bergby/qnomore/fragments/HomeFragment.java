package net.bergby.qnomore.fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import net.bergby.qnomore.R;
import net.glxn.qrgen.android.QRCode;

/**
 * Created by thomas on 04-Mar-17.
 */
public class HomeFragment extends Fragment
{

    public HomeFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view;
        view = inflater.inflate(R.layout.home_main, container, false);

        String message = getArguments().getString("message");

        if (message != null)
        {
            TextView textView = (TextView) view.findViewById(R.id.tv_home);
            textView.setText(message);
        }

        Bitmap bitmap = QRCode.from("Hello world! \n \n This is a test.").bitmap();
        ImageView imageView = (ImageView) view.findViewById(R.id.testImageVIew);
        imageView.setImageBitmap(bitmap);

        return view;
    }
}
