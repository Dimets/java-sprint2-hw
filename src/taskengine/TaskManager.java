package taskengine;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.List;
import java.util.Map;
import java.util.Set;

/*Класс для управления задачами*/
public interface TaskManager {

    int getTaskId();

    Map<Integer, Task> getTasks();

    Map<Integer, SubTask> getSubTasks();

    Map<Integer, Epic> getEpics();

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubTasks();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    SubTask getSubTaskById(int id);

    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubTask(SubTask subTask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(SubTask subTask);

    void deleteTaskById(int taskId);

    void deleteEpicById(int epicId);

    void deleteSubTaskById(int subTaskId);

    List<Task> history();

    Set<Task> getPrioritizedTasks();

    boolean checkTaskPriority(Task task);

}
