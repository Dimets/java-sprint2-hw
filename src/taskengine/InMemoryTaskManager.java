package taskengine;

import history.HistoryManager;
import model.Epic;
import model.SubTask;
import model.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*Класс для управления задачами*/
public class InMemoryTaskManager implements TaskManager {
    private int taskId = 0;
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();

    private HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }


    @Override
    public int getTaskId() {
        taskId++;
        return taskId;
    }

    @Override
    public Map<Integer, Task> getTasks() {
        return tasks;
    }

    @Override
    public Map<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    @Override
    public Map<Integer, Epic> getEpics() {
        return epics;
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        subTasks.clear();
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);

        if (task != null) {
            historyManager.add(task);
        }

        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);

        if (epic != null) {
            historyManager.add(epic);
        }

        return epic;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = subTasks.get(id);

        if (subTask !=null) {
            historyManager.add(subTask);
        }

        return subTask;
    }

    @Override
    public void createTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void createEpic (Epic epic) {
        epics.put((epic.getId()), epic);
    }

    @Override
    public void createSubTask (SubTask subTask) {
        Epic epic = epics.get(subTask.getEpicId());
        subTasks.put(subTask.getId(), subTask);
        epic.addSubtask(subTask);
        epic.updateStatus();
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        //проверяем что статус не изменился
        if (epics.get(epic.getId()).getStatus() == epic.getStatus()) {
            epics.put(epic.getId(), epic);
        } else {
            System.out.println("Запрещено ручное изменение статуса эпика");
        }

    }

    @Override
    public void updateSubTask(SubTask subTask) {
        Epic epic = getEpicById(subTask.getEpicId());
        subTasks.put(subTask.getId(), subTask);
        epic.addSubtask(subTask);
        epic.updateStatus();
    }

    @Override
    public void deleteTaskById(int taskId) {
        tasks.remove(taskId);
    }

    @Override
    public void deleteEpicById(int epicId) {
        epics.remove(taskId);
    }

    @Override
    public void deleteSubTaskById(int subTaskId) {
        SubTask subTask = getSubTaskById(subTaskId);
        Epic epic = getEpicById(subTask.getEpicId());

        subTasks.remove(subTaskId);
        epic.deleteSubTaskById(subTaskId);
        epic.updateStatus();
    }

    @Override
    public List<SubTask> subTasksByEpic(Epic epic) {
        return epic.getSubTasks();
    }

    @Override
    public List<Task> history() {
        return historyManager.getHistory();
    }

}
