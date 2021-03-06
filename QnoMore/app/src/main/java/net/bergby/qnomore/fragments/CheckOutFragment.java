package net.bergby.qnomore.fragments;


import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import net.bergby.qnomore.R;
import org.json.JSONException;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class CheckOutFragment extends Fragment implements View.OnClickListener
{

    public interface CheckOutFragmentInterface
    {
        void onCheckOutFragmentAction(ArrayList<String> items, double sum, String restaurant) throws InterruptedException, ExecutionException, JSONException, IOException;
    }

    public CheckOutFragment()
    {
        // Required empty public constructor
    }

    private CheckOutFragmentInterface mCallback;
    private ArrayList<String> itemArray = new ArrayList<>();
    private ArrayList<String> itemArraySorted = new ArrayList<>();
    private String restaurant;
    private double sum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_check_out, container, false);

        sum = Double.parseDouble(getArguments().getString("sum"));
        sum = round(sum, 2);
        ArrayList<String> itemArrayRaw = getArguments().getStringArrayList("items");
        String stringSum = String.valueOf(sum);
        restaurant = getArguments().getString("restaurant");

        if (itemArray.isEmpty())
        {
            itemArray.addAll(itemArrayRaw);
        }

        Set<String> unique = new HashSet<>(itemArray);

        for (String item: unique)
        {
            itemArraySorted.add(Collections.frequency(itemArray, item) + " x " + item);
        }

        ListView checkOutListView = (ListView) view.findViewById(R.id.checkoutListView);
        Button backButton = (Button) view.findViewById(R.id.backButton);
        Button confirmButton = (Button) view.findViewById(R.id.continue_button);
        TextView sumTextView = (TextView) view.findViewById(R.id.checkoutSum);
        backButton.setOnClickListener(this);
        confirmButton.setOnClickListener(this);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getActivity().getApplicationContext(),
                R.layout.standard_text_view,
                itemArraySorted
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

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        try
        {
            mCallback = (CheckOutFragmentInterface) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString()
                    + " must implement CheckOutFragmentInterface");
        }
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.backButton:
                getFragmentManager().popBackStackImmediate();
                break;
            case R.id.continue_button:
                try
                {
                    mCallback.onCheckOutFragmentAction(itemArraySorted, sum, restaurant);
                } catch (InterruptedException | ExecutionException | IOException | JSONException e)
                {
                    e.printStackTrace();
                }
                break;
        }
    }

}
