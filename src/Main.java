import history.HistoryManager;
import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import taskengine.Managers;
import taskengine.TaskManager;

public class Main {
    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("Первая задача", "Описание первой задачи",
                taskManager.getTaskId(), TaskStatus.NEW);

        Task task2 = new Task("Вторая задача", "Описание второй задачи",
                taskManager.getTaskId(), TaskStatus.NEW);

        Epic epic1 = new Epic ("Первый эпик", "Описание первого эпика",
                taskManager.getTaskId(), TaskStatus.NEW);

        SubTask subTask1 = new SubTask("Первая подзадача", "Описание первой подзадачи",
                taskManager.getTaskId(), TaskStatus.IN_PROGRESS, epic1.getId());

        SubTask subTask2 = new SubTask("Вторая подзадача", "Описание второй подзадачи",
                taskManager.getTaskId(), TaskStatus.NEW, epic1.getId());



        taskManager.createTask(task1);
        taskManager.createEpic(epic1);
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);

        taskManager.getTaskById(1);
        taskManager.getEpicById(3);
        taskManager.getEpicById(3);
        taskManager.getTaskById(1);
        taskManager.getTaskById(1);
        taskManager.getSubTaskById(4);
        taskManager.getSubTaskById(5);
        taskManager.getTaskById(1);
        taskManager.getTaskById(1);
        taskManager.getSubTaskById(4);
        taskManager.getEpicById(3);

        System.out.println(taskManager.history());

    }
}
