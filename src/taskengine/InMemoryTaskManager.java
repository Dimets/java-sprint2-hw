package taskengine;

import history.HistoryManager;
import model.Epic;
import model.SubTask;
import model.Task;

import java.util.*;

/*Класс для управления задачами*/
public class InMemoryTaskManager implements TaskManager {
    private int taskId = 0;
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private HistoryManager historyManager;
    private TreeSet<Task> prioritizedTasks = new TreeSet<>();



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

        if (subTask != null) {
            historyManager.add(subTask);
        }

        return subTask;
    }

    @Override
    public void createTask(Task task) {
        if (checkTaskPriority(task)) {
            tasks.put(task.getId(), task);
            prioritizedTasks.add(task);
        } else {
            System.out.println("Нельзя добавить задачу " + task + " из-за пересечения по датам");
        }
    }

    @Override
    public void createEpic(Epic epic) {
        epics.put((epic.getId()), epic);
    }

    @Override
    public void createSubTask(SubTask subTask) {
        Epic epic = epics.get(subTask.getEpicId());

        if (epic == null) {
            throw new EpicForSubTaskNotExistException();
        } else {
            if (checkTaskPriority(subTask)) {
                subTasks.put(subTask.getId(), subTask);
                prioritizedTasks.add(subTask);
                epic.addSubtask(subTask);
                epic.updateEpic();
            } else {
                System.out.println("Нельзя добавить подзадачу " + subTask + " из-за пересечения по датам");
            }
        }
    }

    @Override
    public void updateTask(Task task) {

        if (tasks.get(task.getId()).getEndTime().equals(task.getEndTime())) {
            prioritizedTasks.remove(tasks.get(task.getId()));
            tasks.put(task.getId(), task);
            prioritizedTasks.add(task);
        } else if (checkTaskPriority(task)) {
            prioritizedTasks.remove(tasks.get(task.getId()));
            tasks.put(task.getId(), task);
            prioritizedTasks.add(task);
        } else {
            System.out.println("Нельзя изменить задачу " + task + " из-за пересечения по датам");
        }
    }

    @Override
    public void updateEpic(Epic epic) {

        //проверяем что статус не изменился
        if (epics.get(epic.getId()).getStatus() == epic.getStatus()) {
            epics.put(epic.getId(), epic);
        } else {
            throw new ManualChangeEpicStatusException();
        }

    }

    @Override
    public void updateSubTask(SubTask subTask) {
            Epic epic = getEpicById(subTask.getEpicId());
            if ((subTasks.get(subTask.getId()).getEndTime() == null && subTask.getEndTime() == null) ||
                    subTasks.get(subTask.getId()).getEndTime().equals(subTask.getEndTime())) {
                prioritizedTasks.remove(subTasks.get(subTask.getId()));
                subTasks.put(subTask.getId(), subTask);
                epic.addSubtask(subTask);
                prioritizedTasks.add(subTask);
                epic.updateEpic();
            } else if (checkTaskPriority(subTask)) {
                prioritizedTasks.remove(subTasks.get(subTask.getId()));
                subTasks.put(subTask.getId(), subTask);
                epic.addSubtask(subTask);
                prioritizedTasks.add(subTask);
                epic.updateEpic();
            } else {
                System.out.println("Нельзя изменить подзадачу " + subTask + " из-за пересечения по датам");
            }

    }

    @Override
    public void deleteTaskById(int taskId) {
        //prioritizedTasks.remove(tasks.get(taskId));
        tasks.remove(taskId);
        historyManager.remove(taskId);
    }

    @Override
    public void deleteEpicById(int epicId) {
        epics.remove(taskId);
        historyManager.remove(epicId);
    }

    @Override
    public void deleteSubTaskById(int subTaskId) {
        SubTask subTask = getSubTaskById(subTaskId);
        Epic epic = getEpicById(subTask.getEpicId());

        //prioritizedTasks.remove(subTask);
        subTasks.remove(subTaskId);
        epic.deleteSubTaskById(subTaskId);
        historyManager.remove(subTaskId);
        epic.updateEpic();
    }

    @Override
    public List<Task> history() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<Task>(prioritizedTasks);
    }

    @Override
    public boolean checkTaskPriority(Task task) {

        if (task.getEndTime() != null) {
            Task floorTask = prioritizedTasks.floor(task);
            Task higherTask = prioritizedTasks.higher(task);

            if (floorTask == null && higherTask == null) {
                return true;
            }

            if (floorTask == null) {
                if (higherTask.getStartTime() == null) {
                    return true;
                } else if (task.getEndTime().isBefore(higherTask.getStartTime())) {
                    return true;
                }
            } else if (higherTask == null) {
                if (floorTask.getEndTime() == null) {
                    return true;
                } else if (task.getStartTime().isAfter(floorTask.getEndTime())) {
                    return true;
                }
            }

            if (floorTask.getEndTime() != null && higherTask.getStartTime() != null) {
                return task.getStartTime().isAfter(floorTask.getEndTime()) &&
                        task.getEndTime().isBefore(higherTask.getStartTime());
            } else if (floorTask.getEndTime() == null) {
                return task.getEndTime().isBefore(higherTask.getStartTime());
            } else if (higherTask.getEndTime() == null) {
                return task.getStartTime().isAfter(floorTask.getEndTime());
            }

        } else {
            return true;
        }
        return false;
    }
}
