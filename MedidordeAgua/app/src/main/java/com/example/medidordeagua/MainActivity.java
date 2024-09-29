package com.example.medidordeagua;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private TextView tvWaterLevel;
    private ProgressBar pbWaterLevel;
    private Button btnViewAllLevels;
    private DatabaseReference databaseReference;

    // Definir el radio y la altura del contenedor
    private static final double CONTAINER_RADIUS = 6.0; // en cm
    private static final double CONTAINER_HEIGHT = 7.0; // en cm

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvWaterLevel = findViewById(R.id.tvWaterLevel);
        pbWaterLevel = findViewById(R.id.pbWaterLevel);
        btnViewAllLevels = findViewById(R.id.btnViewAllLevels);

        // Inicializar Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("latestWaterLevel");

        // Leer los datos del nivel de agua de Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Integer waterLevel = dataSnapshot.child("distance").getValue(Integer.class);
                    if (waterLevel != null) {
                        // Llamar a calculateVolume con tres argumentos: radio, altura del contenedor, y distancia
                        double volume = VolumeCalculator.calculateVolume(CONTAINER_RADIUS, CONTAINER_HEIGHT, waterLevel);
                        tvWaterLevel.setText("Volumen de Agua: " + String.format("%.1f", volume) + " ml");
                        pbWaterLevel.setProgress((int) volume);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar errores
                Toast.makeText(MainActivity.this, "Error al leer los datos de Firebase", Toast.LENGTH_SHORT).show();
            }
        });

        // Navegar a la vista de todos los niveles de agua
        btnViewAllLevels.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, WaterLevelsActivity.class);
            startActivity(intent);
        });
    }
}
