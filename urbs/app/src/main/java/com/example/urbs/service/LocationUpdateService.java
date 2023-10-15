package com.example.urbs.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

public class LocationUpdateService extends Service {
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    @Override
    public void onCreate() {
        super.onCreate();
        // Inicialize o FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Configurar o LocationRequest para solicitar atualizações de localização
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000) // Intervalo em milissegundos (10 segundos)
                .setFastestInterval(5000); // Intervalo mais rápido em milissegundos (5 segundos)

        // Configurar o LocationCallback para lidar com as atualizações de localização
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // A localização do usuário está disponível em "location"
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    // Faça algo com a localização em tempo real
                }
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Inicie a solicitação de atualizações de localização aqui
        startLocationUpdates();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startLocationUpdates() {
        // Inicie as atualizações de localização usando o LocationCallback
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void stopLocationUpdates() {
        // Pare as atualizações de localização quando necessário
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }
}
