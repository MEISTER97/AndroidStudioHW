package com.example.hw1.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.hw1.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textview.MaterialTextView;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private MaterialTextView map_LBL_title;
    GoogleMap googleMap;
    FrameLayout map;

    public MapFragment() {


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_map, container, false);

        findViews(v);
        map = v.findViewById(R.id.map);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return v;
    }

    private void findViews(View v) {

    }

    public void zoom(double lat, double lon) {
        LatLng mapcor = new LatLng(lat, lon);
        if (googleMap != null) {
            googleMap.addMarker(new MarkerOptions().position(mapcor).title("Marker in here"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapcor, 15));
        }
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;

    }
}