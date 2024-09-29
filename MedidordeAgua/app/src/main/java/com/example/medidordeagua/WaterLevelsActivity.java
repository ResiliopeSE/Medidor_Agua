package com.example.medidordeagua;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WaterLevelsActivity extends AppCompatActivity {

    private ListView listViewWaterLevels;
    private Button btnBack;
    private DatabaseReference databaseReference;
    private List<String> waterLevelList;

    // Definir el radio del contenedor
    private static final double CONTAINER_RADIUS = 9.0; // en cm
    private static final double CONTAINER_HEIGHT = 10.0; // en cm

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_levels);

        listViewWaterLevels = findViewById(R.id.listViewWaterLevels);
        btnBack = findViewById(R.id.btnBack);
        waterLevelList = new ArrayList<>();

        // Inicializar Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("waterLevels");

        // Leer los datos de nivel de agua de Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                waterLevelList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String timestamp = snapshot.child("timestamp").getValue(String.class);
                    Integer waterLevel = snapshot.child("distance").getValue(Integer.class);
                    if (timestamp != null && waterLevel != null) {
                        // Llamar a calculateVolume con tres argumentos: radio, altura del contenedor, y distancia
                        double volume = VolumeCalculator.calculateVolume(CONTAINER_RADIUS, CONTAINER_HEIGHT, waterLevel);
                        waterLevelList.add("Timestamp: " + timestamp + "\nVolumen: " + String.format("%.1f", volume) + " ml");
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(WaterLevelsActivity.this, android.R.layout.simple_list_item_1, waterLevelList);
                listViewWaterLevels.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar errores
                Toast.makeText(WaterLevelsActivity.this, "Error al leer los datos de Firebase", Toast.LENGTH_SHORT).show();
            }
        });

        // Volver a la actividad principal
        btnBack.setOnClickListener(v -> finish());
    }
}
