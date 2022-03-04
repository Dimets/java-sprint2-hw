import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import taskengine.InMemoryTaskManager;
import taskengine.Managers;
import taskengine.TaskManager;

public class Main {
    public static void main(String[] args) {
        TaskManager inMemoryTaskManager = Managers.getDefault();

        Task task1 = new Task("Первая задача", "Описание первой задачи",
                inMemoryTaskManager.getTaskId(), TaskStatus.NEW);

        Task task2 = new Task("Вторая задача", "Описание второй задачи",
                inMemoryTaskManager.getTaskId(), TaskStatus.NEW);

        Epic epic1 = new Epic ("Первый эпик", "Описание первого эпика",
                inMemoryTaskManager.getTaskId(), TaskStatus.NEW);

        SubTask subTask1 = new SubTask("Первая подзадача", "Описание первой подзадачи",
                inMemoryTaskManager.getTaskId(), TaskStatus.IN_PROGRESS, epic1.getId());

        SubTask subTask2 = new SubTask("Вторая подзадача", "Описание второй подзадачи",
                inMemoryTaskManager.getTaskId(), TaskStatus.NEW, epic1.getId());



        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createEpic(epic1);
        inMemoryTaskManager.createSubTask(subTask1);
        inMemoryTaskManager.createSubTask(subTask2);

        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getEpicById(3);
        inMemoryTaskManager.getEpicById(3);
        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getSubTaskById(4);
        inMemoryTaskManager.getSubTaskById(5);
        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getSubTaskById(4);
        inMemoryTaskManager.getEpicById(3);

        System.out.println(inMemoryTaskManager.history());

    }
}
