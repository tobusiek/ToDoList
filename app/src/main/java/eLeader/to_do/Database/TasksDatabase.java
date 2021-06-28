package eLeader.to_do.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {DBPosition.class}, version = TasksDatabase.VERSION, exportSchema = false)
public abstract class TasksDatabase extends RoomDatabase {
    public static final String DB_NAME = "TasksDB";
    public static final int VERSION = 1;
    private static TasksDatabase instance;

    public abstract DBPositionDAO positionDAO();

    public static TasksDatabase getInstance(Context context) {
        if (instance == null)
            instance = Room.databaseBuilder(context, TasksDatabase.class, TasksDatabase.DB_NAME)
                    .allowMainThreadQueries().build();
        return instance;
    }
}
