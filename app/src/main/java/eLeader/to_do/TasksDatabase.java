package eLeader.to_do;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {DBPosition.class}, version = TasksDatabase.VERSION, exportSchema = false)
public abstract class TasksDatabase extends RoomDatabase {
    public static final String DB_NAME = "TasksDB";
    public static final int VERSION = 1;

    public abstract DBPositionDAO positionDAO();
}
