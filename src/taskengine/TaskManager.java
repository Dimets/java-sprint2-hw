package taskengine;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*Класс для управления задачами*/
 public interface TaskManager {

    int getTaskId();

    HashMap<Integer, Task> getTasks();

    HashMap<Integer, SubTask> getSubTasks();

    HashMap<Integer, Epic> getEpics();

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubTasks();

     Task getTaskById(int id);

     Epic getEpicById(int id);

     SubTask getSubTaskById(int id);

     void createTask(Task task);

     void createEpic (Epic epic);

     void createSubTask (SubTask subTask);

     void updateTask(Task task);

     void updateEpic(Epic epic);

     void updateSubTask(SubTask subTask);

     void deleteTaskById(int taskId);

     void deleteEpicById(int epicId);

     void deleteSubTaskById(int subTaskId) ;

     ArrayList<SubTask> subTasksByEpic(Epic epic);

     List<Task> history();

}
