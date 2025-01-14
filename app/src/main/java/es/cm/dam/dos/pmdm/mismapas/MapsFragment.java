package es.cm.dam.dos.pmdm.mismapas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapsFragment extends Fragment {

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            ArrayList<Parada> paradas = loadParadas();
            if (paradas == null || paradas.isEmpty()) {
                Toast.makeText(getContext(), "No hay paradas disponibles", Toast.LENGTH_SHORT).show();
                return;
            }
            // Inicializar Builder para LatLngBounds y opciones de Polyline
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            PolylineOptions poly = new PolylineOptions();

            // Añadir marcadores y construir la ruta
            for (Parada p: paradas){
                try {
                    LatLng latLng = new LatLng(Double.parseDouble(p.getLatitud()), Double.parseDouble(p.getLongitud()));
                    googleMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(p.getDireccion())
                            .snippet(p.getHora()));
                    builder.include(latLng);
                    poly.add(latLng);
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Coordenadas inválidas en una parada", Toast.LENGTH_SHORT).show();
                }
            }
            // Añadir la Polyline al mapa
            googleMap.addPolyline(poly);

            // Crear LatLngBounds solo si hay suficientes puntos
            try {
                LatLngBounds bounds = builder.build();
                //Reestablecer los límites del mapa porque está produciendo límites incorrectos.
                LatLngBounds limites = new LatLngBounds(bounds.southwest, bounds.northeast);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(limites, 100));
            } catch (IllegalStateException e) {
                Toast.makeText(getContext(), "No se pudo ajustar la vista, verifica las coordenadas", Toast.LENGTH_SHORT).show();
            }
//            LatLng sydney = new LatLng(-34, 151);
//            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }

        private ArrayList<Parada> loadParadas() {
            ArrayList<Parada> paradas = new ArrayList<>();
            paradas.add(new Parada("40.960780", "-5.663360", "Plaza Mayor, Salamanca", "08:00"));
            paradas.add(new Parada("40.960463", "-5.662917", "Plaza del Corrillo, Salamanca", "08:30"));
            paradas.add(new Parada("40.960898", "-5.665682", "Iglesia de San Martín, Salamanca", "09:00"));
            paradas.add(new Parada("40.963129", "-5.665438", "Casa de las Conchas, Salamanca", "09:30"));
            paradas.add(new Parada("40.962595", "-5.666522", "Catedral Nueva, Salamanca", "10:00"));
            paradas.add(new Parada("40.961378", "-5.667743", "Universidad de Salamanca, Salamanca", "10:15"));
            paradas.add(new Parada("40.962042", "-5.668279", "Huerto de Calixto y Melibea, Salamanca", "10:45"));
            paradas.add(new Parada("40.964556", "-5.665352", "Puente Romano, Salamanca", "11:00"));
            paradas.add(new Parada("40.961813", "-5.668820", "Convento de San Esteban, Salamanca", "11:30"));
            paradas.add(new Parada("40.960227", "-5.662552", "Torre del Clavero, Salamanca", "12:00"));
            return paradas;
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}