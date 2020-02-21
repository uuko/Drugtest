package com.example.drugtest.map;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drugtest.R;
import com.example.drugtest.main.MainActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.net.PlacesClient;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.OnConnectionFailedListener {

    private PlaceAutocompleteAdapter mplaceAutocompleteAdapter;
    private ImageView mGps;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleClient;
    private final LatLngBounds LAT_LNG_BOUNDS=new LatLngBounds(
            new LatLng(-40,-168),new LatLng(71,136)
    );
    private  static  final  String FINAL_LOCATION=Manifest.permission.ACCESS_FINE_LOCATION;
    private  static  final String COURSE_LOCATION=Manifest.permission.ACCESS_COARSE_LOCATION;
    private Boolean mlocationPermissionGranted=false;
    private final int LOCATION_PERMISSION_REQUEST_CODE=1;
    private FusedLocationProviderClient mFusedLocationClient;
    private final  float DEFAUT_ZOOM=15f;
    private AutoCompleteTextView mSearchText;
    private Address address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mSearchText=findViewById(R.id.input_search);
        mGps=findViewById(R.id.ic_gps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        getLocalPermisuion();
        // Initialize Places.


    }

    private void init(){

        mGoogleClient=new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this,this)
                .build();
//        mplaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this,mGoogleClient,LAT_LNG_BOUNDS,null);
//        mplaceAutocompleteAdapter=new PlaceAutocompleteAdapter(this,mGoogleClient,
//                LAT_LNG_BOUNDS,null);
//        mplaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this,Places.getGeoDataClient(this, null),LAT_LNG_BOUNDS,null);
//        mSearchText.setAdapter(mplaceAutocompleteAdapter);
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
               if (actionId == EditorInfo.IME_ACTION_SEARCH
               ||actionId==EditorInfo.IME_ACTION_DONE
               || event.getAction() == KeyEvent.ACTION_DOWN
               || event.getAction() == KeyEvent.KEYCODE_ENTER){
                   geoLocate();
               }
                return false;
            }
        });
        HideKeyboard();
        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceLocation();
            }
        });
    }

    private  void  geoLocate(){
        String searchString=mSearchText.getText().toString();
        Geocoder geocoder=new Geocoder(MapsActivity.this);
        List<Address> list=new ArrayList<>();
        try {
            list=geocoder.getFromLocationName(searchString,1);
        }catch (IOException e){
            Log.d("333", "geoLocate: ");
        }

        if (list.size() > 0){
             address=list.get(0);
            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),DEFAUT_ZOOM,
                    address.getAddressLine(0));
            Log.d("333", "geoLocate: "+address.toString());
        }
    }
    private void  initMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    private  void  getLocalPermisuion(){
        String[] permissions={Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                mlocationPermissionGranted=true;
                initMap();
            }

        }
        else {
            ActivityCompat.requestPermissions(this,permissions
                    ,LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
    private  void  getDeviceLocation(){
        mFusedLocationClient= LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mlocationPermissionGranted){
                Task location=mFusedLocationClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()){
                            Location currentLocation=(Location) task.getResult();
//                            mMap
                            moveCamera(new LatLng(currentLocation.getLatitude(),
                                    currentLocation.getLongitude()),DEFAUT_ZOOM,"My location");
                        }else {
                            Toast.makeText(MapsActivity.this, "err get location now ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }catch (SecurityException e){
            Log.d("88", "updateLocation: "+e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng,float zoom,String title){
        Log.d("333", "moveCamera: "+latLng.latitude+
                "llllll"+latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        if (!title.equals("My location")){
            MarkerOptions options=new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mMap.addMarker(options);
        }
        HideKeyboard();

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case  LOCATION_PERMISSION_REQUEST_CODE :{
                for (int i=0;i<grantResults.length;i++){
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                        mlocationPermissionGranted=false;
                        break;
                    }
                }
                    mlocationPermissionGranted=true;
                    initMap();
            }

        }
    }


    private  void HideKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "map is Ready", Toast.LENGTH_SHORT).show();
        mMap = googleMap;

        if (mlocationPermissionGranted){
            getDeviceLocation();
            mMap.setMyLocationEnabled(true);
            //mMap.getUiSettings().setMyLocationButtonEnabled(false);
            init();
        }
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
