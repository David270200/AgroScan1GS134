package com.agroscan.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.agroscan.R;
import com.agroscan.models.HistoryEntry;
import java.util.List;

public class HistoryAdapter extends ArrayAdapter<HistoryEntry> {

    public HistoryAdapter(Context context, List<HistoryEntry> entries) {
        super(context, 0, entries);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HistoryEntry entry = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_history, parent, false);
        }
        TextView tvDisease = convertView.findViewById(R.id.tvHistoryDisease);
        TextView tvDate = convertView.findViewById(R.id.tvHistoryDate);
        TextView tvStatus = convertView.findViewById(R.id.tvHistoryStatus);

        tvDisease.setText(entry.disease);
        tvDate.setText(entry.date);
        tvStatus.setText("Activo");
        return convertView;
    }
}
