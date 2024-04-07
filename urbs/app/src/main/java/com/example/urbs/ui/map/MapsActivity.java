package com.example.urbs.ui.map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;

import com.example.urbs.data.model.LineResponse;
import com.example.urbs.data.model.ShapeResponse;
import com.example.urbs.data.model.StopResponse;
import com.example.urbs.location.BootReceiver;
import com.example.urbs.service.ApiManager;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

import com.example.urbs.R;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ApiManager apiManager;
    private GoogleMap mMap;
    private Location userLocation;
    private static final int PERMISSIONS_REQUEST_LOCATION = 1;
    private FusedLocationProviderClient fusedLocationClient;

    private List<ShapeResponse> shapeList;
    private List<StopResponse> stopList;
    private LineResponse line;

    List<LatLng> routePoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        shapeList = getIntent().getParcelableArrayListExtra("shape");
        stopList = getIntent().getParcelableArrayListExtra("stops");
        line = getIntent().getParcelableExtra("line");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        AppCompatActivity activity = (AppCompatActivity) mapFragment.getActivity();

        if (activity != null) {
            activity.setTitle("Linha - " + line.getNOME());
        }


//        BootReceiver bootReceiver = new BootReceiver();
//        IntentFilter filter = new IntentFilter("android.intent.action.BOOT_COMPLETED");
//        registerReceiver(bootReceiver, filter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // Ação quando a seta de retorno é pressionada
            onBackPressed(); // Isso pode ser usado para fechar a SegundaActivity e voltar para a PrimeiraActivity
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        mMap = googleMap;
        // Solicitar permissões de localização em tempo de execução
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
        } else {
            showMyLocalization();
        }
        drawRoute();
        addMarkersToMap();

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                focusMapRegion();
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // Mostrar info window ao clicar no marcador
                marker.showInfoWindow();
                return true;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_LOCATION) {
            // Verifique se a resposta de permissão foi concedida ou negada
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissão concedida, execute a ação desejada
                showMyLocalization();
            } else {
                // Permissão negada, lide com isso conforme necessário
            }
        }
    }


    public void showMyLocalization() {
        mMap.setMyLocationEnabled(true);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000)
                .setFastestInterval(5000);

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    userLocation = location;
                    // Faça algo com a localização em tempo real
                    LatLng userLatLng = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(userLatLng));
                }
            }
        };

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);

    }

    private void drawRoute() {
        if (mMap != null && shapeList != null) {
            PolylineOptions polylineOptions = new PolylineOptions();
            routePoints = new ArrayList<>();
            polylineOptions.color(Color.GRAY);
            for (ShapeResponse shapeResponse : shapeList) {
                List<Double> coord = shapeResponse.getCoord();
                LatLng latLng = new LatLng(coord.get(1), coord.get(0));
                polylineOptions.add(latLng);
                routePoints.add(latLng);
            }
            Polyline polyline = mMap.addPolyline(polylineOptions);
        }
    }

    private void focusMapRegion() {
        if (routePoints.isEmpty()) {
            Log.e("MapsActivity", "Route points list is empty");
            return;
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng point : routePoints) {
            builder.include(point);
        }

        LatLngBounds bounds = builder.build();
        int padding = 100; // padding em pixels

        // Ajusta o foco do mapa para a região especificada
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cu);
    }

    private void addMarkersToMap() {
        if (mMap != null && stopList != null) {
            for (StopResponse stop : stopList) {
                List<Object> coord = stop.getCoord();
                double latitude = (double) coord.get(1);
                double longitude = (double) coord.get(0);
                LatLng latLng = new LatLng(latitude, longitude);

                // Adicione um marcador personalizado com nome e sentido como snippet
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng)
                        .title(stop.getNome()) // Define o nome da parada como título do marcador
                        .snippet("Sentido: " + stop.getSentido()); // Define o sentido da parada como snippet do marcador

                mMap.addMarker(markerOptions);
            }
        }
    }
}
