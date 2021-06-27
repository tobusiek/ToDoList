package eLeader.to_do;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Tasks")
public class DBPosition {
    @PrimaryKey(autoGenerate = true)
    public int _id;
    public String task_name;
    public String task_date;
    public String task_cat;

    public int get_id() {
        return _id;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getTask_date() {
        return task_date;
    }

    public void setTask_date(String task_date) {
        this.task_date = task_date;
    }

    public String getTask_cat() {
        return task_cat;
    }

    public void setTask_cat(String task_cat) {
        this.task_cat = task_cat;
    }
}
