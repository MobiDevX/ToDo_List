package a.todolist.database

import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import java.util.*

@Entity(tableName = "tasks")
class TaskEntity {
    // Аннотация @PrimaryKey указывает ключ таблицы
    @PrimaryKey(autoGenerate = true)
    var id = 0
    var description: String
    var priority: Int

    @ColumnInfo(name = "updated_at")
    var updatedAt: Date

    // @Ignore используется конструктор без параметров
    @Ignore
    constructor(description: String, priority: Int, updatedAt: Date) {
        this.description = description
        this.priority = priority
        this.updatedAt = updatedAt
    }

    constructor(id: Int, description: String, priority: Int, updatedAt: Date) {
        this.id = id
        this.description = description
        this.priority = priority
        this.updatedAt = updatedAt
    }
}