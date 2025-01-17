package es.cm.dam.dos.pmdm.mismapas;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Granularity;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Location ultimaUbicacion;
    private FusedLocationProviderClient mFusedLocationClient;
    private int FINE_LOCATION_REQUEST_CODE = 101;
    private Double longitud,latitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        mFusedLocationClient= LocationServices.getFusedLocationProviderClient(
                getApplicationContext());

        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION },
                    FINE_LOCATION_REQUEST_CODE);
        }else {
            solicitaUbicacion();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==FINE_LOCATION_REQUEST_CODE && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            solicitaUbicacion();
        }
    }

    private void solicitaUbicacion(){
        // Configurar la solicitud de ubicación
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,
                10000)
                .setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
                .setDurationMillis(10000)
                .build();

        // Verificar permisos
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Iniciar actualizaciones de ubicación, se indica la callback que manejará las actualizaciones de
        // ubicación y que las actualizaciones deben ser manejadas en el hilo principal
        mFusedLocationClient.requestLocationUpdates(locationRequest,mLocationCallback, Looper.getMainLooper());
        // Obtener última ubicación conocida. En caso de éxito se llama a la rutina onSuccess
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, locationOnSuccessListener);
    }

    OnSuccessListener<Location> locationOnSuccessListener = new OnSuccessListener<Location>() {
        @Override
        public void onSuccess(Location location) {
            if(location!=null) {
                latitud=location.getLatitude();
                longitud=location.getLongitude();
                updateIU();
            }
        }
    };

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (!locationList.isEmpty()) {
                Location location = locationList.get(locationList.size() - 1);
                ultimaUbicacion = location;
                latitud=location.getLatitude();
                longitud=location.getLongitude();
                updateIU();
                if (mFusedLocationClient != null) {
                    mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                }
            }
        }
    };

    private void updateIU(){
        TextView txtUbicacion=findViewById(R.id.txtUbicacion);
        txtUbicacion.setText(getString(R.string.longitud)+longitud+";"+
                            getString(R.string.latitud)+latitud);
    }
}