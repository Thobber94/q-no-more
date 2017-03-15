package net.bergby.qnomore.fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import net.bergby.qnomore.R;


public class WarmColdFragment extends Fragment implements View.OnClickListener
{

    public interface HotColdButtonChosenListener
    {
        void onHotColdButtonSelected(int button);
    }

    private View view;
    private HotColdButtonChosenListener mCallback;

    public WarmColdFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        try
        {
            mCallback = (HotColdButtonChosenListener) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString()
                    + " must implement HotColdButtonChosenListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_warm_cold, container, false);
        // Inflate the layout for this fragment
        ImageButton coldButton = (ImageButton) view.findViewById(R.id.imageButtonCold);
        ImageButton warmButton = (ImageButton) view.findViewById(R.id.imageButtonWarm);
        coldButton.setOnClickListener(this);
        warmButton.setOnClickListener(this);

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
            case R.id.imageButtonCold:
                mCallback.onHotColdButtonSelected(4);
                break;
            case R.id.imageButtonWarm:
                mCallback.onHotColdButtonSelected(3);
        }
    }

}
