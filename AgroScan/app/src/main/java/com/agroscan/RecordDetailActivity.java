package com.agroscan;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.agroscan.adapters.HistoryAdapter;
import com.agroscan.models.HistoryEntry;
import com.agroscan.utils.SharedPrefsHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RecordDetailActivity extends AppCompatActivity {

    private TextView tvPlantName, tvCropInfo, tvDiagnosisTag;
    private TextView tvDiagnosisTitle, tvDescription, tvCauses, tvTreatment;
    private ListView lvHistory;
    private Button btnAddHistory;
    private HistoryAdapter adapter;
    private List<HistoryEntry> historyEntries = new ArrayList<>();
    private String scanId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        scanId = getIntent().getStringExtra("scan_id");

        tvPlantName = findViewById(R.id.tvPlantName);
        tvCropInfo = findViewById(R.id.tvCropInfo);
        tvDiagnosisTag = findViewById(R.id.tvDiagnosisTag);
        tvDiagnosisTitle = findViewById(R.id.tvDiagnosisTitle);
        tvDescription = findViewById(R.id.tvDescription);
        tvCauses = findViewById(R.id.tvCauses);
        tvTreatment = findViewById(R.id.tvTreatment);
        lvHistory = findViewById(R.id.lvHistory);
        btnAddHistory = findViewById(R.id.btnAddHistory);

        adapter = new HistoryAdapter(this, historyEntries);
        lvHistory.setAdapter(adapter);
        registerForContextMenu(lvHistory);

        btnAddHistory.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddHistoryActivity.class);
            intent.putExtra("scan_id", scanId);
            startActivity(intent);
        });

        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        if (scanId == null) return;

        String plantName = SharedPrefsHelper.getScanField(this, scanId, "plant");
        String cropType = SharedPrefsHelper.getScanField(this, scanId, "crop");
        String diagnosis = SharedPrefsHelper.getScanField(this, scanId, "diagnosis");
        String date = SharedPrefsHelper.getScanField(this, scanId, "date");
        boolean isHealthy = SharedPrefsHelper.isScanHealthy(this, scanId);

        tvPlantName.setText(plantName);
        tvCropInfo.setText(cropType + " · " + date);
        tvDiagnosisTag.setText(diagnosis);

        if (isHealthy) {
            tvDiagnosisTag.setTextColor(0xFF2E7D32);
            tvDiagnosisTag.setBackgroundResource(R.drawable.tag_green);
        } else {
            tvDiagnosisTag.setTextColor(0xFFC62828);
            tvDiagnosisTag.setBackgroundResource(R.drawable.tag_red);
        }

        tvDiagnosisTitle.setText(getString(R.string.diagnosis_label) + diagnosis);

        // Load history entries for description/treatment from first entry
        Set<String> histIds = SharedPrefsHelper.getHistoryIdsForScan(this, scanId);
        historyEntries.clear();
        for (String hid : histIds) {
            historyEntries.add(new HistoryEntry(
                    hid, scanId,
                    SharedPrefsHelper.getHistoryField(this, hid, "disease"),
                    SharedPrefsHelper.getHistoryField(this, hid, "treatment"),
                    SharedPrefsHelper.getHistoryField(this, hid, "observations"),
                    SharedPrefsHelper.getHistoryField(this, hid, "date")
            ));
        }

        if (!historyEntries.isEmpty()) {
            HistoryEntry first = historyEntries.get(0);
            tvDescription.setText(first.observations);
            tvTreatment.setText(first.treatment);
            tvCauses.setText("Ver historial completo abajo");
        } else {
            tvDescription.setText("Sin descripción registrada.");
            tvTreatment.setText("Sin tratamiento registrado.");
            tvCauses.setText("—");
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.lvHistory) {
            getMenuInflater().inflate(R.menu.menu_context_history, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        HistoryEntry entry = historyEntries.get(info.position);

        if (item.getItemId() == R.id.ctx_delete_history) {
            SharedPrefsHelper.deleteHistoryEntry(this, scanId, entry.histId);
            Toast.makeText(this, "Entrada eliminada.", Toast.LENGTH_SHORT).show();
            loadData();
            return true;
        }
        return super.onContextItemSelected(item);
    }
}
