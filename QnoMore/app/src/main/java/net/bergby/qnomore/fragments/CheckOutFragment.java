package net.bergby.qnomore.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import net.bergby.qnomore.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class CheckOutFragment extends Fragment implements View.OnClickListener
{

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.backButton:
                getFragmentManager().popBackStackImmediate();
                break;
        }
    }

    public interface CheckOutFragmentInterface
    {
        void onCheckOutFragmentAction();
    }

    public CheckOutFragment()
    {
        // Required empty public constructor
    }

    private CheckOutFragmentInterface mCallback;
    private double sum;
    private ArrayList<String> itemArrayRaw;
    private ArrayList<String> itemArray = new ArrayList<>();
    //String currency = "\u20ac";
    private String stringSum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_check_out, container, false);

        sum = Double.parseDouble(getArguments().getString("sum"));
        sum = round(sum, 2);
        itemArrayRaw = getArguments().getStringArrayList("items");
        stringSum = String.valueOf(sum);

        if (itemArray.isEmpty())
        {
            itemArray.addAll(itemArrayRaw);
        }

        ListView checkOutListView = (ListView) view.findViewById(R.id.checkoutListView);
        Button backButton = (Button) view.findViewById(R.id.backButton);
        TextView sumTextView = (TextView) view.findViewById(R.id.checkoutSum);
        backButton.setOnClickListener(this);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getActivity().getApplicationContext(),
                R.layout.standard_text_view,
                itemArray
        );
        checkOutListView.setAdapter(arrayAdapter);

        sumTextView.setText(getString(R.string.euroSymbol, stringSum));

        // Inflate the layout for this fragment
        return view;
    }

    // Code found here: http://stackoverflow.com/a/2808648/6258554
    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
