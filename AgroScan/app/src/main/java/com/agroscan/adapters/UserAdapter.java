package com.agroscan.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.agroscan.R;
import com.agroscan.models.User;
import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {

    public UserAdapter(Context context, List<User> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_user, parent, false);
        }
        TextView tvInitials = convertView.findViewById(R.id.tvUserInitials);
        TextView tvName = convertView.findViewById(R.id.tvUserName);
        TextView tvEmail = convertView.findViewById(R.id.tvUserEmail);

        tvName.setText(user.fullName);
        tvEmail.setText(user.email);

        // Generate initials from full name
        String[] parts = user.fullName.split(" ");
        String initials = parts.length >= 2
                ? String.valueOf(parts[0].charAt(0)) + String.valueOf(parts[1].charAt(0))
                : user.fullName.substring(0, Math.min(2, user.fullName.length())).toUpperCase();
        tvInitials.setText(initials);
        return convertView;
    }
}
