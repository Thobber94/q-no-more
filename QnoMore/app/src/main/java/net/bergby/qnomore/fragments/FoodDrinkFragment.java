package net.bergby.qnomore.fragments;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import net.bergby.qnomore.R;


public class FoodDrinkFragment extends Fragment implements View.OnClickListener
{

    public interface FoodDrinkButtonChosenListener
    {
        void onFoodDrinkButtonSelected(int button);
    }

    private View view;
    private FoodDrinkButtonChosenListener mCallback;

    public FoodDrinkFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        try
        {
            mCallback = (FoodDrinkButtonChosenListener) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString()
                    + " must implement FoodDrinkButtonChosenListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_food_drink, container, false);

        ImageButton drinksButton = (ImageButton) view.findViewById(R.id.imageButtonDrink);
        ImageButton foodButton = (ImageButton) view.findViewById(R.id.imageButtonFood);
        foodButton.setOnClickListener(this);
        drinksButton.setOnClickListener(this);

        // Makes sure the fab button is gone
        if (view.findViewById(R.id.fab) != null)
        {
            if (view.findViewById(R.id.fab).getVisibility() == View.VISIBLE)
            {
                view.findViewById(R.id.fab).setVisibility(View.GONE);
            }
        }

        return view;
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.imageButtonFood:
                mCallback.onFoodDrinkButtonSelected(2);
                break;
            case R.id.imageButtonDrink:
                mCallback.onFoodDrinkButtonSelected(1);
                break;
        }
    }
}
