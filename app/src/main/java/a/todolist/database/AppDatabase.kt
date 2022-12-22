package a.todolist.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [TaskEntity::class], version = 1, exportSchema = false)
@TypeConverters(
    DateConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao?

    companion object {

        private const val DATABASE_NAME = "todolist"
        private var sInstance: AppDatabase? = null

        private val LOCK = Any()
        fun getInstance(context: Context): AppDatabase? {
            if (sInstance == null) {
                synchronized(LOCK) {
                // Создание нового экземпляра БД
                sInstance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, DATABASE_NAME
                )
                    // TODO: метод allowMainThreadQueries позволяет обрабатывать данных БД в основном потоке (удалить)
                    //.allowMainThreadQueries()
                    .build()
                }
            }
            // Возвращает существующий экземпляр БД
            return sInstance
        }
    }
}