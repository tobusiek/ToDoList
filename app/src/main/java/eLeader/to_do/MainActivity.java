package eLeader.to_do;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import eLeader.to_do.Database.DBPosition;
import eLeader.to_do.Database.TasksDatabase;
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

        db = TasksDatabase.getInstance(getApplicationContext());

        if (db.positionDAO().size() == 0) binding.empty.setVisibility(TextView.VISIBLE);

        if (db.positionDAO().size() > 0) {
            binding.empty.setVisibility(TextView.INVISIBLE);
            updateRV();
        }

        FloatingActionButton addNewTaskFAB = binding.addTaskFAB;
        addNewTaskFAB.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddNewTaskActivity.class));
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
        AlertDialog.Builder builderMain = new AlertDialog.Builder(this);
        builderMain.setTitle("Wybrano zadanie \"" + taskList.get(index).getTask_name() + "\"");
        builderMain.setMessage("Wybierz co chcesz zrobić z zadaniem:");
        builderMain.setPositiveButton("Edytować", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this, EditTaskActivity.class);
                intent.putExtra("id", taskList.get(index).get_id());
                intent.putExtra("taskName", taskList.get(index).getTask_name());
                intent.putExtra("taskDate", taskList.get(index).getTask_date());
                intent.putExtra("taskCat", taskList.get(index).getTask_cat());
                startActivity(intent);
            }
        });
        builderMain.setNegativeButton("Usunąć", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog.Builder builderDelete = new AlertDialog.Builder(MainActivity.this);
                builderDelete.setTitle("Usuwanie zadania");
                builderDelete.setMessage("Czy chcesz usunąć zadanie " + taskList.get(index).getTask_name() + "?");
                builderDelete.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
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
                builderDelete.setNegativeButton("Nie", (dialog1, which1) -> {});

                AlertDialog dialogDelete = builderDelete.create();
                dialogDelete.show();
            }
        });
        builderMain.setNeutralButton("Nic", (dialog, which) -> {});

        AlertDialog dialogMain = builderMain.create();
        dialogMain.show();

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