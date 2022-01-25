package a.todolist

import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.widget.RadioGroup
import a.todolist.database.AppDatabase
import android.os.Bundle
import a.todolist.database.TaskEntity
import android.view.View
import android.widget.Button
import java.util.*

class AddTaskActivity : AppCompatActivity() {
    // Fields for views
    var editText: EditText? = null
    var radioGroup: RadioGroup? = null
    lateinit var button: Button
    var id = DEFAULT_TASK_ID

    // Member variable for the Database
    private var db: AppDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        editText = findViewById(R.id.editTextTaskDescription)
        radioGroup = findViewById(R.id.radioGroup)
        button = findViewById(R.id.saveButton)
        button.setOnClickListener(View.OnClickListener { onSaveButtonClicked() })
        db = AppDatabase.getInstance(applicationContext)

/*        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TASK_ID)) {
            taskId = savedInstanceState.getInt(INSTANCE_TASK_ID, DEFAULT_TASK_ID);
        }
*/
        val intent = intent
        if (intent != null && intent.hasExtra(EXTRA_TASK_ID)) {
            button.setText(R.string.update_button)
            // TODO БД U: запрос задачи по id
            if (id == DEFAULT_TASK_ID) {
                id = intent.getIntExtra(EXTRA_TASK_ID, DEFAULT_TASK_ID)
                // Получение задачи по идентификатору:
                AppExecutors.instance?.diskIO()?.execute(Runnable {
                    val task = db!!.taskDao()!!.loadTaskById(taskId)
                    runOnUiThread { populateUI(task) }
                })
            }
        }
    }
    /*  @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_TASK_ID, taskId);
        super.onSaveInstanceState(outState);
    }
*/
    /**
     * populateUI would be called to populate the UI when in update mode
     *
     * @param task the taskEntity to populate the UI
     */
    private fun populateUI(task: TaskEntity?) {
        // COMPLETED (7) return if the task is null
        if (task == null) {
            return
        }
        editText!!.setText(task.description)
        setPriorityInViews(task.priority)
    }

    /**
     * getPriority is called whenever the selected priority needs to be retrieved
     */
    val priorityFromViews: Int
        get() {
            var priority = 1
            val checkedId = (findViewById<View>(R.id.radioGroup) as RadioGroup).checkedRadioButtonId
            when (checkedId) {
                R.id.radButton1 -> priority = PRIORITY_HIGH
                R.id.radButton2 -> priority = PRIORITY_MEDIUM
                R.id.radButton3 -> priority = PRIORITY_LOW
            }
            return priority
        }

    /**
     * setPriority is called when we receive a task from MainActivity
     *
     * @param priority the priority value
     */
    fun setPriorityInViews(priority: Int) {
        when (priority) {
            PRIORITY_HIGH -> (findViewById<View>(R.id.radioGroup) as RadioGroup).check(R.id.radButton1)
            PRIORITY_MEDIUM -> (findViewById<View>(R.id.radioGroup) as RadioGroup).check(R.id.radButton2)
            PRIORITY_LOW -> (findViewById<View>(R.id.radioGroup) as RadioGroup).check(R.id.radButton3)
        }
    }

    /**
     * onSaveButtonClicked is called when the "save" button is clicked.
     * It retrieves user input and inserts that new task data into the underlying database.
     */
    fun onSaveButtonClicked() {
        val description = editText!!.text.toString()
        val priority = priorityFromViews
        val date = Date()
        val task = TaskEntity(description, priority, date)
        // TODO БД S: добавление задачи с новым id или сохранение изменений с существующим id в БД
        AppExecutors.instance?.diskIO()?.execute(Runnable {
            if (taskId == DEFAULT_TASK_ID) {
                // insert new task
                db!!.taskDao()!!.insertTask(task)
            } else {
                //update task
                task.id = taskId
                db!!.taskDao()!!.updateTask(task)
            }
            finish()
        })
    }

    companion object {
        // Extra for the task ID to be received in the intent
        const val EXTRA_TASK_ID = "extraTaskId"

        // Extra for the task ID to be received after rotation
        const val INSTANCE_TASK_ID = "instanceTaskId"

        // Constants for priority
        const val PRIORITY_HIGH = 1
        const val PRIORITY_MEDIUM = 2
        const val PRIORITY_LOW = 3

        // Constant for default task id to be used when not in update mode
        private const val DEFAULT_TASK_ID = -1

        // Constant for logging
        private val TAG = AddTaskActivity::class.java.simpleName
    }
}