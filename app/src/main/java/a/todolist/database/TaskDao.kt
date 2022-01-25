package a.todolist.database

import androidx.room.*

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY priority")
    fun loadAllTasks(): List<TaskEntity?>?

    @Insert
    fun insertTask(taskEntity: TaskEntity?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateTask(taskEntity: TaskEntity?)

    @Delete
    fun deleteTask(taskEntity: TaskEntity?)

    // Query метод loadTaskById по id выбирает TaskEntity Object
    @Query("SELECT * FROM tasks WHERE id = :id")
    fun loadTaskById(id: Int): TaskEntity?
}