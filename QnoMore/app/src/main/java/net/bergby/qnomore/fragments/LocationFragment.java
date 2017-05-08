package net.bergby.qnomore.fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import net.bergby.qnomore.R;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationFragment extends Fragment implements OnMapReadyCallback
{

    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 0;
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

        mapView = (MapView) view.findViewById(R.id.locationMapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(this);

        MapsInitializer.initialize(this.getActivity());

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_ACCESS_COARSE_LOCATION);
        }
        else
        {
            map = googleMap;
            map.getUiSettings().setMyLocationButtonEnabled(true);

            map.setMyLocationEnabled(true);

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
