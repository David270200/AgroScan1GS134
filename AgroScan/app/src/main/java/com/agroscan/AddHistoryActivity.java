package com.agroscan;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.agroscan.utils.SharedPrefsHelper;

public class AddHistoryActivity extends AppCompatActivity {

    private EditText etDisease, etTreatment, etObservations, etDate;
    private Button btnSave, btnCancel;
    private TextView tvPlantLabel;
    private String scanId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_history);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        scanId = getIntent().getStringExtra("scan_id");

        tvPlantLabel = findViewById(R.id.tvPlantLabel);
        etDisease = findViewById(R.id.etDisease);
        etTreatment = findViewById(R.id.etTreatment);
        etObservations = findViewById(R.id.etObservations);
        etDate = findViewById(R.id.etDate);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        // Show plant name context
        if (scanId != null) {
            String plantName = SharedPrefsHelper.getScanField(this, scanId, "plant");
            tvPlantLabel.setText("🌱  Planta: " + plantName);
        }

        // Default date to today
        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
        etDate.setText(sdf.format(new java.util.Date()));

        btnSave.setOnClickListener(v -> saveHistory());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void saveHistory() {
        String disease = etDisease.getText().toString().trim();
        String treatment = etTreatment.getText().toString().trim();
        String observations = etObservations.getText().toString().trim();
        String date = etDate.getText().toString().trim();

        if (disease.isEmpty() || treatment.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_empty_fields), Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPrefsHelper.saveHistory(this, scanId, disease, treatment, observations, date);
        Toast.makeText(this, getString(R.string.success_saved), Toast.LENGTH_SHORT).show();
        finish();
    }
}
