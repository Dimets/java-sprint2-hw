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

public class Main {
    public static void main(String[] args) {


        //TaskManager taskManager = Managers.getDefault();
        //TaskManager taskManager = Managers.getFileManager();
        TaskManager taskManager = FileBackedTasksManager.loadFromFile(new File("DbTaskManager.csv"));
/*
        Task task1 = new Task("Первая задача", "Описание первой задачи",
                taskManager.getTaskId(), TaskStatus.NEW);

        Task task2 = new Task("Вторая задача", "Описание второй задачи",
                taskManager.getTaskId(), TaskStatus.NEW);

        Task task3 = new Task("Третья задача", "Описание третьей задачи",
                taskManager.getTaskId(), TaskStatus.NEW);


        Epic epic1 = new Epic ("Первый эпик", "Описание первого эпика",
                taskManager.getTaskId(), TaskStatus.NEW);

        Epic epic2 = new Epic ("Второй эпик", "Описание второго эпика",
                taskManager.getTaskId(), TaskStatus.NEW);

        SubTask subTask1 = new SubTask("Первая подзадача", "Описание первой подзадачи",
                taskManager.getTaskId(), TaskStatus.IN_PROGRESS, epic1.getId());

        SubTask subTask2 = new SubTask("Вторая подзадача", "Описание второй подзадачи",
                taskManager.getTaskId(), TaskStatus.NEW, epic1.getId());

        SubTask subTask3 = new SubTask("Третья подзадача", "Описание третьей подзадачи",
                taskManager.getTaskId(), TaskStatus.NEW, epic2.getId());



        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        taskManager.createSubTask(subTask3);


        taskManager.getTaskById(task1.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getEpicById(epic2.getId());
        //taskManager.getTaskById(task1.getId());
        //taskManager.getTaskById(task2.getId());
        taskManager.getSubTaskById(subTask1.getId());
        taskManager.getSubTaskById(subTask2.getId());
        //taskManager.getTaskById(task3.getId());
        //taskManager.getTaskById(task2.getId());
        taskManager.getSubTaskById(subTask3.getId());
        taskManager.getEpicById(epic2.getId());
*/
        /*
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubTasks());
        */

        System.out.println(taskManager.history().size());




    }
}
