package eLeader.to_do;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DBPositionDAO {
    @Insert
    void insert(DBPosition position);

    @Update
    void update(DBPosition position);

    @Query("SELECT * FROM Tasks")
    List<DBPosition> getTaskList();

    @Query("SELECT COUNT(*) FROM Tasks")
    int size();

    @Delete
    void delete(DBPosition position);

    @Query("SELECT EXISTS(SELECT * FROM Tasks WHERE _id =:id)")
    boolean checkIfTaskAdded(int id);
}
