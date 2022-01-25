package a.todolist

import androidx.recyclerview.widget.RecyclerView
import a.todolist.database.TaskEntity
import android.view.ViewGroup
import android.view.LayoutInflater
import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

/* TaskAdapter создает ViewHolders, которые содержат описание и приоритет задачи,
   и связывает его с RecyclerView для эффективного отображения данных.
*/
class TaskAdapter(
/* Конструктор адаптера TaskAdapter, который инициализирует
     * @param context  текущий контекст
     * @param listener слушателя выбора элемента списка
*/
    private val context: Context,
    // переменная слушатель для обработки выбранного элемента списка
    private val itemClickListener: ItemClickListener
    ) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    // экземпляр класса List, который содержит список дел
    private var taskEntities: List<TaskEntity>? = null

    // создание экземпляра форматированной даты
    private val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())

    /* создание ViewHolders для каждого видимого элемента списка */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        // заполнение представления элемента списка
        val view = LayoutInflater.from(context)
            .inflate(R.layout.task_layout, parent, false)
        return TaskViewHolder(view)
    }

    /* метод связывает данные списка с представлением на экране */
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        // определение значений выбранного элемента
        val taskEntity = taskEntities!![position]
        val description = taskEntity.description
        val priority = taskEntity.priority
        val updatedAt = dateFormat.format(taskEntity.updatedAt)
        // вывод значений на экран
        holder.taskDescriptionView.text = description
        holder.updatedAtView.text = updatedAt
        // значение приоритета заполняет TextView
        val priorityString = "" + priority // converts int to String
        holder.priorityView.text = priorityString
    }

    /* Вспомогательный метод, который определяет цвет, соответствующий приоритету:
	1 = red, 2 = orange, 3 = yellow */
    private fun getPriorityColor(priority: Int): Int {
        var priorityColor = 0
        when (priority) {
            1 -> priorityColor = ContextCompat.getColor(context, R.color.materialRed)
            2 -> priorityColor = ContextCompat.getColor(context, R.color.materialOrange)
            3 -> priorityColor = ContextCompat.getColor(context, R.color.materialYellow)
            else -> {}
        }
        return priorityColor
    }

    /* метод возвращает количество элементов в списке */
    override fun getItemCount(): Int {
        return if (taskEntities == null) {
            0
        } else taskEntities!!.size
    }

    /* метод получает список *//* метод обновляет данные в списке, если были внесены изменения */
    var tasks: List<TaskEntity>?
        get() = taskEntities
        set(taskEntities) {
            this.taskEntities = taskEntities
            notifyDataSetChanged()
        }

    // реализация интерфейса при выборе элемента из списка
    interface ItemClickListener {
        fun onItemClickListener(itemId: Int)
    }

    /* Внутренний класс, который расширяет класс RecyclerView.ViewHolder и
       реализует интерфейс View.OnClickListener при выборе элемента списка
    */
    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        // переменные класса для текстовых полей представления
        var taskDescriptionView: TextView
        var updatedAtView: TextView
        var priorityView: TextView

        /* метод обработки выбора элемента и определения позиции выбранного элемента в БД */
        override fun onClick(view: View) {
            val elementId = taskEntities!![adapterPosition].id
            itemClickListener.onItemClickListener(elementId)
        }

        /* конструктор классаe TaskViewHolders */
        init {
            taskDescriptionView = itemView.findViewById(R.id.taskDescription)
            updatedAtView = itemView.findViewById(R.id.taskUpdatedAt)
            priorityView = itemView.findViewById(R.id.priorityTextView)
            itemView.setOnClickListener(this)
        }
    }

    companion object {
        // константа для определения формата даты
        private const val DATE_FORMAT = "dd/MM/yyy"
    }
}