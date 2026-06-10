package com.agroscan.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.agroscan.R;
import com.agroscan.models.PlantRecord;
import java.util.List;

public class PlantAdapter extends ArrayAdapter<PlantRecord> {

    public PlantAdapter(Context context, List<PlantRecord> records) {
        super(context, 0, records);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlantRecord record = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_plant, parent, false);
        }
        TextView tvName = convertView.findViewById(R.id.tvItemPlantName);
        TextView tvDate = convertView.findViewById(R.id.tvItemDate);
        TextView tvStatus = convertView.findViewById(R.id.tvItemStatus);

        tvName.setText(record.plantName);
        tvDate.setText(record.date);

        if (record.isHealthy) {
            tvStatus.setText("Sana");
            tvStatus.setTextColor(0xFF2E7D32);
            tvStatus.setBackgroundResource(R.drawable.tag_green);
        } else {
            tvStatus.setText("Enferma");
            tvStatus.setTextColor(0xFFC62828);
            tvStatus.setBackgroundResource(R.drawable.tag_red);
        }
        return convertView;
    }
}
