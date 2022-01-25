package a.todolist

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import a.todolist.database.AppDatabase
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.content.Intent

class MainActivity : AppCompatActivity(), TaskAdapter.ItemClickListener {
    lateinit var recyclerView: RecyclerView
    private var adapter: a.todolist.TaskAdapter? = null
    private var db: AppDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Связать RecyclerView с соответствующим представлением
        recyclerView = findViewById(R.id.recyclerViewTasks)
        // Определить Менеджер макетов для RecyclerView
        recyclerView.setLayoutManager(LinearLayoutManager(this))
        // Инициализировать адаптер и связать его с RecyclerView
        adapter = a.todolist.TaskAdapter(this, this)
        recyclerView.setAdapter(adapter)
        // плавающая кнопка
        val fabButton = findViewById<FloatingActionButton>(R.id.fab)
        // обработка нажатия на плавающую кнопку
        fabButton.setOnClickListener { // Создание нового интента для запуска AddTaskActivity
            val addTaskIntent = Intent(this@MainActivity, AddTaskActivity::class.java)
            startActivity(addTaskIntent)
        }
        db = AppDatabase.getInstance(applicationContext)
    }

    override fun onResume() {
        super.onResume()
        retrieveTasks()
    }

    // повторный запрос данных при любых изменениях в БД
    private fun retrieveTasks() {
        a.todolist.AppExecutors.instance?.diskIO()?.execute(Runnable {
            val tasks = db!!.taskDao()!!.loadAllTasks()
            runOnUiThread { adapter?.tasks }
        })
    }

    override fun onItemClickListener(itemId: Int) {
        // Запуск AddTaskActivity с параметром itemId при выборе элемента из списка:
        val intent = Intent(this@MainActivity, AddTaskActivity::class.java)
        intent.putExtra(AddTaskActivity.EXTRA_TASK_ID, itemId)
        startActivity(intent)
    }
}