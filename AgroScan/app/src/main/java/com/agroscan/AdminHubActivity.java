package com.agroscan;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.agroscan.adapters.UserAdapter;
import com.agroscan.models.User;
import com.agroscan.utils.SharedPrefsHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AdminHubActivity extends AppCompatActivity {

    private TextView tvUserCount;
    private ListView lvUsers;
    private UserAdapter adapter;
    private List<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_hub);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvUserCount = findViewById(R.id.tvUserCount);
        lvUsers = findViewById(R.id.lvUsers);

        adapter = new UserAdapter(this, users);
        lvUsers.setAdapter(adapter);

        registerForContextMenu(lvUsers);

        findViewById(R.id.btnGoHome).setOnClickListener(v ->
                startActivity(new Intent(this, HomeActivity.class)));

        loadUsers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUsers();
    }

    private void loadUsers() {
        users.clear();
        Set<String> usernames = SharedPrefsHelper.getUsernameSet(this);
        for (String username : usernames) {
            users.add(new User(
                    username,
                    SharedPrefsHelper.getFullName(this, username),
                    SharedPrefsHelper.getEmail(this, username),
                    SharedPrefsHelper.isAdmin(this, username)
            ));
        }
        adapter.notifyDataSetChanged();
        tvUserCount.setText(String.valueOf(users.size()));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.lvUsers) {
            getMenuInflater().inflate(R.menu.menu_context_user, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        User selectedUser = users.get(info.position);

        if (item.getItemId() == R.id.ctx_delete_user) {
            // Prevent deleting admin
            if (selectedUser.isAdmin) {
                Toast.makeText(this, "No se puede eliminar al administrador.", Toast.LENGTH_SHORT).show();
                return true;
            }
            new AlertDialog.Builder(this)
                    .setTitle("Eliminar usuario")
                    .setMessage("¿Estás seguro de eliminar a " + selectedUser.fullName + "?")
                    .setPositiveButton("Eliminar", (d, w) -> {
                        SharedPrefsHelper.deleteUser(this, selectedUser.username);
                        Toast.makeText(this, "Usuario eliminado.", Toast.LENGTH_SHORT).show();
                        loadUsers();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
            return true;
        }
        return super.onContextItemSelected(item);
    }
}
