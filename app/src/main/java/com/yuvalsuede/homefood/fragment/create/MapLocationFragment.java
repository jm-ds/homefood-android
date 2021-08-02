package com.yuvalsuede.homefood.fragment.create;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yuvalsuede.homefood.App;
import com.yuvalsuede.homefood.CreateDishActivity;
import com.yuvalsuede.homefood.R;

import java.util.List;

public class MapLocationFragment extends SupportMapFragment implements GoogleApiClient.ConnectionCallbacks, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {
    private App app;
    private static final String TAG = MapLocationFragment.class.getName();
    private GoogleApiClient mGoogleApiClient;
    private LatLng selectedLocation;
    private SearchView searchView;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
        app = (App) getActivity().getApplication();
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            ((CreateDishActivity) getActivity()).setUI(CreateDishActivity.NavStep.CATEGORY);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.map_menu, menu);
        final MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i(TAG, query);
                search(query);
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                return false;
            }
        });
        myActionMenuItem.setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void search(String query) {
        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocationName(query, 1);
            if (addresses.size() > 0) {
                double latitude = addresses.get(0).getLatitude();
                double longitude = addresses.get(0).getLongitude();
                LatLng latLng = new LatLng(latitude, longitude);
                onMapClick(latLng);
                initCamera(latLng);
            }
        } catch (Exception e) {
            Log.e(TAG, "search fail", e);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

        initListeners();
    }

    private void initListeners() {
        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.setOnMarkerClickListener(MapLocationFragment.this);
                googleMap.setOnMapClickListener(MapLocationFragment.this);
                googleMap.setMyLocationEnabled(true);
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mCurrentLocation != null) {
            selectedLocation = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            onMapClick(selectedLocation);
            initCamera(mCurrentLocation);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private void initCamera(final Location location) {
        final CameraPosition position = CameraPosition.builder()
                .target(new LatLng(location.getLatitude(),
                        location.getLongitude()))
                .zoom(16f)
                .bearing(0.0f)
                .tilt(0.0f)
                .build();
        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), null);
//                googleMap.setTrafficEnabled(true);
            }
        });
    }

    private void initCamera(final LatLng location) {
        final CameraPosition position = CameraPosition.builder()
                .target(location)
                .zoom(16f)
                .bearing(0.0f)
                .tilt(0.0f)
                .build();
        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), null);
//                googleMap.setTrafficEnabled(true);
            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        String address = getAddressFromLatLng(latLng);
        final MarkerOptions options = new MarkerOptions().position(latLng);
        options.title(address);

        options.icon(BitmapDescriptorFactory.defaultMarker());
        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.clear();
                googleMap.addMarker(options);
            }
        });
        selectedLocation = latLng;
    }

    private String getAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(getActivity());
        String address = "";
        try {
            address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1).get(0).getAddressLine(0);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return address;
    }

    public LatLng getSelectedLocation() {
        return selectedLocation;
    }
}
