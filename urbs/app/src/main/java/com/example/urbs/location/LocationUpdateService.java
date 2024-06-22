package com.example.urbs.location;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.location.Location;
import android.util.Log;

import com.example.urbs.R;
import com.example.urbs.data.model.CoordinatorResponse;
import com.example.urbs.service.ApiManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Arrays;

public class LocationUpdateService extends Service {

    private static final int NOTIFICATION_ID = 12345; // ID da notificação em primeiro plano

    private LocationManager locationManager;
    private LocationListener locationListener;

    private ApiManager apiManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("LocationUpdateService", "Service started");

        apiManager = ApiManager.getInstance(this);

        // Configura a notificação em primeiro plano
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default", "Channel name", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        Notification notification = new NotificationCompat.Builder(this, "default")
                .setContentTitle("Location Update Service")
                .setContentText("Serviço de atualização de localização em execução")
                .setSmallIcon(R.drawable.heart_on)
                .build();

        // Inicia o serviço em primeiro plano
        startForeground(NOTIFICATION_ID, notification);

        // Inicializa o LocationManager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Configura o LocationListener para receber atualizações de localização
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {

//                String accessToken = AccessTokenManager.getInstance(context).getAccessToken();

                Log.d("LocationUpdateService", "Nova localização recebida: " + location.getLatitude() + ", " + location.getLongitude());

                CoordinatorResponse coordinator = new CoordinatorResponse(Arrays.asList(-25.441105, -49.276855));

                apiManager.saveLocation(coordinator, new ApiManager.ApiCallback<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        Log.d("LocationUpdateService", "Nova localização enviada: " + location.getLatitude() + ", " + location.getLongitude());
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.d("LocationUpdateService", "Deu Merda ");
                    }
                });

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };

        // Solicita atualizações de localização
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 0, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("LocationUpdateService", "Service destroyed");

        // Remove o LocationListener ao destruir o serviço
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
    }
}
