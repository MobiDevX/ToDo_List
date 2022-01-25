package a.todolist.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [a.todolist.database.TaskEntity::class], version = 1, exportSchema = false)
@TypeConverters(
    DateConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): a.todolist.database.TaskDao?

    companion object {
        private val LOCK = Any()
        private const val DATABASE_NAME = "todolist"
        private var sInstance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase? {
            if (sInstance == null) {
                synchronized(LOCK) {
                    // Создание нового экземпляра БД
                    sInstance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, DATABASE_NAME
                    ).build()
                        // TODO: метод allowMainThreadQueries позволяет обрабатывать данных БД в основном потоке (удалить)
                        //                        .allowMainThreadQueries()
                }
            }
            // Возвращает существующий экземпляр БД
            return sInstance
        }
    }
}