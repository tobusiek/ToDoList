package eLeader.to_do;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import eLeader.to_do.databinding.MainActivityBinding;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.ItemClicked {
    private MainActivityBinding binding;
    private TasksDatabase db;
    public RecyclerView.Adapter adapter;
    private List<DBPosition> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = MainActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = Room.databaseBuilder(getApplicationContext(), TasksDatabase.class,
                TasksDatabase.DB_NAME).allowMainThreadQueries().build();

        if (db.positionDAO().size() == 0) binding.empty.setVisibility(TextView.VISIBLE);

        if (db.positionDAO().size() > 0) {
            binding.empty.setVisibility(TextView.INVISIBLE);
            updateRV();
        }

        FloatingActionButton addNewTaskFAB = binding.addTaskFAB;
        addNewTaskFAB.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, TaskActivity.class));
            Toast.makeText(this, "Dodawanie nowego zadania", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateRV();
    }

    @Override
    public void onItemClicked(int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Usuwanie zadania");
        builder.setMessage("Czy chcesz usunąć zadanie " + taskList.get(index).getTask_name() + "?");
        builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DBPosition dbPosition = taskList.get(index);
                db.positionDAO().delete(dbPosition);
                taskList.remove(index);
                adapter.notifyDataSetChanged();
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });
        builder.setNegativeButton("Nie", (dialog, which) -> {});

        AlertDialog dialog = builder.create();
        dialog.show();

        updateRV();
    }

    public void updateRV() {
        taskList = db.positionDAO().getTaskList();
        if (taskList.size() > 0) {
            adapter = new RecyclerViewAdapter(MainActivity.this, taskList);

            RecyclerView rvList = binding.rvList;
            rvList.setHasFixedSize(true);
            rvList.setLayoutManager(new LinearLayoutManager(this));
            rvList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            binding.empty.setVisibility(TextView.INVISIBLE);
        } else binding.empty.setVisibility(TextView.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        updateRV();
    }
}