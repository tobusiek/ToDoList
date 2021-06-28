package eLeader.to_do;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;

import eLeader.to_do.Database.DBPosition;
import eLeader.to_do.Database.TasksDatabase;
import eLeader.to_do.databinding.ActivityTaskBinding;

public class AddNewTaskActivity extends AppCompatActivity {
    private ActivityTaskBinding binding;

    private TasksDatabase db;

    private DatePickerDialog datePickerDialog;
    private Button dateButton;

    private String taskDate;
    private String taskCat;

    public AddNewTaskActivity() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Dodaj nowe zadanie");

        initDatePicker();
        dateButton = binding.taskDateButton;
        dateButton.setText(getTodaysDate());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Categories, android.R.layout.simple_dropdown_item_1line);
        binding.spinnerTaskCat.setAdapter(adapter);
        binding.spinnerTaskCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                taskCat = parent.getItemAtPosition(position).toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                taskCat = "Inne";
            }
        });

        db = TasksDatabase.getInstance(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String taskName = binding.taskNameEditText.getText().toString().trim();
        taskDate = binding.taskDateButton.getText().toString().trim();

        switch (item.getItemId()) {
            case R.id.saveMenuItem:
                if (taskName.equals("") || taskDate.equals("") || taskCat.equals(""))
                    Toast.makeText(this, "Proszę wprowadzić wszystkie dane", Toast.LENGTH_LONG).show();
                else {
                    DBPosition dbPosition = new DBPosition();
                    dbPosition.setTask_name(taskName);
                    dbPosition.setTask_date(taskDate);
                    dbPosition.setTask_cat(taskCat);
                    db.positionDAO().insert(dbPosition);

                    if (!db.positionDAO().checkIfTaskAdded(dbPosition.getTask_name(),
                            dbPosition.getTask_date(), dbPosition.getTask_cat())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("Dodawanie zadania nie powiodło się. Czy chcesz spróbować dodać zadanie ponownie?");
                        builder.setPositiveButton("Tak", (dialog, which) -> {});
                        builder.setNegativeButton("Nie", (dialog, which) -> AddNewTaskActivity.this.finish());

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else AddNewTaskActivity.this.finish();
                }
                break;

            case R.id.cancelMenuItem:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Czy chcesz anulować dodawanie zadania?");
                builder.setPositiveButton("Tak", (dialog, which) -> AddNewTaskActivity.this.finish());
                builder.setNegativeButton("Nie", (dialog, which) -> {});

                AlertDialog dialog = builder.create();
                dialog.show();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            month += 1;
            taskDate = makeDateString(dayOfMonth, month, year);
            dateButton.setText(taskDate);
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);
    }

    private String makeDateString(int dayOfMonth, int month, int year) {
        StringBuilder stringBuilder = new StringBuilder();

        if (dayOfMonth < 10) stringBuilder.append("0").append(dayOfMonth).append("-");
        else stringBuilder.append(dayOfMonth).append("-");

        if (month < 10) stringBuilder.append(0).append(month).append("-").append(year);
        else stringBuilder.append(month).append("-").append(year);

        return stringBuilder.toString();
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    public String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return makeDateString(day, month, year);
    }
}