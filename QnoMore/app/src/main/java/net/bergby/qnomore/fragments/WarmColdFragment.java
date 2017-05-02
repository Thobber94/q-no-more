package net.bergby.qnomore.fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import net.bergby.qnomore.R;
import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;


public class WarmColdFragment extends Fragment implements View.OnClickListener
{

    public interface HotColdButtonChosenListener
    {
        void onHotColdButtonSelected(int button) throws JSONException, IOException, ExecutionException, InterruptedException;
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
        ImageButton coldButton = (ImageButton) view.findViewById(R.id.imageButtonFood);
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
            case R.id.imageButtonFood:
                try
                {
                    mCallback.onHotColdButtonSelected(4);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                } catch (IOException e)
                {
                    e.printStackTrace();
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                } catch (ExecutionException e)
                {
                    e.printStackTrace();
                }
                break;
            case R.id.imageButtonWarm:
                try
                {
                    mCallback.onHotColdButtonSelected(3);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                } catch (IOException e)
                {
                    e.printStackTrace();
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                } catch (ExecutionException e)
                {
                    e.printStackTrace();
                }
        }
    }

}
