package db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface StateDao {

    @Query("Select * from State WHERE name = :stateName ")
    List<State> getInitialsByFullNameOfState(String stateName);

    @Insert
    void addState(State state);

    @Query("SELECT * FROM state")
    List<State> getStates();


}
