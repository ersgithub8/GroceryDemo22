package Fragment;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import gogrocer.tcc.MainActivity;
import gogrocer.tcc.My_Order_activity;
import gogrocer.tcc.R;

import static android.content.Context.MODE_PRIVATE;


public class Map_fragment extends Fragment {
    LatLng sydney;
    GoogleMap googleMap;
    MapView mMapView;
    LinearLayout linearLayout;

    public Map_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_map, container, false);

        mMapView = view.findViewById(R.id.map);
        linearLayout = view.findViewById(R.id.linear);

        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("location", Context.MODE_PRIVATE);
                Double lat, longi;
                lat = Double.parseDouble(sharedPreferences.getString("lat", "123"));
                longi = Double.parseDouble(sharedPreferences.getString("long", "123"));

                // For dropping a marker at a point on the Map
                sydney = new LatLng(lat, longi);
                googleMap.addMarker(new MarkerOptions().position(sydney).title("Your Location")
                        //.snippet("Marker Description")
                );
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(16).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {

                        sydney = latLng;
                        googleMap.clear();
                        googleMap.addMarker(new MarkerOptions().position(latLng).title("Your Location"));

                       // Toast.makeText(getActivity(), ""+sydney, Toast.LENGTH_SHORT).show();

                    }
                });


            }
        });

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), ""+sydney, Toast.LENGTH_SHORT).show();

                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                try {

                    List<Address> addresses = geocoder.getFromLocation(sydney.latitude, sydney.longitude, 1);
                    String final_address=null;

                    if(addresses.size()>0) {
                        //                        String city = addresses.get(0).getLocality();
//                        String state = addresses.get(0).getAdminArea();
//                        String country = addresses.get(0).getCountryName();
                        final_address = addresses.get(0).getAddressLine(0);
                    }
                    Bundle args = new Bundle();
                    Fragment fm = new Add_delivery_address_fragment();
                    args.putString("Adressy", final_address);
                    args.putString("map_link","https://maps.google.com/?q="+sydney.latitude+","+sydney.longitude);
                    fm.setArguments(args);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                            //.addToBackStack(null)
                            .commit();


                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });

        return view;
    }
    public void addMarker(LatLng latLng) {

        googleMap.clear();
        googleMap.addMarker(new MarkerOptions().position(latLng).title("Your Location"));

    }
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

}