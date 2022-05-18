package test;

import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskengine.*;
import utils.Managers;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @BeforeEach
    public void setUp() {
        taskManager = (FileBackedTasksManager) Managers.getFileBackedTasksManager("tests.csv");
    }

    @Test
    void shouldSave() {
        LocalDateTime defaultStartTime = LocalDateTime.now();

        Task task1 = new Task("Первая задача", "Описание первой задачи",
                taskManager.getTaskId(), TaskStatus.NEW, defaultStartTime.minusDays(2), Duration.ofDays(10));
        taskManager.createTask(task1);

        Epic epic1 = new Epic("Первый эпик", "Описание первого эпика",
                taskManager.getTaskId(), TaskStatus.NEW);
        taskManager.createEpic(epic1);

        SubTask subTask1 = new SubTask("Первая подзадача", "Описание первой подзадачи",
                taskManager.getTaskId(), TaskStatus.NEW, epic1.getId(), defaultStartTime.now(), Duration.ofHours(8));
        taskManager.createSubTask(subTask1);

        SubTask subTask2 = new SubTask("Вторая подзадача", "Описание второй подзадачи",
                taskManager.getTaskId(), TaskStatus.IN_PROGRESS, epic1.getId(), defaultStartTime.now().plusDays(10),
                Duration.ofDays(1));
        taskManager.createSubTask(subTask2);

        taskManager.getEpicById(epic1.getId());
        taskManager.getTaskById(task1.getId());
        taskManager.getSubTaskById(subTask2.getId());

        try (FileReader reader = new FileReader(taskManager.getBackupFile(), StandardCharsets.UTF_8)) {
            String fileData = Files.readString(Path.of(taskManager.getBackupFile().getPath()), StandardCharsets.UTF_8);
            String[] fileLines = fileData.split("\n");

            Assertions.assertEquals("id,type,name,status,description,epic,starttime,duration", fileLines[0],
                    "Строка с заголовками записана  в файл некорректно");
            Assertions.assertEquals("1,TASK,Первая задача,NEW,Описание первой задачи,," +
                    defaultStartTime.minusDays(2) + ",PT240H,", fileLines[1], "Задача записана в файл некорректно");
            Assertions.assertEquals("2,EPIC,Первый эпик,IN_PROGRESS,Описание первого эпика,," +
                            subTask2.getStartTime() + ",PT24H,", fileLines[2],
                    "Эпик записан в файл некорректно");
            Assertions.assertEquals("4,SUBTASK,Вторая подзадача,IN_PROGRESS,Описание второй подзадачи,2," +
                    subTask2.getStartTime() + ",PT24H,", fileLines[3]);
            Assertions.assertEquals("", fileLines[4]);
            Assertions.assertEquals("2,1,4", fileLines[5]);

        } catch (IOException e) {
            throw new ManagerReadException("Error to read file");
        }

    }

    @Test
    void shouldLoadFromFile() {
        TaskManager taskManager = FileBackedTasksManager.loadFromFile(new File("tests.csv"));

        Assertions.assertEquals(1, taskManager.getTasks().size());
        Assertions.assertEquals(1, taskManager.getEpics().size());
        Assertions.assertEquals(1, taskManager.getSubTasks().size());
        Assertions.assertEquals(3, taskManager.history().size());
    }

}
