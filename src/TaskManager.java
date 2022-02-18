import java.util.ArrayList;
import java.util.HashMap;

/*Класс для управления задачами*/
public class TaskManager {
    private int taskId = 0;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();

    public int getTaskId() {
        taskId++;
        return taskId;
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
    }

    public void deleteAllSubTasks() {
        subTasks.clear();
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public SubTask getSubTaskById(int id) {
        return subTasks.get(id);
    }


    public void createTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void createEpic (Epic epic) {
        epics.put((epic.getId()), epic);
    }

    public void createSubTask (SubTask subTask) {
        Epic epic = getEpicById(subTask.getEpicId());
        subTasks.put(subTask.getId(), subTask);
        epic.addSubtask(subTask);
        epic.updateStatus();
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        //проверяем что статус не изменился
        if (epics.get(epic.getId()).getStatus() == epic.getStatus()) {
            epics.put(epic.getId(), epic);
        } else {
            System.out.println("Запрещено ручное изменение статуса эпика");
        }

    }

    public void updateSubTask(SubTask subTask) {
        Epic epic = getEpicById(subTask.getEpicId());
        subTasks.put(subTask.getId(), subTask);
        epic.addSubtask(subTask);
        epic.updateStatus();
    }

    public void deleteTaskById(int taskId) {
        tasks.remove(taskId);
    }

    public void deleteEpicById(int epicId) {
        epics.remove(taskId);
    }

    public void deleteSubTaskById(int subTaskId) {
        SubTask subTask = getSubTaskById(subTaskId);
        Epic epic = getEpicById(subTask.getEpicId());

        subTasks.remove(subTaskId);
        epic.deleteSubTaskById(subTaskId);
        epic.updateStatus();
    }

    public ArrayList<SubTask> subTasksByEpic(Epic epic) {
        return epic.getSubTasks();
    }
}
