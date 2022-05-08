import history.HistoryManager;
import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import taskengine.FileBackedTasksManager;
import taskengine.Managers;
import taskengine.TaskManager;

import java.io.File;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {


        //TaskManager taskManager = Managers.getDefault();
        //TaskManager taskManager = Managers.getFileManager();
        TaskManager taskManager = FileBackedTasksManager.loadFromFile(new File("DbTaskManager.csv"));




        Task task1 = new Task("Первая задача", "Описание первой задачи",
                taskManager.getTaskId(), TaskStatus.NEW, LocalDateTime.now().minusDays(2),Duration.ofDays(10));

        Task task2 = new Task("Вторая задача", "Описание второй задачи",
                taskManager.getTaskId(), TaskStatus.NEW, LocalDateTime.now().plusDays(20),Duration.ofDays(5));

        Task task3 = new Task("Третья задача", "Описание третьей задачи",
                taskManager.getTaskId(), TaskStatus.NEW);


        Epic epic1 = new Epic ("Первый эпик", "Описание первого эпика",
                taskManager.getTaskId(), TaskStatus.NEW);

        Epic epic2 = new Epic ("Второй эпик", "Описание второго эпика",
                taskManager.getTaskId(), TaskStatus.NEW);

        SubTask subTask1 = new SubTask("Первая подзадача", "Описание первой подзадачи",
                taskManager.getTaskId(), TaskStatus.NEW, epic1.getId(), LocalDateTime.now(), Duration.ofHours(8));

        SubTask subTask2 = new SubTask("Вторая подзадача", "Описание второй подзадачи",
                taskManager.getTaskId(), TaskStatus.DONE, epic1.getId(), LocalDateTime.now().plusDays(10), Duration.ofDays(1));

        SubTask subTask3 = new SubTask("Третья подзадача", "Описание третьей подзадачи",
                taskManager.getTaskId(), TaskStatus.NEW, epic2.getId());

        SubTask subTask4 = new SubTask("Четвертая подзадача", "Описание четвертой подзадачи",
                taskManager.getTaskId(), TaskStatus.NEW, epic1.getId(), LocalDateTime.now().minusDays(30), Duration.ofDays(1));



        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        taskManager.createSubTask(subTask3);
        taskManager.createSubTask(subTask4);

        //taskManager.getTaskById(task1.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getEpicById(epic2.getId());
        //taskManager.getTaskById(task1.getId());
        //taskManager.getTaskById(task2.getId());
        taskManager.getSubTaskById(subTask1.getId());
        taskManager.getSubTaskById(subTask2.getId());
        //taskManager.getTaskById(task3.getId());
        //taskManager.getTaskById(task2.getId());
        //taskManager.getSubTaskById(subTask3.getId());
        //taskManager.getEpicById(epic2.getId());


        Task task = new Task("Первая задача", "Описание первой задачи",
                taskManager.getTaskId(), TaskStatus.NEW, LocalDateTime.now().plusDays(15), Duration.ofHours(8));
        taskManager.createTask(task);

        Task updatedTask = new Task("Первая задача", "Описание первой задачи",
                task.getId(), TaskStatus.IN_PROGRESS, LocalDateTime.now().plusDays(20), Duration.ofHours(8));

        taskManager.updateTask(updatedTask);

        System.out.println("tasks:");
        System.out.println(taskManager.getTasks());
        System.out.println("epics:");
        System.out.println(taskManager.getEpics());
        System.out.println("subtasks:");
        System.out.println(taskManager.getSubTasks());

        System.out.println("history:");
        System.out.println(taskManager.history());

        System.out.println("prior tasks:");
        System.out.println(taskManager.getPrioritizedTasks());

    }
}
