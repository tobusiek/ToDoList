package eLeader.to_do.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DBPositionDAO {
    @Insert
    void insert(DBPosition position);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(DBPosition position);

//    @Query("UPDATE Tasks SET task_name =:taskName, task_date =:taskDate, task_cat =:taskCat WHERE _id =:id")
//    void update(int id, String taskName, String taskDate, String taskCat);

    @Query("SELECT * FROM Tasks")
    List<DBPosition> getTaskList();

    @Query("SELECT COUNT(*) FROM Tasks")
    int size();

    @Delete
    void delete(DBPosition position);

    @Query("SELECT EXISTS(SELECT * FROM Tasks WHERE task_name =:taskName AND task_date =:taskDate AND task_cat =:taskCat)")
    boolean checkIfTaskAdded(String taskName, String taskDate, String taskCat);

    @Query("SELECT * FROM Tasks WHERE _id =:id")
    DBPosition getPositionById(int id);

    @Query("SELECT EXISTS(SELECT * FROM Tasks WHERE _id =:id AND task_name =:taskName" +
            " AND task_date =:taskDate AND task_cat =:taskCat)")
    boolean checkIfTaskEdited(int id, String taskName, String taskDate, String taskCat);
}
