package com.agroscan;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import com.agroscan.utils.MLKitHelper;
import com.agroscan.utils.SharedPrefsHelper;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ScanActivity extends AppCompatActivity {

    private static final int REQ_CAMERA = 100;

    private EditText etPlantName, etCropType, etSymptoms;
    private ImageView ivPreview;
    private Button btnCamera, btnGallery, btnAnalyze;
    private LinearLayout layoutProgress;
    private Uri photoUri;

    private final ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && photoUri != null) {
                    ivPreview.setImageURI(photoUri);
                    ivPreview.setAlpha(1f);
                    ivPreview.setPadding(0, 0, 0, 0);
                }
            });

    private final ActivityResultLauncher<Intent> galleryLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    photoUri = result.getData().getData();
                    ivPreview.setImageURI(photoUri);
                    ivPreview.setAlpha(1f);
                    ivPreview.setPadding(0, 0, 0, 0);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        etPlantName = findViewById(R.id.etPlantName);
        etCropType = findViewById(R.id.etCropType);
        etSymptoms = findViewById(R.id.etSymptoms);
        ivPreview = findViewById(R.id.ivPreview);
        btnCamera = findViewById(R.id.btnCamera);
        btnGallery = findViewById(R.id.btnGallery);
        btnAnalyze = findViewById(R.id.btnAnalyze);
        layoutProgress = findViewById(R.id.layoutProgress);

        btnCamera.setOnClickListener(v -> openCamera());
        btnGallery.setOnClickListener(v -> openGallery());
        btnAnalyze.setOnClickListener(v -> startAnalysis());
    }

    private void openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, REQ_CAMERA);
            return;
        }
        try {
            File photoFile = createImageFile();
            photoUri = FileProvider.getUriForFile(this,
                    getPackageName() + ".fileprovider", photoFile);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            cameraLauncher.launch(intent);
        } catch (IOException e) {
            Toast.makeText(this, "Error al abrir la cámara.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(intent);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                .format(new Date());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile("AGRO_" + timeStamp, ".jpg", storageDir);
    }

    private void startAnalysis() {
        String plantName = etPlantName.getText().toString().trim();
        String cropType = etCropType.getText().toString().trim();
        String symptoms = etSymptoms.getText().toString().trim();

        if (plantName.isEmpty()) {
            etPlantName.setError("Ingresa el nombre de la planta");
            return;
        }

        btnAnalyze.setEnabled(false);
        btnAnalyze.setText("Analizando...");
        layoutProgress.setVisibility(View.VISIBLE);

        // Simulate ML Kit processing with Handler delay (as specified in project)
        new Handler().postDelayed(() -> {
            MLKitHelper.DiagnosisResult result = MLKitHelper.analyze(plantName);

            String date = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                    .format(new Date());

            String scanId = SharedPrefsHelper.saveScan(this,
                    plantName, cropType, symptoms, result.diseaseName, date);

            // Also save first history entry automatically
            SharedPrefsHelper.saveHistory(this, scanId,
                    result.diseaseName, result.treatment,
                    result.description + "\nCausas: " + result.causes, date);

            layoutProgress.setVisibility(View.GONE);
            btnAnalyze.setEnabled(true);
            btnAnalyze.setText(getString(R.string.btn_analyze));

            Intent intent = new Intent(this, RecordDetailActivity.class);
            intent.putExtra("scan_id", scanId);
            startActivity(intent);
            finish();
        }, 2500);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_CAMERA &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        }
    }
}
