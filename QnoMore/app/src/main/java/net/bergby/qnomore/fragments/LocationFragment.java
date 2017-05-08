package net.bergby.qnomore.fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import net.bergby.qnomore.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class LocationFragment extends Fragment implements OnMapReadyCallback
{

    private String restaurantName;
    private double latitude;
    private double longitude;
    private MapView mapView;
    private GoogleMap map;

    public LocationFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View view;
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_location, container, false);

        getActivity().setTitle("Locations");

        mapView = (MapView) view.findViewById(R.id.locationMapView);
        mapView.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        
        mapView.getMapAsync(this);

        MapsInitializer.initialize(this.getActivity());

        return view;
    }

    // CODE FOUND FROM http://stackoverflow.com/a/36511134/6258554

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private boolean checkLocationPermission()
    {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION))
            {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                //  TODO: Prompt with explanation!

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

            }
            else
            {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        }
        else
        {
            return true;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        map = googleMap;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //User has previously accepted this permission
            if (ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            }
        }
        else
            {
                //Not in api-23, no need to prompt
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            }

            LatLng remmenFestival = new LatLng(59.1296874, 11.3563347);
            CameraUpdate home = CameraUpdateFactory.newLatLng(remmenFestival);
            CameraUpdate zoom = CameraUpdateFactory.newLatLngZoom(remmenFestival, 15);
            map.moveCamera(home);
            map.animateCamera(zoom);

            ArrayList<HashMap<String, String>> locationData = (ArrayList) getArguments().getSerializable("location_data");
            for (HashMap<String, String> aLocationData : locationData)
            {
                restaurantName = aLocationData.get("restaurant_name");
                latitude = Double.parseDouble(aLocationData.get("latitude"));
                longitude = Double.parseDouble(aLocationData.get("longitude"));
                LatLng restaurant = new LatLng(latitude, longitude);
                googleMap.addMarker(new MarkerOptions().position(restaurant)
                    .title(restaurantName));
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_LOCATION:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {

                    // permission was granted, yay!
                    if (ActivityCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        map.setMyLocationEnabled(true);
                        map.getUiSettings().setMyLocationButtonEnabled(true);
                    }
                }
                else
                {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getContext(), "permission denied", Toast.LENGTH_LONG).show();
                    map.getUiSettings().setMyLocationButtonEnabled(false);
                    map.setMyLocationEnabled(false);
                }
            }

        }
    }

    @Override
    public void onResume()
    {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
    }
}
