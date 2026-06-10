package com.agroscan;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.agroscan.adapters.PlantAdapter;
import com.agroscan.models.PlantRecord;
import com.agroscan.utils.SharedPrefsHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HomeActivity extends AppCompatActivity {

    private TextView tvWelcome, tvTotalScans, tvHealthyPlants, tvDiseases, tvNoScans;
    private ListView lvScans;
    private FloatingActionButton fabScan;
    private PlantAdapter adapter;
    private List<PlantRecord> records = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvWelcome = findViewById(R.id.tvWelcome);
        tvTotalScans = findViewById(R.id.tvTotalScans);
        tvHealthyPlants = findViewById(R.id.tvHealthyPlants);
        tvDiseases = findViewById(R.id.tvDiseases);
        tvNoScans = findViewById(R.id.tvNoScans);
        lvScans = findViewById(R.id.lvScans);
        fabScan = findViewById(R.id.fabScan);

        String username = SharedPrefsHelper.getSession(this);
        String fullName = SharedPrefsHelper.getFullName(this, username);
        tvWelcome.setText(getString(R.string.welcome) + fullName + " 👋");

        adapter = new PlantAdapter(this, records);
        lvScans.setAdapter(adapter);

        lvScans.setOnItemClickListener((parent, view, position, id) -> {
            PlantRecord record = records.get(position);
            Intent intent = new Intent(this, RecordDetailActivity.class);
            intent.putExtra("scan_id", record.scanId);
            startActivity(intent);
        });

        fabScan.setOnClickListener(v ->
                startActivity(new Intent(this, ScanActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        records.clear();
        Set<String> scanIds = SharedPrefsHelper.getScanIds(this);
        for (String id : scanIds) {
            records.add(new PlantRecord(
                    id,
                    SharedPrefsHelper.getScanField(this, id, "plant"),
                    SharedPrefsHelper.getScanField(this, id, "crop"),
                    SharedPrefsHelper.getScanField(this, id, "symptoms"),
                    SharedPrefsHelper.getScanField(this, id, "diagnosis"),
                    SharedPrefsHelper.getScanField(this, id, "date"),
                    SharedPrefsHelper.isScanHealthy(this, id)
            ));
        }

        adapter.notifyDataSetChanged();
        tvTotalScans.setText(String.valueOf(SharedPrefsHelper.getTotalScans(this)));
        tvHealthyPlants.setText(String.valueOf(SharedPrefsHelper.getHealthyCount(this)));
        tvDiseases.setText(String.valueOf(SharedPrefsHelper.getDiseaseCount(this)));
        tvNoScans.setVisibility(records.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_logout) {
            SharedPrefsHelper.clearSession(this);
            startActivity(new Intent(this, LoginActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            return true;
        } else if (id == R.id.menu_about) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.app_name))
                    .setMessage(getString(R.string.about_text))
                    .setPositiveButton("OK", null)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
